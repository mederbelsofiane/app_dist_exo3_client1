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

public class User implements Runnable {

    private int id;
    private String nom;
    private Socket socket;
    private UserInterfaceController interfaceController;
    public static VBox vbox_message;

    public User(int id, String nom, Socket socket, UserInterfaceController interfaceController) {
        this.id = id;
        this.nom = nom;
        this.socket = socket;
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
            interfaceController.setMessage("You : "+s);
            vbox_message.getChildren().add(root);

        } catch (IOException ex) {
        }
    }

    @Override
    public void run() {
        try {
            //in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            InputStream in = socket.getInputStream();

            DataInputStream clientData = new DataInputStream(in);

            String message;
            while ((message = clientData.readUTF()) != null) {

                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("Message.fxml"));
                load.load();
                Message_Controller Controller = load.getController();
                String m = null;
                if (message.equals("M")) {
                    message = clientData.readUTF();
                    m = AES.decrypt(message, "anis");
                    Text t = new Text(m);
                    t.setFill(Color.BLACK);
                    Controller.setText(t, "#FFFFFF");
                } else {
                    if (message.equals("P")) {

                        String fileName;
                        int bytesRead;
                        fileName = clientData.readUTF();
                        OutputStream output = new FileOutputStream(("received_from_server_" + fileName));
                        long size = clientData.readLong();
                        byte[] buffer = new byte[1024];
                        while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                            output.write(buffer, 0, bytesRead);
                            size -= bytesRead;
                        }
                        output.close();
                        System.out.println(fileName);

                        if (fileName.contains(".PNG") || fileName.contains(".JPG") || fileName.contains(".png") || fileName.contains(".jpg")) {
                            FileInputStream seats_fileInputStream = new FileInputStream(("received_from_server_" + fileName));
                            Image ar = new Image(seats_fileInputStream, 150, 150, false, false);
                            ImageView image = new ImageView(ar);
                            Controller.setImage(image, "#FFFFFF");
                        } else {
                            Button btn = new Button(fileName);
                            btn.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {

                                    FileInputStream seats_fileInputStream = null;
                                    try {
                                        File fm = new File("received_from_server_" + fileName);
                                        seats_fileInputStream = new FileInputStream(fm);
                                        String Extension = getFileExtension(fm);
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
                                    } catch (IOException ex) {
                                    } finally {
                                        try {
                                            seats_fileInputStream.close();
                                        } catch (IOException ex) {
                                        }
                                    }

                                }
                            });
                            Controller.setFile(btn, "#FFFFFF");
                        }
                    }
                }
                HBox root = (HBox) load.getRoot();
                root.setAlignment(Pos.CENTER_LEFT);
                final String s = m;
//                Platform.runLater(new Runnable() {
////                    @Override
////                    public void run() {
////                        getInterfaceController().setMessage(s);
////                        if (UserOnline.getId_user_selected() == id) {
////                            vbox_message.getChildren().add(root);
////                        }
////                    }
//
//                });

            }
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

}
