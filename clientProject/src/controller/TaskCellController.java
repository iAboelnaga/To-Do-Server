/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Entity.TeamMate;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import static controller.MainFormController.TeamMateObservableList;
import static controller.OwnedProjectController.TasksObservableList;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.NotificationModel;
import model.Task;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import server_request.RequestHandler;

/**
 * FXML Controller class
 *
 * @author mahmoud mohamed
 */
public class TaskCellController extends ListCell<Task> {

    @FXML
    private MenuButton menu;
    @FXML
    private MenuItem update;
    @FXML
    private MenuItem delete;
    @FXML
    private MenuItem AssignTo;
    @FXML
    private MenuItem comment;

    @FXML
    private TextField details;
    @FXML
    private Label status;
    @FXML
    private AnchorPane taskCell;
    @FXML
    private Label teamMate;
    private FXMLLoader mLoader;
    Task updatedTask;
    String AssignEmail = new String();
    String[] teamMatesEmails;
    static int taskId;

    public TaskCellController() {
        super();

    }

    /**
     * Initializes the controller class.
     */
//    @Override
//    public void initialize(URL url, ResourceBundle rb) {
//        // TODO
//        MenuItem update = new MenuItem("Update");
//        MenuItem delete = new MenuItem("Delete");
//        MenuItem assign = new MenuItem("Assign to");
//        MenuItem comment = new MenuItem("Comments");
//        menu.getItems().addAll(update, delete, assign, comment);
//    }
    @Override
    protected void updateItem(Task item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null) {
            taskId = item.getId();
            if (mLoader == null) {
                mLoader = new FXMLLoader(getClass().getResource("/fxml/TaskCell.fxml"));
                mLoader.setController(this);
                try {
                    mLoader.load();
                    status.setText(item.getStatus());
                    details.setText(item.getDescription());
                    teamMate.setText(item.getUserAssignName() + " " + item.getUserAssignStatus());

                    update.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            updatedTask = new Task();
                            UpdateTaskController updatet = new UpdateTaskController();
                            int resu = updatet.display(item.getId(), item.getListId());
                            if (resu == 1) {

                                OwnedProjectController.TasksObservableList.clear();
                                try {
                                    String parameters[] = new String[2];
                                    parameters[0] = "GetTasks";

                                    parameters[1] = Integer.toString(item.getListId());
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
                    });
                    delete.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            System.out.println("remove task" + taskId);

                            try {
                                String parameters[] = new String[2];
                                parameters[0] = "RemoveTask";
                                parameters[1] = Integer.toString(taskId);
                                RequestHandler req = new RequestHandler();
                                JSONObject resultJsonObject = req.delete(parameters);
                                System.out.println(resultJsonObject);
                                int f = resultJsonObject.getInt("result");
                                if(f==1)
                                {
                                    OwnedProjectController.TasksObservableList.clear();
                                    String parameters2[] = new String[2];
                                    parameters2[0] = "GetTasks";
                                    parameters2[1] = Integer.toString(item.getListId());
                                    JSONObject resultJsonObject2 = req.get(parameters2);
                                    try {
                                        JSONArray res = resultJsonObject2.getJSONArray("result");
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
                                }
                            } catch (IOException ex) {
                                Logger.getLogger(TeamMateCell.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (JSONException ex) {
                                Logger.getLogger(TaskCellController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                    });
                    AssignTo.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            AssignTaskController AssTask = new AssignTaskController();
                            int f = AssTask.display(item.getId(), item.getUserAssignId(), item.getListId());

                            if (f == 1) {

                                OwnedProjectController.TasksObservableList.clear();
                                try {
                                    String parameters[] = new String[2];
                                    parameters[0] = "GetTasks";

                                    parameters[1] = Integer.toString(item.getListId());
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
                    });
                    comment.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            Stage stage = new Stage();
                            Parent root;
                            try {

                                root = FXMLLoader.load(getClass().getResource("../fxml/Comments.fxml"));

                                Scene scene = new Scene(root, 600, 600);
                                scene.getStylesheets().add(getClass().getResource("../fxml/style_sheet.css").toExternalForm());
                                stage.setScene(scene);
                                stage.show();
                            } catch (IOException ex) {
                                Logger.getLogger(TaskCell.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                    });
                    setGraphic(taskCell);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

    }

}
