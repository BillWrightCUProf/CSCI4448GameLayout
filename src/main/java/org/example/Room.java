package org.example;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private final String name;
    private final List<Room> neighbors;

    public static List<Room> createRoomGrid(Integer dimension) {
        List<Room> rooms = new ArrayList<>();
        for (Integer row = 0; row < dimension; row++) {
            for (Integer col = 0; col < dimension; col++) {
                rooms.add(new Room("Room R" + row + "C" + col));
            }
        }
        for (Integer i = 0; i < rooms.size(); i++) {
            if (i % dimension < (dimension - 1)) {
                rooms.get(i).addNeighbor(rooms.get(i+1));
            }
            if (i < dimension * (dimension - 1)) {
                rooms.get(i).addNeighbor(rooms.get(i + dimension));
            }
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
        return List.of("SkyWalker", "Sword", "Ogre");
    }

    private void addNeighbor(Room aNeighbor) {
        this.neighbors.add(aNeighbor);
    }


    public String getName() {
        return name;
    }

    public List<Room> getNeighbors() {
        // Don't just return the list, as it could be modified
        return new ArrayList<>(neighbors);
    }
}
