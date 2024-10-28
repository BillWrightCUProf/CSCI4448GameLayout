package csci.ooad.layout.intf;

import java.util.List;


public interface IMazeSubject {
    void attach(IMazeObserver observer);
    default void detach(IMazeObserver observer) {
        // Implement if needed
    }
    List<IMazeObserver> getObservers();
    IMaze getMaze();

    default void notifyObservers(String statusMessage) {
        notifyObservers(List.of(statusMessage));
    }

    default void notifyObservers(List<String> statusMessages) {
        for (IMazeObserver observer : getObservers()) {
            observer.update(getMaze(), statusMessages);
        }
    }
}
