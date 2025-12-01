package io.github.chargerdefense.model.enemy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.chargerdefense.assets.Assets;

/**
 * A camouflaged enemy that cannot be detected by units without camo detection.
 */
public class CamoEnemy extends Enemy {

	/**
	 * Constructs a new CamoEnemy with slightly reduced health but higher currency
	 * value.
	 */
	public CamoEnemy() {
		super(80, 70, 20); // Less health, faster, worth more currency
	}

	@Override
	public boolean isCamouflaged() {
		return true;
	}

	@Override
	protected void loadSprite() {
		TextureRegion sprite = Assets.getInstance().getCamoEnemySprite();
		if (sprite != null) {
			setSprite(sprite);
		}
	}
}
