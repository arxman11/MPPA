package mppa.model;

import javafx.beans.property.StringProperty;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;

/**
 * Class for different types of Targets
 *
 * @author Argo Neumann
 */
public class Target {
	/** Target name. */
	private final StringProperty targetName;
	/** Target easting. */
	private final StringProperty easting;
	/** Target northing. */
	private final StringProperty northing;
	/** Direction angle of Forward Observer. */
	private final StringProperty directionAngle;
	/** Easting of Forward Observer. */
	private final StringProperty FOeasting;
	/** Northing of Forward Observer. */
	private final StringProperty FOnorthing;
	/** Distance from Forward Observer to Target in meters. */
	private final StringProperty FOdistance;
	/**
	 * Contains a value used for tagging of square target, value is
	 * {@value #SQUARE}.
	 */
	public static final int SQUARE = 1;
	/**
	 * Contains a value used for tagging of polar target, value is
	 * {@value #POLAR}.
	 */
	public static final int POLAR = 2;
	/**
	 * Contains a value used for tagging of fire transfer target, value is
	 * {@value #TUK}.
	 */
	public static final int TUK = 3;
	/** Whether the target has its name set or not. */
	private boolean isNameSet = false;
	/** Whether the target has Forward Observer or not. */
	private boolean forwardObserver = false;
	/** Whether the target is region target or not. */
	private boolean region = false;
	/** Field for tag. */
	private int tag;
	/** Three-dimensional array for containing target points. */
	private ArrayList<ArrayList<ArrayList<Integer>>> targets = new ArrayList<ArrayList<ArrayList<Integer>>>();
	/** Three-dimensional array for containing corrections. */
	private ArrayList<ArrayList<ArrayList<Integer>>> corrections = new ArrayList<ArrayList<ArrayList<Integer>>>();
	/** Array for region target. */
	private ArrayList<Target> regionTargets=new ArrayList<Target>();
	/**
	 * Returns the target name.<br>
	 * 
	 * @return Target name.
	 */
	@Override
	public String toString() {
		return targetName.get();
	}

	/**
	 * Returns the target information, separated by semicolons for saving.
	 * 
	 * @return Target data, separated by semicolons.
	 */
	public String toData() {
		return targetName.get() + ";" + easting.get() + ";" + northing.get() + ";" + directionAngle.get() + ";"
				+ FOeasting.get() + ";" + FOnorthing.get() + ";" + FOdistance.get() + ";" + tag + ";" + isNameSet + ";"+targets.toString();
	}

	/**
	 * Updates target name. Targets name depends of the tag of the Target.
	 */
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

	/**
	 * Default constructor<br>
	 * Sets all values to null.
	 */
	public Target() {
		this(null, null, null, null, null, null, null);
	}

	/**
	 * Constructor for Target with coordinate-method
	 * 
	 * @param targetname
	 *            Name of the Target.
	 * @param easting
	 *            Easting of the Target.
	 * @param northing
	 *            Northing of the Target.
	 * @param directionAngle
	 *            Direction angle from the Forward Observer.
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

	/**
	 * Constructor for Target with Forward Observer.
	 * 
	 * @param targetname
	 *            Name of the Target.
	 * @param easting
	 *            Easting of the Target.
	 * @param northing
	 *            Northing of the Target.
	 * @param directionAngle
	 *            Direction angle from the Forward Observer.
	 * @param FOeasting
	 *            Easting of the Forward Observer.
	 * @param FOnorthing
	 *            Northing of the Forward Observer.
	 * @param FOdistance
	 *            Distance from the Forward Observer to the Target in meters.
	 */
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
	public void addRegionTargets(String direction){
		int middleEasting = Integer.parseInt(getEasting());
		int middleNorthing = Integer.parseInt(getNorthing());
		double directionAngle = Double.valueOf(direction);
		double angle = Math.round(directionAngle / 17.777777777778);
		int firstEasting = (int) Math
				.round((middleEasting * 10 + 50 * Math.sin(angle * Math.PI / 180)) / 10);
		int firstNorthing = (int) Math
				.round((middleNorthing * 10 + 50 * Math.cos(angle * Math.PI / 180)) / 10);
		Target first=new Target("first",Integer.toString(firstEasting),Integer.toString(firstNorthing),"");
		Target second=new Target("second",getEasting(),getNorthing(),"");
		int thirdEasting = (int) Math
				.round((middleEasting * 10 - 50 * Math.sin(angle * Math.PI / 180)) / 10);
		int thirdNorthing = (int) Math
				.round((middleNorthing * 10 - 50 * Math.cos(angle * Math.PI / 180)) / 10);
		Target third = new Target("third",Integer.toString(thirdEasting),Integer.toString(thirdNorthing),"");
		regionTargets.add(first);
		regionTargets.add(second);
		regionTargets.add(third);
			
	}

	/**
	 * Adds 3 target points as array to {@link #targets targets} array.
	 * 
	 * @param easting
	 *            Easting of the target
	 * @param northing
	 *            Northing of the target
	 */
	public void addTargets(String easting, String northing) {
		ArrayList<ArrayList<Integer>> firstPoints = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < 3; i++) {
			ArrayList<Integer> balPoint = new ArrayList<Integer>();
			balPoint.add(Integer.parseInt(easting));
			balPoint.add(Integer.parseInt(northing));
			if (i > 0) {
				balPoint.add(0);
			} // deltaIncrement
			firstPoints.add(balPoint);
		}
		targets.add(firstPoints);
	}

	/**
	 * Sets Targets tag.
	 * 
	 * @param newTag
	 *            New tag of the Target.
	 */
	public void setTag(int newTag) {
		this.tag = newTag;
	}

	/**
	 * Returns Targets tag.
	 * 
	 * @return Target tag.
	 */
	public int getTag() {
		return tag;
	}

	/**
	 * Returns Target name.
	 * 
	 * @return Target name.
	 */
	public String getTargetname() {
		return targetName.get();
	}

	/**
	 * Sets Target name.
	 * 
	 * @param TargetName
	 *            New name of the Target.
	 */
	public void setTargetName(String TargetName) {
		this.targetName.set(TargetName);
	}

	/**
	 * Returns Target name Property
	 * 
	 * @return Target name Property.
	 */
	public StringProperty targetNameProperty() {
		return targetName;
	}

	/**
	 * Returns Target easting as String.
	 * 
	 * @return Target easting as String..
	 */
	public String getEasting() {
		return easting.get();
	}

	/**
	 * Sets Target easting.
	 * 
	 * @param Easting
	 *            New value for easting.
	 */
	public void setEasting(String Easting) {
		this.easting.set(Easting);
	}

	/**
	 * Returns Target easting Property
	 * 
	 * @return Target easting Property.
	 */
	public StringProperty eastingProperty() {
		return easting;
	}

	/**
	 * Returns Target northing as String.
	 * 
	 * @return Target northing as String.
	 */
	public String getNorthing() {
		return northing.get();
	}

	/**
	 * Sets Target northing and adds target points.
	 * 
	 * @param Northing
	 *            New value for northing.
	 */
	public void setNorthing(String Northing) {
		this.northing.set(Northing);
		addTargets(getEasting(), getNorthing());
	}

	/**
	 * Returns Target northing Property
	 * 
	 * @return Target northing Property.
	 */
	public StringProperty northingProperty() {
		return northing;
	}

	/**
	 * Returns Target direction angle.
	 * 
	 * @return Target direction angle.
	 */
	public String getDirectionAngle() {
		return directionAngle.get();
	}

	/**
	 * Sets Target direction angle.
	 * 
	 * @param DirectionAngle
	 *            New value for direction angle.
	 */
	public void setDirectionAngle(String DirectionAngle) {
		this.directionAngle.set(DirectionAngle);
	}

	/**
	 * Returns Target direction angle Property
	 * 
	 * @return Target direction angle Property.
	 */
	public StringProperty directionAngleProperty() {
		return directionAngle;
	}

	/**
	 * Returns Forward Observer easting as String.
	 * 
	 * @return Forward Observer easting as String.
	 */
	public String getFOEasting() {
		return FOeasting.get();
	}

	/**
	 * Sets Forward Observers easting.
	 * 
	 * @param Easting
	 *            New value for Forward Observer easting.
	 */
	public void setFOEasting(String Easting) {
		this.FOeasting.set(Easting);
	}

	/**
	 * Returns Forward Observer easting Property
	 * 
	 * @return Forward Observer easting Property.
	 */
	public StringProperty FOeastingProperty() {
		return FOeasting;
	}

	/**
	 * Returns Forward Observer northing as String.
	 * 
	 * @return Forward Observer northing as String.
	 */
	public String getFONorthing() {
		return FOnorthing.get();
	}

	/**
	 * Sets Forward Observers northing.
	 * 
	 * @param Northing
	 *            New value for Forward Observer northing.
	 */
	public void setFONorthing(String Northing) {
		this.FOnorthing.set(Northing);
	}

	/**
	 * Returns Forward Observer northing Property
	 * 
	 * @return Forward Observer northing Property.
	 */
	public StringProperty FOnorthingProperty() {
		return FOnorthing;
	}

	/**
	 * Returns distance from Forward Observer to Target as String.
	 * 
	 * @return Distance from Forward Observer to Target as String.
	 */
	public String getFOdistance() {
		return FOdistance.get();
	}

	/**
	 * Sets distance from Forward Observers to Target.
	 * 
	 * @param distance
	 *            Distance from Forward Observer to Target.
	 */
	public void setFOdistance(String distance) {
		this.FOdistance.set(distance);
	}

	/**
	 * Returns Forward Observer distance Property
	 * 
	 * @return Forward Observer distance Property.
	 */
	public StringProperty FOdistanceProperty() {
		return FOdistance;
	}

	/**
	 * Adds two dimensional array of correction to {@link #corrections
	 * corrections} array.<br>
	 * Calls method {@link #createNewCorrectionTarget(ArrayList)
	 * createNewCorrectionTarget}.
	 * 
	 * @param correction
	 *            Two-dimensional array of correction
	 */
	public void addCorrection(ArrayList<ArrayList<Integer>> correction) {
		corrections.add(correction);
		createNewCorrectionTarget(correction);

	}

	/**
	 * Removes last correction from {@link #corrections corrections} array if
	 * array size is larger than 0.<br>
	 * Calls method {@link #deleteLastTargetPoints() deleteLastTargetPoints}.
	 */
	public void deleteLastCorrection() { // TO-DO
		if (corrections.size() > 0) {
			corrections.remove(corrections.size() - 1);
			deleteLastTargetPoints();
		}

	}

	/**
	 * Returns last correction from {@link #corrections corrections} array if
	 * array size is larger than 0.<br>
	 * 
	 * @return Last correction or empty two-dimensional array.
	 */
	public ArrayList<ArrayList<Integer>> getLastCorrection() {
		if (corrections.size() > 0) {
			return corrections.get(corrections.size() - 1);
		} else
			return new ArrayList<ArrayList<Integer>>();
	}

	/**
	 * Clears all corrections from {@link #corrections corrections} array.<br>
	 */
	public void clearCorrections() {
		corrections.clear();
	}

	/**
	 * Removes last target from {@link #targets targets} array if array size is
	 * larger than 1.<br>
	 */
	public void deleteLastTargetPoints() {
		if (targets.size() > 1) {
			targets.remove(targets.size() - 1);
		}
	}

	/**
	 * Adds new target points to {@link #targets targets} array.<br>
	 * 
	 * @param correction
	 *            Two-dimensional array of correction.
	 */
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

	/**
	 * Calculates correction of target and returns new target point.<br>
	 * 
	 * @param FOdirection
	 *            Direction of Forward Observer.
	 * @param correction
	 *            Array of corrections.
	 * @param targetPoint
	 *            Array of last target point.
	 * @param type
	 *            Tag of the target.
	 * @return Array of new target point.
	 */
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

	/**
	 * Returns three-dimensional array {@link #targets targets}.<br>
	 * 
	 * @return Three-dimensional array of {@link #targets targets}.
	 */
	public ArrayList<ArrayList<ArrayList<Integer>>> getTargets() {
		return targets;
	}

	/**
	 * Calculates difference between old target points and new target points
	 * after corrections.<br>
	 * 
	 * @return Two-dimensional array of difference of each target types.
	 */
	public ArrayList<ArrayList<Integer>> getDeltas() {
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < 3; i++) {
			ArrayList<Integer> result1 = new ArrayList<Integer>();
			int originalE = targets.get(0).get(i).get(0);
			int originalN = targets.get(0).get(i).get(1);
			int lastE = targets.get(targets.size() - 1).get(i).get(0);
			int lastN = targets.get(targets.size() - 1).get(i).get(1);
			result1.add(lastE - originalE);
			result1.add(lastN - originalN);
			if (targets.get(targets.size() - 1).get(i).size() > 2) {
				int lastDeltaIncr = targets.get(targets.size() - 1).get(i).get(2);
				result1.add(lastDeltaIncr);
			}
			result.add(result1);
		}
		return result;
	}

	/**
	 * Returns if Target name has been set.<br>
	 * 
	 * @return True if Target name has set, false if it´s not.
	 */
	public boolean isNameSet() {
		return isNameSet;
	}

	/**
	 * Set if Target name has been set.<br>
	 * 
	 * @param isNameSet
	 *            Boolean whether the Target name is set or not.
	 */
	public void setNameSet(boolean isNameSet) {
		this.isNameSet = isNameSet;
	}

	/**
	 * Returns if Target has Forward Observer.<br>
	 * 
	 * @return True if Target has Forward Observer, false if not.
	 */
	public boolean hasForwardObserver() {
		return forwardObserver;
	}

	/**
	 * Set if Target has Forward Observer.<br>
	 * 
	 * @param forwardObserver
	 *            Boolean whether the Target has Forward Observer or not.
	 */
	public void setForwardObserver(boolean forwardObserver) {
		this.forwardObserver = forwardObserver;
	}
	/**
	 * Returns if Target is region (MAA-ALA) target.<br>
	 * 
	 * @return True if Target is region target, false if not.
	 */
	public boolean isRegion() {
		return region;
	}
	/**
	 * Set if Target is region target.<br>
	 * 
	 * @param isRegion Boolean whether the Target is region target or not.
	 */

	public void setRegion(boolean isRegion) {
		this.region = isRegion;
	}
	/**
	 * Returns array of three targets. .<br>
	 * 
	 * @return Array of three targets.
	 */
	public ArrayList<Target> getRegionTargets() {
		return regionTargets;
	}
	/**
	 * Sets parameter to targets region targets {@link #regionTargets array}.<br>
	 * 
	 * @param regionTargets Array of three targets.
	 */
	public void setRegionTargets(ArrayList<Target> regionTargets) {
		this.regionTargets = regionTargets;
	}

}
