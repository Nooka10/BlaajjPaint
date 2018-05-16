package controller.tools;

import controller.Layer;
import controller.Project;
import controller.history.ICmd;
import controller.history.RecordCmd;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import utils.SnapshotMaker;
import utils.UndoException;

import java.util.HashSet;
import java.util.Stack;

/**
 * Outil permettant de remplir une zone coloré de meme couleur dans une autre couleur
 */
public class BucketFill extends Tool {
	/** ATTRIBUTS **/
	private static BucketFill toolInstance  = null;
	private FillSave currentFill;

	/**
	 * Constructeur privé car Singleton
	 */
	private BucketFill() {
		toolType = ToolType.BUCKETFILL;
	}

	/**
	 * Retourne l'instance du Singleton
	 * @return l'instance du Singleton
	 */
	public static BucketFill getInstance() {
		if (toolInstance  == null) {
			toolInstance  = new BucketFill();
		}
		return toolInstance ;
	}

	/**
	 * Gestion de l'événement du "l'appuie" de la souris
	 * @return l'événement qui dois être réalisé lorsque l'on clique lorsque le BucketFill est séléctionné
	 */
	@Override
	protected EventHandler<MouseEvent> createMousePressedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				changeCursor(Cursor.WAIT); // Change le curseur en mode "attente"
			}
		};
	}

	/**
	 * Gestion de l'événement du "dragg" de la souris
	 * @return ne fait rien dans le cas du "BucketFill"
	 */
	@Override
	protected EventHandler<MouseEvent> createMouseDraggedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// Ne fait rien
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
			
			/** ATTRIBUTS DE L'EVENEMENT **/
			Stack<Point2D> stack; // Stack des points qui devront être traiter
			HashSet<Point2D> marked; // Map des points traité
			Layer layer; // Layer sur lequel on doit dessiner (Evite l'appelle constant à Project
			
			@Override
			public void handle(MouseEvent event) {
				// Set des attributs
				layer = Project.getInstance().getCurrentLayer();
				currentFill = new FillSave();
				stack = new Stack<>();
				marked = new HashSet<>();

				// Récupération du PixelWriter - permet d'écrire sur des pixel du graphics context
				PixelWriter pixelWriter = layer.getGraphicsContext2D().getPixelWriter();

				// Récupération du PixelReader - permet de lire les pixels sur le graphics context
				WritableImage srcMask = new WritableImage((int) layer.getWidth(), (int) layer.getHeight());
				srcMask = layer.snapshot(null, srcMask);
				PixelReader pixelReader = srcMask.getPixelReader();

				// Couleur de l'élément/zone à colorer
				Color colorSelected = pixelReader.getColor((int) Math.round(event.getX()), (int) Math.round(event.getY()));
				// Couleur qu'on doit coloré l'élément/zone
				Color currentColor = Project.getInstance().getCurrentColor();
				
				stack.push(new Point2D(event.getX(), event.getY())); // Set de l'élément de base de notre parcours

				// Parcours des pixel environnant le point de départ
				while (!stack.isEmpty()) {
					Point2D point = stack.pop();
					// Récupération de la position du point (moins d'accès au fonction)
					int x = (int) Math.round(point.getX());
					int y = (int) Math.round(point.getY());

					// Test si le pixel courant à la meme couleur que le point de départ (fait partie de la forme
					if ((!pixelReader.getColor(x, y).equals(colorSelected))) {
						continue;
					}
					
					pixelWriter.setColor(x, y, currentColor); // mise en couleur du pixel en cours de traitement
					
					marked.add(point); // Marquer le point comme traité

					// Ajout des points adjacent le point courant dans la liste de traitement
					addPointToStack(x - 1, y - 1);
					addPointToStack(x - 1, y);
					addPointToStack(x - 1, y + 1);
					addPointToStack(x, y - 1);
					addPointToStack(x, y + 1);
					addPointToStack(x + 1, y - 1);
					addPointToStack(x + 1, y);
					addPointToStack(x + 1, y + 1);
				}
				// Fin de la coloration - remise en place du curseur et execution de la commande
				resetOldCursor();
				currentFill.execute();
			}

			/**
			 * Ajout d'un point dans la stack de traitement
			 * @param x - position horizontal du point
			 * @param y - position vertical du point
			 * @brief Permet de faire des verification si le point et toujours dans l'image et si il a pas déjà été traité
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
	 * Commande pour remplir une forme - utile pour le undo redo
	 */
	private class FillSave extends ICmd {
		private Image undosave;
		private Image redosave = null;
		private Layer currentLayer;
		
		private FillSave() {
			currentLayer = Project.getInstance().getCurrentLayer();
			undosave = SnapshotMaker.makeSnapshot(currentLayer);
		}
		
		@Override
		public void execute() {
			redosave = SnapshotMaker.makeSnapshot(currentLayer);
			RecordCmd.getInstance().saveCmd(this);
		}
		
		@Override
		public void undo() throws UndoException {
			if (undosave == null) {
				throw new UndoException();
			}
			currentLayer.getGraphicsContext2D().clearRect(0,0, currentLayer.getWidth(), currentLayer.getHeight());
			currentLayer.getGraphicsContext2D().drawImage(undosave, 0, 0);
		}
		
		@Override
		public void redo() throws UndoException {
			if (redosave == null) {
				throw new UndoException();
			}
			currentLayer.getGraphicsContext2D().clearRect(0,0, currentLayer.getWidth(), currentLayer.getHeight());
			Project.getInstance().getCurrentLayer().getGraphicsContext2D().drawImage(redosave, 0, 0);
		}

		public String toString(){
			return "Remplissage de l'image";
		}
	}
}
