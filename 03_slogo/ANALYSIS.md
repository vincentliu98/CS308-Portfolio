CompSci 308: SLogo Analysis
===================

> This is the link to the assignment: [SLogo](https://coursework.cs.duke.edu/CompSci308_2018Fall/slogo_team07)

Design Review
=======

### Overall Design

- Describe the overall design of the complete program:
  - What is the high level design of each part (front and back end) and how do they work together (i.e., what behavior, data, or resources each part depends on from the others).
    - This project is implemented using MVC model. The Controller mediates the communications between the Model and the View. Note, our Model is named "ModelController" instead.
    - `TabView` (an anchorPane containing a button and a TabPane. Initially it contains one Tab. When adding more tabs, it initialize the following content inside the same stage)
      - `Controller`
        - _Overview_: `Controller` is the mediator between the front end and the back end. It contains an instance of `ViewAPI` (external API), `Turtle` (from back end), and `ModelController` (from back end). Therefore, it is reasonable to initialize the Controller before initializing ViewAPI and ModelController because Controller is in a higher level than them.
        - When the user action takes place in the front end, the Controller will receive the command and decide how to process it. Its method  `runCommand()` is used to achieve this. This method takes in a string of command, first checks if the string is empty, and throw error by giving an error message to View to report the error in an Alert.
        - It also contains methods called by the backend to pass updated version of variables and functions to the front end to be displayed, and it contains a method named `updateVar()` to run the command when the user changes the value of the existing variables.
      - Front end:
        - _Overview_: `TabView` contains View and allows multiple GridPanes within View class to be display in multiple tabs. Front end has two APIs, internal (`ViewInternalAPI`) and external(`ViewAPI`). The external methods are called in the `Controller` class, and the internal parts reside in the `View` class. The View class contains a GridPane with lots of sub-components of UI. Some of these sub-components do not have states, such as `HelpView`, then it does not include any state changes and does not interact with the View class. However, most of the sub-components include state changes, thus they contain an instance of the View type and will call View's internal method to achieve some functions, e.g. passing commands
        - `View` (one or more View can be contained by the TabPane)
          - `SubViews` (arranged in a GridPane)
            - `CommandView`
              - take in command and pass command to the View and to the Controller
              - save/load .logo file
              - reset turtle to the origin
            - `FunctionView`
              - display user-defined functions in a ListView
            - `HelpView`
              - display all the available commands in a ListView
              - Upon clicking, an information box will show up, including the command name and its funtion
            - `HistoryView`
              - display all the history of the user
              - Upon clicking the command will re-run
            - `SettingView`
              - include buttons and boxes to change program settings, e.g. language, color, speed, etc.
            - `StateView`
              - include the turtle's state when the mouse is hovering above a certain turtle
            - `VariableView`
              - display all the variables in an _editable_ TableView.
              - Able to change the variable name and value
            - `DisplayView`
              - display the turtle and pen in the center of the GUI with _animation_
              - It implements **Observer** interface because it needs to observe the changes within the turtle in order to make animation
              - `Pen`
                - a Line managed by PathMangaer
              - `Sprite`
                - an ImageView of "turtle" managed by TurtleManager
          - The Undo function is not complete (only apply to palette while now we don't have a palette)
      - Back end:
        - _Overview_: The control center of the back end is `ModelController`, our "Model" in the MVC structure. It parses the string it receives from the front end and create a list of command `Node`. Each command represents one class inside the Command folder. In creating all the Command classes, we use reflection to get the constructor by the command name, and then create a list of such `Node` in a List. Each node extends RootNode, an abstract class that contains an intance of Turtle (which is initialized as a generic Turtle type but is actually a TurtleGroup) then it runs the list of Nodes by calling methods inside the Turtle. Both the TurtleLeaf and the TurtleGroup implements Turtle interface, while there are different methods in the TurtleGroup.
        - `ModelController`
          - It keeps an instance of `Turtle`, `TreeFactory`, `Interpreter`, and `Storage`. It deals with the parsing, creating `Node` type (i.e. our argument Node), running the Node, and passing the result as a list of string to the front end. It also has the method of retrieving the updatd variables and functions from the back end to the Controller.
        - `Interpreter`
          - It takes in the string command and return a list of standardized command in the string form. For example, if the input is "fd 50", it will return [Forward, 50]. It uses the syntax from the Syntax.properties file and interprets the command by matching regex in certain patterns.
        - `Turtle`
          - It is an interface implemented by `TurtleLeaf` and `TurtleGroup`. These three elements form a **Composite patter**. While `TurtleLeaf` has methods to implement individual actions, the methods within `TurtleGroup` manage all the turtles in one methods by looping through all the TurtleLeaf it holds. In this case, each command class does not need to contain a loop through all TurtleLeafs. Instead, because the `RootNode` carries a TurtleGroup, each command class only need to care about individual action of the turtle.
          - Each `TurtleLeaf` is registered as **Observable**, because the positions and movements need to be observed by the front end and constantly updated. Under the current setup of our animation, we need to tell the front end when the turtle is finished updating, therefore, we let Turtle call changed() and clear() methods inside each method.
        - `Node` and `TreeFactory`
          - They together form a **Factory** pattern. `TreeFactory` takes in the output from the `Interpreter`, a list of standardized string command, and uses recursion to generate a list of `Node` and store in the `ModelController`. It starts from the first string command in the list and recursively transforming its children to different types of Node, such as `Number` and `Repeat`.
          - When creating the Nodes, it uses **Reflection**. After identifying the type of the Node, the program hacks into the Commands folder and look for the class with the same name. Then, by calling its Constructor, a Node is created and then added to the list of Node.
  - What is needed to add one of the following (include all the parts of the code or resources that need to be changed):
    - new command to the language
      - Add the new command name (e.g. Forward) and its expression (e.g. forward|fd) inside the .properties file inside FrontEnd/src/resources folder for each language.
      - Create a Command class in BackEnd/src/backend/Commands. It should extend `RootNode` and will only contain one method named `run()` and one public constructor.
      - If the command is an action on the turtle and involve any change of position on the turtle, then inside the `run()` method, it must have a method `myTurtle.Changed();` before changing the state.
        - After the line of code for the state change, it must have `myTurtle.clear();` followed by it.
        - In this case, the front end will know when the turtle's change has completed. For example, in the case of `Forward.java` we need let the front end know the starting position and the ending position.
      - If the command is not about turtle's movement, like changing the pen size or color, then simply call `myTurtle.setPenColor(String penColor);`
    - new component to the front end
      - Inside FrontEnd/src/frontend/GUI/SubViews folder, create a new class named `***View.java`. It has to implement SubView interface
      - After fulfilling the content of the new component, the `getView()` method now should return a Node type of the component (e.g. GridPane, VBox, etc.)
      - Inside FrontEnd/src/frontend/GUI/View.java, initiatialize the new component in the `initializeElements(String initLang)` method while adding it as field.
      - Inside the `addElements(GridPane gridPane)` method, call gridPane.add(), with the parameters being the element and the column and row index.
    - feature from the specification that was not implemented
      - provide a way to move the current turtle(s) graphically (at least FD, BK, LT, and RT by a fixed amount)
        - Add four buttons inside the CommandView, each having the text "Up", "Down", "Left", or "Right".
        - Add events to each buttons upon clicking. The action during the event should be calling the `myView` variable's passCommand method with a fixed number (e.g. when clicking "Right" Button, move forward 50).
  - Are the dependencies between the parts clear and easy to find (e.g., public methods and parameters) or do they exist through "back channels" (e.g., static calls, order of method call, sub-class type requirements)?
    - Front End:
      - As mentioned, in the front end there are lots of layers.
      - Inside the `View` instance, some `SubView` depend on the View, because when they sometimes need to pass information and methods to the View. Therefore, their constructors would contain an instance of View.
      - On the other hand, `View` depends on all the `SubViews`, because View might need to call these SubView's methods.
    - Back End:
      - Back end contains some obvious dependencies as well.
      - `ModelController` depends on `Interpreter`, `Turtle`, and `TreeFactory`, because it acts like a controller in the back end thus it has to aggregate all the actions together.
      - Each command in the Command folder depends on the `Turtle` (extended from the field of `RootNode`), because the command might need to perform some actions on the Turtle. Since commands all extend one abstract class, it makes sense to include the `Turtle` in the abstract class as well.
    - Controller:
      - As a management platform, `Controller` has to depend on `ViewAPI` and `ModelController`.
    - Others:
      - There is no static calls, except in the `TabView`'s line 68-72, I used `AnchorPane`'s static method, which is built-in in Java.
    - Clarification:
      - We tried to eliminate dependencies as much as possible. For example, when designing the communication between the back end and the front end, we decide to just let the back end depend on the front end, while not allowing front end to depend on back end at the same time. This avoids circular dependency, which is a bad design.
      - We adopted Observers and Observables to observe changes in the back end and report to the front end. In such way, we no longer need an instance of `View` in the back end thus avoided the dependency of front end in the back end.
  - Reflect on the project's APIs especially the two you did not implement:
    - What makes it easy to use and hard to misuse or not (i.e., do classes and methods do what you expect and have logic that is clear and easy to follow)? Give specific examples.
    - `Turtle` Interface
      - The **composite pattern** makes it easy to use.
      - It contains methods associated with the turtle, and each method is clear about what it is performing.
      - Also, when calling these APIs, I don't need to loop through all the turtles because I am using the type `TurtleGroup`, which makes it easier to perform the same actions on multiple turtles.
    - `ModelController`'s public method (which my teammate Jose should make it implement an interface instead of making the method public)
      - It has a very defined purpose: Receive command from the `Controller` and process the command using the internal mechanisms in the back end.
      - It has three methods that can be called by the `Controller`: parseCommand(String input), updateVar(), and updateFunc(). These methods are easy to use because they also have a very defined purpose.
  - What makes it encapsulated or not (i.e., can the implementation be easily changed without affecting the rest of the program)? Give specific examples.
  - What have you learned about design (either good or bad) by reading your team mates' code?
- Is the total code generally consistent in its layout, naming conventions and descriptiveness, and style? Give specific examples for or against by comparing code from different team members.
  - Naming conventions: Consistent
    - Each class starts with capital letters and follows specific pattern when implementing certain interface
      - My naming conventions for SubViews: `CommandView` and `FunctionView`
      - Jose's naming conventions for Turtles: `TurtleLeaf` and `TurtleGroup`
    - Each method follows camel case and are descriptive
      - My naming convention for methods: `runCommand()`, `displayVar()`
      - Harry's naming conventions for methods: `addVarName()`, `getInsList()`
    - Naming of variables are clear and descriptive
      - My naming conventions for variables: `myStage` from `TabView`
      - Jose's naming conventions for variables: `myTurtle` from `TreeFactory`
    - Naming of constants
      - My naming convention for constants: `DEFAULT_PEN_TIME` from the `View`
      - Jose's naming convention for constants: `commandError` in `TreeFactory` (this variable is a constant and should have been capitalized)

### Your Design

- Describe how your code is designed at a high level (focus on how the classes relate to each other through behavior (methods) rather than their state (instance variables)).
  - `TabView`: Its constructor simply initializes an AnchorPane that contains a TabPane and a button. Inside the TabPane, a Controller is initialized. It is similar to a wrapper, and that is why it does not have methos to interact with other classes.
  - `Controller`:
    - Its constructor initializes the front end and the back end, including the View and the ModelController.
    - Its `runCommand(String input)` method takes in the string command from the `View` and call ModelController to process the command.
    - Its `checkBackEndFuncUpdate()` and `checkBackEndVarUpdate()` methods are called after the execution of a command to check and possibly add new variables and functions to the GUI.
    - Its `updateVar(Map<String, String> var)` is called when the variable value or name is modified by the user in the `VariableView`.
  - `CommandView`:
    - Its constructor initializes the bottom part of the GUI, which is represented as a GridPane. Besides the helper methods that initialize all the TextArea and buttons, it has 2 extra methods.
      - `runCommand()`: It is called when the user presses the "ENTER" key or click on the "Run" button.
      - `returnValues(List<String> ret)`: It shows the returned result as a promptText in the TextArea.
  - `FunctionView`:
    - `updateFunction(List<String> func)`: Take the function list passed from the back end and render in as a ListView.
    - `runCommand(String text)`: Upon clicking, the function will run as a command.
  - `HelpView`: A static page without state change and interaction with other classes.
  - `HistoryView`:
    - `updateHistory(String newHistory)`: Take the last command from the user and render in as a HyperLink in the ListView.
    - `getLastestCommand()`: retrieve the latest command from the user to be saved in a .logo file.
  - `SettingView`: Inlcude lots of setters and getters for pen color, size, language, and image of turtle
  - `StateView`:
    - `changeState(String id, Sprite sprite, PathManager pathManager)`: changes the information presented on the StateView when the mouse is hovering above a Sprite.
    - `showDefault()`: Only display the attributes of the turtle but not the content
  - `VariableView`:
    - `updateVarName(TableColumn.CellEditEvent<Variable, String> t)`: When the user modifies the variable name, it re-run the command through the Controller to update the variable in the `Storage` instance in the back end.
    - `updateVarVal(TableColumn.CellEditEvent<Variable, String> t)`: When the user modifies the variable value, it re-run the command through the Controller to update the variable in the `Storage` instance in the back end.
    - `updateVariable(Map<String, String> var)`: Receive the variable as a map from the back end and display all of them in the TableView. The left column represents the variable name and the right column represents the column value.
- Discuss any remaining Design Checklist issues within you code (justify why they do not need to be fixed or describe how they could be fixed if you had more time). Note, the checklist tool used in the last lab can be useful in automatically finding many checklist issues.
  - Magic values:
    - Inside the `View` class when I set up the GridPane's layout, the Design Checklist says I have too many magic values there. In order to fix that, I will need to change the values from 0, 1, 2, 3 to fields such as SETTING_VIEW_ROW. But I wonder how much easier and more flexible it will help with the program. It is in low priority.
  - Too many cyclomatic procedures:
    - It says that we have 11 cyclomatic procedures, which exceeded the limit of 10. To fix this, our entire team need to rethink our design. Currently, the communication beteween the front end and the back end is a little bit complicated. A user command from the GUI needs to be passed many times to the Interpreter and to be executed by the ModelController. Such elongated process can be shortened, but it means we need to eliminate some classes as well. For example, we might need to ask, is `Controller` class necessary? If we figure out how to make the FrontEnd and BackEnd module independent of each other and run `Main` outside of these modules, we might not need Controller because in this case front end can interact directly with the back end.
- Describe two features that you implemented in detail — one that you feel is good and one that you feel could be improved:
  - Justify why the code is designed the way it is or what issues you wrestled with that made the design challenging.
    - **First Feature**: Showing turtle's state while the mouse is hovering above the turtle
      - This feature involves `TurtleManager` and `StateView`
      - When a turtle in the `TurtleManager` is initialized in the `createTurtle()` method, it will add two event listeners on the turtle, one detecting when the mouse is hovering above the sprite, and another detecting when the mouse is not hovering above the sprite.
      - Durign these two states, different methods will be passed from `View` to the `StateView`, which will choose to display some messages.
      - For example, when the mouse is hovering above the turtle, `StateView` triggers `changeState(String id, Sprite sprite, PathManager pathManager)` method, which changes the information presented on the StateView using the data from sprite and pathManager.
      - When the mouse is not hovering above the turtle, `StateView` triggers `showDefault()` method, which only shows the attribute but not the value. For example: "Pen Color: ".
      - **Design Rationale**:
        - Firstly, I used the MouseHovering property because MouseClicking property has been used by Benjamin, who used that to change the turtle's state between active and inactive.
        - Since this `StateView` represents a certain turtle's state, I wanted to make it as dynamic as possible. Therefore, I chose to only show the data when the mouse is hovering, and revert the content back to default when the mouse leaves the turtle.
    - **Second Feature**: Supporting multiple tabs
      - This feature involves `TabView` and `Controller`
      - `TabView` is an anchorPane containing a button and a TabPane. Initially it contains one Tab. When adding more tabs, it initialize the following content inside the **same stage**. `TabPane` allows multiple tabs. Within the same tab, there is one `Controller` that contains a `ViewAPI` and `ModelController`. The View class contains a GridPane with all the sub-components.
      - **Design Rationale**:
        - For each tab, front end and back end are integrated. That means, when there are multiple tabs, there has to be multiple controllers that each control a View and ModelController. Also, when the setting changes such as the language, the front end (including the comboBox for choosing language) and the back end (including the language choice for the Interpreter) has to change together.
        - Before my refactoring, only one Controller exists in the `Main.java`, which limits the program flexibility. After I set up the mechanism to add tabs, I realize that when I change the language choice or input command in the second tab, it actually only changes the first tab's state. Therefore, I spent hours refactoring the constructors for `Controller` and `View`, because there can only be one stage but there can be multiple scenes. Finally, it worked out when I wrap Controller inside `TabView`, and whenever a new tab is initialized, a new Controller is initialized and its `View` is added to the `TabView`.
        - Therefore, it makes sense to initialize a `Controller` with a certain language, and let `TabView` hold multiple tabs, each containing one `Controller`.
  - Are there any assumptions or dependencies from this code that impact the overall design of the program? If not, how did you hide or remove them?
    - Yes. There are some dependencies.
      - Inside the `View` instance, some `SubView` depend on the View, because when they sometimes need to pass information and methods to the View. Therefore, their constructors would contain an instance of View.
      - On the other hand, `View` depends on all the `SubViews`, because View might need to call these SubView's methods.
    - My code is only for front end, and it only interact with the external API of the back end. Therefore, it does not depend on back end except for the `Controller`.
    - Our main hurdle in design comes from the module. Unable to add a module on the top level of both FrontEnd and BackEnd, we had to put `Controller.java` and `Main.java` inside FrontEnd module, which decreased the flexibility of the program.

### Flexibilty

- Describe what you think makes this project's design flexible or not (i.e., able to support adding similar features easily).
  - I think our project is relatively flexible.
  - For example, we can use Professor Duvall's method to test our flexibility. Suppose we are going to have one more language, after adding the language's _.properties_ file into the FrontEnd/resources folder, there is only _one_ procedure:
    1. Open FrontEnd/src/GUI/SubViews/SettingView, add the new language name as a string to the `list` field.
  - The above example shows the flexibility in the language setting, which is achieved by separating each component of the progarm.
  - Besides this, suppose we are going to add a new `SubView` in the front end, it should be easy just to add it in the same folder with other SubViews, and it is also easy to add it in the View. Therefore, our front end is very flexible.
  - As for back end, We have one command for each class, which makes it flexible, because it successfully separates commands from each other and it makes it easy to find commands and change them.
  - Admittedly, there are some class and methods that are very long in the back end, and they should be separated, such as the `TreeFactory`. On the other hand, our `Turtle` is very flexible overall because we implmented composite pattern.
- Describe two features from the assignment specification that you did not implement in detail (these can overlap the previous ones but must be discussed from this different perspective):
  - First feature:
    - I did not implement _palettes_ of images and colors with their associates numeric values in details
    - Classes or resources required
      - A `PaletteView` class. It should be added in the way proposed in the previous "new component to the front end" bullet point.
      - Note that it should contain a list of Color variables and another list of images in its field.
    - Describe the design of this feature in detail (what parts are closed? what implementation details are encapsulated? what assumptions are made? do they limit its flexibility?)
      - If I am going to implement this feature in the front end, it only requirs a VBox or an HBox which has a scrolling function. In this way, it can contain multiple colors that the user defines.
      - The palette should be integrated with the back end. However, our back end has not implemented this feature yet, and I was not able to add the palette during the project.
      - The `PaletteView` would have a method called `addColor` and `addImage`, and within its fields it will contain a list of colors and a list of images. These two methods will be called by the `View` when it receives command from the back end to add a new color.
      - The open part of the palette would be the two methods that will be called by `View`, and the closed part would be the event handling when the user is interacting with the palette on the GUI, such as some methods like `changeBackgroundColor(Color)` or `changeTurtleImage(Image)`.
  - Second feature:
    - Benjamin and I worked together on the Undo function, however, we did not figure it out for all commands by the deadline.
    - Classes or resources required
      - A `GUIWrapper` that contains a stack of opeartions that the user entered before.
      - Everything under the frontend/Undo folder, including `ChangePenColorOperation`, `Moment`, `Operation` abstract class
      - The "undo" button in `SettingView`
    - Describe the design of this feature in detail (what parts are closed? what implementation details are encapsulated? what assumptions are made? do they limit its flexibility?)
      - The two methods of the `ChangePenColorOperation` are open. They are both public methods that will be called by the button event reaction in the `SettingView`.
      - Inside the `GUIWrapper`, in fact, some methods should have been closed but they remain open. For example, `deleteElementsAfterPointer(int undoRedoPointer)`.

### Alternate Designs

- Describe how well (or poorly) the original design handled the project's extensions to the original specifications and how these changes were discussed and decisions ultimately made.
  - Overall, the original plan did not go well.
    1. We Created `Nodes` class by the number of arguments in the back end, such as `DoubleCommandNode` and `SingleCommandNode`. For example, `fd 50` will be identified as a `DoubleCommandNode` and it will certainly perform the function inside the node. However, this seems like hard coding, because once we have the requirement for indefinite command, we suddenly realized the limitation of our design. In fact, we were supposed to use recursion to create a node for each parsed argument.
    2. We include all command in one class. This is a bad practice because it adds some complexity to the code and made it not as flexible as our current design. For example, when there are too many commands, it certainly make it look ugly because everything is clogged together. Also, at that time we are not using reflection on classes but on methods.
    3. When the multiple tabs requirement came out, I was a little bit worried, because there is only one `Controller` and it only resides in `Main`. After I set up the mechanism to add tabs, I realize that when I change the language choice or input command in the second tab, it actually only changes the first tab's state. I already talked about how I resolved this issue in "Your Design" section and I will mention it in my conclusion and show it for my master code.
- Describe two design decisions discussed by the team about any part of the program in detail:
  - First Decision
    - When we are changing our original `Nodes` to accommodate infinite command, we talked about how we are going to produce Node for each parsed string and distinguish them by type.
    - Alternate Design and its advantages
      - Since they are all going to be represented by nodes, we decided to create an interface that defines all the actions for the nodes, which turned out to be `Node` interface. Then, since each node can possibly be the root of our recursion tree, we decided to create an abstract class named `RootNode`. As for producing these Nodes, we decided to use a **factory pattern** because it is suitable and flexible in producing different types of products. Since we have so many types of nodes and we are using reflection, we decided to integrate factory pattern in our design, thus `TreeFactory`
      - Which would you prefer and why (it does not have to be the one that is currently implemented)?
        - I prefer the second way, which is how we are doing it. When the advanced specification came out, we all knew the way we did it was not right. It certainly has many advantages such as that it can now deal with multiple commands and is able to make the code more flexible by separating all the commands into different classes.
  - Second Decision
    - When we are working on the animation, we did not think too much about how the backend works and how it passes information to the front end. When we realized that back end cannot send information to the front end in the new design, we decided to use observers and observables to update the front end. We even thought about writing our own listeners and speakers because observers and observables are deprecated.
    - Pros and Cons
      - By having Observers and Observables, we made it easier for the information to be passed across program's two ends. However, when we are passing the argument from back end to the front end, we are passing the entire turtle, and that might cause some issues because it exposes the turtle entirely to the front end. Also, having this two way communication increases the complexity of the program.
    - Which would you prefer and why (it does not have to be the one that is currently implemented)?
      - I would prefer the way we are doing it. Otherwise, it will be even more troublesome to let the back end call the Controller and then call the View and the DisplayView. And in doing so, the Design Checklist might report even a worse cyclomatic procedure.

### Conclusions

- Describe the best feature of the project's current design and what did you learn from reading or implementing it?
  - In my opinion, the best feature of the project is that it supports multiple tabs with independent front end and back end. 
  - I say this because I have learned a lot from refactoring our code and thinking about design. 
  - During Cell Society, I did front end, but I was not able to display multiple tabs or scenees in the same stage, because I was not aware of that. This time, I was able to think in a higher level than the Controller itself and add an AnchorPane and a TabPane. If you compare the Controller and View class from basic implementation, you would notice that I have changed some methods and constructors to make multiple tabs work. I learned a lot from doing it and game me lots of fulfillment.
- Describe the worst feature that remains in the project's current design and what did you learn from reading or implementing it?
  - The worst feature right now, I think, is that when back end passed information to the front end, it passes the entire turtle. I wish we could refine our design to make the turtle more encapsulated and only pass its updated information.
  - Also, there is a TurtleManager in the front end and Turtle in the back end. They are a little bit repetitive in terms of their functionality. I think there is plenty of potential to refine our design of front end to minimize or eliminate the TurtleManager while leaving the Turtle in the back end. After all, we only need one place to store all the turtle information and another to represent the information.
- To be a better designer in the next project, what should you
  - start doing differently
    - Record design decisions whenever we had a discussion. This helps with writing the analysis and helps me learn.
    - Involve in more discussions with my teammates
  - keep doing the same
    - Constantly commenting my code and refactoring
    - Keep a consistent style to make it easy for my teammates to understand
  - stop doing
    - code before you finish designing