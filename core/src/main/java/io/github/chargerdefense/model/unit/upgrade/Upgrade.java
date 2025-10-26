package io.github.chargerdefense.model.unit.upgrade;

import io.github.chargerdefense.model.unit.Unit;

/** Represents an upgrade for a unit. */
public interface Upgrade {
    /** Gets the name of the upgrade. */
    String getName();

    /** Gets the description of the upgrade. */
    String getDescription();

    /** Gets the cost of the upgrade. */
    int getCost();

    /** Applies the upgrade to the specified unit. */
    void apply(Unit unit);
}
