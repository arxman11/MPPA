package mppa.model;

import javafx.beans.property.StringProperty;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;

public class Target {
	private final StringProperty targetName;
	private final StringProperty easting;
	private final StringProperty northing;
	private final StringProperty directionAngle;
	private final StringProperty FOeasting;
	private final StringProperty FOnorthing;
	private final StringProperty FOdistance;
	public static final int SQUARE = 1;
	public static final int POLAR = 2;
	public static final int TUK = 3;
	private int tag;
	private ArrayList<ArrayList<ArrayList<Integer>>> targets = new ArrayList<ArrayList<ArrayList<Integer>>>();
	private ArrayList<ArrayList<ArrayList<Integer>>> corrections = new ArrayList<ArrayList<ArrayList<Integer>>>();

	@Override
	public String toString() {
		return targetName.get();
	}

	public void updateName() {
		if (tag == SQUARE) {
			this.targetName.set("RUUT " + this.easting.get() + " " + this.northing.get());
		} else if (tag == POLAR) {
			this.targetName.set("POL MA " + this.FOeasting.get() + " " + this.FOnorthing.get() + " SND "
					+ this.directionAngle.get() + " K " + this.FOdistance.get());
		} else if (tag == TUK) {

			this.targetName.set(
					"TĆ�K " + this.FOeasting.get() + " " + this.FOnorthing.get() + " SND " + this.directionAngle.get());
		}

	}

	public Target() {
		this(null, null, null, null, null, null, null);
	}

	/**
	 * Constructor with some initial data.
	 * 
	 * @param targetname
	 * @param easting
	 * @param northing
	 */
	public Target(String targetname, String easting, String northing, String directionAngle) {
		super();
		this.targetName = new SimpleStringProperty(targetname);
		this.easting = new SimpleStringProperty(easting);
		this.northing = new SimpleStringProperty(northing);
		this.directionAngle = new SimpleStringProperty(directionAngle);
		this.FOeasting = null;
		this.FOnorthing = null;
		this.FOdistance = null;
		this.tag = 1;
		if (easting != null) {
			addTargets(easting, northing);
		}
		

	}

	public Target(String targetname, String easting, String northing, String directionAngle, String FOeasting,
			String FOnorthing, String FOdistance) {
		super();
		this.targetName = new SimpleStringProperty(targetname);
		this.easting = new SimpleStringProperty(easting);
		this.northing = new SimpleStringProperty(northing);
		this.directionAngle = new SimpleStringProperty(directionAngle);
		this.FOeasting = new SimpleStringProperty(FOeasting);
		this.FOnorthing = new SimpleStringProperty(FOnorthing);
		this.FOdistance = new SimpleStringProperty(FOdistance);
		this.tag = 2;
		if (easting != null) {
			addTargets(easting, northing);
			
		}
	}

	public void addTargets(String easting, String northing) {
		ArrayList<ArrayList<Integer>> firstPoints = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < 3; i++) {
			ArrayList<Integer> balPoint = new ArrayList<Integer>();
			balPoint.add(Integer.parseInt(easting));
			balPoint.add(Integer.parseInt(northing));
			balPoint.add(0);// deltaIncrement
			firstPoints.add(balPoint);
		}
		targets.add(firstPoints);
	}

	public void setTag(int newtag) {
		this.tag = newtag;
	}

	public int getTag() {
		return tag;
	}

	public String getTargetname() {
		return targetName.get();
	}

	public void setTargetName(String TargetName) {
		this.targetName.set(TargetName);
	}

	public StringProperty targetNameProperty() {
		return targetName;
	}

	public String getEasting() {
		return easting.get();
	}

	public void setEasting(String Easting) {
		this.easting.set(Easting);
	}

	public StringProperty eastingProperty() {
		return easting;
	}

	public String getNorthing() {
		return northing.get();
	}

	public void setNorthing(String Northing) {
		this.northing.set(Northing);
		addTargets(getEasting(), getNorthing());
	}

	public StringProperty northingProperty() {
		return northing;
	}

	public String getDirectionAngle() {
		return directionAngle.get();
	}

	public void setDirectionAngle(String DirectionAngle) {
		this.directionAngle.set(DirectionAngle);
	}

	public StringProperty directionAngleProperty() {
		return directionAngle;
	}

	public String getFOEasting() {
		return FOeasting.get();
	}

	public void setFOEasting(String Easting) {
		this.FOeasting.set(Easting);
	}

	public StringProperty FOeastingProperty() {
		return FOeasting;
	}

	public String getFONorthing() {
		return FOnorthing.get();
	}

	public void setFONorthing(String Northing) {
		this.FOnorthing.set(Northing);
	}

	public StringProperty FOnorthingProperty() {
		return FOnorthing;
	}

	public String getFOdistance() {
		return FOdistance.get();
	}

	public void setFOdistance(String distance) {
		this.FOdistance.set(distance);
	}

	public StringProperty FOdistanceProperty() {
		return FOdistance;
	}

	public void addCorrection(ArrayList<ArrayList<Integer>> correction) {// To-DO
		corrections.add(correction);
		createNewCorrectionTarget(correction);

	}

	public void deleteLastCorrection() { // TO-DO
		if (corrections.size() > 0) {
			corrections.remove(corrections.size() - 1);
			deleteLastTargetPoints();
		}

	}

	public void clearCorrections() {
		corrections.clear();
	}

	public void deleteLastTargetPoints() {
		if (targets.size() > 1) {
			targets.remove(targets.size() - 1);
		}
	}

	public void createNewCorrectionTarget(ArrayList<ArrayList<Integer>> correction) {
		ArrayList<ArrayList<Integer>> lastTargets = targets.get(targets.size() - 1);
		int direction = correction.get(0).get(0);
		ArrayList<Integer> kild = correction.get(1);
		ArrayList<Integer> suits = correction.get(2);
		ArrayList<Integer> valgus = correction.get(3);
		ArrayList<ArrayList<Integer>> newTargetPoints = new ArrayList<ArrayList<Integer>>();
		newTargetPoints.add(makeCorrection(direction, kild, lastTargets.get(0), 0));
		newTargetPoints.add(makeCorrection(direction, suits, lastTargets.get(1), 1));
		newTargetPoints.add(makeCorrection(direction, valgus, lastTargets.get(2), 2));
		targets.add(newTargetPoints);

	}

	public ArrayList<Integer> makeCorrection(int FOdirection, ArrayList<Integer> correction,
			ArrayList<Integer> targetPoint, Integer type) {
		ArrayList<Integer> returnablePoint = new ArrayList<Integer>();
		int easting = targetPoint.get(0);
		int northing = targetPoint.get(1);
		int deltaEasting = correction.get(0);
		int deltaNorthing = correction.get(1);
		int deltaIncrement = 0;

		if (correction.size() > 2 && type > 0) {
			deltaIncrement = targets.get(targets.size() - 1).get(type).get(2) + correction.get(2);
		}
		double FOangle1 = Math.round(FOdirection / 17.777777777778);
		if (FOdirection < 3200) {
			int targetEasting1 = (int) Math
					.round((easting * 10 + deltaNorthing * 10 * Math.sin(FOangle1 * Math.PI / 180)) / 10);
			int targetNorthing1 = (int) Math
					.round((northing * 10 + deltaNorthing * 10 * Math.cos(FOangle1 * Math.PI / 180)) / 10);
			FOdirection += 1600;
			double FOangle = Math.round(FOdirection / 17.777777777778);
			int targetEasting = (int) Math
					.round((targetEasting1 * 10 + deltaEasting * 10 * Math.sin(FOangle * Math.PI / 180)) / 10);
			int targetNorthing = (int) Math
					.round((targetNorthing1 * 10 + deltaEasting * 10 * Math.cos(FOangle * Math.PI / 180)) / 10);
			returnablePoint.add(targetEasting);
			returnablePoint.add(targetNorthing);
			if (correction.size() > 2) {
				returnablePoint.add(deltaIncrement);
			}

			return returnablePoint;

		} else if (FOdirection >= 3200) {
			int targetEasting1 = (int) Math
					.round((easting * 10 + deltaNorthing * 10 * Math.sin(FOangle1 * Math.PI / 180)) / 10);
			int targetNorthing1 = (int) Math
					.round((northing * 10 + deltaNorthing * 10 * Math.cos(FOangle1 * Math.PI / 180)) / 10);
			FOdirection -= 1600;
			deltaEasting = -1 * deltaEasting;
			double FOangle = Math.round(FOdirection / 17.777777777778);
			int targetEasting = (int) Math
					.round((targetEasting1 * 10 + deltaEasting * 10 * Math.sin(FOangle * Math.PI / 180)) / 10);
			int targetNorthing = (int) Math
					.round((targetNorthing1 * 10 + deltaEasting * 10 * Math.cos(FOangle * Math.PI / 180)) / 10);
			returnablePoint.add(targetEasting);
			returnablePoint.add(targetNorthing);
			if (correction.size() > 2) {
				returnablePoint.add(deltaIncrement);
			}
			return returnablePoint;

		}
		return returnablePoint;

	}

	public ArrayList<ArrayList<ArrayList<Integer>>> getTargets() {
		return targets;
	}

	public ArrayList<ArrayList<Integer>> getDeltas() {
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < 3; i++) {
			ArrayList<Integer> result1 = new ArrayList();
			int originalE = targets.get(0).get(i).get(0);
			int originalN = targets.get(0).get(i).get(1);
			int lastE = targets.get(targets.size() - 1).get(i).get(0);
			int lastN = targets.get(targets.size() - 1).get(i).get(1);
			result1.add(lastE - originalE);
			result1.add(lastN - originalN);
			if(targets.get(targets.size() - 1).get(i).size()>2){
				int lastDeltaIncr = targets.get(targets.size() - 1).get(i).get(2);
				result1.add(lastDeltaIncr);
			}
			result.add(result1);
		}
		return result;
	}

}
