package controller.tools.Shapes;

import controller.Layer;
import controller.MainViewController;
import controller.Project;
import controller.history.ICmd;
import controller.history.RecordCmd;
import controller.tools.Tool;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import utils.UndoException;
import utils.Utils;

/**
 * Classe abstraite implémentant les propriétés partagées entre toutes les formes.
 */
public abstract class ShapeDrawer extends Tool {
	protected Layer shapeLayer; //  calque temporaire utilisé pour le dessin des formes durant leur création
	protected ShapeSave currentShapeSave; // La forme actuellement dessinée, à sauver
	
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
	}
	
	/**
	 * Crée le calque temporaire sur lequel la forme est dessinée, et y ajoute l'écoute des événements liés à la souris.
	 */
	@Override
	public void CallbackNewToolChanged() {
		MainViewController.getInstance().getToolBarController().shapeTool.setSelected(true);
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
				if(wasDragged) { // vrai si l'évènement drag s'est produit -> la forme possède une hauter/largeur.
					shapeLayer = new Layer(shapeLayer, false); // crée un claque (celui-ci n'est pas temporaire)
					Project.getInstance().addLayer(shapeLayer); // ajoute ce calque au projet
					currentShapeSave.execute();
					CallbackNewToolChanged(); // TODO: À vérifier avec Loyse
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
	 * @param endPosX, position courante de la souris sur l'axe des X.
	 * @param endPosY, position courrant de la souris sur l'axe des Y.
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
	 * Classe interne implémentant une commande sauvegardant la création d'une forme et définissant l'action à effectuer en cas d'appel à undo() ou redo()
	 * sur cette commande.
	 */
	class ShapeSave implements ICmd {
		//image à récupérer en cas de redo
		private Image undosave;
		//image à récupérer en cas de undo
		private Image redosave = null;
		
		/**
		 * Construit une commande sauvegardant la création d'une forme.
		 */
		private ShapeSave() {
			undosave = Utils.makeSnapshot(Project.getInstance().getCurrentLayer(), Color.TRANSPARENT);
		}
		
		@Override
		public String toString() {
			return tooltipHistory;
		}
		
		@Override
		public void execute() {
			RecordCmd.getInstance().saveCmd(this);
		}
		
		@Override
		public void undo() throws UndoException {
			if (undosave == null) {
				throw new UndoException();
			}
			// on supprime le 1er calque s'il ne s'agit pas du calque actuellement sélectionné --> c'est donc un calque temporaire
			if (Project.getInstance().getCurrentLayer() != Project.getInstance().getLayers().getFirst()) {
				Project.getInstance().getLayers().removeFirst();
			}
			redosave = Utils.makeSnapshot(Project.getInstance().getCurrentLayer(), Color.TRANSPARENT); // snapshot le calque actuellement sélectionné
			Project.getInstance().getLayers().removeFirst();
			Project.getInstance().setCurrentLayer(Project.getInstance().getLayers().getFirst());
			undosave = null;
		}
		
		@Override
		public void redo() throws UndoException {
			if (redosave == null) {
				throw new UndoException();
			}
			undosave = Utils.makeSnapshot(Project.getInstance().getCurrentLayer(), Color.TRANSPARENT); // snapshot le calque actuellement sélectionné
			Layer redoLayer = new Layer((int) redosave.getWidth(), (int) redosave.getHeight(), "Forme", false); // crée un nouveau calque
			redoLayer.getGraphicsContext2D().drawImage(redosave, 0, 0); // redessine la forme sur le calque
			Project.getInstance().addLayer(redoLayer); // ajoute le calque au projet
			redosave = null;
		}
	}
}
