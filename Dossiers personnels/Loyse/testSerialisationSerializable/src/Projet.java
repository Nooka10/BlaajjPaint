import java.io.Serializable;
import java.util.LinkedList;

public class Projet implements Serializable{

    private String name;
    public LinkedList<Layer> layers = new LinkedList<Layer>();

    public Projet(){
        this.name = "default";
    }

    public Projet(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void addLayer(Layer layer){
        layers.add(layer);
    }

    public String toString(){
        String s =  "nom : " + name + "\n" + "layers : ";
        for(Layer l : layers){
            s += l;
        }
        return s;
    }


}
