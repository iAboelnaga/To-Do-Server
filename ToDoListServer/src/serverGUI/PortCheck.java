/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverGUI;

import connection.Client;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import connection.RequesManager;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Aboelnaga
 */
public class PortCheck {
    
    private ServerSocket jsoServerSocket;
    private static final int JSON_PORT = 5005;
    private static boolean isStart;

    public PortCheck() {
        isStart = false;
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                jsonPortListener();
            }
        });
        th.start();
    }

    private void jsonPortListener() {
        try {
            jsoServerSocket = new ServerSocket(JSON_PORT);

            while (true) {

                Socket s = jsoServerSocket.accept();
                if (getIsStart()) {
                    new RequesManager(s);
                } else {
                    s.close();
                }

            }
        } catch (IOException ex) {
            //1
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("This Posrt Number is In Use Now, Please Close any application using this Posrt Number " + JSON_PORT + " any try again");
                    alert.showAndWait();
                    Platform.exit();

                }

            });
            //2
            System.out.println(ex);
        }
    }

    //stop server operation
    public static void closeServer() {
        isStart = false;

    }

    //start a server operation
    public static void startServer() {
        isStart = true;
    }

    //get isStart value
    private boolean getIsStart() {
        return isStart;
    }

    public static void SendALL(List<Integer> userIDs, String desc) {
        List<Client> clients = new ArrayList<>();
        for (int i = 0; i < userIDs.size(); i++) {
            if (Client.isInVector(userIDs.get(i))) {
                clients.add(Client.getClientById(userIDs.get(i)));
            }
        }
        try {
            JSONObject responseJson = new JSONObject();
            responseJson.put("NotifyTaskDesc", desc);
            for (int i = 0; i < clients.size(); i++) {
                clients.get(i).getPs().println(responseJson.toString());
            }
        } catch (JSONException ex) {
            Logger.getLogger(PortCheck.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
