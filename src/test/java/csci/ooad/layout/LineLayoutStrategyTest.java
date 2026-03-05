package csci.ooad.layout;

import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LineLayoutStrategyTest {

    @Test
    void testLayout() {
        IRoomLayoutStrategy strategy = new LineLayoutStrategy();
        Set<String> rooms = new HashSet<>(List.of("Room1", "Room2", "Room3", "Room4"));
        Map<String, Point> roomCenters = strategy.calculateRoomLocations(rooms, 1000, 1000, 200);
        List<Point> sorted = new ArrayList<>(roomCenters.values());
        sorted.sort(Comparator.comparing(p -> p.getX()));

        System.out.println(roomCenters);
        assertEquals(new Point(100 + LineLayoutStrategy.MARGIN, 500), sorted.get(0));
    }
}