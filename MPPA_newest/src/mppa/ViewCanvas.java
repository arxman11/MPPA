package mppa;

import java.util.ArrayList;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * Class for drawing graphical content and controlling mouse movements on the
 * graphics.
 * 
 * @author Argo Neumann
 */
public class ViewCanvas extends Pane {
	/** Canvas. */
	private Canvas grid;
	/** Graphics Context. */
	private GraphicsContext gc;
	/** Scale. */
	DoubleProperty myScale = new SimpleDoubleProperty(1.01);
	/** Center line for x-axis */
	private int centerX;
	/** Center line for y-axis */
	private int centerY;
	/** Boolean for displaying target in grid center. Default is false. */
	private boolean isCenterSet=false;

	/** Default constructor. */
	public ViewCanvas() {
		setPrefSize(3200, 3200);
		// add scale transform
		scaleXProperty().bind(myScale);
		scaleYProperty().bind(myScale);
	}

	/** Removes all the elements from pane. Redraws only grid. */
	public void clearCanvas() {
		getChildren().clear();
		addGrid();
	}

	/**
	 * Set´s center lines for correct position display.
	 * 
	 * @param easting
	 *            Easting.
	 * @param northing
	 *            Northing.
	 */
	public void setCenterLines(String easting, String northing) {
		centerX = Math.round(Integer.parseInt(easting) / 100) * 100 - 1600;
		centerY = Math.round(Integer.parseInt(northing) / 100) * 100 - 1600;
	}

	/**
	 * Calculates new coordinates from MGRS system to canvas coordinates.
	 * Parameters are given as strings.
	 * 
	 * @param easting
	 *            Easting.
	 * @param northing
	 *            Northing.
	 * @return Array of string, containing easting and northing.
	 */
	public ArrayList<String> fromCordToCanv(String easting, String northing) {
		ArrayList<String> result = new ArrayList<String>();
		int posX = Integer.parseInt(easting);
		int posY = Integer.parseInt(northing);
		if (Math.abs(centerX - posX) > 5000) {
			if (centerX > posX) {
				posX = 10000 + posX;
			} else {
				posX = -(10000 - posX);
			}
		}
		if (Math.abs(centerY - posY) > 5000) {
			if (centerY > posY) {
				posY = 10000 + posY;
			} else {
				posY = -(10000 - posY);
			}
		}
		posX = posX - centerX;
		posY = posY - centerY;
		int changeY = 1600 - posY;
		posY = changeY + 1600;
		result.add(Integer.toString(posX));
		result.add(Integer.toString(posY));
		return result;

	}

	/**
	 * Calculates new coordinates from MGRS system to canvas coordinates.
	 * Parameters are given as integers.
	 * 
	 * @param easting
	 *            Easting.
	 * @param northing
	 *            Northing.
	 * @return Array of integer, containing easting and northing.
	 */
	public ArrayList<Integer> fromCordToCanv(int easting, int northing) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		int posX = easting;
		int posY = northing;
		if (Math.abs(centerX - posX) > 5000) {
			if (centerX > posX) {
				posX = 10000 + posX;
			} else {
				posX = -(10000 - posX);
			}
		}
		if (Math.abs(centerY - posY) > 5000) {
			if (centerY > posY) {
				posY = 10000 + posY;
			} else {
				posY = -(10000 - posY);
			}
		}
		posX = posX - centerX;
		posY = posY - centerY;
		int changeY = 1600 - posY;
		posY = changeY + 1600;
		result.add(posX);
		result.add(posY);
		return result;

	}

	/** Removes limiter from the graphics. */
	public void deleteLimiter() {
		for (Node o : getChildren()) {
			if (o.getUserData() == "limiter") {
				getChildren().remove(o);
				break;
			}

		}
	}

	/**
	 * Draws ballistic point.
	 * 
	 * @param easting
	 *            Easting.
	 * @param northing
	 *            Northing.
	 * @param type
	 *            Type of ammo.
	 * @param name
	 *            Name.
	 */
	public void drawBallisticPoint(String easting, String northing, String type, String name) {
		Group bal = new Group();
		ArrayList<String> positions = fromCordToCanv(easting, northing);
		int balX = Integer.parseInt(positions.get(0));
		int balY = Integer.parseInt(positions.get(1));
		Line one = new Line(balX, balY - 6, balX, balY - 9);
		Line two = new Line(balX, balY + 6, balX, balY + 9);
		Line three = new Line(balX - 6, balY, balX - 9, balY);
		Line four = new Line(balX + 6, balY, balX + 9, balY);
		Ellipse five = new Ellipse(balX, balY, 6, 6);
		five.setStrokeWidth(1);
		five.setFill(Color.TRANSPARENT);
		five.setStroke(Color.BLACK);
		Circle point = new Circle(balX, balY, 2);
		point.setFill(Color.BLACK);
		Text tekst = new Text(type);
		tekst.setLayoutX(balX + 3);
		tekst.setLayoutY(balY - 6);
		tekst.setFont(Font.font("Verdana", 8));
		bal.getChildren().addAll(one, two, three, four, five, point, tekst);
		bal.setUserData(type + name);
		getChildren().add(bal);
	}

	/**
	 * Removes ballistic point from panes children.
	 * 
	 * @param name
	 *            Name of ballistic point.
	 */
	public void deleteBallisticPoint(String name) {
		for (Node o : getChildren()) {
			if (o.getUserData() != null && o.getUserData().equals((String) name)) {
				getChildren().remove(o);
				break;
			}

		}
	}

	/**
	 * Draws forward observer.
	 * 
	 * @param easting
	 *            Easting.
	 * @param northing
	 *            Northing.
	 * @param name
	 *            Name of target.
	 */
	public void drawForwardObserver(String easting, String northing, String name) {
		Group fo = new Group();
		ArrayList<String> positions = fromCordToCanv(easting, northing);
		int foX = Integer.parseInt(positions.get(0));
		int foY = Integer.parseInt(positions.get(1));
		Line one = new Line(foX, foY - 10, foX, foY - 60);
		Line two = new Line(foX, foY - 10, foX + 10, foY + 9);
		Line three = new Line(foX, foY - 10, foX - 10, foY + 9);
		Line four = new Line(foX - 10, foY + 9, foX + 10, foY + 9);
		Line five = new Line(foX, foY - 60, foX - 9, foY - 50);
		Line six = new Line(foX, foY - 60, foX + 9, foY - 50);
		fo.getChildren().addAll(one, two, three, four, five, six);
		for (Node i : fo.getChildren()) {
			((Line) i).setStrokeWidth(1.5);
		}
		Circle point = new Circle(foX, foY, 2);
		point.setFill(Color.BLACK);
		fo.getChildren().addAll(point);
		fo.setUserData(name);
		getChildren().add(fo);

	}

	/**
	 * Removes target´s forward observer from pane .
	 * 
	 * @param name
	 *            Target name which has forward observer.
	 */
	public void deleteForwardObserver(String name) {
		for (Node o : getChildren()) {
			if (o.getUserData() != null && o.getUserData().equals((String) name)) {
				getChildren().remove(o);
				break;
			}

		}
	}

	/**
	 * Draws position without sector.
	 * 
	 * @param easting
	 *            Easting.
	 * @param northing
	 *            Northing.
	 * @param posNr
	 *            Number of position.
	 * @param name
	 *            Name of position.
	 */
	public void drawPosition(String easting, String northing, int posNr, String name) {
		isCenterSet=true;
		Group pos = new Group();
		pos.setUserData(name);
		ArrayList<String> positions = fromCordToCanv(easting, northing);
		int posX = Integer.parseInt(positions.get(0));
		int posY = Integer.parseInt(positions.get(1));
		Line one = new Line(posX - 20, posY, posX - 3, posY);
		Line two = new Line(posX + 3, posY, posX + 20, posY);
		Line three = new Line(posX, posY - 20, posX, posY - 3);
		Line four = new Line(posX, posY + 3, posX, posY + 20);
		pos.getChildren().addAll(one, two, three, four);
		for (Node i : pos.getChildren()) {
			((Line) i).setStrokeWidth(2);
		}
		Circle point = new Circle(posX, posY, 1);
		point.setFill(Color.BLACK);
		Text tekst = new Text(Integer.toString(posNr));
		tekst.setLayoutX(posX + 2);
		tekst.setLayoutY(posY - 3);
		pos.getChildren().addAll(point, tekst);
		getChildren().add(pos);
	}

	/**
	 * Draws position with sector.
	 * 
	 * @param easting
	 *            Easting.
	 * @param northing
	 *            Northing.
	 * @param posNr
	 *            Number of position.
	 * @param name
	 *            Name of position.
	 * @param direction
	 *            Direction of position´s sector.
	 */
	public void drawPosition(String easting, String northing, int posNr, String name, double direction) {
		isCenterSet=true;
		Group pos = new Group();
		pos.setUserData(name);
		int posX = Integer.parseInt(fromCordToCanv(easting, northing).get(0));
		int posY = Integer.parseInt(fromCordToCanv(easting, northing).get(1));
		Line one = new Line(posX - 20, posY, posX - 3, posY);
		Line two = new Line(posX + 3, posY, posX + 20, posY);
		Line three = new Line(posX, posY - 20, posX, posY - 3);
		Line four = new Line(posX, posY + 3, posX, posY + 20);
		pos.getChildren().addAll(one, two, three, four);
		for (Node i : pos.getChildren()) {
			((Line) i).setStrokeWidth(2);
		}
		Circle point = new Circle(posX, posY, 1);
		point.setFill(Color.BLACK);
		Text tekst = new Text(Integer.toString(posNr));
		tekst.setLayoutX(posX + 2);
		tekst.setLayoutY(posY - 3);
		pos.getChildren().addAll(point, tekst);
		getChildren().add(pos);
		drawSector(easting, northing, direction, name);
	}

	/**
	 * Removes position and sector from pane .
	 * 
	 * @param name
	 *            Position name.
	 */
	public void deletePosition(String name) {
		// Deleting position
		for (Node o : getChildren()) {
			if (o.getUserData() != null && o.getUserData().equals((String) name)) {
				getChildren().remove(o);
				break;
			}

		}
		// Deleting sector
		for (Node o : getChildren()) {
			if (o.getUserData() != null && o.getUserData().equals((String) name)) {
				getChildren().remove(o);
				break;
			}

		}

	}

	/**
	 * Draws sector.
	 * 
	 * @param easting
	 *            Easting.
	 * @param northing
	 *            Northing.
	 * @param direction
	 *            Direction of sector.
	 * @param name
	 *            Name of position.
	 * 
	 */
	public void drawSector(String easting, String northing, double direction, String name) {
		// 2000 MIL = 112.5 degrees
		Group sector = new Group();
		sector.setUserData(name);
		int posX = Integer.parseInt(fromCordToCanv(easting, northing).get(0));
		int posY = Integer.parseInt(fromCordToCanv(easting, northing).get(1));
		double FOAngle = Math.round(direction / 17.777777777778);
		double angle = 360 - (FOAngle + 56.25 - 90);// TO-DO
		Arc a1 = new Arc(posX, posY, 800, 800, angle, 112.5);
		Arc a2 = new Arc(posX, posY, 40, 40, angle, 112.5);
		a1.setType(ArcType.OPEN);
		a1.setStroke(Color.BLACK);
		a1.setFill(null);
		a1.setStrokeWidth(3);
		a2.setType(ArcType.OPEN);
		a2.setStroke(Color.BLACK);
		a2.setFill(null);
		a2.setStrokeWidth(3);
		int arcLeftX1 = (int) Math
				.round((Integer.parseInt(easting) * 10 + 8000 * Math.sin((FOAngle - 56.25) * Math.PI / 180)) / 10);
		int arcLeftY1 = (int) Math
				.round((Integer.parseInt(northing) * 10 + 8000 * Math.cos((FOAngle - 56.25) * Math.PI / 180)) / 10);
		int arcLeftX2 = (int) Math
				.round((Integer.parseInt(easting) * 10 + 400 * Math.sin((FOAngle - 56.25) * Math.PI / 180)) / 10);
		int arcLeftY2 = (int) Math
				.round((Integer.parseInt(northing) * 10 + 400 * Math.cos((FOAngle - 56.25) * Math.PI / 180)) / 10);
		Line first = new Line(fromCordToCanv(arcLeftX2, arcLeftY2).get(0), fromCordToCanv(arcLeftX2, arcLeftY2).get(1),
				fromCordToCanv(arcLeftX1, arcLeftY1).get(0), fromCordToCanv(arcLeftX1, arcLeftY1).get(1));
		first.setStrokeWidth(3);
		int arcRightX1 = (int) Math
				.round((Integer.parseInt(easting) * 10 + 8000 * Math.sin((FOAngle + 56.25) * Math.PI / 180)) / 10);
		int arcRightY1 = (int) Math
				.round((Integer.parseInt(northing) * 10 + 8000 * Math.cos((FOAngle + 56.25) * Math.PI / 180)) / 10);
		int arcRightX2 = (int) Math
				.round((Integer.parseInt(easting) * 10 + 400 * Math.sin((FOAngle + 56.25) * Math.PI / 180)) / 10);
		int arcRightY2 = (int) Math
				.round((Integer.parseInt(northing) * 10 + 400 * Math.cos((FOAngle + 56.25) * Math.PI / 180)) / 10);
		Line second = new Line(fromCordToCanv(arcRightX2, arcRightY2).get(0),
				fromCordToCanv(arcRightX2, arcRightY2).get(1), fromCordToCanv(arcRightX1, arcRightY1).get(0),
				fromCordToCanv(arcRightX1, arcRightY1).get(1));
		second.setStrokeWidth(3);
		int arcCenterX1 = (int) Math
				.round((Integer.parseInt(easting) * 10 + 8000 * Math.sin((FOAngle) * Math.PI / 180)) / 10);
		int arcCenterY1 = (int) Math
				.round((Integer.parseInt(northing) * 10 + 8000 * Math.cos((FOAngle) * Math.PI / 180)) / 10);
		int arcCenterX2 = (int) Math
				.round((Integer.parseInt(easting) * 10 + 9000 * Math.sin((FOAngle) * Math.PI / 180)) / 10);
		int arcCenterY2 = (int) Math
				.round((Integer.parseInt(northing) * 10 + 9000 * Math.cos((FOAngle) * Math.PI / 180)) / 10);
		Line third = new Line(fromCordToCanv(arcCenterX2, arcCenterY2).get(0),
				fromCordToCanv(arcCenterX2, arcCenterY2).get(1), fromCordToCanv(arcCenterX1, arcCenterY1).get(0),
				fromCordToCanv(arcCenterX1, arcCenterY1).get(1));

		third.setStrokeWidth(5);
		int arcCenterX3 = (int) Math.round((arcCenterX2 * 10 + 500 * Math.sin((FOAngle - 120) * Math.PI / 180)) / 10);
		int arcCenterY3 = (int) Math.round((arcCenterY2 * 10 + 500 * Math.cos((FOAngle - 120) * Math.PI / 180)) / 10);
		Line fourth = new Line(fromCordToCanv(arcCenterX2, arcCenterY2).get(0),
				fromCordToCanv(arcCenterX2, arcCenterY2).get(1), fromCordToCanv(arcCenterX3, arcCenterY3).get(0),
				fromCordToCanv(arcCenterX3, arcCenterY3).get(1));

		fourth.setStrokeWidth(5);
		int arcCenterX4 = (int) Math.round((arcCenterX2 * 10 + 500 * Math.sin((FOAngle + 120) * Math.PI / 180)) / 10);
		int arcCenterY4 = (int) Math.round((arcCenterY2 * 10 + 500 * Math.cos((FOAngle + 120) * Math.PI / 180)) / 10);
		Line fifth = new Line(fromCordToCanv(arcCenterX2, arcCenterY2).get(0),
				fromCordToCanv(arcCenterX2, arcCenterY2).get(1), fromCordToCanv(arcCenterX4, arcCenterY4).get(0),
				fromCordToCanv(arcCenterX4, arcCenterY4).get(1));

		fifth.setStrokeWidth(5);
		sector.getChildren().addAll(a1, a2, first, second, third, fourth, fifth);
		getChildren().addAll(sector);

	}

	/**
	 * Draws limitation polygon
	 * 
	 * @param points
	 *            Two-dimensional list containing polygon points.
	 * 
	 */
	public void drawLimitationPolygon(ArrayList<ArrayList<Integer>> points) {
		Polygon polygon = new Polygon();
		for (ArrayList<Integer> point : points) {
			double x = fromCordToCanv(point.get(0), point.get(1)).get(0);
			double y = fromCordToCanv(point.get(0), point.get(1)).get(1);
			polygon.getPoints().add(x);
			polygon.getPoints().add(y);
		}
		ArrayList<Integer> firstPoint = points.get(0);
		double x = fromCordToCanv(firstPoint.get(0), firstPoint.get(1)).get(0);
		double y = fromCordToCanv(firstPoint.get(0), firstPoint.get(1)).get(1);
		polygon.getPoints().add(x);
		polygon.getPoints().add(y);
		polygon.setFill(Color.rgb(127, 255, 0, 0.3));
		polygon.setStroke(Color.BLACK);
		polygon.setStrokeWidth(3);
		polygon.setUserData("limiter");
		getChildren().add(polygon);

	}

	/**
	 * Deletes target from pane.
	 * 
	 * @param targetName
	 *            Target name.
	 */
	public void deleteTarget(String targetName) {
		for (Node o : getChildren()) {
			if (o.getUserData() != null && o.getUserData().equals((String) targetName)) {
				getChildren().remove(o);
				break;
			}
		}
	}

	/**
	 * Draws target.
	 * 
	 * @param easting
	 *            Easting.
	 * @param northing
	 *            Northing.
	 * @param targetName
	 *            Target name.
	 * @param drawName
	 *            Name to be drawn on graphics.
	 * @param isActive
	 *            Boolean whether target is active or not.
	 * 
	 */
	public void drawTarget(String easting, String northing, String targetName, String drawName, boolean isActive) {
		if(isCenterSet==false){
			setCenterLines(easting, northing);
		}
		Group tar = new Group();
		String info = targetName + drawName;
		int posX = Integer.parseInt(fromCordToCanv(easting, northing).get(0));
		int posY = Integer.parseInt(fromCordToCanv(easting, northing).get(1));
		Line one = new Line(posX - 20, posY, posX + 20, posY);
		Line two = new Line(posX, posY - 20, posX, posY + 20);
		tar.getChildren().addAll(one, two);
		for (Node i : tar.getChildren()) {
			if (isActive) {// If target is active
				((Line) i).setStroke(Color.RED);
			} else {// Non-active target
				((Line) i).setStroke(Color.GREEN);
			}
			((Line) i).setStrokeWidth(1.5);
		}
		Text tekst = new Text("");
		if (drawName == "") {
			tekst.setText(targetName);
		} else {
			tekst.setText(drawName);
		}

		tekst.setLayoutX(posX + 2);
		tekst.setLayoutY(posY - 3);
		tekst.setFont(Font.font("Verdana", 8));
		tar.getChildren().add(tekst);
		tar.setUserData(info);
		getChildren().add(tar);
	}

	/**
	 * Adds grid to canvas.
	 */
	public void addGrid() {

		double w = getBoundsInLocal().getWidth();
		double h = getBoundsInLocal().getHeight();
		grid = new Canvas(w, h);
		grid.setMouseTransparent(true);
		gc = grid.getGraphicsContext2D();
		gc.setStroke(Color.GREY);
		gc.setLineWidth(1.2);
		gc.setLineDashOffset(1.0);

		// Drawing grid, every 100th line is wider.
		double offset = 10;
		for (double i = offset; i < w; i += offset) {
			if (i % 100 == 0) {
				gc.setLineWidth(4.0);
				gc.strokeLine(i, 0, i, h);
				gc.strokeLine(0, i, w, i);
				gc.setLineWidth(1.2);

			} else {
				gc.strokeLine(i, 0, i, h);
				gc.strokeLine(0, i, w, i);
			}

		}
		grid.setUserData("grid");
		getChildren().add(grid);
		grid.toBack();

	}

	/**
	 * Returns scale.
	 * 
	 * @return Scale
	 */
	public double getScale() {
		return myScale.get();
	}

	/**
	 * Sets scale.
	 * 
	 * @param scale
	 *            Scale.
	 */
	public void setScale(double scale) {
		myScale.set(scale);
	}

	/**
	 * Sets pivot.
	 * 
	 * @param x
	 *            X-coordinate.
	 * @param y
	 *            Y-coordinate.
	 */
	public void setPivot(double x, double y) {
		setTranslateX(getTranslateX() - x);
		setTranslateY(getTranslateY() - y);
	}
}

/**
 * Class containing mouse points for dragging.
 */
class DragContext {

	double mouseAnchorX;
	double mouseAnchorY;

	double translateAnchorX;
	double translateAnchorY;

}

/**
 * Listeners for making the scene's canvas draggable and zoomable
 */
class SceneGestures {
	/** Maximum size of mouse zoom. */
	private static final double MAX_SCALE = 7.0d;
	/** Minimal size of mouse zoom. */
	private static final double MIN_SCALE = .5d;
	/** Mouse dragging information. */
	private DragContext sceneDragContext = new DragContext();
	/** canvas */
	ViewCanvas canvas;

	/** Constructor. 
	 * @param canvas Canvas.*/
	public SceneGestures(ViewCanvas canvas) {
		this.canvas = canvas;
	}

	/**
	 * Returns mouse pressed event handler.
	 * 
	 * @return Mouse pressed event handler.
	 */
	public EventHandler<MouseEvent> getOnMousePressedEventHandler() {
		return onMousePressedEventHandler;
	}

	/**
	 * Returns mouse dragged event handler.
	 * 
	 * @return Mouse dragged event handler.
	 */
	public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
		return onMouseDraggedEventHandler;
	}

	/**
	 * Returns scrolled event handler.
	 * 
	 * @return Mouse scrolled event handler.
	 */
	public EventHandler<ScrollEvent> getOnScrollEventHandler() {
		return onScrollEventHandler;
	}

	/** Called when mouse is pressed. */
	private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

		public void handle(MouseEvent event) {

			if (!event.isPrimaryButtonDown())
				return;

			sceneDragContext.mouseAnchorX = event.getSceneX();
			sceneDragContext.mouseAnchorY = event.getSceneY();

			sceneDragContext.translateAnchorX = canvas.getTranslateX();
			sceneDragContext.translateAnchorY = canvas.getTranslateY();

		}

	};
	/** Called when mouse is pressed and dragged. */
	private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
		public void handle(MouseEvent event) {

			if (!event.isPrimaryButtonDown())
				return;

			canvas.setTranslateX(sceneDragContext.translateAnchorX + event.getSceneX() - sceneDragContext.mouseAnchorX);
			canvas.setTranslateY(sceneDragContext.translateAnchorY + event.getSceneY() - sceneDragContext.mouseAnchorY);

			event.consume();
		}
	};
	/**
	 * Called when mouse is scrolled. Zooms graphics in or out.
	 */
	private EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<ScrollEvent>() {

		@Override
		public void handle(ScrollEvent event) {
			double delta = 1.05;
			double scale = canvas.getScale();
			double oldScale = scale;

			if (event.getDeltaY() < 0)
				scale /= delta;
			else
				scale *= delta;

			scale = clamp(scale, MIN_SCALE, MAX_SCALE);

			double f = (scale / oldScale) - 1;

			double dx = (event.getSceneX()
					- (canvas.getBoundsInParent().getWidth() / 2 + canvas.getBoundsInParent().getMinX()));
			double dy = (event.getSceneY()
					- (canvas.getBoundsInParent().getHeight() / 2 + canvas.getBoundsInParent().getMinY()));

			canvas.setScale(scale);

			canvas.setPivot(f * dx, f * dy);

			event.consume();

		}

	};

	/**
	 * Function for clamping to a value.
	 * 
	 * @param value
	 *            Value.
	 * @param min
	 *            Minimal scale.
	 * @param max
	 *            Maximum scale.
	 * @return Clamped value.
	 */
	public static double clamp(double value, double min, double max) {

		if (Double.compare(value, min) < 0)
			return min;

		if (Double.compare(value, max) > 0)
			return max;

		return value;
	}
}
