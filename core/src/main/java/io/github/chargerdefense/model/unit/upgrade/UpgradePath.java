package io.github.chargerdefense.model.unit.upgrade;

import java.util.List;

/**
 * Represents the upgrade path for a unit, containing a list of upgrades and the
 * current level.
 */
public class UpgradePath {
    /** The list of upgrades available for the unit. */
    private final List<Upgrade> upgrades;
    /** The current level of the unit's upgrades. */
    private int currentLevel = 0;

    /**
     * Constructs a new UpgradePath with the specified list of upgrades.
     * 
     * @param upgrades The list of upgrades for this path.
     */
    public UpgradePath(List<Upgrade> upgrades) {
        this.upgrades = upgrades;
    }

    /**
     * Gets the next upgrade in the path, or null if there are no more upgrades.
     * 
     * @return The next Upgrade, or null if at max level
     */
    public Upgrade getNextUpgrade() {
        if (currentLevel < upgrades.size()) {
            return upgrades.get(currentLevel);
        }
        return null;
    }

    /** Advances the upgrade path to the next upgrade. */
    public void advance() {
        if (currentLevel < upgrades.size()) {
            currentLevel++;
        }
    }

    /**
     * Gets the current level of the upgrade path.
     * 
     * @return The current upgrade level
     */
    public int getCurrentLevel() {
        return currentLevel;
    }
}
