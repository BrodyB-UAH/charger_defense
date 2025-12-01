package io.github.chargerdefense.model.unit.basic;

import io.github.chargerdefense.assets.Assets;
import io.github.chargerdefense.model.unit.Unit;
import io.github.chargerdefense.model.unit.upgrade.Upgrade;

/**
 * An upgrade that grants camo detection to a unit.
 */
public class CamoDetectionUpgrade implements Upgrade {
	/** The cost of the upgrade. */
	private final int cost;

	/**
	 * Constructs a new CamoDetectionUpgrade.
	 *
	 * @param cost The cost of the upgrade
	 */
	public CamoDetectionUpgrade(int cost) {
		this.cost = cost;
	}

	@Override
	public String getName() {
		return "Camo Detection";
	}

	@Override
	public String getDescription() {
		return "Allows the unit to detect and target camouflaged enemies";
	}

	@Override
	public int getCost() {
		return cost;
	}

	@Override
	public void apply(Unit unit) {
		unit.setCanDetectCamo(true);
		unit.setSprite(Assets.getInstance().getCamoTowerSprite());
	}
}
