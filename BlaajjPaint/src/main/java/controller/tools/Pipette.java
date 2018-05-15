/*
Author: Benoît
 */
package controller.tools;

import controller.Layer;
import controller.Project;
import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 * Classe implémentant l'outil pipette
 */
public class Pipette extends Tool {
	
	private static Pipette toolInstance = null;
	
	public static Pipette getInstance() {
		if (toolInstance == null) {
			toolInstance = new Pipette();
		}
		return toolInstance;
	}
	
	private Pipette() {
		toolType = ToolType.PIPETTE;
	}
	
	// si le calque actif est masqué, l'outil pipette ne prend pas la couleur cliquée
	@Override
	protected EventHandler<MouseEvent> createMousePressedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				
				for (Layer item : Project.getInstance().getLayers()) {
					if (item.isVisible()) {
						WritableImage srcMask = new WritableImage((int) item.getWidth(), (int) item.getHeight());
						final SnapshotParameters spa = new SnapshotParameters();
						spa.setFill(Color.TRANSPARENT);
						srcMask = item.snapshot(spa, srcMask);
						
						PixelReader maskReader = srcMask.getPixelReader();
						
						Color color = maskReader.getColor((int) event.getX(), (int) event.getY());
						if (!color.equals(Color.TRANSPARENT)) {
							Project.getInstance().setCurrentColor(maskReader.getColor((int) event.getX(), (int) event.getY()));
							break;
						}
					}
				}
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
