package csci.ooad.layout;

import org.junit.jupiter.api.Test;

class MazeObserverTest {

    @Test
    void testRadialLayouts() {
        MazeObserver mazeObserver = new MazeObserver(
                "Arcane Adventure Game - Radial Layout",
                new RadialLayoutStrategy());

        mazeObserver.update(Room.createRoomGrid(2));
        mazeObserver.paintToFile("sampleLayouts/testFourRoomRadialLayout");

        mazeObserver.update(Room.createRoomGrid(3));
        mazeObserver.paintToFile("sampleLayouts/testNineRoomRadialLayout");

        mazeObserver.update(Room.createRoomGrid(4));
        mazeObserver.paintToFile("sampleLayouts/testSixteenRoomRadialLayout");
    }

    @Test
    void testGridLayouts() {
        MazeObserver mazeObserver = new MazeObserver(
                "Arcane Adventure Game - Grid Layout",
                new GridLayoutStrategy());

        mazeObserver.update(Room.createRoomGrid(2));
        mazeObserver.paintToFile("sampleLayouts/testFourRoomGridLayout");

        mazeObserver.update(Room.createRoomGrid(3));
        mazeObserver.paintToFile("sampleLayouts/testNineRoomGridLayout");

        mazeObserver.update(Room.createRoomGrid(4));
        mazeObserver.paintToFile("sampleLayouts/testSixteenRoomGridLayout");
    }

    @Test
    void test2RoomLayouts() {
        MazeObserver mazeObserver = new MazeObserver(
                "Arcane Adventure Game - 2-Room Layout",
                new RadialLayoutStrategy());

        mazeObserver.update(Room.createFullyConnectedRooms(2, true));
        mazeObserver.paintToFile("sampleLayouts/testFullyConnected2RoomLayout.radial");

        mazeObserver.update(Room.createFullyConnectedRooms(2, false));
        mazeObserver.paintToFile("sampleLayouts/testFullyConnected2RoomOneWayLayout.radial");
    }

    @Test
    void testFullyConnectedRoomsLayouts() {
        MazeObserver mazeObserver = new MazeObserver(
                "Arcane Adventure Game - Radial Layout",
                new RadialLayoutStrategy());

        mazeObserver.update(Room.createFullyConnectedRooms(2));
        mazeObserver.paintToFile("sampleLayouts/testFullyConnected2RoomLayout.radial");

        mazeObserver.update(Room.createFullyConnectedRooms(3));
        mazeObserver.paintToFile("sampleLayouts/testFullyConnected3RoomLayout.radial");

        mazeObserver.update(Room.createFullyConnectedRooms(3, false));
        mazeObserver.paintToFile("sampleLayouts/testFullyConnectedOneWay3RoomLayout.radial");

        mazeObserver.update(Room.createFullyConnectedRooms(4));
        mazeObserver.paintToFile("sampleLayouts/testFullyConnected4RoomLayout.radial");

        mazeObserver.update(Room.createFullyConnectedRooms(9));
        mazeObserver.paintToFile("sampleLayouts/testFullyConnected9RoomLayout.radial");

        mazeObserver.update(Room.createFullyConnectedRooms(9, false));
        mazeObserver.paintToFile("sampleLayouts/testFullyConnectedOneWay9RoomLayout.radial");
    }

    @Test
    void testFullyConnectedRoomsWithGridLayouts() {
        MazeObserver mazeObserver = new MazeObserver(
                "Arcane Adventure Game - Radial Layout",
                new GridLayoutStrategy());

        mazeObserver.update(Room.createFullyConnectedRooms(4));
        mazeObserver.paintToFile("sampleLayouts/testFullyConnected4RoomLayout.grid");

        mazeObserver.update(Room.createFullyConnectedRooms(16));
        mazeObserver.paintToFile("sampleLayouts/testFullyConnected16RoomLayout.grid");
    }
}