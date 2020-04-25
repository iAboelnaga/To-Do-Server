/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Entity.TeamMate;
import controller.CommentCell;
import static controller.MainFormController.TeamMateObservableList;
import static controller.TaskCellController.taskId;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import model.CommentsModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import server_request.RequestHandler;

/**
 * FXML Controller class
 *
 * @author manal
 */
public class CommentsController implements Initializable {

    CommentsModel Cobj;

    @FXML
    private ListView<CommentsModel> LisV;
    ObservableList<CommentsModel> commentObservableList = FXCollections.observableArrayList();
    CommentsModel comment;

    @FXML
    private TextArea textAr;
    @FXML
    private Button BtnSend;

    public CommentsController() {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {

            String parameters[] = new String[2];
            parameters[0] = "GetComments";
            parameters[1] =  Integer.toString(TaskCellController.taskId) ;
            RequestHandler req = new RequestHandler();
            JSONObject resultJsonObject = req.get(parameters);

            JSONArray res = resultJsonObject.getJSONArray("result");
            for (int i = 0; i < res.length(); i++) {
                System.out.println(i);
                CommentsModel com = new CommentsModel((res.getJSONObject(i)).getInt("taskId"),(res.getJSONObject(i)).getInt("userId"), (res.getJSONObject(i)).getString("userName"),(res.getJSONObject(i)).getString("commentDate"), (res.getJSONObject(i)).getString("commentText"));
               commentObservableList.add(com);

            }

            System.out.println(res);

        } catch (IOException ex) {
            Logger.getLogger(CommentsController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            Logger.getLogger(CommentsController.class.getName()).log(Level.SEVERE, null, ex);
        }

        LisV.setItems(commentObservableList);
        LisV.setCellFactory(param -> new CommentCell());
    }

    @FXML
    private void BtnSend_Action(ActionEvent event) {
        
        
        long millis = System.currentTimeMillis();
        java.sql.Date date = new java.sql.Date(millis);
        System.out.println(date);
        int taskId = TaskCellController.taskId;
        if (!textAr.getText().trim().equals(" ")) {
            comment = new CommentsModel( taskId, Sign_In_Controller.UserId, MainFormController.Name, date.toString(), textAr.getText());
            commentObservableList.add(comment);
        }
        JSONObject AddCommentObject = new JSONObject();
        try {
           AddCommentObject.put("taskId", taskId);
            AddCommentObject.put("userId", Sign_In_Controller.UserId);
            AddCommentObject.put("userName", MainFormController.Name);
            AddCommentObject.put("commentDate", date.toString());
           AddCommentObject.put("commentText", textAr.getText());
            
            String parameters[] = new String[1];
            parameters[0] = "AddComment";
            RequestHandler req = new RequestHandler();

            JSONObject resultJsonObject = req.post(parameters,AddCommentObject );

            String res = resultJsonObject.getString("result");
           switch (res) {
                case "1":
                    System.out.println("insert Successed");
                    break;
                case "2":
                    System.out.println("insert failed");
                    break;
           }
           
            textAr.setText(" ");
    }   catch (JSONException | IOException ex) {
            Logger.getLogger(CommentsController.class.getName()).log(Level.SEVERE, null, ex);
        }

}
}
