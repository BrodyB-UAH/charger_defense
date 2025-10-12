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
		if (profile == null || profile.getUsername() == null || profile.getUsername().trim().isEmpty()) {
			Gdx.app.error("ProfileManager", "Cannot save profile: invalid profile or username");
			return false;
		}

		try {
			String filename = profile.getUsername() + PROFILE_EXTENSION;
			FileHandle profileFile = Gdx.files.local(PROFILES_DIRECTORY + filename);

			profile.updateLastAccessed();

			String jsonData = json.prettyPrint(profile);
			profileFile.writeString(jsonData, false);

			Gdx.app.log("ProfileManager", "Successfully saved profile: " + profile.getUsername());
			return true;
		} catch (Exception e) {
			Gdx.app.error("ProfileManager", "Failed to save profile: " + profile.getUsername(), e);
			return false;
		}
	}

	/**
	 * Loads a user profile from a JSON file.
	 *
	 * @param username The username of the profile to load
	 * @return The loaded UserProfile, or null if loading failed
	 */
	public UserProfile loadProfile(String username) {
		if (username == null || username.trim().isEmpty()) {
			Gdx.app.error("ProfileManager", "Cannot load profile: invalid username");
			return null;
		}

		try {
			String filename = username + PROFILE_EXTENSION;
			FileHandle profileFile = Gdx.files.local(PROFILES_DIRECTORY + filename);

			if (!profileFile.exists()) {
				Gdx.app.log("ProfileManager", "Profile file does not exist: " + filename);
				return null;
			}

			String jsonData = profileFile.readString();
			UserProfile profile = json.fromJson(UserProfile.class, jsonData);

			if (profile != null) {
				profile.updateLastAccessed();
				Gdx.app.log("ProfileManager", "Successfully loaded profile: " + username);
			}

			return profile;
		} catch (Exception e) {
			Gdx.app.error("ProfileManager", "Failed to parse or load profile: " + username, e);
			return null;
		}
	}

	/**
	 * Creates a new user profile with the specified username.
	 * The profile is automatically saved.
	 *
	 * @param username The username for the new profile
	 * @return The created UserProfile
	 */
	public UserProfile createProfile(String username) {
		if (username == null || username.trim().isEmpty()) {
			Gdx.app.error("ProfileManager", "Cannot create profile: invalid username");
			return null;
		}

		if (profileExists(username)) {
			Gdx.app.error("ProfileManager", "Profile already exists: " + username);
			return null;
		}

		UserProfile profile = new UserProfile(username.trim());
		if (saveProfile(profile)) {
			Gdx.app.log("ProfileManager", "Successfully created new profile: " + username);
			return profile;
		} else {
			Gdx.app.error("ProfileManager", "Failed to save new profile: " + username);
			return null;
		}
	}

	/**
	 * Checks if a profile with the specified username exists.
	 *
	 * @param username The username to check
	 * @return true if the profile exists, false otherwise
	 */
	public boolean profileExists(String username) {
		if (username == null || username.trim().isEmpty()) {
			return false;
		}

		String filename = username + PROFILE_EXTENSION;
		FileHandle profileFile = Gdx.files.local(PROFILES_DIRECTORY + filename);
		return profileFile.exists();
	}

	/**
	 * Deletes a user profile file.
	 *
	 * @param username The username of the profile to delete
	 * @return true if the profile was deleted successfully, false otherwise
	 */
	public boolean deleteProfile(String username) {
		if (username == null || username.trim().isEmpty()) {
			Gdx.app.error("ProfileManager", "Cannot delete profile: invalid username");
			return false;
		}

		try {
			String filename = username + PROFILE_EXTENSION;
			FileHandle profileFile = Gdx.files.local(PROFILES_DIRECTORY + filename);

			if (!profileFile.exists()) {
				Gdx.app.log("ProfileManager", "Profile file does not exist, nothing to delete: " + filename);
				return true;
			}

			boolean deleted = profileFile.delete();
			if (deleted) {
				Gdx.app.log("ProfileManager", "Successfully deleted profile: " + username);
			} else {
				Gdx.app.error("ProfileManager", "Failed to delete profile file: " + filename);
			}
			return deleted;
		} catch (Exception e) {
			Gdx.app.error("ProfileManager", "Exception while deleting profile: " + username, e);
			return false;
		}
	}

	/**
	 * Gets a list of all existing profile usernames.
	 *
	 * @return A list of usernames for all existing profiles
	 */
	public List<String> getAllProfileNames() {
		List<String> profileNames = new ArrayList<>();

		try {
			FileHandle profilesDir = Gdx.files.local(PROFILES_DIRECTORY);
			if (!profilesDir.exists()) {
				return profileNames;
			}

			FileHandle[] profileFiles = profilesDir.list(PROFILE_EXTENSION);
			for (FileHandle file : profileFiles) {
				try {
					String filename = file.nameWithoutExtension();
					profileNames.add(filename);
				} catch (Exception e) {
					Gdx.app.error("ProfileManager", "Error processing profile file: " + file.name(), e);
				}
			}

			Gdx.app.log("ProfileManager", "Found " + profileNames.size() + " profile(s)");
		} catch (Exception e) {
			Gdx.app.error("ProfileManager", "Failed to list profile files", e);
		}

		return profileNames;
	}
}