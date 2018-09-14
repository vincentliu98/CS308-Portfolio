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
    * First: 
    * Second: 

### Design

You can put blocks of code in here like this:
```java
    /**
     * Returns sum of all values in given list.
     */
    public int getTotal (Collection<Integer> data) {
        int total = 0;
        for (int d : data) {
            total += d;
        }
        return total;
    }
```

### Alternate Designs

Here is another way to look at my design:

![This is cool, too bad you can't see it](crc-example.png "An alternate design")

