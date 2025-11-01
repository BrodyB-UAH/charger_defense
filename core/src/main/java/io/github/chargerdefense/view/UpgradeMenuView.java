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

        add(upgradeNameLabel).row();
        add(upgradeDescriptionLabel).row();
        add(costLabel).row();
        add(upgradeButton);

        upgradeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.upgradeSelectedUnit();
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
        } else {
            setVisible(false);
        }
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
