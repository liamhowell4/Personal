package byow.Core.Features;

/**
 * @author  Liam Howell, Wil Aquino
 * Date:    November 17, 2020
 * Project: Build Your Own World (BYOW, Proj 3)
 * Module:  Edge.java
 * Purpose: Representation of an EDGE within the world.
 */
public class Edge {

    /** Flag for if this edge is horizontally oriented. */
    private final boolean horizontal;

    /** Bottom/Left coordinate of this edge, the lower bound (LB). */
    private final Point lowerBound;

    /** Top/Right coordinate of this edge, the upper bound (UB). */
    private final Point upperBound;

    /** Flag for if this edge has a hallway connected into it. */
    private boolean hallway;

    /**
     * Initializes the edge.
     * @param hztl orientation of the edge.
     * @param lb lower bound of the edge.
     * @param ub upper bound of the edge.
     * @param hh whether the edge already has a hallway going into it.
     */
    public Edge(boolean hztl, Point lb, Point ub, boolean hh) {
        horizontal = hztl;
        lowerBound = lb;
        upperBound = ub;
        hallway = hh;
    }

    /**
     * Checks if this is a horizontal edge.
     * @return True if the edge is horizontal.
     *         False if the edge is vertical.
     */
    public boolean isHorizontal() {
        return horizontal;
    }

    /**
     * Retrieves the lower bound of this edge.
     * @return the lower bound.
     */
    public Point getLowerBound() {
        return lowerBound;
    }

    /**
     * Retrieves the upper bound of this edge.
     * @return the upper bound.
     */
    public Point getUpperBound() {
        return upperBound;
    }

    /**
     * Checks if the edge has a hallway connected to it.
     * @return True if it does.
     *         False if it does not.
     */
    public boolean hasHallway() {
        return hallway;
    }

    /** Changes the state of the edge to have a hallway. */
    public void addHallwayToEdge() {
        if (hasHallway()) {
            throw new IllegalCallerException("Edge already has hallway!");
        }
        hallway = true;
    }

    /**
     * Checks if a given point is on this edge.
     * @param pt the point to check.
     * @return whether this edge contains the point.
     */
    public boolean contains(Point pt) {
        boolean onSameLevel, withinEdgeBounds;

        if (isHorizontal()) {
            onSameLevel = pt.getY() == lowerBound.getY();
            withinEdgeBounds = pt.getX() >= getLowerBound().getX()
                    && pt.getX() <= getUpperBound().getX();
        } else {
            onSameLevel = pt.getX() == lowerBound.getX();
            withinEdgeBounds = pt.getY() >= getLowerBound().getY()
                    && pt.getY() <= getUpperBound().getY();
        }

        return onSameLevel && withinEdgeBounds;
    }

    /**
     * Checks if an edge is near a boundary, for building hallways FROM rooms.
     * @param worldWidth the width of the world
     * @param worldHeight the height of the world.
     * @return whether the edge is within 2-5 blocks of a boundary.
     */
    public boolean nearBoundary(int worldWidth, int worldHeight) {
        boolean edgeNearBoundary, endpointNearBoundary;

        if (isHorizontal()) {
            edgeNearBoundary = lowerBound.getY() - 5 <= 0
                    || upperBound.getY() + 5 >= (worldHeight - 1);
            endpointNearBoundary = lowerBound.getX() - 2 <= 0
                    || upperBound.getX() + 2 >= (worldWidth - 1);
        } else {
            edgeNearBoundary = lowerBound.getX() - 5 <= 0
                    || upperBound.getX() + 5 >= (worldWidth - 1);
            endpointNearBoundary = lowerBound.getY() - 2 <= 0
                    || upperBound.getY() + 2 >= (worldHeight - 1);
        }

        return edgeNearBoundary && endpointNearBoundary;
    }
}
