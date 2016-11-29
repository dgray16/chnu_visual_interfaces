package main.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Created by a1 on 02.11.16.
 */
public class MainController implements Initializable {

    @FXML
    private AnchorPane matrixPane;

    @FXML
    private AnchorPane animationPane;

    @FXML
    private Button matrixButton;

    private MatrixController matrixController;

    private AnimationController animationController;

    @FXML
    public void goToSDI() {
        matrixController = new MatrixController();
        Platform.runLater(() -> matrixController.createSettingsComponents(matrixPane, true));
        matrixButton.setVisible(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        animationController = new AnimationController();
        Platform.runLater(() -> animationController.create(animationPane));
    }
}
