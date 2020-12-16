package byow.Core.Features;

/**
 * @author  Liam Howell, Wil Aquino
 * Date:    November 18, 2020
 * Project: Build Your Own World (BYOW, Proj 3)
 * Module:  Point.java
 * Purpose: Represents an (x, y) coordinate within a 2D grid.
 *
 * @source  CS61B Proj2AB, provided starter class template.
 */
public class Point {

    /** x- and y- coordinates of the point. */
    private final int x, y;

    /**
     * Initializes the point.
     * @param xCoord x-coordinate of the point.
     * @param yCoord y-coordinate of the point.
     */
    public Point(int xCoord, int yCoord) {
        x = xCoord;
        y = yCoord;
    }

    /**
     * Checks if this point is equal to another point.
     * @param o the object to analyze.
     * @return whether this point is the same as the provided object.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    /**
     * Retrieves the hash code of this point.
     * @return said hash code.
     */
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    /**
     * Finds this point's string representation.
     * @return this point in the form of a string.
     */
    @Override
    public String toString() {
        return "(" + getX() + ", " + getY() + ")";
    }

    /**
     * Retrieve this point's x-coordinate.
     * @return said coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Retrieve this point's y-coordinate.
     * @return said coordinate.
     */
    public int getY() {
        return y;
    }

    /**
     * Finds the distance between this point and a given point.
     * @param other the other point to compare with.
     * @return said distance.
     */
    public int distanceFrom(Point other) {
        if (other.getX() == getX()) {
            return Math.abs(getY() - other.getY());
        }
        return Math.abs(getX() - other.getX());
    }

    /**
     * Checks if this point is near the world boundaries.
     * @param worldWidth the width of the world.
     * @param worldHeight the height of the world.
     * @return whether the point is near the world's width or height.
     */
    public boolean nearEdge(int worldWidth, int worldHeight) {
        boolean nearXBoundaries = getX() - 5 < 0 || getX() + 5 >= worldWidth,
                nearYBoundaries = getY() - 5 < 0 || getY() + 5 >= worldHeight;
        return nearXBoundaries || nearYBoundaries;
    }
}
