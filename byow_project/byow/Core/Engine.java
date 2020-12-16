package byow.Core;

import java.util.NoSuchElementException;
import java.util.Random;

import byow.Core.Features.Point;
import byow.Core.GUI.*;
import byow.Core.Load.*;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.Core.Misc.Audio;

/**
 * @author  Wil Aquino, Liam Howell
 * Date:    November 17, 2020
 * Project: Build Your Own World (BYOW, Proj 3)
 * Module:  Engine.java
 * Purpose: Handles all backend interaction of BYOW.
 *
 * @source  CS61B Staff, created starter code for both
 *          interactWithKeyboard() methods.
 */
public class Engine {

    /** Rendering engine object representation. */
    TERenderer ter = new TERenderer();

    /** Width of the world. */
    public static final int WIDTH = 80;

    /** Height of the world. */
    public static final int HEIGHT = 30;

    /** Last mouse position, for HUD usage. */
    private Point lastMousePos;

    /** Last tile displayed on the HUD. */
    private TETile lastHUDTile;

    /**
     * Starts a new game.
     * @param input the keypress recorder.
     * @param menu the start menu.
     * @return the world created by the game.
     */
    private World startNewGame(StringBuilder input, StartMenu menu) {
        input.append('N');
        long seed = menu.askForSeed();
        if (seed == -1) {
            return null;
        }
        input.append(seed);
        input.append('S');

        World newWorld = new World(seed, new Random(seed), null);
        Audio.playMusic();
        return newWorld;
    }

    /**
     * Displays the HUD on the screen.
     * @param src the keypress recorder.
     * @param screen the in-game screen.
     */
    private void displayHUD(KeyboardInput src, WorldUI screen) {
        while (src.noKeyTypedYet() && screen != null) {
            int lastHUDX = lastMousePos.getX();
            int lastHUDY = lastMousePos.getY();

            if (screen.mouseMoved(lastHUDX, lastHUDY)) {
                lastMousePos = screen.mousePosition();
                lastHUDTile = screen.updateHUD(lastHUDTile);
            }
        }
    }

    /**
     * Loads a save from a given save state.
     * @param chosenSave the chosen save state.
     * @param input the keypress recorder.
     * @param movementTracker the movement recorder.
     * @return the seed associated with the save state.
     */
    private long initializeLoad(Load chosenSave, StringBuilder input,
                                StringBuilder movementTracker) {
        long seed = chosenSave.getSeed();
        System.out.println(seed);
        input.append(seed);

        String previousMovement = chosenSave.getPreviousMovements();
        movementTracker.append(previousMovement);

        Audio.playMusic();
        return seed;
    }

    /**
     * Print the new game prompt.
     * @param world the current state of the world.
     */
    private void runNewGamePrompt(World world) {
        System.out.println("NEW GAME START SUCCESSFUL.");
        System.out.println("Seed: " + world.getSeed());
        System.out.print("Inputs: " + 'N' + world.getSeed() + 'S');
    }

    /** Print the load game prompt. */
    private void runLoadGamePrompt() {
        System.out.println("LOAD SUCCESSFUL.");
        System.out.print("Inputs: " + 'L');
    }

    /**
     * If an inputted char is a valid input within the game,
     * print it on the console.
     * @param c the inputted char.
     */
    private void printChar(char c) {
        if (c == 'W' || c == 'A' || c == 'S' || c == 'D'
            || c == ':' || c == 'Q' || (c >= 49 && c <= 52)) {
            System.out.print(c);
        }
    }

    /**
     * Checks if the avatar moved or not.
     * @param c the current movement input.
     * @param movementTracker the movement recorder.
     * @param world the world the avatar's in
     * @param hasUI a flag for checking if the world UI has started already.
     * @param screen the in-game screen.
     */
    private void checkIfAvatarMoved(char c, StringBuilder movementTracker,
                                    World world, boolean hasUI,
                                    WorldUI screen) {
        if (world.moveAvatar(c)) {
            movementTracker.append(c);
            if (hasUI) {
                screen.updateWorld();
            }
        }
    }

    /**
     * Save the current state of the world if possible.
     * @param world the world instance.
     * @param gameStarted flag for if the game has started or not.
     * @param movementLog the recorded movements since the last load.
     * @param saveSlot the slot the user wants to save the state in.
     */
    private void saveState(World world, boolean gameStarted,
                           String movementLog, int saveSlot) {
        if (gameStarted) {
            world.quitSave(saveSlot, movementLog);
        }
    }

    /**
     * Ends the game.
     * @param movementTracker the movement recorder.
     * @param menu the start menu, for quitting purposes.
     */
    private void endGame(StringBuilder movementTracker, StartMenu menu) {
        System.out.println("\n\nGAME HAS TERMINATED.");
        System.out.println("Movement Summary: " + movementTracker);
        Audio.stopMusic();
        menu.quitGame();
    }

    /**
     * Method used for exploring a fresh world. This method should
     * handle all inputs, including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        KeyboardInput src = new KeyboardInput();
        StartMenu menu = new StartMenu();
        WorldUI screen = null;
        World world = null;
        StringBuilder input = new StringBuilder();

        StringBuilder movementTracker = new StringBuilder();
        long seed;
        boolean lastKeyWasColon = false;
        boolean gameStarted = false;

        lastMousePos = new Point(-1, -1);
        lastHUDTile = null;

        while (src.possibleNextInput()) {
            displayHUD(src, screen);
            char c = src.getNextKey();

            if (c == 'N' && !gameStarted) {
                Audio.playMenuSFX();
                System.out.print(c);
                world = startNewGame(input, menu);
                if (world == null) {
                    continue;
                }
                screen = new WorldUI(world, ter, WIDTH, HEIGHT);

                runNewGamePrompt(world);
                gameStarted = true;
                continue;
            }

            input.append(c);
            if (c == 'L' && !gameStarted) {
                Audio.playMenuSFX();
                System.out.print(c);
                Load slot = menu.chooseLoad();
                if (slot == null) {
                    continue;
                }
                seed = initializeLoad(slot, input, movementTracker);

                if (menu.replayWanted()) {
                    world = new World(seed, new Random(seed), null);
                    screen = new WorldUI(world, ter, WIDTH, HEIGHT);
                    screen.replay(seed);
                } else {
                    world = new World(seed, new Random(seed),
                            slot.getLastSeen());
                    screen = new WorldUI(world, ter, WIDTH, HEIGHT);
                }

                runLoadGamePrompt();
                gameStarted = true;
                continue;
            }

            printChar(c);
            if ((c == 'W' || c == 'A' || c == 'S' || c == 'D')
                && screen != null) {
                lastKeyWasColon = false;
                checkIfAvatarMoved(c, movementTracker, world, true, screen);
                if (screen.gameCompleted()) {
                    break;
                }
            } else if (c == ':') {
                lastKeyWasColon = true;
            } else if (c == 'Q' && (!gameStarted || lastKeyWasColon)) {
                saveState(world, gameStarted, movementTracker.toString(), 0);
                endGame(movementTracker, menu);
                break;
            } else if (c > '0' && c < '5' && lastKeyWasColon) {
                saveState(world, gameStarted,
                        movementTracker.toString(), (int) c - 48);
                endGame(movementTracker, menu);
                break;
            }
        }
    }

    /**
     * Checks if the input string has any errors for
     * interactWithInputString().
     * @param input the input string.
     */
    private void checkInputForErrors(String input) {
        int len = input.length();
        if (len < 1) {
            throw new IllegalArgumentException(
                    "Invalid input of length 1 detected.");
        } else if (input.equals("L")) {
            return;
        }

        char firstChar = input.charAt(0);
        char lastChar = input.charAt(input.length() - 1);
        String lastTwoChars = input.substring(input.length() - 2);

        boolean firstCharNotGood = firstChar != 'N' &&  firstChar != 'L';

        boolean lastCharsNotGood = lastChar != 'W' && lastChar != 'A'
                && lastChar != 'S' && lastChar != 'D' && lastChar != 'L'
                && !lastTwoChars.equals(":Q") && !lastTwoChars.equals(":1")
                && !lastTwoChars.equals(":2") && !lastTwoChars.equals(":3") 
                && !lastTwoChars.equals(":4");

        if (firstCharNotGood) {
            throw new IllegalArgumentException(
                    "Bad first character input detected.");
        } else if (lastCharsNotGood) {
            throw new IllegalArgumentException(
                    "Bad last character(s) input detected.");
        }
    }

    /**
     * Determines the seed of a given input.
     * @param initialVal a random seed.
     * @param input the seed builder.
     * @param newWorld a flag do determine if a new world was selected.
     * @param saves a persistence object for save loading.
     * @return the determined seed.
     */
    private long determineSeed(long initialVal, StringBuilder input,
                               boolean newWorld, Persistence saves) {
        if (input.length() == 0) {
            if (newWorld) {
                return initialVal;
            } else {
                if (saves.getLoadList() == null) {
                    throw new NoSuchElementException("No saved games found.");
                }
                return saves.getLoadList().get(0).getSeed();
            }
        }

        return Long.parseLong(input.toString());
    }

    /**
     * Prints the world on the console and returns it.
     * @param world the world to print.
     * @return the retrieved world.
     */
    private TETile[][] finalWorld(World world) {
        TETile[][] finalWorldFrame = world.getWorld();
        System.out.println(TETile.toString(finalWorldFrame));
        System.out.println("INPUT LOAD WAS SUCCESSFUL.");
        return finalWorldFrame;
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program.
     * @return the 2D TETile[][] representing the state of the world.
     */
    public TETile[][] interactWithInputString(String input) {
        input = input.toUpperCase();
        checkInputForErrors(input);
        System.out.println("\n\n-------------------------------------------");
        System.out.println("LOADING INPUT: " + input);

        StringBuilder stringSeed = new StringBuilder();
        int firstMove = 1;
        long seed = RandomUtils.uniform(new Random(), Long.MAX_VALUE);
        boolean newWorld = true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (i == 0) {
                newWorld = (c == 'N');
            } else {
                if (c < '0' || c > '9') {
                    firstMove = i;
                    break;
                }
                stringSeed.append(c);
            }
        }

        World world;
        Persistence saves = new Persistence();
        seed = determineSeed(seed, stringSeed, newWorld, saves);

        if (newWorld) {
            seed = Long.parseLong(stringSeed.toString());
            world = new World(seed, new Random(seed), null);
            firstMove += 1;
        } else {
            world = new World(seed, new Random(seed), saves.getLastSeen(seed));
        }

        boolean lastKeyWasColon = false;
        StringBuilder movementTracker = new StringBuilder();

        for (int i = firstMove; i < input.length(); i++) {
            char c = input.charAt(i);

            if (c == 'W' || c == 'A' || c == 'S' || c == 'D') {
                checkIfAvatarMoved(c, movementTracker, world, false, null);
                if (world.exitFound()) {
                    System.out.println("EXIT WAS FOUND AND GAME WAS COMPLETED.");
                    break;
                }
            } else if (c == ':') {
                lastKeyWasColon = true;
            } else if (c == 'Q' && lastKeyWasColon) {
                saveState(world, true, movementTracker.toString(), 0);
                break;
            } else if (c > '0' && c < '5' && lastKeyWasColon) {
                saveState(world, true,
                        movementTracker.toString(), (int) c - 48);
                break;
            }
        }

        return finalWorld(world);
    }

    /**
     * Main method for testing Engine implementation.
     * @param args command line arguments.
     */
    public static void main(String[] args) {
        Engine eng = new Engine();
        long rand = 33;

        for (int i = 0; i < 100; i++) {
            rand = RandomUtils.uniform(new Random(), Long.MAX_VALUE);
            TETile[][] world = eng.interactWithInputString(
                    "N" + rand + "Swasdsasds");

            TERenderer render = new TERenderer();
            render.initialize(Engine.WIDTH, Engine.HEIGHT);
            render.renderFrame(world);
            System.out.println("\n" + i + "\n");
        }

        eng.interactWithInputString("N" + rand + "SSDDDWDDS");
    }
}
