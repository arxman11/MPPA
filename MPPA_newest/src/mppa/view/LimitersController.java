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
/**
 * Class for adding graphical limitation points and drawing it
 * Adds all points to array and calls main application to draw it.
 *
 * @author Argo Neumann
 */
public class LimitersController {
	/** Dialog stage. */
	private Stage dialogStage;
	/** Main application */
	private MainApp mainApp;
	/** Boolean for confirmation button.*/
	private boolean okClicked = false;
	/** Two-dimensional array containing coordinates for limitation.*/
	private ArrayList<ArrayList<Integer>> limiters;
	/** Observable list of coordinates as strings.*/
	private ObservableList<String> asStrings= FXCollections.observableArrayList();
	/** ListView containing coordinates.*/
	@FXML
	ListView<String> list = new ListView<String>();


	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
	}
	/** Set´s class main application and main application limiters to {@link #limiters limiters}.
	 * Calls {@link #fillListView() fillListView()} method.
	 * @param mainApp MainApp .
	 *  */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		this.limiters = mainApp.getLimiters();
		fillListView();
	}
	/** Populates ListView with elements from {@link #limiters limiters}.
	 *  Adds coordinates as strings to {@link #asStrings asStrings} Observable List and
	 *  sets ListView items to {@link #asStrings asStrings}.
	 *  */
	public void fillListView(){
		asStrings.clear();
		for(ArrayList<Integer> x:limiters){
			String easting=Integer.toString(x.get(0));
			String northing=Integer.toString(x.get(1));
			asStrings.add(cordCheck(easting)+" "+cordCheck(northing));
		}
		list.setItems(asStrings);
		
	}
	/** Checks coordinates format and returns in correct format.
	 * @param dir Given coordinate or direction as string.
	 * @return Direction or coordinate in correct format.
	 *  */
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
	 * @param dialogStage Dialog stage
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
	/** Calls new dialog window to delete all limitation.
	 * If user wants to delete limitation, clears {@link #list list} and {@link #limiters limiters} and
	 * calls main application to delete limiter.
	 *  */
	@FXML
	private void handleDeleteAllLimiter() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Kas soovid kogu piirangu kustutada?");
		alert.setHeaderText("Kas soovid kogu piirangut kustutada?");

		ButtonType kustuta = new ButtonType("Kustuta");
		ButtonType katkesta = new ButtonType("Katkesta",ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(kustuta,katkesta);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == kustuta){
			list.getItems().clear();
			limiters.clear();
			mainApp.getCanvas().deleteLimiter();
		} else {
			
		}
	}
	/** Called when confirmation button is clicked
	 *  If no limiters is added, closes dialog.
	 *  If amount of limiters added is larger than 3, draws limitation polygon.
	 *  If amount of limiters added is larger than 0 and smaller than 3, calls alert.
	 *  */
	@FXML
	private void handleOk() {
		if(limiters.size()==0){
			okClicked = true;
			dialogStage.close();
		}
		else if(limiters.size()>=3){
			mainApp.getCanvas().deleteLimiter();
			mainApp.getCanvas().drawLimitationPolygon(limiters);
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
	/** Called when user wants to enter coordinate for limitation.
	 * {@link #showLimiterAddDialog() showLimiterAddDialog()} will be calle.
	 *  */
	@FXML
	private void handleLimiterAdd() {
		boolean okClicked = this.showLimiterAddDialog();
		if (okClicked) {
			fillListView();
		}

	}
	/** Called when user wants to enter coordinate for limitation.
	 * New Stage will be created with @{@link mppa.view.AddLimiterController AddLimiterController} class. 
	 * @return True if dialog is closed properly, false if any exception occurs. 
	 *  */
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

			AddLimiterController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setMainApp(mainApp);
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	/** Called when user wants to delete on coordinate in limitation polygon
	 * If no element is chosen, alert will be displayed.
	 * If user chooses to delete coordinate then coordinate will be removed from 
	 * {@link #list list} and {@link #limiters limiters}.
	 * 
	 *  */
	@FXML
	private void handleDeleteLimiter() {
		int selectedIndex = list.getSelectionModel().getSelectedIndex();
		if (selectedIndex >= 0) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Kas soovid piirajapunkti kustutada?");
			alert.setHeaderText("Kas soovid piirajapunkti kustutada?");

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
			alert.setHeaderText("Uhtegi piirajat pole valitud");
			alert.setContentText("Vali tabelist piiraja.");

			alert.showAndWait();
		}
	}

}
