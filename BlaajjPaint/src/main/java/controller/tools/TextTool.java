package controller.tools;

import controller.Layer;
import controller.MainViewController;
import controller.Project;
import controller.history.ICmd;
import controller.history.RecordCmd;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Classe implémentant l'outil <b>Texte</b> permettant d'ajouter un calque contenant du texte de la taille, la police et la couleur choisi par l'utilisateur. Implémente
 * le modèle Singleton.
 */
public class TextTool extends Tool {
	private static TextTool toolInstance = null;
	private textSave textSave;
	private Font font;
	private Layer textLayer;
	private Layer oldCurrentLayer;
	private String text;
	private int x;
	private int y;
	
	/**
	 * Constructeur privé (modèle singleton).
	 */
	private TextTool() {
		toolType = ToolType.TEXT;
		// ajoute un listener modifiant la couleur du texte lorsqu'on change la couleur sélectionnée dans le sélecteur de couleur
		MainViewController.getInstance().getRightMenuController().getColorPicker().setOnHiding(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				changeTextOnLayer();
			}
		});
	}
	
	/**
	 * Retourne l'instance unique du singleton TexteTool.
	 *
	 * @return l'instance unique du singleton TextTool.
	 */
	public static TextTool getInstance() {
		if (toolInstance == null) {
			toolInstance = new TextTool();
		}
		return toolInstance;
	}
	
	/**
	 * Définit la police utilisée pour dessiner le texte.
	 *
	 * @param font, la nouvelle police à utiliser pour dessiner le texte.
	 */
	public void setFont(Font font) {
		this.font = font;
		changeTextOnLayer();
	}
	
	/**
	 * Initialise l'outil texte en créant un calque temporaire affichant le texte à l'endroit définit par l'utilisateur.
	 */
	public void initTextTool() {
		oldCurrentLayer = Project.getInstance().getCurrentLayer();
		
		// crée un calque temporaire utilisé pour afficher le texte à la position choisie par l'utilisateur
		textLayer = new Layer(Project.getInstance().getWidth(), Project.getInstance().getHeight(), "Texte", true);
		
		Project.getInstance().addLayer(textLayer);
		MainViewController.getInstance().getRightMenuController().disableLayerListClick(); // désactive les clics de la souris dans la liste de calque
	}
	
	/**
	 * Valide la création du calque de texte.
	 */
	public void validate() {
		if (textSave != null) { // vrai si l'outil est en cours d'utilisation
			textSave.execute();
			Layer newLayer = new Layer(textLayer, false); // crée un calque non-temporaire
			textSave.setLayerToSaved(newLayer); // définit le calque sur le lequel revenir en cas de undo
			reset();
			Project.getInstance().addLayer(newLayer);
			MainViewController.getInstance().getRightMenuController().updateLayerList();
			initTextTool();
		}
	}
	
	/**
	 * Annule la création du calque de texte.
	 */
	public void cancel() {
		if (textLayer != null) {
			reset();
			initTextTool();
		}
	}
	
	/**
	 * Réinitialise l'outil texte.
	 */
	public void reset() {
		Project.getInstance().getLayers().remove(textLayer); // supprime le calque temporaire d'ajout de text (textLayer)
		Project.getInstance().setCurrentLayer(oldCurrentLayer); // redéfinit l'ancien calque comme calque courant
		
		MainViewController.getInstance().getRightMenuController().updateLayerList(); // redessine la liste de calques
		MainViewController.getInstance().getRightMenuController().activateLayerListClick(); // réactive les clics de la souris dans la liste de calque
		
		// reset des attributs
		textLayer = null;
		oldCurrentLayer = null;
		textSave = null;
		text = "";
	}
	
	/**
	 * Modifie le texte écrit sur le calque temporaire.
	 *
	 * @param text
	 * 		- text à afficher
	 */
	public void changeTextValue(String text) {
		this.text = text;
		changeTextOnLayer();
	}
	
	/**
	 * Affiche (ou met à jour) le texte avec la police, la couleur et la position définies par l'utilisateur.
	 */
	private void changeTextOnLayer() {
		if (textSave != null) { // vrai si l'utilisateur a commencé à utiliser l'outil TextTool
			GraphicsContext graphics = textLayer.getGraphicsContext2D(); // récupère le graphicsContext
			
			graphics.clearRect(0, 0, textLayer.getWidth(), textLayer.getWidth()); // efface le calque
			graphics.setFont(font); // modifie la police d'écriture
			graphics.setFill(Project.getInstance().getCurrentColor()); // définit la couleur du texte à écrire
			graphics.fillText(text, x, y); // écrit le texte à la position x et y donnée
		}
	}
	
	@Override
	public void CallbackOldToolChanged() {
		reset(); // l'outil a été changé -> on supprime le calque temporaire
	}
	
	@Override
	public void CallbackNewToolChanged() {
		initTextTool(); // initialise l'outil d'ajout de texte
	}
	
	@Override
	protected EventHandler<MouseEvent> createMousePressedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (textSave == null) { // vrai si premier clic -> crée un calque temporaire
					if (text == null || text.equals("")) { // vrai si le texte est vide -> affiche un texte par défaut pour pouvoir voir où va être écrit le texte
						text = "Texte";
					}
					textSave = new textSave(); // crée une sauvegarde du texte
				}
				// récupère la position du clic
				x = (int) event.getX();
				y = (int) event.getY();
				
				changeTextOnLayer(); // affiche le texte sur le layer
			}
		};
	}
	
	@Override
	protected EventHandler<MouseEvent> createMouseDraggedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// récupère la position du clic
				x = (int) event.getX();
				y = (int) event.getY();
				changeTextOnLayer(); // affiche le texte sur le layer
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
	protected  EventHandler<MouseEvent> createMouseEnteredEventHandlers(){
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				changeCursor(Cursor.TEXT);
			}
		};
	}

	@Override
	protected EventHandler<MouseEvent> createMouseExitedEventHandlers(){
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				resetOldCursor();
			}
		};
	}
	
	/**
	 * Classe interne implémentant une commande sauvegardant l'ajout d'un calque de texte et définissant l'action à effectuer en cas d'appel à undo() ou redo() sur
	 * cette commande.
	 */
	class textSave implements ICmd {
		private SnapshotParameters params;
		private Layer oldLayerSaved;
		private Layer textLayerSaved;
		
		/**
		 * Construit une commande sauvegardant l'ajout d'un calque de texte.
		 */
		private textSave() {
			params = new SnapshotParameters();
			params.setFill(Color.TRANSPARENT);
			oldLayerSaved = oldCurrentLayer;
		}
		
		/**
		 * Définit le calque à enregistrer dans la commande.
		 * @param layer, le calque à sauvegarder dans la commande.
		 */
		private void setLayerToSaved(Layer layer) {
			textLayerSaved = layer;
		}
		
		@Override
		public void execute() {
			RecordCmd.getInstance().saveCmd(this);
		}
		
		@Override
		public void undo() {
			Project.getInstance().getLayers().remove(textLayerSaved);
			Project.getInstance().setCurrentLayer(oldLayerSaved);
		}
		
		@Override
		public void redo() {
			Project.getInstance().addLayer(textLayerSaved);
		}
		
		@Override
		public String toString() {
			return "Ajout de texte";
		}
	}
}
