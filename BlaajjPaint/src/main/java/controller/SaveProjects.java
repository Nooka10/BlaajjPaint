package controller;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.binary.BinaryStreamDriver;
import com.thoughtworks.xstream.io.xml.CompactWriter;

import com.thoughtworks.xstream.io.xml.XppDriver;

import java.io.*;


public class SaveProjects {


    private static SaveProjects ourInstance = new SaveProjects();

    public static SaveProjects getInstance() {
        return ourInstance;
    }

    private XStream xstream;

    String path = "./auto";
    final static String EXTENTION = ".blaajj";

    private SaveProjects(){
        //xstream = new XStream(new XppDriver());
        initBinary();
    }

    private void initCompat(){
        xstream = new XStream(new XppDriver() {
            @Override
            public HierarchicalStreamWriter createWriter(Writer out) {
                return new CompactWriter(out, getNameCoder());
            }
        });
    }

    private void initBinary(){
        xstream = new XStream(new BinaryStreamDriver() {
            @Override
            public HierarchicalStreamWriter createWriter(Writer out) {
                return new CompactWriter(out, getNameCoder());
            }
        });
    }

    public void generateCompact(File f, Object o){
/*
        if(path == ""){
            // Do error no path value
            System.err.println("No file?");
        }

        File f = new File(f.getPath());
        if(f.exists() && !f.isDirectory()) {
            // do error popup Warining file already exist
            System.err.println("No new file, sur to continue?");
        }
*/
        try{
            ObjectOutputStream objectOutputStream = xstream.createObjectOutputStream(new FileOutputStream(f.getPath()));
            objectOutputStream.writeObject(o);
            objectOutputStream.flush();
            objectOutputStream.close();

        } catch (IOException e){
            System.out.println(" ERROR1" + e.toString());
            // ERROR can't save file
        }

    }

    public Object rebuild(File f) throws IOException, ClassNotFoundException{


        FileInputStream fis = new FileInputStream(f.getPath());
        ObjectInputStream ois = xstream.createObjectInputStream(fis);

        //Object aut =  ois.readObject();

        return  ois.readObject();
    }

}
