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
 * Classe implémentant l'outil <b>pipette</b>
 */
public class Pipette extends Tool {
	
	private static Pipette toolInstance = null;
	
	private static Layer tmpLayer;
	private Layer oldCurrentLayer;
	
	public static Pipette getInstance() {
		if (toolInstance == null) {
			toolInstance = new Pipette();
		}
		return toolInstance;
	}
	
	private Pipette() {
		toolType = ToolType.PIPETTE;
		tmpLayer = new Layer(Project.getInstance().getWidth(), Project.getInstance().getHeight(), true);
	}
	
	// si le calque actif est masqué, l'outil pipette ne prend pas la couleur cliquée
	@Override
	protected EventHandler<MouseEvent> createMousePressedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				
				for (Layer item : Project.getInstance().getLayers()) {
					
					if (item.isVisible()) {
						WritableImage srcMask = new WritableImage((int)item.getWidth(), (int)item.getHeight());
						final SnapshotParameters spa = new SnapshotParameters();
						spa.setFill(Color.TRANSPARENT);
						srcMask = item.snapshot(spa, srcMask);
						
						PixelReader maskReader = srcMask.getPixelReader();
						
						int x = (int) (event.getX() - item.getTranslateX());
						int y = (int) (event.getY() - item.getTranslateY());
						
						if (x >= 0 && x < item.getWidth() && y  >= 0 && y < item.getHeight()) {
							Color color = maskReader.getColor(x, y);
							System.out.println(color);
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
				// ne fait rien
			}
		};
	}
	
	@Override
	protected EventHandler<MouseEvent> createMouseReleasedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// ne fait rien
			}
		};
	}
	
	@Override
	public void CallbackOldToolChanged() {
		Project.getInstance().getLayers().remove(tmpLayer);
		if(Project.getInstance().getCurrentLayer() == tmpLayer) {
			Project.getInstance().setCurrentLayer(oldCurrentLayer);
		}
	}
	
	@Override
	public void CallbackNewToolChanged() {
		oldCurrentLayer = Project.getInstance().getCurrentLayer();
		Project.getInstance().addLayer(tmpLayer);
	}
}
