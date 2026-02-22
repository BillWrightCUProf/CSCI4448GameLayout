package csci.ooad.layout.intf;

public interface IMazeSubject {
    void attach(IMazeObserver observer);
    default void detach(IMazeObserver observer) {
        // Implement if needed
    }
}
