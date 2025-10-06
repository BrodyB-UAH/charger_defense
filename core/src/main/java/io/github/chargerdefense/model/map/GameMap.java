package io.github.chargerdefense.model.map;

import io.github.chargerdefense.model.Path;
import io.github.chargerdefense.model.Projectile;
import io.github.chargerdefense.model.enemy.Enemy;
import io.github.chargerdefense.model.unit.Unit;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game map. It contains the path enemies follow
 * and the locations of all placed units.
 */
public abstract class GameMap {

    /** The path that enemies follow across the map. */
    private Path path;
    /** A list of all units that have been placed on the map. */
    private List<Unit> placedUnits;

    /**
     * Constructs a Map with a specified path.
     *
     * @param path The path that enemies will follow.
     */
    public GameMap(Path path) {
        this.path = path;
        this.placedUnits = new ArrayList<>();
    }

    /**
     * Places a unit on the map if the location is valid.
     *
     * @param unit The unit to place.
     * @param x    The x-coordinate of the desired location.
     * @param y    The y-coordinate of the desired location.
     * @return true if placement is successful, false otherwise.
     */
    public boolean placeUnit(Unit unit, int x, int y) {
        if (isPlacementValid(x, y)) {
            unit.setPosition(new Point(x, y));
            placedUnits.add(unit);
            return true;
        }

        return false;
    }

    /**
     * Updates all placed units, allowing them to find targets and attack.
     * Collects any projectiles created by units during this update.
     *
     * @param deltaTime     The time elapsed since the last update (in seconds)
     * @param activeEnemies A list of enemies currently on the map.
     * @return A list of new projectiles created by units this update
     */
    public List<Projectile> update(float deltaTime, List<Enemy> activeEnemies) {
        List<Projectile> newProjectiles = new ArrayList<>();

        for (Unit unit : placedUnits) {
            Projectile projectile = unit.update(deltaTime, activeEnemies);
            if (projectile != null) {
                newProjectiles.add(projectile);
            }
        }

        return newProjectiles;
    }

    /**
     * Checks if a unit can be placed at the specified coordinates.
     *
     * @param x The x-coordinate to check
     * @param y The y-coordinate to check
     * @return true if placement is valid, false otherwise
     */
    private boolean isPlacementValid(int x, int y) {
        // TODO check if on path
        // TODO check if outside map
        // TODO check if on another unit

        return true;
    }

    /**
     * Gets the path that enemies follow on this map.
     *
     * @return The enemy path for this map
     */
    public Path getPath() {
        return path;
    }
}
