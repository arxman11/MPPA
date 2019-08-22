package mppa.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mppa.MainApp;

public class LimitersController {
	private Stage dialogStage;
	private MainApp mainApp;
	private boolean okClicked = false;
	private ArrayList<ArrayList<Integer>> limiters;
	private ObservableList<String> asStrings= FXCollections.observableArrayList();
	@FXML
	ListView<String> list = new ListView<String>();


	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		this.limiters = mainApp.getLimiters();
		fillListView();
	}
	public void fillListView(){
		asStrings.clear();
		for(ArrayList<Integer> x:limiters){
			String easting=Integer.toString(x.get(0));
			String northing=Integer.toString(x.get(1));
			asStrings.add(cordCheck(easting)+" "+cordCheck(northing));
		}
		list.setItems(asStrings);
		
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

	/**
	 * Sets the stage of this dialog.
	 * 
	 * @param dialogStage
	 */
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
	private void handleOk() {
		if(limiters.size()==0 || limiters.size()>=3){
			okClicked = true;
			dialogStage.close();
		}
		else{
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Vähe piirajaid");
			alert.setHeaderText("Liiga vähe piirajaid lisatud");
			alert.setContentText("Olema peab minimaalselt 3 piirajat");

			alert.showAndWait();
		}
		

	}
	@FXML
	private void handleLimiterAdd() {
		boolean okClicked = this.showLimiterAddDialog();
		if (okClicked) {
			fillListView();
		}

	}

	private boolean showLimiterAddDialog() {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/AddLimiter.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Lisa piiraja");
			dialogStage.getIcons().add(new Image("file:logo.png"));
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(mainApp.getPrimaryStage());
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			AddLimiter controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setMainApp(mainApp);
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	@FXML
	private void handleDeleteLimiter() {
		int selectedIndex = list.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Kas soovid piirajat kustutada?");
			alert.setHeaderText("Kas soovid piirajat kustutada?");

			ButtonType kustuta = new ButtonType("Kustuta");
			ButtonType katkesta = new ButtonType("Katkesta",ButtonData.CANCEL_CLOSE);
			alert.getButtonTypes().setAll(kustuta,katkesta);

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == kustuta){
				list.getItems().remove(selectedIndex);
				limiters.remove(selectedIndex);
				mainApp.setLimiters(limiters);
			} else {
				
			}
			
		} else {
			// Nothing selected.
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(mainApp.getPrimaryStage());
			alert.setTitle("Puudub valik.");
			alert.setHeaderText("Ühtegi piirajat pole valitud");
			alert.setContentText("Vali tabelist piiraja.");

			alert.showAndWait();
		}
	}

}
