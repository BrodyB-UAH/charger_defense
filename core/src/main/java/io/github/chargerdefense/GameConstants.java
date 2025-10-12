package io.github.chargerdefense;

/**
 * Contains constant values used throughout the game.
 * These values define game-wide settings that remain consistent across all maps
 * and gameplay.
 */
public final class GameConstants {

	/** The standard width of the game world in game units. */
	public static final int GAME_WIDTH = 800;

	/** The standard height of the game world in game units. */
	public static final int GAME_HEIGHT = 600;

	/** The minimum distance required between units for valid placement. */
	public static final int MIN_UNIT_SPACING = 32;

	/** The minimum distance from the path required for unit placement. */
	public static final int MIN_PATH_DISTANCE = 48;

	private GameConstants() {
	}
}
