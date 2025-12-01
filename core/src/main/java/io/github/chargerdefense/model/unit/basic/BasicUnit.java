package io.github.chargerdefense.model.unit.basic;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.chargerdefense.assets.Assets;
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
		super(10, 100, 3.0, 100);

		List<Upgrade> upgrades = new ArrayList<>();
		upgrades.add(new CamoDetectionUpgrade(50));
		upgrades.add(new IncreaseDamageUpgrade(100, 5));
		upgrades.add(new IncreaseDamageUpgrade(50, 5));
		this.upgradePath = new UpgradePath(upgrades);
	}

	@Override
	protected void loadSprite() {
		TextureRegion sprite = Assets.getInstance().getTowerSprite();
		if (sprite != null) {
			setSprite(sprite);
		}
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
