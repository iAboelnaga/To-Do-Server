/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Entity.User;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;
import Helper.Validate;
import server_request.RequestHandler;
import Helper.RequestParam;
import Enums.RESPOND_CODE;
import Enums.MESSAGES;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.stage.WindowEvent;
import model.DataValidation;
import org.apache.derby.iapi.store.raw.log.LogInstant;

/**
 * FXML Controller class
 *
 * @author tamimy
 */
public class Sign_In_Controller implements Initializable {

    @FXML
    private TextField userNameField;

    @FXML
    private PasswordField userPassword;

    @FXML
    private Button login;

    @FXML
    private Label signUpLabel;

    @FXML
    private Label NameValidationLabel;

    @FXML
    private Label PassValidationLabel;
     private Stage stage;

    private RequestHandler server;
    private String[] params = {RequestParam.LOGIN};
    public static int UserId;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        signUpLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene oldScene = ((Node) event.getSource()).getScene();
                    Parent root = FXMLLoader.load(getClass().getResource("../fxml/sign_up.fxml"));
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
    protected void login(ActionEvent event) throws JSONException {
        boolean userNameValidation = DataValidation.isNotEmpty(userNameField, NameValidationLabel, "Enter your user name");
        boolean passValidation = DataValidation.isNotEmpty(userPassword, PassValidationLabel, "Enter your password");

        if (!userNameValidation) {
            userNameValidation = DataValidation.UserName(userNameField, NameValidationLabel, "Don't Enter Special Characters");
        }
        if (!passValidation) {
            passValidation = DataValidation.dataLength(userPassword, PassValidationLabel, "Minimum number of characters is 6", 6);
        }

        if (userNameValidation && passValidation) {
            String userName = userNameField.getText().toString();
            String password = userPassword.getText().toString();

            System.out.println("valid");
            signIn(event);
        } else {
            System.out.println("notValid");
        }

        System.out.println("SignIn");
    }

    private void signIn(ActionEvent event) throws JSONException {
        login.setDisable(true);
        //userPassword.setText("");
        //get userName and password from input field
        String email = userNameField.getText().trim();
        String password = userPassword.getText().trim();

        //check and validate input, if empty password or user name
        //boolean isInputNotEmpty = Validate.checkPasswordAndEmail(email, password);
        //if valid input, then make server request
        User user = new User(email, password);
        JSONObject userJson = user.getUserAsJson();

        //TODO:server request in the background, to not freez UI thread
        /* Issue , when making more than one signIn, UI freez 
                even if user input (userName , password) are correct
         */
        try {
            server = new RequestHandler();
            System.out.println("user json after convert " + userJson.toString());
            JSONObject response = server.post(params, userJson);
            int code = 0;
            //get respond code (SUCCESS , FAILD)after server request
            if (response != null) {
                code = response.getInt("Code");
            }
            switch (code) {
                case RESPOND_CODE.SUCCESS:
                    UserId = response.getInt("id");
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    Scene oldScene = ((Node) event.getSource()).getScene();
                    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event1) {
                            try {
                                server.logOut();
                                System.exit(0);
                                //System.out.println("here");
                            } catch (Exception ex) {
                                Logger.getLogger(SignupController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                    Parent root = FXMLLoader.load(getClass().getResource("../fxml/main_form.fxml"));
                    Scene scene = new Scene(root, oldScene.getWidth(), oldScene.getHeight());
                    scene.getStylesheets().add(getClass().getResource("../fxml/style_sheet.css").toExternalForm());
                    stage.setScene(scene);
                    stage.show();
                    break;
                case RESPOND_CODE.FAILD:
                    showFaildAccessMessage();
                    break;

                case RESPOND_CODE.IS_LOGIN:
                    showIsLoginMessage();
                    break;
                default:
                    login.setDisable(false);
            }
        } catch (IOException ex) {
            //AlertDialog.showInfoDialog("Connection Down", "Connection Issue", "Please try again");
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setContentText(ex.toString());
//            alert.showAndWait();
//            login.setDisable(false);
            //System.out.println(ex);
            ex.printStackTrace();
            System.out.println(ex.toString());

        }

        //TODO: send data to server for authontication
    }

    private void showIsLoginMessage() {
        login.setDisable(false);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(MESSAGES.IS_ALREADY_LOGIN);
        alert.showAndWait();
        login.setDisable(false);

    }

    private void showFaildAccessMessage() {
        login.setDisable(false);
        // show wrong access message when wrong password or username got entered from user
        //userPassword.setText(MESSAGES.WRONG_ACCESS);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(MESSAGES.WRONG_ACCESS);
        alert.showAndWait();
        login.setDisable(false);
    }

}
