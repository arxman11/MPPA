package mppa.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import mppa.MainApp;
import mppa.model.Position;

public class PositionEditController {
	@FXML
	private TextField firstEasting;
	@FXML
	private TextField firstNorthing;
	@FXML
	private TextField firstMainDirection;
	@FXML
	private TextField firstBackUpDirection;
	@FXML
	private TextField secondEasting;
	@FXML
	private TextField secondNorthing;
	@FXML
	private TextField secondMainDirection;
	@FXML
	private TextField secondBackUpDirection;
	@FXML
	private TextField thirdEasting;
	@FXML
	private TextField thirdNorthing;
	@FXML
	private TextField thirdMainDirection;
	@FXML
	private TextField thirdBackUpDirection;

	private Stage dialogStage;
	private boolean okClicked = false;
	private MainApp mainApp;

	@FXML
	private void initialize() {

	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public boolean isOkClicked() {
		return okClicked;
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

	}

	public void updateFields() {
		firstEasting.setText(mainApp.getPositionData().get(0).getEasting());
		firstNorthing.setText(mainApp.getPositionData().get(0).getNorthing());
		firstMainDirection.setText(mainApp.getPositionData().get(0).getMainDirection());
		firstBackUpDirection.setText(mainApp.getPositionData().get(0).getBackupDirection());
		secondEasting.setText(mainApp.getPositionData().get(1).getEasting());
		secondNorthing.setText(mainApp.getPositionData().get(1).getNorthing());
		secondMainDirection.setText(mainApp.getPositionData().get(1).getMainDirection());
		secondBackUpDirection.setText(mainApp.getPositionData().get(1).getBackupDirection());
		thirdEasting.setText(mainApp.getPositionData().get(2).getEasting());
		thirdNorthing.setText(mainApp.getPositionData().get(2).getNorthing());
		thirdMainDirection.setText(mainApp.getPositionData().get(2).getMainDirection());
		thirdBackUpDirection.setText(mainApp.getPositionData().get(2).getBackupDirection());
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	@FXML
	public void onEnter(ActionEvent ae) {
		handleOk();
	}

	@FXML
	private void handleOk() {
		Position first = mainApp.getPositionData().get(0);
		Position second = mainApp.getPositionData().get(1);
		Position third = mainApp.getPositionData().get(2);
		if (isInputValid()) {
			if (firstEasting.getText().length() > 0 && firstNorthing.getText().length() > 0) {
				first.setEasting(firstEasting.getText());
				first.setNorthing(firstNorthing.getText());
				if (firstMainDirection.getText().length() > 0) {
					first.setMainDirection(firstMainDirection.getText());
				}
				if (firstBackUpDirection.getText().length() > 0) {
					first.setBackUpDirection(firstBackUpDirection.getText());
				}
			}
			if (secondEasting.getText().length() > 0 && secondNorthing.getText().length() > 0) {
				second.setEasting(secondEasting.getText());
				second.setNorthing(secondNorthing.getText());
				if (secondMainDirection.getText().length() > 0) {
					second.setMainDirection(secondMainDirection.getText());
				}
				if (secondBackUpDirection.getText().length() > 0) {
					second.setBackUpDirection(secondBackUpDirection.getText());
				}
			}
			if (thirdEasting.getText().length() > 0 && thirdNorthing.getText().length() > 0) {
				third.setEasting(thirdEasting.getText());
				third.setNorthing(thirdNorthing.getText());
				if (thirdMainDirection.getText().length() > 0) {
					third.setMainDirection(thirdMainDirection.getText());
				}
				if (thirdBackUpDirection.getText().length() > 0) {
					third.setBackUpDirection(thirdBackUpDirection.getText());
				}
			}

			okClicked = true;
			dialogStage.close();
		}

	}

	public boolean isNumeric(String s) {
		return s.matches("\\d+");
	}

	private boolean isInputValid() {
		String errorMessage = "";
		if (firstEasting.getText().length() == 0 && firstNorthing.getText().length() > 0) {
			errorMessage += "Esimesel puuduv easting!\n";
		}
		if (firstEasting.getText().length() > 0 && firstNorthing.getText().length() == 0) {
			errorMessage += "Esimesel puuduv northing!\n";
		}
		if (secondEasting.getText().length() == 0 && secondNorthing.getText().length() > 0) {
			errorMessage += "Teisel puuduv easting!\n";
		}
		if (secondEasting.getText().length() > 0 && secondNorthing.getText().length() == 0) {
			errorMessage += "Teisel puuduv northing!\n";
		}
		if (thirdEasting.getText().length() == 0 && thirdNorthing.getText().length() > 0) {
			errorMessage += "Kolmandal puuduv easting!\n";
		}
		if (thirdEasting.getText().length() > 0 && thirdNorthing.getText().length() == 0) {
			errorMessage += "Kolmandal puuduv northing!\n";
		}

		if (firstEasting.getText().length() > 0 && firstNorthing.getText().length() > 0) {
			if (firstEasting.getText().length() != 4 || !isNumeric(firstEasting.getText())) {
				errorMessage += "Esimesel vales formaadis easting!\n";
			}
			if (firstNorthing.getText().length() != 4 || !isNumeric(firstNorthing.getText())) {
				errorMessage += "Esimesel vales formaadis northing!\n";
			}

		}
		if (secondEasting.getText().length() > 0 && secondNorthing.getText().length() > 0) {
			if (secondEasting.getText().length() != 4 || !isNumeric(secondEasting.getText())) {
				errorMessage += "Teisel vales formaadis easting!\n";
			}
			if (secondNorthing.getText().length() != 4 || !isNumeric(secondNorthing.getText())) {
				errorMessage += "Teisel vales formaadis northing!\n";
			}

		}
		if (thirdEasting.getText().length() > 0 && thirdNorthing.getText().length() > 0) {
			if (thirdEasting.getText().length() != 4 || !isNumeric(thirdEasting.getText())) {
				errorMessage += "Kolmandal vales formaadis easting!\n";
			}
			if (thirdNorthing.getText().length() != 4 || !isNumeric(thirdNorthing.getText())) {
				errorMessage += "Kolmandal vales formaadis northing!\n";
			}

		}
		if (firstMainDirection.getText().length() > 0) {
			if (firstMainDirection.getText().length() != 4 || !isNumeric(firstMainDirection.getText())) {
				errorMessage += "Esimesel vales formaadis põhisuund!\n";
			}
			else if(Double.parseDouble(firstMainDirection.getText())>6400.0){
				errorMessage += "Esimesel põhisuund liiga suur!\n";
				
			}
		}
		if (firstBackUpDirection.getText().length() > 0) {
			if (firstBackUpDirection.getText().length() != 4 || !isNumeric(firstBackUpDirection.getText())) {
				errorMessage += "Esimesel vales formaadis varusuund!\n";
			}
			else if(Double.parseDouble(firstBackUpDirection.getText())>6400.0){
				errorMessage += "Esimesel varusuund liiga suur!\n";
				
			}
		}
		if (secondMainDirection.getText().length() > 0) {
			if (secondMainDirection.getText().length() != 4 || !isNumeric(secondMainDirection.getText())) {
				errorMessage += "Teisel vales formaadis põhisuund!\n";
			}
			else if(Double.parseDouble(secondMainDirection.getText())>6400.0){
				errorMessage += "Teisel põhisuund liiga suur!\n";
				
			}
		}
		if (secondBackUpDirection.getText().length() > 0) {
			if (secondBackUpDirection.getText().length() != 4 || !isNumeric(secondBackUpDirection.getText())) {
				errorMessage += "Teisel vales formaadis varusuund!\n";
			}
			else if(Double.parseDouble(secondBackUpDirection.getText())>6400.0){
				errorMessage += "Teisel varusuund liiga suur!\n";
				
			}
		}
		if (thirdMainDirection.getText().length() > 0) {
			if (thirdMainDirection.getText().length() != 4 || !isNumeric(thirdMainDirection.getText())) {
				errorMessage += "Kolmandal vales formaadis põhisuund!\n";
			}
			else if(Double.parseDouble(thirdMainDirection.getText())>6400.0){
				errorMessage += "Kolmandal põhisuund liiga suur!\n";
				
			}
		}
		if (thirdBackUpDirection.getText().length() > 0) {
			if (thirdBackUpDirection.getText().length() != 4 || !isNumeric(thirdBackUpDirection.getText())) {
				errorMessage += "Kolmandal vales formaadis varusuund!\n";
			}
			else if(Double.parseDouble(thirdBackUpDirection.getText())>6400.0){
				errorMessage += "Kolmandal varusuund liiga suur!\n";
				
			}
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message.
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Ebakorrektsed väljad");
			alert.setHeaderText("Paranda vigased väljad");
			alert.setContentText(errorMessage);

			alert.showAndWait();

			return false;
		}
	}

}
