package csci.ooad.layout;

import csci.ooad.layout.intf.IMaze;

import java.util.*;

public class MazeLayout {

    public static Map<String, Set<String>> buildAdjacency(IMaze maze) {
        Map<String, Set<String>> adj = new HashMap<>();
        for (String room : maze.getRoomNames()) {
            adj.put(room, new HashSet<>(maze.getNeighborsOf(room)));
        }
        return adj;
    }
}
