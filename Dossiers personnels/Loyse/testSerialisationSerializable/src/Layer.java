import java.awt.*;
import java.io.Serializable;
import java.lang.reflect.Array;

public class Layer implements Serializable {
    private int width;
    private int height;
    private Pixel pixelMatrix[][];

    public Layer(){
        this.width = 0;
        this.height = 0;
        this.pixelMatrix = new Pixel[width][height];
    }

    public Layer(int width, int heigth){
        this.width = width;
        this.height = heigth;
        pixelMatrix = new Pixel[width][height];

        for(int i = 0; i < width; ++i){
            for(int j = 0; j < height; ++j){
                pixelMatrix[i][j] = new Pixel(Color.black);
            }
        }
    }

    public int getWidth(){
        return width;
    }

    public void setWidth(int width){
        this.width = width;
    }

    public int getHeight(){
        return height;
    }

    public void setHeight(int height){
        this.height = height;
    }

    public Pixel[][] getPixelMatrix(){
        return pixelMatrix;
    }

    public void setPixelMatrix(Pixel matrix[][]){
        this.pixelMatrix = matrix;
    }

    /** Cette méthode change le pixel donné par <i>posX</i> et <i>posY</i>*/
    public void setPixel(int posX, int posY, Color color){
        pixelMatrix[posX][posY].setColor(color);
    }

    public String toString(){
        String s = "[";
        for (int i = 0; i < width; ++i){
            for(int j = 0; j < height; ++j){
                s += pixelMatrix[i][j] + " ";
            }
            s += "\n";
        }
        s += "]";
        return s;
    }

}
