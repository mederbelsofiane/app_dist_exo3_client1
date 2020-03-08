package app_dist_ex03_client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class UserOnline {

    private ArrayList<User> list_user_online;
    private VBox vbox_users;
    private Label client_name;
    private static int id_user_selected=0;

    public UserOnline(VBox vbox_users, Label client_name) {
        list_user_online = new ArrayList<User>();
        this.vbox_users = vbox_users;
        this.client_name = client_name;
    }

    public void addUser(User u) {
        try {
            list_user_online.add(u);
            FXMLLoader load = new FXMLLoader();
            load.setLocation(getClass().getResource("UserInterface.fxml"));
            load.load();
            UserInterfaceController Controller = load.getController();
            u.setInterfaceController(Controller);
            Controller.setUserInterface(u.getNom(), "test");
            Controller.setUser(u);
            HBox root = (HBox) load.getRoot();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    vbox_users.getChildren().add(root);
                }

            });
            new Thread(u).start();

        } catch (IOException ex) {
            Logger.getLogger(UserOnline.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public User getUserID(int id) {
        for (User user : list_user_online) {
            if(user.getId()==id){
                return user;
            }
        }
        return null;
    }

    public static void setId_user_selected(int id_user_selected) {
        UserOnline.id_user_selected = id_user_selected;
    }

    public static int getId_user_selected() {
        return id_user_selected;
    }
    
    

}
