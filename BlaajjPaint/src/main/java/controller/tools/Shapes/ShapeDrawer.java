package controller.tools.Shapes;

import controller.Layer;
import controller.MainViewController;
import controller.Project;
import controller.history.ICmd;
import controller.history.RecordCmd;
import controller.tools.Tool;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Classe abstraite implémentant les propriétés partagées entre toutes les formes.
 */
public abstract class ShapeDrawer extends Tool {
	protected Layer shapeLayer; //  calque temporaire utilisé pour le dessin des formes durant leur création
	protected ShapeSave currentShapeSave; // La forme actuellement dessinée, à sauver
	private Layer oldCurrentLayer;
	
	//position du clic de départ de la forme
	protected double beginPointX;
	protected double beginPointY;
	
	//point de départ de la forme
	protected double startPosX;
	protected double startPosY;
	
	//dimensions de la forme
	protected double width;
	protected double height;
	
	//nom de la forme destiné à l'affichage de l'historique
	protected String tooltipHistory = "Dessin de forme";
	protected String nomForme;
	
	private boolean wasDragged;
	
	/**
	 * Supprime le calque temporaire, ainsi que le lien entre celui-ci est les événements de la souris.
	 */
	@Override
	public void CallbackOldToolChanged() {
		MainViewController.getInstance().getToolBarController().shapeTool.setSelected(false);
		// supprime les eventHandler du calque shapeLayer
		shapeLayer.removeEventHandler(MouseEvent.MOUSE_PRESSED, getCurrentOnMousePressedEventHandler());
		shapeLayer.removeEventHandler(MouseEvent.MOUSE_DRAGGED, getCurrentOnMouseDraggedEventHandler());
		shapeLayer.removeEventHandler(MouseEvent.MOUSE_RELEASED, getCurrentOnMouseRelesedEventHandler());
		Project.getInstance().removeLayer(shapeLayer); // supprime le calque du projet
		Project.getInstance().setCurrentLayer(oldCurrentLayer); // si nouvelle forme précédement crée, sera oldCurrentLayer à se moment la
	}
	
	/**
	 * Crée le calque temporaire sur lequel la forme est dessinée, et y ajoute l'écoute des événements liés à la souris.
	 */
	@Override
	public void CallbackNewToolChanged() {
		MainViewController.getInstance().getToolBarController().shapeTool.setSelected(true);
		initShapeTool();
	}
	
	/**
	 * Initialise le calque temporaire et ces événements
	 */
	private void initShapeTool() {
		oldCurrentLayer = Project.getInstance().getCurrentLayer();
		shapeLayer = new Layer(Project.getInstance().getWidth(), Project.getInstance().getHeight(), nomForme, true); // crée un calque temporaire
		// ajoute les eventHandler sur ce nouveau calque
		shapeLayer.addEventHandler(MouseEvent.MOUSE_PRESSED, getCurrentOnMousePressedEventHandler());
		shapeLayer.addEventHandler(MouseEvent.MOUSE_DRAGGED, getCurrentOnMouseDraggedEventHandler());
		shapeLayer.addEventHandler(MouseEvent.MOUSE_RELEASED, getCurrentOnMouseRelesedEventHandler());
		Project.getInstance().addLayer(shapeLayer); // on ajoute le calque au projet
	}
	
	@Override
	protected EventHandler<MouseEvent> createMousePressedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				currentShapeSave = new ShapeSave(); // crée une sauvegarde de la création de la forme
				beginPointX = event.getX(); // enregistre la position x de départ
				beginPointY = event.getY(); // enregistre la position y de départ
				wasDragged = false;
			}
		};
	}
	
	@Override
	protected EventHandler<MouseEvent> createMouseDraggedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				shapeLayer.getGraphicsContext2D().clearRect(0, 0, shapeLayer.getWidth(), shapeLayer.getHeight()); // Efface le calque temporaire
				updateShape(event.getX(), event.getY()); // met à jour les paramètres de dessin de la forme
				drawShape(); // dessine la forme sur le calque temporaire
				wasDragged = true;
			}
		};
	}
	
	@Override
	protected EventHandler<MouseEvent> createMouseReleasedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Project.getInstance().getLayers().remove(shapeLayer); // supprime le calque temporaire du projet
				if (wasDragged) { // vrai si l'évènement drag s'est produit -> la forme possède une hauter/largeur.
					shapeLayer = new Layer(shapeLayer, false); // crée un claque (celui-ci n'est pas temporaire)
					Project.getInstance().addLayer(shapeLayer); // ajoute ce calque au projet
					currentShapeSave.execute(); // a faire avant l'initShapeTool()
					initShapeTool();
				}
			}
		};
	}
	
	/**
	 * Méthode abstraite dessinant la forme sur le calque temporaire.
	 */
	abstract protected void drawShape();
	
	/**
	 * Met à jour les paramètres de dessin de la forme (position X et Y de départ, hauteur et largeur) en fonction de la position de la souris.
	 *
	 * @param endPosX,
	 * 		position courante de la souris sur l'axe des X.
	 * @param endPosY,
	 * 		position courrant de la souris sur l'axe des Y.
	 */
	private void updateShape(double endPosX, double endPosY) {
		if (endPosX < beginPointX) {
			this.startPosX = endPosX;
			width = beginPointX - endPosX;
		} else {
			this.startPosX = beginPointX;
			width = endPosX - beginPointX;
		}
		
		if (endPosY < beginPointY) {
			this.startPosY = endPosY;
			height = beginPointY - endPosY;
		} else {
			this.startPosY = beginPointY;
			height = endPosY - beginPointY;
		}
		
	}
	
	/**
	 * Classe interne implémentant une commande sauvegardant la création d'une forme et définissant l'action à effectuer en cas d'appel à undo() ou redo() sur cette
	 * commande.
	 */
	private class ShapeSave implements ICmd {
		Layer oldLayerSaved;
		Layer shapeLayerSaved;
		
		/**
		 * Construit une commande sauvegardant la création d'une forme.
		 */
		private ShapeSave() {
			oldLayerSaved = oldCurrentLayer;
		}
		
		@Override
		public String toString() {
			return tooltipHistory;
		}
		
		@Override
		public void execute() {
			shapeLayerSaved = shapeLayer;
			RecordCmd.getInstance().saveCmd(this);
		}
		
		@Override
		public void undo() {
			Project.getInstance().removeLayer(shapeLayerSaved);
			Project.getInstance().setCurrentLayer(oldLayerSaved);
		}
		
		@Override
		public void redo() {
			Project.getInstance().addLayer(shapeLayerSaved);
		}
	}
}
