package csci.ooad.layout.example;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class Room implements IRoom {
    public static String[] NAMES = new String[]{
            "Rivendell", "Mordor", "BagEnd", "Swamp", "Crystal Palace", "Pool of Lava",
            "Stalactite Cave", "Goblin's Fountain", "Dragon's Den", "Troll Bridge",
            "Dungeon", "Pit of Despair", "Sanctuary",
            "Den of Souls", "Map Room", "Fangorn Forest", "Room of Horrors"
    };

    private final String name;
    private final Set<Room> neighbors;

    public static Set<Room> createRoomGrid(Integer dimension) {
        List<Room> rooms = new ArrayList<>();
        int roomNameIndex = 0;
        for (Integer row = 0; row < dimension; row++) {
            for (Integer col = 0; col < dimension; col++) {
                Room currentRoom = new Room(NAMES[roomNameIndex % NAMES.length]);
                roomNameIndex++;
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
        return Set.copyOf(rooms);
    }

    public static Set<Room> createFullyConnectedRooms(Integer numRooms) {
        return createFullyConnectedRooms(numRooms, true);
    }

    public static Set<Room> createFullyConnectedRooms(Integer numRooms, Boolean twoWayConnections) {
        Set<Room> rooms = new HashSet<>();
        for (int i = 0; i < numRooms; i++) {
            Room currentRoom = new Room(NAMES[i % NAMES.length]);
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
        neighbors = new HashSet<>();
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

    public Set<IRoom> getNeighbors() {
        // Don't just return the list, as it could be modified
        return Set.copyOf(neighbors);
    }
}
