package mppa.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mppa.MainApp;
/**
 * Class for changing temperature.
 *
 * @author Argo Neumann
 */
public class TempEditController {
	/** Temperature field.*/
	@FXML
	private TextField tempField;
	/** Dialog stage.*/
	private Stage dialogStage;
	/** Boolean for confirmation button.*/
	private boolean okClicked = false;
	/** Main application. */
	private MainApp mainApp;
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		
	}
	/**
	 * Sets the stage of this dialog.
	 * @param dialogStage Dialog Stage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	/** Returns true if confirmation button is pressed, false if not.
	 * @return True if confirmation is pressed, false if not
	 *  */
	public boolean isOkClicked() {
		return okClicked;
	}
	/** Set´s class main application and sets temperature field to previous value.
	 * @param mainApp MainApp object
	 *  */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		tempField.setText(Integer.toString(mainApp.getTemp()));
	}
	/** Called when confirmation button is clicked
	 *  Sets main application´s temperature to new value.
	 *  Sets {@link #okClicked okClicked} to true and closes dialog.
	 *  */
	@FXML
	private void handleOk() {
		if (isInputValid()) {
			okClicked = true;
			mainApp.setTemp(Integer.valueOf(tempField.getText()));
			dialogStage.close();
		}
	}
	/** Calls {@link #handleOk() handleOk()} method if Enter key is pressed.
	 * @param ae ActionEvent.
	 *  */
	@FXML
	public void onEnter(ActionEvent ae) {
		handleOk();
	}

	/**
	 * Called when the user clicks cancel.
	 * Closes stage.
	 */
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}
	/** Returns true if given string is numeric and false if it´s not. Also checks for negative value for temperature.
	 * @param s String to be checked.
	 * @return True if input is numeric, false if not.
	 *  */
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
