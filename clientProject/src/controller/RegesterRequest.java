/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Entity.User;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import org.json.JSONException;
import org.json.JSONObject;
import Enums.RESPOND_CODE;
import server_request.RequestHandler;

/**
 *
 * @author Aboelnaga
 */
public class RegesterRequest {

    private User user;
    private String confirm;

    public RegesterRequest(User user, String confirmPassword) {
        this.user = user;
        this.confirm = confirmPassword;
    }

    public RegesterRequest(User user) {
        this.user = user;
    }

    public int registerUser() {
        int result = 0;
        String username = user.getUserName();
        String email = user.getEmail();
        String password = user.getPassword();
        try {
            JSONObject userJsonObject = new JSONObject();
            userJsonObject.put("username", username);
            userJsonObject.put("email", email);
            userJsonObject.put("password", password);
            result = sendRequest(userJsonObject);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        return result;
    }

    public int sendRequest(JSONObject jSONObject) {
        try {
            String[] paramters = new String[1];
            String email = jSONObject.getString("email");
            String userName = jSONObject.getString("username");
            String password = jSONObject.getString("password");
            System.out.println(email + userName + password);
            paramters[0] = "register";

            RequestHandler req = new RequestHandler();

            JSONObject resultJsonObject = req.post(paramters, jSONObject);

            String res = resultJsonObject.getString("result");
            if (res.equals("1")) {
                return 1;
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("You already exist ");
                alert.showAndWait();

            }

        } catch (JSONException ex) {
            Logger.getLogger(RegesterRequest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Connection lost");
            alert.showAndWait();
        }
        return -1;
    }

  
}
