package csci.ooad.layout;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridLayoutStrategy implements IRoomLayoutStrategy {
    @Override
    public Map<IConnectedRoom, Point> calculateRoomLocations(List<IConnectedRoom> rooms,
                                                   Integer panelWidth, Integer panelHeight,
                                                   Integer roomWidth, Integer roomHeight) {
        Map<IConnectedRoom, Point> roomLocations = new HashMap<>();

        Integer dimension = Math.toIntExact(Math.round(Math.sqrt(rooms.size())));
        Point currentLocation = new Point(roomWidth, roomHeight);
        if (rooms.size() == 1) {
            roomLocations.put(rooms.get(0), currentLocation);
            return roomLocations;
        }

        Integer rowSpacing = (panelHeight - roomHeight * 2) / (dimension - 1);
        Integer colSpacing = (panelWidth - roomWidth * 2) / (dimension - 1);
        Integer roomCount = 0;
        for (IConnectedRoom currentRoom : rooms) {
            roomLocations.put(currentRoom, currentLocation);
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
