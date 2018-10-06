package simulation.models;

import javafx.scene.paint.Color;
import simulation.Cell;
import simulation.CellGraph;
import xml.writer.GameOfLifeWriter;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  GameOfLifeModel implements SimulationModel interface.
 *
 *  @author Inchan Hwang
 */
public class GameOfLifeModel implements SimulationModel<Integer> {
    public static final int DEAD = 0;
    public static final int ALIVE = 1;
    public static final String MODEL_NAME = "Game Of Life";

    @Override
    public int updatePriority(Integer myVal) { return 0; }

    @Override
    public void localUpdate(Cell<Integer> me, List<Cell<Integer>> neighbors) {
        long nLives = neighbors.stream().filter(c -> c.value() == ALIVE).count();
        me.setNext( me.value() == ALIVE ?
                        (nLives < 2 ? DEAD :
                          nLives < 4 ? ALIVE : DEAD) :
                        (nLives == 3 ? ALIVE : DEAD));
    }

    @Override
    public void globalUpdate(CellGraph<Integer> graph) {
        // Game of life model doesn't require global update
    }

    @Override
    public Integer nextValue(Integer myVal) { return myVal == ALIVE ? DEAD : ALIVE; }

    @Override
    public Color chooseColor(Integer myVal) { return myVal == DEAD ? Color.WHITE : Color.BLACK; }

    @Override
    public String modelName() { return MODEL_NAME; }

    @Override
    public Map<String, Integer> getStatistics(List<Integer> values) {
        HashMap<String, Integer> myMap = new HashMap<>();
        int deadNum = 0;
        int aliveNum = 0;
        for (Integer a : values) {
            if (a == DEAD) deadNum++;
            else aliveNum++;
        }
        myMap.put("Dead", deadNum);
        myMap.put("Alive", aliveNum);
        return myMap;
    }

    @Override
    public GameOfLifeWriter getXMLWriter(CellGraph<Integer> graph, File outFile, String language) {
        return new GameOfLifeWriter(graph, outFile, language);
    }

    @Override
    public void updateModelParams(Map<String, String> params) {
        // game of life doesn't have any parameters
    }

    @Override
    public Integer getValFromCode(int code) {
        return code;
    }

    @Override
    public List<Integer> getCodes() {
        return List.of(DEAD, ALIVE);
    }

    @Override
    public int getDefaultCode() {
        return DEAD;
    }
}
