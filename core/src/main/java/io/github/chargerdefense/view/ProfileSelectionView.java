package io.github.chargerdefense.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import io.github.chargerdefense.controller.MainMenuController;
import io.github.chargerdefense.controller.StateManager;
import io.github.chargerdefense.model.MainMenuModel;
import io.github.chargerdefense.model.MainMenuObserver;

import java.util.List;

/**
 * The profile selection screen that displays all available profiles
 * and allows the user to select one, create a new profile, or return to the
 * main menu.
 */
public class ProfileSelectionView implements Screen, MainMenuObserver {
	/** The controller for handling profile selection logic */
	private final MainMenuController controller;
	/** The model for managing profile data */
	private final MainMenuModel model;
	/** The libGDX stage for organizing and rendering UI elements */
	private Stage stage;
	/** The skin containing UI styling and texture information */
	private Skin skin;
	/** The table containing the list of profile buttons */
	private Table profileListTable;

	/**
	 * Constructs a new ProfileSelectionView with the specified state manager.
	 *
	 * @param stateManager The state manager containing controllers and models
	 */
	public ProfileSelectionView(StateManager stateManager) {
		this.controller = stateManager.getMainMenuController();
		this.model = stateManager.getMainMenuModel();
	}

	@Override
	public void show() {
		stage = new Stage(new ScreenViewport());
		Gdx.input.setInputProcessor(stage);

		skin = new Skin(Gdx.files.internal("uiskin.json"));

		model.addObserver(this);

		Table mainTable = new Table();
		mainTable.setFillParent(true);
		stage.addActor(mainTable);

		Label titleLabel = new Label("Select Profile", skin);
		mainTable.add(titleLabel).padBottom(20).row();

		profileListTable = new Table();
		updateProfileList(model.getExistingProfiles());

		ScrollPane scrollPane = new ScrollPane(profileListTable, skin);
		scrollPane.setFadeScrollBars(false);
		mainTable.add(scrollPane).width(300).height(300).padBottom(20).row();

		TextButton createNewButton = new TextButton("Create New Profile", skin);
		mainTable.add(createNewButton).fillX().uniformX().padBottom(10).row();

		createNewButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				ProfileSelectionView.this.showCreateProfileDialog();
			}
		});

		TextButton backButton = new TextButton("Back", skin);
		mainTable.add(backButton).fillX().uniformX().row();

		backButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				controller.onBackClicked();
			}
		});
	}

	/**
	 * Shows a modal dialog for creating a new profile.
	 * Contains a text field for the profile name and confirm/cancel buttons.
	 */
	private void showCreateProfileDialog() {
		Dialog dialog = new Dialog("Create New Profile", skin) {
			@Override
			protected void result(Object object) {
				if ((Boolean) object) {
					TextField textField = findActor("profileNameField");
					if (textField != null) {
						String profileName = textField.getText().trim();
						if (!profileName.isEmpty()) {
							boolean success = model.createNewProfile(profileName);
							if (success) {
								controller.onProfileSelected(profileName);
							} else {
								showErrorDialog("Profile Creation Failed",
										"A profile with that name already exists or the name is invalid.");
							}
						} else {
							showErrorDialog("Invalid Name", "Profile name cannot be empty.");
						}
					}
				}
			}
		};

		Table content = dialog.getContentTable();
		content.pad(20);

		Label instructionLabel = new Label("Enter profile name:", skin);
		content.add(instructionLabel).padBottom(10).row();

		TextField nameField = new TextField("", skin);
		nameField.setName("profileNameField");
		nameField.setMessageText("Profile name");
		nameField.setMaxLength(20);
		content.add(nameField).width(200).padBottom(20).row();

		dialog.button("Confirm", true);
		dialog.button("Cancel", false);

		dialog.key(com.badlogic.gdx.Input.Keys.ENTER, true);
		dialog.key(com.badlogic.gdx.Input.Keys.ESCAPE, false);

		dialog.show(stage);

		stage.setKeyboardFocus(nameField);
	}

	/**
	 * Shows an error dialog with the specified title and message.
	 *
	 * @param title   The dialog title
	 * @param message The error message to display
	 */
	private void showErrorDialog(String title, String message) {
		Dialog errorDialog = new Dialog(title, skin);
		errorDialog.text(message);
		errorDialog.button("OK");
		errorDialog.show(stage);
	}

	/**
	 * Updates the profile list with the current profiles from the model.
	 *
	 * @param profiles The list of profile names to display
	 */
	private void updateProfileList(List<String> profiles) {
		profileListTable.clear();

		if (profiles.isEmpty()) {
			Label emptyLabel = new Label("No profiles found", skin);
			profileListTable.add(emptyLabel).pad(10);
		} else {
			for (String profileName : profiles) {
				TextButton profileButton = new TextButton(profileName, skin);

				if (profileName.equals(model.getActiveProfile())) {
					profileButton.setChecked(true);
				}

				profileButton.addListener(new ChangeListener() {
					@Override
					public void changed(ChangeEvent event, Actor actor) {
						controller.onProfileSelected(profileName);
					}
				});

				profileListTable.add(profileButton).fillX().uniformX().pad(5).row();
			}
		}
	}

	/**
	 * Renders the profile selection screen.
	 *
	 * @param delta The time in seconds since the last render
	 */
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
		model.removeObserver(this);
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
	}

	@Override
	public void onProfilesLoaded(List<String> profiles) {
		updateProfileList(profiles);
	}

	@Override
	public void onActiveProfileChanged(String activeProfile) {
		updateProfileList(model.getExistingProfiles());
	}

	@Override
	public void onLoadingStateChanged(boolean isLoading) {
		// TODO show a loading indicator
	}
}
