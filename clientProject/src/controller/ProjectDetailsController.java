/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Entity.myList;
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;
import server_request.RequestHandler;

/**
 * FXML Controller class
 *
 * @author mahmoud mohamed
 */
public class ProjectDetailsController implements Initializable {

    @FXML
    private AnchorPane deadLinePicker;
    @FXML
    private DatePicker deadlineDate;
    @FXML
    private Button okButton;
    @FXML
    private TextField projectNameTxt;
    myList todo;
    private ArrayList<myList> myLists;
    public static int listId;
    private  int flag = 0;
    private boolean  result = false;
    /**
     * Initializes the controller class.
    */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        todo = new myList();
        myLists = new ArrayList<>();
        // TODO
          okButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {

                if (!projectNameTxt.getText().isEmpty()) {

                    todo.setTitle(projectNameTxt.getText());

                    LocalDate localDate = deadlineDate.getValue();
                    todo.setDeadlineDate(localDate.toString());

                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date date = new Date();
                    System.out.println(dateFormat.format(date));
                    todo.setAssignDate(dateFormat.format(date));

                    if (getToDo().getTitle() != null) {
                        try {
                            if(flag ==1)
                             result = update(createJson(getToDo()));
                            else
                             result = addToServer(createJson(getToDo()));
                            if (result) {
                                try {
                                    Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                    oldStage.close();
                                    Stage newStage = new Stage();
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/OwnedProject.fxml"));
                                    Parent rootFxml = loader.load();
                                    OwnedProjectController tasks = loader.getController();
                                    tasks.transferListDetails(todo, " ");
                                    Scene scene = new Scene(rootFxml, 800, 600);
                                    scene.getStylesheets().add(getClass().getResource("../fxml/style_sheet.css").toExternalForm());
                                    newStage.setScene(scene);
                                    newStage.show();
                                } catch (IOException ex) {
                                    Logger.getLogger(ProjectDetailsController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        } catch (JSONException ex) {
                            Logger.getLogger(ProjectDetailsController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Please enter todo data");
                    alert.showAndWait();
                }
            }
        });    

    }
   private boolean update(JSONObject json) throws JSONException {
        try {
            RequestHandler server = new RequestHandler();
            int jsonResponse = server.put(new String[]{"updateList"}, json);
            if (jsonResponse==1) {
                //currentToDo.setId(jsonResponse.getInt("id"));
                return true;
            } else {
                return false;
            }
        } catch (IOException ex) {
            Logger.getLogger(ProjectDetailsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
 public void formSettings(int flag,myList list) {
        this.flag= flag;
        if(flag ==1)
        {
            okButton.setText("update");
            todo=list;
            
        }
    }
 
    private boolean addToServer(JSONObject json) throws JSONException {
        try {
            RequestHandler server = new RequestHandler();
            JSONObject jsonResponse = server.post(new String[]{"list"}, json);
            if (jsonResponse.has("id")) {
                //listId = jsonResponse.getInt("id");
                  todo.setId(jsonResponse.getInt("id"));
                return true;
            } else {
                listId = -1;
                return false;
            }
        } catch (IOException ex) {
            Logger.getLogger(ProjectDetailsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private JSONObject createJson(myList todo) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("ownerId", Sign_In_Controller.UserId);
        json.put("title", todo.getTitle());
        json.put("assignDate", todo.getAssignDate());
        json.put("deadLineDate", todo.getDeadlineDate());
        json.put("listId",todo.getId());
        return json;
    }

    protected myList getToDo() {
        return todo;
    }
    
    public void setToDo(myList todo) {
        this.todo = todo;
        projectNameTxt.setText(todo.getTitle());
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-M-d");
        //startDate.setValue(LocalDate.parse(todo.getAssignDate(), dtf));
        //endDate.setValue(LocalDate.parse(todo.getDeadlineDate(), dtf));
    }

}
