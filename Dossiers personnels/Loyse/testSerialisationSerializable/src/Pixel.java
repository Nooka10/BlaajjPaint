import java.awt.*;
import java.io.Serializable;

public class Pixel implements Serializable {

    public Color c;

    public Pixel(){
        this.c = Color.WHITE;
    }

    public Pixel(Color c){
        this.c = c;
    }

    public void setColor(Color c){
        this.c = c;
    }

    public Color getColor(){
        return c;
    }

    public String toString(){
        return c.toString();
    }

}
