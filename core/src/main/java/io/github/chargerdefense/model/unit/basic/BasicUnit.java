package io.github.chargerdefense.model.unit.basic;

import io.github.chargerdefense.model.unit.Unit;
import io.github.chargerdefense.model.unit.upgrade.Upgrade;
import io.github.chargerdefense.model.unit.upgrade.UpgradePath;

import java.util.ArrayList;
import java.util.List;

/**
 * A basic unit with balanced stats.
 */
public class BasicUnit extends Unit {

	/**
	 * Constructs a new BasicUnit with predefined stats.
	 */
	public BasicUnit() {
		super(10, 100, 1.0, 100);

		List<Upgrade> upgrades = new ArrayList<>();
		upgrades.add(new IncreaseDamageUpgrade(50, 5));
		upgrades.add(new IncreaseDamageUpgrade(100, 5));
		this.upgradePath = new UpgradePath(upgrades);
	}
}
