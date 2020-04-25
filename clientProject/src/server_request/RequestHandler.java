/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_request;

import Enums.REQUEST;
import com.sun.corba.se.spi.activation.Server;
import controller.Sign_In_Controller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.json.JSONException;
import org.json.JSONObject;
import server_conection.Connection;

/**
 *
 * @author Aboelnaga
 */
public class RequestHandler implements Request{
    
    private static final String IP = "127.0.0.1";
    private static final int PORT = 5005;
    Socket socket;
    PrintStream ps;
    BufferedReader in;
    Sign_In_Controller signInController;
    private static Listener listener;

    public RequestHandler() throws IOException {
        //socket = new Socket(IP, PORT);
        socket = Connection.getSocketConnection();
        ps = new PrintStream(socket.getOutputStream());
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        if (listener == null || !listener.isAlive()) {
            startnewThread();
        }
    }

    @Override
    public JSONObject post(String[] paramters, JSONObject body) {
        
        ps.println(REQUEST.POST);
        
        for (String paramter : paramters) {
            ps.print("/");
            ps.print(paramter);
        }
        
        ps.println();
        ps.println(body.toString());
        
        // to notifay the client the response was ended 
        ps.println(REQUEST.END);

        JSONObject json = null;
        try {
            listener.readJson = true;
            listener.serverResoponse = true;

            // waiting for responde
            listener.join();
            json = listener.json;
        } catch (InterruptedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            startnewThread();
            return json;
        }
    }

    @Override
    public JSONObject get(String[] paramters) {
        ps.println(REQUEST.GET);
        for (String paramter : paramters) {
            ps.print("/");
            ps.print(paramter);
        }
        ps.println();
        
        JSONObject json = null;
        try {
            // waiting for responde
            listener.readJson = true;
            listener.serverResoponse = true;

            listener.join();
            json = listener.json;

        } catch (InterruptedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //even if anything happens
            startnewThread();
            return json;
        }
    }

    @Override
    public int put(String[] paramters, JSONObject body) {
        ps.println(REQUEST.PUT);
        for (String paramter : paramters) {
            ps.print("/");
            ps.print(paramter);
        }
        ps.println();
        ps.println(body.toString());
        // to notifay the client the response was ended 
        ps.println(REQUEST.END);

        int response = 0;

        listener.readJson = false;
        listener.serverResoponse = true;
        try {
            listener.join();
        } catch (InterruptedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        response = Integer.parseInt(listener.data);
        startnewThread();
        return response;
    }
    public void logOut() {
        ps.println(REQUEST.LOGOUT);

        ps.print("/");

        ps.print(Sign_In_Controller.UserId);

        ps.println();
        //listener.stop();
        System.exit(0);
    }

     @Override
    public JSONObject delete(String[] paramters) {
          ps.println(REQUEST.DELETE);
        
        for (String paramter : paramters) {
            ps.print("/");
            ps.print(paramter);
        }
        ps.println();
        //ps.println(REQUEST.END);

        JSONObject json = null;
        try {
            listener.readJson = true;
            listener.serverResoponse = true;

            // waiting for responde
            listener.join();
            json = listener.json;
        } catch (InterruptedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            startnewThread();
            return json;

        }

    }
    
    private void startnewThread() {
        listener = new Listener();
        listener.start();
    }

    private class Listener extends Thread {

        String data;
        JSONObject json;
        boolean readJson = false;
        boolean serverResoponse = false;
        String type;

        @Override
        public void run() {
            try {

                data = in.readLine();
                if(data == null){
                    serverResoponse = true;
                    close();
                }
                // read if notification at real time not server response
                if (!serverResoponse) {
                    type = data;
                    data = "";
                    readJson();
                    //Object object = NotificationFactory.getNotificationObject(type, json);
                    // method send object to view that responsable for deal with it
                    Platform.runLater(() -> {
                        //sendOjbectToView(type, object);
                    });
                    startnewThread();
                }
                if (readJson) {
                    readJson();
                }
            } catch (IOException ex) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Connection Lost ");
                    alert.showAndWait();
                    try {
                        close();
                        System.exit(0);
                    } catch (IOException ex1) {
                        System.out.println(ex1.getMessage());
                    }
                });
            } catch (JSONException ex) {
                Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        void readJson() throws IOException, JSONException {
            StringBuilder body = new StringBuilder();
            System.out.println(data);

            while (!data.equals(REQUEST.END)) {

                body.append(data);
                data = in.readLine();

            }
            System.out.println(body.toString());
                json = new JSONObject(body.toString());
        }

        private void close() throws IOException {
            socket.close();
            in.close();
            ps.close();
        }
        private void readJsonFromServer() {
            try {
                JSONObject nObject = new JSONObject(data);
                System.out.println("Task is "+nObject.get("NotifyTaskDesc"));
                Notifications notifications =Notifications.create().darkStyle().position(Pos.TOP_RIGHT)
                        .title("Task Is Done")
                        .graphic(new ImageView(new Image("/fxml/task_done.png")))
                        .text("Task Description : "+nObject.get("NotifyTaskDesc"))
                         .hideAfter(Duration.seconds(15));
                         notifications.show();
            } catch (JSONException ex) {
                Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
