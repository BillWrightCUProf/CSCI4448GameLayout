package csci.ooad.layout;

import csci.ooad.layout.example.ExampleMaze;
import csci.ooad.layout.example.ExampleSubject;
import csci.ooad.layout.intf.IMazeSubject;
import csci.ooad.layout.intf.MazeObserver;
import org.junit.jupiter.api.Test;

import java.util.List;

class MazeObserverTest {
    @Test
    void testRadialLayouts() {
        for (int numRooms : List.of(4, 9, 16)) {
            IMazeSubject mazeSubject = ExampleSubject.createRoomGrid(numRooms);
            MazeObserver mazeObserver = MazeObserver.getNewBuilder(mazeSubject, "Adventure Game - Radial Layout")
                    .useRadialLayoutStrategy()
                    .setDelayInSecondsAfterUpdate(3)
                    .setRoomDimension(1000 / numRooms)
                    .setDimension(1000)
                    .build();
            mazeObserver.update("updated");
            mazeObserver.paintToFile("sampleLayouts/" + numRooms + "RoomRadialLayout");
        }
    }

    @Test
    void testGridLayouts() {
        for (int numRooms : List.of(1, 4, 7, 9, 12, 16)) {
            IMazeSubject mazeSubject = ExampleSubject.createRoomGrid(numRooms);
            MazeObserver mazeObserver = MazeObserver.getNewBuilder("Adventure Game - Radial Layout")
                    .useGridLayoutStrategy()
                    .setDelayInSecondsAfterUpdate(3)
                    .setRoomDimension(1000 / numRooms)
                    .setDimension(1000)
                    .build();
            mazeObserver.update(ExampleMaze.createRoomGrid(numRooms));
            mazeObserver.paintToFile("sampleLayouts/" + numRooms + "RoomGridLayout");
        }
    }

    @Test
    void testInLineLayouts() {
        for (int numRooms : List.of(1, 3, 5, 7, 9)) {
            IMazeSubject mazeSubject = ExampleSubject.createRoomGrid(numRooms);
            MazeObserver mazeObserver = MazeObserver.getNewBuilder(mazeSubject, "Adventure Game - Radial Layout")
                    .useInLineLayoutStrategy()
                    .setDelayInSecondsAfterUpdate(3)
                    .setRoomDimension(1000 / numRooms)
                    .setDimension(1000)
                    .build();
            mazeObserver.update(ExampleMaze.createRoomGrid(numRooms));
            mazeObserver.paintToFile("sampleLayouts/" + numRooms + "RoomGridLayout");
        }
    }

    @Test
    void testOneWayGridLayouts() {
        for (int numRooms : List.of(2, 3, 5)) {
            IMazeSubject mazeSubject = ExampleSubject.createRoomGrid(numRooms);

            MazeObserver mazeObserver = MazeObserver.getNewBuilder(mazeSubject,"Adventure Game - Radial Layout")
                    .useGridLayoutStrategy()
                    .setDelayInSecondsAfterUpdate(3)
                    .setRoomDimension(1000 / numRooms)
                    .setDimension(1000)
                    .build();
            mazeObserver.update("updated");
            mazeObserver.paintToFile("sampleLayouts/" + numRooms + "RoomGridLayout");
        }
    }

    @Test
    void testFullyConnectedRadialLayout() {
        for (int numRooms : List.of(2, 3, 4, 9)) {
            IMazeSubject mazeSubject = ExampleSubject.createRoomGrid(numRooms);

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
    void testFullyConnectedRoomsWithCircularLayout() {
        IMazeSubject mazeSubject = ExampleSubject.createRoomGrid(7);

        MazeObserver mazeObserver = MazeObserver.getNewBuilder(mazeSubject,"Polymorphia")
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

        mazeObserver.update("After turn 1\n\t# of Adventurers: 2\n\t# of Creatures: 1");
        mazeObserver.update("After turn 2\n\t# of Adventurers: 1\n\t# of Creatures: 0\n\tFood: burger, cheese, Coke");
        mazeObserver.paintToFile("sampleLayouts/FullyConnectedRadial4RoomLayout.grid");
    }

}