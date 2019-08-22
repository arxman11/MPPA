package mppa.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mppa.MainApp;

public class TempEditController {
	@FXML
	private TextField tempField;
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
		tempField.setText(Integer.toString(mainApp.getTemp()));
	}

	@FXML
	private void handleOk() {
		if (isInputValid()) {
			okClicked = true;
			mainApp.setTemp(Integer.valueOf(tempField.getText()));
			dialogStage.close();
		}
	}

	public String getTemp() {
		return tempField.getText();
	}

	@FXML
	public void onEnter(ActionEvent ae) {
		handleOk();
	}

	/**
	 * Called when the user clicks cancel.
	 */
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	public boolean isNumeric(String s) {
		if (s.matches("\\d+")) {
			return true;
		} else if (s.charAt(0)=='-' && s.substring(1).matches("\\d+")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Validates the user input in the text fields.
	 * 
	 * @return true if the input is valid
	 */
	private boolean isInputValid() {
		String errorMessage = "";

		if (!isNumeric(tempField.getText())) {
			errorMessage += "Vale formaat!\n";
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message.
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Pole arv");
			alert.setHeaderText("Paranda sisestus");
			alert.setContentText(errorMessage);

			alert.showAndWait();

			return false;
		}
	}
}
