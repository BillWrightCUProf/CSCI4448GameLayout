package csci.ooad.layout;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GridLayoutStrategy implements IRoomLayoutStrategy {
    @Override
    public Map<String, Point> calculateRoomLocations(Set<String> roomNames, Integer panelWidth, Integer roomWidth) {
        // Define the upper right location so that the room just fits in this corner
        // --------------------------------------
        //|                                     |
        //|   *                             *   |
        //|                                     |
        //|                                     |
        //|                                     |
        //|                                     |
        //|                                     |
        //|                                     |
        //|   *                             *   |
        //|                                     |
        // --------------------------------------
        Map<String, Point> roomLocations = new HashMap<>();

        // We are left with (panelWidth - roomWidth) to space out the remaining rooms.
        // If the roomWidth is too large, they will overlap, but should fill the entire space.
        // Subsequent spacing is (panelWidth - roomWidth) / (dimension - 1)
        // where dimension is the number of rooms in a row or column.
        Integer dimension = Math.toIntExact((long) Math.ceil(Math.sqrt(roomNames.size())));
        Point currentLocation = new Point(roomWidth/2, roomWidth/2);

        // I take the max of dimension-1 and 1 just in case there is only one room in the maze
        Integer rowSpacing = (panelWidth - roomWidth) / Math.max(dimension - 1, 1);
        Integer colSpacing = rowSpacing;

        Integer roomCount = 0;
        for (String currentRoomName : roomNames) {
            roomLocations.put(currentRoomName, currentLocation);
            roomCount += 1;
            if (roomCount % dimension == 0) {
                currentLocation = new Point(roomWidth/2, currentLocation.y + rowSpacing);
            } else {
                currentLocation = new Point(currentLocation.x + colSpacing, currentLocation.y);
            }
        }
        return roomLocations;
    }
}
