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

public class TukTargetController {
	@FXML
	private TextField easting;
	@FXML
	private TextField northing;
	@FXML
	private TextField directionAngle;
	@FXML
	private TextField vas;
	@FXML
	private TextField par;
	@FXML
	private TextField vah;
	@FXML
	private TextField sur;
	private ArrayList<TextField> fields = new ArrayList<TextField>();
	private MainApp mainApp;
	private Stage dialogStage;
	private Target Target;
	private boolean okClicked = false;

	@FXML
	private void initialize() {
		fields.add(vas);
		fields.add(par);
		fields.add(vah);
		fields.add(sur);
	}

	public boolean isNumeric(String s) {
		return s.matches("\\d+");
	}

	public TukTargetController() {
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
				Target.setTargetName("TÜK " + easting.getText() + " " + northing.getText() + " SND "
						+ directionAngle.getText() + " " + eastingString + " " + northingString);
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
		if (easting.getText().length() != 4 || !isNumeric(easting.getText())) {
			errorMessage += "Vales formaadis easting!\n";
		}
		if (northing.getText().length() != 4 || !isNumeric(northing.getText())) {
			errorMessage += "Vales formaadis northing!\n";
		}
		if (directionAngle.getText() == null || directionAngle.getText().length() != 4
				|| !isNumeric(directionAngle.getText())) {
			errorMessage += "Vales formaadis vaatlussuund!\n";
		} else if (directionAngle.getText() != null) {
			if (Integer.parseInt(directionAngle.getText()) > 6400) {
				errorMessage += "Vaatlussuund pole reaalne!\n";
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
