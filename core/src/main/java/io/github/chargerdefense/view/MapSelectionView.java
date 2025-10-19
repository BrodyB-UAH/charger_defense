package io.github.chargerdefense.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.chargerdefense.controller.MainMenuController;
import io.github.chargerdefense.controller.StateManager;
import io.github.chargerdefense.data.game.SavedGameState;
import io.github.chargerdefense.model.MainMenuModel;
import io.github.chargerdefense.model.map.BasicMap;
import io.github.chargerdefense.model.map.GameMap;
import io.github.chargerdefense.model.map.SpiralMap;

import java.util.ArrayList;
import java.util.List;

/**
 * The map selection screen that displays available maps and allows the user
 * to view map details, select existing saves, or start a new game.
 */
public class MapSelectionView implements Screen {
	/** The controller for handling menu interactions */
	private final MainMenuController controller;
	/** The model for managing menu state */
	private final MainMenuModel model;
	/** The libGDX stage for organizing and rendering UI elements */
	private Stage stage;
	/** The skin containing UI styling and texture information */
	private Skin skin;
	/** The currently selected map */
	private GameMap selectedMap;
	/** The table containing map details and save information */
	private Table detailsTable;
	/** List of available maps */
	private List<GameMap> availableMaps;

	/**
	 * Constructs a new MapSelectionView with the specified state manager.
	 *
	 * @param stateManager The state manager containing controllers and models
	 */
	public MapSelectionView(StateManager stateManager) {
		this.controller = stateManager.getMainMenuController();
		this.model = controller.getMainMenuModel();
		this.availableMaps = new ArrayList<>();
		initializeAvailableMaps();
	}

	/**
	 * Initializes the list of available maps.
	 */
	private void initializeAvailableMaps() {
		availableMaps.add(new BasicMap());
		availableMaps.add(new SpiralMap());
	}

	@Override
	public void show() {
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);

		skin = new Skin(Gdx.files.internal("uiskin.json"));

		Table mainTable = new Table();
		mainTable.setFillParent(true);
		mainTable.pad(20);
		stage.addActor(mainTable);

		Label titleLabel = new Label("Map Selection", skin);
		titleLabel.setFontScale(1.5f);
		mainTable.add(titleLabel).colspan(2).padBottom(20).row();

		Table mapListTable = new Table();
		mapListTable.top();

		Label mapListTitle = new Label("Available Maps", skin);
		mapListTable.add(mapListTitle).padBottom(10).row();

		for (GameMap mapInfo : availableMaps) {
			TextButton mapButton = new TextButton(mapInfo.getMapName(), skin);
			mapButton.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					selectMap(mapInfo);
				}
			});
			mapListTable.add(mapButton).fillX().uniformX().width(200).pad(5).row();
		}

		ScrollPane mapListScrollPane = new ScrollPane(mapListTable, skin);
		mapListScrollPane.setFadeScrollBars(false);
		mainTable.add(mapListScrollPane).width(250).expandY().fillY().padRight(20);

		detailsTable = new Table();
		detailsTable.top();
		updateDetailsPanel();

		ScrollPane detailsScrollPane = new ScrollPane(detailsTable, skin);
		detailsScrollPane.setFadeScrollBars(false);
		mainTable.add(detailsScrollPane).width(400).expandY().fillY().row();

		TextButton backButton = new TextButton("Back", skin);
		backButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				controller.onBackClicked();
			}
		});
		mainTable.add(backButton).colspan(2).width(150).padTop(20);

		// select the first map by default
		if (!availableMaps.isEmpty()) {
			selectMap(availableMaps.get(0));
		}
	}

	/**
	 * Selects a map and updates the details panel.
	 *
	 * @param map The map information to display
	 */
	private void selectMap(GameMap map) {
		this.selectedMap = map;
		controller.loadSavesForMap(map.getMapName());
		updateDetailsPanel();
	}

	/**
	 * Updates the details panel with information about the selected map.
	 */
	private void updateDetailsPanel() {
		detailsTable.clear();

		if (selectedMap == null) {
			Label noSelectionLabel = new Label("Select a map to view details", skin);
			detailsTable.add(noSelectionLabel).pad(20);
			return;
		}

		Label mapNameLabel = new Label(selectedMap.getMapName(), skin);
		mapNameLabel.setFontScale(1.2f);
		detailsTable.add(mapNameLabel).padBottom(15).row();

		Label descriptionLabel = new Label(selectedMap.getDescription(), skin);
		descriptionLabel.setWrap(true);
		detailsTable.add(descriptionLabel).width(350).padBottom(20).row();

		TextButton newGameButton = new TextButton("New Game", skin);
		newGameButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				controller.onNewGameClicked(selectedMap);
			}
		});

		detailsTable.add(newGameButton).fillX().width(350).padBottom(20).row();

		Label savesTitle = new Label("Existing Saves", skin);
		savesTitle.setFontScale(1.1f);
		detailsTable.add(savesTitle).padBottom(10).row();

		List<SavedGameState> saves = model.getExistingSaves();

		if (saves.isEmpty()) {
			Label noSavesLabel = new Label("No existing saves", skin);
			detailsTable.add(noSavesLabel).padBottom(15).row();
		} else {
			for (SavedGameState save : saves) {
				Table saveTable = new Table(skin);
				saveTable.add("Profile: " + save.profileName).left().row();
				saveTable.add("Round: " + (save.currentRoundIndex + 1)).left().row();
				saveTable.add("Lives: " + save.lives).left().row();
				saveTable.add("Currency: " + save.currency).left().row();
				saveTable.add("Score: " + save.score).left().row();

				TextButton loadButton = new TextButton("Load", skin);
				loadButton.addListener(new ChangeListener() {
					@Override
					public void changed(ChangeEvent event, Actor actor) {
						controller.onLoadSaveClicked(selectedMap, save);
					}
				});
				saveTable.add(loadButton).padTop(10);

				detailsTable.add(saveTable).fillX().pad(10).row();
			}
		}

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
	}
}
