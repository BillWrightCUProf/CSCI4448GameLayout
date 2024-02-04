package csci.ooad.layout;

import org.junit.jupiter.api.Test;
import java.util.List;

class RoomTest {

    @Test
    void testOneRoomCreation() {
        List<IConnectedRoom> rooms = Room.createRoomGrid(1);
        assert rooms.size() == 1;
    }
}