package io.github.chargerdefense.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import io.github.chargerdefense.controller.GameController;
import io.github.chargerdefense.controller.StateManager;
import io.github.chargerdefense.model.GameModel;

/**
 * The main gameplay screen that renders the game world and handles the game
 * loop. Displays the game map, units, enemies, and other visual elements
 * while coordinating with the game model for state updates.
 */
public class GameView implements Screen {
    /** The main game model containing all game state and logic */
    private final GameModel game;
    /** The sprite batch for efficiently rendering 2D graphics */
    private final SpriteBatch batch;
    /** The controller for processing user input during gameplay */
    private final GameController controller;

    /**
     * Constructs a new GameView with the specified state manager and game model.
     *
     * @param gameManager The state manager for accessing controllers
     * @param game        The game model containing the current game state
     */
    public GameView(StateManager gameManager, GameModel game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.controller = gameManager.getGameController();
    }

    /**
     * Called when this screen becomes the current screen.
     * Sets up the input processor to handle user input during gameplay.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(controller);
    }

    /**
     * Renders the game screen and updates the game state.
     * Updates the game model, clears the screen, and renders all game elements.
     *
     * @param delta The time in seconds since the last render
     */
    @Override
    public void render(float delta) {
        controller.update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        // TODO render game elements
        batch.end();
    }

    /**
     * Called when the screen is resized.
     *
     * @param width  The new screen width
     * @param height The new screen height
     */
    @Override
    public void resize(int width, int height) {
    }

    /**
     * Called when the application is paused.
     */
    @Override
    public void pause() {
    }

    /**
     * Called when the application is resumed from pause.
     */
    @Override
    public void resume() {
    }

    /**
     * Called when this screen is no longer the current screen.
     */
    @Override
    public void hide() {
    }

    /**
     * Called when this screen should release all resources.
     */
    @Override
    public void dispose() {
        batch.dispose();
    }
}
