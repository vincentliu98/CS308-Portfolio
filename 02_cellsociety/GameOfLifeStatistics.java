package visualization.statistics;

import javafx.scene.chart.XYChart;

import java.util.Map;
import java.util.ResourceBundle;

/**
 * A Line Chart to display and update statistics for GameOfLifeModel
 * 
 * I think this class is well-designed because 1. it separates the XYSeries and the chart framework. This makes it flexible to add
 * XYSeries for different models. 2. The updateStatistics() method uses a Map as a parameter. It is general and it is very useful
 * in terms of passing data from the model to the chart
 *
 * @author Vincent Liu
 */

public class GameOfLifeStatistics extends ModelStatistics{
    public XYChart.Series Alive, Dead;

    /**
     * Initialized the two Series
     * <ul>
     *     <li>Alive</li>
     *     <li>Dead</li>
     * </ul>
     */
    public GameOfLifeStatistics() {
        super();
        Alive = new XYChart.Series();
        Alive.setName("Alive");
        Dead = new XYChart.Series();
        Dead.setName("Dead");

        getData().addAll(Alive, Dead);
    }

    @Override
    public void updateStatistics(Double durationCounter, ResourceBundle myResources, Map<String, Integer> newStatistics) {
        Alive.getData().add(new XYChart.Data<>(durationCounter, newStatistics.get("Alive")));
        Dead.getData().add(new XYChart.Data<>(durationCounter, newStatistics.get("Dead")));
    }
}
