package io.github.chargerdefense;

import com.badlogic.gdx.Gdx;

/**
 * Simple HUD that implements {@link GameObserver}.
 *
 * <p>This class represents an example UI component that listens to the {@link GameModel}.
 * The onLivesChanged method is public and part of the API so other developers can
 * expect it to be invoked when the model updates the lives count.</p>
 *
 * <p>Implementation notes:
 * - This example stores the current lives in a field and logs changes.
 * - In a real UI you'd update visual labels and ensure updates happen on the
 *   render thread where required by the UI toolkit.</p>
 */
public class GameHud implements GameObserver {
    private int currentLives = 0;

    /**
     * Called by the model when the number of lives changes.
     *
     * <p>Public API expectations:
     * - Will be invoked with the new total lives value.
     * - Implementations should update UI quickly and not mutate model state.
     * - If a UI framework requires a specific thread for updates, perform the
     *   required scheduling inside this method.</p>
     *
     * @param newLives updated total lives count (non-negative).
     */
    @Override
    public void onLivesChanged(int newLives) {
        this.currentLives = newLives;
        // Minimal example behavior: log the change and (in real code) update UI widgets.
        Gdx.app.log("GameHud", "Lives updated to: " + newLives);
        // ...in a full HUD you'd update labels, hearts icons, etc. here...
    }

    @Override
    public void onScoreChanged(int newScore) {
        // ...existing code...
    }

    @Override
    public void onWaveStarted(int waveIndex) {
        // ...existing code...
    }

    @Override
    public void onEnemyKilled(String enemyId) {
        // ...existing code...
    }

    @Override
    public void onGameOver() {
        // ...existing code...
    }

    // Optional helper to get the value for testing or other components
    public int getCurrentLives() {
        return currentLives;
    }
}