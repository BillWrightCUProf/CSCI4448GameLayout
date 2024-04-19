package csci.ooad.layout;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GridLayoutStrategy implements IRoomLayoutStrategy {
    @Override
    public Map<String, Point> calculateRoomLocations(Set<String> roomNames, Integer panelWidth, Integer roomWidth) {
        Map<String, Point> roomLocations = new HashMap<>();

        Integer dimension = Math.toIntExact(Math.round(Math.sqrt(roomNames.size())));
        Point currentLocation = new Point(roomWidth, roomWidth);
        if (roomNames.size() < 3) {
            currentLocation = new Point(panelWidth/2 - roomWidth/2, panelWidth/3);
        }

        Integer rowSpacing = (panelWidth - roomWidth * 2) / (dimension + 1);
        Integer colSpacing = (panelWidth - roomWidth * 2) / dimension;

        Integer roomCount = 0;
        for (String currentRoomName : roomNames) {
            roomLocations.put(currentRoomName, currentLocation);
            roomCount += 1;
            if (roomCount % dimension == 0) {
                currentLocation = new Point(roomWidth, currentLocation.y + rowSpacing);
            } else {
                currentLocation = new Point(currentLocation.x + colSpacing, currentLocation.y);
            }
        }
        return roomLocations;
    }
}
