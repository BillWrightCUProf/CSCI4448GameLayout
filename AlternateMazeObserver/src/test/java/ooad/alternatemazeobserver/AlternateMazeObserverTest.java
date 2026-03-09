package ooad.alternatemazeobserver;

import ooad.gameobserver.intf.IGameObserver;
import ooad.gameobserver.intf.IMazeSubject;
import ooad.alternatemazeobserver.example.ExampleSubject;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlternateMazeObserverTest {

    @Test
    void testAlternateMazeObserver() {
        IMazeSubject subject = new ExampleSubject();
        IGameObserver observer = new AlternateMazeObserver(subject);
        observer.update("Game is over");
    }

}