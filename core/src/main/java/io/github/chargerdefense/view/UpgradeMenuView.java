package io.github.chargerdefense.view;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import io.github.chargerdefense.model.GameModel;
import io.github.chargerdefense.model.PlayerObserver;
import io.github.chargerdefense.model.unit.Unit;
import io.github.chargerdefense.model.unit.Unit.TargetingMode;
import io.github.chargerdefense.model.unit.upgrade.Upgrade;

/**
 * View for the upgrade menu, displaying upgrade options for the selected unit.
 */
public class UpgradeMenuView extends Table implements PlayerObserver {
    /** The game model containing the current game state. */
    private GameModel game;
    /** The label displaying the name of the upgrade. */
    private Label upgradeNameLabel;
    /** The label displaying the description of the upgrade. */
    private Label upgradeDescriptionLabel;
    /** The label displaying the cost of the upgrade. */
    private Label costLabel;
    /** The button to confirm the upgrade. */
    private TextButton upgradeButton;
    /** The button to cycle through target priorities. */
    private TextButton targetPriorityButton;
    /** The currently displayed upgrade option. */
    private Upgrade currentUpgrade;

    /**
     * Constructs a new UpgradeMenuView with the specified game model and skin.
     * 
     * @param game The game model containing the current game state.
     * @param skin The skin for UI styling.
     */
    public UpgradeMenuView(GameModel game, Skin skin) {
        super(skin);
        this.game = game;
        this.game.getPlayer().addObserver(this);

        upgradeNameLabel = new Label("", skin);
        upgradeDescriptionLabel = new Label("", skin);
        costLabel = new Label("", skin);
        upgradeButton = new TextButton("Upgrade", skin);
    targetPriorityButton = new TextButton("Target Priority: First", skin);

        // Slightly reduce label font sizes to make menu more compact
        upgradeNameLabel.setFontScale(0.95f);
        upgradeDescriptionLabel.setFontScale(0.92f);
        costLabel.setFontScale(0.9f);

        // Use a nested table for the buttons so they share the same parent and sizing
        float buttonWidth = 150f;
        float buttonHeight = 36f;
        float buttonSpacing = 6f;

        // Keep button font scale consistent and balanced with labels
        upgradeButton.getLabel().setFontScale(0.95f);
        targetPriorityButton.getLabel().setFontScale(0.95f);

        // Center descriptive text across two columns so it's visually between both buttons
        add(upgradeNameLabel).colspan(2).center().row();
        add(upgradeDescriptionLabel).colspan(2).center().row();
        add(costLabel).colspan(2).center().padBottom(6).row();

        // Button row grouped in a nested table to ensure same size and spacing
        Table buttonRow = new Table();
        buttonRow.add(upgradeButton).width(buttonWidth).height(buttonHeight).padRight(buttonSpacing);
        buttonRow.add(targetPriorityButton).width(buttonWidth).height(buttonHeight);

        add(buttonRow).colspan(2).center().row();

        upgradeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.upgradeSelectedUnit();
            }
        });

        targetPriorityButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Unit selectedUnit = game.getSelectedUnit();
                if (selectedUnit != null) {
                    selectedUnit.cycleTargetMode();
                    updateTargetPriorityButton(selectedUnit);
                }
            }
        });

        setVisible(false);
    }

    /**
     * Updates the upgrade menu to reflect the currently selected unit's upgrade
     * options.
     * Displays the next available upgrade or hides the menu if no upgrade is
     * available.
     */
    public void update() {
        Unit selectedUnit = game.getSelectedUnit();
        if (selectedUnit != null) {
            currentUpgrade = selectedUnit.getUpgradePath().getNextUpgrade();
            if (currentUpgrade != null) {
                upgradeNameLabel.setText(currentUpgrade.getName());
                upgradeDescriptionLabel.setText(currentUpgrade.getDescription());
                costLabel.setText("Cost: " + currentUpgrade.getCost());
                upgradeButton.setDisabled(!game.getPlayer().canAfford(currentUpgrade.getCost()));
                setVisible(true);
            } else {
                setVisible(false);
            }
            updateTargetPriorityButton(selectedUnit);
        } else {
            setVisible(false);
        }
    }

    /**
     * Updates the target priority button text to reflect the current targeting mode.
     * 
     * @param unit The selected unit to get the targeting mode from.
     */
    private void updateTargetPriorityButton(Unit unit) {
        TargetingMode mode = unit.getTargetMode();
        targetPriorityButton.setText("Target Priority: " + mode.getDisplayName());
    }

    @Override
    public void onCurrencyChanged(int newCurrency) {
        if (currentUpgrade != null) {
            upgradeButton.setDisabled(!game.getPlayer().canAfford(currentUpgrade.getCost()));
        }
    }

    /**
     * Unregister the observer on cleanup
     */
    public void dispose() {
        game.getPlayer().removeObserver(this);
    }
}
