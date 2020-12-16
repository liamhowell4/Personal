package byow.lab12;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * @author  Wil Aquino, Liam Howell
 * Date:    November 12, 2020
 * Project: Getting Started on Project 3 (Lab 12)
 * Module:  HexWorld.java
 * Purpose: Draws a world consisting of hexagonal regions.
 */
public class HexWorld {


    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    /*
    Invariants:
    2 lines of the same length in the middle (3n - 2).
    n-hexagons represent hexagons with side lengths of n
    except the middle.
    Each hexagon is 2n rows long.
    As you go up, add 2 tiles, then decrease once you get to 3n - 2.
     */

    /**
     * Finds the number of rows within a hexagon.
     * @param n the side length of the n-hexagon.
     * @return number of rows.
     */
    public int numberOfRows(int n) {
        return 2 * n;
    }

    /**
     * Finds the length of a row within the n-hexagon.
     * @param n the side length of the n-hexagon.
     * @param level the row "level" within the hexagon.
     * @return said length.
     */
    private int lengthOfRow(int n, int level) {
        return (2 * level) + n;
    }

    /**
     * Finds the length of the middle two rows of the
     * hexagon.
     * @param n the side length of the n-hexagon.
     * @return said length.
     */
    private int lengthOfMiddle(int n) {
        return (3 * n) - 2;
    }

    /**
     * Calculates the amount of blanks within an n-hexagon's level.
     * @param n the number of sides within the hexagon.
     * @param level the level of the hexagon to check blanks for.
     * @return said amount.
     */
    private int blanks(int n, int level) {
        return (lengthOfMiddle(n) - lengthOfRow(n, level)) / 2;
    }

    /**
     * Adds a hexagon to a given position in the world.
     * @param s the side length of the new hexagon.
     * @param X the x-position at the Lower Left Corner of the hexagon.
     * @param Y the y-position at the Lower Left Corner of the hexagon.
     * @param world the tile world to add this hexagon into.
     */
    public void addHexagon(int s, TETile[][] world, int X, int Y) {
        TETile tile = randomTile();
        for (int y = 0; y < s; y += 1) {
            for (int x = 0; x < lengthOfMiddle(s); x += 1) {
                if (x >= blanks(s, y) && x < blanks(s, y) + lengthOfRow(s, y)) {
                    world[x + X][y + Y] = tile;
                    world[x + X][(2 * s - y - 1) + Y] = tile;
                }
            }
        }
    }

    /** Random Color for the Hexagon. */
    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(6);
        switch (tileNum) {
            case 0: return Tileset.WATER;
            case 1: return Tileset.LOCKED_DOOR;
            case 2: return Tileset.GRASS;
            case 3: return Tileset.MOUNTAIN;
            case 4: return Tileset.SAND;
            case 5: return Tileset.TREE;
            default: return Tileset.FLOWER;
        }
    }

    /**
     * Main method of the HexWorld class.
     * @param args user-input.
     */
    public static void main(String[] args) {
        int s = 3;

        HexWorld hw = new HexWorld();

        int hexHeight = hw.numberOfRows(s);
        int hexWidth = hw.lengthOfMiddle(s);

        int worldHeight = 5 * hexHeight;
        int worldWidth = 3 * hexWidth + (2 * s);

        TERenderer ter = new TERenderer();
        ter.initialize(worldWidth, worldHeight);

        TETile[][] world = new TETile[worldWidth][worldHeight];

        // initialize tiles
        for (int x = 0; x < worldWidth; x += 1) {
            for (int y = 0; y < worldHeight; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        // Bottom Row
        int tileLLX = hexWidth + s;
        int tileLLY = 0;
        hw.addHexagon(s, world, tileLLX, tileLLY);

        // Odd-numbered Rows.
        for (int y = hexHeight; y < worldHeight - hexHeight; y += hexHeight) {
            for (int x = 0; x < worldWidth; x += (hexWidth + s)) {
                hw.addHexagon(s, world, x, y);
            }
        }

        //Top Row
        tileLLX = hexWidth + s;
        tileLLY = worldHeight - hexHeight;
        hw.addHexagon(s, world, tileLLX, tileLLY);

        // Even-numbered Rows.
        for (int y = s; y < worldHeight - hexHeight; y += hexHeight) {
            for (int x = 2 * s - 1; x < worldWidth - (hexWidth + s); x += (hexWidth + s)) {
                hw.addHexagon(s, world, x, y);
            }
        }

        ter.renderFrame(world);
    }
}
