package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ProjectWrapper implements Serializable{
    private double weight;
    private double height;

    private Image layer;

    public ProjectWrapper(){};

    public ProjectWrapper(double height, double weight, Image layer){
        this.height = height;
        this.weight = weight;
        this.layer = layer;
    }

    public double getWeight() {
        return weight;
    }

    public double getHeight() {
        return height;
    }


    public Image getLayer() {
        return layer;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setHeight(double height) {
        this.height = height;
    }


    public void setLayer(Image layer) {
        this.layer = layer;
    }



    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        //s.defaultReadObject();
        height = s.readDouble();
        weight = s.readDouble();
        layer = SwingFXUtils.toFXImage(ImageIO.read(s), null);
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        //s.defaultWriteObject();
        s.writeDouble(height);
        s.writeDouble(weight);
        ImageIO.write(SwingFXUtils.fromFXImage(layer, null), "png", s);
    }
}
