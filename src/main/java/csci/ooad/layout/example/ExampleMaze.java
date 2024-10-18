package csci.ooad.layout.example;

import csci.ooad.layout.intf.IMaze;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 */
public class ExampleMaze implements IMaze {

    private final Set<Room> rooms;
    private final Map<String, Room> roomMap = new HashMap<>();

    private ExampleMaze(Set<Room> rooms) {
        this.rooms = rooms;
        for (Room room : rooms) {
            roomMap.put(room.getName(), room);
        }
    }

    /**
     *
     * @param numRooms
     * @return
     */
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
    public Set<String> getRooms() {
        return roomMap.keySet();
    }

    @Override
    public Set<String> getNeighborsOf(String room) {
        return Set.copyOf(roomMap.get(room).getNeighbors().stream().map(IRoom::getName).toList());
    }

    @Override
    public List<String> getContents(String room) {
        if (!roomMap.containsKey(room)) {
            return null;
        }
        return roomMap.get(room).getContents();
    }
}
