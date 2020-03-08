/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app_dist_ex03_client;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author meder
 */
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class APP_DIS_Exo03_Client extends Application{


    public void start(Stage stage) throws Exception {
        FXMLLoader load = new FXMLLoader();
        load.setLocation(getClass().getResource("Login.fxml"));
        load.load();
        LoginController Controller = load.getController();
        Controller.setStage(stage);
        Parent root = load.getRoot();

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
    
