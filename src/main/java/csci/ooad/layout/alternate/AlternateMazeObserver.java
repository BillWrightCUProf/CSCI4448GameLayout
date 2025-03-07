package csci.ooad.layout.alternate;

import csci.ooad.layout.intf.IMaze;
import csci.ooad.layout.intf.IMazeObserver;

import java.util.List;

/**
 * This class represents an alternative observer for a maze,
 * The observer gets updates about the maze and its status.
 */
public class AlternateMazeObserver implements IMazeObserver {

    static {
        System.out.println("AlternateMazeObserver class is being loaded");
    }

    /**
     * This method updates the observer about the maze status, prints status messages, the room names, and their contents.
     *
     * @param maze the maze object that is to be observed.
     * @param statusMessages a list of status messages (String) about the maze.
     */
    @Override
    public void update(IMaze maze, List<String> statusMessages) {
        for (String msg : statusMessages) {
            System.out.println(msg);
        }
        for (String room : maze.getRoomNames()) {
            System.out.println(room + ":");
            for (String content : maze.getContents(room)) {
                System.out.println("  Content: " + content);
            }
        }
    }
}
