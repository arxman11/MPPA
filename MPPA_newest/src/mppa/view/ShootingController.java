package mppa.view;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import mppa.MainApp;
import mppa.model.Position;

/**
 * Class for controlling shooting data and displaying it.
 * 
 * @author Argo Neumann
 */
public class ShootingController {
	/** Main Application. */
	private MainApp mainApp;
	/** First position´s shooting data. */
	private ObservableList<ArrayList<ArrayList<ArrayList<Double>>>> shootingData1 = FXCollections.observableArrayList();
	/** Second position´s shooting data. */
	private ObservableList<ArrayList<ArrayList<ArrayList<Double>>>> shootingData2 = FXCollections.observableArrayList();
	/** Third position´s shooting data. */
	private ObservableList<ArrayList<ArrayList<ArrayList<Double>>>> shootingData3 = FXCollections.observableArrayList();
	/** First position´s fragment mine shooting data. */
	@FXML
	private ComboBox<String> firstK;
	/** First position´s smoke mine shooting data. */
	@FXML
	private ComboBox<String> firstS;
	/** First position´s lightning mine shooting data. */
	@FXML
	private ComboBox<String> firstV;
	/** Second position´s fragment mine shooting data. */
	@FXML
	private ComboBox<String> secondK;
	/** Second position´s smoke mine shooting data. */
	@FXML
	private ComboBox<String> secondS;
	/** Second position´s lightning mine shooting data. */
	@FXML
	private ComboBox<String> secondV;
	/** Third position´s fragment mine shooting data. */
	@FXML
	private ComboBox<String> thirdK;
	/** Third position´s smoke mine shooting data. */
	@FXML
	private ComboBox<String> thirdS;
	/** Third position´s lightning mine shooting data. */
	@FXML
	private ComboBox<String> thirdV;

	/** Default constructor. */
	public ShootingController() {
	}

	/**
	 * Is called by the main application to give a reference back to itself.
	 * Set´s class main application . Calls out {@link #fillData() fillData()}
	 * method to get every positions shooting data. Adds listener to every
	 * positions shooting data. When change is detected, updates all shooting
	 * data.
	 * 
	 * @param mainApp
	 *            MainApp .
	 */
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		fillData();
		shootingData1.addListener(new ListChangeListener<ArrayList<ArrayList<ArrayList<Double>>>>() {
			@Override
			public void onChanged(ListChangeListener.Change c) {
				fillData();
				fillBoxes();
			}

		});
		shootingData2.addListener(new ListChangeListener<ArrayList<ArrayList<ArrayList<Double>>>>() {
			@Override
			public void onChanged(ListChangeListener.Change c) {
				fillData();
				fillBoxes();
			}

		});
		shootingData3.addListener(new ListChangeListener<ArrayList<ArrayList<ArrayList<Double>>>>() {
			@Override
			public void onChanged(ListChangeListener.Change c) {
				fillData();
				fillBoxes();
			}

		});

	}

	/**
	 * Gets main applications positions and applies each position´s shooting
	 * data.
	 */
	public void fillData() {
		ObservableList<Position> positions = mainApp.getPositionData();
		this.shootingData1 = positions.get(0).getShootingData();
		this.shootingData2 = positions.get(1).getShootingData();
		this.shootingData3 = positions.get(2).getShootingData();
	}

	/**
	 * If there is an active target, sets data to ComboBoxes. Selects lowest
	 * charge and displays that information.
	 */
	public void fillBoxes() {
		if (mainApp.isActiveTarget() == true) {
			if (!shootingData1.isEmpty() && shootingData1.size() > 1) {
				ArrayList<Double> disAndDir1K = shootingData1.get(0).get(0).get(0);
				Double dis1K = disAndDir1K.get(0);
				Double dir1K = disAndDir1K.get(1);
				ObservableList<ArrayList<Double>> firstKList = FXCollections
						.observableArrayList(shootingData1.get(1).get(0));
				ObservableList<String> res1K = toString(firstKList, dis1K, dir1K, 0);
				firstK.setItems(res1K);
				firstK.getSelectionModel().select(res1K.get(0)); // Selects
																	// lowest
																	// charge
				ArrayList<Double> disAndDir1S = shootingData1.get(0).get(0).get(1);
				Double dis1S = disAndDir1S.get(0);
				Double dir1S = disAndDir1S.get(1);
				ObservableList<ArrayList<Double>> firstSList = FXCollections
						.observableArrayList(shootingData1.get(1).get(1));
				ObservableList<String> res1S = toString(firstSList, dis1S, dir1S, 1);
				firstS.setItems(res1S);
				firstS.getSelectionModel().select(res1S.get(0));
				ArrayList<Double> disAndDir1V = shootingData1.get(0).get(0).get(2);
				Double dis1V = disAndDir1V.get(0);
				Double dir1V = disAndDir1V.get(1);
				ObservableList<ArrayList<Double>> firstVList = FXCollections
						.observableArrayList(shootingData1.get(1).get(2));
				ObservableList<String> res1V = toString(firstVList, dis1V, dir1V, 2);
				firstV.setItems(res1V);
				firstV.getSelectionModel().select(res1V.get(0));
			}
			if (!shootingData2.isEmpty() && shootingData2.size() > 1) {
				ArrayList<Double> disAndDirK = shootingData2.get(0).get(0).get(0);
				Double disK = disAndDirK.get(0);
				Double dirK = disAndDirK.get(1);
				ObservableList<ArrayList<Double>> secondKList = FXCollections
						.observableArrayList(shootingData2.get(1).get(0));
				ObservableList<String> res2K = toString(secondKList, disK, dirK, 0);
				secondK.setItems(res2K);
				secondK.getSelectionModel().select(res2K.get(0));
				ArrayList<Double> disAndDirS = shootingData2.get(0).get(0).get(1);
				Double disS = disAndDirS.get(0);
				Double dirS = disAndDirS.get(1);
				ObservableList<ArrayList<Double>> secondSList = FXCollections
						.observableArrayList(shootingData2.get(1).get(1));
				ObservableList<String> res2S = toString(secondSList, disS, dirS, 1);
				secondS.setItems(res2S);
				secondS.getSelectionModel().select(res2S.get(0));
				ArrayList<Double> disAndDirV = shootingData2.get(0).get(0).get(2);
				Double disV = disAndDirV.get(0);
				Double dirV = disAndDirV.get(1);
				ObservableList<ArrayList<Double>> secondVList = FXCollections
						.observableArrayList(shootingData2.get(1).get(2));
				ObservableList<String> res2V = toString(secondVList, disV, dirV, 2);
				secondV.setItems(res2V);
				secondV.getSelectionModel().select(res2V.get(0));
			}
			if (!shootingData3.isEmpty() && shootingData3.size() > 1) {
				ArrayList<Double> disAndDirK = shootingData3.get(0).get(0).get(0);
				Double disK = disAndDirK.get(0);
				Double dirK = disAndDirK.get(1);
				ObservableList<ArrayList<Double>> thirdKList = FXCollections
						.observableArrayList(shootingData3.get(1).get(0));
				ObservableList<String> res3K = toString(thirdKList, disK, dirK, 0);
				thirdK.setItems(res3K);
				thirdK.getSelectionModel().select(res3K.get(0));
				ArrayList<Double> disAndDirS = shootingData3.get(0).get(0).get(1);
				Double disS = disAndDirS.get(0);
				Double dirS = disAndDirS.get(1);
				ObservableList<ArrayList<Double>> thirdSList = FXCollections
						.observableArrayList(shootingData3.get(1).get(1));
				ObservableList<String> res3S = toString(thirdSList, disS, dirS, 1);
				thirdS.setItems(res3S);
				thirdS.getSelectionModel().select(res3S.get(0));
				ArrayList<Double> disAndDirV = shootingData3.get(0).get(0).get(2);
				Double disV = disAndDirV.get(0);
				Double dirV = disAndDirV.get(1);
				ObservableList<ArrayList<Double>> thirdVList = FXCollections
						.observableArrayList(shootingData3.get(1).get(2));
				ObservableList<String> res3V = toString(thirdVList, disV, dirV, 2);
				thirdV.setItems(res3V);
				thirdV.getSelectionModel().select(res3V.get(0));

			}

		}
	}

	/**
	 * Converts all shooting data of on positions one ammo type to
	 * ObservableList string. Applies time lift/fall increment if correction is
	 * made with {@link #applyDeltaIncrement(Double, int, int)
	 * applyDeltaIncrement(Double, int, int)} method.
	 * 
	 * @param text
	 *            Input shooting data
	 * @param distance
	 *            Distance
	 * @param direction
	 *            Direction
	 * @param type
	 *            Type of ammo
	 * @return ObservableList of strings about shooting data
	 */
	public ObservableList<String> toString(ObservableList<ArrayList<Double>> text, Double distance, Double direction,
			int type) {
		ArrayList<String> endResult = new ArrayList<String>();
		ObservableList<String> Result = FXCollections.observableArrayList(endResult);
		if (text.isEmpty()) {// No shooting data means that the distance is too
								// far.
			Result.add("Liiga kaugel");
		} else {
			for (ArrayList<Double> i : text) {
				if (!i.isEmpty()) {
					String info = "";
					String dir;
					dir = Integer.toString(direction.intValue());
					if (dir.length() == 1) {
						dir = "0000";
					} else if (dir.length() == 1) {
						dir = new StringBuilder(dir).insert(0, "000").toString();
					} else if (dir.length() == 2) {
						dir = new StringBuilder(dir).insert(0, "00").toString();
					} else if (dir.length() == 3) {
						dir = new StringBuilder(dir).insert(0, "0").toString();
					}
					if (type == 0) {// Fragment mine can´t have lift/fall
									// correction
						info += i.get(0).intValue() + "; " + dir.substring(0, 2) + "-" + dir.substring(2, 4) + "; "
								+ i.get(1).intValue() + "; " + i.get(2) + "s; " + distance.intValue() + "m";
						Result.add(info);
					} else {// Lift/fall correction for smoke and lightning mine
						info += i.get(0).intValue() + "; " + dir.substring(0, 2) + "-" + dir.substring(2, 4) + "; "
								+ i.get(1).intValue() + "; " + applyDeltaIncrement(i.get(2), type, i.get(0).intValue())
								+ "s; " + distance.intValue() + "m";
						Result.add(info);
					}
				} else {
					Result.add("Liiga kaugel");
				}
			}
		}

		return Result;

	}

	/**
	 * Calculates new flight time, based on corrections made to lift/fall. If no
	 * correction is made, returns original time of flight.
	 * 
	 * @param timeInput
	 *            Time of flight
	 * @param type
	 *            Type of ammo
	 * @param charge
	 *            Charge number
	 * @return Time of flight, rounded to one decimal point.
	 */
	public Double applyDeltaIncrement(Double timeInput, int type, int charge) {// TO-DO
		if (type == 1) {// Smoke mine
			int deltaIncrement = mainApp.getActiveTarget().getDeltas().get(type).get(2);
			int times = Math.round(deltaIncrement / 50);
			if (deltaIncrement == 0) {
				return timeInput;
			}
			if (deltaIncrement < 50) {
				times = 1;
			}
			switch (charge) {
			case 1:
			case 0:
				timeInput -= (0.7 * times);
				break;
			case 2:
				timeInput -= (0.5 * times);
				break;
			case 3:
			case 4:
				timeInput -= (0.4 * times);
				break;
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
				timeInput -= (0.3 * times);
				break;
			}

		}
		if (type == 2) {// Lightning mine
			int deltaIncrement = mainApp.getActiveTarget().getDeltas().get(type).get(2);
			int times = Math.round(deltaIncrement / 50);
			if (deltaIncrement == 0) {
				return timeInput;
			}
			if (deltaIncrement < 50) {
				times = 1;
			}
			switch (charge) {
			case 1:
			case 0:
				timeInput -= (0.7 * times);
				break;
			case 2:
				timeInput -= (0.5 * times);
				break;
			case 3:
				timeInput -= (0.4 * times);
				break;
			case 4:
			case 5:
			case 6:
				timeInput -= (0.3 * times);
				break;
			case 7:
			case 8:
			case 9:
				timeInput -= (0.2 * times);
				break;
			}

		}
		return round(timeInput, 1);
	}

	/**
	 * Returns rounded value of flight time, rounded up.
	 * 
	 * @param value
	 *            Time of flight
	 * @param places
	 *            Number of decimal points
	 * @return Time of flight, rounded to one decimal point.
	 */
	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	/**
	 * Initializes the controller class. This method is automatically called
	 * after the fxml file has been loaded.
	 */
	@FXML
	private void initialize() {

	}

}
