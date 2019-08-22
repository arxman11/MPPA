package mppa.view;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import mppa.MainApp;
import mppa.model.Target;

public class TargetInfoController {
	@FXML
    private ImageView imageView;
	@FXML
	private Label header;
	@FXML
	private Label loc;
	@FXML
	private Label deltaK;
	@FXML
	private Label deltaS;
	@FXML
	private Label deltaV;
	private MainApp mainApp;
	private Target target;
	private Stage dialogStage;
	
	private ArrayList<ArrayList<Integer>> deltas;
	private boolean okClicked = false;

	@FXML
	private void initialize() {
		
	}
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public boolean isOkClicked() {
		return okClicked;
	}


	@FXML
	public void handleOk() {
		okClicked = true;
		dialogStage.close();

	}

	public void setTarget(Target Target) {
		this.target = Target;
		this.deltas = Target.getDeltas();
		setLabels();
		

	}
	public void setLabels (){
		header.setText(target.toString());
		imageView.setImage(new Image("file:logo_1.png"));
		loc.setText("SM asukoht: "+target.getEasting()+" "+target.getNorthing());
		deltaK.setText("Kild: ΔE: "+deltas.get(0).get(0)+" ΔN: "+deltas.get(0).get(1));
		deltaS.setText("Suits: ΔE: "+deltas.get(1).get(0)+" ΔN: "+deltas.get(1).get(1) +"ΔToste: "+deltas.get(1).get(2));
		deltaV.setText("Valgus: ΔE: "+deltas.get(2).get(0)+" ΔN: "+deltas.get(2).get(1) +"ΔToste: "+deltas.get(2).get(2));
	}

}
