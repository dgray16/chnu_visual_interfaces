package main.applications.sdi.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import main.controller.MatrixController;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by a1 on 29.09.16.
 */
public class SDIController implements Initializable {

    @FXML private AnchorPane pane;

    private MatrixController matrixController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        matrixController = new MatrixController();
        Platform.runLater(() -> matrixController.createSettingsComponents(pane, true));
    }
}
