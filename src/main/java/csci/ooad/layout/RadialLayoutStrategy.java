package csci.ooad.layout;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RadialLayoutStrategy implements IRoomLayoutStrategy {
    @Override
    public Map<String, Point> calculateRoomLocations(Set<String> roomNames, Integer panelWidth, Integer roomWidth) {
        Map<String, Point> roomLocations = new HashMap<>();

        Point center = new Point(panelWidth/2, panelWidth/2);

        Integer layoutRadius = panelWidth / 2 - roomWidth;
        Double radialInterval = 2 * Math.PI / roomNames.size();
        Double currentAngle = 0.0;
        for (String currentRoomName : roomNames) {
            Integer x = Math.toIntExact(Math.round(center.x + layoutRadius * Math.cos(currentAngle)));
            Integer y = Math.toIntExact(Math.round(center.y - layoutRadius * Math.sin(currentAngle)));
            Point roomLocation = new Point(x, y);
            roomLocations.put(currentRoomName, roomLocation);
            currentAngle += radialInterval;
        }

        return roomLocations;
    }
}
