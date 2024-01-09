package org.example;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridLayoutStrategy implements RoomLayoutStrategy {
    @Override
    public Map<Room, Point> calculateRoomLocations(List<Room> rooms,
                                                   Integer panelWidth, Integer panelHeight,
                                                   Integer roomWidth, Integer roomHeight) {
        Map<Room, Point> roomLocations = new HashMap<>();

        Integer dimension = Math.toIntExact(Math.round(Math.sqrt(rooms.size())));
        Integer rowSpacing = (panelHeight - roomHeight * 2) / (dimension - 1);
        Integer colSpacing = (panelWidth - roomWidth * 2) / (dimension - 1);
        Point currentLocation = new Point(roomWidth, roomHeight);
        Integer roomCount = 0;
        for (Room currentRoom : rooms) {
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
