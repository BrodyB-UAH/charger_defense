package io.github.chargerdefense.model.enemy;

/**
 * A basic enemy type with standard health, speed, and currency value.
 * This represents the most common enemy that players will encounter.
 */
public class NormalEnemy extends Enemy {

    /**
     * Constructs a new NormalEnemy with default stats.
     */
    public NormalEnemy() {
        super(100, 10, 10);
    }
}
