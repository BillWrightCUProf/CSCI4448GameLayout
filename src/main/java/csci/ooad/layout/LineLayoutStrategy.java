package csci.ooad.layout;

import csci.ooad.layout.intf.IMaze;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LineLayoutStrategy implements IRoomLayoutStrategy {

    @Override
    public Map<String, Point> calculateRoomLocations(IMaze maze, Integer panelWidth, Integer panelHeight, Integer roomWidth) {
        // Layout appears like this
        // --------------------------------------
        //|                                     |
        //|                                     |
        //|                                     |
        //|                                     |
        //|   *       *        *         *      |
        //|                                     |
        //|                                     |
        //|                                     |
        // --------------------------------------
        Map<String, Point> roomLocations = new HashMap<>();
        int margin = IRoomLayoutStrategy.MARGIN;

        // First location in the middle of the screen on the far left
        Point currentLocation = new Point(roomWidth / 2 + margin, panelHeight / 2 + margin);
        if (maze.getRoomNames().size() == 1) {
            roomLocations.put(maze.getRoomNames().iterator().next(), currentLocation);
            return roomLocations;
        }

        Integer rowSpacing = (panelWidth - roomWidth - margin*2) / (maze.getRoomNames().size() - 1);

        for (String currentRoomName : maze.getRoomNames()) {
            roomLocations.put(currentRoomName, currentLocation);
            currentLocation = new Point(currentLocation.x + rowSpacing, panelHeight / 2);
        }

        return roomLocations;
    }

}
