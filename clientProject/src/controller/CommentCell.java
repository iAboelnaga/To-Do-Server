/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import javafx.scene.layout.VBox;
import model.CommentsModel;

/**
 *
 * @author manal
 */
public class CommentCell extends ListCell<CommentsModel> {
    
      VBox vbox =new VBox();
       HBox hbox1 =new HBox();
       HBox hbox2 =new HBox();
       HBox hbox3 =new HBox();
       HBox hbox4 =new HBox();
       Label teammate =new Label();
       Label Date =new Label();
       Pane p =new Pane();
       Pane p1 =new Pane();
       TextArea comment =new TextArea();
       

          public CommentCell() {
            super();
           // disc.setPrefWidth(350);
            comment.setMaxWidth(USE_COMPUTED_SIZE);
            comment.setMinWidth(USE_COMPUTED_SIZE);
            comment.setPrefWidth(370);
            comment.setEditable(false);
            hbox1.getChildren().addAll(teammate,p,Date);
            hbox2.getChildren().addAll(comment);
            vbox.getChildren().addAll(hbox1,hbox2);
            hbox1.setHgrow(p, Priority.ALWAYS);
    

        }
       
        
       @Override
    protected void updateItem(CommentsModel item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) {
            Date.setText(item.getCommentDate()); 
            comment.setText(item.getCommentText());
            teammate.setText(item.getUserName());
            setGraphic(vbox);
        
          }
        else
        {
            setText(null);
            setGraphic(null);
        }

}
     
        
    
}
