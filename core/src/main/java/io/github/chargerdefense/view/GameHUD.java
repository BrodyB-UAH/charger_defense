package io.github.chargerdefense.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.chargerdefense.controller.GameController;
import io.github.chargerdefense.model.GameModel;
import io.github.chargerdefense.model.GameObserver;

/**
 * Represents the heads-up display (HUD) for the game view.
 * Displays player stats, round information, and provides controls for
 * starting rounds and accessing the unit shop.
 */
public class GameHUD implements GameObserver {
	/** The stage for rendering UI elements */
	private final Stage stage;
	/** The skin for UI styling */
	private final Skin skin;
	/** The game model to read state from */
	private final GameModel gameModel;
	/** The game controller for handling user actions */
	private final GameController controller;

	/** Label displaying the player's current currency */
	private Label currencyLabel;
	/** Label displaying the number of lives remaining */
	private Label livesLabel;
	/** Label displaying the current round number */
	private Label roundLabel;
	/** Button to start the next round */
	private TextButton startRoundButton;
	/** Button to return to main menu */
	private TextButton menuButton;

	/** Container for the unit shop panel */
	private Table unitShopPanel;
	/** Button to toggle the unit shop panel */
	private TextButton toggleShopButton;
	/** Flag indicating whether the shop panel is visible */
	private boolean shopPanelVisible = false;

	/** The upgrade menu view for displaying unit upgrade options */
	private final UpgradeMenuView upgradeMenuView;

	/**
	 * Constructs a new GameHUD with the specified game model and controller.
	 *
	 * @param gameModel  The game model containing game state
	 * @param controller The game controller for handling actions
	 */
	public GameHUD(GameModel gameModel, GameController controller) {
		this.gameModel = gameModel;
		this.controller = controller;
		this.stage = new Stage(new ScreenViewport());
		this.skin = new Skin(Gdx.files.internal("uiskin.json"));
		this.upgradeMenuView = new UpgradeMenuView(gameModel, skin);
		initializeUI();

		gameModel.addObserver(this);

		onCurrencyChanged(gameModel.getPlayer().getCurrency());
		onLivesChanged(gameModel.getLives());
		onRoundChanged(gameModel.getRoundManager().getCurrentRoundNumber(),
				gameModel.getRoundManager().getTotalRounds());
		onRoundStateChanged(gameModel.getRoundManager().isRoundInProgress(),
				gameModel.getRoundManager().areAllRoundsCompleted());
	}

	/**
	 * Initializes all UI components and layouts.
	 */
	private void initializeUI() {
		Table topTable = new Table();
		topTable.setFillParent(true);
		topTable.top();
		topTable.pad(10);
		stage.addActor(topTable);

		Label currencyIcon = new Label("$:", skin);
		currencyLabel = new Label("0", skin);
		topTable.add(currencyIcon).padRight(5);
		topTable.add(currencyLabel).padRight(20);

		Label livesIcon = new Label("Lives:", skin);
		livesLabel = new Label("0", skin);
		topTable.add(livesIcon).padRight(5);
		topTable.add(livesLabel).padRight(20);

		Label roundIcon = new Label("Round:", skin);
		roundLabel = new Label("0/0", skin);
		topTable.add(roundIcon).padRight(5);
		topTable.add(roundLabel).padRight(20);

		topTable.add().expandX(); // push buttons to the right

		startRoundButton = new TextButton("Start Round", skin);
		startRoundButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				controller.startNextRound();
			}
		});
		topTable.add(startRoundButton).padRight(10);

		menuButton = new TextButton("Main Menu", skin);
		menuButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				controller.returnToMainMenu();
			}
		});
		topTable.add(menuButton);

		Table bottomTable = new Table();
		bottomTable.setFillParent(true);
		bottomTable.bottom();
		bottomTable.add(upgradeMenuView);
		stage.addActor(bottomTable);

		createUnitShopPanel();
	}

	/**
	 * Creates the collapsible unit shop panel on the right side.
	 */
	private void createUnitShopPanel() {
		toggleShopButton = new TextButton(">", skin);
		toggleShopButton.setPosition(Gdx.graphics.getWidth() - 30, Gdx.graphics.getHeight() / 2);
		toggleShopButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				toggleShopPanel();
			}
		});
		stage.addActor(toggleShopButton);

		unitShopPanel = new Table();
		unitShopPanel.setBackground(skin.getDrawable("default-rect"));
		unitShopPanel.top().right();

		int panelWidth = 250;
		unitShopPanel.setSize(panelWidth, Gdx.graphics.getHeight());
		unitShopPanel.setPosition(Gdx.graphics.getWidth(), 0); // hidden off-screen

		Label shopTitle = new Label("Unit Shop", skin);
		shopTitle.setFontScale(1.2f);
		unitShopPanel.add(shopTitle).padTop(20).padBottom(20).row();

		addUnitButton("Basic Unit", 100, "Basic tower unit");
		addUnitButton("Spike Factory", 150, "Produces spikes on ground");

		stage.addActor(unitShopPanel);
	}

	/**
	 * Adds a unit button to the shop panel.
	 *
	 * @param name        The name of the unit
	 * @param cost        The cost of the unit
	 * @param description The description of the unit's stats
	 */
	private void addUnitButton(String name, int cost, String description) {
		Table unitCard = new Table();
		unitCard.setBackground(skin.getDrawable("default-rect"));
		unitCard.pad(10);

		Label nameLabel = new Label(name, skin);
		nameLabel.setFontScale(1.1f);
		unitCard.add(nameLabel).colspan(2).row();

		Label costLabel = new Label("Cost: $" + cost, skin);
		unitCard.add(costLabel).colspan(2).padTop(5).row();

		Label descLabel = new Label(description, skin);
		descLabel.setFontScale(0.8f);
		unitCard.add(descLabel).colspan(2).padTop(5).row();

		TextButton buyButton = new TextButton("Buy", skin);
		buyButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				controller.selectUnitForPlacement(name);
				toggleShopPanel();
			}
		});
		unitCard.add(buyButton).padTop(10).row();

		unitShopPanel.add(unitCard).width(220).padBottom(10).row();
	}

	/**
	 * Toggles the visibility of the unit shop panel.
	 */
	private void toggleShopPanel() {
		shopPanelVisible = !shopPanelVisible;

		if (shopPanelVisible) {
			// slide in from the right
			unitShopPanel.setPosition(Gdx.graphics.getWidth() - unitShopPanel.getWidth(), 0);
			toggleShopButton.setText("<");
			toggleShopButton.setPosition(Gdx.graphics.getWidth() - unitShopPanel.getWidth() - 30,
					Gdx.graphics.getHeight() / 2);
		} else {
			// slide out to the right
			unitShopPanel.setPosition(Gdx.graphics.getWidth(), 0);
			toggleShopButton.setText(">");
			toggleShopButton.setPosition(Gdx.graphics.getWidth() - 30, Gdx.graphics.getHeight() / 2);
		}
	}

	/**
	 * Updates the HUD display with current game state.
	 * Called each frame to update animations and stage.
	 *
	 * @param delta The time in seconds since the last update
	 */
	public void update(float delta) {
		stage.act(delta);
		upgradeMenuView.update();
	}

	@Override
	public void onCurrencyChanged(int newCurrency) {
		currencyLabel.setText(String.valueOf(newCurrency));
	}

	@Override
	public void onLivesChanged(int newLives) {
		livesLabel.setText(String.valueOf(newLives));
	}

	@Override
	public void onRoundChanged(int currentRound, int totalRounds) {
		roundLabel.setText(currentRound + "/" + totalRounds);
	}

	@Override
	public void onRoundStateChanged(boolean roundInProgress, boolean allRoundsComplete) {
		startRoundButton.setDisabled(roundInProgress || allRoundsComplete);

		if (allRoundsComplete) {
			startRoundButton.setText("Complete!");
		} else if (roundInProgress) {
			startRoundButton.setText("In Progress...");
		} else {
			startRoundButton.setText("Start Round");
		}
	}

	@Override
	public void onGameOver(boolean victory) {
		// TODO display modal
		startRoundButton.setDisabled(true);
		if (victory) {
			startRoundButton.setText("Victory!");
		} else {
			startRoundButton.setText("Game Over");
		}
	}

	/**
	 * Renders the HUD.
	 */
	public void render() {
		stage.getViewport().apply();
		stage.draw();
	}

	/**
	 * Handles window resize events.
	 *
	 * @param width  The new window width
	 * @param height The new window height
	 */
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);

		if (shopPanelVisible) {
			toggleShopButton.setPosition(width - unitShopPanel.getWidth() - 30, height / 2);
			unitShopPanel.setPosition(width - unitShopPanel.getWidth(), 0);
		} else {
			toggleShopButton.setPosition(width - 30, height / 2);
			unitShopPanel.setPosition(width, 0);
		}

		unitShopPanel.setHeight(height);
	}

	/**
	 * Gets the stage for input processing.
	 *
	 * @return The UI stage
	 */
	public Stage getStage() {
		return stage;
	}

	/**
	 * Disposes of resources used by the HUD.
	 */
	public void dispose() {
		gameModel.removeObserver(this);
		upgradeMenuView.dispose();
		stage.dispose();
		skin.dispose();
	}
}
