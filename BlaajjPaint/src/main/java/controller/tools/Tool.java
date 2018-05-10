package controller.tools;


import controller.Project;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

/**
 * Classe abstraite implémentant un outil. Classe mère de tous les outils gérés
 */
public abstract class Tool {
	
	protected final EventHandler<MouseEvent> currentOnMousePressedEventHandler = createMousePressedEventHandlers(); // l'évènement déclanché lorsqu'on appuie sur le bouton de la la souris
	protected final EventHandler<MouseEvent> currentOnMouseDraggedEventHandler = createMouseDraggedEventHandlers(); // l'évènement déclanché lorsqu'on maintient le bouton de la souris enfoncé et qu'on la déplace
	protected final EventHandler<MouseEvent> currentOnMouseRelesedEventHandler = createMouseReleasedEventHandlers(); // l'évènement déclanché lorsqu'on relache le bouton de la souris
	
	public enum ToolType {PENCIL, ERASER, PIPETTE, ZOOM, MOVE, BUCKETFILL, OTHER} // énumération de tout les types d'outils gérés
	
	public static boolean toolHasChanged = false; // Vrai lorsqu'on vient de changer d'outil
	
	protected ToolType toolType = ToolType.OTHER; // le type de l'outil
	
	private static Tool currentTool; // l'outil actuellement sélectionné
	
	/**
	 * Retourne le ToolType de l'outil
	 * @return le ToolType de l'outil
	 */
	public ToolType getToolType() {
		return toolType;
	}
	
	/**
	 * Retourne l'outil actuellement sélectionné
	 * @return l'outil actuellement sélectionné
	 */
	public static Tool getCurrentTool() {
		return currentTool;
	}
	
	/**
	 * Remplace l'outil actuellement sélectionné par celui passé en paramètre
	 * @param currentTool, l'outil nouvellement sélectionné
	 */
	public static void setCurrentTool(Tool currentTool) {
		if(Tool.currentTool != currentTool){ // l'outil ne change que s'il n'est pas déjà l'outil sélectionné
			toolHasChanged = true;
			
			Project.getInstance().removeEventHandler(Tool.currentTool); // on enlève les EventHandler de l'outil actuellement sélectionné
			Tool.currentTool = currentTool;
			Project.getInstance().addEventHandlers(Tool.currentTool); // on met les EventHandler de l'outil nouvellement sélectionné
		}
	}
	
	/**
	 * Indique si l'outil sélectionné a changé
	 * @return true si un nouvel outil a été sélectionné, false sinon
	 */
	public static boolean getToolHasChanged(){
		return toolHasChanged;
	}
	
	/**
	 * Permet de modifier la valeur du booléen indiquant si l'outil sélectionné a changé dernièrement par la valeur passée en paramètre
	 * @param value, true si l'outil a été changé dernièrement, false sinon
	 */
	public static void setToolHasChanged(boolean value){
		toolHasChanged = value;
	}
	
	/**
	 * Retourne l'évènement déclanché par l'outil actuellement sélectionné lorsqu'on appuie sur le bouton de la la souris
	 * @return l'évènement déclanché par l'outil actuellement sélectionné lorsqu'on appuie sur le bouton de la la souris
	 */
	public EventHandler<MouseEvent> getCurrentOnMousePressedEventHandler() {
		return currentOnMousePressedEventHandler;
	}
	
	/**
	 * Retourne l'évènement déclanché par l'outil actuellement sélectionné lorsqu'on maintient le bouton de la souris enfoncé et qu'on la déplace
	 * @return l'évènement déclanché par l'outil actuellement sélectionné lorsqu'on maintient le bouton de la souris enfoncé et qu'on la déplace
	 */
	public EventHandler<MouseEvent> getCurrentOnMouseDraggedEventHandler() {
		return currentOnMouseDraggedEventHandler;
	}
	
	/**
	 * Retourne l'évènement déclanché par l'outil actuellement sélectionné lorsqu'on relâche le bouton de la la souris
	 * @return l'évènement déclanché par l'outil actuellement sélectionné lorsqu'on relâche le bouton de la la souris
	 */
	public EventHandler<MouseEvent> getCurrentOnMouseRelesedEventHandler() {
		return currentOnMouseRelesedEventHandler;
	}
	
	/**
	 * Crée l'évènement déclanché par cet outil lorsqu'on appuie sur le bouton de la la souris
	 * @return l'évènement déclanché par cet outil lorsqu'on appuie sur le bouton de la la souris
	 */
	protected abstract EventHandler<MouseEvent> createMousePressedEventHandlers();
	
	/**
	 * Crée l'évènement déclanché par cet outil lorsqu'on maintient le bouton de la souris enfoncé et qu'on la déplace
	 * @return l'évènement déclanché par cet outil lorsqu'on maintient le bouton de la souris enfoncé et qu'on la déplace
	 */
	protected abstract EventHandler<MouseEvent> createMouseDraggedEventHandlers();
	
	/**
	 * Crée l'évènement déclanché par cet outil lorsqu'on relâche le bouton de la la souris
	 * @return l'évènement déclanché par cet outil lorsqu'on relâche le bouton de la la souris
	 */
	protected abstract EventHandler<MouseEvent> createMouseReleasedEventHandlers();
}
