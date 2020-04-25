/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import Entity.TeamMate;
import Entity.myList;
import static controller.MainFormController.TeamMateObservableList;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import server_request.RequestHandler;

/**
 *
 * @author manal
 */
public class AssignTaskController  {

    public String Email = new String();
    public ComboBox comboBox;
    public Label label;
    public Button button;
    String[] teamMatesEmails;
    public ObservableList<String> teamMatesEmails2;
    int userId;
    int task_id;
    int userAssignedId;
    int listId;
    int flag = 0;
    public ObservableList<TeamMate> TeamMateObservableList;
    
    public AssignTaskController()
    {
        TeamMateObservableList = FXCollections.observableArrayList();
    }
        public int display(int taskId, int userAssign_id, int List_id) {

        Stage stage1 = new Stage();
        stage1.initModality(Modality.APPLICATION_MODAL);
        AnchorPane loyout = new AnchorPane();
        comboBox = new ComboBox();
        label = new Label();
        button = new Button();
        task_id = taskId;
        listId = List_id;
        userAssignedId = userAssign_id;
        teamMatesEmails2 = FXCollections.observableArrayList();
        
        loyout.setId("AnchorPane");
        loyout.setPrefHeight(400.0);
        loyout.setPrefWidth(600.0);

        comboBox.setLayoutX(123.0);
        comboBox.setLayoutY(74.0);
        comboBox.setPrefHeight(25.0);
        comboBox.setPrefWidth(328.0);
        try {
                        String parameters[] = new String[2];
                        parameters[0] = "GetTeamMates";
                        int id = Sign_In_Controller.UserId;
                        parameters[1] = Integer.toString(id);
                        RequestHandler req = new RequestHandler();
                        JSONObject resultJsonObject = req.get(parameters);
                        try {
                            JSONArray res = resultJsonObject.getJSONArray("result");
                            for (int i = 0; i < res.length(); i++) {
                                System.out.println(i);
                                TeamMate friend = new TeamMate((res.getJSONObject(i)).getString("teamMateName"), (res.getJSONObject(i)).getInt("teamMateId"), (res.getJSONObject(i)).getString("teamMateStatus"), (res.getJSONObject(i)).getBoolean("online"));
                                TeamMateObservableList.add(friend);
                                System.out.println(TeamMateObservableList.isEmpty());
                            }

                            System.out.println(res);
                        } catch (JSONException ex) {
                            Logger.getLogger(MainFormController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    } catch (IOException ex) {
                        Logger.getLogger(MainFormController.class.getName()).log(Level.SEVERE, null, ex);
                    }
        
        if(!TeamMateObservableList.isEmpty()){
        for(int i = 0; i < TeamMateObservableList.size(); i++)
        {
            if(TeamMateObservableList.get(i).getTeamMateStatus().equals("accept"))
            {
                teamMatesEmails2.add(i, TeamMateObservableList.get(i).getTeamMateName());
                System.out.println("dsjfdshdskjfjh");
                System.out.println(teamMatesEmails2.get(i));
            }
            //MainFormController.TeamMateObservableList.get(0).getTeamMateId();
        }
        }
        //ObservableList<String> oListStavaka = FXCollections.observableArrayList(teamMatesEmails);
            //System.out.println(teamMatesEmails2.get(0));
        comboBox.setItems(teamMatesEmails2);
        
        comboBox.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
                    switch (new_val.intValue()) {
                        case 0:
                            break;
                        case 1:
                            break;
                        case 2:
                            break;

                    }
                    for(int i=0; i<=teamMatesEmails2.size(); i++)
                    {
                        if(i == new_val.intValue())
                        {
                            userId = TeamMateObservableList.get(i).getTeamMateId();
                        }
                    }
                }
        );

        label.setLayoutX(41.0);
        label.setLayoutY(28.0);
        label.setPrefHeight(25.0);
        label.setPrefWidth(196.0);
        label.setText("Assign to :");
        label.setFont(new Font(18.0));

        button.setLayoutX(192.0);
        button.setLayoutY(238.0);
        button.setMnemonicParsing(false);
        button.setPrefHeight(46.0);
        button.setPrefWidth(204.0);
        button.setText("Ok");
        if(userAssign_id == 0)
        {
           
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                try {
                    boolean result = addToServer(createJson());
                    flag = 1;
                    if(result)
                    {
                        
                        System.out.println("assigned");
                    }
                    stage1.close();
                } catch (JSONException ex) {
                    Logger.getLogger(AssignTaskController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }); 
        }
        else
        {
            button.setDisable(true);
             
        }
        loyout.getChildren().add(comboBox);
        loyout.getChildren().add(label);
        loyout.getChildren().add(button);

        Scene scene1 = new Scene(loyout);
        stage1.setScene(scene1);
        stage1.showAndWait();
        return flag;
        
    }
        private JSONObject createJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("userId", task_id);
        json.put("ItemId", userId);
        json.put("listId", listId);
        return json;
    }
        private boolean addToServer(JSONObject json) throws JSONException {
        try {
            RequestHandler server = new RequestHandler();
            JSONObject jsonResponse = server.post(new String[]{"assignTask"}, json);
            if (jsonResponse.has("id")) {
                return true;
            } else {
                return false;
            }
        } catch (IOException ex) {
            Logger.getLogger(ProjectDetailsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

}
