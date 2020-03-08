package app_dist_ex03_client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
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

public class LoginController implements Initializable {

    @FXML
    PasswordField password;
    @FXML
    TextField username;
    @FXML
    Label message;
    Stage stage;
    Socket serveur;

    @FXML
    public void connecter(ActionEvent e) {
        try {
            System.out.println("email : " + username.getText() + " password : " + password.getText());

//            Database database = new Database("127.0.0.1");
//
//            ResultSet result = database.load_table("select * from users where username='" + username.getText() + "' and pass='" + password.getText() + "'");
            int b = 0;
//            int id_user = 0;
//            String username;
//            while (result.next()) {
//                b++;
//                id_user = result.getInt("ID_U");
//                username=result.getString("username");
//            }
            DataOutputStream out = new DataOutputStream(serveur.getOutputStream());
            out.writeUTF(username.getText());
            out.flush();
            out.writeUTF(password.getText());
            out.flush();
            if (b == 1) {
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("Index.fxml"));
                load.load();
                IndexController index = load.getController();
                index.setClient(serveur);

                Parent root = load.getRoot();

                Scene scene = new Scene(root);
                stage.close();
                stage.setScene(scene);
                stage.centerOnScreen();
                stage.show();

            } else {
                message.setText("Le mot de passe entré est incorrect");
            }
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    public void signup(ActionEvent e) {
        System.out.println("app_dist_ex03_serveur.LoginController.signup()");
        try {

            FXMLLoader load = new FXMLLoader();
            load.setLocation(getClass().getResource("SignUp.fxml"));
            load.load();
            SignUpController SignUp = load.getController();
            SignUp.setStage(stage);
            Parent root = load.getRoot();
            Scene scene = new Scene(root);
            stage.close();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            serveur = new Socket("127.0.0.1", 3000);
        } catch (IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }

        username.setText("test");
        password.setText("test");
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

}
