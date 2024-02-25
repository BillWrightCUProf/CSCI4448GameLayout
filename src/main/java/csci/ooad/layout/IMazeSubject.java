package csci.ooad.layout;

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

    IMaze getMaze();
}
