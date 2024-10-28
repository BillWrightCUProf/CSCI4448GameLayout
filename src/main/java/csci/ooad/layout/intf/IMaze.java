package csci.ooad.layout.intf;

import java.util.List;
import java.util.Set;

public interface IMaze {
    Set<String> getRoomNames();
    Set<String> getNeighborsOf(String roomName);
    List<String> getContents(String roomName);
}
