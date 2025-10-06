package io.github.chargerdefense.model.unit;

import io.github.chargerdefense.model.Projectile;
import io.github.chargerdefense.model.enemy.Enemy;

import java.awt.Point;
import java.util.List;

/**
 * Represents a player-controlled tower/unit. It targets and attacks enemies
 * within its range by firing projectiles.
 */
public abstract class Unit {
    /** The damage dealt by the unit's projectiles. */
    private double damage;
    /** The attack range of the unit. */
    private double range;
    /** The rate at which the unit attacks, in attacks per second. */
    private double fireRate;
    /** The cost to purchase the unit. */
    private int cost;
    /** The position of the unit on the game map. */
    private Point position;
    /** The time remaining until the unit can fire again. */
    private double cooldown;
    /** The current enemy being targeted by the unit. */
    private Enemy currentTarget;

    /**
     * Constructs a Unit.
     *
     * @param damage   The damage each projectile deals.
     * @param range    The attack range of the unit.
     * @param fireRate The number of attacks per second.
     * @param cost     The currency cost to purchase the unit.
     */
    public Unit(double damage, double range, double fireRate, int cost) {
        this.damage = damage;
        this.range = range;
        this.fireRate = fireRate;
        this.cost = cost;
        this.cooldown = 0;
    }

    /**
     * Main update method for the unit, called each game tick.
     * Handles target acquisition and firing.
     *
     * @param deltaTime The time elapsed since the last update (in seconds)
     * @param enemies   The list of all active enemies to check for targets.
     * @return A new projectile if the unit fired, null otherwise
     */
    public Projectile update(float deltaTime, List<Enemy> enemies) {
        return null;
    }

    /**
     * Finds the first valid enemy in range and sets it as the current target.
     *
     * @param enemies The list of potential targets.
     */
    private void findNewTarget(List<Enemy> enemies) {
    }

    /**
     * Creates a projectile to fire at the current target.
     *
     * @return The newly created projectile, or null if no valid target
     */
    private Projectile fire() {
        if (currentTarget == null || currentTarget.isDead()) {
            return null;
        }

        return new Projectile(this, currentTarget, damage, 100);
    }

    /**
     * Checks if an enemy is within the unit's attack range.
     *
     * @param enemy The enemy to check
     * @return true if the enemy is within range, false otherwise
     */
    private boolean isEnemyInRange(Enemy enemy) {
        return position.distance(enemy.getPosition()) <= range;
    }

    /**
     * Sets the position of the unit on the map.
     *
     * @param position The new position for the unit
     */
    public void setPosition(Point position) {
        this.position = position;
    }

    /**
     * Gets the current position of the unit on the map.
     *
     * @return The current position of the unit
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Gets the cost to purchase this unit.
     *
     * @return The currency cost of the unit
     */
    public int getCost() {
        return cost;
    }
}
