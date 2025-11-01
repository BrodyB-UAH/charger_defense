package io.github.chargerdefense.model.map;

import io.github.chargerdefense.GameConstants;
import io.github.chargerdefense.model.Path;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * A spiral-shaped map layout.
 */
public class SpiralMap extends GameMap {
	/**
	 * Constructs a SpiralMap with a predefined spiral path.
	 */
	public SpiralMap() {
		super(createSpiralPath());
	}

	/**
	 * Creates a spiral path for the map.
	 *
	 * @return A Path object with spiral waypoints
	 */
	private static Path createSpiralPath() {
		int width = GameConstants.GAME_WIDTH;
		int height = GameConstants.GAME_HEIGHT;

		List<Point> waypoints = new ArrayList<>();
		int margin = 50;

		waypoints.add(new Point(margin, margin));
		waypoints.add(new Point(width - margin, margin));
		waypoints.add(new Point(width - margin, height - margin));
		waypoints.add(new Point(margin, height - margin));
		waypoints.add(new Point(margin, margin * 3));
		waypoints.add(new Point(width - margin * 3, margin * 3));
		waypoints.add(new Point(width - margin * 3, height - margin * 3));
		waypoints.add(new Point(margin * 3, height - margin * 3));
		waypoints.add(new Point(width / 2, height / 2));

		return new Path(waypoints);
	}

	public String getMapName() {
		return "Spiral Map";
	}

	public String getDescription() {
		return "A spiral path.";
	}
}
