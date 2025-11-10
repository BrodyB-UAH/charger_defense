package io.github.chargerdefense;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Simple game model that holds core game state and notifies observers on changes.
 *
 * <p>Design notes:
 * - Observers implement {@link GameObserver} and can register via {@link #addObserver}.
 * - The public {@link #setLives(int)} method is the supported way for external
 *   code to change the lives count; observers will be notified only when the
 *   value actually changes. This makes it trivial for other code to set lives
 *   to 100 by calling {@code gameModel.setLives(100)}.</p>
 *
 * <p>Threading: this implementation uses a thread-safe observer list (CopyOnWriteArrayList).
 * The model does not enforce which thread callers must use; however, UI updates
 * often need to happen on a specific thread — observers should handle that.</p>
 */
public class GameModel {
    private final List<GameObserver> observers = new CopyOnWriteArrayList<>();
    private int lives = 3; // default starting lives, can be changed via setLives

    /**
     * Register an observer to receive model updates.
     *
     * @param observer the observer to add; null observers are ignored
     */
    public void addObserver(GameObserver observer) {
        if (observer == null) return;
        observers.add(observer);
    }

    /**
     * Remove a previously registered observer.
     *
     * @param observer the observer to remove
     */
    public void removeObserver(GameObserver observer) {
        observers.remove(observer);
    }

    /**
     * Returns the current lives count.
     *
     * @return current number of lives
     */
    public int getLives() {
        return lives;
    }

    /**
     * Update the number of lives and notify observers if it changes.
     *
     * <p>Usage example: some external code can set the number of lives to 100 by calling:
     * {@code gameModel.setLives(100);}</p>
     *
     * @param newLives the new total number of lives (clamped to be >= 0)
     */
    public void setLives(int newLives) {
        int safe = Math.max(0, newLives);
        if (this.lives == safe) return;
        this.lives = safe;
        notifyLivesChanged(safe);
    }

    private void notifyLivesChanged(int value) {
        for (GameObserver o : observers) {
            try {
                o.onLivesChanged(value);
            } catch (Exception e) {
                // Observers should not throw; guard here so one bad observer doesn't break notifications
                e.printStackTrace();
            }
        }
    }

    // ...existing code...
}