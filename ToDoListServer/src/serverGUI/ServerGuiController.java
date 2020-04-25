/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverGUI;

import DataBase.DataBaseHandler;
import connection.Client;
import java.io.IOException;
import java.net.URL;
import java.util.List;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class ServerGuiController implements Initializable {

    @FXML
    private Button startButton;

    @FXML
    private Button stopButton;
    @FXML
    private BarChart<?, ?> userChart;

    @FXML
    private CategoryAxis userAxis;

    @FXML
    private NumberAxis numAxis;

    @FXML
    private Button refreshButton;

    boolean flag = false;
    private Stage stage;
    private DataBaseHandler repository;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        stopButton.setDisable(true);
        startButton.setDisable(false);
        refreshButton.setDisable(true);
        
        
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Start Button");
                PortCheck.startServer();
                stopButton.setDisable(false);
                refreshButton.setDisable(false);
                startButton.setDisable(true);
                repository =  DataBaseHandler.createDB();
                updateChart();
            }
        });

        stopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Stop Button");
                PortCheck.closeServer();
                stopButton.setDisable(true);
                refreshButton.setDisable(true);
                startButton.setDisable(false);
                userChart.getData().clear();
//repository.closeConnection();
            }
        });
        
        refreshButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                updateChart();
                
            }
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public void  updateChart() {
        XYChart.Series series1 = new XYChart.Series<>();
        series1.getData().add(new XYChart.Data("Online", Client.getclientVector().size()));

        XYChart.Series series2 = new XYChart.Series<>();
        series2.getData().add(new XYChart.Data("Offline", repository.getNumberOfUsers()-Client.getclientVector().size()));

        XYChart.Series series3 = new XYChart.Series<>();
        series3.getData().add(new XYChart.Data("Projects", repository.getNumberOfProjects()));

        userChart.getData().clear();
        userChart.getData().addAll(series1, series2, series3);
    }
}
