package csci.ooad.layout;

import csci.ooad.layout.example.ExampleMaze;
import csci.ooad.layout.example.Room;
import csci.ooad.layout.intf.IMaze;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LineLayoutStrategyTest {

    @Test
    void testLayout() {
        IMaze maze = new ExampleMaze(Room.createConnectedRooms("Room1", "Room2", "Room3", "Room4"));

        IRoomLayoutStrategy strategy = new LineLayoutStrategy();
        Map<String, Point> roomCenters = strategy.calculateRoomLocations(maze, 1000, 1000, 200);
        List<Point> sorted = new ArrayList<>(roomCenters.values());
        sorted.sort(Comparator.comparing(p -> p.getX()));

        System.out.println(roomCenters);
        assertEquals(new Point(100 + LineLayoutStrategy.MARGIN, 500 + LineLayoutStrategy.MARGIN), sorted.get(0));
    }
}