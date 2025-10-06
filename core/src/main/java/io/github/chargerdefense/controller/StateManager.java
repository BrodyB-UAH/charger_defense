package io.github.chargerdefense.controller;

import com.badlogic.gdx.Game;

import io.github.chargerdefense.model.GameModel;
import io.github.chargerdefense.model.MainMenuModel;
import io.github.chargerdefense.model.Round;
import io.github.chargerdefense.model.map.GameMap;
import io.github.chargerdefense.view.GameView;
import io.github.chargerdefense.view.MainMenuView;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages overall application state and transitions between different game
 * screens.
 * This class acts as the central coordinator by managing models, views, and
 * controllers for different game states.
 */
public class StateManager {
    /** The main libGDX Game instance for screen management */
    private final Game game;
    /** The model for main menu data and state */
    private final MainMenuModel mainMenuModel;
    /** The controller for main menu interactions */
    private final MainMenuController mainMenuController;
    /** The controller for gameplay interactions, created when game starts */
    private GameController gameController;

    /**
     * Creates a new StateManager instance and initializes the main menu components.
     * 
     * @param game The main libGDX Game instance for managing screens
     */
    public StateManager(Game game) {
        this.game = game;
        this.mainMenuModel = new MainMenuModel();
        this.mainMenuController = new MainMenuController(this, mainMenuModel);
    }

    /**
     * Starts the game and transitions to the game MVC.
     * 
     * @param map The game map to use for this game instance.
     */
    public void startGame(GameMap map) {
        // TODO define rounds somewhere else
        List<Round> rounds = new ArrayList<>();
        GameModel gameModel = new GameModel(10, 100, map,
                rounds);

        this.gameController = new GameController(this, gameModel);

        game.setScreen(new GameView(this, gameModel));
    }

    /**
     * Transitions to the main menu screen.
     * Creates and sets the main menu view as the active screen.
     */
    public void showMainMenu() {
        game.setScreen(new MainMenuView(this));
    }

    /**
     * Gets the main menu controller. Used in the main menu view to handle user
     * actions.
     * 
     * @return The main menu controller.
     */
    public MainMenuController getMainMenuController() {
        return mainMenuController;
    }

    /**
     * Gets the game controller. Used in the game view to handle user actions.
     * 
     * @return The game controller.
     */
    public GameController getGameController() {
        return gameController;
    }

    /**
     * Gets the main menu model. Used in the main menu view to observe data changes.
     * 
     * @return The main menu model.
     */
    public MainMenuModel getMainMenuModel() {
        return mainMenuModel;
    }
}
