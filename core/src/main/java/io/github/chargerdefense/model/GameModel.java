package io.github.chargerdefense.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.github.chargerdefense.data.game.SavedGameState;
import io.github.chargerdefense.data.game.SavedUnit;
import io.github.chargerdefense.model.enemy.Enemy;
import io.github.chargerdefense.model.map.GameMap;
import io.github.chargerdefense.model.unit.upgrade.Upgrade;
import io.github.chargerdefense.model.unit.Unit;
import io.github.chargerdefense.model.unit.basic.BasicUnit;

/**
 * Manages the main game loop, game state, and core components.
 */
public class GameModel implements PlayerObserver {
    /** The player in the game. */
    private Player player;
    /** The game map. */
    private GameMap map;
    /** The manager for the rounds . */
    private RoundManager roundManager;
    /** The number of lives the player has remaining. */
    private int lives;
    /** A flag indicating whether the game is over. */
    private boolean isGameOver;
    /** A list of all active projectiles in the game. */
    private List<Projectile> activeProjectiles;
    /** List of observers for game state changes. */
    private List<GameObserver> observers;
    /** The currently selected unit for actions. */
    private Unit selectedUnit;

    /**
     * Constructs a new Game instance.
     *
     * @param initialLives    The starting number of lives for the player.
     * @param initialCurrency The starting currency for the player.
     * @param map             The game map to be used.
     * @param rounds          A list of predefined rounds for the game.
     */
    public GameModel(int initialLives, int initialCurrency, GameMap map, List<Round> rounds) {
        this.player = new Player(initialCurrency);
        this.player.addObserver(this);
        this.map = map;
        this.lives = initialLives;
        this.roundManager = new RoundManager(rounds, this);
        this.isGameOver = false;
        this.activeProjectiles = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    /**
     * Constructs a new Game instance from a saved game state.
     * 
     * @param savedGame The saved game state to load.
     * @param map       The game map to be used.
     * @param rounds    A list of predefined rounds for the game.
     */
    public GameModel(SavedGameState savedGame, GameMap map, List<Round> rounds) {
        this.player = new Player(savedGame.currency);
        this.player.addObserver(this);
        this.player.setScore(savedGame.score);
        this.player.setEnemiesDefeated(savedGame.enemiesDefeated);
        this.player.setUnitsPurchased(savedGame.unitsPurchased);

        this.map = map;
        this.lives = savedGame.lives;

        for (SavedUnit savedUnit : savedGame.placedUnits) {
            Unit unit = createUnitFromSerializedType(savedUnit.type);
            if (unit != null) {
                this.map.placeUnit(unit, savedUnit.x, savedUnit.y);
            }
        }

        this.roundManager = new RoundManager(rounds, this);
        this.roundManager.setCurrentRoundIndex(savedGame.currentRoundIndex);

        this.isGameOver = false;
        this.activeProjectiles = new ArrayList<>();
        this.observers = new ArrayList<>();
    }

    /**
     * Creates a unit instance from the serialized unit type name.
     * 
     * @param unitType The name/type of the unit
     * @return A new unit instance, or null if the type is invalid
     */
    private Unit createUnitFromSerializedType(String unitType) {
        switch (unitType) {
            case "BasicUnit":
                return new BasicUnit();
            default:
                return null;
        }
    }

    /**
     * Adds an observer to receive game state notifications.
     *
     * @param observer The observer to add
     */
    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer from the list of observers.
     *
     * @param observer The observer to remove
     */
    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all observers that currency has changed.
     */
    private void notifyCurrencyChanged() {
        int currency = player.getCurrency();
        for (GameObserver observer : observers) {
            observer.onCurrencyChanged(currency);
        }
    }

    /**
     * Notifies all observers that lives have changed.
     */
    private void notifyLivesChanged() {
        for (GameObserver observer : observers) {
            observer.onLivesChanged(lives);
        }
    }

    /**
     * Notifies all observers that the round has changed.
     */
    private void notifyRoundChanged() {
        int currentRound = roundManager.getCurrentRoundNumber();
        int totalRounds = roundManager.getTotalRounds();
        for (GameObserver observer : observers) {
            observer.onRoundChanged(currentRound, totalRounds);
        }
    }

    /**
     * Notifies all observers that the round state has changed.
     */
    private void notifyRoundStateChanged() {
        boolean roundInProgress = roundManager.isRoundInProgress();
        boolean allRoundsComplete = roundManager.areAllRoundsCompleted();
        for (GameObserver observer : observers) {
            observer.onRoundStateChanged(roundInProgress, allRoundsComplete);
        }
    }

    /**
     * Notifies all observers that the game is over.
     *
     * @param victory true if the player won, false if they lost
     */
    private void notifyGameOver(boolean victory) {
        for (GameObserver observer : observers) {
            observer.onGameOver(victory);
        }
    }

    /**
     * Notifies all observers that a unit was upgraded.
     */
    private void notifyUnitUpgraded() {
        for (GameObserver observer : observers) {
            observer.onUnitUpgraded();
        }
    }

    /**
     * Updates the game state.
     * This method is called on each frame of the game loop.
     *
     * @param deltaTime The time elapsed since the last update (in seconds)
     */
    public void update(float deltaTime) {
        if (isGameOver)
            return;

        boolean wasRoundInProgress = roundManager.isRoundInProgress();

        roundManager.update(deltaTime);

        // notify if the round state changed
        boolean isRoundInProgress = roundManager.isRoundInProgress();
        if (wasRoundInProgress != isRoundInProgress) {
            notifyRoundStateChanged();
        }

        List<Projectile> newProjectiles = map.update(deltaTime, roundManager.getActiveEnemies());
        activeProjectiles.addAll(newProjectiles);

        List<Enemy> activeEnemies = roundManager.getActiveEnemies();
        for (Enemy enemy : activeEnemies) {
            enemy.checkProjectileCollisions(activeProjectiles);
        }

        Iterator<Projectile> iterator = activeProjectiles.iterator();
        while (iterator.hasNext()) {
            Projectile projectile = iterator.next();
            if (projectile.update(deltaTime)) {
                iterator.remove();
            }
        }

        checkGameOver();
    }

    /**
     * Called by an Enemy when it reaches the end of the path.
     * Decrements player lives and notifies the round manager.
     * 
     * @param enemy The enemy that reached the end
     */
    public void enemyReachedEnd(Enemy enemy) {
        this.lives -= 1;
        notifyLivesChanged();
        roundManager.onEnemyReachedEnd(enemy);
    }

    /**
     * Starts the next round of enemies.
     * Called when the player chooses to begin the next wave.
     */
    public void startNextRound() {
        roundManager.startNextRound();
        notifyRoundChanged();
        notifyRoundStateChanged();
    }

    /**
     * Checks for the game's win or loss conditions.
     * When the player has no more lives, the game is lost. If the player completes
     * all rounds, and they still have lives, the game is won. If both of these
     * conditions are not true, then the game is still in-progress.
     */
    private void checkGameOver() {
        if (lives <= 0) {
            isGameOver = true;
            notifyGameOver(false);
        }

        if (roundManager.areAllRoundsCompleted() && !isGameOver) {
            isGameOver = true;
            notifyGameOver(true);
        }
    }

    /**
     * Attempts to purchase a unit and place it on the map.
     * This method handles both the financial transaction and unit placement.
     *
     * @param unit The unit to be purchased
     * @param x    The x-coordinate for placement
     * @param y    The y-coordinate for placement
     * @return true if the purchase and placement were successful, false otherwise
     */
    public boolean purchaseUnit(Unit unit, int x, int y) {
        if (player.canAfford(unit.getCost())) {
            if (map.placeUnit(unit, x, y)) {
                player.spendCurrency(unit.getCost());
                player.incrementUnitsPurchased();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * Awards currency to the player, typically when an enemy is defeated.
     *
     * @param amount The amount of currency to award
     */
    public void awardCurrency(int amount) {
        player.addCurrency(amount);
    }

    /**
     * Awards score points to the player.
     *
     * @param points The points to award
     */
    public void awardScore(int points) {
        player.addScore(points);
    }

    /**
     * Records that an enemy was defeated by the player.
     * Updates statistics and may award currency/score.
     *
     * @param enemy The enemy that was defeated
     */
    public void onEnemyDefeated(Enemy enemy) {
        player.incrementEnemiesDefeated();
        awardCurrency(enemy.getCurrencyValue());
        awardScore(enemy.getCurrencyValue() * 10);

        roundManager.onEnemyDefeated(enemy);
    }

    /**
     * Gets the player instance.
     *
     * @return The player object containing game state data
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the game map instance.
     *
     * @return The game map containing unit placements and paths
     */
    public GameMap getMap() {
        return map;
    }

    /**
     * Gets the current number of lives remaining.
     *
     * @return The number of lives remaining
     */
    public int getLives() {
        return lives;
    }

    /**
     * Sets the number of lives.
     * 
     * Observers are notified of the change.
     *
     * @param lives The new number of lives
     */
    public void setLives(int lives) {
        this.lives = Math.max(0, lives);
        notifyLivesChanged();
    }

    /**
     * Checks if the game is over.
     *
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() {
        return isGameOver;
    }

    /**
     * Gets the list of all active projectiles in the game
     *
     * @return A list of active projectiles
     */
    public List<Projectile> getActiveProjectiles() {
        return activeProjectiles;
    }

    /**
     * Gets the round manager.
     *
     * @return The round manager
     */
    public RoundManager getRoundManager() {
        return roundManager;
    }

    /**
     * Gets the currently selected unit.
     * 
     * The selected unit shows available upgrades and targeting options
     * 
     * @return The selected unit, or null if none is selected
     */
    public Unit getSelectedUnit() {
        return selectedUnit;
    }

    /**
     * Sets the currently selected unit.
     * 
     * The selected unit shows available upgrades and targeting options
     * 
     * @param unit The unit to select, or null to clear selection
     */
    public void setSelectedUnit(Unit unit) {
        if (this.selectedUnit != null) {
            this.selectedUnit.setSelected(false);
        }
        this.selectedUnit = unit;
        if (this.selectedUnit != null) {
            this.selectedUnit.setSelected(true);
        }
    }

    /** Upgrades the currently selected unit, if applicable. */
    public void upgradeSelectedUnit() {
        if (selectedUnit != null) {
            Upgrade nextUpgrade = selectedUnit.getUpgradePath()
                    .getNextUpgrade();
            if (nextUpgrade != null && player.canAfford(nextUpgrade.getCost())) {
                player.spendCurrency(nextUpgrade.getCost());
                selectedUnit.applyUpgrade();
                notifyUnitUpgraded();
            }
        }
    }

    @Override
    public void onCurrencyChanged(int newCurrency) {
        notifyCurrencyChanged();
    }
}