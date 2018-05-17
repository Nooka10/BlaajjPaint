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
						WritableImage srcMask = new WritableImage(Project.getInstance().getWidth(), Project.getInstance().getHeight());
						final SnapshotParameters spa = new SnapshotParameters();
						spa.setFill(Color.TRANSPARENT);
						srcMask = item.snapshot(spa, srcMask); // FIXME: changer le snapshot pour utiliser Utils!!
						
						PixelReader maskReader = srcMask.getPixelReader();
						
						int x = (int)(event.getX() + Project.getInstance().getCurrentLayer().getLayoutX());
						int y = (int) (event.getY() - Project.getInstance().getCurrentLayer().getLayoutY());
						if(x >= 0 && x < item.getWidth() && y >= 0 && y < item.getHeight()) {
							Color color = maskReader.getColor(x,y);
							if (!color.equals(Color.TRANSPARENT)) {
								Project.getInstance().setCurrentColor(color);
								break;
							}
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
