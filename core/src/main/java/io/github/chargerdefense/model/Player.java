package io.github.chargerdefense.model;

/**
 * Represents the player's game state data.
 */
public class Player {

    /** The player's current amount of in-game currency. */
    private int currency;
    /** The player's current score for this game session. */
    private int score;
    /** The total number of enemies defeated by the player. */
    private int enemiesDefeated;
    /** The total number of units purchased by the player. */
    private int unitsPurchased;

    /**
     * Constructs a new Player with the specified initial currency.
     *
     * @param initialCurrency The amount of currency the player starts with.
     */
    public Player(int initialCurrency) {
        this.currency = initialCurrency;
        this.score = 0;
        this.enemiesDefeated = 0;
        this.unitsPurchased = 0;
    }

    /**
     * Gets the player's current currency amount.
     *
     * @return The current amount of currency the player has
     */
    public int getCurrency() {
        return currency;
    }

    /**
     * Sets the player's currency amount.
     * This should only be called by the game model or controller.
     *
     * @param currency The new currency amount
     */
    public void setCurrency(int currency) {
        this.currency = Math.max(0, currency);
    }

    /**
     * Adds currency to the player's total.
     * Typically called when an enemy is defeated.
     *
     * @param amount The amount of currency to add
     */
    public void addCurrency(int amount) {
        this.currency += amount;
    }

    /**
     * Attempts to spend the specified amount of currency.
     *
     * @param amount The amount of currency to spend
     * @return true if the player had enough currency and it was deducted, false
     *         otherwise
     */
    public boolean spendCurrency(int amount) {
        if (currency >= amount) {
            currency -= amount;
            return true;
        }
        return false;
    }

    /**
     * Checks if the player has enough currency for a purchase.
     *
     * @param amount The amount of currency needed
     * @return true if the player has enough currency, false otherwise
     */
    public boolean canAfford(int amount) {
        return currency >= amount;
    }

    /**
     * Gets the player's current score.
     *
     * @return The current score
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the player's score.
     *
     * @param score The new score
     */
    public void setScore(int score) {
        this.score = Math.max(0, score);
    }

    /**
     * Adds points to the player's score.
     *
     * @param points The points to add
     */
    public void addScore(int points) {
        this.score += points;
    }

    /**
     * Gets the total number of enemies defeated by the player.
     *
     * @return The number of enemies defeated
     */
    public int getEnemiesDefeated() {
        return enemiesDefeated;
    }

    /**
     * Increments the count of enemies defeated.
     */
    public void incrementEnemiesDefeated() {
        this.enemiesDefeated++;
    }

    /**
     * Sets the number of enemies defeated.
     *
     * @param enemiesDefeated The new count of enemies defeated
     */
    public void setEnemiesDefeated(int enemiesDefeated) {
        this.enemiesDefeated = Math.max(0, enemiesDefeated);
    }

    /**
     * Gets the total number of units purchased by the player.
     *
     * @return The number of units purchased
     */
    public int getUnitsPurchased() {
        return unitsPurchased;
    }

    /**
     * Increments the count of units purchased.
     */
    public void incrementUnitsPurchased() {
        this.unitsPurchased++;
    }

    /**
     * Sets the number of units purchased.
     *
     * @param unitsPurchased The new count of units purchased
     */
    public void setUnitsPurchased(int unitsPurchased) {
        this.unitsPurchased = Math.max(0, unitsPurchased);
    }
}
