package io.github.chargerdefense.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the sequence of waypoints that enemies travel along.
 */
public class Path {
    /** A List of waypoints defining the Path */
    private final List<Point> waypoints;

    /**
     * Constructs a path from a list of waypoints.
     *
     * @param waypoints The list of (x, y) points defining the path.
     */
    public Path(List<Point> waypoints) {
        this.waypoints = new ArrayList<>(waypoints);
    }

    /**
     * Gets the waypoint at a specific index.
     *
     * @param index The index of the waypoint.
     * @return The Point at the given index.
     */
    public Point getWaypoint(int index) {
        if (index >= 0 && index < waypoints.size()) {
            return waypoints.get(index);
        }

        return null;
    }

    /**
     * Gets the total number of waypoints in the path.
     *
     * @return The number of waypoints in this path
     */
    public int getLength() {
        return waypoints.size();
    }
}
