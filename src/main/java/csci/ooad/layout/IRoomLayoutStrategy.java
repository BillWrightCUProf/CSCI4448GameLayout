package csci.ooad.layout;

// What about this coupling to awt? Is it necessary? Is it okay?

import csci.ooad.layout.intf.IMaze;

import java.awt.*;
import java.util.Map;

public interface IRoomLayoutStrategy {
    int MARGIN = 10;
    Map<String, Point> calculateRoomLocations(IMaze maze, Integer panelWidth, Integer panelHeight, Integer roomWidth);
//    Map<String, Point> calculateRoomLocations(Set<String> roomNames, Integer panelWidth, Integer panelHeight, Integer roomWidth);
}
