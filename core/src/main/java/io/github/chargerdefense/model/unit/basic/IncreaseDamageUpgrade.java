package io.github.chargerdefense.model.unit.basic;

import io.github.chargerdefense.model.unit.Unit;
import io.github.chargerdefense.model.unit.upgrade.Upgrade;

/** An upgrade that increases the damage of a unit. */
public class IncreaseDamageUpgrade implements Upgrade {
    /** The cost of the upgrade. */
    private final int cost;
    /** The amount to increase the damage by. */
    private final double damageIncrease;

    /**
     * Creates a new IncreaseDamageUpgrade.
     * 
     * @param cost           The cost of the upgrade
     * @param damageIncrease The amount to increase the damage by
     */
    public IncreaseDamageUpgrade(int cost, double damageIncrease) {
        this.cost = cost;
        this.damageIncrease = damageIncrease;
    }

    @Override
    public String getName() {
        return "Increase Damage";
    }

    @Override
    public String getDescription() {
        return "Increases damage by " + damageIncrease;
    }

    @Override
    public int getCost() {
        return cost;
    }

    @Override
    public void apply(Unit unit) {
        unit.setDamage(unit.getDamage() + damageIncrease);
    }
}
