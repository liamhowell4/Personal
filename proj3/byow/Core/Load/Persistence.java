package byow.Core.Load;

import edu.princeton.cs.introcs.In;

import byow.Core.Features.Point;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author  Liam Howell
 * Date:    November 23, 2020
 * Project: Build Your Own World (BYOW, Proj 3)
 * Module:  Persistence.java
 * Purpose: Handles file input and output.
 *          With respect to BYOW, this is used for saving
 *          and loading states of the world.
 */
public class Persistence {

    /** Text file that contains all load data. */
    private final File loadLog;

    /** Map of all Load objects matched to their seed. */
    private final HashMap<Long, Load> loadMap;

    /** Map of all matched seeds to their line number in the load file. */
    private final HashMap<Long, Integer> lineNumberMap;

    /** Number of loads stored in the text file. */
    private int numLoads;

    /** List of all loads in the load file. */
    private final ArrayList<Load> loadList;

    /** Map from each seed to a save state string. */
    private final HashMap<Long, String> inputs;

    /**
     * Persistence Constructor.
     * @source Itai Smith, file creation implementation.
     */
    public Persistence() {
        numLoads = 0;
        loadLog = new File("load.txt");
        if (!loadLog.exists()) {
            try {
                loadLog.createNewFile();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }

        In logReader = new In(loadLog);

        loadMap = new HashMap<>();
        lineNumberMap = new HashMap<>();
        loadList = new ArrayList<>();
        inputs = new HashMap<>();

        createLoadLog(logReader);
    }

    /**
     * Private helper for the constructor that parses the Load File, turning
     * all of the data into individual load objects to be searched and changed.
     * @param load the parsable In object created from the text file
     *             containing save information.
     */
    private void createLoadLog(In load) {
        int lineNumber = 0;
        while (load.hasNextLine()) {
            String line = load.readLine();
            String[] loadInfoString = line.split(", ");

            long seed = Long.parseLong(loadInfoString[0]);
            int x = Integer.parseInt(loadInfoString[1]);
            int y = Integer.parseInt(loadInfoString[2]);
            String input;
            try {
                input = loadInfoString[3];
            } catch (ArrayIndexOutOfBoundsException ar) {
                input = null;
            }

            Load thisLoad = new Load(seed, x, y, input);

            loadMap.put(seed, thisLoad);
            lineNumberMap.put(seed, lineNumber);
            loadList.add(thisLoad);
            inputs.put(seed, input);

            lineNumber += 1;
        }
        numLoads = lineNumber;
    }

    /**
     * Returns true if there is a saved Load state for a given seed.
     * @param seed the seed mentioned above.
     */
    public boolean loadExists(long seed) {
        return loadMap.containsKey(seed);
    }

    /**
     * If a load exists for a given seed, returns the load object.
     * @param seed the aforementioned seed.
     */
    public Load getLoad(long seed) {
        if (loadExists(seed)) {
            return loadMap.get(seed);
        } else {
            throw new IllegalArgumentException(
                    "Seed doesn't have a Load State saved!");
        }
    }

    /**
     * Adds a new Save to the loadLog document.
     * @param seed seed of load to be added.
     * @param x x-coordinate of the Avatar.
     * @param y y-coordinate of the Avatar.
     * @param input the sequence of moves during this save.
     */
    public void addNewSaveLine(long seed, int x, int y, String input) {
        if (loadExists(seed)) {
            updateSave(seed, x, y, input);
            return;
        }

        if (numLoads >= 5) {
            throw new IllegalCallerException(
                    "There are already 5 saved states");
        }

        Load thisLoad = new Load(seed, x, y, input);

        logSave(thisLoad, numLoads, seed, input);

        try {
            FileWriter fw = new FileWriter(loadLog, true);
            if (numLoads == 0) {
                fw.write(seed + ", " + x + ", " + y + ", " + input);
            } else {
                fw.write("\n" + seed + ", " + x + ", " + y + ", " + input);
            }
            fw.close();
        } catch (IOException ioe) {
            System.out.println("File is incompatible");
        }

        numLoads += 1;
    }

    /**
     * Updates a save in the load file.
     * @param seed the seed within the load file.
     * @param newX the new x-coordinate of the avatar.
     * @param newY the new y-coordinate of the avatar.
     * @param input the new sequence of moves during the save.
     */
    public void updateSave(long seed, int newX, int newY, String input) {
        addSave(seed, newX, newY, lineNumberMap.get(seed), input);

        if (!loadExists(seed)) {
            throw new IllegalArgumentException(
                    "Seed not in Log, try addLoad");
        } else if (new Load(seed, newX, newY, input).equals(
                loadMap.get(seed))) {
            return;
        }
    }

    /**
     * Updates the information about a given load at a
     * line in the Load text file.
     * @param seed the seed of a pre-saved load that has
     *             a new (x, y) coordinate for the avatar.
     * @param x the new x-coordinate of the avatar.
     * @param y the new y-coordinate of the avatar.
     * @param lineToChange the line within the lod file to change.
     * @param input the list of movements up until this point.
     */
    public void addSave(long seed, int x, int y, int lineToChange,
                        String input) {

        if (lineToChange >= numLoads) {
            addNewSaveLine(seed, x, y, input);
            return;
        }

        long oldSeedAtLine = loadList.get(lineToChange).getSeed();

        lineNumberMap.remove(oldSeedAtLine);
        loadMap.remove(oldSeedAtLine);

        In oldLoads = new In(loadLog);
        StringBuilder newFile = new StringBuilder();
        String newLine;
        int linePointer = 0;

        Load thisLoad = new Load(seed, x, y, input);

        while (linePointer < lineToChange) {
            if (linePointer == 0) {
                newLine = oldLoads.readLine();
            } else {
                newLine = "\n" + oldLoads.readLine();
            }
            newFile.append(newLine);
            linePointer += 1;
        }

        oldLoads.readLine();

        if (lineToChange == 0) {
            newLine = seed + ", " + x + ", " + y + ", " + input;
        } else {
            newLine = "\n" + seed + ", " + x + ", " + y + ", " + input;
        }
        newFile.append(newLine);

        logSave(thisLoad, lineToChange, seed, input);

        while (oldLoads.hasNextLine()) {
            newLine = "\n" + oldLoads.readLine();
            newFile.append(newLine);
        }

        try {
            FileWriter fw = new FileWriter(loadLog);
            fw.write(newFile.toString());
            fw.close();
        } catch (IOException ioe) {
            System.out.println("File is incompatible");
        }
    }

    /** Returns the list of loads. */
    public ArrayList<Load> getLoadList() {
        if (loadList.size() > 0) {
            return loadList;
        } else {
            return null;
        }
    }

    /** Method to update the hashSets and ArrayList to reflect updated Saves.
     *
     * @param save the load object of the new seed.
     * @param numberLine the number of the line at which the new save is
     *                   listed in the load doc.
     * @param seed the seed of the newly saved state.
     * @param input the movements up until this point.
     */
    private void logSave(Load save, int numberLine, long seed, String input) {
        lineNumberMap.put(seed, numberLine);
        loadMap.put(seed, save);
        if (numberLine == numLoads) {
            loadList.add(numberLine, save);
        } else {
            loadList.set(numberLine, save);
        }
        inputs.put(seed, input);
    }

    /**
     * Retrieves the number of saves in the load File.
     * @return said number.
     */
    public int getNumLoads() {
        return numLoads;
    }

    /**
     * Checks if a given seed already has a save.
     * @param seed the seed to check.
     * @return True if it does have a save.
     *         False if it does not.
     */
    public boolean hasSaveForSeed(long seed) {
        return loadMap.containsKey(seed);
    }

    /**
     * Retrieves the last location of the avatar in a given seed.
     * @param seed the seed to analyze.
     * @return said location.
     */
    public Point getLastSeen(long seed) {
        return loadMap.get(seed).getLastSeen();
    }

    /**
     * Returns the input for a given seed.
     * @param seed the seed to analyze.
     * @return the input of a seed.
     */
    public String getInput(long seed) {
        return inputs.get(seed);
    }
}
