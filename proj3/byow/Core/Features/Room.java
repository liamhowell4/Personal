package byow.Core.Features;

import java.util.ArrayList;

/**
 * @author  Liam Howell, Wil Aquino
 * Date:    November 17, 2020
 * Project: Build Your Own World (BYOW, Proj 3)
 * Module:  Room.java
 * Purpose: Representation of a ROOM within the world.
 */
public class Room {

    /** Set of corners of this room. */
    private final ArrayList<Point> corners;

    /** Set of edges of this room. */
    private final ArrayList<Edge> edges;

    /** Coordinates of the lower left corner of this room. */
    private final int x, y;

    /** Width and height of this room. */
    private final int width, height;

    /** Distance from the base room. */
    private final int priority;

    /** End point of the room. */
    private final Point endPoint;

    /** Initializes the room.
     * @param xCoord the lower left x-coordinate of the room.
     * @param yCoord the lower left y-coordinate of the room.
     * @param w the width of the room.
     * @param h the height of the room.
     * @param hasEntrance whether the room has a hallway going into it.
     * @param end if it does have a hallway, this is the point where the
     *            hallway intersects the room.
     * @param p distance from the base room.
     */
    public Room(int xCoord, int yCoord, int w, int h, boolean hasEntrance,
                Point end, int p) {
        x = xCoord;
        y = yCoord;
        width = w;
        height = h;
        priority = p;
        endPoint = end;

        Point lowerLeft = new Point(x, y);
        Point lowerRight = new Point(x + w, y);
        Point upperLeft = new Point(x, y + h);
        Point upperRight = new Point(x + w, y + h);

        corners = new ArrayList<>();
        corners.add(lowerLeft);
        corners.add(upperLeft);
        corners.add(upperRight);
        corners.add(lowerRight);

        edges = new ArrayList<>();
        edges.add(new Edge(false, lowerLeft, upperLeft, false));
        edges.add(new Edge(false, lowerRight, upperRight, false));
        edges.add(new Edge(true, lowerLeft, lowerRight, false));
        edges.add(new Edge(true, upperLeft, upperRight, false));

        if (hasEntrance) {
            addHallwayToRoom(endPoint);
        }
    }

    /**
     * Adds a hallway to a room if the entrance point on a hallway
     * overlaps with one of the room's edges.
     * @param enterPoint the point at which the hallway intersects the room.
     */
    public void addHallwayToRoom(Point enterPoint) {
        for (Edge edge : edges) {
            if (edge.contains(enterPoint)) {
                edge.addHallwayToEdge();
            }
        }
    }

    /**
     * Retrieves this room's corners.
     * @return said corners.
     */
    public ArrayList<Point> getCorners() {
        return corners;
    }

    /**
     * Retrieves this room's edges.
     * @return said edges.
     */
    public ArrayList<Edge> getEdges() {
        return edges;
    }

    /**
     * Retrieves the distance from the base room.
     * @return said distance.
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Returns the end point of this room.
     * @return the entrance point.
     */
    public Point getEnd() {
        return endPoint;
    }

    /**
     * Retrieves the x-coordinate of this room's lower left corner.
     * @return said corner coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Retrieves the y-coordinate of this room's lower left corner.
     * @return said corner coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Retrieves the width of this room.
     * @return said width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Retrieves the height of this room.
     * @return said height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Checks if a given coordinate is on one of this room's edges.
     * @param xCoord the x-coordinate of the point.
     * @param yCoord the y-coordinate of the point.
     * @return whether the coordinate is on an edge or not.
     */
    public boolean onEdge(int xCoord, int yCoord) {
        boolean ifPointOnLeftOrRight = xCoord == getX()
                || xCoord == getX() + getWidth() - 1;
        boolean ifPointOnTopOrBottom = yCoord == getY()
                || yCoord == getY() + getHeight() - 1;

        return ifPointOnLeftOrRight || ifPointOnTopOrBottom;
    }
}
