package mppa;

import java.io.BufferedReader;
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

public class MainApp extends Application {
	// TO-DO list
	/*
	 * Maa-ala sihtmärkidele
	 * Ballistika Laua salvestamine Graafika SM kinnitamine 
	 */

	private Stage primaryStage;
	private AnchorPane rootLayout;
	private Integer temp = 5; // default temperatuur =5
	private Integer ammo = 0;// 0=120mm ,1=81mm, default 120mm
	private TreeMap<Double, ArrayList<ArrayList<Double>>> kild = new TreeMap<Double, ArrayList<ArrayList<Double>>>();
	private TreeMap<Double, ArrayList<ArrayList<Double>>> valgus = new TreeMap<Double, ArrayList<ArrayList<Double>>>();
	private TreeMap<Double, ArrayList<ArrayList<Double>>> suits = new TreeMap<Double, ArrayList<ArrayList<Double>>>();
	private ObservableList<Target> targetData = FXCollections.observableArrayList();
	private ObservableList<Position> positionData = FXCollections.observableArrayList();
	private ArrayList<ArrayList<Integer>> limiters = new ArrayList<ArrayList<Integer>>();
	private Target activeTarget = new Target();
	private boolean isActiveTarget = false;
	private ViewCanvas canvas;

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("Miinipilduja Arvestusrakendus");

		initRootLayout();
		showTargetView();
		showShootingView();
		//showGraphics();
	}

	public void fillTable(String file, TreeMap<Double, ArrayList<ArrayList<Double>>> map)
			throws FileNotFoundException, IOException {
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				processLine(line, map);
			}
		}
	}

	public void readAmmo(int ammo) throws FileNotFoundException, IOException {
		if (ammo == 0) {// 120mm
			fillTable("data/kild_120_uus.txt", kild);
			fillTable("data/valgus_120.txt", valgus);
			fillTable("data/suits_120.txt", suits);
		} else if (ammo == 1) { // 81mm
			fillTable("data/kild_81.txt", kild);
			fillTable("data/valgus_81.txt", valgus);
			fillTable("data/suits_81.txt", suits);
		}
	}

	public void updateAmmo() {
		try {
			System.out.println(getAmmo());
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

	public void processLine(String line, TreeMap<Double, ArrayList<ArrayList<Double>>> map) {
		ArrayList<ArrayList<Double>> finalInfo = new ArrayList<>();
		ArrayList<Double> shootingInfo = new ArrayList<Double>();
		ArrayList<String> stringList = new ArrayList<String>(Arrays.asList(line.split("\\t")));
		int kordused = 0;
		double kaugus = Double.valueOf(stringList.get(0));
		for (int i = 1; i < stringList.size(); i++) {
			double tostenurk = Double.parseDouble(stringList.get(i));
			if (tostenurk != 0.0) {
				if (kordused >= 1) {
					ArrayList<Double> shootingInfo1 = new ArrayList<Double>();
					double laeng = (double) Math.round(i / 2);
					double aeg = Double.parseDouble(stringList.get(i + 1));
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

	public ArrayList<ArrayList<Double>> distanceCalculator(Position position, Target target) {
		ArrayList<ArrayList<Double>> shootingDatas = new ArrayList<ArrayList<Double>>();
		if (target.toString() != null) {// poleks tühi sihtmärk
			ArrayList<ArrayList<ArrayList<Integer>>> targetPoints = target.getTargets();// TO-DO
			ArrayList<ArrayList<Integer>> lastTargetPoints = targetPoints.get(targetPoints.size() - 1);
			for (ArrayList<Integer> i : lastTargetPoints) {
				ArrayList<Double> targetInfo = new ArrayList<Double>();
				int positionEasting = Integer.parseInt(position.getEasting());
				int positionNorthing = Integer.parseInt(position.getNorthing());
				if (target.getTag() == 1) {// SQUARE TARGET
					int targetEasting = i.get(0);
					int targetNorthing = i.get(1);
					double distance = Math.sqrt(Math.pow(positionEasting - targetEasting, 2)
							+ Math.pow(positionNorthing - targetNorthing, 2)) * 10.0;
					double angle = Math.atan2(targetEasting - positionEasting, targetNorthing - positionNorthing) * 180
							/ Math.PI;
					if (angle < 0) {
						angle += 360.0; // Direction in angle
					}
					angle = Math.round(angle * 17.777777777778); // Direction to
																	// NATO
																	// MILS
					if (distance > 50000.0) {
						if ((Math.abs(positionEasting - targetEasting) > 5000)
								&& Math.abs(positionNorthing - targetNorthing) > 5000) {
							targetEasting = -(10000 - targetEasting);
							targetNorthing = -(10000 - targetNorthing);
							double distance1 = Math.sqrt(Math.pow(positionEasting - targetEasting, 2)
									+ Math.pow(positionNorthing - targetNorthing, 2)) * 10.0;
							double angle1 = Math.atan2(targetEasting - positionEasting,
									targetNorthing - positionNorthing) * 180 / Math.PI;
							if (angle1 < 0) {
								angle1 += 360.0;
							}
							angle1 = Math.round(angle1 * 17.777777777778);
							targetInfo.add(distance1);
							targetInfo.add(angle1);

						} else if (Math.abs(positionEasting - targetEasting) > 5000) {
							targetEasting = -(10000 - targetEasting);
							double distance1 = Math.sqrt(Math.pow(positionEasting - targetEasting, 2)
									+ Math.pow(positionNorthing - targetNorthing, 2)) * 10.0;
							double angle1 = Math.atan2(targetEasting - positionEasting,
									targetNorthing - positionNorthing) * 180 / Math.PI;
							if (angle1 < 0) {
								angle1 += 360.0;
							}
							angle1 = Math.round(angle1 * 17.777777777778);
							targetInfo.add(distance1);
							targetInfo.add(angle1);

						} else if (Math.abs(positionNorthing - targetNorthing) > 5000) {
							targetNorthing = -(10000 - targetNorthing);
							double distance1 = Math.sqrt(Math.pow(positionEasting - targetEasting, 2)
									+ Math.pow(positionNorthing - targetNorthing, 2)) * 10.0;
							double angle1 = Math.atan2(targetEasting - positionEasting,
									targetNorthing - positionNorthing) * 180 / Math.PI;
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

				}

				else if (target.getTag() == 2) {// POLAR TARGET
					if (target.getDirectionAngle() != null) {
						int targetEasting = i.get(0);
						int targetNorthing = i.get(1);

						double distance = Math.sqrt(Math.pow(positionEasting - targetEasting, 2)
								+ Math.pow(positionNorthing - targetNorthing, 2)) * 10.0;
						double angle = Math.atan2(targetEasting - positionEasting, targetNorthing - positionNorthing)
								* 180 / Math.PI;
						if (angle < 0) {
							angle += 360.0; // Direction in angle
						}
						angle = Math.round(angle * 17.777777777778); // Direction
																		// to
																		// NATO
																		// MILS
						if (distance > 50000.0) {
							if ((Math.abs(positionEasting - targetEasting) > 5000)
									&& Math.abs(positionNorthing - targetNorthing) > 5000) {
								targetEasting = -(10000 - targetEasting);
								targetNorthing = -(10000 - targetNorthing);
								double distance1 = Math.sqrt(Math.pow(positionEasting - targetEasting, 2)
										+ Math.pow(positionNorthing - targetNorthing, 2)) * 10.0;
								double angle1 = Math.atan2(targetEasting - positionEasting,
										targetNorthing - positionNorthing) * 180 / Math.PI;
								if (angle1 < 0) {
									angle1 += 360.0;
								}
								angle1 = Math.round(angle1 * 17.777777777778);
								targetInfo.add(distance1);
								targetInfo.add(angle1);

							} else if (Math.abs(positionEasting - targetEasting) > 5000) {
								targetEasting = -(10000 - targetEasting);
								double distance1 = Math.sqrt(Math.pow(positionEasting - targetEasting, 2)
										+ Math.pow(positionNorthing - targetNorthing, 2)) * 10.0;
								double angle1 = Math.atan2(targetEasting - positionEasting,
										targetNorthing - positionNorthing) * 180 / Math.PI;
								if (angle1 < 0) {
									angle1 += 360.0;
								}
								angle1 = Math.round(angle1 * 17.777777777778);
								targetInfo.add(distance1);
								targetInfo.add(angle1);

							} else if (Math.abs(positionNorthing - targetNorthing) > 5000) {
								targetNorthing = -(10000 - targetNorthing);
								double distance1 = Math.sqrt(Math.pow(positionEasting - targetEasting, 2)
										+ Math.pow(positionNorthing - targetNorthing, 2)) * 10.0;
								double angle1 = Math.atan2(targetEasting - positionEasting,
										targetNorthing - positionNorthing) * 180 / Math.PI;
								if (angle1 < 0) {
									angle1 += 360.0;
								}
								distance1 = Math.round(distance1);
								angle1 = Math.round(angle1 * 17.777777777778);
								targetInfo.add(distance1);// Check this?
								targetInfo.add(angle1);

							}
						} else {
							distance = Math.round(distance);
							targetInfo.add(distance);// Check this?
							targetInfo.add(angle);

						}

					}

				} else if (target.getTag() == 3) {// TĆ�K TARGET
					int targetEasting = i.get(0);
					int targetNorthing = i.get(1);
					double distance = Math.sqrt(Math.pow(positionEasting - targetEasting, 2)
							+ Math.pow(positionNorthing - targetNorthing, 2)) * 10.0;
					double angle = Math.atan2(targetEasting - positionEasting, targetNorthing - positionNorthing) * 180
							/ Math.PI;
					if (angle < 0) {
						angle += 360.0; // Direction in angle
					}
					angle = Math.round(angle * 17.777777777778); // Direction to
																	// NATO
																	// MILS
					if (distance > 50000.0) {
						if ((Math.abs(positionEasting - targetEasting) > 5000)
								&& Math.abs(positionNorthing - targetNorthing) > 5000) {
							targetEasting = -(10000 - targetEasting);
							targetNorthing = -(10000 - targetNorthing);
							double distance1 = Math.sqrt(Math.pow(positionEasting - targetEasting, 2)
									+ Math.pow(positionNorthing - targetNorthing, 2)) * 10.0;
							double angle1 = Math.atan2(targetEasting - positionEasting,
									targetNorthing - positionNorthing) * 180 / Math.PI;
							if (angle1 < 0) {
								angle1 += 360.0;
							}
							angle1 = Math.round(angle1 * 17.777777777778);
							targetInfo.add(distance1);
							targetInfo.add(angle1);

						} else if (Math.abs(positionEasting - targetEasting) > 5000) {
							targetEasting = -(10000 - targetEasting);
							double distance1 = Math.sqrt(Math.pow(positionEasting - targetEasting, 2)
									+ Math.pow(positionNorthing - targetNorthing, 2)) * 10.0;
							double angle1 = Math.atan2(targetEasting - positionEasting,
									targetNorthing - positionNorthing) * 180 / Math.PI;
							if (angle1 < 0) {
								angle1 += 360.0;
							}
							angle1 = Math.round(angle1 * 17.777777777778);
							targetInfo.add(distance1);
							targetInfo.add(angle1);

						} else if (Math.abs(positionNorthing - targetNorthing) > 5000) {
							targetNorthing = -(10000 - targetNorthing);
							double distance1 = Math.sqrt(Math.pow(positionEasting - targetEasting, 2)
									+ Math.pow(positionNorthing - targetNorthing, 2)) * 10.0;
							double angle1 = Math.atan2(targetEasting - positionEasting,
									targetNorthing - positionNorthing) * 180 / Math.PI;
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

	public ArrayList<ArrayList<ArrayList<Double>>> shootingInfo(ArrayList<ArrayList<Double>> shootingPoints) {
		ArrayList<ArrayList<ArrayList<Double>>> data = new ArrayList<>();
		if (!shootingPoints.isEmpty()) {
			boolean tooFar = true; // KILD
			boolean tooFar2 = true; // VALGUS
			boolean tooFar3 = true; // SUITS
			ArrayList<Double> kildPoint = shootingPoints.get(0);
			ArrayList<Double> suitsPoint = shootingPoints.get(1);
			ArrayList<Double> valgusPoint = shootingPoints.get(2);
			for (double i : kild.keySet()) { // Kild saab lasta kaugemale kui
												// suits
												// ja valgus uue moonaga
				if (kildPoint.get(0) <= i) {
					tooFar = false;
					ArrayList<ArrayList<Double>> kildData = kild.get(i);
					data.add(kildData);
					break;
				}
			}
			if (tooFar) {
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
			for (double i : valgus.keySet()) { // Kild saab lasta kaugemale kui
												// suits
				// ja valgus uue moonaga
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

	public double tempDiffCalc(double distance) {
		if (getTemp() == 5) {
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
		// Testdata
		positionData.add(new Position("1", "", "", "", "", false));
		positionData.add(new Position("2", "", "", "", "", false));
		positionData.add(new Position("3", "", "", "", "", false));
		targetData.add(new Target("AA4005", "0712", "1032", "0500"));
		targetData.add(new Target("RUUT", "9950", "9925", "1600"));
		targetData.add(new Target("RUUT", "0712", "1032", "0800"));
		targetData.add(new Target("AA1300", "0712", "4932", "0800"));
		targetData.add(new Target("AA2400", "0712", "1032", "0800"));
		targetData.add(new Target("AA9400", "5612", "1212", "0550"));
		targetData.add(new Target("AB5400", "3214", "1032", "0550"));
		targetData.add(new Target("AB5500", "2514", "1032", "0550"));

	}

	public ObservableList<Target> getTargetData() {
		return targetData;
	}

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
			rootLayout.setTopAnchor(canvas, 120.0);
			

			PositionController controller = loader.getController();
			controller.setMainApp(this);
			
			
			
			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			SceneGestures sceneGestures = new SceneGestures(canvas);
	        scene.addEventFilter( MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
	        scene.addEventFilter( MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
	        scene.addEventFilter( ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
			primaryStage.setScene(scene);
			primaryStage.getIcons().add(new Image("file:logo.png"));
			primaryStage.show();
			canvas.toBack();
			canvas.addGrid();
			
			

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showTargetView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/TargetListView.fxml"));
			AnchorPane targetView = (AnchorPane) loader.load();

			rootLayout.setTopAnchor(targetView, 86.0);
			rootLayout.getChildren().add(targetView);
			rootLayout.setRightAnchor(targetView, 0.0);
			rootLayout.setLeftAnchor(targetView, 0.0);

			TargetController controller = loader.getController();
			controller.setMainApp(this);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
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

	public int getTemp() {
		return temp;
	}

	public void setTemp(int temperature) {
		this.temp = temperature;
		setActiveTarget(getActiveTarget());
	}

	public int getAmmo() {
		return ammo;
	}

	public void setAmmo(int ammo) {
		this.ammo = ammo;
	}

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

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void setActiveTarget(Target target) {
		if (!this.getPositionData().isEmpty()) {
			this.activeTarget = target;
			for (Position i : this.getPositionData()) {
				if (i.getEasting() != ""  && this.isActiveTarget) {
					ArrayList<ArrayList<Double>> testDist = distanceCalculator(i, target);
					i.getShootingData().clear();
					ArrayList<ArrayList<ArrayList<Double>>> extra = new ArrayList<>();
					extra.add(testDist);
					i.getShootingData().add(extra);
					i.getShootingData().add(shootingInfo(testDist));
				}
			}

		}

	}

	public Target getActiveTarget() {
		return this.activeTarget;
	}

	public static void main(String[] args) {
		launch(args);
	}

	public boolean showPolarTargetEditDialog(Target target) {
		try {
			// Load the fxml file and create a new stage for the popup dialog.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/PolarTargetView.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			// Create the dialog Stage.
			Stage dialogStage = new Stage();
			dialogStage.setTitle("PolaarsihtmĆ¤rgi lisamine");
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

	public boolean isActiveTarget() {
		return isActiveTarget;
	}
	public void SetIsActiveTarget(boolean bool) {
		this.isActiveTarget=bool;
	}

	public void setActiveTarget(boolean isActiveTarget) {
		this.isActiveTarget = isActiveTarget;
	}

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

	public ArrayList<ArrayList<Integer>> getLimiters() {
		return limiters;
	}

	public void setLimiters(ArrayList<ArrayList<Integer>> limiters) {
		this.limiters = limiters;
	}

	

}