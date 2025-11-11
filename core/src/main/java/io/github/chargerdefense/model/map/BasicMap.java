package io.github.chargerdefense.model.map;

import io.github.chargerdefense.GameConstants;
import io.github.chargerdefense.model.Path;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * A basic rectangular map with a simple path layout.
 */
public class BasicMap extends GameMap {
	/**
	 * Constructs a BasicMap with a predefined path.
	 */
	public BasicMap() {
		super(createBasicPath());
	}

	/**
	 * Creates a basic path for the map.
	 * The path enters from the left, makes several turns, and exits on the right.
	 *
	 * @return A Path object with predefined waypoints
	 */
	private static Path createBasicPath() {
		int width = GameConstants.GAME_WIDTH;
		int height = GameConstants.GAME_HEIGHT;

		List<Point> waypoints = new ArrayList<>();

		waypoints.add(new Point(0, height / 2));
		waypoints.add(new Point(width / 4, height / 3));
		waypoints.add(new Point(width / 2, 2 * height / 3));
		waypoints.add(new Point(3 * width / 4, height / 4));
		waypoints.add(new Point(width, height / 2));

		return new Path(waypoints);
	}

	public String getMapName() {
		return "Basic Map";
	}

	public String getDescription() {
		return "A simple winding path.";
	}
}
