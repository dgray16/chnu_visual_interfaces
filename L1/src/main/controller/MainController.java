package main.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {

    @FXML
    public void goToSDI(ActionEvent event) {
        Parent parent = null;
        Stage stage = new Stage();

        try {
            parent = FXMLLoader.load(getClass().getResource("../applications/sdi/view/sdiMain.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(parent, 1100, 800);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("SDI");
        stage.initOwner( ((Node) event.getTarget()).getScene().getWindow() );
        stage.showAndWait();
    }

    @FXML
    public void goToMDI(ActionEvent event) {
        Parent parent = null;
        Stage stage = new Stage();

        try {
            parent = FXMLLoader.load(getClass().getResource("../applications/mdi/view/mdiMain.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(parent, 1100, 1100);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("MDI");
        stage.initOwner( ((Node) event.getTarget()).getScene().getWindow() );
        stage.showAndWait();
    }
}
