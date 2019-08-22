package mppa.view;

import java.util.ArrayList;
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
import mppa.MainApp;
import mppa.model.Target;

public class TargetController {
	@FXML
	private ComboBox<Target> targetBox;

	@FXML
	private Label targetNameLabel;
	@FXML
	private Label eastingLabel;
	@FXML
	private Label northingLabel;
	@FXML
	private Label directionAngleLabel;

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
				.addListener((observable, oldValue, newValue) -> showTargetDetails(newValue));
		targetBox.getSelectionModel().selectedItemProperty()
		.addListener((observable, oldValue, newValue) -> mainApp.SetIsActiveTarget(true));
		targetBox.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> mainApp.setActiveTarget(newValue));
		
		
	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 * 
	 * @param mainApp
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

	private void updateBoxNames() {
		for (Target c : targetBox.getItems()) {
			c.updateName();
		}
	}

	@FXML
	private void changeActiveTarget() {
		Target selected = targetBox.getSelectionModel().getSelectedItem();
		showTargetDetails(selected);
	}

	@FXML
	private void handleDeleteTarget() {
		int selectedIndex = targetBox.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Kas soovid sihtmärki kustutada?");
			alert.setHeaderText("Kas soovid sihtmärki kustutada?");

			ButtonType kustuta = new ButtonType("Kustuta");
			ButtonType katkesta = new ButtonType("Katkesta",ButtonData.CANCEL_CLOSE);
			alert.getButtonTypes().setAll(kustuta,katkesta);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == kustuta){
				targetBox.getItems().remove(selectedIndex);
			} else {
				
			}
			
		} else {
			// Nothing selected.
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Puudub SM.");
			alert.setHeaderText("Ć�htegi sihtmĆ¤rki pole valitud.");
			alert.setContentText("Vali tabelist sihtmĆ¤rk.");

			alert.showAndWait();
		}
	}

	private void showTargetDetails(Target target) {
		if (target != null) {
			// Fill the labels with info from the target object.
			eastingLabel.setText(target.getEasting());
			northingLabel.setText(target.getNorthing());
			// directionAngleLabel.setText(target.getDirectionAngle());

		} else {
			// Person is null, remove all the text.
			eastingLabel.setText("");
			northingLabel.setText("");
			// directionAngleLabel.setText("");
		}
	}

	@FXML
	private void handleEditTarget() {
		Target selectedTarget = targetBox.getSelectionModel().getSelectedItem();
		if (selectedTarget != null) {
			boolean okClicked = mainApp.showTargetEditDialog(selectedTarget);
			if (okClicked) {
				showTargetDetails(selectedTarget);
				targetBox.setItems(mainApp.getTargetData());
				updateBoxNames();
			}

		} else {
			// Nothing selected.
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Puudub valik");
			alert.setHeaderText("Ć�htegi SihtmĆ¤rki pole valitud");
			alert.setContentText("Vali sihtmĆ¤rk.");

			alert.showAndWait();
		}
	}

	@FXML
	private void handleTargetCorrection() {
		Target selectedTarget = targetBox.getSelectionModel().getSelectedItem();
		if (selectedTarget != null) {
			boolean okClicked = mainApp.showTargetCorrectionDialog(selectedTarget);
			if (okClicked) {
				mainApp.setActiveTarget(selectedTarget);
			}

		} else {
			// Nothing selected.
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Puudub valik");
			alert.setHeaderText("Ć�htegi SihtmĆ¤rki pole valitud");
			alert.setContentText("Vali sihtmĆ¤rk.");
			alert.showAndWait();
		}
	}
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
			alert.setHeaderText("Ühtegi sihtmärki pole valitud");
			alert.setContentText("Vali sihtmĆ¤rk.");
			alert.showAndWait();
		}
	}

	@FXML
	private void handleLastTargetCorrectionDelete() {
		Target selectedTarget = targetBox.getSelectionModel().getSelectedItem();
		if (selectedTarget != null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Kas soovid korrektuuri kustutada?");
			alert.setHeaderText("Kas soovid viimast korrektuuri kustutada?");

			ButtonType kustuta = new ButtonType("Kustuta");
			ButtonType katkesta = new ButtonType("Katkesta",ButtonData.CANCEL_CLOSE);
			alert.getButtonTypes().setAll(kustuta,katkesta);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == kustuta){
				selectedTarget.deleteLastCorrection();
				mainApp.setActiveTarget(selectedTarget);
			} else {
				
			}
		
		}

		else {
			// Nothing selected.
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Puudub valik");
			alert.setHeaderText("Ć�htegi SihtmĆ¤rki pole valitud");
			alert.setContentText("Vali sihtmĆ¤rk.");
			alert.showAndWait();
		}
	}
}
