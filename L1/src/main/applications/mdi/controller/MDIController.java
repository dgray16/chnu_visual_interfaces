package main.applications.mdi.controller;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import main.controller.MatrixController;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by a1 on 29.09.16.
 */
public class MDIController implements Initializable {

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab tab1;

    @FXML
    private Tab tab2;

    private MatrixController matrixController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tabPane.setPrefWidth(1100);
        tabPane.setPrefHeight(1100);

        matrixController = new MatrixController();

        /*
         * I do not know why, but FXMLLoader.load() returns null in MainController#goToMDI(),
         * when I am executing this method without Platform.runLater().
         */
        Platform.runLater(() -> matrixController.createSettingsComponents((AnchorPane) tab1.getContent(), false));
    }

    @FXML
    private void changeTab(Event event) {
        Platform.runLater(() -> {
            if ( ((Tab) event.getTarget()).getText().contains("Main") ) {
                tab1.getContent().setVisible(true);
                tab2.getContent().setVisible(false);
            } else {
                tab1.getContent().setVisible(false);
                tab2.getContent().setVisible(true);
            }
        });
    }
}
