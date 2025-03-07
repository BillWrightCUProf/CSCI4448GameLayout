package csci.ooad.layout.intf;

import java.util.Collections;
import java.util.List;

/**
 * This interface represents an Observer of a Maze, notified for updates in the Maze state.
 */
public interface IMazeObserver {

    /**
     * Updates the specified maze.
     *
     * @param maze the maze to be updated
     */
    default void update(IMaze maze) {
        update(maze, "");
    }

    /**
     * Updates the specified maze with the given status message.
     *
     * @param maze the maze to be updated
     * @param statusMessages the status message to be displayed
     */
    default void update(IMaze maze, String statusMessages) {
        update(maze, Collections.singletonList(statusMessages));
    }

    /**
     * Updates the specified maze with the given status messages.
     *
     * @param maze the maze to be updated
     * @param statusMessages the status messages to be displayed
     */
    void update(IMaze maze, List<String> statusMessages);

    /**
     * Paints the maze to a file.
     *
     * @param filePath the file path to paint the maze to
     */
    default void paintToFile(String filePath) {
    }
}
