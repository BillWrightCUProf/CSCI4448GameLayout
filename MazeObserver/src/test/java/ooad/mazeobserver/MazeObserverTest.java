package ooad.mazeobserver;

import ooad.gameobserver.intf.IMazeSubject;
import ooad.mazeobserver.example.ExampleSubject;
import org.junit.jupiter.api.Test;

import java.util.List;

class MazeObserverTest {

    @Test
    void testRadialLayouts() {
        for (int numRooms : List.of(4, 9, 16)) {
            IMazeSubject maze = ExampleSubject.createRoomGrid(numRooms);
            MazeObserver mazeObserver = MazeObserver.getNewBuilder(maze,"Adventure Game - Radial Layout")
                    .useRadialLayoutStrategy()
                    .setDelayInSecondsAfterUpdate(3)
                    .setRoomDimension(1000/numRooms)
                    .setDimension(1000)
                    .build();
            mazeObserver.update("updated!");
            mazeObserver.paintToFile("sampleLayouts/" + numRooms + "RoomRadialLayout");
        }
    }

    @Test
    void testGridLayouts() {
        for (int numRooms : List.of(1, 4, 7, 9, 12, 16)) {
            IMazeSubject maze = ExampleSubject.createRoomGrid(numRooms);
            MazeObserver mazeObserver = MazeObserver.getNewBuilder(maze,"Adventure Game - Radial Layout")
                    .useGridLayoutStrategy()
                    .setDelayInSecondsAfterUpdate(3)
                    .setRoomDimension(1000/numRooms)
                    .setDimension(1000)
                    .build();
            mazeObserver.update("updated!");
            mazeObserver.paintToFile("sampleLayouts/" + numRooms + "RoomGridLayout");
        }
    }

    @Test
    void testInLineLayouts() {
        for (int numRooms : List.of(1, 3, 5, 7, 9)) {
            IMazeSubject maze = ExampleSubject.createRoomGrid(numRooms);
            MazeObserver mazeObserver = MazeObserver.getNewBuilder(maze,"Adventure Game - Radial Layout")
                    .useInLineLayoutStrategy()
                    .setDelayInSecondsAfterUpdate(3)
                    .setRoomDimension(1000/numRooms)
                    .setDimension(1000)
                    .build();
            mazeObserver.update("updated!");
            mazeObserver.paintToFile("sampleLayouts/" + numRooms + "RoomGridLayout");
        }
    }

    @Test
    void testOneWayGridLayouts() {
        for (int numRooms : List.of(2, 3, 5)) {
            IMazeSubject maze = ExampleSubject.createRoomGrid(numRooms);
            MazeObserver mazeObserver = MazeObserver.getNewBuilder(maze,"Adventure Game - Radial Layout")
                    .useGridLayoutStrategy()
                    .setDelayInSecondsAfterUpdate(3)
                    .setRoomDimension(1000/numRooms)
                    .setDimension(1000)
                    .build();
            mazeObserver.update("updated!");
            mazeObserver.paintToFile("sampleLayouts/" + numRooms + "RoomGridLayout");
        }
    }

    @Test
    void testFullyConnectedRadialLayout() {
        for (int numRooms : List.of(2, 3, 4, 9)) {
            IMazeSubject maze = ExampleSubject.createRoomGrid(numRooms);
            for (boolean twoWayConnections : List.of(true, false)) {
                MazeObserver mazeObserver = MazeObserver.getNewBuilder(maze,"Adventure Game - Radial Layout")
                        .useRadialLayoutStrategy()
                        .setDelayInSecondsAfterUpdate(3)
                        .setRoomDimension(1000 / numRooms)
                        .setDimension(1000)
                        .build();
                mazeObserver.update("updated!");
                mazeObserver.paintToFile("sampleLayouts/" + numRooms + "RoomGrid" + twoWayConnections + "Layout");
            }
        }
    }

    @Test
    void testSmartLayoutWithFullyConnectedRooms() {
        IMazeSubject mazeSubject = ExampleSubject.createRoomGrid(4);
        MazeObserver mazeObserver = MazeObserver.getNewBuilder(mazeSubject,"Adventure Game - Smart Layout")
                .useSmartLayoutStrategy()
                .useImageRooms()
                .setDimension(1200)
                .setWidth(1600)
                .setHeight(700)
                .setRoomDimension(100)
                .setDelayInSecondsAfterUpdate(1)
                .build();

        mazeObserver.update("updated!");
        mazeObserver.paintToFile("sampleLayouts/FullyConnected4RoomLayout.smart");

        mazeObserver.update("updated!");
        mazeObserver.paintToFile("sampleLayouts/FullyConnected16RoomLayout.smart");
    }

    @Test
    void testFullyConnectedRoomsWithCircularLayout() {
        IMazeSubject maze = ExampleSubject.createRoomGrid(7);
        MazeObserver mazeObserver = MazeObserver.getNewBuilder(maze,"Polymorphia")
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

    @Test
    void testNoSubject() {
        MazeObserver mazeObserver = new MazeObserver();
        mazeObserver.update("updated!");
        mazeObserver.paintToFile("sampleLayouts/4RoomRadialLayout");
    }

}