package io.github.chargerdefense.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import io.github.chargerdefense.model.GameModel;
import io.github.chargerdefense.model.unit.Unit;

/**
 * Controller for the game view that handles user input and coordinates
 * game actions.
 */
public class GameController extends InputAdapter {
    /** The screen state manager for transitioning between different game states */
    private final StateManager stateManager;
    /** The main game model that the controller manipulates */
    private final GameModel game;

    /**
     * Constructs a new GameController with the specified state manager and game
     * model.
     *
     * @param stateManager The state manager for handling screen transitions
     * @param game         The game model to manipulate based on user input
     */
    public GameController(StateManager stateManager, GameModel game) {
        this.stateManager = stateManager;
        this.game = game;
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
            stateManager.showMainMenu();
            return true;
        } else if (keycode == Input.Keys.SPACE) {
            startNextRound();
            return true;
        }
        return false;
    }

    /**
     * Handles touch/mouse down events during gameplay.
     * This method can be extended to handle unit placement and other interactions.
     *
     * @param screenX The x coordinate of the touch/click
     * @param screenY The y coordinate of the touch/click
     * @param pointer The pointer for the event
     * @param button  The button pressed
     * @return true if the input was handled, false otherwise
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // TODO handle touches
        return true;
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
