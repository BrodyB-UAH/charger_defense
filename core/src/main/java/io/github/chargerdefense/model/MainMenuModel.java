package io.github.chargerdefense.model;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;

import io.github.chargerdefense.data.ProfileManager;
import io.github.chargerdefense.data.UserProfile;

/**
 * Model class for the main menu that manages profile selection and loading
 * states.
 */
public class MainMenuModel {
    /** The currently active user profile name */
    private String activeProfile;
    /** List of available user profile names */
    private List<String> existingProfiles;
    /** Flag indicating whether profiles are currently being loaded */
    private boolean isLoading;
    /** The currently loaded UserProfile object for the active profile */
    private UserProfile currentUserProfile;

    /** Profile manager for handling JSON file operations */
    private final ProfileManager profileManager;

    /** List of observers that will be notified of model changes */
    private final List<MainMenuObserver> observers = new ArrayList<>();

    /**
     * Constructs a new MainMenuModel with empty profile list and loading disabled.
     * Initializes the profile manager for handling file operations.
     */
    public MainMenuModel() {
        this.existingProfiles = new ArrayList<>();
        this.isLoading = false;
        this.profileManager = new ProfileManager();
        this.currentUserProfile = null;
    }

    /**
     * Adds an observer to be notified of model changes.
     *
     * @param observer The observer to add to the notification list
     */
    public void addObserver(MainMenuObserver observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer from the notification list.
     *
     * @param observer The observer to remove from the notification list
     */
    public void removeObserver(MainMenuObserver observer) {
        observers.remove(observer);
    }

    /**
     * Sets the active profile and loads its data from storage.
     * Notifies all observers of the change.
     *
     * @param profileName The name of the profile to set as active
     */
    public void setActiveProfile(String profileName) {
        this.activeProfile = profileName;

        this.currentUserProfile = profileManager.loadProfile(profileName);
        if (this.currentUserProfile == null) {
            Gdx.app.error("MainMenuModel", "Failed to load profile data for: " + profileName);
        }

        notifyActiveProfileChanged();
    }

    /**
     * Gets the currently active profile name.
     *
     * @return The name of the active profile, or null if none is selected
     */
    public String getActiveProfile() {
        return activeProfile;
    }

    /**
     * Gets the currently loaded user profile object.
     *
     * @return The UserProfile object for the active profile, or null if none is
     *         loaded
     */
    public UserProfile getCurrentUserProfile() {
        return currentUserProfile;
    }

    /**
     * Gets the list of existing profiles.
     *
     * @return A list of available profile names
     */
    public List<String> getExistingProfiles() {
        return existingProfiles;
    }

    /**
     * Checks if profiles are currently being loaded.
     *
     * @return true if profiles are loading, false otherwise
     */
    public boolean isLoading() {
        return isLoading;
    }

    /**
     * Initiates the process of checking for and loading existing profiles.
     */
    public void checkForProfiles() {
        setLoading(true);

        new Thread(() -> {
            try {
                List<String> loadedProfiles = profileManager.getAllProfileNames();
                Gdx.app.postRunnable(() -> {
                    this.existingProfiles = loadedProfiles;
                    setLoading(false);
                    notifyProfilesLoaded();
                });
            } catch (Exception e) {
                Gdx.app.error("MainMenuModel", "Unexpected error while loading profiles", e);
                Gdx.app.postRunnable(() -> {
                    setLoading(false);
                    notifyProfilesLoaded();
                });
            }
        }).start();
    }

    /**
     * Sets the loading state and notifies observers.
     *
     * @param loading true if profiles are being loaded, false otherwise
     */
    private void setLoading(boolean loading) {
        isLoading = loading;
        notifyLoadingStateChanged();
    }

    /**
     * Notifies all observers that profiles have been loaded.
     */
    private void notifyProfilesLoaded() {
        for (MainMenuObserver observer : observers) {
            observer.onProfilesLoaded(existingProfiles);
        }
    }

    /**
     * Notifies all observers that the active profile has changed.
     */
    private void notifyActiveProfileChanged() {
        for (MainMenuObserver observer : observers) {
            observer.onActiveProfileChanged(activeProfile);
        }
    }

    /**
     * Notifies all observers that the loading state has changed.
     */
    private void notifyLoadingStateChanged() {
        for (MainMenuObserver observer : observers) {
            observer.onLoadingStateChanged(isLoading);
        }
    }

    /**
     * Creates a new user profile with the specified username.
     * The profile is automatically saved and added to the existing profiles
     * list.
     *
     * @param username The username for the new profile
     * @return true if the profile was created successfully, false otherwise
     */
    public boolean createNewProfile(String username) {
        if (username == null || username.trim().isEmpty()) {
            Gdx.app.error("MainMenuModel", "Cannot create profile with empty username");
            return false;
        }

        String trimmedUsername = username.trim();

        if (profileManager.profileExists(trimmedUsername)) {
            Gdx.app.error("MainMenuModel", "Profile already exists: " + trimmedUsername);
            return false;
        }

        UserProfile newProfile = profileManager.createProfile(trimmedUsername);
        if (newProfile != null) {
            if (!existingProfiles.contains(trimmedUsername)) {
                existingProfiles.add(trimmedUsername);
                notifyProfilesLoaded();
            }

            setActiveProfile(trimmedUsername);

            Gdx.app.log("MainMenuModel", "Successfully created new profile: " + trimmedUsername);
            return true;
        } else {
            Gdx.app.error("MainMenuModel", "Failed to create profile: " + trimmedUsername);
            return false;
        }
    }

    /**
     * Deletes the specified user profile.
     * If the deleted profile was active, clears the active profile.
     *
     * @param username The username of the profile to delete
     * @return true if the profile was deleted successfully, false otherwise
     */
    public boolean deleteProfile(String username) {
        if (username == null || username.trim().isEmpty()) {
            Gdx.app.error("MainMenuModel", "Cannot delete profile with empty username");
            return false;
        }

        String trimmedUsername = username.trim();
        boolean deleted = profileManager.deleteProfile(trimmedUsername);

        if (deleted) {
            existingProfiles.remove(trimmedUsername);

            if (trimmedUsername.equals(activeProfile)) {
                setActiveProfile(null);
            }

            notifyProfilesLoaded();

            Gdx.app.log("MainMenuModel", "Successfully deleted profile: " + trimmedUsername);
        } else {
            Gdx.app.error("MainMenuModel", "Failed to delete profile: " + trimmedUsername);
        }

        return deleted;
    }

    /**
     * Saves the current user profile data.
     * This should be called whenever profile data is modified.
     *
     * @return true if the profile was saved successfully, false otherwise
     */
    public boolean saveCurrentProfile() {
        if (currentUserProfile == null) {
            Gdx.app.error("MainMenuModel", "No current profile to save");
            return false;
        }

        boolean saved = profileManager.saveProfile(currentUserProfile);
        if (saved) {
            Gdx.app.log("MainMenuModel", "Successfully saved current profile: " + currentUserProfile.getUsername());
        } else {
            Gdx.app.error("MainMenuModel", "Failed to save current profile: " + currentUserProfile.getUsername());
        }

        return saved;
    }

    /**
     * Updates the high score for a specific map in the current profile.
     *
     * @param mapName The name of the map
     * @param score   The score to update with
     * @return true if the high score was updated (new record), false otherwise
     */
    public boolean updateHighScore(String mapName, int score) {
        if (currentUserProfile == null) {
            Gdx.app.error("MainMenuModel", "No active profile to update high score");
            return false;
        }

        boolean updated = currentUserProfile.updateHighScore(mapName, score);
        if (updated) {
            saveCurrentProfile();
            Gdx.app.log("MainMenuModel", "New high score for " + mapName + ": " + score);
        }

        return updated;
    }

    /**
     * Saves game data for a specific map in the current profile.
     *
     * @param mapName  The name of the map
     * @param saveData The save data as a JSON string
     */
    public void saveGameData(String mapName, String saveData) {
        if (currentUserProfile == null) {
            Gdx.app.error("MainMenuModel", "No active profile to save game data");
            return;
        }

        currentUserProfile.saveGameData(mapName, saveData);
        saveCurrentProfile();
        Gdx.app.log("MainMenuModel", "Saved game data for map: " + mapName);
    }
}
