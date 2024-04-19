package csci.ooad.layout.intf;

public interface IMazeObserver {
    default void update(IMaze maze) {
        update(maze, "");
    }
    void update(IMaze maze, String statusMessage);
    default void paintToFile(String filePath) {

    };
}
