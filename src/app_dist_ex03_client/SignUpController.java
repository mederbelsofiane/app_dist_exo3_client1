/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app_dist_ex03_client;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author meder
 */
public class SignUpController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    PasswordField password;

    
    @FXML
    public TextField  email;
    @FXML
    Label message;
    @FXML
    TextField username;

    Stage stage;

    @FXML
    public void login(ActionEvent e) {

        try {
            if (password.getText().length() > 5) {
                System.out.println("email : " + email.getText() + " password : " + password.getText());

                Database database = new Database("127.0.0.1");
//                String x = "INSERT INTO user VALUES(email,password)"
//                        + "VALUES ('" + email.getText() + "', '" + password.getText() + "')";

                int result = database.Insert("INSERT INTO users (username,email,pass) "
                        + "VALUES('" + username.getText() + "','" + email.getText() + "', '" + password.getText() + "')");

                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("Login.fxml"));
                load.load();
                LoginController login = load.getController();

                Parent root = load.getRoot();

                Scene scene = new Scene(root);
                stage.close();
                stage.setScene(scene);
                stage.centerOnScreen();
                stage.show();
                
//            }
            } else {
                message.setText("Le mot de passe entré est trop court");
            }
        } catch (SQLException | ClassNotFoundException | IOException ex) {
            Logger.getLogger(SignUpController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
@FXML
//    public void login(ActionEvent e) {}
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    

}
