/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import static controller.TaskCellController.taskId;
import java.io.IOException;
import java.net.URL;
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

/**
 * FXML Controller class
 *
 * @author mahmoud mohamed
 */
public class WorkInTaskCellController extends ListCell<Task> {

    @FXML
    private MenuButton menu;

    @FXML
    private TextField details;
    @FXML
    private Label status;
    @FXML
    private AnchorPane taskCell;
    @FXML
    private Label teamMate;
    private FXMLLoader mLoader;
    MenuItem update = new MenuItem("Update");
    MenuItem comment = new MenuItem("Comments");
     static int taskId;

    public WorkInTaskCellController() {
        super();

    }

    /**
     * Initializes the controller class.
     */
//    @Override
//    public void initialize(URL url, ResourceBundle rb) {
//        // TODO
//        MenuItem delete = new MenuItem("Delete");
//        MenuItem assign = new MenuItem("Assign to");
//       
//       
//    }
    @Override
    protected void updateItem(Task item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null) {
            taskId = item.getId();
            if (mLoader == null) {
                mLoader = new FXMLLoader(getClass().getResource("/fxml/WorkInTaskCell.fxml"));
                mLoader.setController(this);
                try {
                    mLoader.load();
                    status.setText(item.getStatus());
                    details.setText(item.getDescription());
                    teamMate.setText(item.getUserAssignName() + " " + item.getUserAssignStatus());

                    if (item.getUserAssignId() == Sign_In_Controller.UserId && item.getUserAssignStatus().equals("accept")) {
                        menu.getItems().addAll(update, comment);
                        update.setOnAction(new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                //updatedTask = new Task();
                                WorkInUpdateTaskController updatet = new WorkInUpdateTaskController();
                                updatet.display(item.getId(), item.getListId());

                            }
                        });
                    } else {
                        menu.getItems().addAll(comment);
                    }

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
