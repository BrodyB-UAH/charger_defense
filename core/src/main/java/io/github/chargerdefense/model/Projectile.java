package io.github.chargerdefense.model;

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
    private static final double HIT_RADIUS = 5.0;
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

        return false;
    }

    /**
     * Checks if the projectile has hit its target based on distance.
     *
     * @return true if the projectile is within hit radius of the target
     */
    private boolean checkHit() {
        return false;
    }

    /**
     * Simulates the projectile hitting the target and dealing damage.
     * Called when a hit is detected.
     */
    private void impact() {
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
     * Gets the hit radius used for collision detection.
     *
     * @return The hit radius in pixels
     */
    public static double getHitRadius() {
        return HIT_RADIUS;
    }
}
