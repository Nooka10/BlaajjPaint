package controller.tools;

import controller.Project;
import controller.history.ICmd;
import controller.history.RecordCmd;
import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import utils.UndoException;

public class Eraser extends ToolDrawer implements ICmd {
	
	private static Color transparent = new Color(0, 0, 0, 0);
	
	
	private static int id; // FIXME: à virer -> juste pour tests
	private int realid; // FIXME: à virer -> juste pour tests
	private Image undosave;
	private Image redosave = null;
	private SnapshotParameters params;
	
	private EventHandler<MouseEvent> mousedrag;
	private EventHandler<MouseEvent> mouserelease;
	private EventHandler<MouseEvent> mousePressed;

	public Eraser(Canvas canvas, double thickness, double opacity) {
		// stock le cnaevas dans le parent
		super(thickness, opacity);
		
		realid = id++;
		System.out.println("create : " + realid);
		
		toolType = ToolType.ERASER;
		
		// configuration des paramètres utilisés pour la sauvegarde du canevas
		params = new SnapshotParameters();
		params.setFill(Color.TRANSPARENT);
		
		// exécute le snapshot de l'état actuel du canvas
		undosave = canvas.snapshot(params, null);
		
		// définit un handler qui est utilisé pour dessiner sur le canvas et l'ajoute au canvas
		mousedrag = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				//canvas.getGraphicsContext2D().clearRect(event.getX(), event.getY(), thickness, thickness);
				canvas.getGraphicsContext2D().lineTo(event.getX(), event.getY());
				canvas.getGraphicsContext2D().setLineWidth(thickness); // définit l'épaisseur de la gomme
				Color c = new Color(Color.BLACK.getRed(), Color.BLACK.getGreen(), Color.BLACK.getBlue(), 0);
				canvas.getGraphicsContext2D().setStroke(c); // définit la couleur de la gomme
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
				canvas.getGraphicsContext2D().setLineWidth(thickness); // définit l'épaisseur de la gomme
				Color c = new Color(Color.BLACK.getRed(), Color.BLACK.getGreen(), Color.BLACK.getBlue(), 0);
				canvas.getGraphicsContext2D().setStroke(c); // définit la couleur de la gomme
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
	public void execute() {
		RecordCmd.getInstance().saveCmd(this);
		// RecordCmd.getCurrentTool().clearRedo();
	}
	
	@Override
	public void undo() throws UndoException {
		if (undosave == null) {
			throw new UndoException();
		}
		redosave = Project.getInstance().getCurrentLayer().snapshot(params, null);
		GraphicsContext gc = Project.getInstance().getCurrentLayer().getGraphicsContext2D();
		gc.drawImage(undosave, 0, 0);
		undosave = null;
		
	}
	
	@Override
	public void redo() throws UndoException {
		if (redosave == null) {
			throw new UndoException();
		}
		undosave = Project.getInstance().getCurrentLayer().snapshot(params, null);
		
		GraphicsContext gc = Project.getInstance().getCurrentLayer().getGraphicsContext2D();
		gc.drawImage(redosave, 0, 0);
		redosave = null;
	}

	@Override
	protected void onOpacitySet(){
	}

	@Override
	protected void onThicknessSet(){
	}
	
	@Override
	public EventHandler<MouseEvent> addMousePressedEventHandlers() {
		return null;
	}
	
	@Override
	public EventHandler<MouseEvent> addMouseDraggedEventHandlers() {
		return null;
	}
	
	@Override
	public EventHandler<MouseEvent> addMouseReleasedEventHandlers() {
		return null;
	}
}
