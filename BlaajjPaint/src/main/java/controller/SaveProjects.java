package controller;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.binary.BinaryStreamDriver;
import com.thoughtworks.xstream.io.xml.CompactWriter;

import com.thoughtworks.xstream.io.xml.XppDriver;

import java.io.*;


public class SaveProjects {

    private Project projectInstance = Project.getInstance();
    private File saveFile;

    private static SaveProjects ourInstance = new SaveProjects();

    public static SaveProjects getInstance() {
        return ourInstance;
    }

    private SaveProjects(){
        saveFile = null;
    }

    public void saveAs(File f){
        saveFile = f;
        save();
    }

    public void save(){
        if(saveFile == null){
            System.out.println("No project to save");
            return;
        }

        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try {
            fos = new FileOutputStream(saveFile);
            out = new ObjectOutputStream(fos);
            out.writeObject(projectInstance);

            out.close();
            System.out.println("Save done");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void openFile(File f){
        try{
            FileInputStream fileInput = new FileInputStream(f);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInput);

            projectInstance = (Project) objectInputStream.readObject();
            //objectInputStream.readObject();

            // MainViewController.getInstance().setProject((Project) objectInputStream.readObject());

            projectInstance.drawWorkspace();
            System.out.println("openFile done");

            saveFile = f;
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        //return pw;
    }



    public void clear(){
        saveFile = null;
    }
}
