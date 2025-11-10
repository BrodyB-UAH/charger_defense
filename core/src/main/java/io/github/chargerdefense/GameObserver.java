package io.github.chargerdefense;

 /**
  * Observer contract for game-state changes.
  *
  * <p>This interface defines a small API that any UI or other interested component
  * can implement to receive notifications about important game events. Implementers
  * should assume these methods are called by the {@link GameModel} when the
  * corresponding state changes.</p>
  *
  * <p>Important notes for implementers:
  * - Methods should be fast and non-blocking. Long-running work should be
  *   dispatched to background threads if needed.
  * - Implementations should NOT mutate the model state inside these callbacks.
  * - Callers (typically {@link GameModel}) will call these methods whenever the
  *   associated state changes — for example, {@code onLivesChanged} is invoked
  *   whenever the number of lives is updated via {@link GameModel#setLives(int)}.
  * - The API is intentionally small to keep responsibilities focused.</p>
  */
public interface GameObserver {
    /**
     * Called when the player's number of lives changes.
     *
     * <p>Contract / expectations:
     * - Parameter {@code newLives} is the total number of lives after the change.
     * - This method is part of the public API: other developers should be able
     *   to rely on it being invoked whenever the model's lives value is updated.
     * - Implementations typically update UI elements (labels, icons, etc.) to
     *   reflect the new count. Keep the update quick and avoid model mutations.
     * - If the UI toolkit requires updates on a specific thread (e.g., libGDX's
     *   rendering thread), ensure the implementation performs thread-appropriate
     *   operations (or schedules them) — the model does not enforce threading.
     * </p>
     *
     * @param newLives the updated lives count (may be zero or positive)
     */
    void onLivesChanged(int newLives);

    /**
     * Called when the player's score changes.
     *
     * @param newScore the updated score value
     */
    void onScoreChanged(int newScore);

    /**
     * Called when a new wave of enemies begins.
     *
     * @param waveIndex index/number of the new wave (1-based or 0-based depending on model)
     */
    void onWaveStarted(int waveIndex);

    /**
     * Called when an enemy is killed.
     *
     * @param enemyId an identifier for the killed enemy (implementation-defined)
     */
    void onEnemyKilled(String enemyId);

    /**
     * Called when the game enters a terminal "game over" state.
     *
     * Implementations may display final screens or cleanup UI here.
     */
    void onGameOver();
}
