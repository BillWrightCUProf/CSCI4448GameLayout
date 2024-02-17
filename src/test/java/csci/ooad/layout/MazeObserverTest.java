package csci.ooad.layout;

import csci.ooad.layout.example.ExampleMaze;
import org.junit.jupiter.api.Test;

class MazeObserverTest {

    @Test
    void testRadialLayouts() {
        MazeObserver mazeObserver = new MazeObserver(
                "Arcane Adventure Game - Radial Layout",
                new RadialLayoutStrategy());

        mazeObserver.update(ExampleMaze.createRoomGrid(2));
        mazeObserver.paintToFile("sampleLayouts/testFourRoomRadialLayout");

        mazeObserver.update(ExampleMaze.createRoomGrid(3));
        mazeObserver.paintToFile("sampleLayouts/testNineRoomRadialLayout");

        mazeObserver.update(ExampleMaze.createRoomGrid(4));
        mazeObserver.paintToFile("sampleLayouts/testSixteenRoomRadialLayout");
    }

    @Test
    void testGridLayouts() {
        MazeObserver mazeObserver = new MazeObserver(
                "Arcane Adventure Game - Grid Layout",
                new GridLayoutStrategy());

        mazeObserver.update(ExampleMaze.createRoomGrid(2));
        mazeObserver.paintToFile("sampleLayouts/testFourRoomRadialLayout");

        mazeObserver.update(ExampleMaze.createRoomGrid(3));
        mazeObserver.paintToFile("sampleLayouts/testNineRoomRadialLayout");

        mazeObserver.update(ExampleMaze.createRoomGrid(4));
        mazeObserver.paintToFile("sampleLayouts/testSixteenRoomRadialLayout");
    }

    @Test
    void test2RoomLayouts() {
        MazeObserver mazeObserver = new MazeObserver(
                "Arcane Adventure Game - 2-Room Layout",
                new RadialLayoutStrategy());

        mazeObserver.update(ExampleMaze.createFullyConnectedRooms(2, true));
        mazeObserver.paintToFile("sampleLayouts/testFullyConnected2RoomLayout.radial");

        mazeObserver.update(ExampleMaze.createFullyConnectedRooms(2, false));
        mazeObserver.paintToFile("sampleLayouts/testFullyConnected2RoomOneWayLayout.radial");
    }

    @Test
    void testFullyConnectedRoomsLayouts() {
        MazeObserver mazeObserver = new MazeObserver(
                "Arcane Adventure Game - Radial Layout",
                new RadialLayoutStrategy());

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
        MazeObserver mazeObserver = new MazeObserver(
                "Arcane Adventure Game - Radial Layout",
                new GridLayoutStrategy());

        mazeObserver.update(ExampleMaze.createFullyConnectedRooms(4));
        mazeObserver.paintToFile("sampleLayouts/testFullyConnected4RoomLayout.grid");

        mazeObserver.update(ExampleMaze.createFullyConnectedRooms(16));
        mazeObserver.paintToFile("sampleLayouts/testFullyConnected16RoomLayout.grid");
    }
}