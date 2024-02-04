package csci.ooad.layout;

import java.util.List;

public interface IMazeObserver {
    void update(List<IConnectedRoom> rooms);
    void paintToFile(String filePath);
}
