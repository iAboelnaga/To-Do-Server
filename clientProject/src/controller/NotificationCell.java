/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Enums.REQUEST;
import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.NotificationModel;
import javafx.scene.layout.AnchorPane;
import model.TaskState;
import model.TeamMateState;

import org.json.JSONObject;
import server_request.RequestHandler;



/**
 *
 * @author tamimy
 */
public class NotificationCell extends ListCell<Object>{
    
    
    
    @FXML
    private AnchorPane NotificationRow;
    @FXML
    private Label BodyLabel;

    @FXML
    private JFXButton AcceptButton;

    @FXML
    private JFXButton RejectButton;
 
    private FXMLLoader mLLoader;
    
    ListView<Object> listView;

    public NotificationCell(ListView<Object> listView ) {
        super();
        this.listView = listView; // Here I pass the reference to my list. Is it a gd way to do that? 
    }
    
    @Override
    protected void updateItem(Object item, boolean empty) {
        super.updateItem(item, empty);
        if(empty || item == null) {

            setText(null);
            setGraphic(null);

        } else {
            try {
                if (mLLoader == null) {
                    mLLoader = new FXMLLoader(getClass().getResource("/fxml/NotificationCell.fxml"));
                    mLLoader.setController(this);
                    mLLoader.load();
                    
                }
                NotificationModel model = (NotificationModel) item;
                RequestHandler req = new RequestHandler();
                switch(model.getNotificationType())
                {
                    case NotificationModel.TeamMateRequest:
                        
                        BodyLabel.setText(model.getUserName()+" send you a TeamMate request");
                        RejectButton.setVisible(true);
                        AcceptButton.setVisible(true);
                        RejectButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                System.out.println("Reject");
                                String parameters[] = new String[1];
                                parameters[0] = REQUEST.TeamMateState;
                                TeamMateState teamMateState = new TeamMateState(Sign_In_Controller.UserId, model.getSenderId(), "reject");
                                req.post(parameters, new JSONObject(teamMateState));
                                listView.getItems().remove(item);
                            }
                        });
                        AcceptButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                System.out.println("Accept");
                                String parameters[] = new String[1];
                                parameters[0] = REQUEST.TeamMateState;
                                TeamMateState teamMateState = new TeamMateState(Sign_In_Controller.UserId, model.getSenderId(), "accept");
                                JSONObject jsono = req.post(parameters, new JSONObject(teamMateState));
                                listView.getItems().remove(item);
                            }
                        });
                        break;
                    case NotificationModel.AssignTask:
                        BodyLabel.setText(model.getUserName()+" Assign you a Task in Project: "+model.getProjectTitle());
                        RejectButton.setVisible(true);
                        AcceptButton.setVisible(true);
                        
                        RejectButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                System.out.println("Reject");
                                String parameters[] = new String[1];
                                parameters[0] = REQUEST.TaskState;
                                TaskState taskState = new TaskState(Sign_In_Controller.UserId, "reject");
                                req.post(parameters, new JSONObject(taskState));
                                listView.getItems().remove(item);
                            }
                        });
                        AcceptButton.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                System.out.println("Accept");
                                String parameters[] = new String[1];
                                parameters[0] = REQUEST.TaskState;
                                TaskState taskState = new TaskState(Sign_In_Controller.UserId, "accept");
                                req.post(parameters, new JSONObject(taskState));
                                listView.getItems().remove(item);
                            }
                        });
                        break;
                        
                    /*case NotificationModel.TaskIsDone:
                        BodyLabel.setText(model.getTaskName()+" Task is Done");
                        RejectButton.setVisible(false);
                        AcceptButton.setVisible(false);
                        break;*/
                }
                
                setText(null);
                setGraphic(NotificationRow);
            } catch (IOException ex) {
                Logger.getLogger(NotificationCell.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
