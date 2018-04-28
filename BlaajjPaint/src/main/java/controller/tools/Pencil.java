package controller.tools;

import controller.Project;
import controller.history.ICmd;
import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import controller.history.RecordCmd;
import utils.UndoException;

public class Pencil extends Tool implements ICmd {
	
	Image undosave;
	Image redosave = null;
	SnapshotParameters params;
	
	WritableImage pencil;
	
	EventHandler<MouseEvent> mousedrag;
	EventHandler<MouseEvent> mouserelease;
	EventHandler<MouseEvent> mousePressed;
	
	public Pencil(Canvas canvas) {
		// stock le cnaevas dans le parent
		super(canvas);
		
		// définit le pinceau qui sera utilisé par l'évènement de drag pour colorier le canvas
		pencil = new WritableImage(1, 1);
		pencil.getPixelWriter().setColor(0, 0, Color.BLACK);
		
		// configuration des paramètres utilisés pour la sauvegarde du canevas
		params = new SnapshotParameters();
		params.setFill(Color.TRANSPARENT);
		
		// exécute le snapshot de l'état actuel du canvas
		undosave = canvas.snapshot(params, null);
		
		// définit un handler qui est utilisé pour dessiner sur le canvas et l'ajoute au canvas
		mousedrag = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//canvas.getGraphicsContext2D().drawImage(pencil, event.getX(), event.getY());
				canvas.getGraphicsContext2D().lineTo(event.getX(), event.getY());
				canvas.getGraphicsContext2D().stroke();
			}
		};
		mousePressed = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				canvas.getGraphicsContext2D().beginPath();
				canvas.getGraphicsContext2D().moveTo(event.getX(), event.getY());
				canvas.getGraphicsContext2D().stroke();
			}
		};
		canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, mousedrag);
		canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, mousePressed);
		
		// definit un event qui est utilisé pour gérer le release du bouton de la souric sur le canvas
		mouserelease = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				execute();
				canvas.removeEventHandler(MouseEvent.MOUSE_DRAGGED, mousedrag);
				canvas.removeEventHandler(MouseEvent.MOUSE_RELEASED, mouserelease);
			}
		};
		canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, mouserelease);
	}
	
	@Override
	public void execute() {
		RecordCmd.getInstance().saveCmd(this);
		// RecordCmd.getInstance().clearRedo();
	}
	
	@Override
	public void undo() throws UndoException {
		if (undosave == null) {
			throw new UndoException();
		}
		redosave = Project.getInstance().getCurrentCanvas().snapshot(params, null);
		GraphicsContext gc = Project.getInstance().getCurrentCanvas().getGraphicsContext2D();
		gc.drawImage(undosave, 0, 0);
		undosave = null;

	}
	
	@Override
	public void redo() throws UndoException {
		if (redosave == null) {
			throw new UndoException();
		}
		undosave = Project.getInstance().getCurrentCanvas().snapshot(params, null);
		
		GraphicsContext gc = Project.getInstance().getCurrentCanvas().getGraphicsContext2D();
		gc.drawImage(redosave, 0, 0);
		redosave = null;
	}
	
	
}
