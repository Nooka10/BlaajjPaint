package controller;

import controller.history.RecordCmd;

import java.io.File;

public class MasterController {

    private Project modelProject;
    private RecordCmd instance = RecordCmd.getInstance();
    private SaveProjects saveProjects = SaveProjects.getInstance();

    private static MasterController ourInstance = new MasterController();

    public static MasterController getInstance() {
        return ourInstance;
    }

    private MasterController() {
        modelProject = null;
    }

    public void newModel() {
        modelProject = Project.getInstance();
    }

    public void openModel(File f) {
        try {
            modelProject = (Project) saveProjects.rebuild(f);
        } catch (Exception e) {
            //todo
        }
    }

    public void saveAsModel(File f) {
        saveProjects.generateCompact(f, modelProject);
    }
}
