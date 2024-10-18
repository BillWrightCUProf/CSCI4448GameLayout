package csci.ooad.layout;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LineLayoutStrategy implements IRoomLayoutStrategy {

    @Override
    public Map<String, Point> calculateRoomLocations(Set<String> roomNames, Integer panelWidth, Integer panelHeight, Integer roomWidth) {
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

        // First location in the middle of the screen on the far left
        Point currentLocation = new Point(roomWidth / 2, panelHeight / 2);
        Integer rowSpacing = (panelWidth - roomWidth) / (roomNames.size() - 1);

        for (String currentRoomName : roomNames) {
            roomLocations.put(currentRoomName, currentLocation);
            currentLocation = new Point(currentLocation.x + rowSpacing, panelHeight / 2);
        }

        return roomLocations;
    }

}
