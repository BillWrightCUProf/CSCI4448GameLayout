package csci.ooad.layout.example;

import csci.ooad.layout.IRoom;

import java.util.ArrayList;
import java.util.List;

class Room implements IRoom {
    private final String name;
    private final List<Room> neighbors;

    public static List<Room> createRoomGrid(Integer dimension) {
        List<Room> rooms = new ArrayList<>();
        for (Integer row = 0; row < dimension; row++) {
            for (Integer col = 0; col < dimension; col++) {
                Room currentRoom = new Room("Room R" + row + "C" + col);
                rooms.add(currentRoom);
            }
        }
        for (Integer i = 0; i < rooms.size(); i++) {
            if (i % dimension < (dimension - 1)) {
                rooms.get(i).addNeighbor(rooms.get(i + 1));
            }
            if (i < dimension * (dimension - 1)) {
                rooms.get(i).addNeighbor(rooms.get(i + dimension));
            }
        }
        return rooms;
    }

    public static List<Room> createFullyConnectedRooms(Integer numRooms) {
        return createFullyConnectedRooms(numRooms, true);
    }

    public static List<Room> createFullyConnectedRooms(Integer numRooms, Boolean twoWayConnections) {
        List<Room> rooms = new ArrayList<>();
        for (int i = 0; i < numRooms; i++) {
            Room currentRoom = new Room("Room " + i);
            for (Room otherRoom : rooms) {
                if (twoWayConnections) {
                    currentRoom.connect(otherRoom);
                } else {
                    currentRoom.addNeighbor(otherRoom);
                }
            }
            rooms.add(currentRoom);
        }
        return rooms;
    }

    public Room(String aName) {
        this.name = aName;
        neighbors = new ArrayList<>();
    }

    public String toString() {
        return this.name;
    }

    public List<String> getContents() {
        return List.of("# of Neighbors:" + neighbors.size(), "SkyWalker", "Sword", "Ogre");
    }

    private void addNeighbor(Room aNeighbor) {
        this.neighbors.add(aNeighbor);
    }

    private void connect(Room aNeighbor) {
        this.addNeighbor(aNeighbor);
        aNeighbor.addNeighbor(this);
    }

    public String getName() {
        return name;
    }

    public List<IRoom> getNeighbors() {
        // Don't just return the list, as it could be modified
        return new ArrayList<>(neighbors);
    }
}
