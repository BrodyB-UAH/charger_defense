package io.github.chargerdefense.model;

/**
 * Represents the state and progress data for a specific map.
 * Contains high score information and the last saved game state.
 */
public class MapState {
	/** The highest score achieved on this map */
	private int highScore;
	/**
	 * The last saved game state as a JSON string (can be null if no save exists)
	 */
	private String lastSaveData;
	/** The timestamp of when this map was last played */
	private long lastPlayedTimestamp;
	/** Whether this map has been completed at least once */
	private boolean completed;

	/**
	 * Default constructor for JSON deserialization.
	 */
	public MapState() {
		this.highScore = 0;
		this.lastSaveData = null;
		this.lastPlayedTimestamp = 0;
		this.completed = false;
	}

	/**
	 * Constructs a new MapState with the specified initial values.
	 *
	 * @param highScore           The initial high score for this map
	 * @param lastSaveData        The initial save data (can be null)
	 * @param lastPlayedTimestamp The timestamp of when this map was last played
	 * @param completed           Whether this map has been completed
	 */
	public MapState(int highScore, String lastSaveData, long lastPlayedTimestamp, boolean completed) {
		this.highScore = highScore;
		this.lastSaveData = lastSaveData;
		this.lastPlayedTimestamp = lastPlayedTimestamp;
		this.completed = completed;
	}

	/**
	 * Gets the high score for this map.
	 *
	 * @return The highest score achieved on this map
	 */
	public int getHighScore() {
		return highScore;
	}

	/**
	 * Sets the high score for this map.
	 *
	 * @param highScore The new high score to set
	 */
	public void setHighScore(int highScore) {
		this.highScore = highScore;
	}

	/**
	 * Gets the last save data for this map.
	 *
	 * @return The last saved game state as a JSON string, or null if no save exists
	 */
	public String getLastSaveData() {
		return lastSaveData;
	}

	/**
	 * Sets the last save data for this map.
	 *
	 * @param lastSaveData The save data as a JSON string (can be null to clear)
	 */
	public void setLastSaveData(String lastSaveData) {
		this.lastSaveData = lastSaveData;
	}

	/**
	 * Gets the timestamp of when this map was last played.
	 *
	 * @return The last played timestamp in milliseconds since epoch
	 */
	public long getLastPlayedTimestamp() {
		return lastPlayedTimestamp;
	}

	/**
	 * Sets the timestamp of when this map was last played.
	 *
	 * @param lastPlayedTimestamp The timestamp in milliseconds since epoch
	 */
	public void setLastPlayedTimestamp(long lastPlayedTimestamp) {
		this.lastPlayedTimestamp = lastPlayedTimestamp;
	}

	/**
	 * Checks if this map has been completed at least once.
	 *
	 * @return true if the map has been completed, false otherwise
	 */
	public boolean isCompleted() {
		return completed;
	}

	/**
	 * Sets whether this map has been completed.
	 *
	 * @param completed true if the map has been completed, false otherwise
	 */
	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	/**
	 * Updates the high score if the provided score is higher than the current one.
	 *
	 * @param score The score to compare against the current high score
	 * @return true if the high score was updated, false otherwise
	 */
	public boolean updateHighScore(int score) {
		if (score > highScore) {
			highScore = score;
			return true;
		}

		return false;
	}

	/**
	 * Checks if this map has any save data.
	 *
	 * @return true if save data exists, false otherwise
	 */
	public boolean hasSaveData() {
		return lastSaveData != null && !lastSaveData.trim().isEmpty();
	}
}