package utils;


import controller.Project;

import java.io.*;

/**
 * Classe gérant la sauvegarde du projet dans un fichier et l'ouverture d'une sauvegarde. Implémente le modèle Singleton.
 *
 * @apiNote Attention! Il n'y a pas de contrôle sur le fichier passé en paramètre.
 */
public class SaveProject {
	private File saveFile; // fichier dans lequel enregistrer la sauvegarde
	
	private static SaveProject saveProjectInstance = null; // l'instance unique du singleton SaveProject
	
	/**
	 * Constructeur privé (modèle singleton).
	 */
	private SaveProject() {
		saveFile = null;
	}
	
	/**
	 * Retourne l'instance unique du singleton SaveProject.
	 *
	 * @return l'instance unique du singleton SaveProject.
	 */
	public static SaveProject getInstance() {
		if (saveProjectInstance == null) {
			saveProjectInstance = new SaveProject();
		}
		return saveProjectInstance;
	}
	
	public boolean fileIsSetted() {
		return saveFile != null;
	}
	
	/**
	 * Enregiste le projet dans le fichier passé en paramètre. Si le fichier en paramètre vaut null, ouvre une fenêtre permettant à l'utilisateur de choisir où
	 * enregistrer le projet et sous quel nom de fichier.
	 *
	 * @param f,
	 * 		le fichier dans lequel enregistrer le projet.
	 */
	public void saveAs(File f) {
		if (f != null){
			saveFile = f;
		}
		doSave();
	}
	
	/**
	 * Execute la sauvegarde
	 */
	private void doSave() {
		try {
			FileOutputStream fos = new FileOutputStream(saveFile);
			ObjectOutputStream out = new ObjectOutputStream(fos);
			out.writeObject(Project.getInstance());
			
			out.close();
			System.out.println("Save done");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Ouverture d'un ficher Enregistre le File courrant
	 *
	 * @param f
	 * 		fichier source
	 */
	public void openFile(File f) {
		try {
			FileInputStream fileInput = new FileInputStream(f);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInput);
			
			objectInputStream.readObject();
			
			Project.getInstance().drawWorkspace();
			saveFile = f;
		} catch (Exception ex) {
			ex.printStackTrace();
			
		}
	}
	
	/**
	 * Remet à null toutes les variales de la classe sauf la reéférence vers l'instance du projet.
	 */
	public void clear() {
		saveFile = null;
	}
}
