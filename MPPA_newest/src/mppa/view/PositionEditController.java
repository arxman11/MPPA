package mppa.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import mppa.MainApp;
import mppa.model.Position;
/**
 * Class for editing Position information
 *
 * @author Argo Neumann
 */

public class PositionEditController {
	/** Field for first position´s easting. */
	@FXML
	private TextField firstEasting;
	/** Field for first position´s northing. */
	@FXML
	private TextField firstNorthing;
	/** Field for first position´s main direction. */
	@FXML
	private TextField firstMainDirection;
	/** Field for first position´s backup direction. */
	@FXML
	private TextField firstBackUpDirection;
	/** Field for second position´s easting. */
	@FXML
	private TextField secondEasting;
	/** Field for second position´s northing. */
	@FXML
	private TextField secondNorthing;
	/** Field for second position´s main direction. */
	@FXML
	private TextField secondMainDirection;
	/** Field for second position´s backup. */
	@FXML
	private TextField secondBackUpDirection;
	/** Field for third position´s easting. */
	@FXML
	private TextField thirdEasting;
	/** Field for third position´s northing. */
	@FXML
	private TextField thirdNorthing;
	/** Field for third position´s main direction. */
	@FXML
	private TextField thirdMainDirection;
	/** Field for third position´s backup direction. */
	@FXML
	private TextField thirdBackUpDirection;
	/** Dialog stage. */
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
	 * 
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
	/** Set´s class main application.
	 * @param mainApp MainApp object
	 *  */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	/** Sets all fields to main applications positions data.
	 *  */
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

	/** Closes stage if operation is cancelled.
	 *  */
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}
	/** Calls {@link #handleOk() handleOk()} method if Enter key is pressed.
	 * @param ae ActionEvent.
	 *  */
	@FXML
	public void onEnter(ActionEvent ae) {
		handleOk();
	}
	/** Called when confirmation button is clicked
	 *  Updates all fields in all main applications positions.
	 *  Removes all positions from graphical view and redraws them.
	 *  Sets {@link #okClicked okClicked} to true and closes dialog.
	 *  */
	@FXML
	public void handleOk() {
		Position first = mainApp.getPositionData().get(0);
		Position second = mainApp.getPositionData().get(1);
		Position third = mainApp.getPositionData().get(2);
		if (isInputValid()) {//Sets second position to center of the grid.
			if(secondEasting.getText().length() > 0 ){
				mainApp.getCanvas().setCenterLines(secondEasting.getText(), secondNorthing.getText());
			}
			else if(firstEasting.getText().length() > 0 ){
				mainApp.getCanvas().setCenterLines(firstEasting.getText(), firstNorthing.getText());
			}
			else if(thirdEasting.getText().length() > 0 ){
				mainApp.getCanvas().setCenterLines(thirdEasting.getText(), thirdNorthing.getText());
			}
			if (firstEasting.getText().length() > 0 && firstNorthing.getText().length() > 0) {
				first.setEasting(firstEasting.getText());
				first.setNorthing(firstNorthing.getText());
				if (firstMainDirection.getText().length() > 0) {
					first.setMainDirection(firstMainDirection.getText());
					mainApp.getCanvas().deletePosition(first.getpositionname());
					mainApp.getCanvas().drawPosition(first.getEasting(), first.getNorthing(), 1,
							first.getpositionname(), Integer.parseInt(first.getMainDirection()));

				} else {
					mainApp.getCanvas().deletePosition(first.getpositionname());
					mainApp.getCanvas().drawPosition(first.getEasting(), first.getNorthing(), 1,
							first.getpositionname());
				}
				if (firstBackUpDirection.getText().length() > 0) {
					first.setBackUpDirection(firstBackUpDirection.getText());
				}
			}
			else{
				mainApp.getCanvas().deletePosition(first.getpositionname());
			}
			if (secondEasting.getText().length() > 0 && secondNorthing.getText().length() > 0) {
				second.setEasting(secondEasting.getText());
				second.setNorthing(secondNorthing.getText());
				if (secondMainDirection.getText().length() > 0) {
					second.setMainDirection(secondMainDirection.getText());
					mainApp.getCanvas().deletePosition(second.getpositionname());
					mainApp.getCanvas().drawPosition(second.getEasting(), second.getNorthing(), 2,
							second.getpositionname(), Integer.parseInt(second.getMainDirection()));
				}
				else{
					mainApp.getCanvas().deletePosition(second.getpositionname());
					mainApp.getCanvas().drawPosition(second.getEasting(), second.getNorthing(), 2,
							second.getpositionname());
				}
				if (secondBackUpDirection.getText().length() > 0) {
					second.setBackUpDirection(secondBackUpDirection.getText());
				}
			}
			else{
				mainApp.getCanvas().deletePosition(second.getpositionname());
			}
			if (thirdEasting.getText().length() > 0 && thirdNorthing.getText().length() > 0) {
				third.setEasting(thirdEasting.getText());
				third.setNorthing(thirdNorthing.getText());
				if (thirdMainDirection.getText().length() > 0) {
					third.setMainDirection(thirdMainDirection.getText());
					mainApp.getCanvas().deletePosition(third.getpositionname());
					mainApp.getCanvas().drawPosition(third.getEasting(), third.getNorthing(), 3,
							third.getpositionname(), Integer.parseInt(third.getMainDirection()));
					
				}
				else{
					mainApp.getCanvas().deletePosition(third.getpositionname());
					mainApp.getCanvas().drawPosition(third.getEasting(), third.getNorthing(), 3,
							third.getpositionname());
				}
				if (thirdBackUpDirection.getText().length() > 0) {
					third.setBackUpDirection(thirdBackUpDirection.getText());
				}
			}
			else{
				mainApp.getCanvas().deletePosition(third.getpositionname());
			}

			okClicked = true;
			dialogStage.close();
		}

	}
	/** Called when data is being loaded from a text file.
	 *  Updates all fields in all main applications positions.
	 *  Removes all positions from graphical view and redraws them.
	 *  Sets {@link #okClicked okClicked} to true and closes dialog.
	 *  */
	public void handleOk1() {
		Position first = mainApp.getPositionData().get(0);
		Position second = mainApp.getPositionData().get(1);
		Position third = mainApp.getPositionData().get(2);
		if (isInputValid()) {
			if(secondEasting.getText().length() > 0 ){
				mainApp.getCanvas().setCenterLines(secondEasting.getText(), secondNorthing.getText());
			}
			else if(firstEasting.getText().length() > 0 ){
				mainApp.getCanvas().setCenterLines(firstEasting.getText(), firstNorthing.getText());
			}
			else if(thirdEasting.getText().length() > 0 ){
				mainApp.getCanvas().setCenterLines(thirdEasting.getText(), thirdNorthing.getText());
			}
			if (firstEasting.getText().length() > 0 && firstNorthing.getText().length() > 0) {
				first.setEasting(firstEasting.getText());
				first.setNorthing(firstNorthing.getText());
				if (firstMainDirection.getText().length() > 0) {
					first.setMainDirection(firstMainDirection.getText());
					mainApp.getCanvas().deletePosition(first.getpositionname());
					mainApp.getCanvas().drawPosition(first.getEasting(), first.getNorthing(), 1,
							first.getpositionname(), Integer.parseInt(first.getMainDirection()));

				} else {
					mainApp.getCanvas().deletePosition(first.getpositionname());
					mainApp.getCanvas().drawPosition(first.getEasting(), first.getNorthing(), 1,
							first.getpositionname());
				}
				if (firstBackUpDirection.getText().length() > 0) {
					first.setBackUpDirection(firstBackUpDirection.getText());
				}
			}
			else{
				mainApp.getCanvas().deletePosition(first.getpositionname());
			}
			if (secondEasting.getText().length() > 0 && secondNorthing.getText().length() > 0) {
				second.setEasting(secondEasting.getText());
				second.setNorthing(secondNorthing.getText());
				if (secondMainDirection.getText().length() > 0) {
					second.setMainDirection(secondMainDirection.getText());
					mainApp.getCanvas().deletePosition(second.getpositionname());
					mainApp.getCanvas().drawPosition(second.getEasting(), second.getNorthing(), 2,
							second.getpositionname(), Integer.parseInt(second.getMainDirection()));
				}
				else{
					mainApp.getCanvas().deletePosition(second.getpositionname());
					mainApp.getCanvas().drawPosition(second.getEasting(), second.getNorthing(), 2,
							second.getpositionname());
				}
				if (secondBackUpDirection.getText().length() > 0) {
					second.setBackUpDirection(secondBackUpDirection.getText());
				}
			}
			else{
				mainApp.getCanvas().deletePosition(second.getpositionname());
			}
			if (thirdEasting.getText().length() > 0 && thirdNorthing.getText().length() > 0) {
				third.setEasting(thirdEasting.getText());
				third.setNorthing(thirdNorthing.getText());
				if (thirdMainDirection.getText().length() > 0) {
					third.setMainDirection(thirdMainDirection.getText());
					mainApp.getCanvas().deletePosition(third.getpositionname());
					mainApp.getCanvas().drawPosition(third.getEasting(), third.getNorthing(), 3,
							third.getpositionname(), Integer.parseInt(third.getMainDirection()));
					
				}
				else{
					mainApp.getCanvas().deletePosition(third.getpositionname());
					mainApp.getCanvas().drawPosition(third.getEasting(), third.getNorthing(), 2,
							third.getpositionname());
				}
				if (thirdBackUpDirection.getText().length() > 0) {
					third.setBackUpDirection(thirdBackUpDirection.getText());
				}
			}
			else{
				mainApp.getCanvas().deletePosition(third.getpositionname());
			}

			okClicked = true;
			
		}

	}
	/** Returns true if given string is numeric and false if it´s not.
	 * @param s String to be checked.
	 * @return True if input is numeric, false if not.
	 *  */
	public boolean isNumeric(String s) {
		return s.matches("\\d+");
	}
	/** Checks if all fields are valid.
	 *  Calls Alert when fields are incorrect and notifies about user input mistakes.
	 *  @return True if valid inputs, false if invalid.
	 *  */
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
			} else if (Double.parseDouble(firstMainDirection.getText()) > 6400.0) {
				errorMessage += "Esimesel põhisuund liiga suur!\n";

			}
		}
		if (firstBackUpDirection.getText().length() > 0) {
			if (firstBackUpDirection.getText().length() != 4 || !isNumeric(firstBackUpDirection.getText())) {
				errorMessage += "Esimesel vales formaadis varusuund!\n";
			} else if (Double.parseDouble(firstBackUpDirection.getText()) > 6400.0) {
				errorMessage += "Esimesel varusuund liiga suur!\n";

			}
		}
		if (secondMainDirection.getText().length() > 0) {
			if (secondMainDirection.getText().length() != 4 || !isNumeric(secondMainDirection.getText())) {
				errorMessage += "Teisel vales formaadis põhisuund!\n";
			} else if (Double.parseDouble(secondMainDirection.getText()) > 6400.0) {
				errorMessage += "Teisel põhisuund liiga suur!\n";

			}
		}
		if (secondBackUpDirection.getText().length() > 0) {
			if (secondBackUpDirection.getText().length() != 4 || !isNumeric(secondBackUpDirection.getText())) {
				errorMessage += "Teisel vales formaadis varusuund!\n";
			} else if (Double.parseDouble(secondBackUpDirection.getText()) > 6400.0) {
				errorMessage += "Teisel varusuund liiga suur!\n";

			}
		}
		if (thirdMainDirection.getText().length() > 0) {
			if (thirdMainDirection.getText().length() != 4 || !isNumeric(thirdMainDirection.getText())) {
				errorMessage += "Kolmandal vales formaadis põhisuund!\n";
			} else if (Double.parseDouble(thirdMainDirection.getText()) > 6400.0) {
				errorMessage += "Kolmandal põhisuund liiga suur!\n";

			}
		}
		if (thirdBackUpDirection.getText().length() > 0) {
			if (thirdBackUpDirection.getText().length() != 4 || !isNumeric(thirdBackUpDirection.getText())) {
				errorMessage += "Kolmandal vales formaadis varusuund!\n";
			} else if (Double.parseDouble(thirdBackUpDirection.getText()) > 6400.0) {
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
