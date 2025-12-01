package io.github.chargerdefense.model.enemy;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.github.chargerdefense.assets.Assets;

/**
 * A basic enemy type with standard health, speed, and currency value.
 * This represents the most common enemy that players will encounter.
 */
public class NormalEnemy extends Enemy {

    /**
     * Constructs a new NormalEnemy with default stats.
     */
    public NormalEnemy() {
        super(100, 50, 10);
    }

    @Override
    protected void loadSprite() {
        TextureRegion sprite = Assets.getInstance().getEnemySprite();
        if (sprite != null) {
            setSprite(sprite);
        }
    }
}
