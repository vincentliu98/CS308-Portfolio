CompSci 308: Game Analysis
===================

> This is the link to the assignment: [Game](https://coursework.cs.duke.edu/CompSci308_2018Fall/game_jl729)

Project Journal
=======

### Time Review

* Start date: 9.1.2018

* End date: 9.9.2018

* Total Hours Spent: 23

* Time distribution
    * Learning JavaFX: 2
    * Debugging: 8
    * Coding new features: 9
    * Refactoring: 3
    * Designing: 1
    
* Code management: 
    * I usually spend 2 to 3 hours per block to code, and by the end of the it I will commit and push it
    * However, sometimes, I commit and push multiple features at the same time, which might make it harder to reflect
     back when I want to know when I developed a certain feature

* Tasks
    * The easiest task was to make the ball bounce, because I adapted code from Professor Duvall's lab_bounce
    * The hardest task was to make the ball bounce off the bricks correctly. No matter how hard I tried, the ball would 
    bounce weirdly. Now, it works most of the time, but there are some small issues.
    * Good uses of time
        * Gain a basic understanding of JavaFX before diving in
        * Think about the high-level design of a new feature
        * Move forward even though the project is not perfect
    * Bad uses of time
        * Spend too much time debugging on small bug
        * Copy and paste to create duplicated code
    
### Commits

* Total Commit: 22

* Average per day: 2.2 commits

* Looking at my commits, I cannot clearly see a story. Firstly, the names are not in the same format. Most of them started
with a verb, while some started with a noun. Secondly, the descriptions of the commits are not descriptive enough about
what I have done. Therefore, I think I should commit more, and each time I should use a better name for the commit.

* [My favorite commit NO.1](https://coursework.cs.duke.edu/CompSci308_2018Fall/game_jl729/commit/7266174a444fcf7abc3b19423cf44efeb53c7224):
"added size and extra power power-ups", 9.5.2018
    * Purpose: to add two kinds of power-ups. The first one is to expand the paddle, and the second is to give the ball
    extra power so it can break any bricks it hits.
    * I packaged these two changes together because I think they both lie under the power-up feature, and they are similar
    in some ways.
    * There are 175 additions and 15 deletions, so it is relatively readable for people. It might be helpful is I divided
    this commit into two commits, because I actually created 2 new classes.

* [My favorite commit No.2](https://coursework.cs.duke.edu/CompSci308_2018Fall/game_jl729/commit/68c3437114b15c1ca8d046cf0e36ad932077af72):
"refactored code", 9.8.2018
    * Purpose: to construct a superclass for all three kinds of power-ups
    * I package these changes together because the three power-ups' classes inherit from the superclass that I created in
    this commit, which is called PowerUp 
    * There are 62 additions and 117 deletions. It is pretty readable because it is easy to grasp the gist of this commit.

### Conclusions

* I slightly underestimated the size of the project. I knew it would be hard and I started early on the porject. In the end I spent 3 more hours than I expected. Now I have gained a better understanding about working on a JavaFX project, I would be able to estimate better.

* I spent about five hours trying to make the ball bounce off from the bricks correctly. However, it still works weirdly sometimes. This is because I am not very familiar with JavaFX's positioning of nodes, thus I had trouble setting the bounds of the intersection.

* If I can work on the project right now, I would create several classes for bricks and levels and transfer some code from the Main class there. This will make my code in Main class easier to understand.

* To be a better designer, I should start planning what classes and methods I am going to build before I code. I also should take advantage of features such as encapsulation and inheritance. I should keep using variables instead of magic values. I should stop cramming all code in one class or using too many public keywords.

Design Review
=======

### Status

* The code is generally consistent with the naming conventions and style. However, the layout is a bit strange, because the Main class has an unproportional amount of code. The descriptiveness is not very evident.

* The code requires comments to be understood. One solution to improve it is to create methods within separate classes to render a specific goal for each method, and use the method in the Main class. In this way, the method will generally summarize the funtion.

* The constants are made public and can be accessed using the context from the main class. Other variables are made private. Getters and setters are constructed in the class for other classes to access them.

* Two pieces of code
    * __First__: 
    ```java
            // move extraballpower power-ups and check for collisions
            for (int i = 0; i < extraBallPower.size(); i++) {
                if (extraBallPower != null) {
                    extraBallPower.get(i).move(elapsedTime);
                    extraBallPower.get(i).hitPaddle(myPaddleX, SIZE);
                    extraBallPower.get(i).updatePowerState(thereIsExtraPower, power_time_limit, SECOND_DELAY, TIME_LIMIT);
                    extraBallPower.get(i).checkHit(thereIsExtraPower, extraBallPower.get(i));
                }
            }
    ```
     * This code is in the step method within the Main class. Its functions are to move the power-up and check for the collision
        between the power-up and the paddle. If it hits, then it updates the state of the power-up and perform some actions produced
        by the power-up.
     * This is a relatively good piece of code because it is concise and generalizes the function of the code. The methods for the
        ExtraBallPower class are created within the ExtraBallPower class.
    * __Second__: 
    ```java
              public void bouncePaddle() {
                  // deal with bouncing off the paddle
                  if (myBouncer.getBoundsInLocal().intersects(context.myPaddle.getBoundsInLocal())) {
                      context.setRecentlyHit(context.getRecentlyHit());
                      // make the ball bounce normally in the middle, and bounce back to its original route
                      if (context.getBounceWeird() && myBouncer.getBoundsInLocal().getMinX() < context.myPaddle.getBoundsInLocal().getMinX() + context.myPaddle.getBoundsInLocal().getWidth() / 4) {
                          myVelocity = new Point2D(-myVelocity.getX(), -myVelocity.getY());
                      } else if (myBouncer.getBoundsInLocal().getMinX() < context.myPaddle.getBoundsInLocal().getMinX() + context.myPaddle.getBoundsInLocal().getWidth() * 3 / 4) {
                          myVelocity = new Point2D(myVelocity.getX(), -myVelocity.getY());
                      } else if (context.getBounceWeird() && myBouncer.getBoundsInLocal().getMinX() < context.myPaddle.getBoundsInLocal().getMinX() + context.myPaddle.getBoundsInLocal().getWidth()) {
                          myVelocity =new Point2D(-myVelocity.getX(), -myVelocity.getY());
                      } else if (!context.getBounceWeird() && myBouncer.getBoundsInLocal().getMinX() < context.myPaddle.getBoundsInLocal().getMinX() + context.myPaddle.getBoundsInLocal().getWidth()) {
                          myVelocity = new Point2D(-myVelocity.getX(), -myVelocity.getY());
                      }
                  }
              }
    ```
     * This piece of code is from the bouncePaddle class from the Bouncer class. It makes the ball bounce off from the
        same route it came from, if it hits the first quarter or the last quarter of the paddle. The ball will bounce normally
        off the paddle when it hits the middle half of the paddle.
     * This code is relatively a bad design. Firstly, there are so many variable names that make it hard to read. Secondly,
        when the speed is changed, it will be easier to create method such as "reverseX" and "reverseY" to shorten the code.
        
### Design

* High level design
   * The program consists of 8 classes in total. 
   * Welcome.java is the welcome page that displays when the program starts, and it triggers the Main.java to run when the user presses the "Enter" key. 
   * The Main class creates the animation and controls the interaction between the bouncer, the bricks, the paddle, and the power-ups. 
   * The bouncer has its own class named Bouncer.java, which deals with the bouncer's the movement and collision with the wall
   * There are 3 types of power-ups: ExtraBallPower, PointsPower, and SizePower. These three power-ups all have their own class and inherits the PowerUp class. The PowerUp class 
    handles the movement of the power-ups and the collision with the paddle
   * The Texts class contains the winning and losing text when the game reaches a certain stage

* How to add new features
    * Add different levels with different layout of bricks
        * Create a new .txt file in the resources folder. Write digits from 1-4
        * Import the file as a string in the global field
        * Depending on the number of columns in your design, consider changing BRICK_COLUMN in the Main class's field
        * Change the switch(current_level) statement in the setUpGame method
    * Add a different power-up
        * Create a class that extends PowerUp.java
        * Depending on the effect of your method, you might want to override the hitPaddle method. This method deals with
        what would happen when the power-up hits the paddle
        * Import the image as a string in the global field of Main class
        * Initiate an array of your power-up in the setUpGame class
        * In the for loop within the step method, create an instance of your power-up and specify the effect

* Assumptions/Decisions to simplify or resolve ambiguities in the project's functionality
    * At first I tried to use the .gif files from the resources folder to create bricks. However, I was not familiar with
    the coordinates for ImageViews, and I had trouble making the bouncer bounce correctly from the brick. Thus, I chose rectangles
    to construct bricks in the end.
    * When the brick is supposed to disappear, I first set the width and the
    height to 0, but the bouncer still bounces around that region where the brick existed previously. Therefore, in order to
    remove the bricks, I chose to set the X and Y to a negative value additionally. In this way, it resolves the conflict, but
    there must be better implementations.

* 2 features
    * Make the power-up's effect disappear in a certain time interval. Inside the ExtraBallPower class
        ```java
            public void updatePowerState(boolean thereIsPower, double time_limit, double SECOND_DELAY, double TIME_LIMIT) {
                if (thereIsPower) {
                    context.setPower_time_limit(time_limit - SECOND_DELAY);
                }
                if (time_limit <= 0) {
                    context.setThereIsExtraPower(false);
                    context.setPower_time_limit(TIME_LIMIT);
                }
            }
        ```
        This piece of code is relatively a good design. When there the paddle has received the power-up, the state thereIsPower
        will be true, and the time limit will decrease gradually. Once the time limit is below 0, I set thereIsPower back to false,
        and restore the time limit to wait for the next power-up.
     * A welcome page
        ```java
               public void start(Stage stage) {
                   myScene = setupGame(SIZE, SIZE, BACKGROUND);
           
                   stage.setScene(myScene);
                   stage.setTitle(TITLE);
                   stage.show();
           
                   stage.getScene().setOnKeyPressed(
                           event -> {
                               if (event.getCode() == KeyCode.ENTER) {
                                   new Main().start(stage);
                               }
                           }
                   );
           
               }
        ```
        This piece of code is from the Welcome class. It is well designed in that this page is always shown first to the
        player. Once the player presses the "Enter" key, the game will start. In this case, the user interface is more user-friendly.
        
### Alternate Designs

* Describe two design decisions you made, or wish you had done differently, in detail:
    1. First Alternate Design:
        * _Alternative Approach_: create 3 different brick classes, each type of brick having the field named brickLife, instead of using an arrayList to keep
        track the life of bricks.
        * _Trade-offs_: I used Scanner to read the digits from .txt files and understand the layout
                            of different bricks. In order to add or modify the layout, one must be familiar with the convention that I use, which is a con
                            for this design. For instance, the digit 4 in the .txt file means it will create a brick that goes away after 3 hits, and the
                            digit 1 in the .txt file means there is no brick. This design might be confusing for people who see my code for the first time.
        * _Preference_: I would prefer having separate classes for bricks. This make the code easier for others to read.
    2. Second Alternate Design:
        * _Alternative Approach_: Create a separate class for the paddle so that it does not cram up in the Main class.
        * _Trade-offs_: I created the paddle directly inside the Main class rather than creating a separate class for it. When dealing with the
                            collision with the paddle, I simply used the context from Main class to get the paddle.
        * _Preference_: For this project, I would prefer having separate classes for the paddle, because it is easy to deal with,
        and I don't need to pass the private variables across classes, which makes it convenient. If the application is larger,
        I would create a separate class for the paddle.

What are the trade-offs of the design choice (describe the pros and cons of the different designs)?
Which would you prefer and why (it does not have to be the one that is currently implemented)?

What are the three most important bugs that remain in the project's design or implementation?
1. The bouncer bounces off the bricks abnormally sometimes due to the boundary settings in the bouncer class
2. The bouncer will oscillate on the top of the screen if the player uses the cheat key to jump to a certain level
3. When the player loses, the text for losing does not show up. Instead, a black rectangle shows up