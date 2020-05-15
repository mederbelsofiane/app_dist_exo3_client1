package app_dist_ex03_client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class User {

    private int id;
    private String nom;
    private static Socket socket;
    private UserInterfaceController interfaceController;
    public static VBox vbox_message;
    public ArrayList<HBox> list_message;
    private String message;
    public static User personal_user;
    
    public User(int id, String nom) {
        this.id = id;
        this.nom = nom;
        list_message = new ArrayList<HBox>();
    }

    public User(int id, String nom, String message) {
        this(id,nom);
        this.message = message;
    }
    
    public User(int id, String nom, UserInterfaceController interfaceController) {
        this(id, nom);
        this.interfaceController = interfaceController;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public Socket getSocket() {
        return socket;
    }

    public UserInterfaceController getInterfaceController() {
        return interfaceController;
    }

    public void setInterfaceController(UserInterfaceController interfaceController) {
        this.interfaceController = interfaceController;
    }

    public void SendMessage(String s) {
       try {

            DataOutputStream out = new DataOutputStream(getSocket().getOutputStream());
            out.writeUTF("M");
            out.writeInt(id);
            if(id!=1){
                out.writeInt(personal_user.getId());
            }
            String cry = AES.encrypt(s, "anis");
            out.writeUTF(cry);

            FXMLLoader load = new FXMLLoader();
            load.setLocation(getClass().getResource("Message.fxml"));
            load.load();
            Message_Controller Controller = load.getController();

            Text t = new Text(s);
            t.setFill(Color.WHITE);
            Controller.setText(t, "#3578E5");

            HBox root = (HBox) load.getRoot();
            root.setAlignment(Pos.CENTER_RIGHT);
            interfaceController.setMessage("You : " + s);
            list_message.add(root);
            vbox_message.getChildren().add(root);

        } catch (IOException ex) {
        }
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }

    public static void setSocket(Socket socket) {
        User.socket = socket;
    }
    
}
