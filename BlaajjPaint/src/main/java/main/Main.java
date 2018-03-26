package main;

import controller.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
	private Stage primaryStage;
	private BorderPane rootLayout;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("BlaajjPaint");
		
		// this.primaryStage.getIcons().add(new Image("file:resources/images/address_book_32.png")); // FIXME: permet d'ajouter une icone à l'application! :D
		
		initRootLayout();
	}
	
	/**
	 * Initialise la fenêtre de base avec tous ses fxml ainsi que tous les controllers associés.
	 */
	public void initRootLayout() {
		try {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("/view/mainView.fxml"));
			rootLayout = (BorderPane) loader.load();
			
			// Show the scene containing the root layout.
			Scene scene = new Scene(rootLayout);
			primaryStage.setMaximized(true);
			primaryStage.setScene(scene);
			
			// Give the mainViewController access to the main app.
			MainViewController mainViewController = loader.getController();
			
			mainViewController.setMain(this);
			
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
