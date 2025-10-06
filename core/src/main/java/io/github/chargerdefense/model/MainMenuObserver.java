package io.github.chargerdefense.model;

import java.util.List;

/**
 * Observer interface for receiving notifications about main menu model changes.
 */
public interface MainMenuObserver {
    /**
     * Called when the list of available profiles has been loaded.
     *
     * @param profiles The list of available profile names
     */
    void onProfilesLoaded(List<String> profiles);

    /**
     * Called when the active profile changes.
     *
     * @param activeProfile The name of the newly active profile, or null if none
     *                      selected
     */
    void onActiveProfileChanged(String activeProfile);

    /**
     * Called when the loading state changes.
     *
     * @param isLoading true if profiles are currently being loaded, false otherwise
     */
    void onLoadingStateChanged(boolean isLoading);
}
