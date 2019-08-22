package mppa.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import mppa.MainApp;

public class AmmoEditController {
	private MainApp mainApp;
	private Stage dialogStage;
	@FXML
	private ComboBox<String> caliber;
	private boolean okClicked = false;
	@FXML
	private void initialize() {
	}
	public void setMainApp(MainApp mainApp) {
		ObservableList<String> calibers = FXCollections.observableArrayList();
		calibers.add("120mm");
		calibers.add("81mm");
		caliber.setItems(calibers);
		if(mainApp.getAmmo()==0){
			caliber.getSelectionModel().select(0);
		}
		else if(mainApp.getAmmo()==1){
			caliber.getSelectionModel().select(1);
		}
		
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

	public boolean isOkClicked() {
		if(caliber.getSelectionModel().getSelectedItem()=="120mm"){
			mainApp.setAmmo(0);
		}
		else if(caliber.getSelectionModel().getSelectedItem()=="81mm"){
			mainApp.setAmmo(1);
		}
		return okClicked;
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}
	@FXML
	public void handelOk(ActionEvent ae) {
		okClicked = true;
		dialogStage.close();
	}
}
