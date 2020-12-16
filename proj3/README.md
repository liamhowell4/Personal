# Build Your Own World Design Document
**Partner 1:** Wil Aquino

**Partner 2:** Liam Howell

**Date:** November 17, 2020

**Notes:**
1. If a class has information not acknowledged in this document, it is not necessary to know about it, with respect to our project's design.
2. Reference Spec Link for the StdDraw class: https://introcs.cs.princeton.edu/java/stdlib/javadoc/StdDraw.html
3. Descriptions marked with a `TBD` (To Be Done) or a `...` indicate they are unfinished or have yet to have some of their implementation be used within the design.

**Vocabulary Index (Copied from `World` class):**
1. "Room" -- A rectangular area within the world.
2. "Hallway" -- A strictly 3 x n rectangular area which can have a horizontal or vertical orientation.
3. "Edge" -- The wall of a room or hallway which can have a horizontal or vertical orientation.
4. "Point" -- An (x, y) coordinate within the world which CANNOT exceed the world size and thus must exist within the world.
5. "EXIT point" -- Exit point of a room and entrance point of a hallway.
6. "END point" -- End point of a hallway and entrance point of a potential room.
7. "Lower Bound" -- Bottom- or left-most point of an edge.
8. "Upper Bound" -- Top- or right-most point of an edge.
9. "Priority" -- Priorities are actually used for determining which potential hallways get looked at first by using a PQ, although if it's an integer, it doubles as the distance from the base room (priority 0).



## Classes and Data Structures
**TERenderer (TileEngine)**

A 2D rendering engine for building the world. It uses a 2D array of `TETile` objects in order to build such a world. 


----------

**TETile (TileEngine)**

An object representation of tiles to set within the world.


----------

**TileSet (TileEngine)**

A set of tile types to use as `TETile` objects.

**Instance Variables**
1.  `TETile AVATAR` - representation of the user within the world.
2.  `TETile WALL` - representation of a wall.
3.  `TETile FLOOR` - representation of a floor.
4.  `TETile NOTHING` - representation of nothing to display.
5.  `TETile LOCKED_DOOR` - representation of a locked door.
6.  `TETile UNLOCKED_DOOR` - representation of an unlocked door.

7.  `TETile GRASS` - representation of grass.
8.  `TETile WATER` - representation of water.
9.  `TETile FLOWER` - representation of a flower.
10. `TETile SAND` - representation of sand.
11. `TETile MOUNTAIN` - representation of a mountain.
12. `TETile TREE` - representation of a tree.


----------

**Engine (Core)**

The class which handles all backend interaction of the program, processing commands given to `Main` and performing actions based on said commands and interacting with in-program actions as well.

**Instance Variables**
1.  `int WIDTH` - the width of the world.
2.  `int HEIGHT` - the height of the world.


----------

**Main (Core)**

The class which handles all system interaction, from starting the UI and processing user-inputted commands. It is recommended that the `main` method has MINIMAL modifications as much as possible.


----------

**RandomUtils (Core)**

A set of utility methods (static) for handling pseudorandom generation.


----------

**World (Core)**

The class which handles the world generation algorithm.

**Instance Variables**
1. `Random rGen` - a random number generator.
2. `TETile[][] world` - the current state of the world.
3. `long seed` - the seed for the world.
4. `DoubleMapPQ<Hallway> hwMap` - a map of every connected hallway from the base room.
5. `TETile EMPTYTILE` - the tile type used for the world outside of rooms and hallways.
6. `TETile AVATAR` - the tile type used for the avatar.
7. `Point avatarLoc` - point representation of the avatar's location.
8. `boolean gameFinished` - flag to detect if the game has been finished.


----------

**DoubleMapPQ (Features)**

A map implementation in the form of a priority queue (PQ). The PQ is used to choose the order of hallways and rooms to build, during the world generation algorithm. (This class was sourced from CS61B's Proj2C)

**Instance Variables**
1. `TreeMap<Double, Set<T>> priorityToItem` - a map from each priority to its corresponding items (in case there are duplicates).
2. `HashMap<T, Double> itemToPriority` - a map from each item within the PQ to a priority.


----------

**Point (Features)**

A point representation within a 2D grid.

**Instance Variables**
1. `int x, y` - the x- and y- coordinates of a point.


----------

**Room (Features)**

An object representation of a room within a world.

**Instance Variables**
1. `ArrayList<Point> corners` - a list of this room's corner points.
2. `ArrayList<Edge> edges` - a list of this room's edges.
3. `int x, y` - the coordinates of this room's lower left corner.
4. `int width, height` - the width and height of this room.
5. `Point endPoint` - the end point of a hallway and the entrance point of this room.


----------

**Hallway (Features)**

An object representation of a hallway, i.e. the areas which connect rooms, within a world.

**Instance Variables**
1. `Point exitPoint` - the exit point of a room and the entrance point of the hallway.
2. `int length` - the full length of the hallway.
3. `Room source` - the room preceding this hallway.
4. `boolean horizontal` - a flag for if this hallway is horizontally oriented.
5. `boolean direction` - the forward direction of this hallway.
6. `Point endPoint` - the end point of this hallway and the entrance point of a room.


----------

**Edge (Features)**

An object representation of an edge, i.e. the walls of a room or hallway, within a world.

**Instance Variables**
1. `boolean horizontal` - a flag for if this edge is horizontally oriented.
2. `Point lowerBound` - the coordinate of the edge's bottom/left coordinate.
3. `Point upperBound` - the coordinate of the edge's top/right coordinate.
4. `boolean hallway` - a flag for if this edge has a hallway connected to it.


----------

**KeyboardInput (GUI)**

The class which helps process all keyboard inputs.


----------

**UI (GUI)**

The class which handles all interaction with the GUI.

**Instance Variables**
1. `Font menuFont, loadMenuFont, noteFont` - various fonts to use throughout the UI.


----------

**StartMenu (GUI)**

The class which handles all interaction with the start menu.

**Instance Variablaes**
1. `int MENU_WIDTH` - the width of the start menu.
2. `int MENU_HEIGHT` - the height of the start menu.


----------

**WorldUI (GUI)**

The class which handles all interaction with the world (after the start menu).

**Instance Variables**
1. `World state` - the current state of the world.
2. `TERenderer renderengine` - the rendering engine.
3. `String hudText` - a field for displaying text on the Heads-Up-Display (HUD).
4. `Persistence saves` - a field for keeping track of world saves states.


----------

**Persistence (Load)**

The class which handles saving and loading several states of various worlds.

**Instance Variables**
1. `File loadLog` - a text file which contains all load data.
2. `HashMap<Long, Load> loadMap` - a map from each unique seed to their load, if it exists.
3. `HashMap<Long, Integer> lineNumberMap` - a map from each unique seed to their line number within the load file, containing their save state information, if it exists.
4. `int numLoads` - the number of loads stored within the load file.
5. `ArrayList<Load>` - a list of all loads within the load file.
6. `HashMap<Long, String>` - a map from each unique seed to its save state string within the load file.


----------

**Load (Load)**

The class which stores the save state information which goes into each line of the load file.

**Instance Variables**
1. `long seed` - the seed of the save state's world.
2. `Point lastSeen` - the last point the avatar was at at the time of the world's save.
3. `String movement` - the sequence of moves the avatar took from the initial world generated from the seed.


----------

**Audio (Misc)**

The class which handles all audio input and output. (This class makes use of a manually inputted class from the Princton CS library, `StdAudio`).



## Algorithms
**TERenderer (TileEngine)**

**initialize**

The `initialize` method takes in a width `w` and height `h` and builds a (`w` * `h`) world to set tile objects within, by creating a window with such dimensions. The world is only built, NOT rendered.

**renderFrame**

The `renderFrame` method takes in a 2D array of `TETile` objects `world`, applies it to the initialized world, and renders it for the user to see. In order for this method to work, the world MUST have been initialized with the `initialize` method.


----------

**TETile (TileEngine)**

**toString**

The `toString` method takes in a 2D array of `TETile` objects `world` and prints the world in the console as if it was being displayed on the GUI.


----------

**Engine (Core)**

**startNewGame**

The `startNewGame` method starts a new game after a user starts a new game by pressing `N`, inputting a seed, and pressing `S`. Starting a new game initializes the user-input string `input` through the `menu` with the aforementioned sequence of keypresses, returning the world built from the seed.

**displayHUD**

The `displayHUD` method displays the HUD on the screen while no keypress source `src` is detected. If the user is not typing anything, then the HUD will be displayed on the `screen` depending on where the mouse is pointing at. 

**initializeLoad**

The `initializeLoad` method loads a saved game `chosenSave` after user chooses the game load option by pressing `L` and choosing a load. Loading a game initializes the user-input string `input` and loads in the previous movements into string `movementTracker`, based on the saved game, returning the seed found from the saved game.

**runNewGamePrompt**

The `runNewGamePrompt` method prints the "usual new game text" after printing the new world `world` on the console.

**runLoadGamePrompt**

The `runLoadGamePrompt` method prints the "usual load game text".

**printChar**

The `printChar` method prints a chararacter `c` on the console if it is detected as a valid keypress.

**checkIfAvatarMoved**

The `checkIfAvatarMoved` method checks if the avatar moved or hit a wall after the user inputs a move `c`. If it did move, the move is added to the string `movementTracker` and the move is displayed on the `screen`.

**saveState**

The `saveState` method saves the current state of the world on the `screen`, only after the `gameSarted` and the user has quit the game by pressing `:Q`. The save state is saved with the current movements from the `movementLog` string and into save slot `saveSlot`.

**endGame**

The `endGame` method ends the game by displaying a quick summary of the game on the console using string `movementTracker` and displaying the `menu` quit game screen, only after the user has quit the game by pressind `:Q`. This method also stops the ingame audio `song`.

**interactWithKeyboard**

The `interactWithKeyboard` method processes keyboard keypresses and draws the result of each said keypress. All valid keypresses are as follows: `N` (New Game), `L` (Load Game), `Q` (Quitting from the start menu), `0`-`9` (Seed inputting and save state loading), and `:Q` (Quitting and saving from within the game).

**checkInputForErrors**

The `checkInputForErrors` method checks if the `input` stringloaded into the `interactWithInputString` method had a valid format if not.

**determineSeed**

The `determineSeed` method returns the correct seed depending on what the user inputs through `input`. If the user starts a new game, through a flag `newworld`, then the seed is `initialVal`. If not, then the correct seed will be found in the game's `saves`.

**interactWithInputString**

The `interactionWithInputString` method takes keyboard inputs described within the `input` string and returns a world, in the form of a `TETile[][]` object, which represents the world after said inputs have been processed to the world current world.


----------

**RandomUtils (Core)**

**uniform**

Using a random number generator `r`, this `uniform` method returns a random `double` between [0, 1).

**uniform**

Using a random number generator `r`, this `uniform` method returns a random `int` between [0, `n`). Recall that Java's integer bounds are:
[-2147483648, 2147483647].

**uniform**

Using a random number generator `r`, this `uniform` method returns a random `long` between [0, `n`). Recall that Java's long integer bounds 
[-9223372036854775808, 9223372036854775807].

**uniform**

Using a random number generator `r`, this `uniform` method returns a random `int` between [`a`, `b`).

**bernoulli(Random r, double p)**

Using a random number generator `r`, this `bernoulli` method returns returns a random boolean from a Bernoulli Distribution with success probability `p`.


----------

**World (Core)**

**World**

The constructor generates a world using the given seed `s`, random number generator `r`, and location of the avatar `al`, if given. The first room which is generated is called the "base room", and all other hallways and rooms are built off of that room.

**addRoom**

The `addRoom` method builds a room `newRoom` in the world.

**generateHallways**

The `generateHallways` method determines how many hallways (1-4) to build, stemming from room `srcRoom`.

**addHallway**

The `addHallway` method builds a hallway `hw` in the world.

**randomizedWidthHeight**

The `randomizedWidthHeight` method calculates a random width or height for a room depending on some point `origin` and a given boundary `bound`.

**generateRooms**

The `generateRooms` method determines whether to build a room at the end of hallway `hw`, depending on its location and the priority value `srcPriority` given from the room preceding it. If a room is planned to be built, a room will be generated with random properties to see if it could be attached to the hallway until there are no `attemptsLeft`.

**getWorld**

The `getWorld` method returns the current state of the world.

**spaceIsOpen**

The `spaceIsOpen` method checks if tile at point (`x`, `y`) is a floor tile or not.

**wallAccessible**

The `wallAccessible` method checks if a door could be set on a wall, if the `doorIsUnset` at a given coordinate (`x`, `y`).

**generateEntities**

The `generateEntities` method sets the avatar and unlocked door randomly within the world.

**displayOnConsole**

The `displayOnConsole` method displays the current state of the world on the console.

**getTile**

The `getTile` method returns the tile at point (`x`, `y`).

**setTile**

This `setTile` method changes the tile at point (`x`, `y`) to tile `tile`.

**setTile**

This `setTile` method changes the tile at point `p` to tile `tile`.

**getAvatarPosition**

The `getAvatarPosition` method returns the avatar's location.

**moveAvatar**

The `moveAvatar` method moves the avatar to point (`x`, `y`). If the target point is the unlocked door, then the game is said to be completed.

**moveAvatarIfPossible**

The `moveAvatarIfPossible` method moves the avatar to point (`x`, `y`) if the tile at that point is not a wall.

**moveAvatar**

The `moveAvatar` method moves the avatar in the direction given by the keypress `c`.

**getSeed**

The `getSeed` method returns the seed of the world.

**exitFound**

The `exitFound` method checks if the game has been completed, i.e. the unlocked door (exit) has been found.

**quitSave**

The `quitSave` method saves the current world's state into save slot `saveSlot`, which also contains the moves up until that point `input`.


----------

**DoubleMapPQ (Features)**

**getItem**

The `getItem` method returns the item at `s`.

**add**

The `add` method inserts the item `item` into the PQ with priority `priority`.

**contains**

The `contains` method checks if the item `item` is currently in the PQ.

**getSmallest**

Th `getSmallest` method returns the smallest item in the PQ.

**removeSmallest**

The `removeSmallest` method deletes the smallest item in the PQ and returns it.

**changePriority**

The `changePriority` method changes the priority of the node within the PQ with item `item` to new priority `priority`.

**size**

The `size` method returns the size of the PQ.


----------

**Point (Features)**

**Point**

The constructor initializes the instance variables of the point: the x-coordinate to `xCoord` and the y-coordinate to `yCoord`.

**equals**

This overriden `equals` method simply checks if one point's (`o`) x- and y- coordinates are equal to this point's.

**hashCode**

This overriden `hashCode` method returns the superclass's hash code.

**toString**

This overriden `toString` method prints the string representation of this point as "(`x`, `y`)".

**getX**

The `getX` method returns this point's x-coordinate.

**getY**

The `getY` method returns this point's y-coordinate.

**distanceFrom**

The `distanceFrom` method finds the Euclidean distance between this point and another point `other`.

**nearEdge**

The `nearEdge` method checks if this point is near the world's width `worldWidth` and height `worldHeight`.


----------

**Room (Features)**

**Room**

The constructor initializes the room with lower left corner at (`xCoord`, `yCoord`), width `w`, height `h`, whether it `hasEntrance` hallway, the point of the end of that hallway `end` if it exists, and the "distance" from the base room `p` with respect to the PQ priorities.

**addHallwayToRoom**

The `addHallwayToRoom` method adds a hallway to a room if the entrance point `enterPoint` overlaps with one of this room's edges.

**getCorners**

The `getCorners` method returns the corners of this room.

**getEdges**

The `getEdges` method returns the edges of this room.

**getPriority**

The `getPriority` method returns this room's "distance" from the base room.

**getEnd**

The `getEnd` priority returns this room end point.

**getX**

The `getX` method returns the x-coordinate of this room's lower left corner.

**getY**

The `getY` method returns the y-coordinate of this room's lower left corner.

**getWidth**

The `getWidth` method returns the width of this room.

**getHeight**

The `getHeight` method returns the height of this room.

**onEdge**

The `onEdge` method checks if the point at (`xCoord`, `yCoord`) overlaps with one of this room's edges.


----------

**Hallway (Features)**

**Hallway**

The constructor initializes the hallway with exit point `exit`, length `len`, source room `src`, flag `hztl` to tell whether the hallway is horizontally oriented or not, and direction flag `dir` to tell whether the "forward" direction of the hallway is towards positive x-/y-values or towards negative values.

**getEnd**

The `getEnd` method returns the end point of this hallway.

**getExit**

The `getExit` method returns the exit point of this hallway.

**getSource**

The `getSource` method returns the room which precedes this hallway.

**isHorizontal**

The `isHorizontal` method checks if this hallway is horizontally oriented or not.

**getLength**

The `getLength` method returns the length of this hallway.

**increasingDirection**

The `increasingDirection` method checks if this hallway's "forward" direction is positive or negative.

**calculateEnd**

The `calculateEnd` method calculates the end point of this hallway and returns it.


----------

**Edge (Features)**

**Edge**

The constructor initializes the edge with flag `hztl` to tell whether the edge is horizontally oriented or not, lower bound `lb`, upper bound `ub`, and flag `hh` to tell whether the edge has a hallway going into it.

**isHorizontal**

The `isHorizontal` method checks if this edge is horizontally oriented or not.

**getLowerBound**

The `getLowerBound` method returns the lower bound of this edge.

**getUpperBound**

The `getUpperBound` method returns the upper bound of this edge.

**hasHallway**

The `hasHallway` method checks if this edge has a hallway connected to it or not.

**addHallwayToEdge**

The `addHallwayToEdge` method changes the state of the edge to have a hallway.

**contains**

The `contains` method checks if a point `pt` is on this edge.

**nearBoundary**

The `nearBoundary` method checks if an edge is near the boundary given by width `worldWidth` and height `worldHeight`.


----------

**KeyboardInput (GUI)**

**getNextKey**

The `getNextKey` method retrieves the next character keypress made by the user and returns it.

**noKeyTypedYet**

Checks if the character keypress has been made by the user or not.

**possibleNextInput**

Checks if there could be a next character keystroke which could be made by the user.


----------

**UI (GUI)**

**UI**

The constructor initializes menu font, note font, and load menu font.

**clearWindow**

The `clearWindow` method clears the screen with a black background.

**setWindowSize**

The `setWindowSize` method changes the window size to one with width `w` and height `h`.

**showUpdate**

The `showUpdate` method shows the drawings made on the off-screen canvas. (This method is only needed if "double buffering" is made through the screen drawing class StdDraw but is incidentally enabled through TERenderer).

**display**

The `display` method displays a string `text` centered at point (`x`, `y`) on the screen.

**getMenuFont**

The `getMenuFont` method returns the main menu font.

**getLoadMenuFont**

The `getLoadMenuFont` method returns the load menu font.

**getNoteFont**

The `getNoteFont` method returns the note font.

**setFont**

The `setFont` method sets the current text displaying font to font `f`.

**setColor**

The `setColor` method sets the current screen draw color to color `newColor`.

**mouseMoved**

The `mouseMoved` method checks if the mouse has moved away from point (`lastX`, `lastY`) on the screen.

**mousePosition**

The `mousePosition` method returns the current coordinate the mouse pointer is pointing at.

**delayNextScreen**

The `delayNextScreen` method delays any screen updates for `t` milliseconds.


----------

**StartMenu (GUI)**

**StartMenu**

The constructor displays the start menu.

**printBackTip**

The `printBackTip` method prints the tip noting a "back button" exists.

**printTip**

The `printTip` method displays the program tip during the seed input screen.

**updateSeedDisplay**

The `updateSeedDisplay` method updates the seed display on the seed input screen, for every time the user inputs a new number for their wanted seed `s`.

**askForSeed**

The `askForSeed` method launches the seed input screen after choosing the new game option by pressing `N`.

**chooseLoad**

The `chooseLoad` method launches the load save screen after choosing the load game option by pressing 'L'.

**replayWanted**

The `replayWanted` method launches the replay option screen after choosing a load from the load save screen.

**quitGame**

The `quitGame` method displays the game quit screen after the user presses `:Q` during a game.


----------

**WorldUI (GUI)**

**WorldUI**

The constructor displays the world `world`, using the rendering engine `ter`, on the screen, with width `w` and height `h`.

**updateWorld**

The `updateWorld` method displays the world and HUD on the screen.

**replay**

The `replay` method displays a replay of a load's previous moves on the screen, if the option is chosen to do so after loading a save.

**updateHUD**

The `updateHUD` method updates the HUD text based on if the tile the user's mouse is pointing at has changed since the previous tile they pointed at, `lastTile`.

**gameCompleted**

The `gameCompleted` method checks if a game has been completed. If it has, the game completion screen will be displayed.


----------

**Persistence (Load)**

**Persistence**

The constructor finds the load file path and instantiates instance variables based on what it finds in the load file.

**createLoadLog**

The `createLoadLog` method helps the constructor parse through the load file `load` and update instance variables based on the information it finds.

**loadExists**

The `loadExists` method checks if a load exists for seed `seed`.

**getLoad**

The `getLoad` method returns the load instance with seed `seed`, if it exists.

**addNewSaveLine**

The `addNewSaveLine` method adds a new save state to the load file with seed `seed`, last avatar location at point (`x`, `y`), and last sequence of movements `input`.

**updateSave**

The `updateSave` method updates a save with seed `seed`, last avatar location at point (`newX`, `newY`), and last sequence of movements `input`.

**addSave**

The `addSave` method updates a save; with seed `seed`, last avatar location at point (`x`, `y`), load file number line `lineToChange`, and last sequence of movments `input`.

**getLoadList**

The `getLoadList` method returns the list of loads.

**logSave**

The `logSave` method updates the instance variables to reflect an updated save state with new load `save`, load file number line `numberLine`, seed `seed`, and last sequence of movements `input`.

**getNumLoads**

The `getNumLoads` method returns the number of loads within the current load file.

**hasSaveForSeed**

The `hasSaveForSeed` method checks if a save exists for a given seed `seed`.

**getLastSeen**

The `getLastSeen` method returns the last location of the avatar according to seed `seed`.

**getInput**

The `getInput` method returns the whole load string saved in one save state according to seed `seed`.


----------

**Load (Load)**

**Load**

The constructor initializes the load with the information formatted into one save: the seed of the world `sd`, the last position of the avatar at point (`x`, `y`), and the sequence of moves taken before saving `input`.

**getSeed**

The `getSeed` method returns the seed of the save's world.

**getLastSeen**

The `getLastSeen` method returns the point the avatar was left at at the end of the save's world.

**getPreviousMovements**

The `getPreviousMovements` method returns the sequence of moves taken during the save.

**equals**

This overriden `equals` method simply checks if one load object has the same seed and last avatar location as another object `o`.

**hashCode**

This overriden `hashCode` method returns the superclass's hash code.


----------

Audio

**playMusic**

The `playMusic` method plays the in-game song.

**playMenuSFX**

The `playMenuSFX` method plays the menu sound effect.

**stopMusic**

The `stopMusic` method stops all in-game audio.



## Persistence
The project comes with a preloaded load file called `load.txt` located in `proj3/byow/Core/Load/load.txt`. This file will be used as the file for saving and loading. The load file contains five save slots: Q (default), slot 1, slot 2, slot 3, and slot 4. All methods which are about to be mentioned are all in the `Persistence` class.

Due to the slots, the user has 5 ways to quit/save the game; they can end the game by inputting any of the following commands, corresponding to their respective save slot: ":Q", ":1", ":2", ":3", ":4". If a save slot has already been used, it will be updated with the `updateSave` method; otherwise the current save will be stored in the chosen save slot with the `addSave` method and will decrease to the least save slot not being used, if one exists.

Also by this nature, when the user gets to the load screen after pressing `L` from the start menu, they have a choice between multiple save slots, if they exist from past games. The screen will use the `getLoadList` method to retrieve the currently used save slots for displaying the slots to choose and will load based on the user-input.

(Additionally) After loading, users have the option to REPLAY their load's previous moves before continuing from the save slot's current state.
