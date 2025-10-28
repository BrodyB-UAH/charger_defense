package io.github.chargerdefense.data.game;

import java.util.List;

/** Represents the state of a saved game. */
public class SavedGameState {
    /** The name of the profile associated with this save. */
    public String profileName;
    /** The number of lives the player has. */
    public int lives;
    /** The amount of currency the player has. */
    public int currency;
    /** The player's score. */
    public int score;
    /** The number of enemies defeated by the player. */
    public int enemiesDefeated;
    /** The number of units purchased by the player. */
    public int unitsPurchased;
    /** The index of the current round. */
    public int currentRoundIndex;
    /** The list of placed units in the game. */
    public List<SavedUnit> placedUnits;

    /**
     * Constructs a new SavedGameState.
     * 
     * @param lives             The number of lives the player has
     * @param currency          The amount of currency the player has
     * @param score             The player's score
     * @param enemiesDefeated   The number of enemies defeated by the player
     * @param unitsPurchased    The number of units purchased by the player
     * @param currentRoundIndex The index of the current round
     * @param placedUnits       The list of placed units in the game
     */
    public SavedGameState(int lives, int currency, int score, int enemiesDefeated,
            int unitsPurchased, int currentRoundIndex, List<SavedUnit> placedUnits) {
        this.lives = lives;
        this.currency = currency;
        this.score = score;
        this.enemiesDefeated = enemiesDefeated;
        this.unitsPurchased = unitsPurchased;
        this.currentRoundIndex = currentRoundIndex;
        this.placedUnits = placedUnits;
    }
}
