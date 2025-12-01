package io.github.chargerdefense.model;

/**
 * Observer interface for receiving notifications about game state changes.
 * 
 * This interface defines a small API that any UI or other interested component
 * can implement to receive notifications about important game events.
 * Implementers should assume these methods are called by the {@link GameModel}
 * when the corresponding state changes.
 * 
 * Callers (typically {@link GameModel}) will call these methods whenever the
 * associated state changes — for example, {@code onLivesChanged} is invoked
 * whenever the number of lives is updated via {@link GameModel#setLives(int)}.
 */
public interface GameObserver {
	/**
	 * Called when the player's currency changes.
	 * 
	 * Implementations update the UI to reflect the new currency amount.
	 *
	 * @param newCurrency The updated currency amount
	 */
	void onCurrencyChanged(int newCurrency);

	/**
	 * Called when the player's lives change.
	 * 
	 * Implementations typically update UI elements (labels, icons, etc.) to
	 * reflect the new count.
	 *
	 * @param newLives The updated number of lives
	 */
	void onLivesChanged(int newLives);

	/**
	 * Called when the current round number changes.
	 * 
	 * Implementations typically update UI elements to reflect the new round.
	 *
	 * @param currentRound The current round number (1-indexed)
	 * @param totalRounds  The total number of rounds
	 */
	void onRoundChanged(int currentRound, int totalRounds);

	/**
	 * Called when the round state changes (started, completed, etc.).
	 * 
	 * Implementations typically update UI elements to reflect the round state.
	 *
	 * @param roundInProgress   true if a round is currently active
	 * @param allRoundsComplete true if all rounds have been completed
	 */
	void onRoundStateChanged(boolean roundInProgress, boolean allRoundsComplete);

	/**
	 * Called when the game is over.
	 * 
	 * Implementations typically update UI elements to reflect the game over state.
	 *
	 * @param victory true if the player won, false if they lost
	 */
	void onGameOver(boolean victory);

	/**
	 * Called when a unit is upgraded.
	 * 
	 * Implementations can use this to track player progress through the tutorial.
	 */
	void onUnitUpgraded();
}
