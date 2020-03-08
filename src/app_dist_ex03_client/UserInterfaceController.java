/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app_dist_ex03_client;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class UserInterfaceController implements Initializable {

    @FXML
    private Label user_name, message;
    @FXML
    private Pane pane;
    @FXML
    HBox root;
    private User user;
    static public Label user_name_interface;
    static public VBox vbox_message;
    static public IndexController index;

    @FXML
    public void mouseEntred(MouseEvent e) {
        pane.setStyle("-fx-background-color : #FFC107");
        root.setStyle(("-fx-background-color : #337ab7"));
    }

    @FXML
    public void mouseExited(MouseEvent e) {
        pane.setStyle("-fx-background-color : #12181E");
        root.setStyle(("-fx-background-color : #12181E"));

    }
//
//    @FXML
//    public void clicked(MouseEvent e) {
//        //user_name_interface.setText(user_name.getText());
//        index.client_name.setText(user.getNom());
//        UserOnline.setId_user_selected(user.getId());
//        index.setEtatMessage(false);
//    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void setUserInterface(String name, String message) {
        user_name.setText(name);
        this.message.setText(message);

    }

    public void setMessage(String message) {
        this.message.setText(message);
    }

    public void setUser(User user) {
        this.user = user;
    }

}
