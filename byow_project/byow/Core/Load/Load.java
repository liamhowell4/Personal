package byow.Core.Load;

import byow.Core.Features.Point;

import java.util.Objects;

/**
 * @author  Liam Howell
 * Date:    November 23, 2020
 * Project: Build Your Own World (BYOW, Proj 3)
 * Module:  Load.java
 * Purpose: Data Bank for all information about a given load state:
 *          seed, current position of Avatar.
 */

public class Load {

    /** Seed of Save. */
    private final long seed;

    /** Point where the avatar was at Save. */
    private final Point lastSeen;

    /** Previous Movements of the avatar in the seed. */
    private final String movement;

    /**
     * Constructor for Load.
     * @param sd the seed.
     * @param x x-coordinate of the Avatar's last position.
     * @param y y-coordinate of the Avatar's last position.
     * @param input the movements up until this point.
     */
    public Load(long sd, int x, int y, String input) {
        seed = sd;
        lastSeen = new Point(x, y);
        movement = input;
    }

    /** Returns the seed of the load. */
    public long getSeed() {
        return seed;
    }

    /** Returns the Point where the avatar was at the save. */
    public Point getLastSeen() {
        return lastSeen;
    }

    /** Returns a string containing all previous movements of a saved state. */
    public String getPreviousMovements() {
        return movement;
    }

    /** Equals Method. */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Load load = (Load) o;
        return seed == load.seed
                && Objects.equals(lastSeen, load.lastSeen);
    }

    /** Hashcode Method. */
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
