package io.github.chargerdefense.model.map;

import io.github.chargerdefense.GameConstants;
import io.github.chargerdefense.model.Path;
import io.github.chargerdefense.model.Projectile;
import io.github.chargerdefense.model.enemy.Enemy;
import io.github.chargerdefense.model.unit.Unit;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

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
     * Renders the path that enemies follow.
     * The path is drawn as connected line segments between waypoints.
     *
     * @param shapeRenderer The shape renderer to use for drawing
     */
    public void renderPath(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BROWN);

        for (int i = 0; i < path.getLength() - 1; i++) {
            Point start = path.getWaypoint(i);
            Point end = path.getWaypoint(i + 1);

            if (start != null && end != null) {
                shapeRenderer.line(start.x, start.y, end.x, end.y);
            }
        }

        shapeRenderer.end();
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
     * Validates against map boundaries, existing units, and path proximity.
     *
     * @param x The x-coordinate to check
     * @param y The y-coordinate to check
     * @return true if placement is valid, false otherwise
     */
    public boolean isPlacementValid(int x, int y) {
        // check if outside map boundaries
        if (x < 0 || x >= GameConstants.GAME_WIDTH || y < 0 || y >= GameConstants.GAME_HEIGHT) {
            return false;
        }

        // check if too close to the path
        if (isTooCloseToPath(x, y)) {
            return false;
        }

        // check if too close to another unit
        Point proposedPosition = new Point(x, y);
        for (Unit unit : placedUnits) {
            Point unitPosition = unit.getPosition();
            double distance = proposedPosition.distanceSq(unitPosition);
            if (distance < Math.pow(GameConstants.MIN_UNIT_SPACING, 2)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if a position is too close to the enemy path.
     * Units cannot be placed directly on or too near the path.
     *
     * @param x The x-coordinate to check
     * @param y The y-coordinate to check
     * @return true if the position is too close to the path, false otherwise
     */
    private boolean isTooCloseToPath(int x, int y) {
        Point position = new Point(x, y);

        // check distance to each waypoint in the path
        for (int i = 0; i < path.getLength(); i++) {
            Point waypoint = path.getWaypoint(i);
            if (waypoint != null) {
                double distance = position.distanceSq(waypoint);
                if (distance < Math.pow(GameConstants.MIN_PATH_DISTANCE, 2)) {
                    return true;
                }
            }
        }

        // check distance to path segments (between waypoints)
        for (int i = 0; i < path.getLength() - 1; i++) {
            Point waypoint1 = path.getWaypoint(i);
            Point waypoint2 = path.getWaypoint(i + 1);

            if (waypoint1 != null && waypoint2 != null) {
                double distance = perpendicularDistanceToLine(position, waypoint1, waypoint2);
                if (distance < Math.pow(GameConstants.MIN_PATH_DISTANCE, 2)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Calculates the shortest distance from a point to a line segment.
     *
     * @param point     The point to measure from
     * @param lineStart The start point of the line segment
     * @param lineEnd   The end point of the line segment
     * @return The shortest squared distance from the point to the line segment
     */
    private double perpendicularDistanceToLine(Point point, Point lineStart, Point lineEnd) {
        double x = point.x;
        double y = point.y;
        double x1 = lineStart.x;
        double y1 = lineStart.y;
        double x2 = lineEnd.x;
        double y2 = lineEnd.y;

        double dx = x2 - x1;
        double dy = y2 - y1;

        // % projection of point onto line
        double t = ((x - x1) * dx + (y - y1) * dy) / (dx * dx + dy * dy);
        t = Math.max(0, Math.min(1, t));

        // lerp
        double closestX = x1 + t * dx;
        double closestY = y1 + t * dy;

        // return squared distance
        return (x - closestX) * (x - closestX) + (y - closestY) * (y - closestY);
    }

    /**
     * Gets the path that enemies follow on this map.
     *
     * @return The enemy path for this map
     */
    public Path getPath() {
        return path;
    }

    /**
     * Gets the list of all units placed on this map.
     *
     * @return A list of placed units
     */
    public List<Unit> getPlacedUnits() {
        return placedUnits;
    }

    /**
     * Gets the name of this map.
     *
     * @return The map name
     */
    public abstract String getMapName();

    /**
     * Gets a description of this map.
     *
     * @return A brief description of the map
     */
    public abstract String getDescription();
}
