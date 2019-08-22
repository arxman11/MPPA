package mppa.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mppa.MainApp;
import mppa.model.Target;
/**
 * Class for adding target with coordinate method.
 *
 * @author Argo Neumann
 */
public class SquareTargetController {
	/** Easting of the target. */
	@FXML
	private TextField eastingField;
	/** Northing of the target. */
	@FXML
	private TextField northingField;
	/** Direction angle of the target. */
	@FXML
	private TextField directionAngleField;
	/** Target name. */
	@FXML
	private TextField targetName;
	/** Checkbox whether the target is region target or not. */

	@FXML
	private CheckBox regionCheck;
	/** Direction angle for the region target. */
	@FXML
	private TextField regionAngle;
	/** Dialog stage. */
	private Stage dialogStage;
	/** Target to be created. */
	private Target Target;
	/** Boolean for confirmation button.*/
	private boolean okClicked = false;
	/** Returns true if given string is numeric and false if it´s not.
	 * @param s String to be checked.
	 * @return True if input is numeric, false if not.
	 *  */
	public boolean isNumeric(String s) {
		return s.matches("\\d+");
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
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

	/**
	 * Sets the Target to be edited in the dialog.
	 * 
	 * @param Target Target.
	 */
	public void setTarget(Target Target) {
		this.Target = Target;

		eastingField.setText(Target.getEasting());
		northingField.setText(Target.getNorthing());
		directionAngleField.setText(Target.getDirectionAngle());

	}
	/** Returns true if confirmation button is pressed, false if not.
	 * @return True if confirmation is pressed, false if not
	 *  */
	public boolean isOkClicked() {
		return okClicked;
	}

	/**
	 * Called when confirmation button is clicked.
	 * Sets fields of the new Target.
	 * 
	 */
	@FXML
	private void handleOk() {
		if (isInputValid()) {
			if (Target.getTargetname() == null) {
				if(regionCheck.isSelected()){
					Target.setTargetName("MAA-ALA RUUT " + eastingField.getText() + " " + northingField.getText());
				}
				else{
					Target.setTargetName("RUUT " + eastingField.getText() + " " + northingField.getText());
				}
				
			}
			Target.setTag(1); //Square target tag
			Target.setEasting(eastingField.getText());
			Target.setNorthing(northingField.getText());
			if (directionAngleField.getText() == null) {
				Target.setDirectionAngle("0000");
			} else {
				Target.setDirectionAngle(directionAngleField.getText());
			}
			if (targetName.getText() == null || targetName.getText().length()==0) {
				Target.updateName();
			} else {
				Target.setTargetName(targetName.getText());
				Target.setNameSet(true);
			}
			if(regionCheck.isSelected()){
				Target.setRegion(true);
				Target.addRegionTargets(regionAngle.getText());
			}
			
			okClicked = true;
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
	 */
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	/**
	 * Validates the user input in the text fields.
	 * 
	 * @return true if the input is valid
	 */
	private boolean isInputValid() {
		String errorMessage = "";
		if(eastingField.getText()!=null){
			if (eastingField.getText().length() != 4 || !isNumeric(eastingField.getText())) {
				errorMessage += "Vales formaadis easting!\n";
			}
		}
		else{
			errorMessage += "Puuduv easting!\n";
		}
		if(northingField.getText()!=null){
			if (northingField.getText().length() != 4 || !isNumeric(northingField.getText())) {
				errorMessage += "Vales formaadis northing!\n";
			}
		}
		else{
			errorMessage += "Puuduv northing!\n";
		}
		
		
		if (directionAngleField.getText() != null) {

			if (directionAngleField.getText().length() != 4 || !isNumeric(directionAngleField.getText())) {
				errorMessage += "Vales formaadis vaatlussuund!\n";
			} else if (Double.parseDouble(directionAngleField.getText()) > 6400.0) {
				errorMessage += "Vaatlussuund liiga suur!\n";
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
			alert.setTitle("Ebakorrektsed väljad");
			alert.setHeaderText("Paranda vigased väljad");
			alert.setContentText(errorMessage);

			alert.showAndWait();

			return false;
		}
	}
}