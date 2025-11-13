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

	/** Standard sprite size (width and height) used for towers and enemies. */
	public static final float SPRITE_SIZE = 50.0f;

	/** Tower render size (width and height) for consistent sprite display. */
	public static final float TOWER_SIZE = 90.0f;

	/** Vertical offset for enemy health bars to position them above the sprite. */
	public static final float HEALTH_BAR_OFFSET = 55.0f;

	private GameConstants() {
	}
}
