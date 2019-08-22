package mppa.view;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import mppa.MainApp;
import mppa.model.Target;
/**
 * Class for adding target with fire transmission method.
 *
 * @author Argo Neumann
 */
public class TukTargetController {
	/** Easting of the target. */
	@FXML
	private TextField easting;
	/** Northing of the target. */
	@FXML
	private TextField northing;
	/** Direction angle of the target. */
	@FXML
	private TextField directionAngle;
	/** Correction to the left. */
	@FXML
	private TextField vas;
	/** Correction to the right. */
	@FXML
	private TextField par;
	/** Correction to decrease. */
	@FXML
	private TextField vah;
	/** Correction to the increase. */
	@FXML
	private TextField sur;
	/** Checkbox whether the target is region target or not. */
	@FXML
	private CheckBox regionCheck;
	/** Direction angle for the region target. */
	@FXML
	private TextField regionAngle;
	/** Array containing all the correction fields. */
	private ArrayList<TextField> fields = new ArrayList<TextField>();
	/** Main Application. */
	private MainApp mainApp;
	/** Dialog stage. */
	private Stage dialogStage;
	/** Target to be created. */
	private Target Target;
	/** Boolean for confirmation button.*/
	private boolean okClicked = false;
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
		fields.add(vas);
		fields.add(par);
		fields.add(vah);
		fields.add(sur);
	}
	/** Returns true if given string is numeric and false if it´s not.
	 * @param s String to be checked.
	 * @return True if input is numeric, false if not.
	 *  */
	public boolean isNumeric(String s) {
		return s.matches("\\d+");
	}
	/** Default constructor. */
	public TukTargetController() {
	}
	/** Set´s class main application .
	 * @param mainApp MainApp .
	 *  */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		
	}
	/** Method is called when checkbox is ticked or unticked.
	 * Set´s regionAngle textField enabled if checkbox is ticked or disabled if not ticked..
	 * @param e Checkbox ticked.
	 *  */
	@FXML
	private void handleCheckAction(ActionEvent e) {
	    if(regionCheck.isSelected()){
	        regionAngle.setDisable(false);
	     }
	    else{
	    	regionAngle.setDisable(true);
	    }
	 }

	/**
	 * Sets the stage of this dialog.
	 * 
	 * @param dialogStage Dialog stage.
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	/** Set´s class target.
	 * @param Target Target .
	 *  */
	public void setTarget(Target Target) {
		this.Target = Target;

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
	/** Calls {@link #handleOk() handleOk()} method if Enter key is pressed.
	 * @param ae ActionEvent.
	 *  */
	@FXML
	public void onEnter(ActionEvent ae) {
		handleOk();
	}
	/** Called when confirmation button is clicked.
	 * If given fields are valid, sets fields to target fields.
	 * Calculates target´s easting and northing, based on fire transmissions location, direction angle and corrections to the target.
	 * If target is given as region target, adds region target points.
	 *  */
	@FXML
	private void handleOk() {
		if (isInputValid()) {
			String eastingString = "";
			String northingString = "";
			int deltaEasting = Math.round((Integer.parseInt(par.getText()) - Integer.parseInt(vas.getText())) / 10);
			int deltaNorthing = Math.round((Integer.parseInt(sur.getText()) - Integer.parseInt(vah.getText())) / 10);
			int FOdirection=Integer.parseInt(directionAngle.getText());
			int eastingInt= Integer.parseInt(easting.getText());
			int northingInt= Integer.parseInt(northing.getText());
			if (deltaEasting >= 0) {
				eastingString = "PAR " + deltaEasting * 10;
			} else {
				eastingString = "VAS " + Math.abs(deltaEasting * 10);
			}
			if (deltaNorthing >= 0) {
				northingString = "SUR " + deltaNorthing * 10;
			} else {
				northingString = "VÄH " + Math.abs(deltaNorthing * 10);
			}
			if (Target.getTargetname() == null) {
				if(regionCheck.isSelected()){
					Target.setTargetName("MAA-ALA TUK " + easting.getText() + " " + northing.getText() + " SND "
							+ directionAngle.getText() + " " + eastingString + " " + northingString);
				}
				else{
					Target.setTargetName("TUK " + easting.getText() + " " + northing.getText() + " SND "
							+ directionAngle.getText() + " " + eastingString + " " + northingString);
				}
				
			}
			Target.setDirectionAngle(directionAngle.getText());
			Target.setTag(3);
			double FOangle1 = Math.round(FOdirection / 17.777777777778);
			if (FOdirection < 3200) {
				int targetEasting1 = (int) Math
						.round((eastingInt * 10 + deltaNorthing * 10 * Math.sin(FOangle1 * Math.PI / 180)) / 10);
				int targetNorthing1 = (int) Math
						.round((northingInt * 10 + deltaNorthing * 10 * Math.cos(FOangle1 * Math.PI / 180)) / 10);
				FOdirection += 1600;
				double FOangle = Math.round(FOdirection / 17.777777777778);
				int targetEasting = (int) Math
						.round((targetEasting1 * 10 + deltaEasting * 10 * Math.sin(FOangle * Math.PI / 180)) / 10);
				int targetNorthing = (int) Math
						.round((targetNorthing1 * 10 + deltaEasting * 10 * Math.cos(FOangle * Math.PI / 180)) / 10);
				String eas=String.valueOf(targetEasting);
				String nor = String.valueOf(targetNorthing);
				Target.setEasting(cordCheck(eas));
				Target.setNorthing(cordCheck(nor));

			} else if (FOdirection >= 3200) {
				int targetEasting1 = (int) Math
						.round((eastingInt * 10 + deltaNorthing * 10 * Math.sin(FOangle1 * Math.PI / 180)) / 10);
				int targetNorthing1 = (int) Math
						.round((northingInt * 10 + deltaNorthing * 10 * Math.cos(FOangle1 * Math.PI / 180)) / 10);
				FOdirection -= 1600;
				deltaEasting = -1 * deltaEasting;
				double FOangle = Math.round(FOdirection / 17.777777777778);
				int targetEasting = (int) Math
						.round((targetEasting1 * 10 + deltaEasting * 10 * Math.sin(FOangle * Math.PI / 180)) / 10);
				int targetNorthing = (int) Math
						.round((targetNorthing1 * 10 + deltaEasting * 10 * Math.cos(FOangle * Math.PI / 180)) / 10);
				String eas=String.valueOf(targetEasting);
				String nor = String.valueOf(targetNorthing);
				Target.setEasting(cordCheck(eas));
				Target.setNorthing(cordCheck(nor));

			}
			if(regionCheck.isSelected()){
				Target.setRegion(true);
				Target.addRegionTargets(regionAngle.getText());
			}
			okClicked = true;
			dialogStage.close();
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
	/** Checks if fields are valid.
	 *  Calls Alert when fields are incorrect and notifies about user input mistakes.
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
		//Check whether field is not empty or in wrong format
		if(easting.getText()!=null){
			if (easting.getText().length() != 4 || !isNumeric(easting.getText())) {
				errorMessage += "Vales formaadis easting!\n";
			}
		}
		else{
			errorMessage += "Puuduv easting!\n";
		}
		if(northing.getText()!=null){
			if (northing.getText().length() != 4 || !isNumeric(northing.getText())) {
				errorMessage += "Vales formaadis northing!\n";
			}
		}
		else{
			errorMessage += "Puuduv northing!\n";
		}
		if (directionAngle.getText() == null || directionAngle.getText().length() != 4
				|| !isNumeric(directionAngle.getText())) {
			errorMessage += "Vales formaadis vaatlussuund!\n";
		} else if (directionAngle.getText() != null) {
			if (Integer.parseInt(directionAngle.getText()) > 6400) {
				errorMessage += "Vaatlussuund pole reaalne!\n";
			}
		}
		if(regionCheck.isSelected()){
			if (regionAngle.getText() != null) {

				if (regionAngle.getText().length() != 4 || !isNumeric(regionAngle.getText())) {
					errorMessage += "Vales formaadis maa-ala suund!\n";
				} else if (Double.parseDouble(regionAngle.getText()) > 6400.0) {
					errorMessage += "Maa-ala suund liiga suur!\n";
				}
		}
			else{
				errorMessage += "Puuduv maa-ala suund!\n";
			}
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
