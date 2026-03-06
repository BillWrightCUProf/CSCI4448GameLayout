package csci.ooad.layout.alternate;

import csci.ooad.layout.intf.IMaze;
import csci.ooad.layout.intf.IGameObserver;
import csci.ooad.layout.intf.IMazeSubject;

import java.util.List;

/**
 * This class represents an alternative observer for a maze,
 * The observer gets updates about the maze and its status.
 */
public class AlternateMazeObserver implements IGameObserver {
    IMazeSubject mazeSubject;

    AlternateMazeObserver(IMazeSubject mazeSubject) {
        this.mazeSubject = mazeSubject;
    }

    static {
        System.out.println("AlternateMazeObserver class is being loaded");
    }

    @Override
    public void update(String message) {
        update(mazeSubject.getMaze(), message);
    }

    /**
     * This method updates the observer about the maze status, prints status messages, the room names, and their contents.
     *
     * @param maze           the maze object that is to be observed.
     * @param statusMessages a list of status messages (String) about the maze.
     */
    public void update(IMaze maze, String statusMessage) {
        System.out.println(statusMessage);
        for (String room : maze.getRoomNames()) {
            System.out.println(room + ":");
            for (String content : maze.getArtifacts(room)) {
                System.out.println("  Content: " + content);
            }
        }
    }
}
