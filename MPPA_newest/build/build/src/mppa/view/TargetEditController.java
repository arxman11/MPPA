package mppa.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import mppa.model.Target;

public class TargetEditController {

	@FXML
	private TextField eastingField;
	@FXML
	private TextField northingField;
	@FXML
	private TextField FOeastingField;
	@FXML
	private TextField FOnorthingField;

	@FXML
	private TextField directionAngleField;
	@FXML
	private TextField FOdistanceField;

	private Stage dialogStage;
	private Target Target;
	private boolean okClicked = false;

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

	/**
	 * Sets the stage of this dialog.
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/**
	 * Sets the Target to be edited in the dialog.
	 * 
	 * @param Target
	 */
	public void setTarget(Target Target) {
		this.Target = Target;

		eastingField.setText(Target.getEasting());
		northingField.setText(Target.getNorthing());
		if (Target.FOeastingProperty() != null) {
			FOeastingField.setText(Target.getFOEasting());
		}
		if (Target.FOnorthingProperty() != null) {
			FOnorthingField.setText(Target.getFONorthing());
		}
		if (Target.FOdistanceProperty() != null) {
			FOdistanceField.setText(Target.getFOdistance());
		}
		if (Target.directionAngleProperty() != null) {
			directionAngleField.setText(Target.getDirectionAngle());
		}

	}

	/**
	 * Returns true if the user clicked OK, false otherwise.
	 * 
	 * @return
	 */
	public boolean isOkClicked() {
		return okClicked;
	}

	/**
	 * Called when the user clicks ok.
	 */
	@FXML
	private void handleOk() {
		if (isInputValid()) {
			if (Target.getTargetname() == null) {
				Target.setTargetName(eastingField.getText() + " " + northingField.getText());
			}
			Target.setEasting(eastingField.getText());
			Target.setNorthing(northingField.getText());
			if (FOdistanceField.getText() == null) {
				//Target.setFOdistance("000");

			} else {
				Target.setFOdistance(FOdistanceField.getText());
			}

			if (directionAngleField.getText() == null) {
				Target.setDirectionAngle("0000");
			} else {
				Target.setDirectionAngle(directionAngleField.getText());
			}
			if (FOeastingField.getText() == null) {
				Target.setFOEasting("0000");
			} else {
				Target.setFOEasting(FOeastingField.getText());
			}
			if (FOnorthingField.getText() == null) {
				Target.setFONorthing("0000");
			} else {
				Target.setFONorthing(FOnorthingField.getText());
			}
			Target.updateName();
			okClicked = true;
			dialogStage.close();
		}
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

	/**
	 * Validates the user input in the text fields.
	 * 
	 * @return true if the input is valid
	 */
	private boolean isInputValid() {
		String errorMessage = "";
		if (Target.FOdistanceProperty() == null) {
			if (eastingField.getText().length() != 4 || !isNumeric(eastingField.getText())) {
				errorMessage += "Vales formaadis easting!\n";
			}
			if (northingField.getText().length() != 4 || !isNumeric(northingField.getText())) {
				errorMessage += "Vales formaadis northing!\n";
			}
		}
		if (directionAngleField.getText() != null) {
			if (directionAngleField.getText().length() != 4 || !isNumeric(directionAngleField.getText())) {
				errorMessage += "Vales formaadis vaatlussuund!\n";
			}
		}
		if (directionAngleField.getText() != null) {
			if (Integer.parseInt(directionAngleField.getText()) > 6400) {
				errorMessage += "Vaatlussuund pole reaalne!\n";
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