package sample;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ImageWrapper implements Serializable {

    Image image;
    public ImageWrapper(){};

    public ImageWrapper(Image writableImage) {
        this.image = writableImage;
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        image = SwingFXUtils.toFXImage(ImageIO.read(s), null);
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", s);
    }

/*

    private void writeObject(ObjectOutputStream out) throws IOException{
        out.writeObject(writableImage);
    }

    private void readObject(ObjectOutputStream out) throws IOException{

    }
*/
    public Image getImage() {
        return image;
    }

    public void setImage(Image writableImage) {
        this.image = writableImage;
    }
}
