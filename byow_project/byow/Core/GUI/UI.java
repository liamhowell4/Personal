package byow.Core.GUI;

import byow.Core.Engine;
import byow.Core.Features.Point;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Font;
import java.awt.Color;

/**
 * @author  Wil Aquino
 * Date:    November 23, 2020
 * Project: Build Your Own World (BYOW, Proj 3)
 * Module:  UI.java
 * Purpose: Handles all interaction with the GUI.
 */
public class UI {

    /** Different fonts for the start menu. */
    private final Font menuFont, loadMenuFont, noteFont;

    /** Initialize the UI fonts. */
    public UI() {
        menuFont = new Font("menu", Font.PLAIN,
                (StartMenu.MENU_HEIGHT / 3) - 15);
        noteFont = new Font("note", Font.PLAIN,
                (StartMenu.MENU_HEIGHT / 3) - 20);
        loadMenuFont = new Font("load menu", Font.PLAIN,
                StartMenu.MENU_HEIGHT / 3);
    }

    /** Clears the current window with a black background. */
    public void clearWindow() {
        StdDraw.clear(Color.BLACK);
    }

    /**
     * Sets the window size to be (w x h).
     * @param w the width of the new window.
     * @param h the height of the new window
     */
    public void setWindowSize(int w, int h) {
        StdDraw.setXscale(0, w);
        StdDraw.setYscale(0, h);
    }

    /** Show the drawings on the off-screen canvas. */
    public void showUpdate() {
        StdDraw.show();
    }

    /**
     * Displays text at position (x,y) on the current window.
     * @param x the x-position within the window.t
     * @param y the y-position within the window.
     * @param text the text to display.
     */
    public void display(int x, int y, String text) {
        StdDraw.text(x, y, text);
        showUpdate();
    }

    /**
     * Retrieves the menu font.
     * @return the menu font.
     */
    public Font getMenuFont() {
        return menuFont;
    }

    /**
     * Retrieves the load menu font.
     * @return the load menu font.
     */
    public Font getLoadMenuFont() {
        return loadMenuFont;
    }

    /**
     * Retrieves the note font.
     * @return the note font.
     */
    public Font getNoteFont() {
        return noteFont;
    }

    /**
     * Sets the current window's text font.
     * @param f the font to set it to.
     */
    public void setFont(Font f) {
        StdDraw.setFont(f);
    }

    /**
     * Resets the draw color to another color.
     * @param newColor the new color to set to.
     */
    public void setColor(Color newColor) {
        StdDraw.setPenColor(newColor);
    }

    /**
     * Check if the mouse moved or not.
     * @param lastX the mouse's previous x-position.
     * @param lastY the mouse's previous y-position.
     * @return True if the mouse has moved.
     *         False if it didn't move.
     */
    public boolean mouseMoved(int lastX, int lastY) {
        int currX = mousePosition().getX();
        int currY = mousePosition().getY();

        if (currX >= 0 && currX <= Engine.WIDTH - 1
            && currY >= 0 && currY <= Engine.HEIGHT - 1) {
            boolean xChanged = currX != lastX;
            boolean yChanged = currY != lastY;
            return xChanged || yChanged;
        }

        return false;
    }

    /**
     * Retrieves the coordinates of the mouse pointer at any unit coordinate.
     * @return said coordinates.
     */
    public Point mousePosition() {
        int mouseX = (int) StdDraw.mouseX();
        int mouseY = (int) StdDraw.mouseY();
        return new Point(mouseX, mouseY);
    }

    /**
     * Delay the next screen update for some time, if one exists.
     * @param t the amount of time to delay, in milliseconds.
     */
    public void delayNextScreen(int t) {
        StdDraw.pause(t);
    }
}
