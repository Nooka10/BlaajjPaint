package controller.tools;

import controller.Layer;
import controller.MainViewController;
import controller.Project;
import controller.history.ICmd;
import controller.history.RecordCmd;
import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import utils.SnapshotMaker;
import utils.UndoException;

/**
 * Outil permettant de mettre du text dans l'image
 */
public class TextTool extends Tool {
	private static TextTool toolInstance = null;
	private AddText addText;
	
	private Font font;
	private Layer textLayer;
	private Layer oldCurrentLayer;
	private String text;
	private int x;
	private int y;
	
	/**
	 * Constructeur privé de l'objet pour rajouter du text à l'image
	 */
	private TextTool() {
		toolType = ToolType.TEXT;
	}
	
	/**
	 * Retourne l'instance de l'objet - Singleton
	 *
	 * @return l'instance de l'objet
	 */
	public static TextTool getInstance() {
		if (toolInstance == null) {
			toolInstance = new TextTool();
		}
		return toolInstance;
	}
	
	/**
	 * Setter permettant de changer la police du text à ajouter
	 *
	 * @param font
	 * 		- Police d'écriture de Javafx
	 */
	public void setFont(Font font) {
		this.font = font;
		changeTextOnLayer();
	}
	
	/**
	 * Fonction appeler lorsque la personne valide la création du calque ou change d'outil
	 */
	public void validate() {
		// Test si l'outil est en cours d'édition
		if (addText != null) {
			Layer newLayer = new Layer(textLayer, false); // Devient un calque non-temporaire
			Project.getInstance().addLayer(newLayer);
			MainViewController.getInstance().getRightMenuController().updateLayerList();
			Project.getInstance().drawWorkspace();
			cancel();
			Project.getInstance().setCurrentLayer(newLayer);
		}
	}
	
	/**
	 * Change la valeur du text
	 *
	 * @param text
	 * 		- text à afficher
	 */
	public void changeTextValue(String text) {
		this.text = text;
		changeTextOnLayer();
	}
	
	/**
	 * refresh ou affiche le text avec la font, text et position donné précédement
	 */
	private void changeTextOnLayer() {
		// Test si la personne commencé l'ajout du text (nécessite le clique sur le calque)
		if (addText != null) {
			GraphicsContext graphics = textLayer.getGraphicsContext2D(); // récupération du graphics context
			// Nettoyage du calque (permet de déplacer le text)
			graphics.clearRect(0, 0, textLayer.getWidth(), textLayer.getWidth());
			graphics.setFont(font); // changement de la police d'écriture
			graphics.fillText(text, x, y); // positionnement et ajout du text
		}
	}
	
	/**
	 * Annulation de l'ajout du text
	 */
	public void cancel() {
		if(textLayer != null){
			reset();
			initTextTool();
		}
	}

	public void reset(){
		// Si le calque temporaire est initialisé, remettre dans l'état inital
		if(textLayer != null){
			// Suppression du calque d'ajout de text (textLayer)
			Project.getInstance().getLayers().remove(textLayer);
			// Le calque courant redevient l'ancien calque courant
			Project.getInstance().setCurrentLayer(oldCurrentLayer);
			// redessine les layers et list de layers
			MainViewController.getInstance().getRightMenuController().updateLayerList();
			Project.getInstance().drawWorkspace();
			textLayer = null;
			// reset des attributs
			addText = null;
			text = "";
		}
	}

	
	/**
	 * Si outil quitté, valide l'ajout du text si l'uilisateur à au moins cliquer sur l'interface pour rajouter le text,
	 * autrement annule l'ajout du text (suppression du calque)
	 */
	@Override
	public void CallbackOldToolChanged() {
		super.CallbackOldToolChanged();
		reset();
	}

	@Override
	public void CallbackNewToolChanged(){
		super.CallbackNewToolChanged();
		initTextTool();
	}

	public void initTextTool(){
		oldCurrentLayer = Project.getInstance().getCurrentLayer();
		textLayer = new Layer(Project.getInstance().getWidth(), Project.getInstance().getHeight(), "Texte", true);
		textLayer.setVisible(true);
		Project.getInstance().setCurrentLayer(textLayer);
		Project.getInstance().getLayers().addFirst(textLayer);
		Project.getInstance().drawWorkspace();
	}
	
	@Override
	protected EventHandler<MouseEvent> createMousePressedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// premier clique - set des valeurs et création du calque
				if (addText == null) {
					if(textLayer == null){
						initTextTool();
					}
					// Si le text est vide, mettre un text pour voir ou va être situé le text
					if (text == null || text.equals("")) {
						text = "Text";
					}
					addText = new AddText();

				}
				// récupération de la position du clique
				x = (int) event.getX();
				y = (int) event.getY();
				
				changeTextOnLayer(); // affichage du text sur le layer
			}
		};
	}
	
	@Override
	protected EventHandler<MouseEvent> createMouseDraggedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				/** Déplace le text sur le layer **/
				x = (int) event.getX();
				y = (int) event.getY();
				changeTextOnLayer();
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
	
	class AddText extends ICmd {
		private Image undosave;
		private Image redosave = null;
		private SnapshotParameters params;
		
		public AddText() {
			params = new SnapshotParameters();
			params.setFill(Color.TRANSPARENT);
			
			this.undosave = SnapshotMaker.makeSnapshot(Project.getInstance().getCurrentLayer());
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
			redosave = SnapshotMaker.makeSnapshot(Project.getInstance().getCurrentLayer());
			Project.getInstance().getCurrentLayer().getGraphicsContext2D().drawImage(undosave, 0, 0);
			undosave = null;
		}
		
		@Override
		public void redo() throws UndoException {
			if (redosave == null) {
				throw new UndoException();
			}
			undosave = SnapshotMaker.makeSnapshot(Project.getInstance().getCurrentLayer());
			Project.getInstance().getCurrentLayer().getGraphicsContext2D().drawImage(redosave, 0, 0);
			redosave = null;
		}
		
		public String toString() {
			return "Ajout de texte";
		}
	}
}
