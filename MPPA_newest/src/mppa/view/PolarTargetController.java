package mppa.view;

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
 * Class for adding target with polar method.
 *
 * @author Argo Neumann
 */
public class PolarTargetController {
	/** Easting of the target. */
	@FXML
	private TextField easting;
	/** Northing of the target. */
	@FXML
	private TextField northing;
	/** Direction angle of the Forward Observer. */
	@FXML
	private TextField FOdirectionAngle;
	/** Distance from Forward Observer to target. */
	@FXML
	private TextField distance;
	/** Checkbox whether the target is region target or not. */
	@FXML
	private CheckBox regionCheck;
	/** Direction angle for the region target. */
	@FXML
	private TextField regionAngle;
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
	}
	/** Returns true if given string is numeric and false if it´s not.
	 * @param s String to be checked.
	 * @return True if input is numeric, false if not.
	 *  */
	public boolean isNumeric(String s) {
		return s.matches("\\d+");
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
	/** Default constructor. */
	public PolarTargetController() {
	}
	/** Set´s class main application .
	 * @param mainApp MainApp .
	 *  */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	/**
	 * Sets the stage of this dialog.
	 * 
	 * @param dialogStage Dialog stage.
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}
	/** Set´s class target and fields to targets fields. .
	 * @param Target Target .
	 *  */
	public void setTarget(Target Target) {
		this.Target = Target;

		easting.setText(Target.getEasting());
		northing.setText(Target.getNorthing());
		FOdirectionAngle.setText(Target.getDirectionAngle());

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
	 * Calculates target´s easting and northing, based on Forward Observer´s location, direction angle and distance to target.
	 * If target is given as region target, adds region target points.
	 *  */
	@FXML
	private void handleOk() {
		if (isInputValid()) {
			if (Target.getTargetname() == null) {
				if(regionCheck.isSelected()){
					Target.setTargetName("MAA-ALA POL MA " + easting.getText() + " " + northing.getText() + " SND "
							+ FOdirectionAngle.getText() + " K " + distance.getText());
				}
				else{
					Target.setTargetName("POL MA " + easting.getText() + " " + northing.getText() + " SND "
							+ FOdirectionAngle.getText() + " K " + distance.getText());
				}
			}
			Target.setFOEasting(easting.getText());
			Target.setFONorthing(northing.getText());
			Target.setTargetName("POL MA " + easting.getText() + " " + northing.getText() + " SND "
					+ FOdirectionAngle.getText() + " K " + distance.getText());
			Target.setFOdistance(distance.getText());
			Target.setDirectionAngle(FOdirectionAngle.getText());
			Target.setTag(2);
			int FOEasting = Integer.parseInt(easting.getText());
			int FONorthing = Integer.parseInt(northing.getText());
			int FODistance = Integer.parseInt(distance.getText());
			double FODirectionAngle = Double.valueOf(FOdirectionAngle.getText());
			double FOangle = Math.round(FODirectionAngle / 17.777777777778);
			int targetEasting = (int) Math
					.round((FOEasting * 10 + FODistance * Math.sin(FOangle * Math.PI / 180)) / 10);
			int targetNorthing = (int) Math
					.round((FONorthing * 10 + FODistance * Math.cos(FOangle * Math.PI / 180)) / 10);
			Target.setEasting(cordCheck(Integer.toString(targetEasting)));
			Target.setNorthing(cordCheck(Integer.toString(targetNorthing)));
			Target.setForwardObserver(true);
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
	/** Checks if easting and northing are valid.
	 *  Calls Alert when fields are incorrect and notifies about user input mistakes.
	 *  @return True if valid inputs, false if invalid.
	 *  */
	private boolean isInputValid() {
		String errorMessage = "";
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
		
		if (FOdirectionAngle.getText() == null || FOdirectionAngle.getText().length() != 4
				|| !isNumeric(FOdirectionAngle.getText())) {
			errorMessage += "Vales formaadis vaatlussuund!\n";
		} else if (FOdirectionAngle.getText() != null) {
			if (Integer.parseInt(FOdirectionAngle.getText()) > 6400) {
				errorMessage += "Vaatlussuund pole reaalne!\n";
			}
		}

		if (distance.getText() == null || !isNumeric(distance.getText())) {
			errorMessage += "Puuduv kaugus!\n";
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
