package sample;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintWriter;
import java.util.Base64;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;


public class ImageXMLAccessor {

    private final ExecutorService exec = Executors.newCachedThreadPool(r -> {
        Thread thread = new Thread(r);
        thread.setDaemon(true);
        return thread ;
    });

    public void saveImage(XMLEventWriter writer, XMLEventFactory eventFactory, Image image)
            throws IOException, XMLStreamException {

        BufferedImage buffImage = SwingFXUtils.fromFXImage(image, null);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ImageIO.write(buffImage, "png", bytes);
        String encodedImage = Base64.getEncoder().encodeToString(bytes.toByteArray());
        writer.add(eventFactory.createCharacters(encodedImage));
    }

    public Image readImage(XMLEventReader eventReader)
            throws IOException, XMLStreamException, InterruptedException, ExecutionException {

        PipedInputStream pipedInput = new PipedInputStream();

        FutureTask<Image> imageTask = new FutureTask<>(new Callable<Image>() {
            @Override
            public Image call() throws Exception {
                try (InputStream imageStream = Base64.getDecoder().wrap(pipedInput)) {

                    BufferedImage buffImage = ImageIO.read(imageStream);
                    return SwingFXUtils.toFXImage(buffImage, null);
                }
            }
        });

        exec.submit(imageTask);

        try (PrintWriter output = new PrintWriter(new PipedOutputStream(pipedInput))) {
            for (boolean done = false ; eventReader.hasNext() && ! done; ) {
                XMLEvent event = eventReader.nextEvent();
                if (event.isCharacters()) {
                    Characters characters = event.asCharacters();
                    String data = characters.getData();
                    output.write(data);
                } else if (event.isEndElement()) {
                    output.close();
                    done = true ;
                }
            }
        }

        return imageTask.get();
    }

}