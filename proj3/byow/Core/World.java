package byow.Core;

import byow.Core.Features.*;
import byow.Core.Load.Persistence;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * @author  Liam Howell, Wil Aquino
 * Date:    November 16, 2020
 * Project: Build Your Own World (BYOW, Proj 3)
 * Module:  RoomWorld.java
 * Purpose: Generates and draws a world.
 *
 * Implementation Vocabulary Index:
 * - "Room"        -- A rectangular area within the world.
 * - "Hallway"     -- A strictly 3 x n rectangular area which can have a
 *                    horizontal or vertical orientation.
 * - "Edge"        -- The wall of a room or hallway which can have a
 *                    horizontal or vertical orientation.
 * - "Point"       -- An (x, y) coordinate within the world which CANNOT
 *                    exceed the world size and thus must exist within
 *                    the world.
 * - "EXIT point"  -- Exit point of a room and entrance point of a hallway.
 * - "END point"   -- End point of a hallway and entrance point of a potential
 *                    room.
 * - "Lower Bound" -- Bottom- or left-most point of an edge.
 * - "Upper Bound" -- Top- or right-most point of an edge.
 * - "Priority"    -- Priorities are actually used for determining which
 *                    potential hallways get looked at first by using a PQ,
 *                    although if it's an integer, it doubles as the distance
 *                    from the base room (priority 0).
 */
public class World {

    /** Random number generator. */
    private final Random rGen;

    /** State of the world. */
    private final TETile[][] world;

    /** Seed for the world. */
    private final long seed;

    /** PQ which maps every hallway from the base (first) room. */
    private final DoubleMapPQ<Hallway> hwMap;

    /** Tile template for any tile outside rooms and hallways. */
    public static final TETile EMPTYTILE = Tileset.WATER;

    /** Tile template for the avatar. */
    public static final TETile AVATAR = Tileset.TREE;

    /** Location of the avatar. */
    private Point avatarLoc;

    /** Flag for detecting if the game was finished. */
    private boolean gameFinished;

    /**
     * Initializes the world.
     * @param s the seed of the world.
     * @param r a random number generator.
     * @param al the avatar's location, if it exists.
     */
    public World(long s, Random r, Point al) {
        rGen = r;
        world = new TETile[Engine.WIDTH][Engine.HEIGHT];
        seed = s;
        hwMap = new DoubleMapPQ<>();
        avatarLoc = al;

        for (int x = 0; x < Engine.WIDTH; x += 1) {
            for (int y = 0; y < Engine.HEIGHT; y += 1) {
                setTile(x, y, EMPTYTILE);
            }
        }

        int baseRoomX = RandomUtils.uniform(rGen, 10, 40);
        int baseRoomY = RandomUtils.uniform(rGen, 10, 20);
        int baseRoomWidth = RandomUtils.uniform(rGen, 5, 10);
        int baseRoomHeight = RandomUtils.uniform(rGen, 5, 10);

        Room base = new Room(baseRoomX, baseRoomY, baseRoomWidth,
                baseRoomHeight, false, null, 0);
        addRoom(base);

        while (hwMap.size() > 0) {
            generateRooms(hwMap.removeSmallest(), 0, 5);
        }

        generateEntities();
    }

    /**
     * Builds a room within the world.
     * @param newRoom the room to add.
     */
    private void addRoom(Room newRoom) {
        int xCoord = newRoom.getX();
        int yCoord = newRoom.getY();
        int w = newRoom.getWidth();
        int h = newRoom.getHeight();
        Point endPoint = newRoom.getEnd();

        boolean buildingBase = endPoint == null;

        for (int x = xCoord; x < xCoord + w; x += 1) {
            for (int y = yCoord; y < yCoord + h; y += 1) {
                if (newRoom.onEdge(x, y)) {
                    if (buildingBase || !spaceIsOpen(x, y)) {
                        setTile(x, y, Tileset.WALL);
                    }
                } else {
                    setTile(x, y, Tileset.FLOOR);
                }
            }
        }

        if (!buildingBase) {
            setTile(endPoint.getX(), endPoint.getY(), Tileset.FLOOR);
        }

        generateHallways(newRoom);
    }

    /**
     * Generates hallways to add to a room.
     * @param srcRoom the room to add potential hallways to.
     */
    private void generateHallways(Room srcRoom) {
        int edgesLeft = 4;
        int minHallways = 3;

        if (srcRoom.getEnd() != null) {
            edgesLeft -= 1;
            minHallways -= 1;
        }

        int numHallways = RandomUtils.uniform(rGen, minHallways, edgesLeft + 1);
        for (Edge edge : srcRoom.getEdges()) {
            if (!edge.hasHallway()) {
                if (edgesLeft == 0 || numHallways == 0) {
                    break;
                }

                Hallway newHW;
                int hwLength = RandomUtils.uniform(rGen, 3, 5);
                boolean hallwayWanted =
                        RandomUtils.bernoulli(rGen,
                                ((double) numHallways / (double) edgesLeft));

                if (hallwayWanted) {
                    if (edge.nearBoundary(Engine.WIDTH, Engine.HEIGHT)) {
                        numHallways -= 1;
                        edgesLeft -= 1;
                        continue;
                    }

                    if (!edge.isHorizontal()) {
                        int yLowerBound = edge.getLowerBound().getY() + 1;
                        int yUpperBound = edge.getUpperBound().getY() - 1;
                        int x = edge.getUpperBound().getX();
                        int hallwayY = RandomUtils.uniform(
                                rGen, yLowerBound, yUpperBound);

                        Point newExit = new Point(x, hallwayY);
                        newHW = new Hallway(newExit, hwLength, srcRoom,
                                !edge.isHorizontal(),
                                x != srcRoom.getX()
                                        && hallwayY != srcRoom.getY());
                    } else {
                        int xLowerBound = edge.getLowerBound().getX() + 1;
                        int xUpperBound = edge.getUpperBound().getX() - 1;
                        int y = edge.getUpperBound().getY();
                        int hallwayX = RandomUtils.uniform(
                                rGen, xLowerBound, xUpperBound);

                        Point newExit = new Point(hallwayX, y);
                        if (newExit.nearEdge(Engine.WIDTH, Engine.HEIGHT)) {
                            numHallways -= 1;
                            edgesLeft -= 1;
                            continue;
                        }

                        newHW = new Hallway(newExit, hwLength, srcRoom,
                                !edge.isHorizontal(),
                                y != srcRoom.getY()
                                        && hallwayX != srcRoom.getX());
                    }

                    if (!newHW.getEnd().nearEdge(Engine.WIDTH, Engine.HEIGHT)) {
                        addHallway(newHW);
                    }

                    numHallways -= 1;
                }
                edgesLeft -= 1;
            }
        }
    }

    /**
     * Builds a hallway within the world.
     * @param hw the hallway to add.
     */
    private void addHallway(Hallway hw) {
        int axisStart = hw.getExit().getX() - 1;
        int other = hw.getExit().getY();
        Point end = hw.getEnd();

        if (!hw.isHorizontal()) {
            axisStart = hw.getExit().getY() - 1;
            other = hw.getExit().getX();
        }
        if (!hw.increasingDirection()) {
            axisStart = axisStart - (hw.getLength() - 2);
        }

        Point hwExitLeft =
                new Point(axisStart + 1, other - 1);
        Point hwExitRight =
                new Point(axisStart + 1, other + 1);

        if (!hw.isHorizontal()) {
            hwExitLeft =
                    new Point(other - 1, axisStart + 1);
            hwExitRight =
                    new Point(other + 1, axisStart + 1);
        }

        if (!hw.increasingDirection()) {
            Point oldHWExitLeft = hwExitLeft;
            hwExitLeft = hwExitRight;
            hwExitRight = oldHWExitLeft;
        }

        boolean hallwayExitEmpty =
                getTile(end.getX(), end.getY()).equals(EMPTYTILE);
        boolean hallwayExitLeftEmpty =
                getTile(hwExitLeft.getX(), hwExitLeft.getY()).equals(EMPTYTILE);
        boolean hallwayExitRightEmpty =
                getTile(hwExitRight.getX(), hwExitRight.getY()).equals(EMPTYTILE);

        if (!hallwayExitEmpty || !hallwayExitLeftEmpty || !hallwayExitRightEmpty) {
            return;
        }

        for (int i = axisStart; i < axisStart + hw.getLength(); i += 1) {
            if (hw.isHorizontal()) {
                setTile(i, other - 1, Tileset.WALL);
                setTile(i, other, Tileset.FLOOR);
                setTile(i, other + 1, Tileset.WALL);
            } else if (!hw.isHorizontal()) {
                setTile(other - 1, i, Tileset.WALL);
                setTile(other, i, Tileset.FLOOR);
                setTile(other + 1, i, Tileset.WALL);
            }
        }

        double srcPriority = hw.getSource().getPriority();
        double priority = RandomUtils.uniform(
                rGen, srcPriority, srcPriority + 1);

        if (end.nearEdge(Engine.WIDTH, Engine.HEIGHT)) {
            setTile(end.getX(), end.getY(), Tileset.WALL);
        } else {
            hwMap.add(hw, priority);
            setTile(end.getX(), end.getY(), EMPTYTILE);
        }

        hw.getSource().addHallwayToRoom(hw.getExit());
    }

    /**
     * Calculates a random width or height depending on how far
     * a point is from a boundary.
     * @param origin the point to base the calculation off of.
     * @param bound the boundary.
     * @return said random width or height.
     */
    private int randomizedWidthHeight(Point origin, int bound) {
        int wh;
        if (origin.distanceFrom(new Point(origin.getX(), bound)) > 9) {
            wh = RandomUtils.uniform(rGen, 3, 9);
        } else {
            wh = RandomUtils.uniform(rGen, 3, origin.distanceFrom(new Point(
                    origin.getX(), bound)));
        }

        return wh;
    }

    /**
     * Generates rooms until a room can connect to a given hallway.
     * @param hw the given hallway.
     * @param srcPriority the given hallway's precede room's distance from
     *                    the base room.
     * @param attemptsLeft the amount of attempts left to try and generate
     *                     a room that can connect to the given hallway.
     */
    private void generateRooms(Hallway hw, int srcPriority, int attemptsLeft) {
        Point entrance = hw.getEnd();

        if (attemptsLeft == 0) {
            TETile entranceTile = getTile(entrance.getX(), entrance.getY());
            if (!entranceTile.equals(Tileset.FLOOR)) {
                setTile(entrance.getX(), entrance.getY(), Tileset.WALL);
            }
            return;
        }

        int x, y, w, h;
        if (hw.isHorizontal()) {
            x = entrance.getX();
            h = RandomUtils.uniform(rGen, 4, 9);
            y = RandomUtils.uniform(rGen, entrance.getY() - (h - 1),
                    entrance.getY());
            if (hw.increasingDirection()) {
                w = randomizedWidthHeight(entrance, Engine.WIDTH - 1);
            } else {
                w = randomizedWidthHeight(entrance, 0);
                x = x - (w - 1);
            }
        } else {
            y = entrance.getY();
            w = RandomUtils.uniform(rGen, 4, 9);
            x = RandomUtils.uniform(rGen,
                    entrance.getX() - (w - 1), entrance.getX());
            if (hw.increasingDirection()) {
                h = randomizedWidthHeight(entrance, Engine.HEIGHT - 1);
            } else {
                h = randomizedWidthHeight(entrance, 0);
                y = y - (h - 1);
            }
        }

        Room newRoom = new Room(x, y, w, h, true, hw.getEnd(), srcPriority + 1);

        for (Point corner : newRoom.getCorners()) {
            boolean exceedsXBounds = corner.getX() < 0
                    || corner.getX() + w >= Engine.WIDTH;
            boolean exceedsYBounds = corner.getY() < 0
                    || corner.getY() + h >= Engine.HEIGHT;

            if (exceedsXBounds || exceedsYBounds) {
                generateRooms(hw, srcPriority, attemptsLeft - 1);
                return;
            }

            boolean cornerOverlaps =
                    !getTile(corner.getX(), corner.getY()).equals(EMPTYTILE);

            Point hwExitLeft = new Point(
                    hw.getExit().getX(), hw.getExit().getY() - 1);
            Point hwExitRight = new Point(
                    hw.getExit().getX(), hw.getExit().getY() + 1);

            if (!hw.isHorizontal()) {
                hwExitLeft = new Point(
                        hw.getExit().getX() - 1, hw.getExit().getY());
                hwExitRight =
                        new Point(hw.getExit().getX() + 1, hw.getExit().getY());
            }

            boolean cornerOnHallwayWall =
                    corner.equals(hwExitLeft) || corner.equals(hwExitRight);

            if ((cornerOverlaps && !cornerOnHallwayWall)) {
                generateRooms(hw, srcPriority, attemptsLeft - 1);
                return;
            }
        }

        addRoom(newRoom);
    }

    /**
     * Retrieves the current state of the world.
     * @return said state.
     */
    public TETile[][] getWorld() {
        return world;
    }

    /**
     * Checks if a tile at a specific coordinate is not a wall or
     * locked exit.
     * @param x the x-coordinate of the tile.
     * @param y the y-coordinate of the tile.
     * @return True if the tile is not a wall.
     *         False if the tile is a wall.
     */
    public boolean spaceIsOpen(int x, int y) {
        boolean withinBounds = x >= 0 && x < Engine.WIDTH
                && y >= 0 && y < Engine.HEIGHT;
        boolean targetTileIsValid = getTile(x, y).equals(Tileset.FLOOR)
                || getTile(x, y).equals(Tileset.UNLOCKED_DOOR);
        return withinBounds && targetTileIsValid;
    }

    /**
     * Checks if a tile is a wall and can be set as the locked door.
     * @param doorIsUnset a flag to see if the locked door was set or not.
     * @param x the x-coordinate of the tile to check.
     * @param y the y-coordinate of the tile to check
     * @return True if the avatar could reach the locked door.
     *         False if the avatar cannot reach the locked door.
     */
    private boolean wallAccessible(boolean doorIsUnset, int x, int y) {
        if (doorIsUnset && getTile(x, y).equals(Tileset.WALL)) {
            return spaceIsOpen(x - 1, y) || spaceIsOpen(x + 1, y)
                    || spaceIsOpen(x, y - 1) || spaceIsOpen(x, y + 1);
        }
        return false;
    }

    /** Set the avatar and locked door. */
    private void generateEntities() {
        boolean avatarUnset = avatarLoc == null;
        boolean lockedDoorUnset = true;

        while (avatarUnset || lockedDoorUnset) {
            int randX = RandomUtils.uniform(rGen, Engine.WIDTH - 1);
            int randY = RandomUtils.uniform(rGen, Engine.HEIGHT - 1);

            if (avatarUnset && spaceIsOpen(randX, randY)) {
                avatarLoc = new Point(randX, randY);
                avatarUnset = false;
            } else if (wallAccessible(lockedDoorUnset, randX, randY)) {
                setTile(randX, randY, Tileset.UNLOCKED_DOOR);
                lockedDoorUnset = false;
            }
        }

        moveAvatar(avatarLoc.getX(), avatarLoc.getY());
    }

    /** Displays the state of the world on the console. */
    public void displayOnConsole() {
//        System.out.println("INITIAL WORLD BUILD:");
//        System.out.println(TETile.toString(getWorld()));
    }

    /**
     * Retrieves the tile at a specific coordinate.
     * @param x the x-coordinate of the tile.
     * @param y the y-coordinate of the tile.
     * @return said tile.
     */
    public TETile getTile(int x, int y) {
        return world[x][y];
    }

    /**
     * Change the tile at a specific coordinate.
     * @param x the x-coordinate to move to.
     * @param y the y-coordinate to move to
     * @param tile the tile to set at point (x, y)
     */
    public void setTile(int x, int y, TETile tile) {
        world[x][y] = tile;
    }

    /**
     * Change the tile at a specific coordinate.
     * @param p the point to change.
     * @param tile the tile to set at point (x, y)
     */
    public void setTile(Point p, TETile tile) {
        setTile(p.getX(), p.getY(), tile);
    }

    /**
     * Retrieves the position coordinate of the avatar.
     * @return said coordinate.
     */
    public Point getAvatarPosition() {
        return avatarLoc;
    }

    /**
     * Moves the avatar to a new coordinate.
     * @param x the new x-coordinate to move to.
     * @param y the new y-coordinate to move to.
     */
    private void moveAvatar(int x, int y) {
        if (getTile(x, y).equals(Tileset.UNLOCKED_DOOR)) {
            gameFinished = true;
            return;
        }
        setTile(avatarLoc, Tileset.FLOOR);
        avatarLoc = new Point(x, y);
        setTile(avatarLoc, AVATAR);
    }

    /**
     * Move the avatar from one coordinate to another, if possible.
     * @param x the potentially new x-coordinate of the avatar.
     * @param y the potentially new y-coordinate of the avatar.
     * @return True if the avatar moved.
     *         False if it did not.
     */
    public boolean moveAvatarIfPossible(int x, int y) {
        boolean spaceOpen = spaceIsOpen(x, y);
        if (spaceOpen) {
            moveAvatar(x, y);
        }
        return spaceOpen;
    }

    /**
     * Moves the avatar in the given direction, if possible.
     * @param c the direction to move in, according to usual
     *          game WASD controls.
     * @return True if the avatar was moved.
     *         False if it was not.
     */
    public boolean moveAvatar(char c) {
        int avatarX = getAvatarPosition().getX();
        int avatarY = getAvatarPosition().getY();
        switch (c) {
            case 'W':
                return moveAvatarIfPossible(avatarX, avatarY + 1);
            case 'A':
                return moveAvatarIfPossible(avatarX - 1, avatarY);
            case 'S':
                return moveAvatarIfPossible(avatarX, avatarY - 1);
            case 'D':
                return moveAvatarIfPossible(avatarX + 1, avatarY);
            default:
                return false;
        }
    }

    /**
     * Retrieves this world's seed.
     * @return said seed.
     */
    public long getSeed() {
        return seed;
    }

    /** Checks if the games was completed. */
    public boolean exitFound() {
        return gameFinished;
    }

    /**
     * Stores a save state every time a user quits.
     * @param saveSlot the slot to save in.
     * @param input the log of movements up until this state.
     */
    public void quitSave(int saveSlot, String input) {
        Persistence saves = new Persistence();

        Point lastAL = getAvatarPosition();

        if (saves.hasSaveForSeed(getSeed())) {
            saves.updateSave(getSeed(),
                    lastAL.getX(), lastAL.getY(), input);
        } else {
            saves.addSave(getSeed(),
                    lastAL.getX(), lastAL.getY(), saveSlot, input);
        }
    }
}
