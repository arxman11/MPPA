package mppa.view;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import mppa.MainApp;
import mppa.model.Target;

public class PolarTargetController {
	@FXML
	private TextField easting;
	@FXML
	private TextField northing;
	@FXML
	private TextField FOdirectionAngle;
	@FXML
	private TextField distance;

	private MainApp mainApp;
	private Stage dialogStage;
	private Target Target;
	private boolean okClicked = false;

	@FXML
	private void initialize() {
	}

	public boolean isNumeric(String s) {
		return s.matches("\\d+");
	}

	public PolarTargetController() {
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	/**
	 * Sets the stage of this dialog.
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setTarget(Target Target) {
		this.Target = Target;

		easting.setText(Target.getEasting());
		northing.setText(Target.getNorthing());
		FOdirectionAngle.setText(Target.getDirectionAngle());

	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	@FXML
	public void onEnter(ActionEvent ae) {
		handleOk();
	}

	@FXML
	private void handleOk() {
		if (isInputValid()) {
			if (Target.getTargetname() == null) {
				Target.setTargetName(easting.getText() + " " + northing.getText());
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
			okClicked = true;
			dialogStage.close();
		}
	}
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

	private boolean isInputValid() {
		String errorMessage = "";
		if (easting.getText().length() != 4 || !isNumeric(easting.getText())) {
			errorMessage += "Vales formaadis easting!\n";
		}
		if (northing.getText().length() != 4 || !isNumeric(northing.getText())) {
			errorMessage += "Vales formaadis northing!\n";
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
