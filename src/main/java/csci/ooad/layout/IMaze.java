package csci.ooad.layout;

import java.util.List;

public interface IMaze {
    List<IRoom> getRooms();
    List<IRoom> getNeighborsOf(IRoom room);
}
