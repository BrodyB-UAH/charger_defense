package io.github.chargerdefense.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.chargerdefense.model.enemy.Enemy;
import io.github.chargerdefense.model.unit.Unit;

import java.awt.Point;

/**
 * Represents a projectile fired from a Unit towards an Enemy.
 * The projectile moves from its origin position towards the target at a
 * specified speed, and hits when it gets within a certain distance of the
 * target.
 */
public class Projectile {
    /** The unit that fired the projectile. */
    private final Unit origin;
    /** The enemy being targeted by the projectile. */
    private final Enemy target;
    /** The amount of damage the projectile will deal upon impact. */
    private final double damage;
    /** The current position of the projectile. */
    private final Point.Double position;
    /** The speed at which the projectile moves (pixels per second). */
    private final double speed;

    /**
     * The distance at which the projectile is considered to have hit the target.
     */
    private double hitRadius = 5.0;
    /** Whether the projectile has hit its target or should be removed. */
    private boolean isDestroyed;

    /**
     * Constructs a projectile that starts at the origin unit's position and moves
     * towards the target.
     *
     * @param origin The Unit that fired the projectile.
     * @param target The Enemy being targeted.
     * @param damage The damage to be dealt upon impact.
     * @param speed  The speed at which the projectile moves (pixels per second).
     */
    public Projectile(Unit origin, Enemy target, double damage, double speed) {
        this.origin = origin;
        this.target = target;
        this.damage = damage;
        this.speed = speed;
        this.isDestroyed = false;

        Point originPos = origin.getPosition();
        this.position = new Point.Double(originPos.x, originPos.y);
    }

    /**
     * Constructs a stationary projectile (like a spike) at a specific position.
     *
     * @param origin   The Unit that created the projectile.
     * @param position The position where the projectile should be placed.
     * @param damage   The damage to be dealt upon impact.
     */
    public Projectile(Unit origin, Point.Double position, double damage) {
        this.origin = origin;
        this.target = null;
        this.damage = damage;
        this.speed = 0;
        this.isDestroyed = false;
        this.position = position;
    }

    /**
     * Updates the projectile's position and checks for collision with the target.
     *
     * @param deltaTime The time elapsed since the last update (in seconds)
     * @return true if the projectile hit its target or should be removed, false if
     *         it should continue
     */
    public boolean update(float deltaTime) {
        if (isDestroyed) {
            return true;
        }

        if (speed != 0) {
            Point targetPos = target.getPosition();
            Point.Double targetPosition = new Point.Double(targetPos.x, targetPos.y);

            double dx = targetPosition.x - position.x;
            double dy = targetPosition.y - position.y;
            double distance = Math.sqrt(dx * dx + dy * dy);

            Point.Double velocity;
            if (distance > 0) {
                velocity = new Point.Double(
                        (dx / distance) * speed,
                        (dy / distance) * speed);
            } else {
                velocity = new Point.Double(0, 0);
            }

            position.x += velocity.x * deltaTime;
            position.y += velocity.y * deltaTime;
        }

        // only check for collision if projectile has a target (non-stationary);
        // stationary projectiles are checked by enemies
        if (target != null && checkHit()) {
            impact();
            isDestroyed = true;
            return true;
        }

        return false;
    }

    /**
     * Checks if the projectile has hit its target based on distance.
     *
     * @return true if the projectile is within hit radius of the target
     */
    private boolean checkHit() {
        if (target == null || target.isDead()) {
            return false;
        }

        Point targetCurrentPos = target.getPosition();
        double distanceToTarget = Math.pow(position.x - targetCurrentPos.x, 2) +
                Math.pow(position.y - targetCurrentPos.y, 2);

        return distanceToTarget <= Math.pow(hitRadius, 2);
    }

    /**
     * Simulates the projectile hitting the target and dealing damage.
     * Called when a hit is detected.
     */
    private void impact() {
        if (target != null && !target.isDead()) {
            target.takeDamage(damage);
        }
    }

    /**
     * Checks if this projectile collides with the given enemy.
     * Used by enemies to check collision with stationary projectiles like spikes.
     *
     * @param enemy The enemy to check collision with
     * @return true if collision detected
     */
    public boolean checkCollisionWithEnemy(Enemy enemy) {
        if (isDestroyed || enemy == null || enemy.isDead()) {
            return false;
        }

        Point enemyPos = enemy.getPosition();
        double distanceSq = Math.pow(position.x - enemyPos.x, 2) +
                Math.pow(position.y - enemyPos.y, 2);

        return distanceSq <= Math.pow(hitRadius, 2);
    }

    /**
     * Applies damage to the given enemy and destroys this projectile.
     * Called by enemies when they collide with a stationary projectile.
     *
     * @param enemy The enemy to damage
     */
    public void hitEnemy(Enemy enemy) {
        if (!isDestroyed && enemy != null && !enemy.isDead()) {
            enemy.takeDamage(damage);
            isDestroyed = true;
        }
    }

    /**
     * Gets the unit that fired this projectile.
     *
     * @return The origin unit
     */
    public Unit getOrigin() {
        return origin;
    }

    /**
     * Gets the target enemy of this projectile.
     *
     * @return The target enemy
     */
    public Enemy getTarget() {
        return target;
    }

    /**
     * Gets the damage this projectile will deal.
     *
     * @return The damage amount
     */
    public double getDamage() {
        return damage;
    }

    /**
     * Gets the current position of the projectile.
     *
     * @return The current position as a Point.Double
     */
    public Point.Double getPosition() {
        return position;
    }

    /**
     * Gets the movement speed of the projectile.
     *
     * @return The speed in pixels per second
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Checks if the projectile has been destroyed (hit target or missed).
     *
     * @return true if the projectile should be removed from the game
     */
    public boolean isDestroyed() {
        return isDestroyed;
    }

    /**
     * Renders the projectile as a small circle.
     *
     * @param shapeRenderer The shape renderer to use for drawing
     */
    public void render(ShapeRenderer shapeRenderer) {
        if (isDestroyed) {
            return;
        }

        float radius = 3.0f;

        shapeRenderer.setColor(Color.YELLOW);
        shapeRenderer.circle((float) position.x, (float) position.y, radius);
    }
}
