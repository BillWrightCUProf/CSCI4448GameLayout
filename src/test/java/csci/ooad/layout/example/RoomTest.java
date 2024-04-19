package csci.ooad.layout.example;

import org.junit.jupiter.api.Test;
import java.util.Set;

class RoomTest {

    @Test
    void testOneRoomCreation() {
        Set<Room> rooms = Room.createRoomGrid(1);
        assert rooms.size() == 1;
    }
}