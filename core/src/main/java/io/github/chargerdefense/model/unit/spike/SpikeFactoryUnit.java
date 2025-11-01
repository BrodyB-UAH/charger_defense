package io.github.chargerdefense.model.unit.spike;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.chargerdefense.model.Projectile;
import io.github.chargerdefense.model.SpikeProjectile;
import io.github.chargerdefense.model.enemy.Enemy;
import io.github.chargerdefense.model.unit.Unit;
import io.github.chargerdefense.model.unit.upgrade.Upgrade;
import io.github.chargerdefense.model.unit.upgrade.UpgradePath;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * A spike factory unit that periodically produces spikes on the ground within
 * its range.
 * Spikes damage enemies that walk over them and have a limited lifetime.
 */
public class SpikeFactoryUnit extends Unit {
	/** The lifetime of each spike produced (in seconds). */
	private double spikeLifetime;
	/** The maximum number of enemies each spike can hit. */
	private int spikeMaxHits;
	/** The rate at which spikes are produced. */
	private double spikeProductionRate;
	/** Timer for controlling spike production. */
	private double productionTimer;

	/**
	 * Constructs a new SpikeFactoryUnit with predefined stats.
	 */
	public SpikeFactoryUnit() {
		// damage: 5, range: 80, fireRate: 0.5 (2 seconds per spike), cost: 150
		super(5, 80, 0.5, 150);

		this.spikeProductionRate = 0.5;
		this.spikeLifetime = 10.0; // 10 second duration
		this.spikeMaxHits = 3; // 3 hits per spike
		this.productionTimer = 0;

		List<Upgrade> upgrades = new ArrayList<>();
		upgrades.add(new IncreaseSpikeLifetimeUpgrade(75, 5.0));
		upgrades.add(new IncreaseSpikeMaxHitsUpgrade(100, 2));
		this.upgradePath = new UpgradePath(upgrades);
	}

	/**
	 * Main update method that produces spikes periodically.
	 *
	 * @param deltaTime The time elapsed since the last update (in seconds)
	 * @param enemies   The list of all active enemies (unused for spike factory)
	 * @return A new spike projectile if one was produced, null otherwise
	 */
	@Override
	public Projectile update(float deltaTime, List<Enemy> enemies) {
		productionTimer -= deltaTime;

		if (productionTimer <= 0) {
			Projectile spike = produceSpike();
			productionTimer = 1.0 / spikeProductionRate;
			return spike;
		}

		return null;
	}

	/**
	 * Produces a new spike at a random location within range.
	 *
	 * @return A new SpikeProjectile
	 */
	private SpikeProjectile produceSpike() {
		Point unitPos = getPosition();
		if (unitPos == null) {
			return null;
		}

		// place the spike at a random position within the range
		double angle = Math.random() * 2 * Math.PI;
		double distance = Math.random() * getRange() * 0.8;

		double spikeX = unitPos.x + Math.cos(angle) * distance;
		double spikeY = unitPos.y + Math.sin(angle) * distance;

		Point.Double spikePosition = new Point.Double(spikeX, spikeY);

		SpikeProjectile spike = new SpikeProjectile(this, spikePosition, getDamage(), spikeLifetime, spikeMaxHits);

		return spike;
	}

	@Override
	public void render(ShapeRenderer shapeRenderer) {
		Point position = getPosition();
		if (position == null) {
			return;
		}

		float size = 16.0f;

		// range indicator
		shapeRenderer.setColor(0.4f, 0.3f, 0.2f, 0.2f);
		shapeRenderer.circle(position.x, position.y, (float) getRange());

		// selection highlight
		if (isSelected()) {
			shapeRenderer.setColor(Color.YELLOW);
			shapeRenderer.rect(position.x - size / 2 - 2, position.y - size / 2 - 2, size + 4, size + 4);
		}

		shapeRenderer.setColor(Color.GRAY);
		shapeRenderer.rect(position.x - size / 2, position.y - size / 2, size, size);

		shapeRenderer.setColor(Color.DARK_GRAY);
		shapeRenderer.rect(position.x - size / 4, position.y - size / 4, size / 2, size / 2);
	}

	/**
	 * Gets the lifetime of spikes produced by this factory.
	 *
	 * @return The spike lifetime in seconds
	 */
	public double getSpikeLifetime() {
		return spikeLifetime;
	}

	/**
	 * Sets the lifetime of spikes produced by this factory.
	 *
	 * @param spikeLifetime The new spike lifetime in seconds
	 */
	public void setSpikeLifetime(double spikeLifetime) {
		this.spikeLifetime = spikeLifetime;
	}

	/**
	 * Gets the maximum number of hits per spike.
	 *
	 * @return The maximum hits per spike
	 */
	public int getSpikeMaxHits() {
		return spikeMaxHits;
	}

	/**
	 * Sets the maximum number of hits per spike.
	 *
	 * @param spikeMaxHits The new maximum hits per spike
	 */
	public void setSpikeMaxHits(int spikeMaxHits) {
		this.spikeMaxHits = spikeMaxHits;
	}

}
