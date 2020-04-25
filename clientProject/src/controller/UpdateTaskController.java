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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Task;
import org.json.JSONException;
import org.json.JSONObject;
import server_request.RequestHandler;

/**
 *
 * @author manal
 */
public class UpdateTaskController {

    public TextArea textArea;
    public ColorPicker colorPicker;
    public ComboBox comboBox;
    public Label label;
    public Button button;
    public Label label0;
    public Label label1;
    public Stage stage;
    public static Task task;
    protected String status;
    String colorVal = "White";
    protected Color col ;
    int flag =0;

    public int display(int taskId ,int listId) {

        Stage stage1 = new Stage();
        stage1.initModality(Modality.APPLICATION_MODAL);
        AnchorPane loyout = new AnchorPane();
        textArea = new TextArea();
        textArea.setLayoutX(24.0);
        textArea.setLayoutY(73.0);
        textArea.setPrefHeight(73.0);
        textArea.setPrefWidth(302.0);
        textArea.setPromptText("Enter Task Desc");
        colorPicker = new ColorPicker();
        colorPicker.setLayoutX(161.0);
        colorPicker.setLayoutY(208.0);
        colorPicker.setPrefHeight(25.0);
        colorPicker.setPrefWidth(146.0);
        colorPicker.setOnAction((ActionEvent e) -> {
            col = colorPicker.getValue();
            colorVal = col.toString();
        });
        comboBox = new ComboBox();
        comboBox.setLayoutX(159.0);
        comboBox.setLayoutY(274.0);
        comboBox.setPrefWidth(150.0);
        comboBox.getItems().addAll("TO DO", "IN PROGRESS", "DONE");
        comboBox.getSelectionModel().selectedIndexProperty().addListener(
                (ObservableValue<? extends Number> ov, Number old_val, Number new_val) -> {
                    switch (new_val.intValue()) {
                        case 0:
                            status = "to do";
                            break;
                        case 1:
                            status = "in progress";
                            break;
                        case 2:
                            status = "done";
                            break;

                    }

                }
        );

        comboBox.getSelectionModel().selectFirst();

        label = new Label();
        label.setLayoutX(35.0);
        label.setLayoutY(249.0);
        label.setPrefHeight(25.0);
        label.setPrefWidth(146.0);
        label.setText("Choose The Status :");
        label.setFont(new Font(14.0));

        button = new Button();
        button.setLayoutX(122.0);
        button.setLayoutY(334.0);
        button.setMnemonicParsing(false);
        button.setPrefHeight(37.0);
        button.setPrefWidth(108.0);
        button.setText("Update Card ");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!textArea.getText().trim().equals(" ")) {
                    task = new Task(taskId, textArea.getText(), colorVal, status, listId);
                    JSONObject AddTaskRequestObject = new JSONObject();
                    try {
                        AddTaskRequestObject.put("id",taskId);
                        AddTaskRequestObject.put("description", textArea.getText());
                        AddTaskRequestObject.put("bg_color", colorVal);
                        AddTaskRequestObject.put("status", status);
                        AddTaskRequestObject.put("listId", listId);
                        String parameters[] = new String[1];
                        parameters[0] = "UpdateTask";
                        RequestHandler req = new RequestHandler();

                        JSONObject resultJsonObject = req.post(parameters, AddTaskRequestObject);

                        String res = resultJsonObject.getString("result");

                        switch (res) {
                            case "1":
                                Alert alert0 = new Alert(Alert.AlertType.INFORMATION);
                                alert0.setContentText("Update Task Successfully ");
                                alert0.showAndWait();
                                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                stage.close();
                                System.out.println("Update Task Successfully");
                                flag =1;
                                   
                                break;
                            case "2":
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setContentText("Update Task failed");
                                alert.showAndWait();
                                System.out.println("Update Task failed");
                                
                        }

                    } catch (JSONException | IOException ex) {
                        Logger.getLogger(AddTeamMateController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            }
        });
        label0 = new Label();
        label0.setLayoutX(34.0);
        label0.setLayoutY(180.0);
        label0.setPrefHeight(17.0);
        label0.setPrefWidth(125.0);
        label0.setText("Choose the color :");
        label0.setFont(new Font(14.0));

        label1 = new Label();
        label1.setAlignment(javafx.geometry.Pos.CENTER);
        label1.setLayoutX(56.0);
        label1.setLayoutY(14.0);
        label1.setPrefHeight(37.0);
        label1.setPrefWidth(206.0);
        label1.setText("Update Task");
        label1.setFont(new Font(18.0));
        loyout.setId("AnchorPane");
        loyout.setPrefHeight(404.0);
        loyout.setPrefWidth(352.0);
        loyout.getChildren().add(textArea);
        loyout.getChildren().add(colorPicker);
        loyout.getChildren().add(comboBox);
        loyout.getChildren().add(label);
        loyout.getChildren().add(button);
        loyout.getChildren().add(label0);
        loyout.getChildren().add(label1);
        Scene scene1 = new Scene(loyout);
        stage1.setScene(scene1);
        stage1.showAndWait();
        return flag;
        
    }

}
