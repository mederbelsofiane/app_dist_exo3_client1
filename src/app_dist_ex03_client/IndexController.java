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
    ReceiveMessage receive_message;
//    private connected connected;
    Socket serveur;
    Stage stage;
    DataOutputStream out;

    public void envoyer() {
        try {

            out = new DataOutputStream(serveur.getOutputStream());
            out.writeUTF("M");

            String cry = AES.encrypt(message.getText(), "anis");
            out.writeUTF(cry);

            FXMLLoader load = new FXMLLoader();
            load.setLocation(getClass().getResource("Message.fxml"));
            load.load();
            Message_Controller Controller = load.getController();

            Text t = new Text(message.getText());
            t.setFill(Color.WHITE);
            Controller.setText(t, "#3578E5");

            HBox root = (HBox) load.getRoot();
            root.setAlignment(Pos.CENTER_RIGHT);
            vbox_message.getChildren().add(root);
            message.setText("");

        } catch (IOException ex) {
            Logger.getLogger(IndexController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //   }
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
        UserInterfaceController.user_name_interface = client_name;
        UserInterfaceController.vbox_message = vbox_message;
//        try {
//            serveur = new Socket("127.0.0.1", 3000);
//            
//        } catch (IOException ex) {
//            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
//        }
        receive_message = new ReceiveMessage(vbox_message, "anis");
        receive_message.setClient(serveur);
        receive_message.start();
    }

    public void setClient(Socket client) {
        this.serveur = client;
        receive_message = new ReceiveMessage(vbox_message, "anis");
        receive_message.setClient(client);
        receive_message.start();
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
}
