package csci.ooad.layout;

import java.util.List;

public interface IConnectedRoom {
    String getName();
    List<String> getContents();
    List<IConnectedRoom> getNeighbors();
}
