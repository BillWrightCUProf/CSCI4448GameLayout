package ooad.alternatemazeobserver;

import ooad.alternatemazeobserver.example.ExampleSubject;
import ooad.gameobserver.intf.IGameObserver;
import ooad.gameobserver.intf.IMazeSubject;
import org.junit.jupiter.api.Test;

class AlternateGraphicalMazeObserverTest {

    @Test
    void testAlternateMazeObserver() {
        AlternateGraphicalMazeObserver.DEFAULT_UPDATE_DELAY_IN_SECONDS = 2;
        IMazeSubject subject = ExampleSubject.createRoomGrid(4);
        IGameObserver observer = new AlternateGraphicalMazeObserver(subject);

        observer.update("After turn 1\n\t# of Adventurers: 2\n\t# of Creatures: 1");
        observer.update("After turn 2\n\t# of Adventurers: 1\n\t# of Creatures: 0\n\tFood: burger, cheese, Coke");
        observer.update("Game ended");
    }

}