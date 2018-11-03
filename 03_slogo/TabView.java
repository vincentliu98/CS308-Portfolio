package frontend.GUI;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.Controller;

/**
 * TabView class is created to make each tab separate in its functionality. By assigning each tab with a
 * Controller, each tab's communication between the frontend and the backend will be all contained in the Controller. 
 * <p>
 * TabView simply represents an AnchorPane that contains a TabPane with a Button. When the buttons is clicked,
 * a new Controller will be initialized, and a new set of frontend and backend will be initialized as well.
 * Then, the View (i.e. GridPane) within the frontend will be added to the new tab. 
 * <p>
 * In this way, whenever the setting changes in the frontend (e.g. Language) or when a new tab is added,
 * the existing tabs won't be affected.
 * 
 * I think it is well-designed because it wraps the Controller within and extract the Controller's View to be displayed
 * on the TabPane. In this way, the user interface and the backend mechanisms are separate to each other, while remaining
 * integral within the same tab.
 *
 * @author Vincent Liu
 */
public class TabView {
    public static final String TITLE = "SLogo";
    public static final String STYLESHEET = "style.css";
    public static final int TAB_PANE_HEIGHT = 35;
    public static final double TAB_PANE_TOP_MARGIN = 5.0;
    public static final double TAB_PANE_LEFT_MARGIN = 5.0;
    public static final double TAB_PANE_RIGHT_MARGIN = 5.0;
    public static final double BUTTON_TOP_MARGIN = 5.0;
    public static final double BUTTON_RIGHT_MARGIN = 9.0;
    public static final int FRAMES_PER_SECOND = 60;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final int SCREEN_HEIGHT = 700;
    public static final int SCREEN_WIDTH = 1000;
    private final Stage myStage;
    private final String myLanguage;
    private final VBox root = new VBox();
    private final TabPane tabPane = new TabPane();
    private final Button addButton = new Button("+");
    private AnchorPane anchorPane;
    private Scene myScene;

    public TabView(Stage primaryStage, String language) {
        myStage = primaryStage;
        myLanguage = language;
        initializeAnchorPane();
        addTab(new Controller(myStage, myLanguage));
        addTabToAnchorPane();
        display();
    }
    /**
     * Set up the anchorPane that will contain the AddButton and TabPane
     */
    private void initializeAnchorPane() {
        anchorPane = new AnchorPane();
        anchorPane.setPadding(Insets.EMPTY);
        AnchorPane.setTopAnchor(tabPane, TAB_PANE_TOP_MARGIN);
        AnchorPane.setLeftAnchor(tabPane, TAB_PANE_LEFT_MARGIN);
        AnchorPane.setRightAnchor(tabPane, TAB_PANE_RIGHT_MARGIN);
        AnchorPane.setTopAnchor(addButton, BUTTON_TOP_MARGIN);
        AnchorPane.setRightAnchor(addButton, BUTTON_RIGHT_MARGIN);
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addTab(new Controller(myStage, myLanguage));
            }
        });
    }
    /**
     * Add a new tab inside the View from the Controller to the GUI
     * @param controller
     */
    public void addTab(Controller controller) {
        GridPane newGridPane = controller.getMyView();
        newGridPane.setMaxHeight(SCREEN_HEIGHT - TAB_PANE_HEIGHT);
        addToTabPane(newGridPane);
    }
    /**
     * A helper method to add the new tab to the tabPane
     * @param gridPane
     */
    private void addToTabPane(GridPane gridPane) {
        Tab tab = new Tab();
        tab.setText("Tab " + (tabPane.getTabs().size() + 1));
        tab.setContent(gridPane);
        tabPane.getTabs().add(tab);
    }
    /**
     * Add the TabPane to the AnchorPane
     */
    private void addTabToAnchorPane() {
        anchorPane.getChildren().addAll(tabPane, addButton);
        root.getChildren().add(anchorPane);
    }
    /**
     * Start showing the application
     */
    private void display() {
        myScene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        myScene.getStylesheets().add(STYLESHEET);
        startView();
    }
    /**
     * helper method to play the animation
     */
    public void startView() {
        myStage.setScene(myScene);
        myStage.setTitle(TITLE);
        myStage.show();
        var frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY));
        var animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }
}
