package csci.ooad.layout;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RadialLayoutStrategy implements IRoomLayoutStrategy {
    @Override
    public Map<String, Point> calculateRoomLocations(Set<String> roomNames, Integer panelWidth, Integer panelHeight, Integer roomWidth) {
        Map<String, Point> roomLocations = new HashMap<>();

        Point center = new Point(panelWidth/2, panelHeight/2);

        Integer layoutRadius = Math.min(panelWidth, panelHeight) / 2 - (roomWidth/2);
        Double horizontalAdjustmentRatio = (double) (panelWidth - roomWidth) / layoutRadius / 2;
        Double verticalAdjustmentRatio = (double) (panelHeight - roomWidth) / layoutRadius / 2;

        Double radialInterval = 2 * Math.PI / roomNames.size();
        Double currentAngle = 0.0;
        for (String currentRoomName : roomNames) {
            Integer x = Math.toIntExact(Math.round(center.x + layoutRadius * Math.cos(currentAngle) * horizontalAdjustmentRatio));
            Integer y = Math.toIntExact(Math.round(center.y - layoutRadius * Math.sin(currentAngle) * verticalAdjustmentRatio));
            Point roomLocation = new Point(x, y);
            roomLocations.put(currentRoomName, roomLocation);
            currentAngle += radialInterval;
        }

        return roomLocations;
    }
}
