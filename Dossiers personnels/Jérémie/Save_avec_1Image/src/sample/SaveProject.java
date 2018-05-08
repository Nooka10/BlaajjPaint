package sample;

import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;


import java.io.*;

public class SaveProject {

    private ProjectWrapper pw;

    public SaveProject(Canvas c){

        double height = c.getHeight(), weight = c.getWidth();
        pw = new ProjectWrapper(height, weight, generateImage(c, (int) height, (int) weight));
    }

    public void save(File f){


        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = new FileOutputStream(f);
            out = new ObjectOutputStream(fos);
            out.writeObject(pw);

            out.close();
            System.out.println("Save done");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public double getHeight(){
        return  pw.getHeight();
    }

    public double getWeight(){
        return  pw.getWeight();
    }

    public ProjectWrapper openFile(File f){
        try{
            FileInputStream fileInput = new FileInputStream(f);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInput);

            pw = (ProjectWrapper) objectInputStream.readObject();
            System.out.println("openFile done");
        } catch (Exception ex) {
            ex.printStackTrace();
        }


         return pw;
    }

    private Image generateImage(Canvas c, int height, int weight){
        SnapshotParameters params = new SnapshotParameters();
        WritableImage writableImage = new WritableImage(height, weight);
        c.snapshot(params, writableImage);
        return writableImage;
    }

}
