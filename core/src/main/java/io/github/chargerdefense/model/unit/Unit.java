package io.github.chargerdefense.model.unit;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.chargerdefense.model.Projectile;
import io.github.chargerdefense.model.enemy.Enemy;
import io.github.chargerdefense.GameConstants;
import io.github.chargerdefense.model.unit.upgrade.Upgrade;
import io.github.chargerdefense.model.unit.upgrade.UpgradePath;

import java.awt.Point;
import java.util.List;

/**
 * Represents a player-controlled tower/unit. It targets and attacks enemies
 * within its range by firing projectiles.
 */
public abstract class Unit {
    /**
     * Enum representing targeting priority modes for the unit.
     */
    public enum TargetingMode {
        FIRST("First"),
        LAST("Last"),
        CLOSEST("Closest");

        private final String displayName;

        TargetingMode(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        /**
         * Cycles to the next targeting mode in order: FIRST -> LAST -> CLOSEST ->
         * FIRST...
         */
        public TargetingMode next() {
            switch (this) {
                case FIRST:
                    return LAST;
                case LAST:
                    return CLOSEST;
                case CLOSEST:
                    return FIRST;
                default:
                    return FIRST;
            }
        }
    }

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
    /** The upgrade path for this unit. */
    protected UpgradePath upgradePath;
    /** Whether the unit is currently selected. */
    private boolean isSelected = false;
    /** The current targeting mode for this unit. */
    private TargetingMode targetMode = TargetingMode.FIRST;
    /** The sprite texture for rendering the unit. */
    private TextureRegion sprite;
    /** Whether the unit can detect camouflaged enemies. */
    private boolean canDetectCamo = false;

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
        loadSprite();
    }

    /**
     * Loads the sprite for this unit from the Assets singleton.
     * Subclasses can override to load different sprites.
     */
    protected void loadSprite() {
        // Default implementation - does nothing
        // Subclasses should override to set their specific sprite
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
     * Finds the best valid enemy in range based on the current targeting mode and
     * sets it as the target.
     *
     * @param enemies The list of potential targets.
     */
    private void findNewTarget(List<Enemy> enemies) {
        currentTarget = null;

        if (enemies.isEmpty()) {
            return;
        }

        switch (targetMode) {
            case FIRST:
                findTargetFirst(enemies);
                break;
            case LAST:
                findTargetLast(enemies);
                break;
            case CLOSEST:
                findTargetClosest(enemies);
                break;
        }
    }

    /**
     * Finds the enemy furthest along the path (closest to the end).
     *
     * @param enemies The list of potential targets.
     */
    private void findTargetFirst(List<Enemy> enemies) {
        int maxPathIndex = -1;
        for (Enemy enemy : enemies) {
            double distanceSq = position.distanceSq(enemy.getPosition());
            if (canSeeEnemy(enemy, distanceSq)) {
                int pathIndex = enemy.getPathIndex();
                if (pathIndex > maxPathIndex) {
                    maxPathIndex = pathIndex;
                    currentTarget = enemy;
                }
            }
        }
    }

    /**
     * Finds the enemy nearest the start of the path (closest to beginning).
     *
     * @param enemies The list of potential targets.
     */
    private void findTargetLast(List<Enemy> enemies) {
        int minPathIndex = Integer.MAX_VALUE;
        for (Enemy enemy : enemies) {
            double distanceSq = position.distanceSq(enemy.getPosition());
            if (canSeeEnemy(enemy, distanceSq)) {
                int pathIndex = enemy.getPathIndex();
                if (pathIndex < minPathIndex) {
                    minPathIndex = pathIndex;
                    currentTarget = enemy;
                }
            }
        }
    }

    /**
     * Finds the enemy physically closest to the tower.
     *
     * @param enemies The list of potential targets.
     */
    private void findTargetClosest(List<Enemy> enemies) {
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
        return isInRange(enemy, distanceSq) && (canDetectCamo || !enemy.isCamouflaged());
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
     * Gets the attack range of this unit.
     *
     * @return The range in pixels
     */
    public double getRange() {
        return range;
    }

    /**
     * Gets the damage dealt by this unit.
     * 
     * @return The damage amount
     */
    public double getDamage() {
        return damage;
    }

    /**
     * Renders the unit sprite.
     *
     * @param batch The SpriteBatch for drawing the sprite
     */
    public void renderSprite(SpriteBatch batch) {
        if (position == null) {
            return;
        }

        // Draw sprite
        if (sprite != null) {
            float spriteSize = GameConstants.TOWER_SIZE;
            batch.draw(sprite, position.x - spriteSize / 2, position.y - spriteSize / 2, spriteSize, spriteSize);
        }
    }

    /**
     * Renders the unit overlays (range circle, selection indicator, targeting
     * line).
     *
     * @param shapeRenderer The shape renderer to use for drawing
     */
    public void renderOverlay(ShapeRenderer shapeRenderer) {
        if (position == null) {
            return;
        }

        float size = GameConstants.TOWER_SIZE;

        // draw the range indicator
        shapeRenderer.setColor(0.5f, 0.5f, 1.0f, 0.2f);
        shapeRenderer.circle(position.x, position.y, (float) range);

        if (isSelected) {
            shapeRenderer.setColor(Color.YELLOW);
            // Draw selection highlight centered on tower, matching sprite bounds exactly
            shapeRenderer.rect(position.x - size / 2, position.y - size / 2, size, size);
        }

        // Do not draw a fallback rectangle; sprites define the unit visuals.
        // targeting line
        if (currentTarget != null && !currentTarget.isDead()) {
            Point targetPos = currentTarget.getPosition();
            shapeRenderer.setColor(Color.RED);
            shapeRenderer.line(position.x, position.y, targetPos.x, targetPos.y);
        }
    }

    /**
     * Renders the unit (legacy method for compatibility).
     *
     * @param shapeRenderer The shape renderer to use for drawing
     */
    public void render(ShapeRenderer shapeRenderer) {
        renderOverlay(shapeRenderer);
    }

    /**
     * Gets the upgrade path for this unit.
     * 
     * @return The upgrade path
     */
    public UpgradePath getUpgradePath() {
        return upgradePath;
    }

    /** Applies the next upgrade in the upgrade path. */
    public void applyUpgrade() {
        if (upgradePath != null) {
            Upgrade nextUpgrade = upgradePath.getNextUpgrade();
            if (nextUpgrade != null) {
                nextUpgrade.apply(this);
                upgradePath.advance();
            }
        }
    }

    /**
     * Checks if the unit is currently selected.
     * 
     * @return true if the unit is selected, false otherwise
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * Sets whether the unit is selected.
     * 
     * @param selected true to select the unit, false to deselect
     */
    public void setSelected(boolean selected) {
        this.isSelected = selected;
    }

    /**
     * Checks if the unit contains the specified coordinates.
     * 
     * @param x The x coordinate to check
     * @param y The y coordinate to check
     * @return true if the coordinates are within the unit's bounds, false otherwise
     */
    public boolean contains(int x, int y) {
        float size = GameConstants.TOWER_SIZE;
        return (x >= position.x - size / 2 && x <= position.x + size / 2 &&
                y >= position.y - size / 2 && y <= position.y + size / 2);
    }

    /**
     * Sets the damage dealt by the unit.
     * 
     * @param damage The new damage value
     */
    public void setDamage(double damage) {
        this.damage = damage;
    }

    /**
     * Sets the attack range of the unit.
     * 
     * @param range The new range value
     */
    public void setRange(double range) {
        this.range = range;
    }

    /**
     * Sets the fire rate of the unit.
     * 
     * @param fireRate The new fire rate value
     */
    public void setFireRate(double fireRate) {
        this.fireRate = fireRate;
    }

    /**
     * Gets the current targeting mode of the unit.
     * 
     * @return The current targeting mode
     */
    public TargetingMode getTargetMode() {
        return targetMode;
    }

    /**
     * Sets the targeting mode of the unit.
     * 
     * @param targetMode The new targeting mode
     */
    public void setTargetMode(TargetingMode targetMode) {
        this.targetMode = targetMode;
    }

    /**
     * Cycles to the next targeting mode.
     * Order: FIRST -> LAST -> CLOSEST -> FIRST...
     */
    public void cycleTargetMode() {
        this.targetMode = targetMode.next();
    }

    /**
     * Sets the sprite texture for rendering the unit.
     *
     * @param sprite The TextureRegion to use for rendering
     */
    public void setSprite(TextureRegion sprite) {
        this.sprite = sprite;
    }

    /**
     * Gets the sprite currently assigned to this unit.
     *
     * @return The TextureRegion sprite or null if none set
     */
    public TextureRegion getSprite() {
        return sprite;
    }

    /**
     * Sets whether the unit can detect camouflaged enemies.
     *
     * @param canDetectCamo true to enable camo detection, false to disable
     */
    public void setCanDetectCamo(boolean canDetectCamo) {
        this.canDetectCamo = canDetectCamo;
    }

    /**
     * Checks if the unit can detect camouflaged enemies.
     *
     * @return true if the unit can detect camo, false otherwise
     */
    public boolean canDetectCamo() {
        return canDetectCamo;
    }
}
