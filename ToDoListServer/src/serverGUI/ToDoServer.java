/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverGUI;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 *
 * @author Aboelnaga
 */
public class ToDoServer extends Application{
    
    public static ServerGuiController controller;
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/serverGUI/ServerGui.fxml"));
        Parent root = (Parent) loader.load();

        controller = loader.getController();
        controller.setStage(stage);
        Scene scene = new Scene(root);
        stage.setScene(scene);
       
        
        stage.setScene(scene);
        stage.show();
        new PortCheck();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event1) {
                            try {
                                //PortCheck.closeServer();
                                System.exit(0);
                            } catch (Exception ex) {
                                Logger.getLogger(ServerGuiController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
