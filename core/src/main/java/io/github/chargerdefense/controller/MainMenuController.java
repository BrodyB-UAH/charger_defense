package io.github.chargerdefense.controller;

import com.badlogic.gdx.Gdx;
import io.github.chargerdefense.model.MainMenuModel;
import io.github.chargerdefense.model.MapState;
import io.github.chargerdefense.model.map.GameMap;

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
        stateManager.startGame(map);
    }

    /**
     * Handles loading an existing save from map selection.
     * Loads the game state and starts the game.
     *
     * @param map      The game map
     * @param mapState The state of the map to load
     */
    public void onLoadSaveClicked(GameMap map, MapState mapState) {
        // TODO start game with save data
        stateManager.startGame(map);
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
