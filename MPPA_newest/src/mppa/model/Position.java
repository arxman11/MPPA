package mppa.model;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
/**
 * Position class
 *
 * @author Argo Neumann
 */
public class Position {
	/** Position name. */
	private final StringProperty positionName;
	/** Position easting. */
    private final StringProperty easting;
    /** Position northing. */
    private final StringProperty northing;
    /** Position´s main direction. */
    private final StringProperty mainDirection;
    /** Position´s backup direction */
    private final StringProperty backupDirection;
    /** True if position is in the center of two other positions. Typically True if Positions name is 2.*/
    private final boolean isCentre;
    /** Three-dimensional array containing shooting data for each target.*/
    private ObservableList<ArrayList<ArrayList<ArrayList<Double>>>> shootingData = FXCollections.observableArrayList();
    
    
    /**
     * Returns the position name.<br>
     * @return Position name.
     */
	@Override
	public String toString() {
		return "Position [positionName=" + positionName + ", easting=" + easting + ", northing=" + northing
				+ ", mainDirection=" + mainDirection + ", backupDirection=" + backupDirection + ", isCentre=" + isCentre + "]";
	}
	/**
     * Returns the position information, separated by semicolons for saving table information.
     * @return Position data, separated by semicolons.
     */
	public String toData(){
		return positionName.get() + ";" + easting.get()  + ";" + northing.get()  + ";" + mainDirection.get() + ";" + backupDirection.get()  + ";" + isCentre ;
	}
	/**
     * Default constructor<br>
     * Sets all values to null.
     */
	public Position(){
    	this(null,null,null,null,null,false);
    }
	/**
	 * Constructor for position with initial data.
	 * 
	 * @param positionName Name of the position.
	 */
	public Position(String positionName){
		this.positionName=new SimpleStringProperty(positionName);
		this.easting = null;
		this.northing = null;
		this.mainDirection = null;
		this.backupDirection= null;
		this.isCentre= false;
    }
    
	/**
	 * Constructor for position with coordinates and without backup direction.
	 * 
	 * @param positionName Name of the position.
	 * @param easting Easting of the position.
	 * @param northing Northing of the position.
	 * @param mainDirection Main direction of the position.
	 * @param isCentre True if position is in the middle, false if it is not.
	 */
	public Position(String positionName, String easting, String northing, String mainDirection,boolean isCentre) {
		super();
		this.positionName = new SimpleStringProperty(positionName);
		this.easting = new SimpleStringProperty(easting);
		this.northing = new SimpleStringProperty(northing);
		this.mainDirection = new SimpleStringProperty(mainDirection);
		this.backupDirection= null;
		this.isCentre=isCentre;
	}
	/**
	 * Constructor for position with coordinates and without backup direction.
	 * 
	 * @param positionName Name of the position.
	 * @param easting Easting of the position.
	 * @param northing Northing of the position.
	 * @param mainDirection Main direction of the position.
	 * @param backupDirection Backup direction of the position.
	 * @param isCentre True if position is in the middle, false if it is not.
	 */
	
	public Position(String positionName, String easting, String northing, String mainDirection, String backupDirection,boolean isCentre) {
		super();
		this.positionName = new SimpleStringProperty(positionName);
		this.easting = new SimpleStringProperty(easting);
		this.northing = new SimpleStringProperty(northing);
		this.mainDirection = new SimpleStringProperty(mainDirection);
		this.backupDirection = new SimpleStringProperty(backupDirection);
		this.isCentre=isCentre;
	}
	
	
	public StringProperty backupDirectionProperty() {
		return backupDirection;
	}
	/**
	 * Returns position´s backup direction.
	 * 
	 * @return Position´s backup direction.
	 */
	public String getBackupDirection(){
		return backupDirection.get();
	}
	/**
	 * Sets position´s backup direction.
	 * 
	 * @param backupDirection New value for backup direction.
	 */
	public void setBackUpDirection(String backupDirection) {
        this.backupDirection.set(backupDirection);
	}
	/**
	 * Returns whether the position is in the middle of two other positions or not.
	 * 
	 * @return True if position is in the middle, false if it is not.
	 */
	public boolean isCentre() {
		return isCentre;
	}
	/**
	 * Returns position´s name.
	 * 
	 * @return Position´s name.
	 */
	public String getpositionname() {
		return positionName.get();
	}
	/**
	 * Sets position´s name.
	 * 
	 * @param positionName Position´s name.
	 */
	public void setpositionName(String positionName) {
        this.positionName.set(positionName);
	}
	/**
	 * Returns position´s name Property
	 * 
	 * @return Position´s name Property.
	 */
	public StringProperty positionNameProperty() {
        return positionName;
    }
	
	/**
	 * Returns position easting as String.
	 * 
	 * @return Position easting as String.
	 */
	public String getEasting() {
		return easting.get();
	}
	/**
	 * Sets position easting.
	 * 
	 * @param easting New value for easting.
	 */
	public void setEasting(String easting) {
        this.easting.set(easting);
	}
	/**
	 * Returns position easting Property
	 * 
	 * @return Position easting Property.
	 */
	public StringProperty eastingProperty() {
        return easting;
    }

	/**
	 * Returns position northing as String.
	 * 
	 * @return Position northing as String.
	 */
	public String getNorthing() {
		return northing.get();
	}
	/**
	 * Sets position northing.
	 * 
	 * @param northing New value for northing.
	 */
	public void setNorthing(String northing) {
        this.northing.set(northing);
	}
	/**
	 * Returns position northing Property.
	 * 
	 * @return Position northing Property.
	 */
	public StringProperty northingProperty() {
        return northing;
    }
	/**
	 * Returns position´s main direction.
	 * 
	 * @return Position´s main direction.
	 */
	public String getMainDirection() {
		return mainDirection.get();
	}
	/**
	 * Sets position´s main direction.
	 * 
	 * @param mainDirection New value for main direction.
	 */
	public void setMainDirection(String mainDirection) {
        this.mainDirection.set(mainDirection);
	}
	/**
	 * Returns position´s main direction Property
	 * 
	 * @return Position´s main direction Property.
	 */
	public StringProperty mainDirectionProperty() {
        return mainDirection;
    }
	/**
	 * Returns position´s shooting data {@link #shootingData array}.
	 * 
	 * @return Position´s shooting data {@link #shootingData array}.
	 */
	public ObservableList<ArrayList<ArrayList<ArrayList<Double>>>> getShootingData() {
		return this.shootingData;
	}
	/**
	 * Sets position´s shooting data {@link #shootingData array}.
	 * 
	 * @param data Sets position´s shooting data {@link #shootingData array} to given array.
	 */
	public void setShootingData(ObservableList<ArrayList<ArrayList<ArrayList<Double>>>> data) {
		this.shootingData=data;
	}
	/**
	 * Clears all elements from shooting data {@link #shootingData array}.
	 */
	public void clearShootingData(){
		this.shootingData.clear();
	}

}
