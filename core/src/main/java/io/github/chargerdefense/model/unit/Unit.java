package io.github.chargerdefense.model.unit;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

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
        if (cooldown > 0) {
            cooldown -= deltaTime;
        }

        if (currentTarget == null || currentTarget.isDead()
                || !canSeeEnemy(currentTarget, position.distanceSq(currentTarget.getPosition()))) {
            findNewTarget(enemies);
        }

        if (currentTarget != null && cooldown <= 0) {
            Projectile projectile = fire();
            cooldown = 1.0 / fireRate;
            return projectile;
        }

        return null;
    }

    /**
     * Finds the first valid enemy in range and sets it as the current target.
     *
     * @param enemies The list of potential targets.
     */
    private void findNewTarget(List<Enemy> enemies) {
        currentTarget = null;
        double closestDistanceSq = Double.MAX_VALUE;
        for (Enemy enemy : enemies) {
            double distanceSq = position.distanceSq(enemy.getPosition());
            if (distanceSq < closestDistanceSq) {
                if (canSeeEnemy(enemy, distanceSq)) {
                    closestDistanceSq = distanceSq;
                    currentTarget = enemy;
                }
            }
        }
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
     * Checks if the enemy is within range
     *
     * @param enemy      The enemy to check
     * @param distanceSq The squared distance to the enemy
     * @return true if the enemy is within range, false otherwise
     */
    private boolean isInRange(Enemy enemy, double distanceSq) {
        return distanceSq <= Math.pow(range, 2);
    }

    /**
     * Checks if the unit can see an enemy
     *
     * @param enemy      The enemy to check
     * @param distanceSq The squared distance to the enemy
     * @return true if the enemy is within range, false otherwise
     */
    private boolean canSeeEnemy(Enemy enemy, double distanceSq) {
        return isInRange(enemy, distanceSq) && !enemy.isCamouflaged();
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

    /**
     * Renders the unit as a colored square with a range circle.
     *
     * @param shapeRenderer The shape renderer to use for drawing
     * @param scaleX        The horizontal scale factor (screen width / game width)
     * @param scaleY        The vertical scale factor (screen height / game height)
     */
    public void render(ShapeRenderer shapeRenderer, float scaleX, float scaleY) {
        if (position == null) {
            return;
        }

        float screenX = position.x * scaleX;
        float screenY = position.y * scaleY;
        float size = 16.0f;

        // draw the range indicator
        shapeRenderer.setColor(0.5f, 0.5f, 1.0f, 0.2f);
        shapeRenderer.circle(screenX, screenY, (float) range * scaleX);

        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(screenX - size / 2, screenY - size / 2, size, size);

        // targeting line
        if (currentTarget != null && !currentTarget.isDead()) {
            Point targetPos = currentTarget.getPosition();
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.line(screenX, screenY, targetPos.x * scaleX, targetPos.y * scaleY);
        }
    }
}
