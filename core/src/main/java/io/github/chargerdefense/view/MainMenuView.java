package io.github.chargerdefense.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.chargerdefense.controller.StateManager;
import io.github.chargerdefense.controller.MainMenuController;
import io.github.chargerdefense.model.MainMenuModel;
import io.github.chargerdefense.model.MainMenuObserver;

import java.util.List;

/**
 * The main menu screen of the game that provides the user interface for
 * profile selection, game start, and application exit.
 * Implements Screen from libGDX for screen lifecycle management and
 * MainMenuObserver to receive notifications about model changes.
 */
public class MainMenuView implements Screen, MainMenuObserver {
    /** The controller for handling main menu user interactions and logic */
    private final MainMenuController controller;
    /** The model for managing main menu state and profile data */
    private final MainMenuModel model;
    /** The libGDX stage for organizing and rendering UI elements */
    private Stage stage;
    /** The skin containing UI styling and texture information */
    private Skin skin;
    /** The label for displaying the currently selected profile name */
    private Label profileLabel;
    /** The button for starting the game, disabled when no profile is selected */
    private TextButton playButton;

    /**
     * Constructs a new MainMenuView with the specified state manager.
     * Registers itself as an observer to the main menu model for state updates.
     *
     * @param gameManager The state manager containing controllers and models
     */
    public MainMenuView(StateManager gameManager) {
        this.controller = gameManager.getMainMenuController();
        this.model = gameManager.getMainMenuModel();
        this.model.addObserver(this);
    }

    /**
     * Called when this screen becomes the current screen.
     * Initializes the UI components and sets up event handlers.
     */
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label titleLabel = new Label("Charger Defense", skin);
        table.add(titleLabel).padBottom(40).row();

        profileLabel = new Label("No profile selected", skin);
        table.add(profileLabel).padBottom(20).row();

        playButton = new TextButton("Play", skin);
        playButton.setDisabled(true);
        table.add(playButton).fillX().uniformX().padBottom(10).row();


        TextButton quitButton = new TextButton("Quit to Desktop", skin);
        table.add(quitButton).fillX().uniformX().row();


        quitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                controller.onQuitClicked();
            }
        });
    }

    /**
     * Renders the main menu screen.
     * Clears the screen with a dark gray color and draws all UI elements.
     *
     * @param delta The time in seconds since the last render
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    /**
     * Called when the screen is resized.
     * Updates the viewport to maintain proper UI scaling.
     *
     * @param width  The new screen width
     * @param height The new screen height
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
     * Removes the observer, disposes of the stage and skin to prevent memory leaks.
     */
    @Override
    public void dispose() {
        model.removeObserver(this);
        stage.dispose();
        skin.dispose();
    }

    /**
     * Called when profiles have been loaded from storage.
     *
     * @param profiles The list of available profile names
     */
    @Override
    public void onProfilesLoaded(List<String> profiles) {
    }

    /**
     * Called when the active profile changes.
     * Updates the profile label text and enables/disables the play button
     * accordingly.
     *
     * @param activeProfile The name of the newly active profile, or null if none
     *                      selected
     */
    @Override
    public void onActiveProfileChanged(String activeProfile) {
        if (activeProfile != null) {
            profileLabel.setText("Profile: " + activeProfile);
            playButton.setDisabled(false);
        } else {
            profileLabel.setText("No profile selected");
            playButton.setDisabled(true);
        }
    }

    /**
     * Called when the loading state changes.
     * Displays a loading message while profiles are being loaded.
     *
     * @param isLoading true if profiles are currently being loaded, false otherwise
     */
    @Override
    public void onLoadingStateChanged(boolean isLoading) {
    }
}
