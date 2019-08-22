package mppa.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import mppa.MainApp;
import mppa.model.Target;
/**
 * Class for controlling position data and displaying it.
 * 
 *
 * @author Argo Neumann
 */
public class PositionController {
	/** Drop-down list containing all targets. */
	@FXML
	private TableView<Target> targetTable;
	/** Position name. */
	@FXML
	private Label positionNameLabel;
	/** Easting label of first position. */
	@FXML
	private Label firstEastingLabel;
	/** Northing label of first position. */
	@FXML
	private Label firstNorthingLabel;
	/** Main direction label of first position. */
	@FXML
	private Label firstMainDirectionLabel;
	/** Backup direction label of first position. */
	@FXML
	private Label firstBackUpDirectionLabel;
	/** Easting label of second position. */
	@FXML
	private Label secondEastingLabel;
	/** Northing label of second position. */
	@FXML
	private Label secondNorthingLabel;
	/** Main direction label of second position. */
	@FXML
	private Label secondMainDirectionLabel;
	/** Backup direction label of second position. */
	@FXML
	private Label secondBackUpDirectionLabel;
	/** Easting label of third position. */
	@FXML
	private Label thirdEastingLabel;
	/** Northing label of third position. */
	@FXML
	private Label thirdNorthingLabel;
	/** Main direction label of third position. */
	@FXML
	private Label thirdMainDirectionLabel;
	/** Backup direction label of third position. */
	@FXML
	private Label thirdBackUpDirectionLabel;
	/** Temperature label. */
	@FXML
	private Label tempLabel;
	/** Ammunition caliber label. */
	@FXML
	private Label ammoLabel;
	/** Main application */
	private MainApp mainApp;
	/** Default constructor. */
	public PositionController() {
	}
	/** Set´s class main application and temperature.
	 * Adds listener to Main application Updates list, which updates fields if any update to positions id made.
	 * @param mainApp MainApp .
	 *  */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		tempLabel.setText(mainApp.getTempString());
		setAmmoLabel(this.mainApp.getAmmo());
		mainApp.getUpdates().addListener(new ListChangeListener<Integer>() {
			@Override
			public void onChanged(ListChangeListener.Change c) {
				updateFields();
				mainApp.CallPositionEdit();
			}

		});
		

	}
	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {
	}
	/**
	 * Updates all positions fields.
	 */
	public void updateFields() {
		firstEastingLabel.setText(mainApp.getPositionData().get(0).getEasting());
		firstNorthingLabel.setText(mainApp.getPositionData().get(0).getNorthing());
		firstMainDirectionLabel.setText(toDirection(mainApp.getPositionData().get(0).getMainDirection()));
		firstBackUpDirectionLabel.setText(toDirection(mainApp.getPositionData().get(0).getBackupDirection()));
		secondEastingLabel.setText(mainApp.getPositionData().get(1).getEasting());
		secondNorthingLabel.setText(mainApp.getPositionData().get(1).getNorthing());
		secondMainDirectionLabel.setText(toDirection(mainApp.getPositionData().get(1).getMainDirection()));
		secondBackUpDirectionLabel.setText(toDirection(mainApp.getPositionData().get(1).getBackupDirection()));
		thirdEastingLabel.setText(mainApp.getPositionData().get(2).getEasting());
		thirdNorthingLabel.setText(mainApp.getPositionData().get(2).getNorthing());
		thirdMainDirectionLabel.setText(toDirection(mainApp.getPositionData().get(2).getMainDirection()));
		thirdBackUpDirectionLabel.setText(toDirection(mainApp.getPositionData().get(2).getBackupDirection()));
	}
	/**
	 * Changes input direction from type 0000 to 00-00. Or returns empty string if direction length is smaller than 4.
	 * @param s Input string
	 * @return direction in the form of 00-00
	 */
	private String toDirection(String s) {
		if (s.length() < 4) {
			return "";
		} else {
			s = new StringBuilder(s).insert(s.length() - 2, "-").toString();

			return s;
		}
	}
	/**
	 *  Sets ammo label text.
	 * @param ammo Ammo type
	 */
	private void setAmmoLabel(int ammo) {
		switch (ammo) {
		case 0:
			ammoLabel.setText("120mm");
			break;
		case 1:
			ammoLabel.setText("81mm");
			break; 
		default:
			ammoLabel.setText("120mm");
			break;
		}
	}
	/**
	 *  Called when user clicks on new square target button "RUUT".
	 *  Creates new target and passes it to main application .
	 *  When user has finished entering target information, the new target will be set as active target.
	 */
	@FXML
	private void handleNewTarget() {
		Target tempTarget = new Target();
		boolean okClicked = mainApp.showSquareTargetAdd(tempTarget);
		if (okClicked) {
			mainApp.SetIsActiveTarget(true);
			mainApp.getTargetData().add(tempTarget);
			
		}
	}

	/**
	 *  Called when user clicks on temperature button "TEMP".
	 *  Temperature editing dialog will be shown to the user.
	 *  Temperature label will be updated with new temperature.
	 */
	@FXML
	private void handleTempEdit() {
		boolean okClicked = mainApp.showTempEditDialog();
		if (okClicked) {
			tempLabel.setText(mainApp.getTempString());
		}
	}
	/**
	 *  Called when user clicks on ammo editing button "MOON".
	 *  Ammo editing dialog will be shown to the user.
	 *  Ammo label and shooting tables will be updated.
	 */
	@FXML
	private void handleAmmoEdit() {
		boolean okClicked = mainApp.showAmmoEditDialog();
		if (okClicked) {
			setAmmoLabel(mainApp.getAmmo());
			mainApp.updateAmmo();
		}
	}
	/**
	 *  Called when user clicks on "SALVESTA" button.
	 *  Alert will be shown. When user wants to save current position and targets, then
	 *  file chooser will be prompted.
	 *  Creates .txt file with information about positions and targets.
	 */
	@FXML
	private void handleSaveData() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Laua salvestamine");
		alert.setHeaderText("Kas soovid lauda salvestada?");

		ButtonType salvesta = new ButtonType("Salvesta");
		ButtonType katkesta = new ButtonType("Katkesta",ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(salvesta,katkesta);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == salvesta){
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().addAll(
	                new FileChooser.ExtensionFilter("TXT", "*.txt")
	            );
			fileChooser.setTitle("Vali laud");
			File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
			if(file != null){
                SaveFile(mainApp.saveData(), file);
            }
		} else {
			
		}
		
	}
	/**
	 *  Writes content string into file.
	 *  @param content Content of the file
	 *  @param file File to be written.
	 */
	 private void SaveFile(String content, File file){
	        try {
	            FileWriter fileWriter = null;
	             
	            fileWriter = new FileWriter(file);
	            fileWriter.write(content);
	            fileWriter.close();
	        } catch (IOException ex) {
	            
	        }
	         
	    }
	 /**
		 *  Called when user clicks on "LAE LAUD" button.
		 *  When user agrees to load information from file, file chooser will be prompted.
		 *  File will be passed to main application, where the data will be loaded.
		 */
	@FXML
	private void handleLoadData() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Laua lugemine");
		alert.setHeaderText("Kas soovid lauda lugeda?");

		ButtonType lae = new ButtonType("Loe laud");
		ButtonType katkesta = new ButtonType("Katkesta",ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(lae,katkesta);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == lae){
			try {
				FileChooser fileChooser = new FileChooser();
				fileChooser.getExtensionFilters().addAll(
		                new FileChooser.ExtensionFilter("TXT", "*.txt")
		            );
				fileChooser.setTitle("Vali laud");
				File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());
				if (file != null) {
					mainApp.loadData(file);
                }
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			
		}
	}
	/**
	 *  Called when user clicks on new  target button "TÜK".
	 *  Creates new target and passes it to main application .
	 *  When user has finished entering target information, the new target will be set as active target.
	 */
	@FXML
	private void handleNewTukTarget() {
		Target tempTarget = new Target();
		boolean okClicked = mainApp.showTukTargetAdd(tempTarget);
		if (okClicked) {
			mainApp.SetIsActiveTarget(true);
			mainApp.getTargetData().add(tempTarget);
		}
	}
	/**
	 *  Called when user clicks position button "POSITSIOON"
	 *  Position editing dialog will be shown.
	 *  After saving, all positions fields will be updated.
	 */
	@FXML
	private void handlePositionEdit() {
		boolean okClicked = mainApp.showPositionEditDialog();
		if (okClicked) {
			updateFields();
		}

	}
	/**
	 *  Called when user clicks on new polar target button "POL".
	 *  Creates new target and passes it to main application .
	 *  When user has finished entering target information, the new target will be set as active target.
	 */
	@FXML
	private void handleNewPolarTarget() {
		Target tempTarget = new Target();
		boolean okClicked = mainApp.showPolarTargetEditDialog(tempTarget);
		if (okClicked) {
			mainApp.SetIsActiveTarget(true);
			mainApp.getTargetData().add(tempTarget);
			
		}
	}
	/**
	 *  Called when user wants to exit program.
	 */
	@FXML
	private void handleExitProgram(){
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Kas soovid programmi sulgeda?");
		alert.setHeaderText("Kas soovid programmi sulgeda?");

		ButtonType sulge = new ButtonType("Sulge");
		ButtonType katkesta = new ButtonType("Katkesta",ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(sulge,katkesta);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == sulge){
			System.exit(0);
		} else {
			
		}
		
	}
	/**
	 *  Called when user wants to add or edit limitation polygon.
	 */
	@FXML
	private void handleLimitersEdit() {
		boolean okClicked = mainApp.showLimitersEditDialog();
		if (okClicked) {
		}

	}

}
