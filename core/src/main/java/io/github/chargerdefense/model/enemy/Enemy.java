package io.github.chargerdefense.model.enemy;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.chargerdefense.assets.Assets;
import io.github.chargerdefense.model.GameModel;
import io.github.chargerdefense.model.Path;
import io.github.chargerdefense.model.Projectile;
import io.github.chargerdefense.GameConstants;

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
    /** The precise floating-point x position for accurate movement. */
    private double preciseX;
    /** The precise floating-point y position for accurate movement. */
    private double preciseY;
    /**
     * The index of the current waypoint on the path that the enemy is moving
     * towards.
     */
    private int pathIndex;
    /** A flag indicating whether the enemy is dead. */
    private boolean isDead = false;
    /** Reference to the game model for handling enemy events. */
    private GameModel gameModel;
    /** The sprite texture for rendering the enemy. */
    private TextureRegion sprite;

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
        loadSprite();
    }

    /**
     * Loads the sprite for this enemy from the Assets singleton.
     * Subclasses can override to load different sprites.
     */
    protected void loadSprite() {
        // Default implementation - does nothing
        // Subclasses should override to set their specific sprite
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

        // initialize to first waypoint
        if (position == null) {
            Point firstWaypoint = path.getWaypoint(0);
            preciseX = firstWaypoint.x;
            preciseY = firstWaypoint.y;
            position = new Point((int) preciseX, (int) preciseY);
            pathIndex = 1; // start moving towards the second waypoint
            return;
        }

        if (pathIndex >= path.getLength()) {
            handleReachedEnd();
            return;
        }

        float remainingDistance = (float) (speed * deltaTime);

        while (remainingDistance > 0 && pathIndex < path.getLength()) {
            Point targetWaypoint = path.getWaypoint(pathIndex);

            double dx = targetWaypoint.x - preciseX;
            double dy = targetWaypoint.y - preciseY;
            double distanceToWaypoint = Math.sqrt(dx * dx + dy * dy);

            if (distanceToWaypoint <= remainingDistance) {
                preciseX = targetWaypoint.x;
                preciseY = targetWaypoint.y;
                position = new Point((int) preciseX, (int) preciseY);
                remainingDistance -= (float) distanceToWaypoint;
                pathIndex += 1;

                if (pathIndex >= path.getLength()) {
                    handleReachedEnd();
                    return;
                }
            } else {
                double ratio = remainingDistance / distanceToWaypoint;
                preciseX += dx * ratio;
                preciseY += dy * ratio;
                position.x = (int) preciseX;
                position.y = (int) preciseY;
                remainingDistance = 0;
            }
        }
    }

    /**
     * Checks for collisions with stationary projectiles (like spikes) and takes
     * damage.
     * Should be called after movement to check if the enemy walked over any spikes.
     *
     * @param projectiles The list of active projectiles to check
     */
    public void checkProjectileCollisions(java.util.List<Projectile> projectiles) {
        if (isDead || position == null) {
            return;
        }

        for (Projectile projectile : projectiles) {
            if (projectile.checkCollisionWithEnemy(this)) {
                projectile.hitEnemy(this);
            }
        }
    }

    /**
     * Applies damage to the enemy's health.
     * If health drops to or below zero, the enemy is marked as dead.
     *
     * @param damage The amount of damage to inflict.
     */
    public void takeDamage(double damage) {
        Sound hitSound = Assets.getInstance().getEnemyHitSound();
        if (hitSound != null) {
            hitSound.play();
        }

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
        this.position = null;
        this.preciseX = 0;
        this.preciseY = 0;
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
     * Gets the path progress of the enemy.
     * Higher values indicate enemies further along the path.
     *
     * @return The current path waypoint index
     */
    public int getPathIndex() {
        return pathIndex;
    }

    /**
     * Gets the current health of the enemy.
     *
     * @return The current health value
     */
    public double getHealth() {
        return health;
    }

    /**
     * Sets the health of the enemy and updates the initial health accordingly.
     *
     * @param newHealth The new health value
     */
    public void setHealth(double newHealth) {
        this.initialHealth = newHealth;
        this.health = newHealth;
    }

    /**
     * Sets the sprite texture for rendering the enemy.
     *
     * @param sprite The TextureRegion to use for rendering
     */
    public void setSprite(TextureRegion sprite) {
        this.sprite = sprite;
    }

    /**
     * Gets the sprite currently assigned to this enemy.
     *
     * @return The TextureRegion sprite or null if none set
     */
    public TextureRegion getSprite() {
        return sprite;
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
     * Renders the enemy sprite.
     *
     * @param batch The SpriteBatch for drawing the sprite
     */
    public void renderSprite(SpriteBatch batch) {
        if (position == null) {
            return;
        }

        if (sprite == null) {
            loadSprite();
        }

        // Draw sprite
        if (sprite != null) {
            float spriteSize = GameConstants.SPRITE_SIZE;
            batch.draw(sprite, position.x - spriteSize / 2, position.y - spriteSize / 2, spriteSize, spriteSize);
        }
    }

    /**
     * Renders the enemy's health bar.
     *
     * @param shapeRenderer The ShapeRenderer for drawing the health bar
     */
    public void renderHealthBar(ShapeRenderer shapeRenderer) {
        if (position == null || isDead) {
            return;
        }

        // Draw health bar
        float healthBarWidth = 20.0f;
        float healthBarHeight = 3.0f;
        float healthBarY = position.y + GameConstants.HEALTH_BAR_OFFSET;
        float healthPercentage = (float) (health / initialHealth);

        // background
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(position.x - healthBarWidth / 2, healthBarY, healthBarWidth, healthBarHeight);

        // foreground
        shapeRenderer.setColor(Color.GREEN);
        shapeRenderer.rect(position.x - healthBarWidth / 2, healthBarY, healthBarWidth * healthPercentage,
                healthBarHeight);
    }

    /**
     * Renders the enemy sprite and health bar (legacy method for compatibility).
     *
     * @param batch         The SpriteBatch for drawing the sprite
     * @param shapeRenderer The ShapeRenderer for drawing the health bar
     */
    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        renderSprite(batch);
        renderHealthBar(shapeRenderer);
    }
}
