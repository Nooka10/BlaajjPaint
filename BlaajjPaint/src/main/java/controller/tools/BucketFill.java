/**
 * @file BucketFill
 * @authors Blaajj
 */

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
import utils.UndoException;

import java.util.HashSet;
import java.util.Stack;

public class BucketFill extends Tool {
	private static BucketFill instance = new BucketFill();
	private Fill currentFill;
	
	private BucketFill() {
		toolType = ToolType.BUCKETFILL;
	}
	
	public static BucketFill getInstance() {
		return instance;
	}
	
	@Override
	protected EventHandler<MouseEvent> createMousePressedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				changeCursor(Cursor.WAIT);
			}
		};
	}
	
	@Override
	protected EventHandler<MouseEvent> createMouseDraggedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// do nothing
			}
		};
	}
	
	@Override
	protected EventHandler<MouseEvent> createMouseReleasedEventHandlers() {
		return new EventHandler<MouseEvent>() {
			
			// ATTRIBUTS EVENTHANDLER BUCKETFILL
			Stack<Point2D> stack;
			HashSet<Point2D> marked;
			Layer layer;
			
			@Override
			public void handle(MouseEvent event) {
				layer = Project.getInstance().getCurrentLayer();
				currentFill = new Fill();
				stack = new Stack<>();
				marked = new HashSet<>();
				PixelWriter pixelWriter = layer.getGraphicsContext2D().getPixelWriter();
				// PixelReader
				WritableImage srcMask = new WritableImage((int) layer.getWidth(), (int) layer.getHeight());
				srcMask = layer.snapshot(null, srcMask);
				PixelReader pixelReader = srcMask.getPixelReader();
				
				Color colorSelected = pixelReader.getColor((int) Math.ceil(event.getX()), (int) Math.ceil(event.getY()));
				Color currentColor = Project.getInstance().getCurrentColor();
				
				stack.push(new Point2D(event.getX(), event.getY()));
				
				while (!stack.isEmpty()) {
					Point2D point = stack.pop();
					int x = (int) Math.ceil(point.getX());
					int y = (int) Math.ceil(point.getY());
					
					if ((!pixelReader.getColor(x, y).equals(colorSelected))) {
						continue;
					}
					
					pixelWriter.setColor(x, y, currentColor);
					
					marked.add(point);
					
					addPointToStack(x - 1, y - 1);
					addPointToStack(x - 1, y);
					addPointToStack(x - 1, y + 1);
					addPointToStack(x, y - 1);
					addPointToStack(x, y + 1);
					addPointToStack(x + 1, y - 1);
					addPointToStack(x + 1, y);
					addPointToStack(x + 1, y + 1);
				}
				resetOldCursor();
			}
			
			private void addPointToStack(int x, int y) {
				Point2D point = new Point2D(x, y);
				if (marked.contains(point) || x < 0 || x >= layer.getWidth() || y < 0 || y >= layer.getHeight()) {
					return;
				}
				stack.push(point);
				currentFill.execute();
			}
		};
	}
	
	class Fill implements ICmd {
		private Image undosave;
		private Image redosave = null;
		private SnapshotParameters params;
		
		public Fill() {
			params = new SnapshotParameters();
			params.setFill(Color.TRANSPARENT);
			
			this.undosave = Project.getInstance().getCurrentLayer().snapshot(params, null);
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
			redosave = Project.getInstance().getCurrentLayer().snapshot(params, null);
			Project.getInstance().getCurrentLayer().getGraphicsContext2D().drawImage(undosave, 0, 0);
			undosave = null;
		}
		
		@Override
		public void redo() throws UndoException {
			if (redosave == null) {
				throw new UndoException();
			}
			undosave = Project.getInstance().getCurrentLayer().snapshot(params, null);
			Project.getInstance().getCurrentLayer().getGraphicsContext2D().drawImage(redosave, 0, 0);
			redosave = null;
		}
	}
}
