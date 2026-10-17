package ooad.alternatemazeobserver;

import ooad.alternatemazeobserver.example.ExampleSubject;
import ooad.gameobserver.intf.IGameObserver;
import ooad.gameobserver.intf.IMazeSubject;
import org.junit.jupiter.api.Test;

class AlternateMazeObserverTest {

    @Test
    void testAlternateMazeObserver() {
        IMazeSubject subject = new ExampleSubject();
        IGameObserver observer = new AlternateMazeObserver(subject);
        observer.update("Game is over");
    }

}