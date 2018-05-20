package controller.tools;


import controller.MainViewController;
import controller.Project;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

/**
 * Classe abstraite implémentant un outil et définissant toutes les propriétés communes à tous les outils. Classe mère de tous les outils gérés par le programme.
 */
public abstract class Tool {
	protected ToolType toolType = ToolType.MOVE; // le type de l'outil
	
	private static Tool currentTool; // l'outil actuellement sélectionné
	
	private static boolean toolHasChanged = false; // vrai lorsqu'on vient de changer d'outil
	
	private Cursor oldCursor;

	// l'évènement déclanché lorsqu'on appuie sur le bouton de la souris
	private final EventHandler<MouseEvent> currentOnMousePressedEventHandler = createMousePressedEventHandlers();
	// l'évènement déclanché lorsqu'on maintient le bouton de la souris enfoncé et qu'on la déplace
	private final EventHandler<MouseEvent> currentOnMouseDraggedEventHandler = createMouseDraggedEventHandlers();
	// l'évènement déclanché lorsqu'on relâche le bouton de la souris
	private final EventHandler<MouseEvent> currentOnMouseRelesedEventHandler = createMouseReleasedEventHandlers();
	// l'évènement déclanché lorsque la souris entre dans le calque
	private final EventHandler<MouseEvent> currentOnMouseEnteredEventHandler = createMouseEnteredEventHandlers();
	// l'évènement déclanché  lorsque la souris sort du calque
	private final EventHandler<MouseEvent> currentOnMouseExitedEventHandler = createMouseExitedEventHandlers();

	// énumération contenant tous les outils gérés par le programme
	protected enum ToolType {
		HAND, MOVE, CROP, PIPETTE, PENCIL, ERASER, BUCKETFILL, TEXT, FILLEDRECTANGLE,
		EMPTYRECTANGLE, FILLEDELLIPSE, EMPTYELLIPSE
	}
	
	/**
	 * Retourne l'outil actuellement sélectionné.
	 *
	 * @return l'outil actuellement sélectionné.
	 */
	public static Tool getCurrentTool() {
		return currentTool;
	}
	
	/**
	 * Appelé sur l'outil qui est sur le point d'être désélectionné.
	 */
	public abstract void CallbackOldToolChanged();
	
	/**
	 * Appelé sur l'outil qui vient d'être sélectionné.
	 */
	public abstract void CallbackNewToolChanged();
	
	/**
	 * Remplace l'outil actuellement sélectionné par celui passé en paramètre.
	 *
	 * @param currentTool,
	 * 		l'outil nouvellement sélectionné.
	 */
	public static void setCurrentTool(Tool currentTool) {
		if (Tool.currentTool != currentTool) { // l'outil ne change que s'il n'est pas déjà l'outil sélectionné
			if (Tool.currentTool != null) {
				// appelle la fonction de callback sur l'ancien outil pour le notifier qu'il n'est plus sélectionné
				Tool.currentTool.CallbackOldToolChanged();
				Project.getInstance().removeEventHandler(Tool.currentTool); // supprime les eventHandlers de l'outil qui n'est plus sélectionné
			}
			toolHasChanged = true;
			Tool.currentTool = currentTool; // définit le nouveau currentTool
			Project.getInstance().addEventHandlers(Tool.currentTool); // ajoute les eventHandler de l'outil nouvellement sélectionné
			
			// appelle la fonction de callback sur le nouvel outil pour le notifier qu'il a été sélectionné
			Tool.currentTool.CallbackNewToolChanged();
		}
	}
	
	/**
	 * Retourne vrai si l'outil sélectionné a changé dernièrement, false sinon.
	 *
	 * @return true si un nouvel outil a été sélectionné, false sinon.
	 */
	public static boolean getToolHasChanged() {
		return toolHasChanged;
	}
	
	/**
	 * Permet de modifier la valeur du booléen indiquant si l'outil sélectionné a changé dernièrement par la valeur passée en paramètre.
	 *
	 * @param value,
	 * 		true si l'outil a été changé dernièrement, false sinon.
	 */
	public static void setToolHasChanged(boolean value) {
		toolHasChanged = value;
	}
	
	/**
	 * Retourne l'évènement déclanché par l'outil actuellement sélectionné lorsqu'on appuie sur le bouton de la la souris.
	 *
	 * @return l'évènement déclanché par l'outil actuellement sélectionné lorsqu'on appuie sur le bouton de la la souris.
	 */
	public EventHandler<MouseEvent> getCurrentOnMousePressedEventHandler() {
		return currentOnMousePressedEventHandler;
	}
	
	/**
	 * Retourne l'évènement déclanché par l'outil actuellement sélectionné lorsqu'on maintient le bouton de la souris enfoncé et qu'on la déplace.
	 *
	 * @return l'évènement déclanché par l'outil actuellement sélectionné lorsqu'on maintient le bouton de la souris enfoncé et qu'on la déplace.
	 */
	public EventHandler<MouseEvent> getCurrentOnMouseDraggedEventHandler() {
		return currentOnMouseDraggedEventHandler;
	}
	
	/**
	 * Retourne l'évènement déclanché par l'outil actuellement sélectionné lorsqu'on relâche le bouton de la la souris.
	 *
	 * @return l'évènement déclanché par l'outil actuellement sélectionné lorsqu'on relâche le bouton de la la souris.
	 */
	public EventHandler<MouseEvent> getCurrentOnMouseRelesedEventHandler() {
		return currentOnMouseRelesedEventHandler;
	}

	/**
	 * Retourne l'évènement déclanché par l'outil actuellement sélectionné lorsque la souris entre dans le calque.
	 *
	 * @return l'évènement déclanché par l'outil actuellement sélectionné lorsque la souris entre dans le calque.
	 */
	public EventHandler<MouseEvent> getCurrentOnMouseEnteredEventHandler() {
		return currentOnMouseEnteredEventHandler;
	}

	/**
	 * Retourne l'évènement déclanché par l'outil actuellement sélectionné lorsque la souris sort du calque.
	 *
	 * @return l'évènement déclanché par l'outil actuellement sélectionné lorsque la souris sort du calque.
	 */
	public EventHandler<MouseEvent> getCurrentOnMouseExitedEventHandler(){
		return currentOnMouseExitedEventHandler;
	}
	
	/**
	 * Crée l'évènement déclanché par cet outil lorsqu'on appuie sur le bouton de la la souris.
	 *
	 * @return l'évènement déclanché par cet outil lorsqu'on appuie sur le bouton de la la souris.
	 */
	protected abstract EventHandler<MouseEvent> createMousePressedEventHandlers();
	
	/**
	 * Crée l'évènement déclanché par cet outil lorsqu'on maintient le bouton de la souris enfoncé et qu'on la déplace.
	 *
	 * @return l'évènement déclanché par cet outil lorsqu'on maintient le bouton de la souris enfoncé et qu'on la déplace.
	 */
	protected abstract EventHandler<MouseEvent> createMouseDraggedEventHandlers();
	
	/**
	 * Crée l'évènement déclanché par cet outil lorsqu'on relâche le bouton de la la souris.
	 *
	 * @return l'évènement déclanché par cet outil lorsqu'on relâche le bouton de la la souris.
	 */
	protected abstract EventHandler<MouseEvent> createMouseReleasedEventHandlers();
	
	/**
	 * Crée l'évènement déclanché par cet outil lorsque la souris entre dans le calque.
	 *
	 * @return l'évènement déclanché par cet outil lorsque la souris entre dans le calque.
	 */
	protected EventHandler<MouseEvent> createMouseEnteredEventHandlers(){
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// ne fait rien
			}
		};
	}

	/**
	 *  Crée l'évènement déclanché par cet outil lorsque la souris sort du calque.
	 *
	 * @return l'évènement déclanché par cet outil lorsque la souris sort du calque.
	 */
	protected EventHandler<MouseEvent> createMouseExitedEventHandlers(){
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// ne fait rien
			}
		};
	}
	/**
	 * Remplace le curseur de la souris par celui passé en paramètre.
	 * @param cursor, le nouveau curseur à attribuer à la souris.
	 */
	protected void changeCursor(Cursor cursor) {
		Scene scene = MainViewController.getInstance().getMain().getPrimaryStage().getScene();
		oldCursor = scene.getCursor();
		scene.setCursor(cursor);
	}
	
	/**
	 * Permet de redonner l'apparence d'origine au curseur de la souris.
	 */
	protected void resetPreviousCursor() {
		Scene scene = MainViewController.getInstance().getMain().getPrimaryStage().getScene();
		scene.setCursor(oldCursor);
	}
}
