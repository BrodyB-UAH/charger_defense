package io.github.chargerdefense.model.unit.basic;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.chargerdefense.model.unit.Unit;
import io.github.chargerdefense.model.unit.upgrade.Upgrade;
import io.github.chargerdefense.model.unit.upgrade.UpgradePath;

import java.util.ArrayList;
import java.util.List;

/**
 * A basic unit with balanced stats.
 */
public class BasicUnit extends Unit {

	/** Default sprite for BasicUnit (set by GameView at startup) */
	private static TextureRegion DEFAULT_SPRITE;

	/**
	 * Sets the default sprite for all BasicUnit instances. Called by GameView
	 * after loading assets.
	 */
	public static void setDefaultSprite(TextureRegion sprite) {
		DEFAULT_SPRITE = sprite;
	}

	/**
	 * Constructs a new BasicUnit with predefined stats.
	 */
	public BasicUnit() {
		super(10, 100, 1.0, 100);

		// Initialize sprite from default if available
		if (DEFAULT_SPRITE != null) {
			this.setSprite(DEFAULT_SPRITE);
		}

		List<Upgrade> upgrades = new ArrayList<>();
		upgrades.add(new IncreaseDamageUpgrade(50, 5));
		upgrades.add(new IncreaseDamageUpgrade(100, 5));
		this.upgradePath = new UpgradePath(upgrades);
	}

	/**
	 * Render the basic unit sprite at the consistent tower size.
	 */
	@Override
	public void renderSprite(SpriteBatch batch) {
		java.awt.Point pos = this.getPosition();
		if (pos == null) {
			return;
		}

		TextureRegion s = this.getSprite();
		if (s != null) {
			float spriteSize = io.github.chargerdefense.GameConstants.TOWER_SIZE;
			batch.draw(s, pos.x - spriteSize / 2, pos.y - spriteSize / 2, spriteSize, spriteSize);
		} else {
			super.renderSprite(batch);
		}
	}
}
