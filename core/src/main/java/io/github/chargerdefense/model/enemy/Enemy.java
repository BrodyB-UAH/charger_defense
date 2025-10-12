package io.github.chargerdefense.model.enemy;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.chargerdefense.model.GameModel;
import io.github.chargerdefense.model.Path;

import java.awt.Point;

/**
 * Represents an enemy unit in the game. Manages its own health, speed, and
 * movement along a predefined path.
 */
public abstract class Enemy {
    /**
     * The initial health of the enemy. Used when resetting the enemy to initial
     * conditions.
     */
    private double initialHealth;
    /** The current health of the enemy. */
    private double health;
    /** The movement speed of the enemy in units per update. */
    private double speed;
    /** The amount of currency awarded to the player when the enemy is defeated. */
    private int currencyValue;
    /** The current position of the enemy on the map. */
    private Point position;
    /**
     * The index of the current waypoint on the path that the enemy is moving
     * towards.
     */
    private int pathIndex;
    /** A flag indicating whether the enemy is dead. */
    private boolean isDead = false;
    /** Reference to the game model for handling enemy events. */
    private GameModel gameModel;

    /**
     * Constructs an Enemy.
     *
     * @param health        The starting health of the enemy.
     * @param speed         The movement speed of the enemy (units per update).
     * @param currencyValue The currency awarded upon defeat.
     */
    public Enemy(double health, double speed, int currencyValue) {
        this.initialHealth = health;
        this.health = health;
        this.speed = speed;
        this.currencyValue = currencyValue;
        this.pathIndex = 0;
    }

    /**
     * Moves the enemy along the given path.
     *
     * @param deltaTime The time elapsed since the last update (in seconds)
     * @param path      The path to follow.
     */
    public void move(float deltaTime, Path path) {
        if (isDead)
            return;

        Point targetWaypoint = path.getWaypoint(pathIndex);

        if (position == null) {
            position = new Point(path.getWaypoint(0));
        }

        double dx = targetWaypoint.x - position.x;
        double dy = targetWaypoint.y - position.y;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < speed) {
            position = targetWaypoint;
            pathIndex += 1;
            if (pathIndex >= path.getLength()) {
                handleReachedEnd();
            }
        } else {
            position.x += (dx / distance) * speed * deltaTime;
            position.y += (dy / distance) * speed * deltaTime;
        }
    }

    /**
     * Applies damage to the enemy's health.
     * If health drops to or below zero, the enemy is marked as dead.
     *
     * @param damage The amount of damage to inflict.
     */
    public void takeDamage(double damage) {
        this.health -= damage;
        if (this.health <= 0 && !isDead) {
            isDead = true;
            handleDeath();
        }
    }

    /**
     * Handles the enemy's death by notifying the game model.
     * Called when the enemy's health drops to or below zero.
     */
    private void handleDeath() {
        if (gameModel != null) {
            gameModel.onEnemyDefeated(this);
        }
    }

    /**
     * Handles the enemy reaching the end of the path.
     * The enemy is marked as dead and the game model is notified.
     */
    private void handleReachedEnd() {
        isDead = true;
        if (gameModel != null) {
            gameModel.enemyReachedEnd(this);
        }
    }

    /**
     * Resets the enemy's state for potential reuse in a new round.
     * Resets path index and death status.
     * 
     * @param gameModel The game model to notify of enemy events.
     */
    public void reset(GameModel gameModel) {
        this.pathIndex = 0;
        this.isDead = false;
        this.health = this.initialHealth;
        this.gameModel = gameModel;
    }

    /**
     * Checks if the enemy is dead.
     *
     * @return true if the enemy is dead, false otherwise
     */
    public boolean isDead() {
        return isDead;
    }

    /**
     * Gets the currency value awarded when this enemy is defeated.
     *
     * @return The currency value of this enemy
     */
    public int getCurrencyValue() {
        return currencyValue;
    }

    /**
     * Gets the current position of the enemy.
     *
     * @return The current position as a Point object
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Checks if the enemy is camouflaged.
     *
     * @return true if the enemy is camouflaged, false otherwise
     */
    public boolean isCamouflaged() {
        return false;
    }

    /**
     * Renders the enemy as a colored circle with a health bar.
     *
     * @param shapeRenderer The shape renderer to use for drawing
     * @param scaleX        The horizontal scale factor (screen width / game width)
     * @param scaleY        The vertical scale factor (screen height / game height)
     */
    public void render(ShapeRenderer shapeRenderer, float scaleX, float scaleY) {
        if (position == null || isDead) {
            return;
        }

        float screenX = position.x * scaleX;
        float screenY = position.y * scaleY;
        float radius = 8.0f;

        shapeRenderer.setColor(Color.RED);
        shapeRenderer.circle(screenX, screenY, radius);

        // health bar
        float healthBarWidth = 20.0f;
        float healthBarHeight = 3.0f;
        float healthBarY = screenY + radius + 5.0f;
        float healthPercentage = (float) (health / initialHealth);

        // background
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(screenX - healthBarWidth / 2, healthBarY, healthBarWidth, healthBarHeight);

        // foreground
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(screenX - healthBarWidth / 2, healthBarY, healthBarWidth * healthPercentage,
                healthBarHeight);
    }
}
