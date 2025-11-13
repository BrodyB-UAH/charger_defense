package io.github.chargerdefense.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.github.chargerdefense.model.enemy.Enemy;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single wave of enemies. Contains the enemy data for the round
 * and manages their spawning and state.
 */
public class Round {
    /** Interval (in seconds) between spawning subsequent enemies. */
    private static final float SPAWN_INTERVAL = 4.0f;

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
     * Spawns the first enemy immediately, then subsequent enemies spawn every SPAWN_INTERVAL seconds.
     * 
     * @param gameModel The main game object for callbacks.
     */
    public void start(GameModel gameModel) {
        this.game = gameModel;
        this.spawnIndex = 0;
        this.enemiesDefeated = 0;
        this.enemiesEscaped = 0;
        this.spawnTimer = 0;
        
        for (Enemy enemy : enemiesToSpawn) {
            enemy.reset(gameModel);
            // Halve health for round 1 only
            if (gameModel.getRoundManager().getCurrentRoundNumber() == 1) {
                double currentHealth = enemy.getHealth();
                enemy.setHealth(currentHealth / 2);
            }
        }
        
        // Spawn the first enemy immediately
        if (enemiesToSpawn.size() > 0) {
            Enemy firstEnemy = enemiesToSpawn.get(spawnIndex++);
            activeEnemies.add(firstEnemy);
            spawnTimer = 0;  // Next enemy will spawn after SPAWN_INTERVAL seconds
        }
    }

    /**
     * Updates the round, spawning new enemies over time and moving existing ones.
     *
     * @param deltaTime The time elapsed since the last update (in seconds)
     */
    public void update(float deltaTime) {
        // Spawn remaining enemies every SPAWN_INTERVAL seconds
        spawnTimer += deltaTime;
        if (spawnTimer >= SPAWN_INTERVAL && spawnIndex < enemiesToSpawn.size()) {
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

    /**
     * Sets the sprite texture for all enemies in this round.
     *
     * @param sprite The TextureRegion to apply to all enemies
     */
    public void setEnemySprite(TextureRegion sprite) {
        for (Enemy enemy : enemiesToSpawn) {
            enemy.setSprite(sprite);
        }
        for (Enemy enemy : activeEnemies) {
            enemy.setSprite(sprite);
        }
    }
}
