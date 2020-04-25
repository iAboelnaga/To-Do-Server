/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import serverEntity.User;
import DataBase.DataBaseHandler;
import Enums.REQUEST;
import connection.Requests;
import Enums.REQUEST;

/**
 *
 * @author Aboelnaga
 */
public class RequesManager extends Thread{
    
    private BufferedReader in;
    private PrintStream ps;
    private Socket s;


    public BufferedReader getBufferReader() {
        return in;
    }

    public PrintStream getPrintStream() {
        return ps;
    }

    public Socket getS() {
        return s;
    }

    public RequesManager(Socket s) {
        try {
            this.s = s;
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            ps = new PrintStream(s.getOutputStream());
            start();
        } catch (IOException ex) {
            System.out.println("error cleint out");
            ex.printStackTrace();
       
        }

    }

    @Override
    public void run() {
        Requests request = new Requests();
        boolean connected = true;
        while (connected) {
            try {
                // first read http clientRequest type(GET , POST,PUT,DELETE);
                String clientRequest = in.readLine();
                String[] paramter = in.readLine().split("/");

                switch (clientRequest) {
                    case REQUEST.POST:
                        JSONObject requestJson = readJson();

                        JSONObject responseJson = request.post(paramter, requestJson, this);
                        ps.println(responseJson.toString());
                        // to notifay the client the response was ended 
                        ps.println(REQUEST.END);
                        break;

                    case REQUEST.GET:
                        responseJson = request.get(paramter);
                        ps.println(responseJson.toString());
                        // to notifay the client the response was ended 
                       ps.println(REQUEST.END);
                        break;
                    case REQUEST.PUT:
                        requestJson = readJson();
                        int response = request.put(paramter, requestJson);
                        ps.println(response);
                        // ps.println(REQUEST.END);
                        break;
                    case REQUEST.DELETE:
                        JSONObject requestJson2 = request.delete(paramter);
                        ps.println(requestJson2.toString());
                        ps.println(REQUEST.END);


                        //ps.println(REQUEST.END);
                        break;
                    case REQUEST.LOGOUT:
                        int id = Integer.parseInt(paramter[1]);
                        //ArrayList<User> friends = repository.getUserFriends(id);
                        //User user = repository.getUserData(id);

                        //Client.notifiyFriends(user, friends, REQUEST.FRIEND_OFFLINE);
                        Client.removeClient(id);
                }
            } catch (IOException | JSONException ex) {
                ex.printStackTrace();
                connected = false;
            }
        }
    }

    JSONObject readJson() throws IOException, JSONException {
        StringBuilder body = new StringBuilder();
        String data = in.readLine();

        while (!data.equals(REQUEST.END)) {
            body.append(data);
            data = in.readLine();

        }
        System.out.println("json sent "+ body.toString());
        return new JSONObject(body.toString());
    }

    private void close() throws IOException {
        in.close();
        s.close();
    }
}
