package csci.ooad.layout;

import csci.ooad.layout.example.ExampleMaze;
import csci.ooad.layout.intf.IMaze;
import csci.ooad.layout.intf.ImageFactory;
import csci.ooad.layout.intf.MazeObserver;
import org.junit.jupiter.api.Test;

class MazeObserverTest {

    @Test
    void test4RoomRadialLayouts() {
        MazeObserver mazeObserver = MazeObserver.getNewBuilder("Adventure Game - Radial Layout")
                .useRadialLayoutStrategy()
                .build();
        mazeObserver.update(ExampleMaze.createRoomGrid(2));
        mazeObserver.paintToFile("sampleLayouts/FourRoomRadialLayout");
    }

    @Test
    void test9RoomRadialLayouts() {
        MazeObserver mazeObserver = MazeObserver.getNewBuilder("Adventure Game - Radial Layout")
                .useRadialLayoutStrategy()
                .build();
        mazeObserver.update(ExampleMaze.createRoomGrid(3));
        mazeObserver.paintToFile("sampleLayouts/NineRoomRadialLayout");
    }

    @Test
    void test16RoomRadialLayouts() {
        MazeObserver mazeObserver = MazeObserver.getNewBuilder("Adventure Game - Radial Layout")
                .useRadialLayoutStrategy()
                .build();
        mazeObserver.update(ExampleMaze.createRoomGrid(4));
        mazeObserver.paintToFile("sampleLayouts/SixteenRoomRadialLayout");
    }

    @Test
    void testGrid2x2Layout() {
        MazeObserver mazeObserver = MazeObserver.getNewBuilder("Adventure Game - Grid Layout")
                .useGridLayoutStrategy()
                .build();

        mazeObserver.update(ExampleMaze.createRoomGrid(4));
        mazeObserver.paintToFile("sampleLayouts/FourRoomRadialLayout");
    }

    @Test
    void testGrid3x3Layout() {
        MazeObserver mazeObserver = MazeObserver.getNewBuilder("Adventure Game - Grid Layout")
                .useGridLayoutStrategy()
                .build();

        mazeObserver.update(ExampleMaze.createRoomGrid(9));
        mazeObserver.paintToFile("sampleLayouts/NineRoomRadialLayout");
    }

    @Test
    void testGrid4x4Layout() {
        MazeObserver mazeObserver = MazeObserver.getNewBuilder("Adventure Game - Grid Layout")
                .useGridLayoutStrategy()
                .setRoomDimension(200)
                .setDimension(1000)
                .build();

        mazeObserver.update(ExampleMaze.createRoomGrid(16));
        mazeObserver.paintToFile("sampleLayouts/SixteenRoomRadialLayout");
    }

    @Test
    void testInlineLayout() {
        MazeObserver mazeObserver = MazeObserver.getNewBuilder("Adventure Game - Grid Layout")
                .useInLineLayoutStrategy()
                .setRoomDimension(200)
                .setDimension(1000)
                .build();

        mazeObserver.update(ExampleMaze.createRoomGrid(4));
        mazeObserver.paintToFile("sampleLayouts/fourRoomInlineLayout");
    }

    @Test
    void test1RoomLayout() {
        MazeObserver mazeObserver = MazeObserver.getNewBuilder("Adventure Game - Grid Layout")
                .useGridLayoutStrategy()
                .build();

        mazeObserver.update(ExampleMaze.createFullyConnectedRooms(1, true));
        mazeObserver.paintToFile("sampleLayouts/FullyConnected1RoomLayout.radial");
    }

    @Test
    void test2RoomOneWayLayout() {
        MazeObserver mazeObserver = MazeObserver.getNewBuilder("Adventure Game - Grid Layout")
                .useGridLayoutStrategy()
                .build();
        mazeObserver.update(ExampleMaze.createFullyConnectedRooms(2, false));
        mazeObserver.paintToFile("sampleLayouts/FullyConnected2RoomOneWayLayout.radial");
    }

    @Test
    void test3RoomOneWayLayout() {
        MazeObserver mazeObserver = MazeObserver.getNewBuilder("Adventure Game - Grid Layout")
                .useGridLayoutStrategy()
                .build();
        mazeObserver.update(ExampleMaze.createFullyConnectedRooms(3, false));
        mazeObserver.paintToFile("sampleLayouts/FullyConnected3RoomOneWayLayout.radial");
    }

    @Test
    void test5RoomOneWayLayout() {
        MazeObserver mazeObserver = MazeObserver.getNewBuilder("Adventure Game - Grid Layout")
                .useGridLayoutStrategy()
                .build();
        mazeObserver.update(ExampleMaze.createFullyConnectedRooms(5, false));
        mazeObserver.paintToFile("sampleLayouts/FullyConnected5RoomOneWayLayout.radial");
    }

    @Test
    void testFullyConnected2RoomsLayout() {
        MazeObserver mazeObserver = MazeObserver.getNewBuilder("Adventure Game - Grid Layout")
                .useRadialLayoutStrategy()
                .build();

        mazeObserver.update(ExampleMaze.createFullyConnectedRooms(2));
        mazeObserver.paintToFile("sampleLayouts/FullyConnected2RoomLayout.radial");
    }

    void testFullyConnected3RoomsLayouts() {
        MazeObserver mazeObserver = MazeObserver.getNewBuilder("Adventure Game - Grid Layout")
                .useRadialLayoutStrategy()
                .build();

        mazeObserver.update(ExampleMaze.createFullyConnectedRooms(3));
        mazeObserver.paintToFile("sampleLayouts/FullyConnected3RoomLayout.radial");
    }

    void testFullyConnected3RoomsOneWayLayouts() {
        MazeObserver mazeObserver = MazeObserver.getNewBuilder("Adventure Game - Grid Layout")
                .useRadialLayoutStrategy()
                .build();

        mazeObserver.update(ExampleMaze.createFullyConnectedRooms(3, false));
        mazeObserver.paintToFile("sampleLayouts/FullyConnectedOneWay3RoomLayout.radial");
    }

    void testFullyConnected4RoomsLayouts() {
        MazeObserver mazeObserver = MazeObserver.getNewBuilder("Adventure Game - Grid Layout")
                .useRadialLayoutStrategy()
                .build();

        mazeObserver.update(ExampleMaze.createFullyConnectedRooms(4));
        mazeObserver.paintToFile("sampleLayouts/FullyConnected4RoomLayout.radial");
    }

    void testFullyConnected9RoomsLayouts() {
        MazeObserver mazeObserver = MazeObserver.getNewBuilder("Adventure Game - Grid Layout")
                .useRadialLayoutStrategy()
                .build();

        mazeObserver.update(ExampleMaze.createFullyConnectedRooms(9));
        mazeObserver.paintToFile("sampleLayouts/FullyConnected9RoomLayout.radial");
    }

    void testFullyConnected9RoomsOneWayLayouts() {
        MazeObserver mazeObserver = MazeObserver.getNewBuilder("Adventure Game - Grid Layout")
                .useRadialLayoutStrategy()
                .build();

        mazeObserver.update(ExampleMaze.createFullyConnectedRooms(9, false));
        mazeObserver.paintToFile("sampleLayouts/FullyConnectedOneWay9RoomLayout.radial");
    }

    @Test
    void testFullyConnectedRoomsWithGridLayouts() {
        MazeObserver mazeObserver = MazeObserver.getNewBuilder("Adventure Game - Grid Layout")
                .useGridLayoutStrategy()
                .useSquareRooms()
                .useImageRooms()
                .build();

        mazeObserver.update(ExampleMaze.createFullyConnectedRooms(4));
        mazeObserver.paintToFile("sampleLayouts/FullyConnected4RoomLayout.grid");

        mazeObserver.update(ExampleMaze.createFullyConnectedRooms(16));
        mazeObserver.paintToFile("sampleLayouts/FullyConnected16RoomLayout.grid");
    }

    @Test
    void testFullyConnectedRoomsWithCircularLayout() {
        MazeObserver mazeObserver = MazeObserver.getNewBuilder("Polymorphia")
                .useRadialLayoutStrategy()
                .useGridLayoutStrategy()
                .useImageRooms()
                .setDelayInSecondsAfterUpdate(5)
                .setRoomDimension(200)
                .setDimension(1500)
                .build();

        // This was only for local testing, as the test file won't exist on the CI machine
        //  ImageFactory.getInstance().loadCustomImage("/tmp/test-circle.png");

        IMaze maze = ExampleMaze.createFullyConnectedRooms(7, false);
        mazeObserver.update(maze, java.util.List.of("After turn 1", "# of Adventurers: 2", "# of Creatures: 1"));
        mazeObserver.update(maze, java.util.List.of("After turn 2", "# of Adventurers: 1", "# of Creatures: 0", "Food: burger, cheese, Coke"));
        mazeObserver.paintToFile("sampleLayouts/FullyConnectedRadial4RoomLayout.grid");
    }

}