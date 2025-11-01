package io.github.chargerdefense.model;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import io.github.chargerdefense.model.enemy.Enemy;
import io.github.chargerdefense.model.unit.Unit;

import java.awt.Point;

/**
 * Represents a stationary spike projectile placed on the ground.
 * Spikes have a limited lifetime and can hit multiple enemies.
 */
public class SpikeProjectile extends Projectile {
	/** The maximum lifetime of the spike in seconds. */
	private double maxLifetime;
	/** The remaining lifetime of the spike in seconds. */
	private double remainingLifetime;
	/** The maximum number of enemies this spike can hit. */
	private int maxHits;
	/** The number of enemies this spike has already hit. */
	private int hitCount;

	/**
	 * Constructs a spike projectile at the specified position.
	 *
	 * @param origin   The unit that created the spike.
	 * @param position The position where the spike is placed.
	 * @param damage   The damage dealt to enemies.
	 * @param lifetime The duration the spike lasts (in seconds).
	 * @param maxHits  The maximum number of enemies the spike can hit.
	 */
	public SpikeProjectile(Unit origin, Point.Double position, double damage, double lifetime, int maxHits) {
		super(origin, position, damage);
		this.maxLifetime = lifetime;
		this.remainingLifetime = lifetime;
		this.maxHits = maxHits;
		this.hitCount = 0;
	}

	/**
	 * Updates the spike's lifetime.
	 *
	 * @param deltaTime The time elapsed since the last update (in seconds)
	 * @return true if the spike should be removed, false otherwise
	 */
	@Override
	public boolean update(float deltaTime) {
		if (isDestroyed()) {
			return true;
		}

		remainingLifetime -= deltaTime;

		// remove spike if the lifetime expired or it has hit the max number of enemies
		if (remainingLifetime <= 0 || hitCount >= maxHits) {
			return true;
		}

		return super.update(deltaTime);
	}

	/**
	 * Applies damage to an enemy and increments the hit count.
	 *
	 * @param enemy The enemy to damage
	 */
	@Override
	public void hitEnemy(Enemy enemy) {
		if (!isDestroyed() && enemy != null && !enemy.isDead() && hitCount < maxHits) {
			enemy.takeDamage(getDamage());
			hitCount += 1;

			// destroy if it has hit the max number of enemies
			if (hitCount >= maxHits) {
				super.hitEnemy(enemy);
			}
		}
	}

	/**
	 * Gets the remaining lifetime of the spike.
	 *
	 * @return The remaining lifetime in seconds
	 */
	public double getRemainingLifetime() {
		return remainingLifetime;
	}

	/**
	 * Gets the lifetime percentage (0.0 to 1.0).
	 *
	 * @return The percentage of lifetime remaining
	 */
	public double getLifetimePercentage() {
		return remainingLifetime / maxLifetime;
	}

	/**
	 * Gets the number of hits remaining.
	 *
	 * @return The number of hits remaining
	 */
	public int getHitsRemaining() {
		return maxHits - hitCount;
	}

	/**
	 * Renders the spike as a star-like shape on the ground.
	 *
	 * @param shapeRenderer The shape renderer to use for drawing
	 */
	@Override
	public void render(ShapeRenderer shapeRenderer) {
		if (isDestroyed()) {
			return;
		}

		float x = (float) getPosition().x;
		float y = (float) getPosition().y;
		float size = 6.0f;

		// color fades as lifetime decreases
		float alpha = Math.max(0.3f, (float) getLifetimePercentage());

		shapeRenderer.setColor(0.6f, 0.6f, 0.6f, alpha);

		shapeRenderer.rectLine(x, y - size, x, y + size, 2.0f);
		shapeRenderer.rectLine(x - size, y, x + size, y, 2.0f);
		shapeRenderer.rectLine(x - size * 0.7f, y - size * 0.7f, x + size * 0.7f, y + size * 0.7f, 1.5f);
		shapeRenderer.rectLine(x - size * 0.7f, y + size * 0.7f, x + size * 0.7f, y - size * 0.7f, 1.5f);
	}
}
