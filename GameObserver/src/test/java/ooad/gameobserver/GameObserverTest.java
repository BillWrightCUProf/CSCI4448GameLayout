package ooad.gameobserver;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameObserverTest {

    @Test
    void testUpdate() {
        GameObserver gameObserver = new GameObserver();
        gameObserver.update("Game is over");
    }

}