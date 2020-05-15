package app_dist_ex03_client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class ReceiveMessage extends Thread {

    Socket client;

    BufferedReader in;
    VBox vbox_message;
    String Password;
    UserOnline list_user_online;
    private UserInterfaceController interfaceController;

    public ReceiveMessage(VBox vbox_message, UserOnline list_user_online) {
        this.vbox_message = vbox_message;
        this.list_user_online = list_user_online;
    }

    @Override
    public void run() {
        try {
            //in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            InputStream in = client.getInputStream();

            DataInputStream clientData = new DataInputStream(in);

            String message;
            while ((message = clientData.readUTF()) != null) {
                System.out.println(message);
                FXMLLoader load = new FXMLLoader();
                load.setLocation(getClass().getResource("Message.fxml"));
                load.load();
                Message_Controller Controller = load.getController();
                int id_u = -1;
                String m = null;

                if (message.equals("M")) {
                    id_u = clientData.readInt();
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
                            Controller.setFile(btn, "#FFFFFF");
                        }
                    }
                }

                HBox root = (HBox) load.getRoot();
                root.setAlignment(Pos.CENTER_LEFT);
                final String s = m;
                User u = list_user_online.getUserID(id_u);
                System.out.println(id_u);
                if (u != null) {
                    u.list_message.add(root);
                     
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            //                     getInterfaceController().setMessage(s);
                            u.getInterfaceController().setMessage(s);
                            if (UserOnline.getId_user_selected() == u.getId()) {
                               
                                vbox_message.getChildren().add(root);
                            }
                        }

                    });
                }
            }
        } catch (IOException ex) {
            System.exit(0);
        }

    }

    public void setClient(Socket client) {
        this.client = client;
    }

    public UserInterfaceController getInterfaceController() {
        return interfaceController;
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
