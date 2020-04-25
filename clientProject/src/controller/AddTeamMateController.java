/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.JsonObject;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;
import server_request.RequestHandler;

/**
 * FXML Controller class
 *
 * @author manal
 */
public class AddTeamMateController implements Initializable {

    @FXML
    private Button invite;
    @FXML
    private TextField teammate_email;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void invite_click(ActionEvent event) {
        
if(!teammate_email.getText().trim().equals(" "))
{
     String Email = teammate_email.getText();
        int id = Sign_In_Controller.UserId;
        JSONObject teamMateRequestObject = new JSONObject();
        try {
            teamMateRequestObject.put("userId", id);
            teamMateRequestObject.put("email", Email);
            String parameters[] = new String[1];
            parameters[0] = "AddTeamMate";
            RequestHandler req = new RequestHandler();

            JSONObject resultJsonObject = req.post(parameters, teamMateRequestObject);

            String res = resultJsonObject.getString("result");
           
            switch (res) {
                case "1":
                    Alert alert0 = new Alert(Alert.AlertType.INFORMATION);
                    alert0.setContentText("insert Successfully ");
                    alert0.showAndWait();
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.close();
                    System.out.println("insert Successfully");

                    break;
                case "2":
                     Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("this request sent before");
                    alert.showAndWait();
                    System.out.println("this request sent before");
                    teammate_email.setText("");
                    break;
                case "3":
                    Alert alert2 = new Alert(Alert.AlertType.ERROR);
                    alert2.setContentText("Ensure the data inserted is correct");
                    alert2.showAndWait();
                    System.out.println("Ensure the data inserted is correct");
                     teammate_email.setText("");
                    break;
                case "4":
                     Alert alert3 = new Alert(Alert.AlertType.ERROR);
                    alert3.setContentText("The user Doesn't exist");
                    alert3.showAndWait();
                     teammate_email.setText("");
                    System.out.println("The user Doesn't exist");
                    break;
                case "5":
                     Alert alert4 = new Alert(Alert.AlertType.WARNING);
                     alert4.setContentText("The user already exist");
                    alert4.showAndWait();
                     teammate_email.setText("");
                    System.out.println("The user already exist");
                    break;

                default:
                    break;
            }

           // Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
           // stage.close();

        } catch (JSONException | IOException ex) {
            Logger.getLogger(AddTeamMateController.class.getName()).log(Level.SEVERE, null, ex);
        }

}
       
    }

}
