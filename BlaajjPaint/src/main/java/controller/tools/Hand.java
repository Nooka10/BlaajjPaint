/*
Author: Benoît
 */
package controller.tools;

import controller.MainViewController;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;

/**
 * Classe implémentant l'outil main permettant de se déplacer dans l'espace de travail (lorsque l'image est suffisemment grande pour activer le scrollPane).
 * Implémente le modèle Singleton.
 */
public class Hand extends Tool {
	
	private static Hand toolInstance; // l'instance unique du singleton Hand
	
	/**
	 * Constructeur privé (modèle singleton).
	 */
	private Hand() {
		toolType = ToolType.HAND;
	}
	
	/**
	 * Retourne l'instance unique du singleton Hand.
	 * @return l'instance unique du singleton Hand.
	 */
	public static Hand getInstance() {
		if (toolInstance == null) {
			toolInstance = new Hand();
		}
		return toolInstance;
	}
	
	
	@Override
	protected EventHandler<MouseEvent> createMousePressedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				MainViewController.getInstance().getScrollPane().setPannable(true); // active le déplacement dans le scrollPane à l'aide de la souris
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
	protected EventHandler<MouseEvent> createMouseReleasedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				MainViewController.getInstance().getScrollPane().setPannable(false); // désactive le déplacement dans le scrollPane à l'aide de la souris
			}
		};
	}
	
	@Override
	protected EventHandler<MouseEvent> createMouseEnteredEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				changeCursor(Cursor.HAND); // change le curseur de la souris en mode "main"
			}
		};
	}

	@Override
	protected EventHandler<MouseEvent> createMouseExitedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				resetPreviousCursor(); // remet le curseur précédent
			}
		};
	}
	
	@Override
	public void CallbackOldToolChanged() {
		MainViewController.getInstance().getToolBarController().handTool.setSelected(false);
	}
	
	@Override
	public void CallbackNewToolChanged() {
		MainViewController.getInstance().getToolBarController().handTool.setSelected(true);
	}
}
