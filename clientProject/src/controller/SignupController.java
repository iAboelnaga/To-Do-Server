/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Entity.User;
import com.sun.java.swing.plaf.windows.resources.windows;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import Helper.Validate;
import javafx.scene.control.Button;
import model.DataValidation;

/**
 * FXML Controller class
 *
 * @author mahmoud mohamed
 */
public class SignupController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private Stage stage;
    @FXML
    BorderPane container;
    @FXML
    VBox leftSide;
    @FXML
    VBox centerBox;
    @FXML
    TextField userNameField;
    @FXML
    TextField userEmailField;
    @FXML
    TextField userPassword;
    @FXML
    TextField userRePassword;
    @FXML
    private Label signInLabel;

    @FXML
    private Label NameValidationLabel;

    @FXML
    private Label EmailValidationLabel;

    @FXML
    private Label PassValidationLabel;

    @FXML
    private Label RePassValidationLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        signInLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene oldScene = ((Node) event.getSource()).getScene();
                    Parent root = FXMLLoader.load(getClass().getResource("../fxml/Sign_In.fxml"));
                    Scene scene = new Scene(root, oldScene.getWidth(), oldScene.getHeight());
                    scene.getStylesheets().add(getClass().getResource("../fxml/style_sheet.css").toExternalForm());
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException ex) {
                    Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    @FXML
    protected void register(ActionEvent event) {
        boolean emailValidation = DataValidation.isNotEmpty(userEmailField, EmailValidationLabel, "Enter your email");
        boolean userNameValidation = DataValidation.isNotEmpty(userNameField, NameValidationLabel, "Enter your user name");
        boolean passValidation = DataValidation.isNotEmpty(userPassword, PassValidationLabel, "Enter your password");
        boolean rePassValidation = DataValidation.isNotEmpty(userRePassword, RePassValidationLabel, "Enter your re-password");
        if (emailValidation) {
            emailValidation = DataValidation.emailFormat(userEmailField, EmailValidationLabel, "Format must be name@emailaddress.com");
        }
        if (userNameValidation) {
            userNameValidation = DataValidation.UserName(userNameField, NameValidationLabel, "Don't Enter Speial Characters");
        }
        if (passValidation) {
            passValidation = DataValidation.dataLength(userPassword, PassValidationLabel, "Minimum number of characters is 6", 6);
        }
        if (rePassValidation) {
            rePassValidation = DataValidation.isMatch(userPassword, userRePassword, RePassValidationLabel, "re-password not match password");
        }

        if (emailValidation && userNameValidation && passValidation && rePassValidation) {
            String email = userEmailField.getText().toString();
            String userName = userNameField.getText().toString();
            String password = userPassword.getText().toString();
            String repass = userRePassword.getText().toString();
            User user = new User(userName, email, password);
            signUp(user, event);
            System.out.println("valid");
        } else {
            System.out.println("notValid");
        }

    }

    private void signUp(User user, ActionEvent event) {

        RegesterRequest registration = new RegesterRequest(user);
        int result = registration.registerUser();
        if (result == 1) {
            /*
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene oldScene = ((Node) event.getSource()).getScene();
            Parent root;
            try {
                root = FXMLLoader.load(getClass().getResource("../fxml/main_form.fxml"));
                Scene scene = new Scene(root, oldScene.getWidth(), oldScene.getHeight());
                scene.getStylesheets().add(getClass().getResource("../fxml/style_sheet.css").toExternalForm());
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, null, ex);
            }*/
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene oldScene = ((Node) event.getSource()).getScene();
            Parent root;
            try {
                root = FXMLLoader.load(getClass().getResource("../fxml/sign_in.fxml"));
                Scene scene = new Scene(root, oldScene.getWidth(), oldScene.getHeight());
                scene.getStylesheets().add(getClass().getResource("../fxml/style_sheet.css").toExternalForm());
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            
              Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene oldScene = ((Node) event.getSource()).getScene();
            Parent root;
            try {
                root = FXMLLoader.load(getClass().getResource("../fxml/sign_in.fxml"));
                Scene scene = new Scene(root, oldScene.getWidth(), oldScene.getHeight());
                scene.getStylesheets().add(getClass().getResource("../fxml/style_sheet.css").toExternalForm());
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
