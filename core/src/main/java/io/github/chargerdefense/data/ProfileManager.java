package io.github.chargerdefense.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the persistence of user profiles to and from JSON files.
 * Each profile is stored as a separate JSON file in the profiles directory.
 */
public class ProfileManager {
	/** The directory where profile files are stored */
	private static final String PROFILES_DIRECTORY = "profiles/";
	/** The file extension for profile files */
	private static final String PROFILE_EXTENSION = ".json";

	/** JSON parser instance */
	private final Json json;

	/**
	 * Constructs a new ProfileManager and initializes the JSON parser.
	 */
	public ProfileManager() {
		this.json = new Json();
		json.setUsePrototypes(false);
		json.setIgnoreUnknownFields(true);

		ensureProfilesDirectory();
	}

	/**
	 * Ensures that the profiles directory exists, creating it if necessary.
	 */
	private void ensureProfilesDirectory() {
		try {
			FileHandle profilesDir = Gdx.files.local(PROFILES_DIRECTORY);
			if (!profilesDir.exists()) {
				profilesDir.mkdirs();
			}
		} catch (Exception e) {
			Gdx.app.error("ProfileManager", "Failed to create profiles directory", e);
		}
	}

	/**
	 * Saves a user profile to a JSON file.
	 * The file name is based on the username.
	 *
	 * @param profile The user profile to save
	 * @return true if the profile was saved successfully, false otherwise
	 */
	public boolean saveProfile(UserProfile profile) {
		return false;
	}

	/**
	 * Loads a user profile from a JSON file.
	 *
	 * @param username The username of the profile to load
	 * @return The loaded UserProfile, or null if loading failed
	 */
	public UserProfile loadProfile(String username) {
	}

	/**
	 * Creates a new user profile with the specified username.
	 * The profile is automatically saved.
	 *
	 * @param username The username for the new profile
	 * @return The created UserProfile
	 */
	public UserProfile createProfile(String username) {
	}

	/**
	 * Checks if a profile with the specified username exists.
	 *
	 * @param username The username to check
	 * @return true if the profile exists, false otherwise
	 */
	public boolean profileExists(String username) {
		return false;
	}

	/**
	 * Deletes a user profile file.
	 *
	 * @param username The username of the profile to delete
	 * @return true if the profile was deleted successfully, false otherwise
	 */
	public boolean deleteProfile(String username) {
		return false;
	}

	/**
	 * Gets a list of all existing profile usernames.
	 *
	 * @return A list of usernames for all existing profiles
	 */
	public List<String> getAllProfileNames() {
		List<String> profileNames = new ArrayList<>();


		return profileNames;
	}
}