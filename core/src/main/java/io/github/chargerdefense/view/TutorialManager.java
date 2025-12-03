package io.github.chargerdefense.view;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.graphics.Color;

/**
 * Manages the tutorial UI sequence for new players.
 * Displays contextual hints at appropriate times during gameplay.
 */
public class TutorialManager {
	/** The stage to add tutorial labels onto */
	private final Stage stage;
	/** The skin for UI styling */
	private final Skin skin;
	/** Container for the tutorial hint */
	private Table tutorialContainer;
	/** Label displaying the tutorial hint text */
	private Label tutorialLabel;
	/** The current tutorial step */
	private TutorialStep currentStep;
	/** Flag to track if tutorial is enabled */
	private boolean tutorialEnabled;

	/**
	 * Enumeration of tutorial steps in sequence.
	 */
	public enum TutorialStep {
		/** Step to prompt placing the first unit */
		PLACE_FIRST_UNIT("Click the '>' button to open the unit shop, then place your first unit!"),
		/** Step to prompt starting the first round */
		START_ROUND("Great! Now click 'Start Round' to begin the first wave of enemies."),
		/** Step to prompt upgrading a unit */
		UPGRADE_UNIT("Excellent! Click on a unit to select it, then upgrade it to make it stronger."),
		/** Tutorial completed */
		COMPLETED("");

		/** The message associated with the tutorial step */
		private final String message;

		/**
		 * Constructs a TutorialStep with the specified message.
		 *
		 * @param message The message for the tutorial step.
		 */
		TutorialStep(String message) {
			this.message = message;
		}

		/**
		 * Gets the message for the tutorial step.
		 *
		 * @return The tutorial step message.
		 */
		public String getMessage() {
			return message;
		}
	}

	/**
	 * Constructs a new TutorialManager.
	 *
	 * @param stage The stage to add tutorial UI elements to
	 * @param skin  The skin for UI styling
	 */
	public TutorialManager(Stage stage, Skin skin) {
		this.stage = stage;
		this.skin = skin;
		this.tutorialEnabled = true;
		this.currentStep = TutorialStep.PLACE_FIRST_UNIT;
		initializeTutorialUI();
	}

	/**
	 * Initializes the tutorial UI container and label.
	 */
	private void initializeTutorialUI() {
		tutorialContainer = new Table();
		tutorialContainer.setFillParent(true);
		tutorialContainer.center();
		tutorialContainer.padBottom(150); // position above bottom UI

		Table backgroundTable = new Table();
		backgroundTable.setBackground(skin.getDrawable("default-rect"));
		backgroundTable.pad(15);

		tutorialLabel = new Label("", skin);
		tutorialLabel.setFontScale(1.3f);
		tutorialLabel.setColor(Color.YELLOW);
		tutorialLabel.setWrap(true);

		backgroundTable.add(tutorialLabel).width(500).center();
		tutorialContainer.add(backgroundTable);

		stage.addActor(tutorialContainer);
		updateTutorialDisplay();
	}

	/**
	 * Updates the tutorial display with the current step's message.
	 */
	private void updateTutorialDisplay() {
		if (!tutorialEnabled || currentStep == TutorialStep.COMPLETED) {
			tutorialContainer.setVisible(false);
		} else {
			tutorialContainer.setVisible(true);
			tutorialLabel.setText(currentStep.getMessage());
		}
	}

	/**
	 * Called when the player opens the unit shop.
	 * Hides the "place first unit" hint.
	 */
	public void onShopOpened() {
		if (currentStep == TutorialStep.PLACE_FIRST_UNIT) {
			tutorialContainer.setVisible(false);
		}
	}

	/**
	 * Called when the player places a unit.
	 * Advances to the "start round" step if on the first step.
	 */
	public void onUnitPlaced() {
		if (currentStep == TutorialStep.PLACE_FIRST_UNIT) {
			currentStep = TutorialStep.START_ROUND;
			updateTutorialDisplay();
		}
	}

	/**
	 * Called when the player starts a round.
	 * Hides the "start round" hint.
	 */
	public void onRoundStarted() {
		if (currentStep == TutorialStep.START_ROUND) {
			tutorialContainer.setVisible(false);
		}
	}

	/**
	 * Called when the first round completes.
	 * Advances to the "upgrade unit" step.
	 */
	public void onFirstRoundCompleted() {
		if (currentStep == TutorialStep.START_ROUND) {
			currentStep = TutorialStep.UPGRADE_UNIT;
			tutorialContainer.setVisible(true);
			updateTutorialDisplay();
		}
	}

	/**
	 * Called when the player selects a unit.
	 * Hides the "upgrade unit" hint.
	 */
	public void onUnitSelected() {
		if (currentStep == TutorialStep.UPGRADE_UNIT) {
			tutorialContainer.setVisible(false);
		}
	}

	/**
	 * Called when the player upgrades a unit.
	 * Completes the tutorial.
	 */
	public void onUnitUpgraded() {
		if (currentStep == TutorialStep.UPGRADE_UNIT) {
			currentStep = TutorialStep.COMPLETED;
			updateTutorialDisplay();
		}
	}

	/**
	 * Disables the tutorial system.
	 */
	public void disable() {
		tutorialEnabled = false;
		updateTutorialDisplay();
	}

	/**
	 * Checks if the tutorial is currently active.
	 *
	 * @return true if tutorial is enabled and not completed
	 */
	public boolean isActive() {
		return tutorialEnabled && currentStep != TutorialStep.COMPLETED;
	}

	/**
	 * Gets the current tutorial step.
	 *
	 * @return The current tutorial step
	 */
	public TutorialStep getCurrentStep() {
		return currentStep;
	}
}
