package byow.Core.GUI;

import edu.princeton.cs.introcs.StdDraw;

/**
 * @author  Wil Aquino
 * Date:    November 17, 2020
 * Project: Build Your Own World (BYOW, Proj 3)
 * Module:  KeyboardInput.java
 * Purpose: Processes keyboard inputs.
 *
 * @source  CS61B Staff, adapted implementation of keystroke
 *          recording from the byow.InputDemo package.
 */
public class KeyboardInput {

    /**
     * Retrieves next keystroke.
     * @return said keystroke.
     */
    public char getNextKey() {
        while (true) {
            if (!noKeyTypedYet()) {
                return Character.toUpperCase(StdDraw.nextKeyTyped());
            }
        }
    }

    /**
     * Checks if a key has been typed or not.
     * @return True if a keypress was detected.
     *         False if a keypress was not detected.
     */
    public boolean noKeyTypedYet() {
        return !StdDraw.hasNextKeyTyped();
    }

    /**
     * Checks if there could be a next keystroke.
     * @return that is is always possible.
     */
    public boolean possibleNextInput() {
        return true;
    }
}
