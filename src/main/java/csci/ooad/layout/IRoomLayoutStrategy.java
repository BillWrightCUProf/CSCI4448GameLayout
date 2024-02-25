package csci.ooad.layout;

// What about this coupling to awt? Is it necessary? Is it okay?
import java.awt.Point;
import java.util.List;
import java.util.Map;

public interface IRoomLayoutStrategy {
    Map<String, Point> calculateRoomLocations(List<String> roomNames, Integer panelWidth, Integer roomWidth);
}
