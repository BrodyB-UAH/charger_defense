package io.github.chargerdefense.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.github.chargerdefense.model.enemy.Enemy;
import io.github.chargerdefense.model.map.GameMap;
import io.github.chargerdefense.model.unit.Unit;

/**
 * Manages the main game loop, game state, and core components.
 */
public class GameModel {
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
        this.map = map;
        this.lives = initialLives;
        this.roundManager = new RoundManager(rounds, this);
        this.isGameOver = false;
        this.activeProjectiles = new ArrayList<>();
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

        roundManager.update(deltaTime);

        List<Projectile> newProjectiles = map.update(deltaTime, roundManager.getActiveEnemies());
        activeProjectiles.addAll(newProjectiles);

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
        roundManager.onEnemyReachedEnd(enemy);
    }

    /**
     * Starts the next round of enemies.
     * Called when the player chooses to begin the next wave.
     */
    public void startNextRound() {
        roundManager.startNextRound();
    }

    /**
     * Checks for the game's win or loss conditions.
     */
    private void checkGameOver() {
        if (lives <= 0) {
            isGameOver = true;
        }

        if (roundManager.areAllRoundsCompleted()) {
            isGameOver = true;
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
     * @param lives The new number of lives
     */
    public void setLives(int lives) {
        this.lives = Math.max(0, lives);
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
}
