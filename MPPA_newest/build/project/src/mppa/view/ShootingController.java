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

public class ShootingController {
	private MainApp mainApp;
	private ObservableList<ArrayList<ArrayList<ArrayList<Double>>>> shootingData1 = FXCollections.observableArrayList();
	private ObservableList<ArrayList<ArrayList<ArrayList<Double>>>> shootingData2 = FXCollections.observableArrayList();
	private ObservableList<ArrayList<ArrayList<ArrayList<Double>>>> shootingData3 = FXCollections.observableArrayList();

	@FXML
	private ComboBox<String> firstK;
	@FXML
	private ComboBox<String> firstS;
	@FXML
	private ComboBox<String> firstV;
	@FXML
	private ComboBox<String> secondK;
	@FXML
	private ComboBox<String> secondS;
	@FXML
	private ComboBox<String> secondV;
	@FXML
	private ComboBox<String> thirdK;
	@FXML
	private ComboBox<String> thirdS;
	@FXML
	private ComboBox<String> thirdV;

	public ShootingController() {
	}

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

	public void fillData() {
		ObservableList<Position> positions = mainApp.getPositionData();
		this.shootingData1 = positions.get(0).getShootingData();
		this.shootingData2 = positions.get(1).getShootingData();
		this.shootingData3 = positions.get(2).getShootingData();

	}

	public void fillBoxes() {
		if (mainApp.isActiveTarget() == true) {
			if (!shootingData1.isEmpty() && shootingData1.size() > 1) {
				ArrayList<Double> disAndDir1K = shootingData1.get(0).get(0).get(0);
				Double dis1K = disAndDir1K.get(0);
				Double dir1K = disAndDir1K.get(1);
				ObservableList<ArrayList<Double>> firstKList = FXCollections
						.observableArrayList(shootingData1.get(1).get(0));
				ObservableList<String> res1K = toString(firstKList, dis1K, dir1K, 0);
				firstK.setItems(res1K); // Lisab terve listi elementideks
				firstK.getSelectionModel().select(res1K.get(0)); // Valib
																	// vähima
																	// lisalaengu
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
				secondK.setItems(res2K); // Lisab terve listi elementideks
				secondK.getSelectionModel().select(res2K.get(0)); // Valib
																	// vähima
																	// lisalaengu
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
				thirdK.setItems(res3K); // Lisab terve listi elementideks
				thirdK.getSelectionModel().select(res3K.get(0)); // Valib
																	// vähima
																	// lisalaengu
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

	public ObservableList<String> toString(ObservableList<ArrayList<Double>> text, Double distance, Double direction,
			int type) {
		ArrayList<String> endResult = new ArrayList();
		ObservableList<String> Result = FXCollections.observableArrayList(endResult);
		if (text.isEmpty()) {
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
					if (type == 0) {
						info += i.get(0).intValue() + "; " + dir.substring(0, 2) + "-" + dir.substring(2, 4) + "; "
								+ i.get(1).intValue() + "; " + i.get(2) + "s; " + distance.intValue() + "m";
						Result.add(info);
					} else {
						info += i.get(0).intValue() + "; " + dir.substring(0, 2) + "-" + dir.substring(2, 4) + "; "
								+ i.get(1).intValue() + "; " +applyDeltaIncrement(i.get(2),type,i.get(0).intValue()) + "s; " + distance.intValue() + "m";
						Result.add(info);
					}
					//applyDeltaIncrement(i.get(2),type,i.get(0).intValue())

				} else {
					Result.add("Liiga kaugel");
				}
			}
		}

		return Result;

	}

	public Double applyDeltaIncrement(Double timeInput, int type, int charge) {// TO-DO
		if (type == 1) {// SUITS
			int deltaIncrement = mainApp.getActiveTarget().getDeltas().get(type).get(2);
			int times = Math.round(deltaIncrement / 50);
			if(deltaIncrement<50){
				times=1;
			}
			switch (charge) {
			case 1:case 0:
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
		if (type == 2) {// VALGUS
			int deltaIncrement = mainApp.getActiveTarget().getDeltas().get(type).get(2);
			int times = Math.round(deltaIncrement / 50);
			if(deltaIncrement<50){
				times=1;
			}
			switch (charge) {
			case 1:case 0:
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
		return round(timeInput,1);
	}
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}

	@FXML
	private void initialize() {

	}

}
