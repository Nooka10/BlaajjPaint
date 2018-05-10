/**
 * @file BucketFill
 * @authors Blaajj
 */

package controller.tools;

import controller.Project;
import controller.history.ICmd;
import controller.history.RecordCmd;
import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import utils.UndoException;

public class BucketFill extends Tool {
    private BucketFill instance = new BucketFill();
    private Fill currentFill;

    private BucketFill(){
        toolType = ToolType.BUCKETFILL;
    }

    @Override
    protected EventHandler<MouseEvent> createMousePressedEventHandlers() {
        return new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                currentFill = new Fill();
                
            }
        };
    }

    @Override
    protected EventHandler<MouseEvent> createMouseDraggedEventHandlers() {
        return null;
    }

    @Override
    protected EventHandler<MouseEvent> createMouseReleasedEventHandlers() {
        return null;
    }

    class Fill implements ICmd{
        private Image undosave;
        private Image redosave = null;
        private SnapshotParameters params;

        public Fill(){
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
