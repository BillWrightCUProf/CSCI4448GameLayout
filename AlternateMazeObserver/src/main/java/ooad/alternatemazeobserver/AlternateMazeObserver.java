package ooad.alternatemazeobserver;


import ooad.gameobserver.intf.IGameObserver;
import ooad.gameobserver.intf.IMaze;
import ooad.gameobserver.intf.IMazeSubject;

/**
 * This class represents an alternative observer for a maze,
 * The observer gets updates about the maze and its status.
 */
public class AlternateMazeObserver implements IGameObserver {
    IMazeSubject mazeSubject;

    public AlternateMazeObserver(IMazeSubject mazeSubject) {
        this.mazeSubject = mazeSubject;
    }

    static {
        System.out.println("AlternateMazeObserver class is being loaded");
    }

    @Override
    public void update(String message) {
        update(null, message);
    }

    /**
     * This method updates the observer about the maze status, prints status messages, the room names, and their contents.
     *
     * @param maze           the maze object that is to be observed.
     * @param statusMessage a list of status messages (String) about the maze.
     */
    public void update(IMaze maze, String statusMessage) {
        System.out.println(statusMessage);
        if (maze == null) {
            return;
        }

        for (String room : maze.getRoomNames()) {
            System.out.println(room + ":");
            for (String content : maze.getArtifacts(room)) {
                System.out.println("  Content: " + content);
            }
        }
    }
}
