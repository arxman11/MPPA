package mppa.view;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import mppa.MainApp;

public class AddLimiter {
	@FXML
	private TextField easting;
	@FXML
	private TextField northing;
	private Stage dialogStage;
	private MainApp mainApp;
	private ArrayList<ArrayList<Integer>> limiters;
	private boolean okClicked = false;
	@FXML
	private void initialize() {
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		this.limiters = mainApp.getLimiters();
	}
	
	public boolean isNumeric(String s) {
		return s.matches("\\d+");
	}
	@FXML
	public void onEnter(ActionEvent ae) {
		handleOk();
	}

	/**
	 * Sets the stage of this dialog.
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	@FXML
	private void handleOk() {
		if(isInputValid()){
			ArrayList<Integer> limiterPoint=new ArrayList<Integer>();
			limiterPoint.add(Integer.parseInt(easting.getText()));
			limiterPoint.add(Integer.parseInt(northing.getText()));
			mainApp.getLimiters().add(limiterPoint);
			okClicked = true;
			dialogStage.close();
		}
		

	}
	private boolean isInputValid() {
		String errorMessage = "";

		if (easting.getText().length() != 4 || !isNumeric(easting.getText())) {
			errorMessage += "Vales formaadis easting!\n";
		}
		if (northing.getText().length() != 4 || !isNumeric(northing.getText())) {
			errorMessage += "Vales formaadis northing!\n";
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
