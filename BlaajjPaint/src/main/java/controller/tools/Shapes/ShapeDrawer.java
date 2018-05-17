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
import utils.Utils;
import utils.UndoException;

/**
 * Classe abstraite destinée au dessin de formes prédéfinies
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

	private boolean hasDragged;

	
	/**
	 * Classe interne destine à la sauvegarde des formes dans l'historique des actions
	 */
	class ShapeSave implements ICmd {
		//image à récupérer en cas de redo
		private Image undosave;
		//image à récupérer en cas de undo
		private Image redosave = null;
		
		/**
		 * Constructeur de ShapeSave
		 */
		public ShapeSave() {
			undosave = Utils.makeSnapshot(Project.getInstance().getCurrentLayer(), Color.TRANSPARENT);
		}
		
		/**
		 * Affichage de ShapeSave
		 *
		 * @return la chaîne de caractère associée à la forme
		 */
		@Override
		public String toString() {
			return tooltipHistory;
		}
		
		/**
		 * Sauvegarde une instance de ShapeSave dans le RecordCmd
		 */
		@Override
		public void execute() {
			RecordCmd.getInstance().saveCmd(this);
		}
		
		/**
		 * Lors de l'appel à undo, le calque contenant la forme dessinée est supprimé de la liste des calques du projets.
		 *
		 * @throws UndoException, si il n'y a aucune instance sur laquelle on peut faire un undo
		 */
		@Override
		public void undo() throws UndoException {
			if (undosave == null) {
				throw new UndoException();
			}
			if (Project.getInstance().getCurrentLayer() != Project.getInstance().getLayers().getFirst()) {
				Project.getInstance().getLayers().removeFirst();
			}
			redosave = Utils.makeSnapshot(Project.getInstance().getCurrentLayer(), Color.TRANSPARENT);
			Project.getInstance().getLayers().removeFirst();
			//MainViewController.getInstance().getRightMenuController().deleteLayer(0);
			Project.getInstance().setCurrentLayer(Project.getInstance().getLayers().getFirst());
			Project.getInstance().drawWorkspace();
			undosave = null;
		}
		
		/**
		 * Lors de l'appel à redo, le calque contenant la forme dessinée est recréé et replacé dans la liste des calques
		 *
		 * @throws UndoException, si il n'y a aucune instance sur laquelle on peut faire un redo
		 */
		@Override
		public void redo() throws UndoException {
			if (redosave == null) {
				throw new UndoException();
			}
			undosave = Utils.makeSnapshot(Project.getInstance().getCurrentLayer(), Color.TRANSPARENT);
			Layer redoLayer = new Layer((int) redosave.getWidth(), (int) redosave.getHeight(), "Forme", false);
			redoLayer.getGraphicsContext2D().drawImage(redosave, 0, 0);
			Project.getInstance().addLayer(redoLayer);
			redosave = null;
		}
	}
	
	
	/**
	 * Crée le calque temporaire sur lequel la forme est dessinée, et y ajoute l'écoute des événements liés à la souris
	 */
	@Override
	public void CallbackNewToolChanged() {
		shapeLayer = new Layer(Project.getInstance().getWidth(), Project.getInstance().getHeight(), nomForme, true);
		Project.getInstance().removeEventHandler(Tool.getCurrentTool());
		shapeLayer.addEventHandler(MouseEvent.MOUSE_PRESSED, currentOnMousePressedEventHandler);
		shapeLayer.addEventHandler(MouseEvent.MOUSE_DRAGGED, currentOnMouseDraggedEventHandler);
		shapeLayer.addEventHandler(MouseEvent.MOUSE_RELEASED, currentOnMouseRelesedEventHandler);
		
		Project.getInstance().getLayers().addFirst(shapeLayer);
		Project.getInstance().drawWorkspace();
	}
	
	/**
	 * Supprime le calque temporaire, ainsi que le lien entre celui-ci est les événements de la souris
	 */
	@Override
	public void CallbackOldToolChanged() {
		Project.getInstance().getLayers().remove(shapeLayer);
		shapeLayer.removeEventHandler(MouseEvent.MOUSE_PRESSED, currentOnMousePressedEventHandler);
		shapeLayer.removeEventHandler(MouseEvent.MOUSE_DRAGGED, currentOnMouseDraggedEventHandler);
		shapeLayer.removeEventHandler(MouseEvent.MOUSE_RELEASED, currentOnMouseRelesedEventHandler);
		Project.getInstance().drawWorkspace();
	}
	
	
	/**
	 * Lors d'un clic de la souris, une instance de la forme à sauver est créée. Le point de départ de la forme à dessiner est fixé.
	 *
	 * @return l'événement à réaliser lorsque le clique de la souris est pressé
	 */
	@Override
	protected EventHandler<MouseEvent> createMousePressedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				currentShapeSave = new ShapeSave();
				beginPointX = event.getX();
				beginPointY = event.getY();
				hasDragged = false;
			}
		};
	}
	
	/**
	 * Les paramètres de dessin (point de départ, hauteur et largeur) de la forme sont mis à jour en fonction de la position de la souris. Le calque temporaire est mis à
	 * jour
	 *
	 * @return l'événement à réaliser lorsque le clic de la souris est maintenue enfoncée
	 */
	@Override
	protected EventHandler<MouseEvent> createMouseDraggedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				shapeLayer.getGraphicsContext2D().clearRect(0, 0, shapeLayer.getWidth(), shapeLayer.getHeight());
				updateShape(event.getX(), event.getY());
				
				drawShape();
				hasDragged = true;
				
			}
		};
	}
	
	/**
	 * Transforme le calque temporaire en un vrai calque ajouté à l'historique et recrée un nouveau calque temporaire pour le dessin de la prochaine forme
	 *
	 * @return l'événement à réaliser lorsque le clic de la souris est relâché
	 */
	@Override
	protected EventHandler<MouseEvent> createMouseReleasedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {

				Project.getInstance().getLayers().remove(shapeLayer);
				if(hasDragged) {
					shapeLayer = new Layer(shapeLayer, false);
					Project.getInstance().addLayer(shapeLayer);
					CallbackNewToolChanged(); // A verifier avec Loyse
					currentShapeSave.execute();

				}
			}
		};
	}
	
	/**
	 * Fonction abstraite de dessin de la forme sur le calque
	 */
	abstract protected void drawShape();
	
	/**
	 * Met à jour les paramètres de dessinb de la forme abstraite (position de départ, hauteur et largeur, en fonction de la position de la souris)
	 *
	 * @param endPosX, position courante de la souris
	 * @param endPosY, position courrant de la souris
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
}
