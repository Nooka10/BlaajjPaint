package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

public class Main extends Application {





	Canvas layer1;
	Canvas layer2;



	LinkedList<Canvas> layers = new LinkedList<>();
	
	GraphicsContext gc1;
	GraphicsContext gc2;
	Stage primaryStage;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
		layer1 = new Canvas(300, 250);
		layer2 = new Canvas(300, 250);
		
		layers.add(layer1);
		layers.add(layer2);
		// Obtain Graphics Contexts
		gc1 = layer1.getGraphicsContext2D();
		gc1.setFill(Color.GREEN);
		gc1.fillOval(50, 50, 20, 20);
		gc2 = layer1.getGraphicsContext2D();
		gc2.setFill(Color.BLUE);
		gc2.fillOval(100, 100, 20, 20);
		
		
		Group root = new Group();
		
		Button buttonSave = new Button("Save");
		buttonSave.setOnAction(t -> {

			FileChooser fileChooser = new FileChooser();

			FileChooser.ExtensionFilter extXML =
					new FileChooser.ExtensionFilter("XML (*.xml)", "*.xml");
			fileChooser.getExtensionFilters().addAll(extXML);

			File file = fileChooser.showSaveDialog(primaryStage);
			SaveProject sp = new SaveProject(layer1);
			sp.save(file);

		});
		
		Button importButton = new Button("Import");
		importButton.setOnAction(t -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("All images files", "*.xml"),
					new FileChooser.ExtensionFilter("XML (*.xml)", "*.xml"));

			File file = fileChooser.showOpenDialog(primaryStage);
			if(file == null)
				return;

			System.err.println(file.getPath());

			SaveProject sp = new SaveProject(layer1);


			Image image = sp.openFile(file).getLayer();

			layer2.getGraphicsContext2D().drawImage(image, 0, 0);
			System.out.println(layer2.getHeight());

		});
		
		
		HBox hBox = new HBox();
		hBox.getChildren().addAll(buttonSave, importButton);
		
		VBox vBox = new VBox();
		vBox.getChildren().addAll(hBox, layer1, layer2);
		root.getChildren().add(vBox);
		Scene scene = new Scene(root, 400, 425);
		primaryStage.setTitle("java-buddy.blogspot.com");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	
	private void mergeLayers(Canvas foregroundCanvas, Canvas backgroundCanvas) {
		SnapshotParameters params = new SnapshotParameters();
		params.setFill(Color.TRANSPARENT);
		Image snapshot = foregroundCanvas.snapshot(params, null);
		backgroundCanvas.getGraphicsContext2D().drawImage(snapshot, 0, 0);
	}
	
	
	private void exportToPNG() {
		FileChooser fileChooser = new FileChooser();
		
		FileChooser.ExtensionFilter extPNG =
				new FileChooser.ExtensionFilter("PNG (*.png)", "*.png");
		FileChooser.ExtensionFilter extJPG =
				new FileChooser.ExtensionFilter("JPG (*.jpg)", "*.jpg");
		fileChooser.getExtensionFilters().addAll(extPNG, extJPG);
		
		File file = fileChooser.showSaveDialog(primaryStage);
		
		if (file != null) {
			try {
				Canvas canvas = new Canvas(300, 250);
				WritableImage writableImage = new WritableImage(300, 250);

				SnapshotParameters spa = new SnapshotParameters();
				for (Canvas c : layers) {
					mergeLayers(c, canvas);
				}
				SnapshotParameters params = new SnapshotParameters();
				String chosenExtension = "";
				
				int i = file.getPath().lastIndexOf('.');
				if (i > 0) {
					chosenExtension = file.getPath().substring(i + 1);
				}
				if (chosenExtension.equals("png")) {
					params.setFill(Color.TRANSPARENT);
				} else if (chosenExtension.equals("jpg")) {
					params.setFill(Color.WHITE);
				}
				
				canvas.snapshot(params, writableImage);
				layer1 = canvas;
				BufferedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
				ImageIO.write(renderedImage, chosenExtension, file);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private void saveImageToXML()
			throws IOException, FactoryConfigurationError, XMLStreamException {

		SnapshotParameters params = new SnapshotParameters();
		WritableImage writableImage = new WritableImage(300, 250);
		layer1.snapshot(params, writableImage);

		ImageXMLAccessor accessor = new ImageXMLAccessor();


		FileChooser fileChooser = new FileChooser();

		FileChooser.ExtensionFilter extXML =
				new FileChooser.ExtensionFilter("XML (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().addAll(extXML);

		File xmlFile = fileChooser.showSaveDialog(primaryStage);


		Writer writer = Files.newBufferedWriter(xmlFile.toPath());
		XMLEventFactory eventFactory = XMLEventFactory.newFactory();
		XMLEventWriter eventWriter = XMLOutputFactory.newFactory().createXMLEventWriter(writer);
		eventWriter.add(eventFactory.createStartDocument());
		QName imageElement = new QName("image");
		eventWriter.add(eventFactory.createStartElement(imageElement, null, null));
		accessor.saveImage(eventWriter, eventFactory, writableImage);
		eventWriter.add(eventFactory.createEndElement(imageElement, null));
		eventWriter.add(eventFactory.createEndDocument());
		writer.close();
	}
	public void openFromXMLFile(Canvas canvas) {
		ImageXMLAccessor accessor = new ImageXMLAccessor();
		ObjectProperty<Image> currentImage = new SimpleObjectProperty<>();
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("All images files", "*.xml"),
				new FileChooser.ExtensionFilter("XML (*.xml)", "*.xml"));

		File xmlFile = fileChooser.showOpenDialog(primaryStage);
		if(xmlFile == null)
			return;

		String chosenExtension = "";
		int i = xmlFile.getPath().lastIndexOf('.');
		if (i > 0) {
			chosenExtension = xmlFile.getPath().substring(i + 1);
		} else{
			return;
		}


		new Thread(() -> {
			XMLInputFactory inputFactory = XMLInputFactory.newFactory() ;
			XMLEventReader eventReader = null ;
			try {
				eventReader = inputFactory.createXMLEventReader(Files.newBufferedReader(xmlFile.toPath()));
				while (eventReader.hasNext()) {
					XMLEvent event = eventReader.nextEvent();
					if (event.isStartElement()) {
						StartElement startEl = event.asStartElement();
						if ("image".equals(startEl.getName().getLocalPart())) {
							Image image;
							try {
								image = accessor.readImage(eventReader);
								Platform.runLater(() -> currentImage.set(image));

								canvas.getGraphicsContext2D().drawImage(image, 0, 0);
							} catch (Exception e) {
								e.printStackTrace();
							}

						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (eventReader != null ){
					try {
						eventReader.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}).start();

	}



	private void importImage(Canvas canvas) {
		FileChooser fileChooser = new FileChooser();
		
		fileChooser.setTitle("Import an image");
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("All images files", "*.png", "*.jpg"),
				new FileChooser.ExtensionFilter("PNG (*.png)", "*.png"),
				new FileChooser.ExtensionFilter("JPG (*.jpg)", "*.jpg"));
		
		File file = fileChooser.showOpenDialog(primaryStage);
		
		String chosenExtension = "";
		int i = file.getPath().lastIndexOf('.');
		if (i > 0) {
			chosenExtension = file.getPath().substring(i + 1);
		}
		
		Image image = null;
		
		if (chosenExtension.equals("png") || chosenExtension.equals("jpg")) {
			image = new Image(file.toURI().toString());
		}
		
		
		canvas.getGraphicsContext2D().drawImage(image, 0, 0);
		
	}
	
	
}