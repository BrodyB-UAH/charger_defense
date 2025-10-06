package io.github.chargerdefense;

import com.badlogic.gdx.Game;
import io.github.chargerdefense.controller.StateManager;

/**
 * Extends libGDX's Game class to manage the overall application
 * lifecycle and coordinates with the StateManager to handle different game
 * screens.
 */
public class Main extends Game {
    /**
     * The state manager responsible for coordinating between different game screens
     */
    private StateManager stateManager;

    /**
     * Called when the application is created.
     * Initializes the state manager and shows the main menu as the starting screen.
     */
    @Override
    public void create() {
        stateManager = new StateManager(this);
        stateManager.showMainMenu();
    }
}
