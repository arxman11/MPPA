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

public class CorrectionController {
	@FXML
	private TextField FOdirection;
	@FXML
	private TextField Kvas;
	@FXML
	private TextField Kpar;
	@FXML
	private TextField Ksur;
	@FXML
	private TextField Kvah;
	@FXML
	private TextField Svas;
	@FXML
	private TextField Spar;
	@FXML
	private TextField Ssur;
	@FXML
	private TextField Svah;
	@FXML
	private TextField Stos;
	@FXML
	private TextField Slan;
	@FXML
	private TextField Vvas;
	@FXML
	private TextField Vpar;
	@FXML
	private TextField Vsur;
	@FXML
	private TextField Vvah;
	@FXML
	private TextField Vtos;
	@FXML
	private TextField Vlan;
	private MainApp mainApp;
	private Target target;
	private Stage dialogStage;
	private boolean okClicked = false;
	private ArrayList<TextField> fields = new ArrayList<TextField>();

	@FXML
	private void initialize() {
	}

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

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	@FXML
	public void handleOk() {
		if (isInputValid()) {
			okClicked = true;
			addCorrection();
			dialogStage.close();
		}

	}

	public void setTarget(Target Target) {
		this.target = Target;

	}

	@FXML
	public void onEnter(ActionEvent ae) {
		handleOk();
	}

	public boolean isNumeric(String s) {
		if (s.matches("\\d+")) {
			return true;
		} else {
			return false;
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
		target.addCorrection(correctionAll);

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
