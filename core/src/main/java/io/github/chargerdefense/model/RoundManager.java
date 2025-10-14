package io.github.chargerdefense.model;

import io.github.chargerdefense.model.enemy.Enemy;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the sequence of rounds in the game. It controls the spawning of
 * enemies for the current round and tracks progress.
 */
public class RoundManager {
    /** A list of all rounds in the game. */
    private List<Round> allRounds;
    /** The index of the current round in the `allRounds` list. */
    private int currentRoundIndex;
    /** The current round being played. */
    private Round currentRound;
    /** A reference to the main game model, used for callbacks. */
    private GameModel game;

    /**
     * Constructs a new RoundManager with the specified rounds and game reference.
     *
     * @param rounds The list of rounds to manage
     * @param game   The main game model for callbacks and state management
     */
    public RoundManager(List<Round> rounds, GameModel game) {
        this.allRounds = rounds;
        this.currentRoundIndex = -1;
        this.game = game;
    }

    /**
     * Starts the next round if one is available.
     * 
     * @return true if a new round was started, false if all rounds are complete.
     */
    public boolean startNextRound() {
        if (currentRound != null && !currentRound.isComplete()) {
            return false;
        }

        currentRoundIndex += 1;
        if (currentRoundIndex < allRounds.size()) {
            currentRound = allRounds.get(currentRoundIndex);
            currentRound.start(game);
            return true;
        }

        return false;
    }

    /**
     * Updates the current round's state (e.g., spawning and moving enemies).
     *
     * @param deltaTime The time elapsed since the last update (in seconds)
     */
    public void update(float deltaTime) {
        if (currentRound != null && !currentRound.isComplete()) {
            currentRound.update(deltaTime);
        }
    }

    /**
     * Gets the list of currently active enemies from the current round.
     *
     * @return A list of active enemies, or an empty list if no round is active
     */
    public List<Enemy> getActiveEnemies() {
        if (currentRound != null) {
            return currentRound.getActiveEnemies();
        }
        return new ArrayList<>();
    }

    /**
     * Checks if all rounds have been completed.
     *
     * @return true if all rounds are finished, false otherwise
     */
    public boolean areAllRoundsCompleted() {
        return currentRoundIndex >= allRounds.size() - 1 &&
                (currentRound == null || currentRound.isComplete());
    }

    /**
     * Called when an enemy is defeated to notify the current round.
     *
     * @param enemy The enemy that was defeated
     */
    public void onEnemyDefeated(Enemy enemy) {
        if (currentRound != null) {
            currentRound.onEnemyDefeated(enemy);
        }
    }

    /**
     * Called when an enemy reaches the end to notify the current round.
     *
     * @param enemy The enemy that reached the end
     */
    public void onEnemyReachedEnd(Enemy enemy) {
        if (currentRound != null) {
            currentRound.onEnemyReachedEnd(enemy);
        }
    }

    /**
     * Gets the current round number (1-indexed for display).
     *
     * @return The current round number, or 0 if no round has started
     */
    public int getCurrentRoundNumber() {
        return currentRoundIndex + 1;
    }

    /**
     * Gets the total number of rounds.
     *
     * @return The total number of rounds in the game
     */
    public int getTotalRounds() {
        return allRounds.size();
    }

    /**
     * Checks if a round is currently in progress.
     *
     * @return true if a round is active and not complete, false otherwise
     */
    public boolean isRoundInProgress() {
        return currentRound != null && !currentRound.isComplete();
    }
}
