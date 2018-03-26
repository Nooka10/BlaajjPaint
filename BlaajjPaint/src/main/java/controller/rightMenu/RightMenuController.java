package controller.rightMenu;

import controller.MainViewController;

public class RightMenuController {
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
}
