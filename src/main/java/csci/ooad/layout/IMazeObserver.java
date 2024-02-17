package csci.ooad.layout;

import java.util.List;

public interface IMazeObserver {
    void update(IMaze maze);
    void paintToFile(String filePath);
}
