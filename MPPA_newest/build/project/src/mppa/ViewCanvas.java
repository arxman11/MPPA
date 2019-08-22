package mppa;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;


public class ViewCanvas extends Pane {
	private Canvas grid;
	private GraphicsContext gc;
	DoubleProperty myScale = new SimpleDoubleProperty(1.01);

	public ViewCanvas() {
		setPrefSize(3200, 3200);

		// add scale transform
		scaleXProperty().bind(myScale);
		scaleYProperty().bind(myScale);
	}
	public void drawPosition(String easting,String northing,int posNr){
		
	}
public void drawTarget(String easting,String northing){
		
	}

	/**
	 * Add a grid to the canvas, send it to back
	 */
	public void addGrid() {

		double w = getBoundsInLocal().getWidth();
		double h = getBoundsInLocal().getHeight();
		// add grid
		grid = new Canvas(w, h);

		// don't catch mouse events
		grid.setMouseTransparent(true);

		gc = grid.getGraphicsContext2D();

		gc.setStroke(Color.GREY);
		gc.setLineWidth(1.2);
		gc.setLineDashOffset(1.0);

		// draw grid lines
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

		getChildren().add(grid);

		grid.toBack();
	}

	public double getScale() {
		return myScale.get();
	}

	public void setScale(double scale) {
		myScale.set(scale);
	}

	public void setPivot(double x, double y) {
		setTranslateX(getTranslateX() - x);
		setTranslateY(getTranslateY() - y);
	}
}

/**
 * Mouse drag context used for scene and nodes.
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

	private static final double MAX_SCALE = 10.0d;
	private static final double MIN_SCALE = .5d;

	private DragContext sceneDragContext = new DragContext();

	ViewCanvas canvas;

	public SceneGestures(ViewCanvas canvas) {
		this.canvas = canvas;
	}

	public EventHandler<MouseEvent> getOnMousePressedEventHandler() {
		return onMousePressedEventHandler;
	}

	public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
		return onMouseDraggedEventHandler;
	}

	public EventHandler<ScrollEvent> getOnScrollEventHandler() {
		return onScrollEventHandler;
	}

	private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

		public void handle(MouseEvent event) {

			// right mouse button => panning
			if (!event.isPrimaryButtonDown())
				return;

			sceneDragContext.mouseAnchorX = event.getSceneX();
			sceneDragContext.mouseAnchorY = event.getSceneY();

			sceneDragContext.translateAnchorX = canvas.getTranslateX();
			sceneDragContext.translateAnchorY = canvas.getTranslateY();

		}

	};

	private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
		public void handle(MouseEvent event) {

			// right mouse button => panning
			if (!event.isPrimaryButtonDown())
				return;

			canvas.setTranslateX(sceneDragContext.translateAnchorX + event.getSceneX() - sceneDragContext.mouseAnchorX);
			canvas.setTranslateY(sceneDragContext.translateAnchorY + event.getSceneY() - sceneDragContext.mouseAnchorY);

			event.consume();
		}
	};

	/**
	 * Mouse wheel handler: zoom to pivot point
	 */
	private EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<ScrollEvent>() {

		@Override
		public void handle(ScrollEvent event) {

			double delta = 1.05;

			double scale = canvas.getScale(); // currently we only use Y, same
												// value is used for X
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

			// note: pivot value must be untransformed, i. e. without scaling
			canvas.setPivot(f * dx, f * dy);

			event.consume();

		}

	};

	public static double clamp(double value, double min, double max) {

		if (Double.compare(value, min) < 0)
			return min;

		if (Double.compare(value, max) > 0)
			return max;

		return value;
	}
}
