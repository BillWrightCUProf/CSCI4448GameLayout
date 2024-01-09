package org.example;

// What about this coupling to awt? Is it necessary? Is it okay?
import java.awt.Point;
import java.util.List;
import java.util.Map;

public interface RoomLayoutStrategy {
    Map<Room, Point> calculateRoomLocations(List<Room> rooms,
                                            Integer panelWidth, Integer panelHeight,
                                            Integer roomWidth, Integer roomHeight);
}
