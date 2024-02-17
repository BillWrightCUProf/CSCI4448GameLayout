package csci.ooad.layout;

// What about this coupling to awt? Is it necessary? Is it okay?
import java.awt.Point;
import java.util.List;
import java.util.Map;

public interface IRoomLayoutStrategy {
    Map<IRoom, Point> calculateRoomLocations(List<IRoom> rooms,
                                             Integer panelWidth, Integer panelHeight,
                                             Integer roomWidth, Integer roomHeight);
}
