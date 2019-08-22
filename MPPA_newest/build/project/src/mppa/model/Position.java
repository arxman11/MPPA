package mppa.model;

import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;

public class Position {
	private final StringProperty positionName;
    private final StringProperty easting;
    private final StringProperty northing;
    private final StringProperty mainDirection;
    private final StringProperty backupDirection;
    private final boolean isCentre;
    private ObservableList<ArrayList<ArrayList<ArrayList<Double>>>> shootingData = FXCollections.observableArrayList();
    
    
    
	@Override
	public String toString() {
		return "Position [positionName=" + positionName + ", easting=" + easting + ", northing=" + northing
				+ ", mainDirection=" + mainDirection + ", backupDirection=" + backupDirection + ", isCentre=" + isCentre + "]";
	}

	public Position(){
    	this(null,null,null,null,null,false);
    }
	public Position(String positionName){
		this.positionName=new SimpleStringProperty(positionName);
		this.easting = null;
		this.northing = null;
		this.mainDirection = null;
		this.backupDirection= null;
		this.isCentre= false;
    }
    
	/**
     * Constructor with some initial data.
     * 
     * @param positionname
     * @param easting
     * @param northing
     */
	public Position(String positionname, String easting, String northing, String mainDirection,boolean isCentre) {
		super();
		this.positionName = new SimpleStringProperty(positionname);
		this.easting = new SimpleStringProperty(easting);
		this.northing = new SimpleStringProperty(northing);
		this.mainDirection = new SimpleStringProperty(mainDirection);
		this.backupDirection= null;
		this.isCentre=isCentre;
	}
	
	public Position(String positionname, String easting, String northing, String mainDirection, String backupDirection,boolean isCentre) {
		super();
		this.positionName = new SimpleStringProperty(positionname);
		this.easting = new SimpleStringProperty(easting);
		this.northing = new SimpleStringProperty(northing);
		this.mainDirection = new SimpleStringProperty(mainDirection);
		this.backupDirection = new SimpleStringProperty(backupDirection);
		this.isCentre=isCentre;
	}
	
	
	public StringProperty backupDirectionProperty() {
		return backupDirection;
	}
	public String getBackupDirection(){
		return backupDirection.get();
	}
	public void setBackUpDirection(String backUpDirection) {
        this.backupDirection.set(backUpDirection);
	}

	public boolean isCentre() {
		return isCentre;
	}

	public String getpositionname() {
		return positionName.get();
	}
	public void setpositionName(String positionName) {
        this.positionName.set(positionName);
	}
	public StringProperty positionNameProperty() {
        return positionName;
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
	}
	public StringProperty northingProperty() {
        return northing;
    }
	
	public String getMainDirection() {
		return mainDirection.get();
	}
	public void setMainDirection(String mainDirection) {
        this.mainDirection.set(mainDirection);
	}
	public StringProperty mainDirectionProperty() {
        return mainDirection;
    }
	public ObservableList<ArrayList<ArrayList<ArrayList<Double>>>> getShootingData() {
		return this.shootingData;
	}
	public void setShootingData(ObservableList<ArrayList<ArrayList<ArrayList<Double>>>> data) {
		this.shootingData=data;
	}
	
	public void clearShootingData(){
		this.shootingData.clear();
	}

}
