package io.github.chargerdefense.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Singleton class for managing game assets.
 * Provides centralized access to textures and sprites throughout the game.
 */
public class Assets {
	private static Assets instance;
	private final AssetManager assetManager;
	private boolean assetsLoaded = false;

	// Cached sprites
	private TextureRegion enemySprite;
	private TextureRegion camoEnemySprite;
	private TextureRegion towerSprite;
	private TextureRegion spikeFactorySprite;
	private TextureRegion camoTowerSprite;
	private TextureRegion thornSpikeFactorySprite;

	// Cached sounds
	private Sound enemyHitSound;

	/**
	 * Private constructor for singleton pattern.
	 */
	private Assets() {
		this.assetManager = new AssetManager();
	}

	/**
	 * Gets the singleton instance of Assets.
	 *
	 * @return The Assets instance
	 */
	public static Assets getInstance() {
		if (instance == null) {
			instance = new Assets();
		}
		return instance;
	}

	/**
	 * Loads all game assets.
	 * Should be called once at game startup.
	 */
	public void loadAssets() {
		// prevent loading assets multiple times
		if (assetsLoaded) {
			return;
		}

		assetManager.load("enemies/mushroom_black.png", Texture.class);
		assetManager.load("enemies/mushroom_camo.png", Texture.class);
		assetManager.load("towers/Charger_Blue_Sprite.png", Texture.class);
		assetManager.load("towers/spike_factory_base.png", Texture.class);
		assetManager.load("towers/spike_factory_thorn.png", Texture.class);
		assetManager.load("towers/charger_blue_fire.png", Texture.class);
		assetManager.load("sfx/oof.mp3", Sound.class);
		assetManager.finishLoading();

		Texture enemyTexture = assetManager.get("enemies/mushroom_black.png", Texture.class);
		enemySprite = new TextureRegion(enemyTexture);

		Texture camoEnemyTexture = assetManager.get("enemies/mushroom_camo.png", Texture.class);
		camoEnemySprite = new TextureRegion(camoEnemyTexture);

		Texture towerTexture = assetManager.get("towers/Charger_Blue_Sprite.png", Texture.class);
		towerSprite = new TextureRegion(towerTexture);

		Texture spikeFactoryTexture = assetManager.get("towers/spike_factory_base.png", Texture.class);
		spikeFactorySprite = new TextureRegion(spikeFactoryTexture);

		Texture camoTowerTexture = assetManager.get("towers/charger_blue_fire.png", Texture.class);
		camoTowerSprite = new TextureRegion(camoTowerTexture);

		Texture thornSpikeFactoryTexture = assetManager.get("towers/spike_factory_thorn.png", Texture.class);
		thornSpikeFactorySprite = new TextureRegion(thornSpikeFactoryTexture);

		enemyHitSound = assetManager.get("sfx/oof.mp3", Sound.class);

		assetsLoaded = true;
	}

	/**
	 * Gets the enemy sprite.
	 *
	 * @return The enemy sprite TextureRegion
	 */
	public TextureRegion getEnemySprite() {
		return enemySprite;
	}

	/**
	 * Gets the camo enemy sprite.
	 *
	 * @return The camo enemy sprite TextureRegion
	 */
	public TextureRegion getCamoEnemySprite() {
		return camoEnemySprite;
	}

	/**
	 * Gets the tower sprite (BasicUnit).
	 *
	 * @return The tower sprite TextureRegion
	 */
	public TextureRegion getTowerSprite() {
		return towerSprite;
	}

	/**
	 * Gets the camo tower sprite (CamoUnit).
	 *
	 * @return The camo tower sprite TextureRegion
	 */
	public TextureRegion getCamoTowerSprite() {
		return camoTowerSprite;
	}

	/**
	 * Gets the spike factory sprite.
	 *
	 * @return The spike factory sprite TextureRegion
	 */
	public TextureRegion getSpikeFactorySprite() {
		return spikeFactorySprite;
	}

	/**
	 * Gets the thorn spike factory sprite.
	 *
	 * @return The thorn spike factory sprite TextureRegion
	 */
	public TextureRegion getThornSpikeFactorySprite() {
		return thornSpikeFactorySprite;
	}

	/**
	 * Gets the enemy hit sound.
	 *
	 * @return The enemy hit sound
	 */
	public Sound getEnemyHitSound() {
		return enemyHitSound;
	}

	/**
	 * Gets the underlying AssetManager.
	 *
	 * @return The AssetManager instance
	 */
	public AssetManager getAssetManager() {
		return assetManager;
	}

	/**
	 * Disposes of all loaded assets.
	 * Should be called when the game is closing.
	 */
	public void dispose() {
		assetManager.dispose();
	}
}
