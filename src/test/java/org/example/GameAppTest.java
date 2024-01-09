package org.example;

import org.junit.jupiter.api.Test;

class GameAppTest {

    @Test
    void testSmallGrid() {
        new GameApp(
                "Arcane Adventure Game",
                Room.createRoomGrid(3),
                new RadialLayoutStrategy());
    }
}