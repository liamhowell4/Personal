package byow.Core.GUI;

import byow.Core.Engine;
import byow.Core.Load.*;
import byow.Core.Misc.Audio;
import byow.Core.RandomUtils;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author  Wil Aquino, Liam Howell
 * Date:    November 18, 2020
 * Project: Build Your Own World (BYOW, Proj 3)
 * Module:  StartMenu.java
 * Purpose: Handles all interaction with the start menu.
 */
public class StartMenu extends UI {

    /** Start menu's width. */
    public static final int MENU_WIDTH = 100;

    /** Start menu's height. */
    public static final int MENU_HEIGHT = 100;

    /** Initializes the start menu. */
    public StartMenu() {
        super();
        clearWindow();
        setWindowSize(MENU_WIDTH, MENU_HEIGHT);
        setColor(Color.ORANGE);

        Font titleFont = new Font("title", Font.PLAIN, MENU_HEIGHT / 2);

        setFont(titleFont);
        display(MENU_WIDTH / 2,
                (MENU_HEIGHT / 2) + 15, "CS61B: THE GAME");

        setFont(getMenuFont());
        display(MENU_WIDTH / 2,
                (MENU_HEIGHT / 2) - 15, "New Game (N)");
        display(MENU_WIDTH / 2,
                (MENU_HEIGHT / 2) - 20, "Load Game (L)");
        display(MENU_WIDTH / 2,
                (MENU_HEIGHT / 2) - 25, "Quit (Q)");
    }

    /** Print the "back" button tip at the top of the new/loa game screen. */
    private void printBackTip() {
        setFont(getMenuFont());
        setColor(Color.WHITE);
        display(20, MENU_HEIGHT - 5, "Press 'B' to go back!");
        setColor(Color.ORANGE);
    }

    /** Print the tip at the bottom of the "New Game" screen. */
    private void printTip() {
        setFont(getNoteFont());
        display(MENU_WIDTH / 2, (MENU_HEIGHT / 2) - 24, "Did you know?:");
        display(MENU_WIDTH / 2, (MENU_HEIGHT / 2) - 28,
                "To quit and save to the default slot, enter :Q. To save to "
                        + "any other");
        display(MENU_WIDTH / 2, (MENU_HEIGHT / 2) - 32,
                "slot (1-4), quit by typing : then the NUMBER slot to which "
                        + "you want to save!");
        display(MENU_WIDTH / 2, (MENU_HEIGHT / 2) - 36,
                "If there is already a save for a seed, any save option will "
                        + "automatically");
        display(MENU_WIDTH / 2, (MENU_HEIGHT / 2) - 40,
                "overwrite the existing save for that seed, i.e. not saved to "
                        + "your chosen slot!");
    }

    /**
     * Updates the seed input interface.
     * @param s the user-inputted seed.
     */
    private void updateSeedDisplay(String s) {
        clearWindow();
        printBackTip();

        display(MENU_WIDTH / 2, (MENU_HEIGHT / 2) + 15, "Enter Seed.");
        display(MENU_WIDTH / 2, (MENU_HEIGHT / 2) - 10,
                "Press 'S' when finished seeding.");
        setColor(Color.WHITE);
        display(MENU_WIDTH / 2, (MENU_HEIGHT / 2), s);
        setColor(Color.ORANGE);

        printTip();
    }

    /**
     * Launches seed input interface and retrieves the seed
     * through user-inputted numbers, during the "New Game" screen.
     * @return the user-inputted seed.
     */
    public long askForSeed() {
        clearWindow();
        printBackTip();

        display(MENU_WIDTH / 2, (MENU_HEIGHT / 2) + 15, "Enter Seed.");
        display(MENU_WIDTH / 2, (MENU_HEIGHT / 2) - 10,
                "Press 'S' when finished seeding.");

        printTip();
        setColor(Color.WHITE);

        KeyboardInput src = new KeyboardInput();
        StringBuilder input = new StringBuilder();
        long seed = RandomUtils.uniform(new Random(), Long.MAX_VALUE);

        while (src.possibleNextInput()) {
            char c = src.getNextKey();
            if (c == 'B') {
                Audio.playMenuSFX();
                Engine en = new Engine();
                en.interactWithKeyboard();
                return -1;
            } else if (c == 'S') {
                break;
            } else if (c < '0' || c > '9') {
                continue;
            }

            input.append(c);

            try {
                seed = Long.parseLong(input.toString());
                if (input.toString().length() > MENU_WIDTH / 2) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                setColor(Color.ORANGE);
                input.deleteCharAt(input.length() - 1);
                display(MENU_WIDTH / 2, (MENU_HEIGHT / 2) - 30,
                        "Max seed entered. Loading game...");
                delayNextScreen(2000);
                break;
            }

            System.out.print(c);
            updateSeedDisplay(input.toString());
        }

        System.out.println();
        return seed;
    }

    /**
     * Launches load choosing interface and loads a save based
     * based on the user-input slot, during the "Load Game" screen.
     * @return the chose save slot to load, if it exists.
     */
    public Load chooseLoad() {
        clearWindow();

        Persistence saves = new Persistence();
        ArrayList<Load> savesListed = saves.getLoadList();
        Load chosenSave;

        printBackTip();

        setFont(getLoadMenuFont());
        display(MENU_WIDTH / 2,
                (MENU_HEIGHT / 2) + 15, "CS61B: THE GAME: THE SAVES");

        setFont(getMenuFont());
        display(MENU_WIDTH / 2,
                (MENU_HEIGHT / 2) + 8, "Press a (number) to select a save "
                        + "state to load.");

        String printLine = "___________________________________________";
        display(MENU_WIDTH / 2, (MENU_HEIGHT / 2) + 3, printLine);

        if (saves.getNumLoads() == 0) {
            display(MENU_WIDTH / 2,
                    (MENU_HEIGHT / 2) - 5, "No Saved States! Press (B) to "
                            + "return to the main menu");
        } else {
            int numSaves = savesListed.size();
            for (int i = 0; i < numSaves; i += 1) {
                display(MENU_WIDTH / 2,
                        (MENU_HEIGHT / 2) - (5 + 7 * i), "(" + i + ") Seed: "
                                + savesListed.get(i).getSeed());
            }
        }

        KeyboardInput src = new KeyboardInput();
        while (src.possibleNextInput()) {
            char c = src.getNextKey();
            if (c == 'B') {
                Audio.playMenuSFX();
                Engine en = new Engine();
                en.interactWithKeyboard();
                return null;
            }
            if (c < '0' || c >= (char) (saves.getNumLoads() + 48)) {
                continue;
            }
            chosenSave = savesListed.get((int) c - 48);
            return chosenSave;
        }
        return null;
    }

    /**
     * Checks if the user would like to see a replay of a
     * load's previous movements.
     * @return True if they want to see the replay.
     *         False if they do not.
     */
    public boolean replayWanted() {
        clearWindow();

        setFont(getMenuFont());
        display(MENU_WIDTH / 2,
                (MENU_HEIGHT / 2) + 2, "Would you like a replay of the moves "
                        + "made");
        display(MENU_WIDTH / 2,
                (MENU_HEIGHT / 2) - 3, "during the last game before "
                        + "playing? (Y/N)");

        KeyboardInput src = new KeyboardInput();
        while (src.possibleNextInput()) {
            char c = src.getNextKey();
            if (c == 'Y') {
                return true;
            } else if (c == 'N') {
                return false;
            }
        }

        return false;
    }

    /** Quits the game. */
    public void quitGame() {
        setColor(Color.ORANGE);
        setFont(getMenuFont());
        clearWindow();
        setWindowSize(MENU_WIDTH, MENU_HEIGHT);

        display(MENU_WIDTH / 2, (MENU_HEIGHT / 2) + 5,
                "You have quit the game.");
        display(MENU_WIDTH / 2, (MENU_HEIGHT / 2) - 5,
                "Thanks for playing!");
        delayNextScreen(2000);
        setFont(getNoteFont());
        display(MENU_WIDTH / 2, (MENU_HEIGHT / 2) - 20,
                "(Close the window to exit the program)");
    }
}
