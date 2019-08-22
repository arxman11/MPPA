package mppa.view;

import java.util.ArrayList;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import mppa.MainApp;
import mppa.model.Target;
/**
 * Class for making corrections
 *
 * @author Argo Neumann
 */
public class CorrectionController {
	/** Direction of correction. */
	@FXML
	private TextField FOdirection;
	/** Correction of mortar mine to left. */
	@FXML
	private TextField Kvas;
	/** Correction of mortar mine to right. */
	@FXML
	private TextField Kpar;
	/** Correction of mortar mine in increase. */
	@FXML
	private TextField Ksur;
	/** Correction of mortar mine in decrease. */
	@FXML
	private TextField Kvah;
	/** Correction of smoke mortar mine to left. */
	@FXML
	private TextField Svas;
	/** Correction of smoke mortar mine to right. */
	@FXML
	private TextField Spar;
	/** Correction of smoke mortar mine in increase. */
	@FXML
	private TextField Ssur;
	/** Correction of smoke mortar mine in decrease. */
	@FXML
	private TextField Svah;
	/** Correction of smoke mortar mine in lift. */
	@FXML
	private TextField Stos;
	/** Correction of smoke mortar mine in fall. */
	@FXML
	private TextField Slan;
	/** Correction of lightning mortar mine to left. */
	@FXML
	private TextField Vvas;
	/** Correction of lightning mortar mine to right. */
	@FXML
	private TextField Vpar;
	/** Correction of lightning mortar mine in increase. */
	@FXML
	private TextField Vsur;
	/** Correction of lightning mortar mine in decrease. */
	@FXML
	private TextField Vvah;
	/** Correction of lightning mortar mine in lift. */
	@FXML
	private TextField Vtos;
	/** Correction of lightning mortar mine in fall. */
	@FXML
	private TextField Vlan;
	/** Main Application. */
	private MainApp mainApp;
	/** Target. */
	private Target target;
	/** Dialog stage. */
	private Stage dialogStage;
	/** Boolean for confirmation button.*/
	private boolean okClicked = false;
	/** Array containing all correction fields.*/
	private ArrayList<TextField> fields = new ArrayList<TextField>();
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
	}
	/** Set´s class main application .
	 * Adds all fields to {@link #fields fields} array.
	 * @param mainApp MainApp 
	 *  */
	public void setMainApp(MainApp mainApp) {
		fields.add(Kvas);
		fields.add(Kpar);
		fields.add(Ksur);
		fields.add(Kvah);
		fields.add(Svas);
		fields.add(Spar);
		fields.add(Ssur);
		fields.add(Svah);
		fields.add(Stos);
		fields.add(Slan);
		fields.add(Vvas);
		fields.add(Vpar);
		fields.add(Vsur);
		fields.add(Vvah);
		fields.add(Vtos);
		fields.add(Vlan);
		this.mainApp = mainApp;
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
	 * fields are valid.
	 * Calls {@link #addCorrection() addCorrection()} method to make the correction to target.
	 * Sets {@link #okClicked okClicked} to true and closes dialog.
	 *  */
	@FXML
	public void handleOk() {
		if (isInputValid()) {
			okClicked = true;
			addCorrection();
			dialogStage.close();
		}

	}
	/**
	 * Sets target.
	 * @param Target Given target.
	 */

	public void setTarget(Target Target) {
		this.target = Target;

	}
	/** Calls {@link #handleOk() handleOk()} method if Enter key is pressed.
	 * @param ae ActionEvent.
	 *  */
	@FXML
	public void onEnter(ActionEvent ae) {
		handleOk();
	}
	/** Returns true if giveen string is numeric and false if it´s not.
	 * @param s String to be checked.
	 * @return True if input is numeric, false if not.
	 *  */
	public boolean isNumeric(String s) {
		if (s.matches("\\d+")) {
			return true;
		} else {
			return false;
		}
	}
	/** Checks coordinate or direction format and returns it in correct format.
	 * @param dir Given coordinate or direction as string.
	 * @return Direction or coordinate in correct format.
	 *  */
	public String cordCheck(String dir) {
		if (dir.length() == 4) {
			return dir;
		} else if (dir.length() == 1) {
			dir = "0000";
		} else if (dir.length() == 1) {
			dir = new StringBuilder(dir).insert(0, "000").toString();
		} else if (dir.length() == 2) {
			dir = new StringBuilder(dir).insert(0, "00").toString();
		} else if (dir.length() == 3) {
			dir = new StringBuilder(dir).insert(0, "0").toString();
		}
		return dir;
	}
	/** Makes correction to the target
	 *  Creates new array for each type of ammunition containing it´s corrections.
	 *  If target is region target, makes corrections to all three targets.
	 *  */
	public void addCorrection() {//
		int FOdirectionAngle = Integer.parseInt(FOdirection.getText());
		int deltaEastingK = Math.round((Integer.parseInt(Kpar.getText()) - Integer.parseInt(Kvas.getText())) / 10);
		int deltaNorthingK = Math.round((Integer.parseInt(Ksur.getText()) - Integer.parseInt(Kvah.getText())) / 10);
		int deltaEastingS = Math.round((Integer.parseInt(Spar.getText()) - Integer.parseInt(Svas.getText())) / 10);
		int deltaNorthingS = Math.round((Integer.parseInt(Ssur.getText()) - Integer.parseInt(Svah.getText())) / 10);
		int deltaIncrementS = Math.round((Integer.parseInt(Stos.getText()) - Integer.parseInt(Slan.getText())));
		int deltaEastingV = Math.round((Integer.parseInt(Vpar.getText()) - Integer.parseInt(Vvas.getText())) / 10);
		int deltaNorthingV = Math.round((Integer.parseInt(Vsur.getText()) - Integer.parseInt(Vvah.getText())) / 10);
		int deltaIncrementV = Math.round((Integer.parseInt(Vtos.getText()) - Integer.parseInt(Vlan.getText())));
		ArrayList<Integer> correctionK = new ArrayList<>();
		correctionK.add(deltaEastingK);
		correctionK.add(deltaNorthingK);
		ArrayList<Integer> correctionS = new ArrayList<>();
		correctionS.add(deltaEastingS);
		correctionS.add(deltaNorthingS);
		correctionS.add(deltaIncrementS);
		ArrayList<Integer> correctionV = new ArrayList<>();
		correctionV.add(deltaEastingV);
		correctionV.add(deltaNorthingV);
		correctionV.add(deltaIncrementV);
		ArrayList<ArrayList<Integer>> correctionAll = new ArrayList<>();
		ArrayList<Integer> direction = new ArrayList<>();
		direction.add(FOdirectionAngle);
		correctionAll.add(direction);
		correctionAll.add(correctionK);
		correctionAll.add(correctionS);
		correctionAll.add(correctionV);
		if(target.isRegion()){
			for(Target i: target.getRegionTargets()){
				i.addCorrection(correctionAll);
			}
			target.addCorrection(correctionAll);
		}
		else{
			target.addCorrection(correctionAll);
		}
		

	}
	/** Checks if fields are valid.
	 *  If correction fields except direction field are empty, sets value to 0.
	 *  Calls Alert when fields are incorrect and notifys about user input mistakes.
	 *  @return True if valid inputs, false if invalid.
	 *  */
	private boolean isInputValid() {
		String errorMessage = "";
		for (TextField i : fields) {
			if (i.getText().isEmpty()) {
				i.setText("0");
			}
			if (!i.getText().isEmpty()) {
				if (!isNumeric(i.getText())) {
					errorMessage += "Kontrolli sisendeid!\n";
				}
			}
		}
		if (FOdirection.getText() == null || FOdirection.getText().length() != 4 || !isNumeric(FOdirection.getText())) {
			errorMessage += "Vales formaadis vaatlussuund!\n";
		} else if (FOdirection.getText() != null) {
			if (Integer.parseInt(FOdirection.getText()) > 6400) {
				errorMessage += "Vaatlussuund pole reaalne!\n";
			}
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message.
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Vigane sisend");
			alert.setHeaderText("Paranda sisestus");
			alert.setContentText(errorMessage);

			alert.showAndWait();

			return false;
		}
	}

}
