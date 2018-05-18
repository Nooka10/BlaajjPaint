package controller.tools;

import controller.Layer;
import controller.MainViewController;
import controller.Project;
import controller.history.ICmd;
import controller.history.RecordCmd;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import utils.UndoException;
import utils.Utils;

/**
 * Classe implémentant l'outil <b>rogner</b> permettant de rogner un calque. Implémente le modèle Singleton.
 */
public class Crop extends Tool {
	private static Crop toolInstance = null; // l'instance unique du singleton Crop
	private CropSave cropSave; // la commande de sauvegarde du recadrage
	private Layer oldCurrentLayer;
	private Layer selectionCropLayer;
	private double startX;
	private double startY;
	private double posX;
	private double posY;
	
	/**
	 * Contructeur privé (modèle Singleton).
	 */
	private Crop() {
		toolType = ToolType.CROP;
	}
	
	/**
	 * Retourne l'instance unique du singleton Crop.
	 * @return l'instance unique du singleton Crop.
	 */
	public static Crop getInstance() {
		if (toolInstance == null) {
			toolInstance = new Crop();
		}
		return toolInstance;
	}
	
	/**
	 * Initialise un rognage en créant un calque temporaire affichant la zone actuellement sélectionnée par l'utilisateur.
	 */
	private void initCrop() {
		startX = 0;
		startY = 0;
		posX = 0;
		posY = 0;
		
		oldCurrentLayer = Project.getInstance().getCurrentLayer(); // sauvegarde le calque actuellement sélectionné. C'est lui qui sera rogné
		System.out.println(oldCurrentLayer + " " + oldCurrentLayer.getWidth() + " " + oldCurrentLayer.getHeight() );
		// Crée un calque temporaire utilisé pour afficher la sélection
		selectionCropLayer = new Layer((int) oldCurrentLayer.getWidth(), (int) oldCurrentLayer.getHeight(), true);
		selectionCropLayer.setTranslateX(oldCurrentLayer.getTranslateX());
		selectionCropLayer.setTranslateY(oldCurrentLayer.getTranslateY());
		
		Project.getInstance().setCurrentLayer(selectionCropLayer); // définit le calque temporaire comme calque courant
		Project.getInstance().addLayer(selectionCropLayer); // ajoute le calque temporaire au projet
	}
	
	/**
	 * Permet de quitter l'outil proprement et de réinitialiser l'outil Crop avant une nouvelle utilisation.
	 */
	private void reset() {
		if (selectionCropLayer != null) {
			Project.getInstance().getLayers().remove(selectionCropLayer); // Supprime le calque temporaire
			Project.getInstance().setCurrentLayer(oldCurrentLayer); // redéfinit le calque actuellement sélectionné de départ
			selectionCropLayer = null;
			oldCurrentLayer = null;
			MainViewController.getInstance().getRightMenuController().updateLayerList(); // redessine la liste des calques de la GUI
		}
		cropSave = null;
	}
	
	/**
	 * Annule le rognage en cours, supprime la sélection actuelle et prépare l'outil Crop pour une nouvelle utilisation.
	 */
	public void cancel() {
		reset(); // réinitialisation de l'outil Crop
		initCrop(); // Préparation à une nouvelle utilisation de l'outil Crop
	}
	
	/**
	 * Méthode appelée pour effectuer le rognage du calque selon la sélection effectuée par l'utilisateur à l'aide de la souris.
	 */
	public void validate() {
		if (cropSave != null) {
			oldCurrentLayer.crop(startX, startY, posX, posY); // rogne le calque
			
			cropSave.execute();
			cancel(); // réinitialise l'outil de rognage afin de pouvoir réutiliser l'outil Crop directement.
		}
	}
	
	/**
	 * Dessine le rectangle de sélection représentant les bordure du calque tel qu'elles seront après le rognage.
	 */
	private void drawRectOnLayer() {
		if (cropSave != null) { // vrai si la sélection est initialisée
			double width = Math.abs(startX - posX);
			double height = Math.abs(startY - posY);
			double x = startX <= posX ? startX : posX;
			double y = startY <= posY ? startY : posY;
			GraphicsContext gc = selectionCropLayer.getGraphicsContext2D();
			gc.clearRect(0, 0, selectionCropLayer.getWidth(), selectionCropLayer.getHeight()); // on efface le calque temporaire
			gc.setStroke(Color.BLUE);
			gc.strokeRect(x, y, width, height); // on redessine le rectangle
		}
	}
	
	@Override
	public void CallbackOldToolChanged() {
		super.CallbackOldToolChanged();
		reset(); // on change d'outil -> reset le Crop
	}
	
	@Override
	public void CallbackNewToolChanged() {
		super.CallbackNewToolChanged();
		initCrop(); // initialise l'outil Crop pour qu'il soit opérationnel pour le premier clic de souris
	}
	
	@Override
	protected EventHandler<MouseEvent> createMousePressedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (cropSave == null) {    // premier clic -> on set les valeurs de départ et on crée un calque temporaire qui affichera le rectangle de sélection
					if (selectionCropLayer == null) { // à initialisé si l'outil a été utilisé
						initCrop(); // FIXME: Est-ce vraiment utile??!
					}
					cropSave = new CropSave(oldCurrentLayer); // crée une sauvegarde du rognage
					startX = event.getX();
					startY = event.getY();
					posX = event.getX();
					posY = event.getY();
				} else {
					// détermine la position du clic
					if (Math.abs(posX - event.getX()) <= Math.abs(startX - event.getX())) {
						posX = event.getX();
					} else {
						startX = event.getX();
					}
					if (Math.abs(posY - event.getY()) <= Math.abs(startY - event.getY())) {
						posY = event.getY();
					} else {
						startY = event.getY();
					}
				}
				
				drawRectOnLayer(); // dessine le rectangle de sélection
			}
		};
	}
	
	@Override
	protected EventHandler<MouseEvent> createMouseDraggedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// détermine la position du clic
				if (Math.abs(posX - event.getX()) <= Math.abs(startX - event.getX())) {
					posX = event.getX();
				} else {
					startX = event.getX();
				}
				if (Math.abs(posY - event.getY()) <= Math.abs(startY - event.getY())) {
					posY = event.getY();
				} else {
					startY = event.getY();
				}
				
				drawRectOnLayer(); // dessine le rectangle de sélection
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
	
	/**
	 * Classe interne implémentant une commande sauvegardant le rognage d'un calque et définissant l'action à effectuer en cas d'appel à undo() ou redo() sur
	 * cette commande.
	 */
	class CropSave implements ICmd {
		private Image undosave;
		private Image redosave = null;
		private double widthLayer;
		private double heightLayer;
		private double translateXLayer;
		private double translateYLayer;
		private Layer layerCropped;
		
		/**
		 * Construit une commande sauvegardant le rognage d'un calque.
		 * @param layerToCrop, le calque que l'on souhaite rogner.
		 */
		public CropSave(Layer layerToCrop) {
			undosave = Utils.makeSnapshot(layerToCrop, Color.TRANSPARENT);
			widthLayer = layerToCrop.getWidth();
			heightLayer = layerToCrop.getHeight();
			translateXLayer = layerToCrop.getTranslateX();
			translateYLayer = layerToCrop.getTranslateY();
			layerCropped = layerToCrop;
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
			redosave = Utils.makeSnapshot(layerCropped, Color.TRANSPARENT); // on fait un snapshot du calque après sa modification
			Layer currentLayer = layerCropped; // sauvegarde le calque sur lequel on souhaite annuler le rognage
			
			// sauvegarde les dimensions de ce calque
			double widthTemp = currentLayer.getWidth();
			double heightTemp = currentLayer.getHeight();
			double translateXTemp = currentLayer.getTranslateX();
			double translateYTemp = currentLayer.getTranslateY();
			
			// redéfinit les dimensions et le décalage tel qu'ils étaient définit avant le Crop
			layerCropped.setWidth(widthLayer);
			layerCropped.setHeight(heightLayer);
			layerCropped.setTranslateX(translateXLayer);
			layerCropped.setTranslateY(translateYLayer);
			
			Utils.redrawSnapshot(layerCropped, undosave); // redessine le snapshot undosave sur le calque layerCropped
			
			// FIXME: tu fais quoi là?
			// définit les dimensions et le décalage tel qu'ils étaient définit après le Crop
			widthLayer = widthTemp;
			heightLayer = heightTemp;
			translateXLayer = translateXTemp;
			translateYLayer = translateYTemp;
			
			undosave = null;
		}
		
		@Override
		public void redo() throws UndoException {
			if (redosave == null) {
				throw new UndoException();
			}
			
			undosave = Utils.makeSnapshot(layerCropped, Color.TRANSPARENT); // on fait un snapshot du calque avant sa modification
			Layer currentLayer = layerCropped; // sauvegarde le calque que l'on va rogner
			
			// sauvegarde des dimensions de ce calque
			double widthTemp = currentLayer.getWidth();
			double heightTemp = currentLayer.getHeight();
			double translateXTemp = currentLayer.getTranslateX();
			double translateYTemp = currentLayer.getTranslateY();
			
			layerCropped.getGraphicsContext2D().clearRect(0, 0, layerCropped.getWidth(), layerCropped.getHeight()); // efface le calque
			
			// redéfinit les dimensions et le décalage tel qu'ils étaient définit avant le Crop
			layerCropped.setWidth(widthLayer);
			layerCropped.setHeight(heightLayer);
			//layerCropped.setLayoutX(translateXLayer);
			//layerCropped.setLayoutY(translateYLayer);
			layerCropped.setTranslateX(translateXLayer);
			layerCropped.setTranslateY(translateYLayer);
			
			//FIXME: Voir avec James si ya moyen d'utiliser Utils.redrawSnapshot()
			
			layerCropped.getGraphicsContext2D().drawImage(redosave, 0, 0); // redessine le snapshot redosave sur le calque layerCropped
			
			// FIXME: tu fais quoi là?
			// définit les dimensions et le décalage tel qu'ils étaient définit avant le Crop
			widthLayer = widthTemp;
			heightLayer = heightTemp;
			translateXLayer = translateXTemp;
			translateYLayer = translateYTemp;
			redosave = null;
		}
		
		@Override
		public String toString() {
			return "Rognage du " + oldCurrentLayer;
		}
	}
}
