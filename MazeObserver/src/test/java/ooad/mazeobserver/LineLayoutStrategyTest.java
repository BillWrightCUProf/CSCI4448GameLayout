package ooad.mazeobserver;

import ooad.gameobserver.intf.IMaze;
import ooad.mazeobserver.example.ExampleMaze;
import ooad.mazeobserver.strategy.IRoomLayoutStrategy;
import ooad.mazeobserver.strategy.LineLayoutStrategy;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


class LineLayoutStrategyTest {

    @Test
    void testLayout() {
        IMaze maze = ExampleMaze.createRoomGrid(4);

        IRoomLayoutStrategy strategy = new LineLayoutStrategy();
        Map<String, Point> roomCenters = strategy.calculateRoomLocations(maze, 1000, 1000, 200);
        List<Point> sorted = new ArrayList<>(roomCenters.values());
        sorted.sort(Comparator.comparing(p -> p.getX()));

        System.out.println(roomCenters);
        assertEquals(new Point(100 + LineLayoutStrategy.MARGIN, 500 + LineLayoutStrategy.MARGIN), sorted.get(0));
    }
}