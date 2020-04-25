/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Entity.myList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import fxml.ShowProgressController;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Task;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import server_request.RequestHandler;

/**
 * FXML Controller class
 *
 * @author manal
 */
public class OwnedProjectController implements Initializable {

    @FXML
    private Button btnAddTask;
    @FXML
    private MenuButton menue_id;
    @FXML
    private Label titleLbl;
    @FXML
    private Label assignDateLbl;
    @FXML
    private Label deadlineDateLbl;

    @FXML
    private ListView<Task> ListV;
    static ObservableList<Task> TasksObservableList = FXCollections.observableArrayList();
    Task t;
    @FXML
    private Button ShowProg;
    private RequestHandler server;
    private ArrayList<Task> tasksList;
    public myList listDetails;

    public OwnedProjectController() {
        // getTasks();

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ListV.setItems(TasksObservableList);
        ListV.setCellFactory(param -> new TaskCellController());
    }

    @FXML
    private void BtnAdd_Action(ActionEvent event) {
        AddTaskController addt = new AddTaskController();
         addt.display(listDetails.getId());
       
            TasksObservableList.clear();
            getTasks();
        

    }

    @FXML
    private void ShowProg_Action(ActionEvent event) {
        Stage stage = new Stage();
        Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("ProjectChart");
        stage.initOwner(oldStage);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/ShowProgress.fxml"));
            Parent root = loader.load();
            ShowProgressController show = loader.getController();
            show.fillData(TasksObservableList);
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(MainFormController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

   public void transferListDetails(myList list ,String flag) {
        this.listDetails = list;
        titleLbl.setText(listDetails.getTitle());
        deadlineDateLbl.setText(listDetails.getDeadlineDate());
        assignDateLbl.setText(listDetails.getAssignDate());
        if(flag.equals("main"))
        {
            TasksObservableList.clear();
            getTasks();
        }
    }

    public void getTasks() {
        try {
            String parameters[] = new String[2];
            parameters[0] = "GetTasks";
            
            int ListId = listDetails.getId();
            parameters[1] = Integer.toString(ListId);
            RequestHandler req = new RequestHandler();
            JSONObject resultJsonObject = req.get(parameters);
            try {
                JSONArray res = resultJsonObject.getJSONArray("result");
                for (int i = 0; i < res.length(); i++) {
                    System.out.println(i);
                    Task UpdatedTask;
                    UpdatedTask = new Task((res.getJSONObject(i)).getInt("id"),(res.getJSONObject(i)).getString("description"), (res.getJSONObject(i)).getString("bg_color"), (res.getJSONObject(i)).getString("status"), (res.getJSONObject(i)).getInt("listId"), (res.getJSONObject(i)).getInt("userAssignId"), (res.getJSONObject(i)).getString("userAssignName"), (res.getJSONObject(i)).getString("userAssignStatus"));
                    TasksObservableList.add(UpdatedTask);

                }

                System.out.println(res);
            } catch (JSONException ex) {
                Logger.getLogger(MainFormController.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (IOException ex) {
            Logger.getLogger(MainFormController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
