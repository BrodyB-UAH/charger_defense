package io.github.chargerdefense.view;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.graphics.Color;

/**
 * Displays the game over message when the player wins or loses.
 */
public class GameOverDisplay {
	/** The stage to add the game over label to */
	private final Stage stage;
	/** The skin for UI styling */
	private final Skin skin;
	/** Container for the game over message */
	private Table gameOverContainer;
	/** Label displaying the game over message */
	private Label gameOverLabel;

	/**
	 * Constructs a new GameOverDisplay.
	 *
	 * @param stage The stage to add UI elements to
	 * @param skin  The skin for UI styling
	 */
	public GameOverDisplay(Stage stage, Skin skin) {
		this.stage = stage;
		this.skin = skin;
		initializeUI();
	}

	/**
	 * Initializes the game over UI container and label.
	 */
	private void initializeUI() {
		gameOverContainer = new Table();
		gameOverContainer.setFillParent(true);
		gameOverContainer.center();

		Table backgroundTable = new Table();
		backgroundTable.setBackground(skin.getDrawable("default-rect"));
		backgroundTable.pad(30);

		gameOverLabel = new Label("", skin);
		gameOverLabel.setFontScale(2.0f);
		gameOverLabel.setWrap(true);

		backgroundTable.add(gameOverLabel).width(600).center();
		gameOverContainer.add(backgroundTable);

		stage.addActor(gameOverContainer);
		gameOverContainer.setVisible(false); // Hidden by default
	}

	/**
	 * Shows the victory message.
	 */
	public void showVictory() {
		gameOverLabel.setText("VICTORY!\n\nYou have successfully defended against all waves!");
		gameOverLabel.setColor(Color.GREEN);
		gameOverContainer.setVisible(true);
	}

	/**
	 * Shows the defeat message.
	 */
	public void showDefeat() {
		gameOverLabel.setText("DEFEAT!\n\nYour defenses have been overrun!");
		gameOverLabel.setColor(Color.RED);
		gameOverContainer.setVisible(true);
	}

	/**
	 * Hides the game over display.
	 */
	public void hide() {
		gameOverContainer.setVisible(false);
	}
}
