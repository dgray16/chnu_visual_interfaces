package sample;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * What this application can do:
 * 1. Show live image from your webcam.
 * 2. Take a shot when you clicked on button.
 */
public class Controller implements Initializable {

    public static Stage stage;

    @FXML private AnchorPane mainPane;

    private Webcam webcam;

    private BufferedImage grabbedImage;

    private ObjectProperty<Image> imageObjectProperty = new SimpleObjectProperty<>();

    private ImageView imageView = new ImageView();

    private Thread thread;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        imageView.setLayoutX(80);
        imageView.setLayoutY(30);
        mainPane.getChildren().add(imageView);

        /* Thread to initialize webcam */
        Task<Void> webCamTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                webcam = Webcam.getDefault();
                webcam.setViewSize(WebcamResolution.VGA.getSize());
                webcam.open();
                return null;
            }
        };
        thread = new Thread(webCamTask);
        thread.setDaemon(true);
        thread.start();

        /* Thread to show live image */
        new Thread(() -> {

            /* Just a delay */
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            while (true) {
                if ( (grabbedImage = webcam.getImage()) != null ) {
                    imageObjectProperty.set(SwingFXUtils.toFXImage(grabbedImage, null));
                    imageView.imageProperty().bind(imageObjectProperty);
                }

            }

        }).start();
    }

    @FXML
    @SuppressWarnings("unused")
    private void captureImage() {
        BufferedImage image = webcam.getImage();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(stage);
        if ( file != null ) {
            saveImage(image, file);
        }
    }

    private void saveImage(BufferedImage image, File file) {
        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
