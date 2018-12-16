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
  - Editor
  - Side bar
  Game Authoring
  - Introduce the Grid Tab
    - Grid
      - It’s a framework
      - It’s resizable -> Tools MenuBar
    - Tile -> background/land, e.g. grassland, dessert
    - Entity
  - Show side bar
    - Add entity -> Circle & Arrowman
    - Delete entity -> x
    - Say “Tile works the same way”
    - Show Editor
    - Drag Entity
    - Drag text -> o
    - Drag newly added image -> Circle/Arrowman
    - Talk about make them draggable inside Editor
  - Show Phase
    - Make one phase
    - Drag button to add a node
    - Connect them by dragging
    - Go to groovy code
    - Amy’s part
    - Multiple phase

- How is the design tied (or not) to the chosen game genre. What assumptions need to be changed (if anything) to make it handle different genres?
- What is needed to represent a specific game (such as new code or resources or asset files)?

### Describe two packages that you did not implement:

- Which is the most readable (i.e., whose classes and methods do what you expect and whose logic is clear and easy to follow) and give specific examples?
- Which is the most encapsulated (i.e., with minimal and clear dependencies that are easy to find rather than through "back channels") and give specific examples?
- What have you learned about design (either good or bad) by reading your team mates' code?

## Your Design

### Describe how your code is designed at a high level (focus on how the classes relate to each other through behavior (methods) rather than their state (instance variables))

### Describe two features that you implemented in detail — one that you feel is good and one that you feel could be improved

- Justify why the code is designed the way it is or what issues you wrestled with that made the design challenging.
- Are there any assumptions or dependencies from this code that impact the overall design of the program? If not, how did you hide or remove them?

## Flexibilty

### Describe how the project's final API, the one you were most involved with, balances

- Power (flexibility and helping users to use good design practices) and
- Simplicity (ease of understanding and preventing users from making mistakes)

### Describe two features that you did not implement in detail — one that you feel is good and one that you feel could be improved

- What is interesting about this code (why did you choose it)?
- What classes or resources are required to implement this feature?
- Describe the design of this feature in detail (what parts are closed? what implementation details are encapsulated? what assumptions are made? do they limit its flexibility?).
- How extensible is the design for this feature (is it clear how to extend the code as designed or what kind of change might be hard given this design)?

## Alternate Designs

### Describe how the original APIs changed over the course of the project and how these changes were discussed and decisions ultimately made

### Describe a design decision discussed about two parts of the program in detail:

- What alternate designs were proposed?
- What are the trade-offs of the design choice (describe the pros and cons of the different designs)?
- Which would you prefer and why (it does not have to be the one that is currently implemented)?

## Conclusions

### Describe the best feature of the project's current design and what did you learn from reading or implementing it?

### Describe the worst feature that remains in the project's current design and what did you learn from reading or implementing it?

### Consider how your coding perspective and practice has changed during the semester:

- What is your biggest strength as a coder/designer?
- What is your favorite part of coding/designing?
- What are two specific things you have done this semester to improve as a programmer this semester?