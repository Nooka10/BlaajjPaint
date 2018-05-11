/*
Author: Benoît
 */
package controller.tools;

import controller.Project;
import javafx.event.EventHandler;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;

/**
 * Classe implémentant l'outil pipette
 */
public class Pipette extends Tool {
	
	private static Pipette toolInstance = new Pipette();
	
	public static Pipette getInstance() {
		return toolInstance;
	}
	
	private Pipette() {
		toolType = ToolType.PIPETTE;
	}
	
	@Override
	protected EventHandler<MouseEvent> createMousePressedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				WritableImage srcMask = new WritableImage((int) Project.getInstance().getCurrentLayer().getWidth(), (int) Project.getInstance().getCurrentLayer().getHeight());
				srcMask = Project.getInstance().getCurrentLayer().snapshot(null, srcMask);
				
				PixelReader maskReader = srcMask.getPixelReader();
				
				Project.getInstance().setCurrentColor(maskReader.getColor((int) event.getX(), (int) event.getY()));
			}
		};
	}
	
	@Override
	protected EventHandler<MouseEvent> createMouseDraggedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// do nothing
			}
		};
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
