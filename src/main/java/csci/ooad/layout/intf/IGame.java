package csci.ooad.layout.intf;

public interface IGame {
    void attach(IGameObserver observer);
    default void detach(IGameObserver observer) {
        // Implement if needed
    }
}
