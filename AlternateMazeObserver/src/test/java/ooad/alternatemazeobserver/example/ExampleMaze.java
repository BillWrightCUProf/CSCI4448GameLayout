package ooad.alternatemazeobserver.example;



import ooad.gameobserver.intf.IMaze;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class ExampleMaze implements IMaze {

    private final Map<String, Room> roomMap = new HashMap<>();

    private ExampleMaze(Set<Room> rooms) {
        for (Room room : rooms) {
            roomMap.put(room.getName(), room);
        }
    }

    public static IMaze createRoomGrid(Integer numRooms) {
        Integer gridSize = (int)Math.round(Math.sqrt(numRooms));
        return new ExampleMaze(Room.createRoomGrid(gridSize));
    }

    public static IMaze createFullyConnectedRooms(Integer numRooms) {
        return createFullyConnectedRooms(numRooms, true);
    }

    public static IMaze createFullyConnectedRooms(Integer numRooms, Boolean twoWayConnections) {
        return new ExampleMaze(Room.createFullyConnectedRooms(numRooms, twoWayConnections));
    }

    @Override
    public Set<String> getRoomNames() {
        return roomMap.keySet();
    }

    @Override
    public Set<String> getNeighborsOf(String room) {
        return Set.copyOf(roomMap.get(room).getNeighbors().stream().map(IRoom::getName).toList());
    }

    @Override
    public List<String> getArtifacts(String room) {
        if (!roomMap.containsKey(room)) {
            return null;
        }
        return roomMap.get(room).getArtifacts();
    }

    @Override
    public List<String> getCharacters(String room) {
        if (!roomMap.containsKey(room)) {
            return null;
        }
        return roomMap.get(room).getCharacters();
    }

    @Override
    public String toString() {
        return roomMap.values().stream().map(Room::toString).reduce("", (a, b) -> a + "\n" + b);
    }
}
