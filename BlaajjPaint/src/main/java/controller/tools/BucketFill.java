package controller.tools;

import controller.Layer;
import controller.Project;
import controller.history.ICmd;
import controller.history.RecordCmd;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import utils.UndoException;
import utils.Utils;

import java.util.HashSet;
import java.util.Stack;

/**
 * Classe implémentant l'outil <b>pot de peinture</b> permettant remplir une zone de la couleur actuellement sélectionnée dans le sélecteur de couleur.
 * Implémente le modèle Singleton.
 */
public class BucketFill extends Tool {
	private static BucketFill toolInstance  = null; // l'instance unique du singleton BucketFill
	private FillSave currentFill; // la sauvegarde du remplissage actuel
	
	/**
	 * Constructeur privé (modèle singleton).
	 */
	private BucketFill() {
		toolType = ToolType.BUCKETFILL;
	}
	
	/**
	 * Retourne l'instance unique du singleton BucketFill.
	 * @return l'instance unique du singleton BucketFill.
	 */
	public static BucketFill getInstance() {
		if (toolInstance  == null) {
			toolInstance  = new BucketFill();
		}
		return toolInstance ;
	}

	@Override
	protected EventHandler<MouseEvent> createMousePressedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				changeCursor(Cursor.WAIT); // change le curseur de la souris en mode "attente"
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
	protected EventHandler<MouseEvent> createMouseEnteredEventHandlers(){
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				Image img = new Image("/cursors/bucketFillCursor.png");
				changeCursor(new ImageCursor(img,0,0));
			}
		};
	}

	@Override
	protected EventHandler<MouseEvent> createMouseExitedEventHandlers(){
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				changeCursor(Cursor.DEFAULT); // remet le curseur par défaut
			}
		};
	}

	/**
	 * Gestion de l'événement du relachement du clique de la souris
	 * @return l'événement qui lorsque l'on relache le clique de la souris peint dans la zone du calque
	 */
	@Override
	protected EventHandler<MouseEvent> createMouseReleasedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			Stack<Point2D> stack; // stack des points qui devront être traités
			HashSet<Point2D> marked; // map des points déjà traités
			Layer layer; // calque sur lequel on doit dessiner (évite d'appeler constemment Project)
			
			@Override
			public void handle(MouseEvent event) {
				// set des attributs
				layer = Project.getInstance().getCurrentLayer();
				currentFill = new FillSave(); // crée une sauvegarde du remplissage
				stack = new Stack<>();
				marked = new HashSet<>();
				// récupération du PixelWriter -> permet d'écrire sur des pixel du graphicsContext
				PixelWriter pixelWriter = layer.getGraphicsContext2D().getPixelWriter();

				// récupération du PixelReader - permet de lire les pixels sur le graphicsContext
				WritableImage srcMask = new WritableImage((int) layer.getWidth(), (int) layer.getHeight());
				srcMask = Utils.makeSnapshot(layer, Color.TRANSPARENT, srcMask);
				PixelReader pixelReader = srcMask.getPixelReader();

				// couleur actuelle du pixel à colorer
				Color colorSelected = pixelReader.getColor((int) Math.round(event.getX()), (int) Math.round(event.getY()));
				// couleur dans laquelle on doit colorer le pixel
				Color currentColor = Project.getInstance().getCurrentColor();
				
				stack.push(new Point2D(event.getX(), event.getY())); // ajoute à la pile le point de départ de la zone à colorer

				// on parcours les pixels environnant le point de départ
				while (!stack.isEmpty()) {
					Point2D point = stack.pop();
					// on récupère de la position X et Y du point
					int x = (int) Math.round(point.getX());
					int y = (int) Math.round(point.getY());

					// teste si le pixel actuellement visité a la même couleur que le point de départ (et donc fait partie de la zone à colorer)
					if (!pixelReader.getColor(x, y).equals(colorSelected)) {
						continue; // si le pixel ne doit pas être coloré, on passe à l'itération suivante
					}
					
					pixelWriter.setColor(x, y, currentColor); // on colorie le pixel actuellement visité dans la couleur souhaitée
					
					marked.add(point); // on ajoute le point à la liste des points déjà traités

					// ajoute les points adjacents au point courant dans la pile des points à traiter
					addPointToStack(x - 1, y);
					addPointToStack(x, y - 1);
					addPointToStack(x, y + 1);
					addPointToStack(x + 1, y);
				}
				// la coloration est terminée
				Image img = new Image("/cursors/bucketFillCursor.png");
				changeCursor(new ImageCursor(img,0,0));
				currentFill.execute();
			}

			/**
			 * Ajoute un point dans la pile des points à traiter s'il n'a pas déjà été traité et qu'il n'est pas en dehors du calque.
			 * @param x, la position du point sur l'axe X.
			 * @param y, la position du point sur l'axe Y.
			 */
			private void addPointToStack(int x, int y) {
				Point2D point = new Point2D(x, y);
				if (marked.contains(point) || x < 0 || x >= layer.getWidth() || y < 0 || y >= layer.getHeight()) {
					return;
				}
				stack.push(point);
			}
		};
	}

	/**
	 * Classe interne implémentant une commande sauvegardant une utilisation du pot de peinture et définissant l'action à effectuer en cas d'appel à undo() ou redo()
	 * sur cette commande.
	 */
	private class FillSave implements ICmd {
		private Image undosave;
		private Image redosave = null;
		private Layer currentLayer;
		
		/**
		 * Construit une commande sauvegardant une utilisation du pot de peinture.
		 */
		private FillSave() {
			currentLayer = Project.getInstance().getCurrentLayer();
			undosave = Utils.makeSnapshot(currentLayer, Color.TRANSPARENT); // on fait un snapshot du calque actuellement sélectionné avant sa modification
		}
		
		@Override
		public void execute() {
			redosave = Utils.makeSnapshot(currentLayer, Color.TRANSPARENT); // on fait un snapshot du calque actuellement sélectionné après sa modification
			RecordCmd.getInstance().saveCmd(this);
		}
		
		@Override
		public void undo() throws UndoException {
			if (undosave == null) {
				throw new UndoException();
			}
			Utils.redrawSnapshot(currentLayer, undosave); // redessine le snapshot undosave sur le calque currentLayer
		}
		
		@Override
		public void redo() throws UndoException {
			if (redosave == null) {
				throw new UndoException();
			}
			Utils.redrawSnapshot(currentLayer, redosave); // redessine le snapshot redosave sur le calque currentLayer
		}
		
		@Override
		public String toString() {
			return "Remplissage de l'image";
		}
	}
}
