package csci.ooad.layout;

import java.util.List;

public interface IMazeObserver {
    default void update(IMaze maze) {
        update(maze, "");
    }
    void update(IMaze maze, String statusMessage);
    void paintToFile(String filePath);
}
