package io.github.chargerdefense.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import io.github.chargerdefense.data.ProfileManager;
import io.github.chargerdefense.data.UserProfile;
import io.github.chargerdefense.data.game.SavedGameState;
import io.github.chargerdefense.data.game.SavedUnit;
import io.github.chargerdefense.model.GameModel;
import io.github.chargerdefense.model.unit.Unit;
import io.github.chargerdefense.model.unit.BasicUnit;

import java.awt.Point;
import java.util.ArrayList;

/**
 * Controller for the game view that handles user input and coordinates
 * game actions.
 */
public class GameController extends InputAdapter {
    /** The screen state manager for transitioning between different game states */
    private final StateManager stateManager;
    /** The main game model that the controller manipulates */
    private final GameModel game;
    /** The user profile manager */
    private final ProfileManager profileManager;
    /** The currently selected unit type for placement, or null if none selected */
    private String selectedUnitType;
    /** The current mouse position in game coordinates */
    private Point.Float mousePosition;
    /** The unit being previewed for placement */
    private Unit previewUnit;

    /**
     * Constructs a new GameController with the specified state manager and game
     * model.
     *
     * @param stateManager   The state manager for handling screen transitions
     * @param game           The game model to manipulate based on user input
     * @param profileManager The profile manager for saving/loading game state
     */
    public GameController(StateManager stateManager, GameModel game, ProfileManager profileManager) {
        this.stateManager = stateManager;
        this.game = game;
        this.profileManager = profileManager;
        this.mousePosition = new Point.Float(0, 0);
    }

    /**
     * Handles key press events during gameplay.
     * - ESC key to return to the main menu
     * - SPACE key to start the next round
     *
     * @param keycode The key code of the pressed key
     * @return true if the key was handled, false otherwise
     */
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE) {
            returnToMainMenu();
            return true;
        } else if (keycode == Input.Keys.SPACE) {
            startNextRound();
            return true;
        }
        return false;
    }

    /**
     * Handles touch/mouse down events during gameplay.
     * 
     * @param gameX   The x coordinate in game coordinates
     * @param gameY   The y coordinate in game coordinates
     * @param pointer The pointer for the event
     * @param button  The button pressed
     * @return true if the input was handled, false otherwise
     */
    @Override
    public boolean touchDown(int gameX, int gameY, int pointer, int button) {
        if (button == 2) {
            clearSelectedUnit();
            return true;
        }

        if (selectedUnitType != null && previewUnit != null) {
            if (purchaseUnit(previewUnit, gameX, gameY)) {
                clearSelectedUnit();
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int gameX, int gameY) {
        if (selectedUnitType != null) {
            mousePosition.x = gameX;
            mousePosition.y = gameY;
            return true;
        }
        return false;
    }

    /**
     * Handles user action of purchasing a unit at the specified coordinates.
     * Delegates to the game model to handle the business logic.
     *
     * @param unit The unit to purchase and place
     * @param x    The x-coordinate for placement
     * @param y    The y-coordinate for placement
     * @return true if the unit was successfully purchased and placed
     */
    public boolean purchaseUnit(Unit unit, int x, int y) {
        return game.purchaseUnit(unit, x, y);
    }

    /**
     * Signals to start the next round of enemies.
     * This method is typically called when the player is ready to proceed.
     */
    public void startNextRound() {
        game.startNextRound();
    }

    /**
     * Returns to the main menu screen.
     * Transitions the game state back to the main menu.
     */
    public void returnToMainMenu() {
        UserProfile activeProfile = profileManager.getActiveProfile();
        if (activeProfile != null) {
            SavedGameState savedGame = new SavedGameState();
            savedGame.lives = game.getLives();
            savedGame.currency = game.getPlayer().getCurrency();
            savedGame.score = game.getPlayer().getScore();
            savedGame.enemiesDefeated = game.getPlayer().getEnemiesDefeated();
            savedGame.unitsPurchased = game.getPlayer().getUnitsPurchased();
            savedGame.currentRoundIndex = game.getRoundManager().getCurrentRoundNumber() - 2; // adjust for 0-based
                                                                                              // index
            savedGame.placedUnits = new ArrayList<>();
            for (Unit unit : game.getMap().getPlacedUnits()) {
                SavedUnit savedUnit = new SavedUnit();
                savedUnit.type = unit.getClass().getSimpleName();
                savedUnit.x = unit.getPosition().x;
                savedUnit.y = unit.getPosition().y;
                savedGame.placedUnits.add(savedUnit);
            }

            activeProfile.saveGameData(game.getMap().getMapName(), savedGame);
            profileManager.saveProfile(activeProfile);
        }

        stateManager.showMainMenu();
    }

    /**
     * Selects a unit type for placement on the map.
     * The selected unit will be placed when the player clicks on the map.
     *
     * @param unitType The name/type of the unit to place
     */
    public void selectUnitForPlacement(String unitType) {
        Unit tempUnit = createUnitFromType(unitType);
        if (tempUnit != null && game.getPlayer().canAfford(tempUnit.getCost())) {
            this.selectedUnitType = unitType;
            this.previewUnit = tempUnit;
        }
    }

    /**
     * Creates a unit instance from the unit type name.
     *
     * @param unitType The name/type of the unit
     * @return A new unit instance, or null if the type is invalid
     */
    private Unit createUnitFromType(String unitType) {
        switch (unitType) {
            case "Basic Unit":
                return new BasicUnit();
            default:
                return null;
        }
    }

    /**
     * Gets the currently selected unit type for placement.
     *
     * @return The selected unit type, or null if none is selected
     */
    public String getSelectedUnitType() {
        return selectedUnitType;
    }

    /**
     * Gets the current mouse position in game coordinates.
     *
     * @return The mouse position
     */
    public Point.Float getMousePosition() {
        return mousePosition;
    }

    /**
     * Gets the preview unit (for rendering).
     *
     * @return The unit being previewed, or null if none selected
     */
    public Unit getPreviewUnit() {
        return previewUnit;
    }

    /**
     * Clears the currently selected unit type.
     */
    public void clearSelectedUnit() {
        this.selectedUnitType = null;
        this.previewUnit = null;
    }

    /**
     * Updates the game logic and state.
     * Handles enemy movement, unit attacks, collision detection, and game rule
     * enforcement.
     *
     * @param deltaTime The time elapsed since the last update (in seconds)
     */
    public void update(float deltaTime) {
        if (!game.isGameOver()) {
            game.update(deltaTime);
        }
    }

    /**
     * Gets the current game model.
     *
     * @return The game model being controlled
     */
    public GameModel getGame() {
        return game;
    }
}