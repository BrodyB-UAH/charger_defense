package io.github.chargerdefense.controller;

import com.badlogic.gdx.Game;

import io.github.chargerdefense.data.game.SavedGameState;
import io.github.chargerdefense.model.GameModel;
import io.github.chargerdefense.model.MainMenuModel;
import io.github.chargerdefense.model.Round;
import io.github.chargerdefense.model.enemy.CamoEnemy;
import io.github.chargerdefense.model.enemy.NormalEnemy;
import io.github.chargerdefense.model.map.GameMap;
import io.github.chargerdefense.view.GameView;
import io.github.chargerdefense.view.MainMenuView;
import io.github.chargerdefense.view.MapSelectionView;
import io.github.chargerdefense.view.ProfileSelectionView;

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
     * Generates a list of rounds for the game.
     * Each round contains a predefined set of enemies.
     * 
     * @return A list of Round objects representing the game's rounds.
     */
    public List<Round> generateRounds() {
        return new ArrayList<>(
                List.of(
                        new Round(List.of(
                                new NormalEnemy(), new NormalEnemy(), new NormalEnemy(), new NormalEnemy(),
                                new NormalEnemy())),
                        new Round(List.of(
                                new CamoEnemy(), new CamoEnemy(), new CamoEnemy(), new CamoEnemy())),
                        new Round(List.of(new CamoEnemy(), new NormalEnemy(), new CamoEnemy(), new NormalEnemy(),
                                new CamoEnemy(), new NormalEnemy(), new CamoEnemy(), new NormalEnemy())))

        );
    }

    /**
     * Starts the game and transitions to the game MVC.
     * 
     * @param map       The game map to use for this game instance.
     * @param savedGame The saved game state to load, or null to start a new game.
     */
    public void startGame(GameMap map, SavedGameState savedGame) {
        GameModel gameModel;
        if (savedGame != null) {
            gameModel = new GameModel(savedGame, map, generateRounds());
        } else {
            gameModel = new GameModel(3, 250, map, generateRounds());
        }

        this.gameController = new GameController(this, gameModel, mainMenuModel.getProfileManager());

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
     * Transitions to the profile selection screen.
     * Creates and sets the profile selection view as the active screen.
     */
    public void showProfileSelection() {
        mainMenuModel.checkForProfiles();
        game.setScreen(new ProfileSelectionView(this));
    }

    /**
     * Transitions to the map selection screen.
     * Creates and sets the map selection view as the active screen.
     */
    public void showMapSelection() {
        game.setScreen(new MapSelectionView(this));
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
