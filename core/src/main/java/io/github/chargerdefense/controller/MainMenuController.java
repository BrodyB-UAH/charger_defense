package io.github.chargerdefense.controller;

import com.badlogic.gdx.Gdx;
import io.github.chargerdefense.model.MainMenuModel;
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
     * Starts the game by transitioning to the game screen.
     * 
     * @param map The game map to use for this game instance.
     */
    public void onPlayClicked(GameMap map) {
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
     * Initiates the process of loading available profiles.
     */
    public void onProfileClicked() {
        model.checkForProfiles();
    }

    /**
     * Handles the quit button click event.
     * Exits the application.
     */
    public void onQuitClicked() {
        Gdx.app.exit();
    }
}
