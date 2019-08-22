package mppa.view;

import java.util.Optional;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import mppa.MainApp;
import mppa.model.Target;

/**
 * Class for controlling target data and displaying active target.
 * 
 *
 * @author Argo Neumann
 */
public class TargetController {
	/** ComboBox containing all the targets. */
	@FXML
	private ComboBox<Target> targetBox;
	/** Label of target´s name. */
	@FXML
	private Label targetNameLabel;
	/** Label of target´s easting. */
	@FXML
	private Label eastingLabel;
	/** Label of target´s northing. */
	@FXML
	private Label northingLabel;
	/** Label of direction angle. */
	@FXML
	private Label directionAngleLabel;
	/** Main Application. */
	private MainApp mainApp;

	/**
	 * The constructor. The constructor is called before the initialize()
	 * method.
	 */
	public TargetController() {
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		showTargetDetails(null);
		targetBox.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> update(newValue));

	}

	/**
	 * Updates all labels if new target is added or previous one is updated.
	 * 
	 * @param newTarget
	 *            New target
	 */
	private void update(Target newTarget) {
		showTargetDetails(newTarget);
		if (mainApp.getTargetData().isEmpty()) {
			mainApp.resetPrevTarget();
			mainApp.SetIsActiveTarget(false);
		} else {
			mainApp.SetIsActiveTarget(true);
			mainApp.setActiveTarget(newTarget);
		}

	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 * Adds all targets to ComboBox. If any target is added, selects newly added
	 * target.
	 * 
	 * @param mainApp
	 *            Main Application
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		ObservableList<Target> targets = mainApp.getTargetData();
		// Add observable list data to the table
		targetBox.setItems(targets);
		targets.addListener(new ListChangeListener<Target>() {
			@Override
			public void onChanged(ListChangeListener.Change c) {
				while (c.next()) {
					if (c.wasAdded()) {
						targetBox.getSelectionModel().select(targets.size() - 1);
					}

				}

			}
		});
	}

	/**
	 * Updates all targets names.
	 */
	private void updateBoxNames() {
		for (Target c : targetBox.getItems()) {
			if (c.isNameSet() == false) {
				c.updateName();
			}

		}
	}

	/**
	 * Called when target is changed. Calls {@link #showTargetDetails(Target)
	 * showTargetDetails(Target)} to update target information to be displayed.
	 */
	@FXML
	private void changeActiveTarget() {
		Target selected = targetBox.getSelectionModel().getSelectedItem();
		showTargetDetails(selected);
	}

	/**
	 * Called when user wants to delete target and presses "Kustuta SM" button.
	 * Alert is shown. If user wants to delete target, deletes all target´s
	 * information from graphics. Deletes target from ComboBox and main
	 * applications target data.
	 */
	@FXML
	private void handleDeleteTarget() {
		Target selectedTarget = targetBox.getSelectionModel().getSelectedItem();
		if (selectedTarget != null) {
			int selectedIndex = targetBox.getSelectionModel().getSelectedIndex();
			String targetName = selectedTarget.getTargetname();
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Kas soovid sihtmärki kustutada?");
			alert.setHeaderText("Kas soovid sihtmärki kustutada?");
			ButtonType kustuta = new ButtonType("Kustuta");
			ButtonType katkesta = new ButtonType("Katkesta", ButtonData.CANCEL_CLOSE);
			alert.getButtonTypes().setAll(kustuta, katkesta);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == kustuta) {
				targetBox.getItems().remove(selectedIndex);
				mainApp.getCanvas().deleteTarget(targetName);
				if (selectedTarget.isRegion()) {
					mainApp.getCanvas().deleteTarget(targetName + "1");
					mainApp.getCanvas().deleteTarget(targetName + "2");
					mainApp.getCanvas().deleteTarget(targetName + "3");
					if (!selectedTarget.getRegionTargets().get(0).getLastCorrection().isEmpty()) {
						mainApp.getCanvas().deleteBallisticPoint("1K" + targetName);
						mainApp.getCanvas().deleteBallisticPoint("1S" + targetName);
						mainApp.getCanvas().deleteBallisticPoint("1V" + targetName);
						mainApp.getCanvas().deleteBallisticPoint("2K" + targetName);
						mainApp.getCanvas().deleteBallisticPoint("2S" + targetName);
						mainApp.getCanvas().deleteBallisticPoint("2V" + targetName);
						mainApp.getCanvas().deleteBallisticPoint("3K" + targetName);
						mainApp.getCanvas().deleteBallisticPoint("3S" + targetName);
						mainApp.getCanvas().deleteBallisticPoint("3V" + targetName);
					}

				}
				mainApp.getCanvas().deleteForwardObserver("fo" + targetName);
				mainApp.getCanvas().deleteBallisticPoint("K" + targetName);
				mainApp.getCanvas().deleteBallisticPoint("S" + targetName);
				mainApp.getCanvas().deleteBallisticPoint("V" + targetName);
				mainApp.getTargetData().remove(selectedTarget);
			}
		} else {
			// Nothing selected.
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Puudub SM.");
			alert.setHeaderText("Ühtegi sihtmärki pole valitud.");
			alert.setContentText("Vali tabelist sihtmärk.");
			alert.showAndWait();
		}
	}

	/**
	 * Sets new information to target labels.
	 * 
	 * @param target
	 *            Target
	 */
	private void showTargetDetails(Target target) {
		if (target != null) {
			// Fill the labels with info from the target object.
			eastingLabel.setText(target.getEasting());
			northingLabel.setText(target.getNorthing());
			// directionAngleLabel.setText(target.getDirectionAngle());

		} else {
			// Target is null, remove all the text.
			eastingLabel.setText("");
			northingLabel.setText("");
		}
	}

	/**
	 * Called when user wants to edit target data and presses "Muuda" button.
	 * Calls main application´s target editing dialog.
	 */
	@FXML
	private void handleEditTarget() {
		Target selectedTarget = targetBox.getSelectionModel().getSelectedItem();
		if (selectedTarget != null) {
			String previousName = selectedTarget.getTargetname();
			boolean okClicked = mainApp.showTargetEditDialog(selectedTarget);
			if (okClicked) {
				mainApp.getCanvas().deleteTarget(previousName);

				mainApp.getCanvas().drawTarget(selectedTarget.getEasting(), selectedTarget.getNorthing(),
						selectedTarget.getTargetname(), "", true);

				showTargetDetails(selectedTarget);
				targetBox.setItems(mainApp.getTargetData());
				updateBoxNames();
			}

		} else {
			// Nothing selected.
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Puudub valik");
			alert.setHeaderText("Ühtegi sihtmärki pole valitud");
			alert.setContentText("Vali sihtmärk.");
			alert.showAndWait();
		}
	}

	/**
	 * Called when user wants to confirm target and presses "Kinnita SM" button.
	 * Displays new dialog for user to insert target name. Removes all ballistic
	 * points from graphics and draws new topographic target.
	 */
	@FXML
	private void handleConfirmTarget() {
		Target selectedTarget = targetBox.getSelectionModel().getSelectedItem();
		if (selectedTarget != null) {
			String previousName = selectedTarget.getTargetname();
			TextInputDialog dialog = new TextInputDialog("SM nimi");
			dialog.setTitle("Kinnita SM");
			dialog.setHeaderText("Sisesta sihtmärgi nimetus");
			dialog.setContentText("SM nimetus:");
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()) {
				selectedTarget.setTargetName(result.get());
				selectedTarget.setNameSet(true);
				if (selectedTarget.isRegion()) {
					mainApp.getCanvas().deleteTarget(previousName + "1");
					mainApp.getCanvas().deleteTarget(previousName + "2");
					mainApp.getCanvas().deleteTarget(previousName + "3");
					if (!selectedTarget.getRegionTargets().get(0).getLastCorrection().isEmpty()) {
						mainApp.getCanvas().deleteBallisticPoint("1K" + previousName);
						mainApp.getCanvas().deleteBallisticPoint("1S" + previousName);
						mainApp.getCanvas().deleteBallisticPoint("1V" + previousName);
						mainApp.getCanvas().deleteBallisticPoint("2K" + previousName);
						mainApp.getCanvas().deleteBallisticPoint("2S" + previousName);
						mainApp.getCanvas().deleteBallisticPoint("2V" + previousName);
						mainApp.getCanvas().deleteBallisticPoint("3K" + previousName);
						mainApp.getCanvas().deleteBallisticPoint("3S" + previousName);
						mainApp.getCanvas().deleteBallisticPoint("3V" + previousName);
					}
				} else {
					mainApp.getCanvas().deleteTarget(previousName);
				}
				mainApp.getCanvas().drawTarget(selectedTarget.getEasting(), selectedTarget.getNorthing(),
						selectedTarget.getTargetname(), "", true);
				// draw 1500m circle?
				showTargetDetails(selectedTarget);
				targetBox.setItems(mainApp.getTargetData());
				updateBoxNames();
			}
		}

		else {
			// Nothing selected.
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Puudub valik");
			alert.setHeaderText("Ühtegi sihtmärki pole valitud");
			alert.setContentText("Vali sihtmärk.");

			alert.showAndWait();
		}
	}

	/**
	 * Called when user wants to make correction to current target and presses
	 * "Korrektuur" button. Calls main application´s target correction
	 * displaying method. After new correction, deletes all ballistic points and
	 * target.
	 */
	@FXML
	private void handleTargetCorrection() {
		Target selectedTarget = targetBox.getSelectionModel().getSelectedItem();
		if (selectedTarget != null) {
			String targetName = selectedTarget.getTargetname();
			boolean okClicked = mainApp.showTargetCorrectionDialog(selectedTarget);
			if (okClicked) {
				if (selectedTarget.isRegion()) {
					if (!selectedTarget.getRegionTargets().get(0).getLastCorrection().isEmpty()) {
						mainApp.getCanvas().deleteBallisticPoint("1K" + targetName);
						mainApp.getCanvas().deleteBallisticPoint("1S" + targetName);
						mainApp.getCanvas().deleteBallisticPoint("1V" + targetName);
						mainApp.getCanvas().deleteBallisticPoint("2K" + targetName);
						mainApp.getCanvas().deleteBallisticPoint("2S" + targetName);
						mainApp.getCanvas().deleteBallisticPoint("2V" + targetName);
						mainApp.getCanvas().deleteBallisticPoint("3K" + targetName);
						mainApp.getCanvas().deleteBallisticPoint("3S" + targetName);
						mainApp.getCanvas().deleteBallisticPoint("3V" + targetName);
					}
				} else {
					mainApp.getCanvas().deleteBallisticPoint("K" + targetName);
					mainApp.getCanvas().deleteBallisticPoint("S" + targetName);
					mainApp.getCanvas().deleteBallisticPoint("V" + targetName);
				}

				mainApp.setActiveTarget(selectedTarget);

			}

		} else {
			// Nothing selected.
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Puudub valik");
			alert.setHeaderText("Ühtegi sihtmärki pole valitud");
			alert.setContentText("Vali sihtmärk.");
			alert.showAndWait();
		}
	}

	/**
	 * Called when user wants to view current target´s information and user
	 * presses "SM INFO" button. Calls main application´s target correction
	 * displaying method.
	 */
	@FXML
	private void handleTargetInformation() {
		Target selectedTarget = targetBox.getSelectionModel().getSelectedItem();
		if (selectedTarget != null) {
			boolean okClicked = mainApp.showTargetInformationDialog(selectedTarget);
			if (okClicked) {

			}

		} else {
			// Nothing selected.
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Puudub valik");
			alert.setHeaderText("Uhtegi sihtmärki pole valitud");
			alert.setContentText("Vali sihtmĆ¤rk.");
			alert.showAndWait();
		}
	}
	/**
	 * Called when user wants to delete last made correction and presses "Tühista korrektuur" button.
	 * Alert is shown. When user wants to delete last correction, all the ballistic points are deleted.
	 */
	@FXML
	private void handleLastTargetCorrectionDelete() {
		Target selectedTarget = targetBox.getSelectionModel().getSelectedItem();
		if (selectedTarget != null) {
			String targetName = selectedTarget.getTargetname();
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Kas soovid korrektuuri kustutada?");
			alert.setHeaderText("Kas soovid viimast korrektuuri kustutada?");

			ButtonType kustuta = new ButtonType("Kustuta");
			ButtonType katkesta = new ButtonType("Katkesta", ButtonData.CANCEL_CLOSE);
			alert.getButtonTypes().setAll(kustuta, katkesta);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == kustuta) {
				if (selectedTarget.isRegion()) {
					if (!selectedTarget.getRegionTargets().get(0).getLastCorrection().isEmpty()) {
						mainApp.getCanvas().deleteBallisticPoint("1K" + targetName);
						mainApp.getCanvas().deleteBallisticPoint("1S" + targetName);
						mainApp.getCanvas().deleteBallisticPoint("1V" + targetName);
						mainApp.getCanvas().deleteBallisticPoint("2K" + targetName);
						mainApp.getCanvas().deleteBallisticPoint("2S" + targetName);
						mainApp.getCanvas().deleteBallisticPoint("2V" + targetName);
						mainApp.getCanvas().deleteBallisticPoint("3K" + targetName);
						mainApp.getCanvas().deleteBallisticPoint("3S" + targetName);
						mainApp.getCanvas().deleteBallisticPoint("3V" + targetName);
						for (Target i : selectedTarget.getRegionTargets()) {
							i.deleteLastCorrection();
						}
						selectedTarget.deleteLastCorrection();
					}

				} else {
					selectedTarget.deleteLastCorrection();
					mainApp.getCanvas().deleteBallisticPoint("K" + targetName);
					mainApp.getCanvas().deleteBallisticPoint("S" + targetName);
					mainApp.getCanvas().deleteBallisticPoint("V" + targetName);
				}
				mainApp.setActiveTarget(selectedTarget);
			} else {

			}

		}

		else {
			// Nothing selected.
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Puudub valik");
			alert.setHeaderText("Ühtegi sihtmärki pole valitud");
			alert.setContentText("Vali sihtmärk.");
			alert.showAndWait();
		}
	}
}
