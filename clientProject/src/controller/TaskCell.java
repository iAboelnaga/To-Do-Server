/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Task;


/**
 *
 * @author manal
 */
public class TaskCell extends ListCell<Task> {

    VBox vbox = new VBox();
    HBox hbox1 = new HBox();
    HBox hbox2 = new HBox();
    HBox hbox3 = new HBox();
    HBox hbox4 = new HBox();
    Label teammate = new Label();
    Label status = new Label();
    Pane p = new Pane();
    Pane p1 = new Pane();
    TextArea disc = new TextArea();
    ComboBox menu = new ComboBox();
    String option = new String();
    String AssignEmail = new String();
    String[] teamMatesEmails;
    Task updatedTask;

    public TaskCell() {
        super();
        menu.getItems().addAll("Update", "Delete", "Assign to", "Comments");
      

        // disc.setPrefWidth(350);
        disc.setMaxWidth(USE_COMPUTED_SIZE);
        disc.setMinWidth(USE_COMPUTED_SIZE);
        disc.setPrefWidth(370);
        disc.setEditable(false);
        hbox1.getChildren().addAll(status, teammate);
        hbox2.getChildren().addAll(disc);
        hbox3.getChildren().addAll(p, menu);
        hbox4.getChildren().addAll(p1);
        vbox.getChildren().addAll(hbox1, hbox2, hbox3, hbox4);
        hbox1.setHgrow(p, Priority.ALWAYS);

    }

    @Override
    protected void updateItem(Task item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) {
            status.setText(item.getStatus());
            disc.setText(item.getDescription());
            menu.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
                    switch (new_val.intValue()) {
                        case 0:
                           /* updatedTask = new Task();
                            AddTaskController addt = new AddTaskController();
                             OwnedProjectController tasks = loader.getController();
                            tasks.transferListDetails(todoList.get(i));
                           updatedTask = addt.display();
                            System.out.println( updatedTask.getStatus());*/
                            break;
                        case 1:

                            break;
                        case 2:
                            AssignTaskController AssTask = new AssignTaskController();
                            //AssignEmail = AssTask.display(teamMatesEmails);
                            break;
                        case 3:
                            Stage stage = new Stage();
                            Parent root;
                            try {
                                root = FXMLLoader.load(getClass().getResource("Comments.fxml"));

                                Scene scene = new Scene(root, 600, 600);
                                scene.getStylesheets().add(getClass().getResource("style_sheet.css").toExternalForm());
                                stage.setScene(scene);
                                stage.show();
                            } catch (IOException ex) {
                                Logger.getLogger(TaskCell.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            break;

                    }

                }
        );
            setGraphic(vbox);

        } else {
            setText(null);
            setGraphic(null);
        }

    }

}
