/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import model.Task;

/**
 * FXML Controller class
 *
 * @author tamimy
 */
public class ShowProgressController implements Initializable {

    @FXML
    private BarChart<?, ?> clientChart;


    ObservableList<Task> TasksList;
    int Progress=0;
    int Done=0;
    int Todo=0;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

public void fillData(ObservableList<Task> list)
{
    TasksList = list;
    for (int i = 0; i < TasksList.size(); i++) {
        Task task = TasksList.get(i);
        switch(task.getStatus())
        {
            case "done":
                Done++;
                break;
            case "in progress":
                Progress++;
                break;
            case "to do":
                Todo++;
                break;
        }
    }
    updateChart();
}
 public void  updateChart() {
        XYChart.Series series1 = new XYChart.Series<>();
        series1.getData().add(new XYChart.Data("Todo", Todo));
        
        XYChart.Series series2 = new XYChart.Series<>();
        series2.getData().add(new XYChart.Data("Progress", Progress));

        XYChart.Series series3 = new XYChart.Series<>();
        series3.getData().add(new XYChart.Data("Done", Done));

        clientChart.getData().clear();
        clientChart.getData().addAll(series1, series2, series3);
    }   
}
