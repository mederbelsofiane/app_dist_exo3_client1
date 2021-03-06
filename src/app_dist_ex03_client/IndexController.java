package app_dist_ex03_client;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class IndexController implements Initializable {

    @FXML
    public Label user_name, client_name;
    @FXML
    public Circle user_img, client_img;
    @FXML
    public VBox vbox_users, vbox_message;
    @FXML
    private TextField message;
    @FXML private Button file,Envoyer;
    ReceiveMessage receive_message;
    UserOnline list_user_online;
    Socket serveur;
    Stage stage;
    DataOutputStream out;
    User user;

    public void envoyer() {
        if (UserOnline.getId_user_selected() != 0) {
            User u = list_user_online.getUserID(UserOnline.getId_user_selected());
            u.SendMessage(message.getText());
            message.setText("");
        }
    }
     @FXML
    public void envoyer_file(ActionEvent e) {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichier", "*.*"));
        File f = fc.showOpenDialog(stage);
        try {
            //handle file read
            byte[] mybytearray = new byte[(int) f.length()];

            FileInputStream fis = new FileInputStream(f);
            BufferedInputStream bis = new BufferedInputStream(fis);

            DataInputStream dis = new DataInputStream(bis);
            dis.readFully(mybytearray, 0, mybytearray.length);

            //handle file send over socket
            OutputStream os = serveur.getOutputStream();

            //Sending file name and file size to the server
            DataOutputStream dos = new DataOutputStream(os);
            dos.writeUTF("P");
            dos.writeUTF(f.getName());
            dos.writeLong(mybytearray.length);
            dos.write(mybytearray, 0, mybytearray.length);
            dos.flush();
            System.out.println("File sent to serveur.");
        } catch (IOException er) {
            System.err.println("File does not exist!");
        }
        FXMLLoader load = new FXMLLoader();
        load.setLocation(getClass().getResource("Message.fxml"));
        try {
            load.load();
        } catch (IOException ex) {
            Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Message_Controller Controller = load.getController();
        String fileName = f.getName();
        if (fileName.contains(".PNG") || fileName.contains(".JPG") || fileName.contains(".png") || fileName.contains(".jpg")) {
            FileInputStream seats_fileInputStream = null;
            try {
                seats_fileInputStream = new FileInputStream(f);
                Image ar = new Image(seats_fileInputStream, 150, 150, false, false);
                ImageView image = new ImageView(ar);
                Controller.setImage(image, "#3578E5");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    seats_fileInputStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            Button btn = new Button(fileName);
            btn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    FileInputStream seats_fileInputStream = null;
                    try {
                        seats_fileInputStream = new FileInputStream(f);
                        String Extension = getFileExtension(f);
                        FileChooser fileChooser = new FileChooser();
                        // String[] s = fileName.split(".", 2);
                        fileChooser.setInitialFileName(fileName);
                        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("(*" + Extension + ")", "*" + Extension));
                        File f = fileChooser.showSaveDialog(null);
                        if (f != null) {
                            FileOutputStream outputStream = new FileOutputStream(f);
                            int read = 0;
                            byte[] bytes = new byte[1024];
                            while ((read = seats_fileInputStream.read(bytes)) != -1) {
                                outputStream.write(bytes, 0, read);
                            }
                        }
                    } catch (FileNotFoundException ex) {
                        Logger.getLogger(ReceiveMessage.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(ReceiveMessage.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        try {
                            seats_fileInputStream.close();
                        } catch (IOException ex) {
                            Logger.getLogger(ReceiveMessage.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }
            });
            Controller.setFile(btn, "#3578E5");
        }
        HBox root = (HBox) load.getRoot();
        root.setAlignment(Pos.CENTER_RIGHT);
        vbox_message.getChildren().add(root);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        UserInterfaceController.index = this;
 
    }
    
    public void setEtatMessage(boolean b) {
        message.setDisable(b);
        file.setDisable(b);
        Envoyer.setDisable(b);
    }

    public void setServeur(Socket Serveur) {
      //  list_user_online=new UserOnline(vbox_message,client_name);
        this.serveur = Serveur;
         User.vbox_message=vbox_message;
        receive_message = new ReceiveMessage(vbox_message,list_user_online);
        receive_message.setClient(Serveur);
        receive_message.start();
    }

    public void setList_user_online(UserOnline list_user_online) {
        this.list_user_online = list_user_online;
        this.list_user_online.setClient_name(client_name);
        this.list_user_online.setVbox_users(vbox_users);
       
        for(User u:list_user_online.getList_user_online()){
            vbox_users.getChildren().add(u.getInterfaceController().root);
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
      private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return ""; // empty extension
        }
        return name.substring(lastIndexOf);
    }

    public void setUser(User user) {
        this.user = user;
        user_name.setText(user.getNom());
        User.personal_user=user;
         try {
            Database db = new Database("localhost");
            PreparedStatement sql = db.getCon().prepareStatement("select users.id_u,username ,message, send_date from users "
                    + "join amis on users.id_u = amis.id_user1  join messages on amis.id_ami= messages.id_ami and amis.id_message=messages.id_message where id_user2=? "
                    + "union select users.id_u,username ,message, send_date from users join amis on users.id_u = amis.id_user2  "
                    + "join messages on amis.id_ami= messages.id_ami and amis.id_message=messages.id_message where id_user1=? order by send_date");
            
            sql.setInt(1, user.getId());
            sql.setInt(2, user.getId());
            ResultSet result = db.load_table(sql);
           
            while (result.next()) {
                list_user_online.addUser(new User(result.getInt("id_u"),result.getString("username"),result.getString("message")));

            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
}
