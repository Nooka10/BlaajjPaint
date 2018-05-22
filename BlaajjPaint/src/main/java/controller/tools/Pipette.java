package controller.tools;

import controller.Layer;
import controller.MainViewController;
import controller.Project;
import javafx.event.EventHandler;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import utils.Utils;

/**
 * Classe implémentant l'outil <b>Pipette</b> permettant d'attribuer une couleur au sélecteur de couleur en sélectionnant une couleur dans l'espace de travail à l'aide de
 * la souris. Implémente le modèle Singleton.
 */
public class Pipette extends Tool {
	private static Pipette toolInstance; // l'instance unique du singleton Hand
	private Layer tmpLayer;
	private Layer oldCurrentLayer;
	
	/**
	 * Constructeur privé (modèle singleton).
	 */
	private Pipette() {
		toolType = ToolType.PIPETTE;
		tmpLayer = new Layer(Project.getInstance().getWidth(), Project.getInstance().getHeight(), true);
	}
	
	/**
	 * Retourne l'instance unique du singleton Pipette.
	 *
	 * @return l'instance unique du singleton Pipette.
	 */
	public static Pipette getInstance() {
		if (toolInstance == null) {
			toolInstance = new Pipette();
		}
		return toolInstance;
	}
	
	@Override
	protected EventHandler<MouseEvent> createMousePressedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				for (Layer layer : Project.getInstance().getLayers()) { // parcours tous les calques
					if (layer.isVisible()) { // l'outil pipette ignore les calques masqués
						WritableImage srcMask = new WritableImage((int) layer.getWidth(), (int) layer.getHeight());
						srcMask = Utils.makeSnapshot(layer, Color.TRANSPARENT, srcMask);  // on fait un snapshot du calque actuellement traité
						
						PixelReader maskReader = srcMask.getPixelReader();
						
						int x = (int) (event.getX() - layer.getTranslateX());
						int y = (int) (event.getY() - layer.getTranslateY());
						
						if (x >= 0 && x < layer.getWidth() && y >= 0 && y < layer.getHeight()) { // si le clique de la souris est dans le calque actuellement traité
							Color color = maskReader.getColor(x, y); // on récupère la couleur du pixel sélectionné
							if (!color.equals(Color.TRANSPARENT)) { // vrai si la couleur n'est pas transparente
								Project.getInstance().setCurrentColor(color); // on attribue cette couleur au sélecteur de couleur
								MainViewController.getInstance().getRightMenuController().setColorPickerColor(color);
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
	protected EventHandler<MouseEvent> createMouseEnteredEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Image img = new Image("/cursors/pipetteCursor.png");
				changeCursor(new ImageCursor(img, 2, 30)); // change le curseur de la souris en mode "pipette"
			}
		};
	}
	
	@Override
	protected EventHandler<MouseEvent> createMouseExitedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				resetPreviousCursor(); // remet le curseur précédent
			}
		};
	}
	
	@Override
	public void CallbackOldToolChanged() {
		MainViewController.getInstance().getToolBarController().pipetteTool.setSelected(false);
		Project.getInstance().getLayers().remove(tmpLayer); // on supprime le calque temporaire
		if (Project.getInstance().getCurrentLayer() == tmpLayer) {
			Project.getInstance().setCurrentLayer(oldCurrentLayer);
		}
	}
	
	@Override
	public void CallbackNewToolChanged() {
		MainViewController.getInstance().getToolBarController().pipetteTool.setSelected(true);
		oldCurrentLayer = Project.getInstance().getCurrentLayer();
		Project.getInstance().addLayer(tmpLayer);
	}
}
