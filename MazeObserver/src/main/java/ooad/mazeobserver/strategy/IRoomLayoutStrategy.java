package ooad.mazeobserver.strategy;

// What about this coupling to awt? Is it necessary? Is it okay?

import ooad.gameobserver.intf.IMaze;

import java.awt.*;
import java.util.Map;
import java.util.Set;

public interface IRoomLayoutStrategy {
    int MARGIN = 10;
    Map<String, Point> calculateRoomLocations(IMaze maze, Integer panelWidth, Integer panelHeight, Integer roomWidth);
}
