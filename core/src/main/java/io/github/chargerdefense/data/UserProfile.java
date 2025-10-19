package io.github.chargerdefense.data;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.utils.Json;
import io.github.chargerdefense.data.game.SavedGameState;
import io.github.chargerdefense.model.MapState;

/**
 * Represents a user profile containing username and progress data for all maps.
 * This class is designed to be serialized to and from JSON files for
 * persistence.
 */
public class UserProfile {
	/** The username for this profile */
	private String username;
	/** Map of map names to their corresponding state and progress data */
	private Map<String, MapState> mapStates;
	/** The timestamp when this profile was created */
	private long createdTimestamp;
	/** The timestamp when this profile was last accessed */
	private long lastAccessedTimestamp;

	/**
	 * JSON serializer/deserializer; marked as transient because the class is
	 * directly passed to the JSON library for deserialization
	 */
	private transient final Json json = new Json();

	/**
	 * Default constructor for JSON deserialization.
	 */
	public UserProfile() {
		this.mapStates = new HashMap<>();
		this.createdTimestamp = System.currentTimeMillis();
		this.lastAccessedTimestamp = this.createdTimestamp;
	}

	/**
	 * Constructs a new UserProfile with the specified username.
	 *
	 * @param username The username for this profile
	 */
	public UserProfile(String username) {
		this();
		this.username = username;
	}

	/**
	 * Gets the username for this profile.
	 *
	 * @return The username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Sets the username for this profile.
	 *
	 * @param username The new username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Gets the map states for all maps.
	 *
	 * @return A map of map names to their corresponding MapState objects
	 */
	public Map<String, MapState> getMapStates() {
		return mapStates;
	}

	/**
	 * Sets the map states for this profile.
	 *
	 * @param mapStates The map of map names to MapState objects
	 */
	public void setMapStates(Map<String, MapState> mapStates) {
		this.mapStates = mapStates != null ? mapStates : new HashMap<>();
	}

	/**
	 * Gets the creation timestamp for this profile.
	 *
	 * @return The timestamp when this profile was created (milliseconds since
	 *         epoch)
	 */
	public long getCreatedTimestamp() {
		return createdTimestamp;
	}

	/**
	 * Sets the creation timestamp for this profile.
	 *
	 * @param createdTimestamp The creation timestamp in milliseconds since epoch
	 */
	public void setCreatedTimestamp(long createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	/**
	 * Gets the last accessed timestamp for this profile.
	 *
	 * @return The timestamp when this profile was last accessed (milliseconds since
	 *         epoch)
	 */
	public long getLastAccessedTimestamp() {
		return lastAccessedTimestamp;
	}

	/**
	 * Sets the last accessed timestamp for this profile.
	 *
	 * @param lastAccessedTimestamp The last accessed timestamp in milliseconds
	 *                              since epoch
	 */
	public void setLastAccessedTimestamp(long lastAccessedTimestamp) {
		this.lastAccessedTimestamp = lastAccessedTimestamp;
	}

	/**
	 * Gets the MapState for a specific map, creating it if it doesn't exist.
	 *
	 * @param mapName The name of the map
	 * @return The MapState for the specified map
	 */
	public MapState getMapState(String mapName) {
		return mapStates.computeIfAbsent(mapName, k -> new MapState());
	}

	/**
	 * Updates the high score for a specific map if the new score is higher.
	 *
	 * @param mapName The name of the map
	 * @param score   The score to update with
	 * @return true if the high score was updated, false otherwise
	 */
	public boolean updateHighScore(String mapName, int score) {
		MapState mapState = getMapState(mapName);
		boolean updated = mapState.updateHighScore(score);
		if (updated) {
			mapState.setLastPlayedTimestamp(System.currentTimeMillis());
		}
		return updated;
	}

	/**
	 * Saves game data for a specific map.
	 *
	 * @param mapName  The name of the map
	 * @param saveData The save data to store
	 */
	public void saveGameData(String mapName, SavedGameState saveData) {
		MapState mapState = getMapState(mapName);
		mapState.setLastSaveData(json.toJson(saveData));
		mapState.setLastPlayedTimestamp(System.currentTimeMillis());
	}

	/**
	 * Loads game data for a specific map.
	 *
	 * @param mapName The name of the map
	 * @return The loaded game data, or null if no save data exists
	 */
	public SavedGameState loadGameData(String mapName) {
		MapState mapState = getMapState(mapName);
		String saveData = mapState.getLastSaveData();
		if (saveData == null || saveData.trim().isEmpty()) {
			return null;
		}

		return json.fromJson(SavedGameState.class, saveData);
	}

	/**
	 * Marks a map as completed.
	 *
	 * @param mapName The name of the map to mark as completed
	 */
	public void markMapCompleted(String mapName) {
		MapState mapState = getMapState(mapName);
		mapState.setCompleted(true);
		mapState.setLastPlayedTimestamp(System.currentTimeMillis());
	}

	/**
	 * Updates the last accessed timestamp to the current time.
	 * Should be called whenever the profile is accessed or used.
	 */
	public void updateLastAccessed() {
		this.lastAccessedTimestamp = System.currentTimeMillis();
	}

	/**
	 * Checks if this profile has any saved game data for any map.
	 *
	 * @return true if any map has save data, false otherwise
	 */
	public boolean hasAnySaveData() {
		return mapStates.values().stream().anyMatch(MapState::hasSaveData);
	}
}