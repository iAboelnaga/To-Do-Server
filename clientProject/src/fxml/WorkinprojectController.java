/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml;

import Entity.myList;
import com.jfoenix.controls.JFXListView;
import controller.MainFormController;
import controller.TaskCellController;
import controller.WorkInTaskCellController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
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
public class WorkinprojectController implements Initializable {

    @FXML
    private Label Titlelab;
    @FXML
    private Label AsDatelab;
    @FXML
    private Label deadlinelab;
    @FXML
    private JFXListView<Task> ListV;
    static ObservableList<Task> TasksObservableList = FXCollections.observableArrayList();
    public myList listDetails;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ListV.setItems(TasksObservableList);
        ListV.setCellFactory(param -> new WorkInTaskCellController());
    }

    public void transferListDetails(myList list, String flag) {
        this.listDetails = list;
        Titlelab.setText(listDetails.getTitle());
        deadlinelab.setText(listDetails.getDeadlineDate());
        AsDatelab.setText(listDetails.getAssignDate());
        if (flag.equals("main")) {
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
                    UpdatedTask = new Task((res.getJSONObject(i)).getInt("id"), (res.getJSONObject(i)).getString("description"), (res.getJSONObject(i)).getString("bg_color"), (res.getJSONObject(i)).getString("status"), (res.getJSONObject(i)).getInt("listId"), (res.getJSONObject(i)).getInt("userAssignId"), (res.getJSONObject(i)).getString("userAssignName"), (res.getJSONObject(i)).getString("userAssignStatus"));
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
