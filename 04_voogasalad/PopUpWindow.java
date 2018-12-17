package authoringInterface.spritechoosingwindow;

import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * An abstract class for all pop-up windows.
 *
 * @author jl729
 */

public abstract class PopUpWindow {
    protected final Stage primaryStage;
    protected final Stage dialog;

    protected PopUpWindow(Stage primaryStage) {
        this.primaryStage = primaryStage;
        dialog = new Stage();
        dialog.initModality(Modality.NONE);
        dialog.initOwner(primaryStage);
    }

    abstract public void showWindow();

    abstract public void closeWindow();
}
