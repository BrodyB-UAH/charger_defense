package io.github.chargerdefense.controller;

import com.badlogic.gdx.Gdx;
import io.github.chargerdefense.data.ProfileManager;
import io.github.chargerdefense.data.UserProfile;
import io.github.chargerdefense.data.game.SavedGameState;
import io.github.chargerdefense.model.MainMenuModel;
import io.github.chargerdefense.model.MapState;
import io.github.chargerdefense.model.map.GameMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for the main menu that handles user interactions and coordinates
 * between the main menu view and model.
 */
public class MainMenuController {

    /** The state manager for handling screen transitions */
    private final StateManager stateManager;
    /** The main menu model for managing profile data and state */
    private final MainMenuModel model;

    /**
     * Constructs a new MainMenuController with the specified state manager and
     * model.
     *
     * @param stateManager The state manager for transitioning between screens
     * @param model        The main menu model for managing menu state
     */
    public MainMenuController(StateManager stateManager, MainMenuModel model) {
        this.stateManager = stateManager;
        this.model = model;
    }

    /**
     * Gets the main menu model for managing menu state
     * 
     * @return The MainMenuModel instance
     */
    public MainMenuModel getMainMenuModel() {
        return model;
    }

    /**
     * Handles the play button click event.
     * Transitions to the map selection screen.
     */
    public void onPlayClicked() {
        stateManager.showMapSelection();
    }

    /**
     * Handles the new game button click from map selection.
     * Starts a new game with the selected map.
     *
     * @param map The selected game map
     */
    public void onNewGameClicked(GameMap map) {
        stateManager.startGame(map, null);
    }

    /**
     * Loads existing saves for the specified map into the map selection model.
     * 
     * @param mapName The name of the map to load saves for
     */
    public void loadSavesForMap(String mapName) {
        List<SavedGameState> saves = new ArrayList<>();
        // TODO load existing saves
        model.setExistingSaves(saves);
    }

    /**
     * Handles profile selection by updating the model's active profile.
     *
     * @param profile The name of the selected profile
     */
    public void onProfileSelected(String profile) {
        model.setActiveProfile(profile);
    }

    /**
     * Handles the profile button click event.
     * Transitions to the profile selection screen.
     */
    public void onProfileSelectionClicked() {
        stateManager.showProfileSelection();
    }

    /**
     * Handles back button clicks.
     * Returns to the main menu.
     */
    public void onBackClicked() {
        stateManager.showMainMenu();
    }

    /**
     * Handles the quit button click event.
     * Exits the application.
     */
    public void onQuitClicked() {
        Gdx.app.exit();
    }
}