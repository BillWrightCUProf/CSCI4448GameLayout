package org.example;

import java.awt.Point;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RadialLayoutStrategy implements RoomLayoutStrategy {
    @Override
    public Map<Room, Point> calculateRoomLocations(List<Room> rooms, Integer panelWidth, Integer panelHeight, Integer roomWidth, Integer roomHeight) {
        Map<Room, Point> roomLocations = new HashMap<>();

        Point center = new Point(panelWidth/2, panelHeight/2);

        Integer layoutRadius = Math.min(panelWidth, panelHeight) / 2 - roomWidth;
        Double radialInterval = 2 * Math.PI / rooms.size();
        Double currentAngle = 0.0;
        for (Room currentRoom : rooms) {
            Integer x = Math.toIntExact(Math.round(center.x + layoutRadius * Math.cos(currentAngle)));
            Integer y = Math.toIntExact(Math.round(center.y - layoutRadius * Math.sin(currentAngle)));
            Point roomLocation = new Point(x, y);
            roomLocations.put(currentRoom, roomLocation);
            currentAngle += radialInterval;
        }

        return roomLocations;
    }
}
