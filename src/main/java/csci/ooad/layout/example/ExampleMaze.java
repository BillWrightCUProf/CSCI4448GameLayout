package csci.ooad.layout.example;

import csci.ooad.layout.IMaze;
import csci.ooad.layout.IRoom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class ExampleMaze implements IMaze {

    private List<Room> rooms;
    private Map<String, Room> roomMap = new HashMap<>();

    private ExampleMaze(List<Room> rooms) {
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
    public List<String> getRooms() {
        return roomMap.keySet().stream().toList();
    }

    @Override
    public List<String> getNeighborsOf(String room) {
        return roomMap.get(room).getNeighbors().stream().map(IRoom::getName).toList();
    }

    @Override
    public List<String> getContents(String room) {
        return roomMap.get(room).getContents();
    }
}
