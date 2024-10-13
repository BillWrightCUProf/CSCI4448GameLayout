package csci.ooad.layout.intf;

import java.util.ArrayList;
import java.util.List;

public interface IMazeSubject {
    List<IMazeObserver> observers = new ArrayList<>();
    default void attach(IMazeObserver observer) {
        observers.add(observer);
    }
    default void notifyObservers(String statusMessage) {
        for (IMazeObserver observer : observers) {
            observer.update(getMaze(), statusMessage);
        }
    }
    default void notifyObservers(List<String> statusMessages) {
        for (IMazeObserver observer : observers) {
            observer.update(getMaze(), statusMessages);
        }
    }

    IMaze getMaze();
}
