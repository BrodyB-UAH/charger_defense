package io.github.chargerdefense.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.InputMultiplexer;

import io.github.chargerdefense.GameConstants;
import io.github.chargerdefense.assets.Assets;
import io.github.chargerdefense.controller.GameController;
import io.github.chargerdefense.controller.StateManager;
import io.github.chargerdefense.model.GameModel;
import io.github.chargerdefense.model.Projectile;
import io.github.chargerdefense.model.enemy.Enemy;
import io.github.chargerdefense.model.unit.Unit;

import java.util.List;

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
    /** The shape renderer for drawing lines and geometric shapes */
    private final ShapeRenderer shapeRenderer;
    /** The controller for processing user input during gameplay */
    private final GameController controller;
    /** The heads-up display showing game stats and controls */
    private final GameHUD hud;
    /** The camera for viewing the game world */
    private final OrthographicCamera camera;
    /** The viewport for handling screen resizing with proper aspect ratio */
    private final Viewport viewport;

    /**
     * Constructs a new GameView with the specified state manager and game model.
     *
     * @param gameManager The state manager for accessing controllers
     * @param game        The game model containing the current game state
     */
    public GameView(StateManager gameManager, GameModel game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();
        this.controller = gameManager.getGameController();
        this.hud = new GameHUD(game, controller);

        // for scaling
        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT);
        this.viewport = new FitViewport(GameConstants.GAME_WIDTH, GameConstants.GAME_HEIGHT, camera);

        // Load assets
        loadAssets();
    }

    /**
     * Loads all game assets including textures and sprites.
     */
    private void loadAssets() {
        Assets.getInstance().loadAssets();
    }

    /**
     * Called when this screen becomes the current screen.
     * Sets up the input processor to handle user input during gameplay.
     */
    @Override
    public void show() {
        // Set tutorial manager in controller so it can notify on unit placement
        controller.setTutorialManager(hud.getTutorialManager());

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(hud.getStage());

        // coordinate conversion layer
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 worldCoords = viewport.unproject(new Vector3(screenX, screenY, 0));
                return controller.touchDown((int) worldCoords.x, (int) worldCoords.y, pointer, button);
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                Vector3 worldCoords = viewport.unproject(new Vector3(screenX, screenY, 0));
                return controller.mouseMoved((int) worldCoords.x, (int) worldCoords.y);
            }

            @Override
            public boolean keyDown(int keycode) {
                return controller.keyDown(keycode);
            }
        });

        Gdx.input.setInputProcessor(multiplexer);
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

        Gdx.gl.glClearColor(0.337f, 0.4901f, 0.2745f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        camera.update();

        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);

        game.getMap().renderPath(shapeRenderer);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        List<Unit> units = game.getMap().getPlacedUnits();
        for (Unit unit : units) {
            unit.renderOverlay(shapeRenderer);
        }

        List<Projectile> projectiles = game.getActiveProjectiles();
        for (Projectile projectile : projectiles) {
            projectile.render(shapeRenderer);
        }

        renderPlacementPreview(shapeRenderer);

        shapeRenderer.end();

        // Render unit and enemy sprites
        batch.begin();
        for (Unit unit : units) {
            unit.renderSprite(batch);
        }
        List<Enemy> enemies = game.getRoundManager().getActiveEnemies();
        for (Enemy enemy : enemies) {
            enemy.renderSprite(batch);
        }

        // Draw placement preview sprite (if any) here so we can use SpriteBatch
        // tint/alpha
        Unit previewUnit = controller.getPreviewUnit();
        if (previewUnit != null && controller.getSelectedUnitType() != null) {
            java.awt.Point.Float mousePos = controller.getMousePosition();
            float gameX = mousePos.x;
            float gameY = mousePos.y;

            boolean isValid = game.getMap().isPlacementValid((int) gameX, (int) gameY);

            float alpha = isValid ? 0.8f : 0.5f;
            batch.setColor(1f, 1f, 1f, alpha);
            previewUnit.setPosition(new java.awt.Point((int) gameX, (int) gameY));
            previewUnit.renderSprite(batch);
            batch.setColor(1f, 1f, 1f, 1f);
        }
        batch.end();

        // Render enemy health bars
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Enemy enemy : enemies) {
            enemy.renderHealthBar(shapeRenderer);
        }
        shapeRenderer.end();

        // render game HUD on top of game view
        hud.update(delta);
        hud.render();
    }

    /**
     * Renders the unit placement preview circle at the mouse position.
     *
     * @param shapeRenderer The shape renderer to use for drawing
     */
    private void renderPlacementPreview(ShapeRenderer shapeRenderer) {
        Unit previewUnit = controller.getPreviewUnit();
        if (previewUnit != null && controller.getSelectedUnitType() != null) {
            java.awt.Point.Float mousePos = controller.getMousePosition();

            float gameX = mousePos.x;
            float gameY = mousePos.y;

            boolean isValid = game.getMap().isPlacementValid((int) gameX, (int) gameY);

            // range indicator
            if (isValid) {
                shapeRenderer.setColor(0.3f, 0.8f, 0.3f, 0.3f);
            } else {
                shapeRenderer.setColor(0.8f, 0.3f, 0.3f, 0.3f);
            }
            float range = (float) previewUnit.getRange();
            shapeRenderer.circle(gameX, gameY, range);

            // unit preview (sprite will be drawn later during the SpriteBatch phase)
            // We keep only the range indicator here and draw the sprite preview in the
            // batch section so it can be rendered with transparency and the correct sprite.
        }
    }

    /**
     * Called when the screen is resized.
     *
     * @param width  The new screen width
     * @param height The new screen height
     */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        hud.resize(width, height);
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
        shapeRenderer.dispose();
        hud.dispose();
    }
}
