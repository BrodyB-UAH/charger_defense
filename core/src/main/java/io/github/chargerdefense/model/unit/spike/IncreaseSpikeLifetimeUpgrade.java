package io.github.chargerdefense.model.unit.spike;

import io.github.chargerdefense.assets.Assets;
import io.github.chargerdefense.model.unit.Unit;
import io.github.chargerdefense.model.unit.upgrade.Upgrade;

/**
 * Upgrade that increases spike lifetime.
 */
public class IncreaseSpikeLifetimeUpgrade implements Upgrade {
	/** The cost of the upgrade. */
	private final int cost;
	/** The amount to increase spike lifetime by. */
	private final double lifetimeIncrease;

	/**
	 * Constructs the upgrade with the specified cost and lifetime increase.
	 * 
	 * @param cost
	 * @param lifetimeIncrease
	 */
	public IncreaseSpikeLifetimeUpgrade(int cost, double lifetimeIncrease) {
		this.cost = cost;
		this.lifetimeIncrease = lifetimeIncrease;
	}

	@Override
	public String getName() {
		return "Increase Spike Lifetime";
	}

	@Override
	public String getDescription() {
		return "Increases spike lifetime by " + lifetimeIncrease + " seconds";
	}

	@Override
	public int getCost() {
		return cost;
	}

	@Override
	public void apply(Unit unit) {
		if (unit instanceof SpikeFactoryUnit) {
			SpikeFactoryUnit factory = (SpikeFactoryUnit) unit;
			factory.setSpikeLifetime(factory.getSpikeLifetime() + lifetimeIncrease);
			factory.setSprite(Assets.getInstance().getThornSpikeFactorySprite());
		}
	}
}