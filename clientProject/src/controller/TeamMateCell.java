/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Entity.TeamMate;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import model.NotificationModel;
import org.json.JSONArray;
import org.json.JSONObject;
import server_request.RequestHandler;

/**
 * FXML Controller class
 *
 * @author manal
 */
public class TeamMateCell extends ListCell<TeamMate> {

    @FXML
    private AnchorPane Row;
    @FXML
    private Label teamMateLable;
    @FXML
    private ImageView statusImg;
    @FXML
    private FXMLLoader mLLoader;
    @FXML
    private ImageView deleteimV;
    @FXML
    private Label stateLable;

    @Override
    protected void updateItem(TeamMate item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {

            setText(null);
            setGraphic(null);

        } else {
            if (mLLoader == null) {
                mLLoader = new FXMLLoader(getClass().getResource("/fxml/TeamMateCell.fxml"));
                mLLoader.setController(this);
                try {
                    mLLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            teamMateLable.setText(item.getTeamMateName());
            if (item.getTeamMateStatus().equals("wait")) {
                stateLable.setText("Painding");
            } else {
                stateLable.setText(null);
            }
            if (item.isOnline()) {
                Image img = new Image("/fxml/online.png");
                statusImg.setImage(img);
            }
            else
            {
                Image img = new Image("/fxml/offline.png");
                statusImg.setImage(img);
            }

            Image img2 = new Image("/fxml/delete.png");
            deleteimV.setImage(img2);
            deleteimV.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    System.out.println("remove" + item.getTeamMateId());

                    try {
                        String parameters[] = new String[3];
                        parameters[0] = "RemoveTeamMate";
                        parameters[1] = Integer.toString(Sign_In_Controller.UserId);
                        parameters[2] = Integer.toString(item.getTeamMateId());
                        RequestHandler req = new RequestHandler();
                        JSONObject resultJsonObject = req.delete(parameters);
                        System.out.println(resultJsonObject);
                        MainFormController.TeamMateObservableList.remove(getIndex());
                    } catch (IOException ex) {
                        Logger.getLogger(TeamMateCell.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            });

            setGraphic(Row);
        }
    }

}
