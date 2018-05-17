package utils;


import controller.MainViewController;
import controller.Project;

import java.io.*;

/**
 * Classe permettant la sauvegarde et de l'application.
 * Sauvegarde le Projet dans un fichier.
 * Recupère le projet depuismun fichier de sauvegarde.
 * Permet un de stocker le ficher de sauvegarde.
 *
 * Il n'y a pas de contrôle sur le fichier passé en paramêtre
 *
 * Singleton pour être accessible depuis n'importe quelle classe du projet ou et avoir qu'une seule instance.
 */
public class SaveProject {
	private Project projectInstance = null; // instance du projet à sauvegarder
	private File saveFile; // Fichier de sauvegarde

	private static SaveProject saveProjectInstance = null;
	
	public static SaveProject getInstance() {
		if (saveProjectInstance == null) {
			saveProjectInstance = new SaveProject();
		}
		return saveProjectInstance;
	}
	
	private SaveProject() {
		saveFile = null;
	}
	
	/**
	 * Enregiste le projet dans un fichier spécifié.
	 * Le fichier spécifié est stocké en cas d'appelle de la méthode save.
	 * @param f   Fichier à enregistrer
	 */
	public void saveAs(File f) {
		saveFile = f;
		save();
	}

	/**
	 * Sauvegarde l'instance de Porjet dans le File courrant.
	 * Si il n'y a pas de File, appelle la méthode pour enregistrer comme un projet jamais enregistré
	 */
	public void save() {
		if (saveFile == null) {		// Si le fichier n'a jamais été sauvegardé
			MainViewController.getInstance().saveAs();

		} else {
			try {
				FileOutputStream fos = new FileOutputStream(saveFile);
				ObjectOutputStream out = new ObjectOutputStream(fos);
				out.writeObject(projectInstance);

				out.close();
				System.out.println("Save done");
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Ouverture d'un ficher
	 * Enregistre le File courrant
	 * @param f		fichier source
	 *
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void openFile(File f) throws IOException, ClassNotFoundException {

		FileInputStream fileInput = new FileInputStream(f);
		ObjectInputStream objectInputStream = new ObjectInputStream(fileInput);
		projectInstance = (Project) objectInputStream.readObject();

		projectInstance.drawWorkspace();
		System.out.println("openFile done");
		saveFile = f;

	}

	/**
	 * Remet à null toutes les variales de la classe sauf la reéférence vers l'instance du projet.
	 */
	public void clear() {
		saveFile = null;
		// FIXME: pourquoi tu garde l'instance actuelle du projet?
	}
}
