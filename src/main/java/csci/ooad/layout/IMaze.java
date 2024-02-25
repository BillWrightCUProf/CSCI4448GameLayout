package csci.ooad.layout;

import java.util.List;

public interface IMaze {
    List<String> getRooms();
    List<String> getNeighborsOf(String room);
    List<String> getContents(String room);
}
