/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import Entity.TeamMate;
import Entity.User;
import Entity.myList;
import Enums.REQUEST;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.jfoenix.controls.JFXListView;
import com.sun.java.swing.plaf.windows.resources.windows;
import fxml.WorkinprojectController;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.NotificationModel;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import server_request.RequestHandler;

/**
 * FXML Controller class
 *
 * @author mahmoud mohamed
 */
public class MainFormController implements Initializable {

    @FXML
    private BorderPane container;
    @FXML
    private HBox topHbox;
    @FXML
    private TreeView<String> projectTreeView;
    @FXML
    private JFXListView<Object> notifuList;
    @FXML
    private JFXListView<TeamMate> teamMateList;
    @FXML
    private ImageView notifyImagView;
    @FXML
    private ImageView teamMatesImagView;
    @FXML
    private ImageView addMateImageView;
    @FXML
    private Button creatProject;
    @FXML
    private Label userName;
    static String Name;

    private ObservableList<Object> notificationObservableList;
    public static ObservableList<TeamMate> TeamMateObservableList;
    ArrayList<myList> todoList, sharedList;
    final ContextMenu contextMenu = new ContextMenu();
    boolean Notificationflag = false;
    boolean teamMateflag = false;

    public MainFormController() {

        notificationObservableList = FXCollections.observableArrayList();
        TeamMateObservableList = FXCollections.observableArrayList();

        //add some Notifications
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MenuItem delete = new MenuItem("delete");
        MenuItem update = new MenuItem("update");
        contextMenu.getItems().addAll(delete, update);
        try {
            RequestHandler server = new RequestHandler();
            JSONObject json = server.get(new String[]{"todo", Sign_In_Controller.UserId + ""});
            System.out.println("get");
            User user = new User(json.getInt("id"), json.getString("username"), json.getString("password"));
            Gson gson = new GsonBuilder().create();
            System.out.println(json);
            // convert jsonArray to todoList
            Type ListType = new TypeToken<ArrayList<myList>>() {
            }.getType();
            todoList = gson.fromJson(json.getJSONArray("todo_list").toString(), ListType);
            userName.setText(user.getUserName());
            Name = user.getUserName();
            // convert shared todo
            sharedList = gson.fromJson(json.getJSONArray("shared_list").toString(), ListType);
            if (todoList.size() != 0) {

                // convert shared todo
                //ArrayList<myList> sharedList = gson.fromJson(json.getJSONArray("shared_list").toString(), ListType);
                System.out.println(todoList.size());
                System.out.println(todoList.get(0).getTitle());
            }

            // start home screen
            //start(user, todoList, sharedList,);
        } catch (IOException ex) {
            Logger.getLogger(MainFormController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JSONException ex) {
            System.out.println(ex);
        }

        //userName.setLayoutX((double)container.getScene().getWindow().getWidth() /2);
        notifuList.setVisible(false);
        notifuList.setItems(notificationObservableList);
        notifuList.setCellFactory(param -> new NotificationCell(notifuList));
        teamMateList.setVisible(false);
        teamMateList.setItems(TeamMateObservableList);
        teamMateList.setCellFactory(param -> new TeamMateCell());

        TreeItem<String> root = new TreeItem<String>("Navigator");
        TreeItem<String> rootItem = new TreeItem<String>("owner Projects", getRootIcon());
        TreeItem<String> rootItem2 = new TreeItem<String>("Projects", getRootIcon());

        if (todoList.size() != 0) {
            root.setExpanded(true);
            rootItem.setExpanded(true);
            for (int i = 0; i < todoList.size(); i++) {
                TreeItem dep = new TreeItem(todoList.get(i).getTitle(), getProjectIcon());
                rootItem.getChildren().add(dep);
            }
        } else {
            root.setExpanded(true);
        }
        if (sharedList.size() != 0) {
            root.setExpanded(true);
            rootItem2.setExpanded(true);
            for (int i = 0; i < sharedList.size(); i++) {
                TreeItem dep = new TreeItem(sharedList.get(i).getTitle(), getProjectIcon());
                rootItem2.getChildren().add(dep);
            }
        } else {

            root.setExpanded(true);
            rootItem2.setExpanded(true);

        }
        root.getChildren().addAll(rootItem, rootItem2);
        projectTreeView.setRoot(root);

        projectTreeView.addEventHandler(MouseEvent.MOUSE_CLICKED, event
                -> {
            TreeItem<String> selectedItem = projectTreeView.getSelectionModel().getSelectedItem();
            if (event.getButton() == MouseButton.PRIMARY) {
                contextMenu.hide();
                //item is selected - this prevents fail when clicking on empty space 
                String parentroot = "";
                if (selectedItem != null) {
                    if (selectedItem.getParent() != null) {
                        parentroot = selectedItem.getParent().toString();
                        if (parentroot.equals("TreeItem [ value: owner Projects ]")) {
                            System.out.println("item name " + selectedItem.getValue());
                            for (int i = 0; i < todoList.size(); i++) {
                                if (todoList.get(i).getTitle().equals(selectedItem.getValue())) {
                                    try {
                                        Stage newStage = new Stage();
                                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/OwnedProject.fxml"));
                                        Parent rootFxml = loader.load();
                                        OwnedProjectController tasks = loader.getController();
                                        tasks.transferListDetails(todoList.get(i), "main");
                                        Scene scene = new Scene(rootFxml, 800, 600);
                                        scene.getStylesheets().add(getClass().getResource("../fxml/style_sheet.css").toExternalForm());
                                        newStage.setScene(scene);
                                        newStage.show();
                                    } catch (IOException ex) {
                                        Logger.getLogger(ProjectDetailsController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }

                        } else if (parentroot.equals("TreeItem [ value: Projects ]")) {
                            for (int i = 0; i < todoList.size(); i++) {
                                if (sharedList.get(i).getTitle().equals(selectedItem.getValue())) {
                                    try {
                                        Stage newStage = new Stage();
                                        FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/workinproject.fxml"));
                                        Parent rootFxml = loader.load();
                                        WorkinprojectController tasks = loader.getController();
                                        tasks.transferListDetails(sharedList.get(i), "main");
                                        Scene scene = new Scene(rootFxml, 800, 600);
                                        scene.getStylesheets().add(getClass().getResource("../fxml/style_sheet.css").toExternalForm());
                                        newStage.setScene(scene);
                                        newStage.show();
                                    } catch (IOException ex) {
                                        Logger.getLogger(ProjectDetailsController.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }
                        }
                        // do what ever you want 
                    }
                }

            } else if (event.getButton() == MouseButton.SECONDARY) {
                //open context menu on current screen position 
                contextMenu.show(container, event.getScreenX(), event.getScreenY());

            } else {
                contextMenu.hide();
            }
        });
        /*
        projectTreeView.setCellFactory(tree -> {
            TreeCell<String> cell = new TreeCell<String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item);
                        if (item.equals("Projects") || item.equals("owner Projects")) {
                            setGraphic(getRootIcon());
                        } else if (item.equals("Navigator")) {
                            setGraphic(null);
                        } else {
                            setGraphic(getProjectIcon());
                        }
                    }
                }
            };
            cell.setOnMouseClicked(event -> {
                if (!cell.isEmpty()) {
                    TreeItem<String> treeItem = cell.getTreeItem();
                    if (event.getButton() == MouseButton.PRIMARY) {
                        contextMenu.hide();
                        if (treeItem.getParent() != null) {
                            if (treeItem.getParent().toString().equals("TreeItem [ value: owner Projects ]")) {
                                System.out.println("item name " + treeItem.getValue());
                                for (int i = 0; i < todoList.size(); i++) {
                                    if (todoList.get(i).getTitle().equals(treeItem.getValue())) {
                                        try {
                                            Stage newStage = new Stage();
                                            FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/OwnedProject.fxml"));
                                            Parent rootFxml = loader.load();
                                            OwnedProjectController tasks = loader.getController();
                                            tasks.transferListDetails(todoList.get(i), "main");
                                            Scene scene = new Scene(rootFxml, 800, 600);
                                            scene.getStylesheets().add(getClass().getResource("../fxml/style_sheet.css").toExternalForm());
                                            newStage.setScene(scene);
                                            newStage.show();
                                        } catch (IOException ex) {
                                            Logger.getLogger(ProjectDetailsController.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                }

                            } else if (treeItem.getParent().toString().equals("TreeItem [ value: Projects ]")) {
                                System.out.println("item name " + treeItem.getValue());
                            }
                        }

                    } else if (event.getButton() == MouseButton.SECONDARY) {

                        String parentroot = "";
                        if (treeItem != null) {
                            if (treeItem.getParent() != null) {
                                parentroot = treeItem.getParent().toString();
                                if (parentroot.equals("TreeItem [ value: owner Projects ]")) {
                                    System.out.println("item name " + treeItem.getValue());
                                    contextMenu.hide();
                                    contextMenu.show(container, event.getScreenX(), event.getScreenY());
                                    delete.setOnAction(new EventHandler() {
                                        @Override
                                        public void handle(Event event) {
                                            try {
                                                myList list = new myList();
                                                for (int i = 0; i < todoList.size(); i++) {
                                                    if (todoList.get(i).getTitle().equals(treeItem.getValue())) {
                                                        list = todoList.get(i);
                                                        break;
                                                    }
                                                }
                                                RequestHandler server = new RequestHandler();
                                                JSONObject json = server.delete(new String[]{"RemoveList", list.getId() + ""});
                                                int response = Integer.parseInt(json.get("result").toString());
                                                if (response == 1) {
                                                    treeItem.getParent().getChildren().remove(treeItem);
                                                }
                                            } catch (IOException | JSONException ex) {
                                                Logger.getLogger(MainFormController.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }
                                    });
                                    update.setOnAction(new EventHandler() {
                                        @Override
                                        public void handle(Event event) {
                                            try {
                                                myList list = new myList();
                                                for (int i = 0; i < todoList.size(); i++) {
                                                    if (todoList.get(i).getTitle().equals(treeItem.getValue())) {
                                                        list = todoList.get(i);
                                                        break;
                                                    }
                                                }
                                                System.out.println("update " + list.getTitle());
                                                FXMLLoader loader = new FXMLLoader(getClass().getResource("../fxml/ProjectDetails.fxml"));
                                                Parent root = loader.load();
                                                ProjectDetailsController controller = loader.getController();
                                                controller.formSettings(1,list);
                                                Stage stage =new Stage();
                                                Scene scene = new Scene(root, 500, 250);
                                                scene.getStylesheets().add(getClass().getResource("../fxml/style_sheet.css").toExternalForm());
                                                stage.setScene(scene);
                                                stage.setResizable(false);
                                                stage.show();
                                            } catch (IOException ex) {
                                                Logger.getLogger(MainFormController.class.getName()).log(Level.SEVERE, null, ex);
                                            }
                                        }
                                    });

                                } else if (parentroot.equals("TreeItem [ value: Projects ]")) {
                                    contextMenu.hide();
                                  

                                }
                                // do what ever you want 
                            }
                        }

                    } else {
                        contextMenu.hide();
                    }
                }
            });
            return cell;
        });*/

        notifyImagView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                System.out.println(".handle()");

                if (!Notificationflag) {
                    Notificationflag = true;
                    teamMateflag = false;
                    notifuList.setVisible(true);
                    teamMateList.setVisible(false);
                    try {

                        RequestHandler req = new RequestHandler();
                        String parameters[] = new String[2];
                        parameters[0] = REQUEST.NOTIFICATION;
                        parameters[1] = String.valueOf(Sign_In_Controller.UserId);
                        JSONObject resultJsonObject = req.get(parameters);
                        JSONArray resultJsonArray = resultJsonObject.getJSONArray("result");
                        System.out.println("RESPONSE: " + resultJsonObject);
                        notificationObservableList.clear();
                        for (int i = 0; i < resultJsonArray.length(); i++) {
                            JSONObject notifyObjson = (JSONObject) resultJsonArray.get(i);
                            if (notifyObjson.getString("notificationType").equals(NotificationModel.TeamMateRequest)) {
                                notificationObservableList.add(new NotificationModel(notifyObjson.getString("userName"), notifyObjson.getInt("senderId"), "date"));
                            } else if (notifyObjson.getString("notificationType").equals(NotificationModel.AssignTask)) {
                                notificationObservableList.add(new NotificationModel(notifyObjson.getString("projectTitle"), notifyObjson.getString("userName"), notifyObjson.getInt("taskId"), notifyObjson.getString("taskDesc"), "date"));
                            }

                        }
                        System.out.println("size : _______" + notificationObservableList.size());
                        //notifyImagView.setImage(new Image(getClass().getResourceAsStream("../fxml/notification_click.png")));
                    } catch (IOException ex) {
                        Logger.getLogger(MainFormController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (JSONException ex) {
                        Logger.getLogger(MainFormController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else {
                    notifuList.setVisible(false);
                    Notificationflag = false;
                    //notifyImagView.setImage(new Image(getClass().getResourceAsStream("../fxml/notification.png")));
                }
            }
        });
        teamMatesImagView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                System.out.println(".handle()");
                if (!teamMateflag) {
                    notifuList.setVisible(false);
                    teamMateList.setVisible(true);
                    teamMateflag = true;
                    Notificationflag = false;
                    TeamMateObservableList.clear();

                    try {
                        String parameters[] = new String[2];
                        parameters[0] = "GetTeamMates";
                        int id = Sign_In_Controller.UserId;
                        parameters[1] = Integer.toString(id);
                        RequestHandler req = new RequestHandler();
                        JSONObject resultJsonObject = req.get(parameters);
                        try {
                            JSONArray res = resultJsonObject.getJSONArray("result");
                            for (int i = 0; i < res.length(); i++) {
                                System.out.println(i);
                                TeamMate friend = new TeamMate((res.getJSONObject(i)).getString("teamMateName"), (res.getJSONObject(i)).getInt("teamMateId"), (res.getJSONObject(i)).getString("teamMateStatus"), (res.getJSONObject(i)).getBoolean("online"));
                                TeamMateObservableList.add(friend);

                            }

                            System.out.println(res);
                        } catch (JSONException ex) {
                            Logger.getLogger(MainFormController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    } catch (IOException ex) {
                        Logger.getLogger(MainFormController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    //notifyImagView.setImage(new Image(getClass().getResourceAsStream("../fxml/notification_click.png")));
                } else {
                    teamMateList.setVisible(false);
                    teamMateflag = false;
                    //notifyImagView.setImage(new Image(getClass().getResourceAsStream("../fxml/notification.png")));
                }

            }
        });
        addMateImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                Stage stage = new Stage();
                Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.initModality(Modality.WINDOW_MODAL);
                stage.setTitle("Add Team Mate");
                stage.initOwner(oldStage);
                Parent root;
                try {
                    root = FXMLLoader.load(getClass().getResource("../fxml/addTeamMate.fxml"));
                    Scene scene = new Scene(root, 500, 250);
                    scene.getStylesheets().add(getClass().getResource("../fxml/style_sheet.css").toExternalForm());
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.show();
                } catch (IOException ex) {
                    Logger.getLogger(MainFormController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        creatProject.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent event) {
                Stage stage = new Stage();
                Stage oldStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.initModality(Modality.WINDOW_MODAL);
                stage.setTitle("ProjectDetails");
                stage.initOwner(oldStage);
                Parent root;
                try {
                    root = FXMLLoader.load(getClass().getResource("../fxml/ProjectDetails.fxml"));
                    Scene scene = new Scene(root, 500, 250);
                    scene.getStylesheets().add(getClass().getResource("../fxml/style_sheet.css").toExternalForm());
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.show();
                } catch (IOException ex) {
                    Logger.getLogger(MainFormController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    public ImageView getRootIcon() {
        return new ImageView(new Image(getClass().getResourceAsStream("../fxml/folder.png")));
    }

    public ImageView getProjectIcon() {
        return new ImageView(new Image(getClass().getResourceAsStream("../fxml/project.png")));
    }

    public void getMessage(int user) {
        System.out.println("user id " + user);
        System.out.println(Sign_In_Controller.UserId);
    }

}
