package csci.ooad.layout;

import csci.ooad.layout.example.ExampleMaze;
import csci.ooad.layout.intf.MazeObserver;
import org.junit.jupiter.api.Test;

class MazeObserverTest {

    @Test
    void testRadialLayouts() {
        MazeObserver mazeObserver = MazeObserver.getNewBuilder("Arcane Adventure Game - Radial Layout")
                .useRadialLayoutStrategy()
                .build();

        mazeObserver.update(ExampleMaze.createRoomGrid(2));
        mazeObserver.paintToFile("sampleLayouts/testFourRoomRadialLayout");

        mazeObserver.update(ExampleMaze.createRoomGrid(3));
        mazeObserver.paintToFile("sampleLayouts/testNineRoomRadialLayout");

        mazeObserver.update(ExampleMaze.createRoomGrid(4));
        mazeObserver.paintToFile("sampleLayouts/testSixteenRoomRadialLayout");
    }

    @Test
    void testGridLayouts() {
        MazeObserver mazeObserver = MazeObserver.getNewBuilder("Arcane Adventure Game - Grid Layout")
                .useGridLayoutStrategy()
                .build();

        mazeObserver.update(ExampleMaze.createRoomGrid(2));
        mazeObserver.paintToFile("sampleLayouts/testFourRoomRadialLayout");

        mazeObserver.update(ExampleMaze.createRoomGrid(3));
        mazeObserver.paintToFile("sampleLayouts/testNineRoomRadialLayout");

        mazeObserver.update(ExampleMaze.createRoomGrid(4));
        mazeObserver.paintToFile("sampleLayouts/testSixteenRoomRadialLayout");
    }

    @Test
    void test2RoomLayouts() {
        MazeObserver mazeObserver = MazeObserver.getNewBuilder("Arcane Adventure Game - Grid Layout")
                .useGridLayoutStrategy()
                .build();

        mazeObserver.update(ExampleMaze.createFullyConnectedRooms(1, true));
        mazeObserver.paintToFile("sampleLayouts/testFullyConnected2RoomLayout.radial");

        mazeObserver.update(ExampleMaze.createFullyConnectedRooms(2, false));
        mazeObserver.paintToFile("sampleLayouts/testFullyConnected2RoomOneWayLayout.radial");

        mazeObserver.update(ExampleMaze.createFullyConnectedRooms(3, false));
        mazeObserver.paintToFile("sampleLayouts/testFullyConnected2RoomOneWayLayout.radial");

        mazeObserver.update(ExampleMaze.createFullyConnectedRooms(5, false));
        mazeObserver.paintToFile("sampleLayouts/testFullyConnected2RoomOneWayLayout.radial");
    }

    @Test
    void testFullyConnectedRoomsLayouts() {
        MazeObserver mazeObserver = MazeObserver.getNewBuilder("Arcane Adventure Game - Grid Layout")
                .useRadialLayoutStrategy()
                .build();

        mazeObserver.update(ExampleMaze.createFullyConnectedRooms(2));
        mazeObserver.paintToFile("sampleLayouts/testFullyConnected2RoomLayout.radial");

        mazeObserver.update(ExampleMaze.createFullyConnectedRooms(3));
        mazeObserver.paintToFile("sampleLayouts/testFullyConnected3RoomLayout.radial");

        mazeObserver.update(ExampleMaze.createFullyConnectedRooms(3, false));
        mazeObserver.paintToFile("sampleLayouts/testFullyConnectedOneWay3RoomLayout.radial");

        mazeObserver.update(ExampleMaze.createFullyConnectedRooms(4));
        mazeObserver.paintToFile("sampleLayouts/testFullyConnected4RoomLayout.radial");

        mazeObserver.update(ExampleMaze.createFullyConnectedRooms(9));
        mazeObserver.paintToFile("sampleLayouts/testFullyConnected9RoomLayout.radial");

        mazeObserver.update(ExampleMaze.createFullyConnectedRooms(9, false));
        mazeObserver.paintToFile("sampleLayouts/testFullyConnectedOneWay9RoomLayout.radial");
    }

    @Test
    void testFullyConnectedRoomsWithGridLayouts() {
        MazeObserver mazeObserver = MazeObserver.getNewBuilder("Arcane Adventure Game - Grid Layout")
                .useGridLayoutStrategy()
                .build();

        mazeObserver.update(ExampleMaze.createFullyConnectedRooms(4));
        mazeObserver.paintToFile("sampleLayouts/testFullyConnected4RoomLayout.grid");

        mazeObserver.update(ExampleMaze.createFullyConnectedRooms(16));
        mazeObserver.paintToFile("sampleLayouts/testFullyConnected16RoomLayout.grid");
    }
}