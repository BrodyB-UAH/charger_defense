package io.github.chargerdefense.model.unit.spike;

import io.github.chargerdefense.model.unit.Unit;
import io.github.chargerdefense.model.unit.upgrade.Upgrade;

/**
 * Upgrade that increases the maximum hits per spike.
 */
public class IncreaseSpikeMaxHitsUpgrade implements Upgrade {
	/** The cost of the upgrade. */
	private final int cost;
	/** The amount to increase max hits by. */
	private final int hitsIncrease;

	/**
	 * Constructs the upgrade with the specified cost and hits increase.
	 *
	 * @param cost         The cost of the upgrade.
	 * @param hitsIncrease The amount to increase max hits by.
	 */
	public IncreaseSpikeMaxHitsUpgrade(int cost, int hitsIncrease) {
		this.cost = cost;
		this.hitsIncrease = hitsIncrease;
	}

	@Override
	public String getName() {
		return "Increase Spike Max Hits";
	}

	@Override
	public String getDescription() {
		return "Increases max hits per spike by " + hitsIncrease;
	}

	@Override
	public int getCost() {
		return cost;
	}

	@Override
	public void apply(Unit unit) {
		if (unit instanceof SpikeFactoryUnit) {
			SpikeFactoryUnit factory = (SpikeFactoryUnit) unit;
			factory.setSpikeMaxHits(factory.getSpikeMaxHits() + hitsIncrease);
		}
	}
}