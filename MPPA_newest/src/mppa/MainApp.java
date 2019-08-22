package mppa;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeMap;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mppa.model.*;
import mppa.view.AmmoEditController;
import mppa.view.CorrectionController;
import mppa.view.LimitersController;
import mppa.view.PolarTargetController;
import mppa.view.PositionController;
import mppa.view.PositionEditController;
import mppa.view.ShootingController;
import mppa.view.SquareTargetController;
import mppa.view.TargetController;
import mppa.view.TargetEditController;
import mppa.view.TargetInfoController;
import mppa.view.TempEditController;
import mppa.view.TukTargetController;

/**
 * MainApp.java Main class for running the application
 *
 * @author Argo Neumann
 */
public class MainApp extends Application {
	// TO-DO 
	/*
	 * MGRS coordinate numbers on screen´s sides
	 * Saving corrections with save function
	 */

	// **************************************************
	// Fields
	// **************************************************
	/** Primary stage. */
	private Stage primaryStage;
	/** Root layout. */
	private AnchorPane rootLayout;
	/** Temperature. */
	private Integer temp = 5; // default temperature =5
	/** Ammo caliber. */
	private Integer ammo = 0;// 0=120mm ,1=81mm, default 120mm
	/** Fragment mine shooting table TreeMap. */
	private TreeMap<Double, ArrayList<ArrayList<Double>>> kild = new TreeMap<Double, ArrayList<ArrayList<Double>>>();
	/** Lightning mine shooting table TreeMap. */
	private TreeMap<Double, ArrayList<ArrayList<Double>>> valgus = new TreeMap<Double, ArrayList<ArrayList<Double>>>();
	/** Smoke mine shooting table TreeMap. */
	private TreeMap<Double, ArrayList<ArrayList<Double>>> suits = new TreeMap<Double, ArrayList<ArrayList<Double>>>();
	/** Observable list of all the targets. */
	private ObservableList<Target> targetData = FXCollections.observableArrayList();
	/** Observable list of all the updates. */
	private ObservableList<Integer> updates = FXCollections.observableArrayList();
	/** Observable list of all the positions. */
	private ObservableList<Position> positionData = FXCollections.observableArrayList();
	/** Two-dimensional array containing limitation polygon points. */
	private ArrayList<ArrayList<Integer>> limiters = new ArrayList<ArrayList<Integer>>();
	/** Active target. */
	private Target activeTarget = new Target();
	/** Boolean whether there is active target or not. */
	private boolean isActiveTarget = false;
	/** Canvas. */
	private ViewCanvas canvas;
	/** Previous target. */
	private Target prevTarget = new Target();

	/**
	 * Starts the application. Initializes root layout and main views .
	 * 
	 * @param primaryStage
	 *            Primary stage.
	 */
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Miinipilduja Arvestusprogramm");

		initRootLayout();
		showTargetView();
		showShootingView();
	}

	/**
	 * Converts all data to one string
	 * 
	 * @return String of all data.
	 */
	public String saveData() {
		String text = "";
		text += "pos\n";
		for (Position i : positionData) {
			text += i.toData() + "\n";
		}
		text += "tar\n";
		for (Target i : targetData) {
			text += i.toData() + "\n";
		}
		text += "end\n";
		return text;

	}

	/** Clears graphics and deletes all targets. */
	public void clearData() {
		canvas.clearCanvas();
		targetData.clear();

	}

	/**
	 * Reads data from file and adds positions and targets from it.
	 * 
	 * @param file
	 *            File to be read .
	 */
	public void loadData(File file) throws FileNotFoundException, IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			clearData();
			String line;
			String state = "";
			while ((line = br.readLine()) != null) {
				if (line.equals("pos")) {
					state = "pos";
					continue;
				} else if (line.equals("tar")) {
					state = "tar";
					continue;
				} else if (line.equals("end")) {
					break;
				}
				if (state == "pos") {
					String[] result = line.split(";");
					String posNr = String.valueOf(result[0].charAt(3));
					Position pos = positionData.get(Integer.parseInt(posNr) - 1);
					pos.setEasting(result[1]);
					pos.setNorthing(result[2]);
					pos.setMainDirection(result[3]);
					pos.setBackUpDirection(result[4]);
				}
				if (state == "pos1") {
					String[] result = line.split(";");
					Position pos = new Position(result[0], result[1], result[2], result[3], result[4],
							Boolean.getBoolean(result[5]));
					positionData.add(pos);
				}
				if (state == "tar") {
					String[] result = line.split(";");
					Target tar = new Target(result[0], result[1], result[2], result[3], result[4], result[5],
							result[6]);
					tar.setTag(Integer.parseInt(result[7]));
					tar.setNameSet(Boolean.getBoolean(result[8]));
					targetData.add(tar);
				}
			}
			updates.add(1);

		}
	}

	/**
	 * Reads data from file and fills shooting table TreeMaps from it.
	 * 
	 * @param file
	 *            File to be read
	 * @param map
	 *            Map to be written.           
	 */
	public void fillTable(String file, TreeMap<Double, ArrayList<ArrayList<Double>>> map)
			throws java.io.FileNotFoundException, java.io.IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				processLine(line, map);
			}
		}
	}

	/**
	 * Reads data from all ammo types and calls
	 * {@link #fillTable(String, TreeMap) fillTable(String, TreeMap) } method on
	 * every ammo type based on ammo caliber.
	 * 
	 * @param ammo
	 *            Ammo caliber.
	 */
	public void readAmmo(int ammo) throws FileNotFoundException, IOException {
		if (ammo == 0) {// 120mm
			fillTable("data/kild_120.txt", kild);
			fillTable("data/valgus_120.txt", valgus);
			fillTable("data/suits_120.txt", suits);
		} else if (ammo == 1) { // 81mm
			fillTable("data/kild_81.txt", kild);
			fillTable("data/valgus_81.txt", valgus);
			fillTable("data/suits_81.txt", suits);
		}
	}

	/**
	 * Called if ammo caliber is changed. Clears all shooting table files and
	 * refills them. Calls {@link #readAmmo(int) readAmmo(int) } method and sets
	 * new Active target.
	 */
	public void updateAmmo() {
		try {
			kild.clear();
			suits.clear();
			valgus.clear();
			readAmmo(getAmmo());
			setActiveTarget(getActiveTarget());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Processes line from shooting table file.
	 * 
	 * 
	 * @param line
	 *            Line
	 * @param map
	 *            Shooting table where data will be added.
	 */
	public void processLine(String line, TreeMap<Double, ArrayList<ArrayList<Double>>> map) {
		ArrayList<ArrayList<Double>> finalInfo = new ArrayList<>();
		ArrayList<Double> shootingInfo = new ArrayList<Double>();
		ArrayList<String> stringList = new ArrayList<String>(Arrays.asList(line.split("\\t")));
		int kordused = 0;
		double kaugus = Double.valueOf(stringList.get(0)); // Distance
		for (int i = 1; i < stringList.size(); i++) {
			double tostenurk = Double.parseDouble(stringList.get(i)); // Lifting
																		// angle
			if (tostenurk != 0.0) {
				if (kordused >= 1) {// If possible to shoot with more than one
									// charge
					ArrayList<Double> shootingInfo1 = new ArrayList<Double>();
					double laeng = (double) Math.round(i / 2); // Charge
					double aeg = Double.parseDouble(stringList.get(i + 1)); // Time
																			// of
																			// flight
					shootingInfo1.add(laeng);
					shootingInfo1.add(tostenurk);
					shootingInfo1.add(aeg);
					i += 1;
					finalInfo.add(shootingInfo1);

				} else {
					double laeng = (double) Math.round(i / 2);
					double aeg = Double.parseDouble(stringList.get(i + 1));
					shootingInfo.add(laeng);
					shootingInfo.add(tostenurk);
					shootingInfo.add(aeg);
					i += 1;
					kordused += 1;
					finalInfo.add(shootingInfo);
				}

			}
		}
		map.put(kaugus, finalInfo);

	}

	/**
	 * Calculates distance and direction between position and all target points.
	 * 
	 * 
	 * @param position
	 *            Position.
	 * @param target
	 *            Target.
	 * @return Two-dimensional array containing distance and direction of from position to each target point (fragment, smoke and lightning).
	 */
	public ArrayList<ArrayList<Double>> distanceCalculator(Position position, Target target) {
		ArrayList<ArrayList<Double>> shootingDatas = new ArrayList<ArrayList<Double>>();
		if (target.toString() != null) {// poleks tühi sihtmärk
			ArrayList<ArrayList<ArrayList<Integer>>> targetPoints = target.getTargets();// TO-DO
			ArrayList<ArrayList<Integer>> lastTargetPoints = targetPoints.get(targetPoints.size() - 1);
			for (ArrayList<Integer> i : lastTargetPoints) {
				ArrayList<Double> targetInfo = new ArrayList<Double>();
				int positionEasting = Integer.parseInt(position.getEasting());
				int positionNorthing = Integer.parseInt(position.getNorthing());
				int targetEasting = i.get(0);
				int targetNorthing = i.get(1);
				double distance = Math.sqrt(
						Math.pow(positionEasting - targetEasting, 2) + Math.pow(positionNorthing - targetNorthing, 2))
						* 10.0;
				double angle = Math.atan2(targetEasting - positionEasting, targetNorthing - positionNorthing) * 180
						/ Math.PI;
				if (angle < 0) {
					angle += 360.0; // Direction in angles.
				}
				angle = Math.round(angle * 17.777777777778); // Direction to
																// NATO MILS
				double tooFarDist = 50000.0; //If distance is too far, then needs to recalculate where the target is.
				if (distance > tooFarDist) {
					if ((Math.abs(positionEasting - targetEasting) > 5000)
							&& Math.abs(positionNorthing - targetNorthing) > 5000) {
						if (positionEasting > targetEasting) {
							targetEasting = 10000 + targetEasting;
						} else {
							targetEasting = -(10000 - targetEasting);
						}
						if (positionNorthing > targetNorthing) {
							targetNorthing = 10000 + targetNorthing;
						} else {
							targetNorthing = -(10000 - targetNorthing);
						}

						double distance1 = Math.sqrt(Math.pow(positionEasting - targetEasting, 2)
								+ Math.pow(positionNorthing - targetNorthing, 2)) * 10.0;
						double angle1 = Math.atan2(targetEasting - positionEasting, targetNorthing - positionNorthing)
								* 180 / Math.PI;
						if (angle1 < 0) {
							angle1 += 360.0;
						}
						angle1 = Math.round(angle1 * 17.777777777778);
						targetInfo.add(distance1);
						targetInfo.add(angle1);

					} else if (Math.abs(positionEasting - targetEasting) > 5000) {
						if (positionEasting > targetEasting) {
							targetEasting = 10000 + targetEasting;
						} else {
							targetEasting = -(10000 - targetEasting);
						}
						double distance1 = Math.sqrt(Math.pow(positionEasting - targetEasting, 2)
								+ Math.pow(positionNorthing - targetNorthing, 2)) * 10.0;
						double angle1 = Math.atan2(targetEasting - positionEasting, targetNorthing - positionNorthing)
								* 180 / Math.PI;
						if (angle1 < 0) {
							angle1 += 360.0;
						}
						angle1 = Math.round(angle1 * 17.777777777778);
						targetInfo.add(distance1);
						targetInfo.add(angle1);

					} else if (Math.abs(positionNorthing - targetNorthing) > 5000) {
						if (positionNorthing > targetNorthing) {
							targetNorthing = 10000 + targetNorthing;
						} else {
							targetNorthing = -(10000 - targetNorthing);
						}
						double distance1 = Math.sqrt(Math.pow(positionEasting - targetEasting, 2)
								+ Math.pow(positionNorthing - targetNorthing, 2)) * 10.0;
						double angle1 = Math.atan2(targetEasting - positionEasting, targetNorthing - positionNorthing)
								* 180 / Math.PI;
						if (angle1 < 0) {
							angle1 += 360.0;
						}
						distance1 = Math.round(distance1);
						angle1 = Math.round(angle1 * 17.777777777778);
						targetInfo.add(distance1);
						targetInfo.add(angle1);

					}
				} else {
					distance = Math.round(distance);
					targetInfo.add(distance);
					targetInfo.add(angle);

				}

				shootingDatas.add(targetInfo);

			}

		} else {
			for (int i = 0; i < 3; i++) {
				ArrayList<Double> targetInfo = new ArrayList<Double>();
				targetInfo.add(0.0);
				targetInfo.add(0.0);
				shootingDatas.add(targetInfo);
			}

		}
		return shootingDatas;

	}
	/**
	 * Gets shooting info for shooting points from shooting tables.
	 * 
	 * 
	 * @param shootingPoints
	 *            Shooting points for all three ammo types.
	 * @return Three-dimensional array, containing all plausible shooting  options for all ammo types.
	 */
	public ArrayList<ArrayList<ArrayList<Double>>> shootingInfo(ArrayList<ArrayList<Double>> shootingPoints) {
		ArrayList<ArrayList<ArrayList<Double>>> data = new ArrayList<>();
		if (!shootingPoints.isEmpty()) {
			boolean tooFar = true; // KILD
			boolean tooFar2 = true; // SUITS
			boolean tooFar3 = true; // VALGUS
			ArrayList<Double> kildPoint = shootingPoints.get(0);
			ArrayList<Double> suitsPoint = shootingPoints.get(1);
			ArrayList<Double> valgusPoint = shootingPoints.get(2);
			for (double i : kild.keySet()) { //Iterates through distances from shooting tables, starting from lowest
				if (kildPoint.get(0) <= i) {// Compares to shooting points distance
					tooFar = false;
					ArrayList<ArrayList<Double>> kildData = kild.get(i);//Two dimensional array containing all shooting options for given ammo type.
					data.add(kildData);
					break;
				}
			}
			if (tooFar) {//If not possible to shoot, adds an empty list to data.
				data.add(new ArrayList<>());
			}

			for (double i : suits.keySet()) {
				if (tempDiffCalc(suitsPoint.get(0)) <= i) {
					tooFar2 = false;
					ArrayList<ArrayList<Double>> suitsData = suits.get(i);
					data.add(suitsData);
					break;
				}
			}
			if (tooFar2) {
				data.add(new ArrayList<>());
			}
			for (double i : valgus.keySet()) {
				if (tempDiffCalc(valgusPoint.get(0)) <= i) {
					tooFar3 = false;
					ArrayList<ArrayList<Double>> valgusData = valgus.get(i);
					data.add(valgusData);
					break;
				}
			}
			if (tooFar3) {
				data.add(new ArrayList<>());
			}

		}
		return data;
	}
	/**
	 * Calculates new distance based on temperature.
	 * 
	 * 
	 * @param distance
	 *            Original distance.
	 * @return Calculated distance.
	 */
	public double tempDiffCalc(double distance) {
		if (getTemp() == 5) {//Distance is not changed when temperature=5
			return distance;
		}
		if (getTemp() < 5) {
			double deltaTemp = Math.abs(getTemp() - 6.0);
			double deltaDist = deltaTemp * 0.0016 * (distance);
			return distance + deltaDist;
		} else {
			double deltaTemp = Math.abs(getTemp() - 4.5);
			double deltaDist = deltaTemp * 0.0016 * (distance);
			return distance - deltaDist;
		}
	}
	/**
	 * Constructor.
	 * Calls {@link #readAmmo(int) readAmmo(int)} method to read shooting tables.
	 * Adds three positions with no data.
	 */
	public MainApp() {

		try {
			readAmmo(getAmmo());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		positionData.add(new Position("POS1", "", "", "", "", false));
		positionData.add(new Position("POS2", "", "", "", "", false));
		positionData.add(new Position("POS3", "", "", "", "", false));

	}
	/**
	 * Returns {@link #targetData targetData}.
	 * @return Target data
	 */
	public ObservableList<Target> getTargetData() {
		return targetData;
	}
	/**
	 * Returns {@link #positionData positionData}.
	 * @return Position data
	 */
	public ObservableList<Position> getPositionData() {
		return positionData;
	}

	/**
	 * Initializes the root layout.
	 */
	public void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/RootLayoutView.fxml"));
			rootLayout = (AnchorPane) loader.load();
			canvas = new ViewCanvas();
			rootLayout.getChildren().add(canvas);
			AnchorPane.setTopAnchor(canvas, 120.0);

			PositionController controller = loader.getController();
			controller.setMainApp(this);
			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			SceneGestures sceneGestures = new SceneGestures(canvas);
			canvas.setTranslateX(-900);//Sets default view point to center of grid
			canvas.setTranslateY(-1400);
			scene.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
			scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
			scene.addEventFilter(ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(new Image("file:logo.png"));
			primaryStage.setMaximized(true);
			primaryStage.show();
			canvas.toBack();
			canvas.addGrid();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Returns {@link #canvas canvas}.
	 * @return ViewCanvas canvas.
	 */
	public ViewCanvas getCanvas() {
		return this.canvas;
	}
	/**
	 * Shows target view.
	 */
	public void showTargetView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/TargetListView.fxml"));
			AnchorPane targetView = (AnchorPane) loader.load();

			AnchorPane.setTopAnchor(targetView, 86.0);
			rootLayout.getChildren().add(targetView);
			AnchorPane.setRightAnchor(targetView, 0.0);
			AnchorPane.setLeftAnchor(targetView, 0.0);

			TargetController controller = loader.getController();
			controller.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Shows shooting view.
	 */
	public void showShootingView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/ShootingView.fxml"));
			AnchorPane shootingView = (AnchorPane) loader.load();

			AnchorPane.setBottomAnchor(shootingView, 0.0);
			rootLayout.getChildren().add(shootingView);
			AnchorPane.setRightAnchor(shootingView, 0.0);
			AnchorPane.setLeftAnchor(shootingView, 0.0);

			ShootingController controller = loader.getController();
			controller.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	/**
	 * Shows target editing dialog.
	 * @param target Target
	 * @return True if dialog is closed, false if not.
	 */
	public boolean showTargetEditDialog(Target target) {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/TargetEditDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Muuda SM");
			dialogStage.getIcons().add(new Image("file:logo.png"));
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			TargetEditController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setTarget(target);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * Shows square target adding dialog
	 * @param target Target
	 * @return True if dialog is closed, false if not.
	 */	
	public boolean showSquareTargetAdd(Target target) {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/SquareTargetView.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Lisa SM");
			dialogStage.getIcons().add(new Image("file:logo.png"));
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			SquareTargetController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setTarget(target);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * Shows fire transmission target adding dialog.
	 * @param target Target
	 * @return True if dialog is closed, false if not.
	 */
	public boolean showTukTargetAdd(Target target) {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/TukTargetView.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Lisa TÜK SM");
			dialogStage.getIcons().add(new Image("file:logo.png"));
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			TukTargetController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setTarget(target);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * Returns temperature.
	 * @return Temperature.
	 */
	public int getTemp() {
		return temp;
	}
	/**
	 * Sets temperature and forces to recalculate all shooting data.
	 * @param temperature Temperature.
	 */
	public void setTemp(int temperature) {
		this.temp = temperature;
		setActiveTarget(getActiveTarget());
	}
	/**
	 * Returns ammo caliber.
	 * @return Ammo caliber.
	 */
	public int getAmmo() {
		return ammo;
	}
	/**
	 * Sets ammo caliber.
	 * @param ammo Ammo caliber.
	 */
	public void setAmmo(int ammo) {
		this.ammo = ammo;
	}
	/**
	 * Returns temperature in correct format.
	 * @return Temperature in string.
	 */
	public String getTempString() {
		final String DEGREE = "\u00b0";
		if (temp == 0) {
			return "0";
		}
		if (temp > 0) {
			return "+" + temp.toString() + DEGREE + "C";
		} else {
			return temp.toString() + DEGREE + "C";
		}

	}
	/**
	 * Shows temperature editing dialog.
	 * @return True if dialog is closed, false if not.
	 */
	public boolean showTempEditDialog() {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/TempEdit.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Muuda temperatuuri");
			dialogStage.getIcons().add(new Image("file:logo.png"));
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			TempEditController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setMainApp(this);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * Shows ammo caliber editing dialog.
	 * @return True if dialog is closed, false if not.
	 */
	public boolean showAmmoEditDialog() {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/ChangeAmmoView.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Muuda moona");
			dialogStage.getIcons().add(new Image("file:logo.png"));
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			AmmoEditController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setMainApp(this);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * Shows position editing dialog.
	 * @return True if dialog is closed, false if not.
	 */
	public boolean showPositionEditDialog() {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/PositionEditDialog.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Muuda positsiooni info");
			dialogStage.getIcons().add(new Image("file:logo.png"));
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			// Set the person into the controller.
			PositionEditController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setMainApp(this);
			controller.updateFields();

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();
			this.setActiveTarget(this.getActiveTarget());

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * Called when positions are loaded from file, forces to draw and calculate all context.
	 * @return True if dialog is closed, false if not.
	 */
	public boolean CallPositionEdit() {

		try {
			FXMLLoader loader = new FXMLLoader();
			AnchorPane page;
			loader.setLocation(MainApp.class.getResource("view/PositionEditDialog.fxml"));
			page = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Muuda positsiooni info");
			dialogStage.getIcons().add(new Image("file:logo.png"));
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			PositionEditController controller = loader.getController();
			controller.setMainApp(this);
			controller.updateFields();
			controller.handleOk1();

			this.setActiveTarget(this.getActiveTarget());

			return controller.isOkClicked();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}
	/**
	 * Returns primary stage
	 * @return Primary stage.
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	/**
	 * Draws all ballistic points for regular target.
	 * @param target Target.
	 */
	public void drawBallisticPoints(Target target) {
		ArrayList<ArrayList<ArrayList<Integer>>> targets = target.getTargets();
		ArrayList<ArrayList<Integer>> topoTargets = targets.get(0);
		ArrayList<ArrayList<Integer>> lastTargets = targets.get(targets.size() - 1);
		for (int i = 0; i < topoTargets.size(); i++) {
			if (!topoTargets.get(i).equals(lastTargets.get(i))) {
				if (i == 0) {// Shell
					ArrayList<Integer> point = lastTargets.get(0);
					canvas.drawBallisticPoint(point.get(0).toString(), point.get(1).toString(), "K",
							target.getTargetname());
				}
				if (i == 1) {// Smoke
					ArrayList<Integer> point = lastTargets.get(1);
					canvas.drawBallisticPoint(point.get(0).toString(), point.get(1).toString(), "S",
							target.getTargetname());
				}
				if (i == 2) {// Light
					ArrayList<Integer> point = lastTargets.get(2);
					canvas.drawBallisticPoint(point.get(0).toString(), point.get(1).toString(), "V",
							target.getTargetname());
				}
			}
		}
	}
	/**
	 * Draws all ballistic points for region target.
	 * @param target Target.
	 * @param name Name of target.
	 * @param number Number of target.
	 */
	public void drawBallisticPoints(Target target, String name, String number) {
		ArrayList<ArrayList<ArrayList<Integer>>> targets = target.getTargets();
		ArrayList<ArrayList<Integer>> topoTargets = targets.get(0);
		ArrayList<ArrayList<Integer>> lastTargets = targets.get(targets.size() - 1);
		for (int i = 0; i < topoTargets.size(); i++) {
			if (!topoTargets.get(i).equals(lastTargets.get(i))) {
				if (i == 0) {// Shell
					ArrayList<Integer> point = lastTargets.get(0);
					canvas.drawBallisticPoint(point.get(0).toString(), point.get(1).toString(), number + "K", name);
				}
				if (i == 1) {// Smoke
					ArrayList<Integer> point = lastTargets.get(1);
					canvas.drawBallisticPoint(point.get(0).toString(), point.get(1).toString(), number + "S", name);
				}
				if (i == 2) {// Light
					ArrayList<Integer> point = lastTargets.get(2);
					canvas.drawBallisticPoint(point.get(0).toString(), point.get(1).toString(), number + "V", name);
				}
			}
		}
	}
	/**
	 * Sets active target. Draws and redraws target after target changes.
	 * Calculates shooting info between all positions and currently active target.
	 * @param target Target.
	 */
	public void setActiveTarget(Target target) {
		if (!this.getPositionData().isEmpty() && !getTargetData().isEmpty()) {
			this.activeTarget = target;
			if (this.isActiveTarget && activeTarget != null) {
				if (prevTarget.getEasting() != null) {// Deletes previous target
														// and redraws it as
														// non-active(green)
														// target
					if (prevTarget.isRegion()) {
						getCanvas().deleteTarget(target.getTargetname() + "1");
						getCanvas().deleteTarget(target.getTargetname() + "2");
						getCanvas().deleteTarget(target.getTargetname() + "3");
						Target first = prevTarget.getRegionTargets().get(0);
						Target second = prevTarget.getRegionTargets().get(1);
						Target third = prevTarget.getRegionTargets().get(2);
						getCanvas().drawTarget(first.getEasting(), first.getNorthing(), target.getTargetname(), "1",
								false);
						getCanvas().drawTarget(second.getEasting(), second.getNorthing(), target.getTargetname(), "2",
								false);
						getCanvas().drawTarget(third.getEasting(), third.getNorthing(), target.getTargetname(), "3",
								false);
					} else {
						getCanvas().deleteTarget(prevTarget.getTargetname());
						getCanvas().drawTarget(prevTarget.getEasting(), prevTarget.getNorthing(),
								prevTarget.getTargetname(), "", false);
					}

				}
				// Deletes current target and redraws it as active(red) target
				if (target.isRegion()) {// If target is region target
					getCanvas().deleteTarget(target.getTargetname() + "1");
					getCanvas().deleteTarget(target.getTargetname() + "2");
					getCanvas().deleteTarget(target.getTargetname() + "3");
					Target first = target.getRegionTargets().get(0);
					Target second = target.getRegionTargets().get(1);
					Target third = target.getRegionTargets().get(2);
					getCanvas().drawTarget(first.getEasting(), first.getNorthing(), target.getTargetname(), "1", true);
					getCanvas().drawTarget(second.getEasting(), second.getNorthing(), target.getTargetname(), "2",
							true);
					getCanvas().drawTarget(third.getEasting(), third.getNorthing(), target.getTargetname(), "3", true);
					if (!first.getLastCorrection().isEmpty()) {
						drawBallisticPoints(first, target.getTargetname(), "1");
						drawBallisticPoints(second, target.getTargetname(), "2");
						drawBallisticPoints(third, target.getTargetname(), "3");
					}
				} else {
					getCanvas().deleteTarget(target.getTargetname());
					getCanvas().drawTarget(target.getEasting(), target.getNorthing(), target.getTargetname(), "", true);
				}

				if (target.hasForwardObserver() == true) {
					getCanvas().drawForwardObserver(target.getFOEasting(), target.getFONorthing(),
							"fo" + target.getTargetname());
				}
				if (!target.getLastCorrection().isEmpty()) {
					drawBallisticPoints(target);
				}
			}

			for (int t = 0; t < this.positionData.size(); t++) {//Calculates shooting info for each position
				Position i = this.positionData.get(t);
				if (i.getEasting() != "" && this.isActiveTarget) {
					if (target.isRegion()) {//Calculations for region targets.
						ArrayList<ArrayList<Double>> testDist = distanceCalculator(i, target.getRegionTargets().get(t));
						i.getShootingData().clear();
						ArrayList<ArrayList<ArrayList<Double>>> extra = new ArrayList<>();
						extra.add(testDist);
						i.getShootingData().add(extra);
						i.getShootingData().add(shootingInfo(testDist));
					} else {
						ArrayList<ArrayList<Double>> testDist = distanceCalculator(i, target);
						i.getShootingData().clear();
						ArrayList<ArrayList<ArrayList<Double>>> extra = new ArrayList<>();
						extra.add(testDist);
						i.getShootingData().add(extra);
						i.getShootingData().add(shootingInfo(testDist));
					}
				}
			}

		}//Sets previous target to current target.
		prevTarget = target;

	}
	/**
	 * Returns active target.
	 * @return Active target
	 */
	public Target getActiveTarget() {
		return this.activeTarget;
	}
	/**
	 * Resets previous target.
	 */
	public void resetPrevTarget() {
		this.prevTarget = new Target();
	}
	/**
	 * Main method.
	 * @param args Arguments	 */
	public static void main(String[] args) {
		launch(args);
	}
	/**
	 * Shows polar  target adding dialog.
	 * @param target Target
	 * @return True if dialog is closed, false if not.
	 */
	public boolean showPolarTargetEditDialog(Target target) {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/PolarTargetView.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Polaarsihtmärgi lisamine");
			dialogStage.getIcons().add(new Image("file:logo.png"));
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			PolarTargetController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setTarget(target);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * Shows dialog for making corrections to target.
	 * @param target Target
	 * @return True if dialog is closed, false if not.
	 */
	public boolean showTargetCorrectionDialog(Target target) {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/Correction.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("SM korrektuur");
			dialogStage.getIcons().add(new Image("file:logo.png"));
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			CorrectionController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setTarget(target);
			controller.setMainApp(this);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * Returns if there is active target or not.
	 * @return True if there is active target, false if there is not.
	 */
	public boolean isActiveTarget() {
		return isActiveTarget;
	}
	/**
	 * Sets if there is active target or not boolean {@link #isActiveTarget isActiveTarget}.
	 * @param bool Boolean 
	 */
	public void SetIsActiveTarget(boolean bool) {
		this.isActiveTarget = bool;
	}

	/**
	 * Shows target information dialog.
	 * @param selectedTarget Target
	 * @return True if dialog is closed, false if not.
	 */
	public boolean showTargetInformationDialog(Target selectedTarget) {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/TargetInfo.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("SM info");
			dialogStage.getIcons().add(new Image("file:logo.png"));
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			TargetInfoController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setTarget(selectedTarget);
			controller.setMainApp(this);
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * Shows limitation adding dialog.
	 * @return True if dialog is closed, false if not.
	 */
	public boolean showLimitersEditDialog() {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/LimitersView.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Piirajad");
			dialogStage.getIcons().add(new Image("file:logo.png"));
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);

			LimitersController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setMainApp(this);
			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	/**
	 * Returns {@link #limiters limiters}.
	 * @return Two-dimensional array of limitation polygon points.
	 */
	public ArrayList<ArrayList<Integer>> getLimiters() {
		return limiters;
	}
	/**
	 * Sets {@link #limiters limiters}.
	 * @param limiters Two-dimensional array of limitation polygon points.
	 */
	public void setLimiters(ArrayList<ArrayList<Integer>> limiters) {
		this.limiters = limiters;
	}
	/**
	 * Returns {@link #updates updates}.
	 * @return Observable list of updates.
	 */
	public ObservableList<Integer> getUpdates() {
		return updates;
	}
	/**
	 * Sets {@link #updates updates}.
	 * @param updates Observable list of updates.
	 */
	public void setUpdates(ObservableList<Integer> updates) {
		this.updates = updates;
	}

}