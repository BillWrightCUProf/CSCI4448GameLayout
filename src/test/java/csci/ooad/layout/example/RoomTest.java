package csci.ooad.layout.example;

import org.junit.jupiter.api.Test;
import java.util.List;

class RoomTest {

    @Test
    void testOneRoomCreation() {
        List<Room> rooms = Room.createRoomGrid(1);
        assert rooms.size() == 1;
    }
}