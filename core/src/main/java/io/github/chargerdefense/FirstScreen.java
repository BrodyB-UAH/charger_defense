package io.github.chargerdefense;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

/**
 * First screen of the application. Displayed after the application is created.
 */
public class FirstScreen implements Screen {
    @Override
    public void show() {
        // Prepare your screen here.

        // Example usage demonstrating the API requested by the professor:
        // create a model and HUD, register the HUD as an observer, then set lives to 100.
        GameModel model = new GameModel();
        GameHud hud = new GameHud();
        model.addObserver(hud);

        // This call shows that other code can set lives to 100:
        model.setLives(100);

        // The hud.onLivesChanged(100) will be invoked by the model and the HUD should update accordingly.
        Gdx.app.log("FirstScreen", "Requested setLives(100) on GameModel.");
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1F, 1F, 1F, 1F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height
        // are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a
        // normal size before updating.
        if (width <= 0 || height <= 0)
            return;

        // Resize your screen here. The parameters represent the new window size.
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        // Destroy screen's assets here.
    }
}