package mppa.view;

import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import mppa.MainApp;
import mppa.model.Target;
/**
 * Class for displaying information about the target
 *
 * @author Argo Neumann
 */
public class TargetInfoController {
	/** Image of mortar battery.*/
	@FXML
	private ImageView imageView;
	/** Header label for target name.*/
	@FXML
	private Label header;
	/** Location label.*/
	@FXML
	private Label loc;
	/** Correction for fragment mine.*/
	@FXML
	private Label deltaK;
	/** Correction for smoke mine.*/
	@FXML
	private Label deltaS;
	/** Correction for lightning mine.*/
	@FXML
	private Label deltaV;
	/** Information about the last correction made.*/
	@FXML
	private Label lastCorrection;
	/** Main application.*/
	private MainApp mainApp;
	/** Current target.*/
	private Target target;
	/** Dialog stage.*/
	private Stage dialogStage;
	/** Array containing coordinate deltas.*/
	private ArrayList<ArrayList<Integer>> deltas;
	/** Array containing last correction information.*/
	private ArrayList<ArrayList<Integer>> lastCorrectionData;
	/** Boolean for confirmation button.*/
	private boolean okClicked = false;
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {

	}
	/** Set´s class main application.
	 * @param mainApp MainApp object
	 *  */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	/**
	 * Sets the stage of this dialog.
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
	/** Called when confirmation button is clicked.
	 *  Sets {@link #okClicked okClicked} to true and closes dialog.
	 *  */
	@FXML
	public void handleOk() {
		okClicked = true;
		dialogStage.close();

	}	
	/** Sets target. Gets target deltas and last correction.
	 * Calls {@link #setLabels() setLabels()} method.
	 * Sets {@link #okClicked okClicked} to true and closes dialog.
	 * @param Target Target
	 *  */

	public void setTarget(Target Target) {
		this.target = Target;
		this.deltas = Target.getDeltas();
		this.lastCorrectionData = Target.getLastCorrection();
		setLabels();

	}
	/** Sets all labels text.
	 *  */
	public void setLabels() {
		header.setText(target.toString());
		imageView.setImage(new Image("file:logo_1.png"));
		loc.setText("SM asukoht: " + target.getEasting() + " " + target.getNorthing());
		deltaK.setText("Kild: ΔE: " + deltas.get(0).get(0) + " ΔN: " + deltas.get(0).get(1));
		deltaS.setText("Suits: ΔE: " + deltas.get(1).get(0) + " ΔN: " + deltas.get(1).get(1) + " ΔTõste: "
				+ deltas.get(1).get(2));
		deltaV.setText("Valgus: ΔE: " + deltas.get(2).get(0) + " ΔN: " + deltas.get(2).get(1) + " ΔTõste: "
				+ deltas.get(2).get(2));
		if (lastCorrectionData.size() > 0) {
			lastCorrection.setText("Viimane korrektuur: " + "SND: " + lastCorrectionData.get(0) + "\n" + "Kild: "
					+ lastCorrectionData.get(1).get(0) * 10 + " " + lastCorrectionData.get(1).get(1) * 10 + "\n"
					+ "Suits: " + lastCorrectionData.get(2).get(0) * 10 + " " + lastCorrectionData.get(2).get(1) * 10 + " "+ lastCorrectionData.get(2).get(2)
					+ "\n" + "Valgus: " + lastCorrectionData.get(3).get(0) * 10 + " " +lastCorrectionData.get(3).get(1) * 10+" "+ lastCorrectionData.get(3).get(2)+ "\n");
		} else {
			lastCorrection.setText("Viimane korrektuur: puudub");
		}

	}

}
