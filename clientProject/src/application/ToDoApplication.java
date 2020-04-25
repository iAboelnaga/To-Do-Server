/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import controller.Sign_In_Controller;
import controller.SignupController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author mahmoud mohamed
 */
public class ToDoApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        try {
            // TODO
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/Sign_In.fxml"));
            Parent root = (Parent) loader.load();
            Sign_In_Controller signInController = loader.getController();
            //signInController.setStage(stage);
            Scene scene = new Scene(root,800,600);
            scene.getStylesheets().add(getClass().getResource("../fxml/style_sheet.css").toExternalForm());
            stage.setTitle("Sign In");
            stage.setScene(scene);

            stage.show();
            
        } catch (IOException ex) {
            Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

