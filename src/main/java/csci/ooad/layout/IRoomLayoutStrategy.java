package csci.ooad.layout;

// What about this coupling to awt? Is it necessary? Is it okay?
import java.awt.Point;
import java.util.List;
import java.util.Map;

public interface IRoomLayoutStrategy {
    Map<IConnectedRoom, Point> calculateRoomLocations(List<IConnectedRoom> rooms,
                                            Integer panelWidth, Integer panelHeight,
                                            Integer roomWidth, Integer roomHeight);
}
