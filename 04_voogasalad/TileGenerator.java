package authoringInterface.editor.menuBarView.subMenuBarView;

import authoringInterface.spritechoosingwindow.PopUpWindow;
import gameObjects.crud.GameObjectsCRUDInterface;
import gameObjects.tile.TileClass;
import gameObjects.tileGeneration.GenerationMode;
import grids.Point;
import grids.PointImpl;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Pair;
import utils.imageManipulation.ImageManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a pop-up window for the user to add information about the tile generation.
 * The user could add multiple tiles in either RANDOM or REPEATING mode as specificed in
 * GenerationMode enum type.
 * <p>
 * The user would drag and drop the existing tiles into the position of the ImageView
 * and input the starting position and span for the tiles.
 * <p>
 * If the user want multiple tiles, then he/she can click on the "Add Tile" button. In this way,
 * one more "tile and probability pair (TileProbPair)" would be generated in the pop-up window.
 * <p>
 * I think it is a good design because it utilizes an abstract class named "PopUpWindow", which contains
 * methods to open the window and close the window. It is designed in a way that the drag event from the
 * side view could be detected here, thus this window needs to use the primary stage of the game engine
 * instead of generating a new stage to be used. Also, `addPair()` method serves as a generator, and the
 * data inside the TileProbPair class is encapsulated
 *
 * @author jl729
 */

public class TileSettingDialog extends PopUpWindow {
    public static final double WIDTH = 500.0;
    public static final double HEIGHT = 500.0;
    public static final int TOP = 10;
    public static final int RIGHT = 50;
    public static final int BOTTOM = 50;
    public static final int LEFT = 50;
    public static final int SPACING = 30;
    private GameObjectsCRUDInterface gameObjectManager;
    private Map<Double, TileClass> tileClasses;
    private Point start;
    private int numRow;
    private int numCol;
    private GenerationMode gMode = GenerationMode.RANDOM;
    private ComboBox gModeChoice;
    private List<TileProbPair> pairList;
    private VBox myPane;
    private VBox dialogPane;

    public TileSettingDialog(GameObjectsCRUDInterface gameObjectManager, Stage primaryStage) {
        super(primaryStage);
        this.gameObjectManager = gameObjectManager;
        tileClasses = new HashMap<>();
        pairList = new ArrayList<>();
        myPane = setUpTileBox();
        Button addTile = new Button("Add A Tile");
        addTile.setOnAction(e -> myPane.getChildren().add(addPair()));
        selectMode();
        setUpLayout(addTile);
    }

    private void setUpLayout(Button addTile) {
        HBox xyBox = new HBox();
        HBox xSySBox = new HBox();
        Label xLabel = new Label("X Pos: ");
        Label yLabel = new Label("Y Pos: ");
        Label xSLabel = new Label("X Span: ");
        Label ySLabel = new Label("Y Span: ");
        TextField xText = new TextField();
        TextField yText = new TextField();
        TextField xSText = new TextField();
        TextField ySText = new TextField();

        xyBox.getChildren().addAll(xLabel, xText, yLabel, yText);
        xSySBox.getChildren().addAll(xSLabel, xSText, ySLabel, ySText);
        dialogPane = new VBox(myPane);

        Button apply = new Button("Apply");
        apply.setOnAction(e -> {
            extractInputValue(xText, yText, xSText, ySText);
            closeWindow();
        });

        dialogPane.getChildren().addAll(addTile, gModeChoice, xyBox, xSySBox, apply);
        dialogPane.setPadding(new Insets(TOP, RIGHT, BOTTOM, LEFT));
        dialogPane.setSpacing(SPACING);
    }

    private void extractInputValue(TextField xText, TextField yText, TextField xSText, TextField ySText) {
        start = new PointImpl(Integer.parseInt(xText.getText()), Integer.parseInt(yText.getText()));
        numRow = Integer.parseInt(xSText.getText());
        numCol = Integer.parseInt(ySText.getText());
        pairList.forEach(p -> {
            p.setProb(Double.parseDouble(p.probText.getText()));
            tileClasses.put(p.prob, p.tileClass);
        });
    }

    private void selectMode() {
        gModeChoice = new ComboBox();
        gModeChoice.getItems().addAll("Random", "Repeating");
        gModeChoice.setPromptText("Choose Mode");
        gModeChoice.valueProperty().addListener((ov, t, t1) -> {
            if (t1.equals("Random")) gMode = GenerationMode.RANDOM;
            else gMode = GenerationMode.REPEATING;
        });
    }

    public Point getStart() {
        return start;
    }

    public int getNumRow() {
        return numRow;
    }

    public int getNumCol() {
        return numCol;
    }

    private TileProbPair addPair() {
        TileProbPair newPair = new TileProbPair();
        pairList.add(newPair);
        return newPair;
    }

    /**
     * add one default TileProbPair to the DialogPane
     *
     * @return
     */
    private VBox setUpTileBox() {
        VBox vb = new VBox();
        vb.getChildren().add(addPair());
        return vb;
    }

    /**
     * @return needed information for editorView
     */
    public Pair<Map<Double, TileClass>, GenerationMode> retrieveInfo() {
        return new Pair<>(tileClasses, gMode);
    }

    @Override
    public void showWindow() {
        dialog.setScene(new Scene(dialogPane, WIDTH, HEIGHT));
        dialog.showAndWait();
    }

    @Override
    public void closeWindow() {
        dialog.close();
    }

    /**
     * An inner class to create a pair of tile ImageView and the Textfield for the user to
     * input the probability for generating the tile in the grid
     */
    private class TileProbPair extends HBox {
        public static final double PREF_WIDTH = 100.0;
        public static final double PREF_HEIGHT = 100.0;
        public static final double OPACITY = 0.5;
        private TileClass tileClass;
        private Double prob;
        private ImageView tileView;
        private TextField probText;

        TileProbPair() {
            Pane wrapper = new Pane();
            wrapper.setPrefSize(PREF_WIDTH, PREF_HEIGHT);
            wrapper.setBorder(new Border(new BorderStroke(Color.BLACK,
                    BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
            tileView = new ImageView();
            tileView.setFitHeight(PREF_WIDTH);
            tileView.setFitWidth(PREF_HEIGHT);
            wrapper.getChildren().add(tileView);
            wrapper.setOnDragOver(e -> setUpHoveringColorDraggedOver(e));
            wrapper.setOnDragDropped(e -> handleDragFromSideView(e));

            Label probLabel = new Label("Probability: ");
            probText = new TextField();

            setPrefSize(PREF_WIDTH, PREF_HEIGHT);
            getChildren().addAll(wrapper, probLabel, probText);
        }

        public void setProb(Double prob) {
            this.prob = prob;
        }

        private void handleDragFromSideView(DragEvent dragEvent) {
            dragEvent.acceptTransferModes(TransferMode.ANY);
            try {
                tileClass = gameObjectManager.getGameObjectClass(dragEvent.getDragboard().getString());
                tileView.setImage(ImageManager.getPreview(tileClass));
            } catch (Exception e) {
                e.printStackTrace();
            }
            dragEvent.consume();
        }

        /**
         * This method accepts a Region as input and another Paint variable as input to set up a hovering coloring scheme. The region that is inputted will change to the defined color when hovered over.
         *
         * @param dragEvent: A DragEvent which should be DraggedOver
         */
        private void setUpHoveringColorDraggedOver(DragEvent dragEvent) {
            dragEvent.acceptTransferModes(TransferMode.ANY);
            if (dragEvent.getGestureSource() instanceof TreeCell) {
                tileView.setOpacity(OPACITY);
            }
            dragEvent.consume();
        }

    }

}
