package io.github.chargerdefense.model.unit.upgrade;

import io.github.chargerdefense.model.unit.Unit;

/** Represents an upgrade for a unit. */
public interface Upgrade {
    /**
     * Gets the name of the upgrade.
     * 
     * @return The name of the upgrade.
     */
    String getName();

    /**
     * Gets the description of the upgrade.
     * 
     * @return The description of the upgrade.
     */
    String getDescription();

    /**
     * Gets the cost of the upgrade.
     * 
     * @return The cost in in-game currency.
     */
    int getCost();

    /**
     * Applies the upgrade to the specified unit.
     * 
     * @param unit The unit to apply the upgrade to.
     */
    void apply(Unit unit);
}
