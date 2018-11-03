package main;
import backend.IllegalCommandException;
import backend.ModelController;
import backend.Turtle;
import backend.TurtleGroup;
import frontend.ExternalAPI.ViewAPI;
import frontend.GUI.View;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.*;
import java.util.regex.Pattern;

/**
 * This project is implemented using MVC model, and this Controller mediates the communications
 * between the Model and the View.
 * <p>
 * Controller is initialized when a tab is added inside TabView. Each controller contains an instance of
 * a generic Turtle turtle (initialized as the TurtleGroup type), ViewAPI (external API of our View),
 * and ModelController (our model that represents backend).
 * 
 * I think it is well-designed because it is flexible. For example, before whenever the front end changes language, 
 * a new Controller will be initialized with the new language, which means the front end will need to refresh, thus causing
 * a flash on the screen. But now when the languages changes, I only need to call setUpFrontEnd() with the same stage and
 * the new language, thus it does not impact front end.
 *
 * @author Vincent Liu
 */
public class Controller {
    public static final String COMMAND_ERROR = "Errors";
    public static final String LANG_PATH = "languages/";
    public static final String SYNTAX = "languages/Syntax";
    private ViewAPI myView;
    private Turtle myTurtle;
    private ModelController modelController;
    private List<Map.Entry<String, Pattern>> mySymbols;
    private ResourceBundle myErrors;

    /**
     * Create an instance of the Controller
     * @param primaryStage
     * @param language
     */
    public Controller(Stage primaryStage, String language) {
        myTurtle = new TurtleGroup();
        myErrors = ResourceBundle.getBundle(COMMAND_ERROR);
        setUpFrontEnd(primaryStage, language);
        setUpBackEnd(language);
    }

    /**
     * Set up the frontend by initiating a View class and register turtle as the observables
     * @param primaryStage
     * @param language
     */
    private void setUpFrontEnd(Stage primaryStage, String language) {
        myView = new View(primaryStage, this, language);
        myView.registerDisplay(myTurtle);
    }

    /**
     * Set up the backend elements such as the syntax for parsing and the modelController
     * @param language
     */
    public void setUpBackEnd(String language) {
        mySymbols = new ArrayList<>();
        addPatterns(LANG_PATH + language); // language syntax
        addPatterns(SYNTAX);
        modelController = new ModelController(myTurtle, mySymbols);
    }

    /**
     * Called by the View when a command is received.
     * Run the command and throw errors when appropriate
     * @param input
     */
    public void runCommand(String input) {
        if (reportEmptyString(input)) return;
        try {
            List<String> ret = modelController.parseCommand(input);
            myView.returnValues(ret);
            myView.updateHistory(input);
            checkBackEndVarUpdate();
            checkBackEndFuncUpdate();
        } catch (Exception e) {
            e.printStackTrace();
//            throwErrorByType(e);
        }
    }

    /**
     * Check empty string from the command
     * @param input
     * @return
     */
    private boolean reportEmptyString(String input) {
        if (input.isEmpty()) {
            myView.displayErrors("Please enter a command!");
            return true;
        }
        return false;
    }

    /**
     * Throw errors according to the exception type from the backend
     * @param e
     */
    private void throwErrorByType(Exception e) {
        if (e instanceof IllegalCommandException) {
            myView.displayErrors(myErrors.getString("commandError"));
        } else myView.displayErrors(e.toString());
    }

    // Check function in the backend and display new one in the functionView
    private void checkBackEndFuncUpdate() {
        List<String> newFunc = modelController.updateFunc();
        if (!newFunc.isEmpty()) myView.displayFunc(newFunc);

    }
    // Check variable in the backend and display new one in the VariableView
    private void checkBackEndVarUpdate() {
        Map<String, String> newVar = modelController.updateVar();
        if (!newVar.isEmpty()) myView.displayVar(newVar);
    }
    // Adds the given resource file to this language's recognized types
    private void addPatterns(String syntax) {
        var resources = ResourceBundle.getBundle(syntax);
        for (var key : Collections.list(resources.getKeys())) {
            var regex = resources.getString(key);
            mySymbols.add(new AbstractMap.SimpleEntry<>(key,
                    Pattern.compile(regex, Pattern.CASE_INSENSITIVE)));
        }
    }
    /**
     * Run the command to update variable name and value entered by the user in the VariableView
     *
     * @param var
     */
    public void updateVar(Map<String, String> var) {
        String key = var.keySet().toArray()[0].toString();
        try {
            modelController.parseCommand("make " + ":" + key + " " + var.get(key));
        } catch (Exception e) {
            throwErrorByType(e);
        }
    }
    /**
     * getter for the GridPane inside View
     * @return
     */
    public GridPane getMyView() {
        return myView.getMyGridPane();
    }
    /**
     * getter for the turtle
      * @return
     */
    public Turtle getMyTurtle() {
        return myTurtle;
    }
}
