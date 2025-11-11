package io.github.chargerdefense.model;

/**
 * Observer interface for receiving notifications about game state changes.
 */
public interface GameObserver {
	/**
	 * Called when the player's currency changes.
	 *
	 * @param newCurrency The updated currency amount
	 */
	void onCurrencyChanged(int newCurrency);

	/**
	 * Called when the player's lives change.
	 *
	 * @param newLives The updated number of lives
	 */
	void onLivesChanged(int newLives);

	/**
	 * Called when the current round number changes.
	 *
	 * @param currentRound The current round number (1-indexed)
	 * @param totalRounds  The total number of rounds
	 */
	void onRoundChanged(int currentRound, int totalRounds);

	/**
	 * Called when the round state changes (started, completed, etc.).
	 *
	 * @param roundInProgress   true if a round is currently active
	 * @param allRoundsComplete true if all rounds have been completed
	 */
	void onRoundStateChanged(boolean roundInProgress, boolean allRoundsComplete);

	/**
	 * Called when the game is over.
	 *
	 * @param victory true if the player won, false if they lost
	 */
	void onGameOver(boolean victory);
}
