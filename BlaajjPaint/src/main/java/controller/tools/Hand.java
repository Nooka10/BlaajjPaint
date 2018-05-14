/*
Author: Benoît
 */
package controller.tools;

import controller.Layer;
import controller.MainViewController;
import controller.Project;
import controller.history.ICmd;
import controller.history.RecordCmd;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseEvent;
import utils.UndoException;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

/**
 * Classe implémentant l'outil pipette
 */
public class Hand extends Tool {
	
	private static Hand toolInstance = new Hand();
	
	public static Hand getInstance() {
		return toolInstance;
	}
	
	private Hand() {
		toolType = ToolType.MOVE;
	}
	
	private Point dragStartScreen;
	
	private Point dragEndScreen;
	
	private AffineTransform coordTransform = new AffineTransform();
	
	private double x;
	
	private double y;
	
	@Override
	protected EventHandler<MouseEvent> createMousePressedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//x = event.getSceneX();
				//y = event.getSceneY();
				
				dragStartScreen = new Point((int)event.getX(), (int)event.getY());
				dragEndScreen = null;
			}
		};
	}
	
	@Override
	protected EventHandler<MouseEvent> createMouseDraggedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				System.out.println("============= Move camera ============");
				try {
					dragEndScreen = new Point((int)event.getX(), (int)event.getY());
					Point dragStart = transformPoint(dragStartScreen);
					Point dragEnd = transformPoint(dragEndScreen);
					double dx = dragEnd.getX() - dragStart.getX();
					double dy = dragEnd.getY() - dragStart.getY();
					coordTransform.translate(dx, dy);
					dragStartScreen = dragEndScreen;
					dragEndScreen = null;
					Project.getInstance().drawWorkspace();
				} catch (NoninvertibleTransformException ex) {
					ex.printStackTrace();
				}
				
				
				/*
				for (Layer l : Project.getInstance().getLayers()) {
					l.setTranslateX(l.getTranslateX() + event.getX() - x);
					l.setTranslateY(l.getTranslateY() + event.getY() - y);
				}
				/*
				workspace.setScaleX(workspace.getScaleX() * factor);
				workspace.setScaleY(workspace.getScaleY() * factor);
				workspace.setScaleZ(workspace.getScaleZ() * factor);
				*/
			}
		};
	}
	
	private Point transformPoint(Point p1) throws NoninvertibleTransformException {
//        System.out.println("Model -> Screen Transformation:");
//        showMatrix(coordTransform);
		AffineTransform inverse = coordTransform.createInverse();
//        System.out.println("Screen -> Model Transformation:");
//        showMatrix(inverse);
		
		Point p2 = new Point();
		inverse.transform(p1, p2);
		return p2;
	}
	
	@Override
	protected EventHandler<MouseEvent> createMouseReleasedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// do nothing
			}
		};
	}
}
