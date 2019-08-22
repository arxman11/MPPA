package mppa.view;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import mppa.MainApp;
/**
 * Class for adding graphical limitation´s coordinates.
 *
 * @author Argo Neumann
 */
public class AddLimiterController {
	/** Limitation point s easting. */
	@FXML
	private TextField easting;
	/** Limitation point s northing. */
	@FXML
	private TextField northing;
	/** Dialog Stage. */
	private Stage dialogStage;
	/** Main application. */
	private MainApp mainApp;
	/** Array of coordinates for limitation. */
	private ArrayList<ArrayList<Integer>> limiters;
	/** Boolean for confirmation button.*/
	private boolean okClicked = false;
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
	}
	/** Set´s class main application and main application limiters to {@link #limiters limiters}.
	 * @param mainApp MainApp 
	 *  */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		this.limiters = mainApp.getLimiters();
	}
	/** Returns true if giveen string is numeric and false if it´s not.
	 * @param s String to be checked
	 * @return True if input is numeric, false if not.
	 *  */
	public boolean isNumeric(String s) {
		return s.matches("\\d+");
	}
	/** Calls {@link #handleOk() handleOk()} method if Enter key is pressed.
	 * @param ae ActionEvent.
	 *  */
	@FXML
	public void onEnter(ActionEvent ae) {
		handleOk();
	}

	/**
	 * Sets the stage of this dialog.
	 * 
	 * @param dialogStage Given dialog Stage
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
	/** Closes stage if operation is cancelled.
	 *  */
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}
	/** Called when confirmation button is clicked
	 * Calls {@link #isInputValid() isInputValid()} method to check whether the
	 * easting and northing is valid and adds them to array.
	 * Array will be added to main application´s limitation array.
	 * Sets {@link #okClicked okClicked} to true and closes dialog.
	 *  */
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
	/** Checks if easting and northing are valid.
	 *  Calls Alert when fields are incorrect and notifys about user input mistakes.
	 *  @return True if valid inputs, false if invalid.
	 *  */
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
