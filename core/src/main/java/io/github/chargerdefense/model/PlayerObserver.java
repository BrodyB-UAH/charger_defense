package io.github.chargerdefense.model;

/**
 * Observer interface for receiving notifications about player state changes.
 */
public interface PlayerObserver {
    /**
     * Called when the player's currency changes.
     *
     * @param newCurrency The updated currency amount
     */
    void onCurrencyChanged(int newCurrency);
}
