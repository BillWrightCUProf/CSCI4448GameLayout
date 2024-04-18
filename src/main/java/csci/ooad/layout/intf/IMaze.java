package csci.ooad.layout.intf;

import java.util.List;
import java.util.Set;

public interface IMaze {
    Set<String> getRooms();
    Set<String> getNeighborsOf(String room);
    List<String> getContents(String room);
}
