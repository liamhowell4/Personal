package byow.Core.Features;

/**
 * @author  Liam Howell
 * Date:    November 17, 2020
 * Project: Build Your Own World (BYOW, Proj 3)
 * Module:  Hallway.java
 * Purpose: Representation of a HALLWAY within the world.
 */
public class Hallway {

    /** Point of the hallway where it exits the source room. */
    private final Point exitPoint;

    /** Length of the hallway, including ends. */
    private final int length;

    /** Room preceding this hallway. */
    private final Room source;

    /** Flag for if this hallway is oriented horizontally. */
    private final boolean horizontal;

    /** Forward direction of this hallway. */
    private final boolean direction;

    /** End point of this hallway. */
    private final Point endPoint;

    /**
     * Initializes the hallway.
     * @param exit point at which the hallway exits its source room.
     * @param len length of the hallway.
     * @param src source room of the hallway.
     * @param hztl orientation of the hallway.
     * @param dir direction of the hallway.
     */
    public Hallway(Point exit, int len, Room src,
                   boolean hztl, boolean dir) {
        exitPoint = exit;
        length = len;
        source = src;
        horizontal = hztl;
        direction = dir;
        endPoint = calculateEnd();
    }

    /**
     * Retrieves end point of this hallway.
     * @return said point.
     */
    public Point getEnd() {
        return endPoint;
    }

    /**
     * Retrieves entrance point of this hallway.
     * @return said point.
     */
    public Point getExit() {
        return exitPoint;
    }

    /**
     * Retrieves the room that precedes this hallway.
     * @return said room.
     */
    public Room getSource() {
        return source;
    }

    /**
     * Checks if the hallway is horizontally oriented.
     * @return True if the hallway is horizontal.
     *         False if the hallway is vertical.
     */
    public boolean isHorizontal() {
        return horizontal;
    }

    /** Returns the length of the hallway. */
    public int getLength() {
        return length;
    }

    /**
     * Retrieves the forward direction of this hallway.
     * @return True if the hallway is travelling north/east.
     *         False if the hallway is travelling south/west.
     */
    public boolean increasingDirection() {
        return direction;
    }

    /**
     * Finds the endpoint of this hallway.
     * @return said endpoint.
     */
    private Point calculateEnd() {
        int axisStart = getExit().getX() - 1;
        int otherCoord = getExit().getY();
        Point end = new Point(axisStart + (getLength() - 1), otherCoord);

        if (!isHorizontal()) {
            axisStart = getExit().getY() - 1;
            otherCoord = getExit().getX();
            end = new Point(otherCoord, axisStart + (getLength() - 1));
        }

        if (!increasingDirection()) {
            axisStart = axisStart - (length - 2);
            if (isHorizontal()) {
                end = new Point(axisStart, otherCoord);
            } else {
                end = new Point(otherCoord, axisStart);
            }
        }

        return end;
    }
}
