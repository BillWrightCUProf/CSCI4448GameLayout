package csci.ooad.layout.intf;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface IMazeObserver {
    default void update(IMaze maze) {
        update(maze, "");
    }
    default void update(IMaze maze, String statusMessages) {
        update(maze, Collections.singletonList(statusMessages));
    }
    void update(IMaze maze, List<String> statusMessages);

    default void paintToFile(String filePath) {
    }
}
