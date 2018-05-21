package main;

import controller.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;

/**
 * Classe principale du programme.
 */
public class Main extends Application {
	private Stage primaryStage;
	private BorderPane rootLayout;

	/**
	 * Initialise et affiche l'interface graphique.
	 * @param primaryStage, la scène principale
	 */
	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("BlaajjPaint");
		
		this.primaryStage.getIcons().add(new Image("/images/BlaajjPaint.jpg"));
		
		initRootLayout();
	}

	/**
	 * Initialise la fenêtre de base avec tous ses fxmls ainsi que tous les controllers associés.
	 */
	private void initRootLayout() {
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
		
		MainViewController.getInstance().setMain(this); // établit un lien entre le main et le mainViewController
		
		Scene scene = new Scene(rootLayout);
		primaryStage.setMaximized(true);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Permet de récupérer la scène principale.
	 * @return la scène principale.
	 */
	public Stage getPrimaryStage() {
		return primaryStage;
	}

	/**
	 * Fonction main. Lance l'exécution du programme.
	 * @param args - Arguments passés au programme.
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
