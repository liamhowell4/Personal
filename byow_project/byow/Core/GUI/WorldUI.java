package byow.Core.GUI;

import byow.Core.Engine;
import byow.Core.Load.Persistence;
import byow.Core.World;
import byow.TileEngine.*;

import java.awt.*;

/**
 * @author  Wil Aquino, Liam Howell
 * Date:    November 23, 2020
 * Project: Build Your Own World (BYOW, Proj 3)
 * Module:  WorldUI.java
 * Purpose: Handles all interaction with the world.
 */
public class WorldUI extends UI {

    /** Field for the current state of the world. */
    private final World state;

    /** Rendering engine. */
    private TERenderer renderEngine;

    /** Field for the HUD text. */
    private String hudText;

    /** Field for keeping track of world save states. */
    private final Persistence saves;

    /**
     * Initializes the world user interface.
     * @param world the initial state of the world.
     * @param ter the provided rendering engine.
     * @param w the width of the UI.
     * @param h the height of the UI.
     */
    public WorldUI(World world, TERenderer ter, int w, int h) {
        state = world;
        renderEngine = ter;
        hudText = "";
        saves = new Persistence();

        renderEngine.initialize(w, h);
        setColor(Color.WHITE);
        updateWorld();
        world.displayOnConsole();
    }

    /** Renders the current state of the world. */
    public void updateWorld() {
        renderEngine.renderFrame(state.getWorld());
        setColor(Color.WHITE);
        display(3, Engine.HEIGHT - 2, hudText);
    }

    /**
     * Simulates the movements from the past load.
     * @param seed the seed to begin the replay on.
     */
    public void replay(long seed) {
        String input = saves.getInput(seed);

        if (input == null) {
            return;
        }

        try {
            char[] prevMoves = input.toCharArray();
            for (char direction : prevMoves) {
                state.moveAvatar(direction);
                updateWorld();
                delayNextScreen(500);
            }
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    /**
     * Updates the HUD through mouse movement.
     * @param lastTile the last tile found on the HUD.
     * @return the tile the mouse is currently pointing at.
     */
    public TETile updateHUD(TETile lastTile) {
        int ptrX = mousePosition().getX();
        int ptrY = mousePosition().getY();
        TETile tile = state.getTile(ptrX, ptrY);

        if (tile.equals(lastTile)) {
            return tile;
        }

        clearWindow();
        if (tile.equals(World.EMPTYTILE)) {
            hudText = "water";
        } else if (tile.equals(Tileset.WALL)) {
            hudText = "wall";
        } else if (tile.equals(Tileset.FLOOR)) {
            hudText = "floor";
        } else if (tile.equals(World.AVATAR)) {
            hudText = "player";
        } else if (tile.equals(Tileset.UNLOCKED_DOOR)) {
            hudText = "exit";
        }

        renderEngine = new TERenderer();
        updateWorld();

        return tile;
    }

    /**
     * Checks if the game was completed by the user.
     * @return True if the game was completed.
     *         False if it was not.
     */
    public boolean gameCompleted() {
        if (!state.exitFound()) {
            return false;
        }

        setColor(Color.ORANGE);
        setFont(getMenuFont());
        clearWindow();
        setWindowSize(StartMenu.MENU_WIDTH, StartMenu.MENU_HEIGHT);

        display(StartMenu.MENU_WIDTH / 2, (StartMenu.MENU_HEIGHT / 2) + 5,
                "You have escaped the sea turtle!");
        display(StartMenu.MENU_WIDTH / 2, (StartMenu.MENU_HEIGHT / 2) - 5,
                "Thanks for playing!");
        delayNextScreen(2000);
        setFont(getNoteFont());
        display(StartMenu.MENU_WIDTH / 2, (StartMenu.MENU_HEIGHT / 2) - 20,
                "(Close the window to exit the program)");

        System.out.println("\nEXIT WAS FOUND AND GAME WAS COMPLETED.");
        return true;
    }
}
