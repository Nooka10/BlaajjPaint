package controller.tools;

import controller.Project;
import controller.history.ICmd;
import controller.history.RecordCmd;
import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import utils.UndoException;

public class Pencil extends Tool implements ICmd {
	
	private static int id; // FIXME: à virer -> juste pour tests
	private int realid; // FIXME: à virer -> juste pour tests
	private Image undosave;
	private Image redosave = null;
	private SnapshotParameters params;
	
	private EventHandler<MouseEvent> mousedrag;
	private EventHandler<MouseEvent> mouserelease;
	private EventHandler<MouseEvent> mousePressed;
	
	public Pencil(Canvas canvas) {
		// stock le cnaevas dans le parent
		super(canvas);
		
		realid = id++;
		System.out.println("create : " + realid);
		
		toolType = ToolType.PENCIL;
		
		// définit le pinceau qui sera utilisé par l'évènement de drag pour colorier le canvas
		// handlePencil = new WritableImage(1, 1); //FIXME: permet de définir un pinceau (taille, couleur etc) mais lag énromément au drag...
		// handlePencil.getPixelWriter().setColor(0, 0, Color.BLACK);
		
		// configuration des paramètres utilisés pour la sauvegarde du canevas
		params = new SnapshotParameters();
		params.setFill(Color.TRANSPARENT);
		
		// exécute le snapshot de l'état actuel du canvas
		undosave = canvas.snapshot(params, null);
		
		// définit un handler qui est utilisé pour dessiner sur le canvas et l'ajoute au canvas
		mousedrag = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//canvas.getGraphicsContext2D().drawImage(handlePencil, event.getX(), event.getY()); //FIXME: permet d'utiliser le handlePencil définit plus haut
				canvas.getGraphicsContext2D().lineTo(event.getX(), event.getY());
				canvas.getGraphicsContext2D().setLineWidth(1); // définit l'épaisseur du handlePencil
				canvas.getGraphicsContext2D().setStroke(Color.BLACK); // définit la couleur du handlePencil
				canvas.getGraphicsContext2D().stroke();
				System.out.println("drag : " + realid);
			}
		};
		canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, mousedrag);
		
		mousePressed = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				canvas.getGraphicsContext2D().beginPath();
				canvas.getGraphicsContext2D().moveTo(event.getX(), event.getY());
				canvas.getGraphicsContext2D().setLineWidth(1); // définit l'épaisseur du handlePencil
				canvas.getGraphicsContext2D().setStroke(Color.BLACK); // définit la couleur du handlePencil
				canvas.getGraphicsContext2D().stroke();
				System.out.println("pressed : " + realid);
			}
		};
		canvas.addEventHandler(MouseEvent.MOUSE_PRESSED, mousePressed);
		
		// definit un event qui est utilisé pour gérer le release du bouton de la souric sur le canvas
		mouserelease = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				System.out.println("release : " + realid);
				execute();
				canvas.removeEventHandler(MouseEvent.MOUSE_DRAGGED, mousedrag);
				canvas.removeEventHandler(MouseEvent.MOUSE_PRESSED, mousePressed);
				canvas.removeEventHandler(MouseEvent.MOUSE_RELEASED, mouserelease);
			}
		};
		canvas.addEventHandler(MouseEvent.MOUSE_RELEASED, mouserelease);
	}
	
	@Override
	public void unregisterEventHandlers() {
		getCanvas().removeEventHandler(MouseEvent.MOUSE_DRAGGED, mousedrag);
		getCanvas().removeEventHandler(MouseEvent.MOUSE_PRESSED, mousePressed);
		getCanvas().removeEventHandler(MouseEvent.MOUSE_RELEASED, mouserelease);
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
