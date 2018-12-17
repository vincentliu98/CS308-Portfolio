# CompSci 308: VOOGASalad Analysis
===================

> This is the link to the assignment: [VOOGASalad](http://www.cs.duke.edu/courses/compsci308/current/assign/04_voogasalad/)

Design Review

=======

## Overall Design

### Describe the overall design of the complete program(s):

- What is the high level design of each sub-part and how do they work together (i.e., what behavior, data, or resources does each part depends on from the others)?
  - frontend
    - Menu bar
      - It contains user actions, such as saving, loading, exporting. It also has some other functions such as resizing the grid, setting the background music, and resizing for each cell. It also contains a help link that guide the user to learn to use the program.
    - Editor
      - Grid
        - It is a gridpane consisting of many cells. Each cell and the width and height of the grid can be resized.
      - Tile is the background of the game, e.g. grassland, dessert
      - Entity is the object on the top of tiles, such as a character or a chess.
    - Side bar
      - Entity Treeview
        - Add entities
        - Edit entities
        - Delete entities
        - Drag entities to the Editor
      - Tile TreeView
        - Add tiles
        - Edit tiles
        - Delete tiles
        - Drag tiles to the Editor
      - Player TreeView
        - Add players
        - Edit players
        - Delete players
    - Phase graph
      - Create one or multiple phases for the game
      - Drag button to add a node
      - Connect them by dragging
      - Go to groovy code
      - Groovy graph
        - Users drag and connect nodes together to create groovy code that defines game rules
  - engine (backend)
    - Stores all the files that games need to be run
    - Implements the components of the phase graph and the groovy graph
    - Implements the connections between the nodes in the phase graph and the groovy graph (eg. Mouse click and key press)
    - Implement turn to switch between players
  - controller (front end Game Laucher)
    - A game store that displays all the games on the GUI and allows the user to click on games to start playing
  - authoring backend (authoring environment backend)
    - stores all the information from the front end (i.e. global game data as a map, which will be retrieved from groovy code through a map)
    - The phaseDB saves all the phase information, and the CRUD object manager saves all the entities and instances that are created.
- How is the design tied (or not) to the chosen game genre. What assumptions need to be changed (if anything) to make it handle different genres?
  - This turn-based strategy game engine design is based on Finite State Machine (FSM). For TBS games, taking away any rigid strictions on game creation, what is left is only user actions and the result. For other genres, user actions and the action results will still stay. Our game engine is flexible enough to be directly adapted by other genres of games.
  - The only thing that needs to be changed is that we need to further consider the animation. Displaying multiple images using our current ImageSelector makes animation pretty hard, and the time interval of the animation frame is fixed.
- What is needed to represent a specific game (such as new code or resources or asset files)?
  - All the pictures and backgroud music should be stored in the engine modules. Its corresponding XML files needs also be saved in the same directory.
  - If there are complicated games or rules, then the method can be written in Java code inside the `GameMethods.java` file to make the phase graphs and groovy graphs look cleaner. Then the corresponding tile could be dragged and connected in the groovy graph.

### Describe two packages that you did not implement:

- Which is the most readable (i.e., whose classes and methods do what you expect and whose logic is clear and easy to follow) and give specific examples?
  - engine module
    - Natalie did a good job implementing this module.
    - Clear methods and APIs are set up to indicate how classes interact with each other
    - example 1: clear interface design for the `Edge.java` class, which checks the validity of the user action after receiving the trigger
      ```java
      public interface ArgumentListener {
      String DONT_PASS = "%@#%@#%@#%@#%@!@#%!@#%!@%";

      /**
      * Whenever an event occurs, the listeners(Edges)
      * check the validity of the transition;
      * If it IS valid, the method returns the id of the node that we're transitioning into
      * If it's not, it returns ""
      */
        String trigger(Event event);
      }
      ```
    - example 2: Inside `GameData.java` there are clear methods that notify the argument listener that some actions are received.
      ```java
      public static void addArgument(MouseEvent event, ClickTag tag) {
          var target = (tag.getType().equals(Tile.class) ? TILES : ENTITIES).get(tag.getID());
          shell.setVariable("$clicked", target);
          notifyArgumentListeners(event);
      }

      public static void addArgument(KeyEvent event, KeyTag tag) { // todo: connect this with the window
          shell.setVariable("$pressed", tag.code());
          notifyArgumentListeners(event);
      }
      ```
  - authoring_backend
    - Jingchao and Inchan did a great job implementing this module. It is pretty readable.
    - There are very specific exception handlers. If you look at the exception package under the authoringUtils package, there are 15 exceptions alone for the CRUD interface.
    - gameObjects package is very clear, although it has a large amount of code. It takes advantage of Java 8's lamda expression very well to perform adding/deleting frontend instances.
    ```java
        private TileClass createTileClass(TileClass tile) {
        tile.equipContext(
                myTileInstanceFactory,
                this::changeAllGameObjectInstancesClassName,
                this::getAllInstances,
                this::deleteGameObjectInstance
        );
        addGameObjectClassToMaps(tile);
        return tile;
    }
    ```
- Which is the most encapsulated (i.e., with minimal and clear dependencies that are easy to find rather than through "back channels") and give specific examples?
  - Instances of entity, tile, and player are most encapsulated by using Consumer or BiConsumer. In this case, the attributes of these classes will not be known by other classes.
  ```java
    private Function<Integer, Boolean> verifyEntityInstanceIdFunc;
    private Consumer<GameObjectInstance> requestInstanceIdFunc;
    private ThrowingConsumer<GameObjectInstance, InvalidIdException> addInstanceToMapFunc;

    public EntityInstanceFactory(
            Function<Integer, Boolean> verifyEntityInstanceIdFunc,
            Consumer<GameObjectInstance> requestInstanceIdFunc,
            ThrowingConsumer<GameObjectInstance, InvalidIdException> addInstanceToMapFunc
    ) {

        this.verifyEntityInstanceIdFunc = verifyEntityInstanceIdFunc;
        this.requestInstanceIdFunc = requestInstanceIdFunc;
        this.addInstanceToMapFunc = addInstanceToMapFunc;
    }

    public EntityInstance createInstance(EntityClass entityPrototype, Point point)
            throws GameObjectTypeException, InvalidIdException {
        // TODO locality
        //        if (!verifyEntityInstanceIdFunc.apply(tileId)) {
        //            throw new InvalidGameObjectInstanceException("Entity cannot be created on Tile Instance with invalid Tile Id");
        //        }
        if (entityPrototype.getType() != GameObjectType.ENTITY) {
            throw new GameObjectTypeException(GameObjectType.ENTITY);
        }
        var imagePathListCopy = new ArrayList<>(entityPrototype.getImagePathList());
        var propertiesMapCopy = new HashMap<>(entityPrototype.getPropertiesMap());
        EntityInstance entityInstance = new SimpleEntityInstance(entityPrototype.getClassName(), imagePathListCopy, propertiesMapCopy, entityPrototype);
        entityInstance.setCoord(point);
        entityInstance.setHeight(entityPrototype.getHeight());
        entityInstance.setWidth(entityPrototype.getWidth());
        requestInstanceIdFunc.accept(entityInstance);
        addInstanceToMapFunc.accept(entityInstance);
        return entityInstance;
  ``` 
- What have you learned about design (either good or bad) by reading your team mates' code?
  - I learned how to correctly use Consumer and Supplier from Jingchao (Jason) from above examples
  - I learned how to use advanced abstractions to make the program more flexible and general like below
  ```java
      public static <U> Try<U> apply(ThrowingSupplier<U> op) {
        try {
            return Try.success(op.get());
        } catch (Throwable e) {
            return Try.failure(e);
        }
    }
  ``` 
  - I learned to create specific exceptions like below
  ```java
  public class InvalidIdException extends IdException {
    public InvalidIdException() {
        super("GameObject Instance has an invalid Id");
    }

    public InvalidIdException(String message) {
        super(message);
    }

    public InvalidIdException(Throwable e) {
        super(e);
    }

    public InvalidIdException(String message, Throwable e) {
        super(message, e);
    }
  }
  ```  

## Your Design

### Describe how your code is designed at a high level (focus on how the classes relate to each other through behavior (methods) rather than their state (instance variables))

- View (a group of elements in the front end. Top of hierarchy): calls `initializeElements()` to add menu bar and side bar
  - Menu bar extends SubView
    - When the menu bar initializes, a tab pane will be formed, including the 1. editor, 2. the main tab with logo, 3. the tab for phase graph, and 4. the tab for winning condition
      - Main tab: dummy tab with our logo
      - Editor extends SubView
        - Grid
          - can be resized by calling `ResizeGridView` in menu bar.
        - Tile
          - can be formatted on the grid by triggering `TileSettingDialog`.
        - Entity
          - can be edited on `EntityEditor`
      - Phase Tab
        - Phase graph
          - `PhaseChooserPane` is the aggregate of all the phase graphs. It contains the left side which is a listView of names of all phases, and the right side where it is the actual phase graph.
          - `PhaseNodeFactory` calls `makeNodes(double xPos, double yPos, String name)` to generate node on the right side of the phase graph
          - Groovy graph
            - `GroovyNode` class is a StackPane that contains a single groovy block (rectangular shape). These nodes can be dragged by the user and moved as there are listeners for the node that update their locations.
  - Side bar extends SubView
    - Each treeview is separate from each other, and all the instances on the treeview is on the track of the CRUD object manager
    - Each type of object has their own class such as `Player`, `Entity`, and `Tile` in the treeItemEntries package.

### Describe two features that you implemented in detail — one that you feel is good and one that you feel could be improved

- Feel good: `TileSettingDialog`
  - Justify why the code is designed the way it is or what issues you wrestled with that made the design challenging.
    - The challenging issue was to connect the side view with this dialog, which is a pop-up window that comes out when the user clicks the "Tile Setting" in the menu bar.
    - This class is created for users to easily map tiles on the grid. To make the user experience better, an interactive model should be employed.
    - To enable drag and drop function between these two completely unrelated windows, I tried very hard to associate their stages together.
    - finally, I was able to write this method
    ```java
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
    ```
  - Are there any assumptions or dependencies from this code that impact the overall design of the program? If not, how did you hide or remove them?
    - The dependency comes from View, which passes its stage all the way down to this pop-up window. This dependency cannot be removed because it is essential to make them the same stages, otherwise the dragevent would not recognize the pop-up window.
    - It also requires a `GameObjectsCRUDInterface` to identify and generate instances of tiles on the pop-up window. This dependency cannot be removed.
- To be improved: `View`
  - Justify why the code is designed the way it is or what issues you wrestled with that made the design challenging.
    - View is well designed in that it makes sure it is adding elements that implement the SubView interface. It add all the elements into one big group to be displayed.
    - However, in terms of positioning the elements, maybe a better way is using GridPane rather than AnchorPane. Here we've used anchor pane to set positions of elements.
    ```java
    /**
     * Set the positions of the components in an AnchorPane.
     */
    private void setElements() {
        AnchorPane.setLeftAnchor(menuBar.getView(), 0.0);
        AnchorPane.setRightAnchor(menuBar.getView(), 0.0);
        AnchorPane.setTopAnchor(menuBar.getView(), 0.0);
        AnchorPane.setLeftAnchor(mainView, 0.0);
        AnchorPane.setTopAnchor(mainView, MENU_BAR_HEIGHT);
        AnchorPane.setRightAnchor(mainView, 0.0);
        AnchorPane.setBottomAnchor(mainView, 0.0);
    }
    ```
    - This creates one disadvantage in that if the pane size is changed, the layout will look ugly, because now the positioning is always fixed.
    - If we use grid pane, the relative location will not be dependent on the window size.
  - Are there any assumptions or dependencies from this code that impact the overall design of the program? If not, how did you hide or remove them?
    - Yes, by using AnchorPane, we use static methods from it.
    - Also, this class depends on all its SubView children.
    - It also depends on `GameObjectsCRUDInterface` which would manage all instances in the program

## Flexibilty

### Describe how the project's final API, the one you were most involved with, balances

- `SubView` (Haotian and I created this together but I did not put my name in the author) 
  ```java
  public interface SubView<T extends Node> {
      /**
      * This method returns the responsible JavaFx Node responsible to be added or deleted from other graphical elements.
      *
      * @return A "root" JavaFx Node representative of this object.
      */
      T getView();
  }
  ```
   - Power (flexibility and helping users to use good design practices) and Simplicity (ease of understanding and preventing users from making mistakes)
   - It is powerful in that it make all the children of view abstract and inhabits any changes to the children of view. in this ways it prevents modification by the `View` class.
   - It is simple in that it allows only method to be called to retrieve the element from the children.
- `Viewable`: an interface for `EntityView`
  ```java
  public interface Viewable {

    /**
     * This method changes the image that the viewable displays
     *
     * @param path The path to the new image
     */
    void changeImage(String path);

    /**
     * This method changes the position of the viewable
     *
     * @param xPos The new x position
     * @param yPos The new y position
     */
    void changeCoordinates(Double xPos, Double yPos);

    /**
     * This method get the Imageview stored in the viewable
     *
     * @return The stored Imageview
     */
    ImageView getImageView();

    /**
     * This method provides a consumer to remove the entity from the current javafx scene
     *
     * @return
     */
    void removeEntity();

    /**
     * This method adds a PropertyChangeListener to the viewable
     *
     * @param listener The java beans listener to be added
     */
    void addListener(PropertyChangeListener listener);
}
  ```
  - Power (flexibility and helping users to use good design practices) and Simplicity (ease of understanding and preventing users from making mistakes)
  - It's powerful in that it specifies the actions that can be performed on the Viewable objects such as the entityView, including `removeEntity()`, `addListener()`, etc.
  - It's simple in that the naming conventions are easy to follow and the method names are very self-explanatory.

### Describe two features that you did not implement in detail — one that you feel is good and one that you feel could be improved

- First Feature: `StatusView` can show "Batch Mode" and "Delete Mode"
  - What is interesting about this code (why did you choose it)?
    - Uses simple animation in the side view to notify the user which mode he/she is on. It is really cool because the animation is connected with the program setting. If it's on Batch mode, then the user could add a bunch of objects. If it's on delete mode, then the user could delete stuff by hovering over.
  - What classes or resources are required to implement this feature?
    - `SingleNodeFade` class in the simpleAnimation method is required to create the fading animation.
  - Describe the design of this feature in detail (what parts are closed? what implementation details are encapsulated? what assumptions are made? do they limit its flexibility?).
    - The constructor and all the methods are public and open. In the SingleNodeFade class all methods are public as well.
    - This is necessary because here the methods in `StatusView` act like API called by the user's key board action. These methods have to be public. Therefore, there is no encapsulation.
  - How extensible is the design for this feature (is it clear how to extend the code as designed or what kind of change might be hard given this design)?
    - Yes, it is easy to extend this feature. If we want to add more key bindings to change the editing mode, then we can directly add to `StatusView`. If we want to change the animation, we could go to the `SingleNodeFade` and change it.
- Second Feature: `SimpleGameObjectsCRUD` that keeps track of all the instances and objects in the front end and back end
  ```java
  public class SimpleGameObjectsCRUD implements GameObjectsCRUDInterface {
    private static final String ROOT_NAME = "Game Objects";

    private int numCols;
    private int numRows;
    private String bgmPath;
    private Map<String, GameObjectClass> gameObjectClassMapByName;
    private Map<Integer, GameObjectClass> gameObjectClassMapById;
    private Map<Integer, GameObjectInstance> gameObjectInstanceMapById;

    private TileInstanceFactory myTileInstanceFactory;
    private EntityInstanceFactory myEntityInstanceFactory;
    private CategoryInstanceFactory myCategoryInstanceFactory;
    //    private SoundInstanceFactory mySoundInstanceFactory;
    private PlayerInstanceFactory myPlayerInstanceFactory;

    private IdManager myIdManager;
  }
  ```
  - What is interesting about this code (why did you choose it)?
    - CRUD (create, read, update, and delete) interface is really useful because it in real time manages all the instances and objects globally.
  - What classes or resources are required to implement this feature?
    - It requires `TileInstanceFactory`, `EntityInstanceFactory`, `CategoryInstanceFactory`, `PlayerInstanceFactory`, and `IdManager`
  - Describe the design of this feature in detail (what parts are closed? what implementation details are encapsulated? what assumptions are made? do they limit its flexibility?).
    - Retrieving information from the internal maps is closed, because it does not want other classes to access its data.
    - Creating classes and instances are open, because the user could trigger actions to let the CRUD interface add objects into its internal map
    - However, some methods look really similar and seem a little bit repetitive.
    - Also, this is a huge class with over 800 lines of code
  - How extensible is the design for this feature (is it clear how to extend the code as designed or what kind of change might be hard given this design)?
    - It is extensible, but it requires plenty of effort to add new item entry on the side bar to be managed by this interface. Suppose another type of game object named "Power Up" is added to the side bar, then it means the programmer must go the this inteface and add methods for power ups, including delete, create, add, and get.

## Alternate Designs

### Describe how the original APIs changed over the course of the project and how these changes were discussed and decisions ultimately made

- Game Object and Logic Design for User Experience and Flexibility
  - Background: turn-based strategy game is a very broad category. It can be a card game, board game, or can overlap with some RPG games. In order to make the design flexible, we should abort some assumptions and restrictions to provide maximum flexibility. Therefore, we need to provide an engine that allows the user to define their own settings, actions, and game rules.
  - Initial Design Decision
    - We thought about defining some basic actions such as "moving forward", "change image", and "attack", but adding such restrictions would limit the number of games that we can create. Users could only follow the actions that we provide but hardly customize their unique actions.
    - We thought about when defining a type of object such as entity, we attach all the actions to it. Every such instance must have the same actions. This way it should be easy for the program to recognize and process. However, it prevents us adding variations for characters with the same image.
    - We thought about defining some attributes such as "hp" and "power" for the entire engine initially. The user could select attributes from a combo box and assign a value to the character. However, all the attributes would be hard-coded this way.
  - Later on
    - Natalie and Inchan proposed having Phase-Specific FSMs, while I was actually a little bit worried because I was unsure whether we could finish the project on time if we adopt the approach. Then we had a discussion on the design of the
    - Pros:
      - A single FSM would allow for everyone's turn to be represented in the graph. This would encapsulate the logic of reversing, skipping, and adding turns.
      - Enable a lot of flexibility for the users in terms of what actions they can take, because they can basically write groovy code while connecting groovy nodes together.
    - Cons:
      - specific to each phase makes it easier to implement different rules/difficulty/goals for various levels of a game. A single FSM that represents all the game logic would also have nodes specific to each player's turn, which provides unnecessary complexity (which make it time-consuming to create one game).
    - Our Rationale:
      - We decided to use phase-specific FSMs because it is more encapsulated than a game-wise FSM. As mentioned, it also allows for the easy addition of different levels, and it also separates the logic of turns into a separate class.

### Describe a design decision discussed about two parts of the program in detail:

1. Change TreeView to Accordion and TitlePane
   - What are the trade-offs of the design choice (describe the pros and cons of the different designs)?
     - TreeView
       - Pros: the appearance is really nice and professional. We could add categories such as "Entity", "Player", and "Tile". Under each of these items, more items can be added as their child items (TreeCell). The appearance resembles IntelliJ.
       - Cons: Each TreeCell is not an Node element, thus don't have drag and drop listener and functionality. Therefore, we cannot drag and drop these items on the right to the editor. Even when drag and drop function is achieved, it is hard to integrate the front end with the back end.
     - Accordion & TitlePane
       - Pros: Each element on the TitlePane is draggable and can be arranged in the order specified by the programmer. For example, as some other teams have executed, they used accordion to store pre-made stuff. Since the objects inside a TitlePane are fixed, backend could easily identify and keep track with the objects in the front end.
       - Cons: It is really hard to enable the user to dynamically add/remove/edit elements inside the accordion. Normally, the user could only use whatever they are given in the accordion.
   - Our Decision: We sticked with TreeView and wrote a bunch of helper classes to achieve the drag and drop functionality. We spent around one week trying to figure out the problem and integrate with back end. (we created a package specifically for sidebar, which contains `Entity`, `Tile`, `Player`, `CustomTreeCellImpl`. Inside our `CRUDInterface`, we also used instance and class to differentiate front end objects and back end objects)
   - My Preference: I would prefer using accordion and titlePane, because in that way we could potentially save a lot of work. However, when we realized we could have used TitlePane, we are half way done with TreeView and it was hard to start over. Therefore, we decided to stick with TitlePane.
2. One manager in each front end and back end
   - What are the trade-offs of the design choice (describe the pros and cons of the different designs)?
     - Two managers in total
       - Pros: The modules of front end and back end could reduce some dependencies by setting up a listener.
       - Cons: These two managers need to communicate with each othe consistently whenever a change in the front end or back end happens, which might cause some unnecessary, circular communications.
     - Only one manager in back end
       - Pros: CRUDInterface (create, read, update, delete) interface is extremely powerful in that it could listen to changes that happen to it either through front end or back end. It serves as a centralized manager for all the object data. When something is added to backend, it will first update back end information and then make it appear in the front end.
       - Cons: It creates dependencies for many classes across front end and back end.
   - Our Decision: We used CRUDInterface to manage all the objects and theri instances.
   - My Preference: I am pretty happy with our decision. This is because CRUDInterface is really powerful in managing all the objects data that we have. Since this is a huge project, we would like to just keep on manager so we do not confuse ourselves by having two. The CRUDInterface was firstly written in the back end, and then when we integrated into our front end, it was really hard. It took us about 4 days to integrate them together because lots of things in the front end now need to depend on CRUDInterface.

## Conclusions

### Describe the best feature of the project's current design and what did you learn from reading or implementing it?

- The best feature is the phase graph and the grooby graph embedded inside the phase graph. All these classes reside in the package `graphUI`, including two packages `groovy` and `phase`. These 2 packages alone create the GUI for the user to create phase graphs for the game logic and the groovy graph for the game rules and conditions to customize their game. These graphs are used as the FSM within a game.
- In the backend, the `engine` package manages all the concrete classes referred by the graph's GUI. These include `Edge`, `Entity`, and `Node`. `GameMethods` is very useful because it allows the user to create their own methods that check the rules and conditions of the game. In this way, it saves the user time and effort from connecting blocks together.
- I learned about how to use factory pattern to generate multiple instances of phase and groovy nodes.
- I also learned about how to use XStream to serialize objects. In this case, the graph positions and the connections between nodes.
- I learned how to build an interpreter to translate groovy blocks into groovy codes and generate corresponding error messages

### Describe the worst feature that remains in the project's current design and what did you learn from reading or implementing it?

- Inconsistency of grid size in the GameObjectManager and the grid in the editor
  - If the grid size in the view is 4*4 and we resize the grid into 6*6. Then later on when you want to drag something onto position (5, 5), then it will report error.
  - This happens because of the resize method does not go through the GameObjectManager.
  - It indicates an underlying issue with the initial setting of the game engine, because in order to change the grid size, there are many settings that need also be changed in the front end and back end. This says that some settings are not consistent
- I learned that the programmer of a certain feature should make sure it can be easily changed by the user. Dependencies and passing parameters around are not good. One should make it simple for users to change program settings.

### Consider how your coding perspective and practice has changed during the semester:

- What is your biggest strength as a coder/designer?
  - To think about design plans in the long run in order to find the balance between flexibility and viability
- What is your favorite part of coding/designing?
  - Communicating with teammates about their ideas on design and figure out the best route to take
- What are two specific things you have done this semester to improve as a programmer this semester?
  - I have maintained good documentation practices such as commenting on the classes I wrote.
  - I have constantly refactored my code to make them readable and reusable.