import java.awt.*;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;

public class Main {

    public static void main(String... args) {
        Layer l = new Layer(10, 10);

        Projet p = new Projet("dessin");

        p.addLayer(l);

        String file = "./projet.xml";
        XMLEncoder encoder = null;

        try {
            encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(file)));
        } catch (FileNotFoundException fileNotFound) {
            System.out.println("Error while creating or opening th file");
        } catch (IOException e) {
            e.printStackTrace();
        }
        encoder.writeObject(p);
        encoder.close();


        XMLDecoder decoder = null;

        try {
            decoder=new XMLDecoder(new BufferedInputStream(new FileInputStream(file)));
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: File dvd.xml not found");
        }

        Projet p2 = (Projet)decoder.readObject();
        System.out.println(p2);





    }

}
