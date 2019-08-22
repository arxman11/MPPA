package mppa.view;

import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import mppa.MainApp;
import mppa.model.Target;

public class PositionController {
	@FXML
	private TableView<Target> targetTable;

	@FXML
	private Label positionNameLabel;
	@FXML
	private Label firstEastingLabel;
	@FXML
	private Label firstNorthingLabel;
	@FXML
	private Label firstMainDirectionLabel;
	@FXML
	private Label firstBackUpDirectionLabel;
	@FXML
	private Label secondEastingLabel;
	@FXML
	private Label secondNorthingLabel;
	@FXML
	private Label secondMainDirectionLabel;
	@FXML
	private Label secondBackUpDirectionLabel;
	@FXML
	private Label thirdEastingLabel;
	@FXML
	private Label thirdNorthingLabel;
	@FXML
	private Label thirdMainDirectionLabel;
	@FXML
	private Label thirdBackUpDirectionLabel;
	@FXML
	private Label tempLabel;
	@FXML
	private Label ammoLabel;

	private MainApp mainApp;

	public PositionController() {
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		tempLabel.setText(mainApp.getTempString());
		setAmmoLabel(this.mainApp.getAmmo());

	}

	@FXML
	private void initialize() {
		showInfo();

	}

	public void updateFields() {
		firstEastingLabel.setText(mainApp.getPositionData().get(0).getEasting());
		firstNorthingLabel.setText(mainApp.getPositionData().get(0).getNorthing());
		firstMainDirectionLabel.setText(toDirection(mainApp.getPositionData().get(0).getMainDirection()));
		firstBackUpDirectionLabel.setText(toDirection(mainApp.getPositionData().get(0).getBackupDirection()));
		secondEastingLabel.setText(mainApp.getPositionData().get(1).getEasting());
		secondNorthingLabel.setText(mainApp.getPositionData().get(1).getNorthing());
		secondMainDirectionLabel.setText(toDirection(mainApp.getPositionData().get(1).getMainDirection()));
		secondBackUpDirectionLabel.setText(toDirection(mainApp.getPositionData().get(1).getBackupDirection()));
		thirdEastingLabel.setText(mainApp.getPositionData().get(2).getEasting());
		thirdNorthingLabel.setText(mainApp.getPositionData().get(2).getNorthing());
		thirdMainDirectionLabel.setText(toDirection(mainApp.getPositionData().get(2).getMainDirection()));
		thirdBackUpDirectionLabel.setText(toDirection(mainApp.getPositionData().get(2).getBackupDirection()));
	}

	private String toDirection(String s) {
		if (s.length() < 4) {
			return "";
		} else {
			s = new StringBuilder(s).insert(s.length() - 2, "-").toString();

			return s;
		}
	}

	private void showInfo() {

	}

	private void setAmmoLabel(int ammo) {
		switch (ammo) {
		case 0:
			ammoLabel.setText("120mm");
			break;
		case 1:
			ammoLabel.setText("81mm");
			break; 
		default:
			ammoLabel.setText("120mm");
			break;
		}
	}

	@FXML
	private void handleNewTarget() {
		Target tempTarget = new Target();
		boolean okClicked = mainApp.showSquareTargetAdd(tempTarget);
		if (okClicked) {
			mainApp.SetIsActiveTarget(true);
			mainApp.getTargetData().add(tempTarget);
		}
	}

	// TODO-uuenda andmeid temp muutudes
	@FXML
	private void handleTempEdit() {
		boolean okClicked = mainApp.showTempEditDialog();
		if (okClicked) {
			tempLabel.setText(mainApp.getTempString());
		}
	}

	@FXML
	private void handleAmmoEdit() {
		boolean okClicked = mainApp.showAmmoEditDialog();
		if (okClicked) {
			setAmmoLabel(mainApp.getAmmo());
			mainApp.updateAmmo();
			// To-DO
		}
	}

	@FXML
	private void handleNewTukTarget() {
		Target tempTarget = new Target();
		boolean okClicked = mainApp.showTukTargetAdd(tempTarget);
		if (okClicked) {
			mainApp.SetIsActiveTarget(true);
			mainApp.getTargetData().add(tempTarget);
		}
	}

	@FXML
	private void handlePositionEdit() {
		boolean okClicked = mainApp.showPositionEditDialog();
		if (okClicked) {
			updateFields();
		}

	}

	@FXML
	private void handleNewPolarTarget() {
		Target tempTarget = new Target();
		boolean okClicked = mainApp.showPolarTargetEditDialog(tempTarget);
		if (okClicked) {
			mainApp.SetIsActiveTarget(true);
			mainApp.getTargetData().add(tempTarget);
			
		}
	}
	@FXML
	private void handleExitProgram(){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Kas soovid programmi sulgeda?");
		alert.setHeaderText("Kas soovid programmi sulgeda?");

		ButtonType sulge = new ButtonType("Sulge");
		ButtonType katkesta = new ButtonType("Katkesta",ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(sulge,katkesta);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == sulge){
			System.exit(0);
		} else {
			
		}
		
	}
	@FXML
	private void handleLimitersEdit() {
		boolean okClicked = mainApp.showLimitersEditDialog();
		if (okClicked) {
		}

	}

}
