package main;

import controller.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

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
		try{
			FXMLLoader loader = new FXMLLoader();
			loader.setBuilderFactory(new JavaFXBuilderFactory());
			loader.setLocation(getClass().getResource("/view/MainView.fxml"));
			loader.setController(MainViewController.getInstance());
			InputStream fxmlStream = getClass().getResourceAsStream("/view/MainView.fxml");
			rootLayout = loader.load(fxmlStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		MainViewController.getInstance().setMain(this);
		
		// Show the scene containing the root layout.
		Scene scene = new Scene(rootLayout);
		primaryStage.setMaximized(true);
		primaryStage.setScene(scene);
		
		primaryStage.show();
	}
	
	public Stage getPrimaryStage() {
		return primaryStage;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
