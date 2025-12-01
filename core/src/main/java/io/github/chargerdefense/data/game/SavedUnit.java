package io.github.chargerdefense.data.game;

/**
 * Represents a saved unit's data for serialization.
 */
public class SavedUnit {
    /** The type of the unit. */
    public String type;
    /** The x-coordinate of the unit. */
    public int x;
    /** The y-coordinate of the unit. */
    public int y;
    /** The upgrade level of the unit. */
    public int upgradeLevel;

    /**
     * Constructs a new SavedUnit
     * 
     * @param type         The type of the unit
     * @param x            The x-coordinate of the unit
     * @param y            The y-coordinate of the unit
     * @param upgradeLevel The upgrade level of the unit
     */
    public SavedUnit(String type, int x, int y, int upgradeLevel) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.upgradeLevel = upgradeLevel;
    }

    /** Default constructor for serialization purposes. */
    public SavedUnit() {
    }
}
