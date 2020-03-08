/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app_dist_ex03_client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserConnected extends Thread {

    private Socket User;
    private UserOnline list_user_online;
    private final String secretKey = "Sofiane&Test&Anis.";

    public UserConnected(Socket User, UserOnline list_user_online) {
        this.User = User;
        this.list_user_online = list_user_online;
    }

    @Override
    public void run() {
        try {
            String message, Password = "anis";
           // BufferedReader in = new BufferedReader(new InputStreamReader(User.getInputStream()));
            InputStream in = User.getInputStream();

            DataInputStream clientData = new DataInputStream(in);
            do {
                message = clientData.readUTF();
                message =  AES.decrypt(message, "anis");
                System.out.println(message);
            } while (!(Password.equals(message)));
            System.out.println("User " +   1);
            list_user_online.addUser(new User( 1, "User " + 1, User,null));
        } catch (IOException ex) {
            
        }

    }

}
