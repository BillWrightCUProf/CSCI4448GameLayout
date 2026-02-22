package csci.ooad.layout.intf;

import java.util.Collections;
import java.util.List;

/**
 * This interface represents an Observer of a Maze, notified for updates in the Maze state.
 */
public interface IMazeObserver {

    default void update(String statusMessage) {
        update(null, Collections.singletonList(statusMessage));
    }

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
     * @param statusMessage the status message to be displayed
     */
    default void update(IMaze maze, String statusMessage) {
        update(maze, Collections.singletonList(statusMessage));
    }

    /**
     * Updates the specified maze with the given status messages.
     *
     * @param maze the maze to be updated
     * @param statusMessages the status messages to be displayed
     */
    void update(IMaze maze, List<String> statusMessages);

}
