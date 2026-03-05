package csci.ooad.layout;

import csci.ooad.layout.example.ExampleMaze;
import csci.ooad.layout.intf.IMaze;
import csci.ooad.layout.intf.ImageFactory;
import csci.ooad.layout.intf.MazeObserver;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.lang.Thread.sleep;

class MazeObserverTest {

    @Test
    void testRadialLayouts() {
        for (int numRooms : List.of(4, 9, 16)) {
            MazeObserver mazeObserver = MazeObserver.getNewBuilder("Adventure Game - Radial Layout")
                    .useRadialLayoutStrategy()
                    .setDelayInSecondsAfterUpdate(3)
                    .setRoomDimension(1000/numRooms)
                    .setDimension(1000)
                    .build();
            mazeObserver.update(ExampleMaze.createRoomGrid(numRooms));
            mazeObserver.paintToFile("sampleLayouts/" + numRooms + "RoomRadialLayout");
        }
    }

    @Test
    void testGridLayouts() {
        for (int numRooms : List.of(1, 4, 7, 9, 12, 16)) {
            MazeObserver mazeObserver = MazeObserver.getNewBuilder("Adventure Game - Radial Layout")
                    .useGridLayoutStrategy()
                    .setDelayInSecondsAfterUpdate(3)
                    .setRoomDimension(1000/numRooms)
                    .setDimension(1000)
                    .build();
            mazeObserver.update(ExampleMaze.createRoomGrid(numRooms));
            mazeObserver.paintToFile("sampleLayouts/" + numRooms + "RoomGridLayout");
        }
    }

    @Test
    void testInLineLayouts() {
        for (int numRooms : List.of(1, 3, 5, 7, 9)) {
            MazeObserver mazeObserver = MazeObserver.getNewBuilder("Adventure Game - Radial Layout")
                    .useInLineLayoutStrategy()
                    .setDelayInSecondsAfterUpdate(3)
                    .setRoomDimension(1000/numRooms)
                    .setDimension(1000)
                    .build();
            mazeObserver.update(ExampleMaze.createRoomGrid(numRooms));
            mazeObserver.paintToFile("sampleLayouts/" + numRooms + "RoomGridLayout");
        }
    }

    @Test
    void testOneWayGridLayouts() {
        for (int numRooms : List.of(2, 3, 5)) {
            MazeObserver mazeObserver = MazeObserver.getNewBuilder("Adventure Game - Radial Layout")
                    .useGridLayoutStrategy()
                    .setDelayInSecondsAfterUpdate(3)
                    .setRoomDimension(1000/numRooms)
                    .setDimension(1000)
                    .build();
            mazeObserver.update(ExampleMaze.createFullyConnectedRooms(numRooms, false));
            mazeObserver.paintToFile("sampleLayouts/" + numRooms + "RoomGridLayout");
        }
    }

    @Test
    void testFullyConnectedRadialLayout() {
        for (int numRooms : List.of(2, 3, 4, 9)) {
            for (boolean twoWayConnections : List.of(true, false)) {
                MazeObserver mazeObserver = MazeObserver.getNewBuilder("Adventure Game - Radial Layout")
                        .useRadialLayoutStrategy()
                        .setDelayInSecondsAfterUpdate(3)
                        .setRoomDimension(1000 / numRooms)
                        .setDimension(1000)
                        .build();
                mazeObserver.update(ExampleMaze.createFullyConnectedRooms(numRooms, twoWayConnections));
                mazeObserver.paintToFile("sampleLayouts/" + numRooms + "RoomGrid" + twoWayConnections + "Layout");
            }
        }
    }

    @Test
    void testSmartLayoutWithFullyConnectedRooms() {
        MazeObserver mazeObserver = MazeObserver.getNewBuilder("Adventure Game - Smart Layout")
                .useSmartLayoutStrategy()
                .useImageRooms()
                .setDimension(1200)
                .setWidth(1600)
                .setHeight(700)
                .setRoomDimension(100)
                .setDelayInSecondsAfterUpdate(1)
                .build();

        mazeObserver.update(ExampleMaze.createFullyConnectedRooms(4));
        mazeObserver.paintToFile("sampleLayouts/FullyConnected4RoomLayout.smart");

        mazeObserver.update(ExampleMaze.createFullyConnectedRooms(16));
        mazeObserver.paintToFile("sampleLayouts/FullyConnected16RoomLayout.smart");
    }

    @Test
    void testFullyConnectedRoomsWithCircularLayout() {
        MazeObserver mazeObserver = MazeObserver.getNewBuilder("Polymorphia")
                .useGridLayoutStrategy()
                .useImageRooms()
                .setDelayInSecondsAfterUpdate(5)
                .setRoomDimension(350)
                .setWidth(3000)
                .setHeight(1500)
                .setRatioOfSpacingToRoomWidth(0.7)
                .build();

        // This was only for local testing, as the test file won't exist on the CI machine
        //  ImageFactory.getInstance().loadCustomImage("/tmp/test-circle.png");

        IMaze maze = ExampleMaze.createFullyConnectedRooms(7, false);
        mazeObserver.update(maze, java.util.List.of("After turn 1", "# of Adventurers: 2", "# of Creatures: 1"));
        mazeObserver.update(maze, java.util.List.of("After turn 2", "# of Adventurers: 1", "# of Creatures: 0", "Food: burger, cheese, Coke"));
        mazeObserver.paintToFile("sampleLayouts/FullyConnectedRadial4RoomLayout.grid");
    }

}