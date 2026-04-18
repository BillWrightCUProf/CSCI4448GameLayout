package ooad.gameobserver.intf;


import java.util.List;

public interface IGame {
    void attach(IGameObserver observer);
    default void attach(IGameObserver observer, List<EventType> events) {
        attach(observer);
    };
    default void detach(IGameObserver observer) {
        // Implement if needed
    }
}
