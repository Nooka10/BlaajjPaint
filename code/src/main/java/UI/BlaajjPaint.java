package UI;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class BlaajjPaint extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("BlaajjPaint");
        primaryStage.setMaximized(true);
        Group root = new Group();
        Scene scene = new Scene(root, Color.rgb(75, 75, 75));



        // À placer à la fin
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
