package io.github.chargerdefense.model;

import io.github.chargerdefense.model.enemy.Enemy;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single wave of enemies. Contains the enemy data for the round
 * and manages their spawning and state.
 */
public class Round {
    /** A list of all enemies that will be spawned during this round. */
    private List<Enemy> enemiesToSpawn;
    /** A list of enemies that are currently active on the map. */
    private List<Enemy> activeEnemies;
    /** The number of enemies that have been defeated in this round. */
    private int enemiesDefeated;
    /** The number of enemies that have escaped by reaching the end of the path. */
    private int enemiesEscaped;
    /** A timer to control the spawning of enemies. */
    private double spawnTimer;
    /** The index of the next enemy to spawn from the `enemiesToSpawn` list. */
    private int spawnIndex;
    /** A reference to the main game model, used for callbacks. */
    private GameModel game;

    /**
     * Constructs a new Round with the specified list of enemies.
     *
     * @param enemies The list of enemies that will be spawned during this round
     */
    public Round(List<Enemy> enemies) {
        this.enemiesToSpawn = enemies;
        this.activeEnemies = new ArrayList<>();
    }

    /**
     * Prepares the round to begin.
     * 
     * @param gameModel The main game object for callbacks.
     */
    public void start(GameModel gameModel) {
        this.game = gameModel;
        this.spawnIndex = 0;
        this.enemiesDefeated = 0;
        this.enemiesEscaped = 0;
        for (Enemy enemy : enemiesToSpawn) {
            enemy.reset(gameModel);
        }
    }

    /**
     * Updates the round, spawning new enemies over time and moving existing ones.
     *
     * @param deltaTime The time elapsed since the last update (in seconds)
     */
    public void update(float deltaTime) {
        // for now, spawn one enemy per 0.5s
        spawnTimer += deltaTime;
        if (spawnTimer >= 0.5 && spawnIndex < enemiesToSpawn.size()) {
            Enemy enemy = enemiesToSpawn.get(spawnIndex++);
            activeEnemies.add(enemy);
            spawnTimer = 0;
        }

        activeEnemies.removeIf(Enemy::isDead);
        Path path = game.getMap().getPath();
        for (Enemy enemy : activeEnemies) {
            enemy.move(deltaTime, path);
        }
    }

    /**
     * Checks if the round is complete.
     * A round is complete when all enemies have been spawned and no active enemies
     * remain.
     *
     * @return true if the round is complete, false otherwise
     */
    public boolean isComplete() {
        return spawnIndex >= enemiesToSpawn.size() && activeEnemies.isEmpty();
    }

    /**
     * Gets the list of currently active enemies on the map.
     *
     * @return A list of active enemies
     */
    public List<Enemy> getActiveEnemies() {
        return activeEnemies;
    }

    /**
     * Gets the number of enemies defeated in this round.
     *
     * @return The number of enemies defeated
     */
    public int getEnemiesDefeated() {
        return enemiesDefeated;
    }

    /**
     * Gets the number of enemies that escaped in this round.
     *
     * @return The number of enemies that escaped
     */
    public int getEnemiesEscaped() {
        return enemiesEscaped;
    }

    /**
     * Called by the game model when an enemy from this round is defeated.
     * Increments the defeated count.
     *
     * @param enemy The enemy that was defeated
     */
    public void onEnemyDefeated(Enemy enemy) {
        enemiesDefeated += 1;
        // Enemy is removed in update()
    }

    /**
     * Called by the game model when an enemy from this round reaches the end.
     * Increments the escaped count.
     *
     * @param enemy The enemy that reached the end
     */
    public void onEnemyReachedEnd(Enemy enemy) {
        enemiesEscaped += 1;
        // Enemy is removed in update()
    }
}
