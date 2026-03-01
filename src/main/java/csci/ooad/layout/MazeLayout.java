package csci.ooad.layout;

import csci.ooad.layout.intf.IMaze;

import java.util.*;

/**
 * Creates a layout strategy from a maze.
 * Uses force-directed layout which handles any topology automatically.
 *
 * Usage:
 *   IRoomLayoutStrategy strategy = MazeLayoutDetector.detectAndCreate(maze);
 */
public class MazeLayout {

    public static Map<String, Set<String>> buildAdjacency(IMaze maze) {
        Map<String, Set<String>> adj = new HashMap<>();
        for (String room : maze.getRoomNames()) {
            adj.put(room, new HashSet<>(maze.getNeighborsOf(room)));
        }
        return adj;
    }
}
