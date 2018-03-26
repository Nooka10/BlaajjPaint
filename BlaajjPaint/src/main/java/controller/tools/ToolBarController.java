package controller.tools;

import controller.MainViewController;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;

/**
 * This class represents the tool bar controller which is responsible for handling the operations that can be done from the tool bar as geometric shapes drawing, moving,
 * resizing, deleting and so on.
 */
public class ToolBarController {
	
	public ToggleGroup ToolBarButtons;
	
	// Reference to the mainViewController
	private MainViewController mainViewController;
	
	/**
	 * Is called by the main application to give a reference back to itself.
	 *
	 * @param mainViewController
	 */
	public void setMainViewController(MainViewController mainViewController) {
		this.mainViewController = mainViewController;
	}
	
	/**
	 * Starts pencil sketching mode.
	 */
	@FXML
	private void pencilSketching() {
	
	}
	
	/**
	 * Starts brush sketching mode.
	 */
	@FXML
	private void brushSketching() {
	}
	
	/**
	 * Starts erasing mode.
	 */
	@FXML
	private void eraserTool() {
	}
	
	/**
	 * Draws a rectangle.
	 */
	@FXML
	private void drawRectangle() {
	}
	
	/**
	 * Draws a square.
	 */
	@FXML
	private void drawSquare() {
	}
	
	/**
	 * Draws a line segment.
	 */
	@FXML
	private void drawLine() {
	}
	
	/**
	 * Draws a triangle.
	 */
	@FXML
	private void drawTriangle() {
	}
	
	/**
	 * Starts dragging/moving mode.
	 */
	@FXML
	private void draggingTool() {
	}
	
	/**
	 * Starts resizing mode.
	 */
	@FXML
	private void resizingTool() {
	}
	
	/**
	 * Undoes an operation.
	 */
	@FXML
	private void undoTool() {
	}
	
	/**
	 * Redoes an operation.
	 */
	@FXML
	private void redoTool() {
	}
	
	/**
	 * Starts filling mode.
	 */
	@FXML
	private void fillingTool() {
	}
	
	/**
	 * Starts deleting mode.
	 */
	@FXML
	private void deletingTool() {
	}
}