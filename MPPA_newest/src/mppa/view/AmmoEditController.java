package mppa.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import mppa.MainApp;
/**
 * Class for editing ammo type.
 *
 * @author Argo Neumann
 */
public class AmmoEditController {
	/** Main application. */
	private MainApp mainApp;
	/** Dialog stage. */
	private Stage dialogStage;
	/** Drop-down selection for caliber. */
	@FXML
	private ComboBox<String> caliber;
	/** Boolean for confirmation button.*/
	private boolean okClicked = false;
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
	}
	/** Set´s class main application and populates ComboBox with two options:81mm and 120mm.
	 * Selects current ammo based on main applications ammo.
	 * @param mainApp MainApp object
	 *  */
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
	/** Closes stage if operation is cancelled.
	 *  */
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}
	/** Called when confirmation button is clicked
	 *  Sets main application´s ammy type whether to 120mm or to 81mm, depending on user input.
	 *  Sets {@link #okClicked okClicked} to true and closes dialog.
	 *  */
	@FXML
	public void handleOk() {
		if(caliber.getSelectionModel().getSelectedItem()=="120mm"){
			mainApp.setAmmo(0);
		}
		else if(caliber.getSelectionModel().getSelectedItem()=="81mm"){
			mainApp.setAmmo(1);
		}
		okClicked = true;
		dialogStage.close();
	}
	/** Calls {@link #handleOk() handleOk()} method if Enter key is pressed.
	 * @param ae ActionEvent.
	 *  */
	@FXML
	public void onEnter(ActionEvent ae) {
		handleOk();
	}
}
