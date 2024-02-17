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
    private Map<IRoom, Room> roomMap = new HashMap<>();

    private ExampleMaze(List<Room> rooms) {
        this.rooms = rooms;
        for (Room room : rooms) {
            roomMap.put(room, room);
        }
    }

    /**
     *
     * @param gridSize
     * @return
     */
    public static IMaze createRoomGrid(Integer gridSize) {
        return new ExampleMaze(Room.createRoomGrid(2));
    }

    public static IMaze createFullyConnectedRooms(Integer numRooms) {
        return createFullyConnectedRooms(numRooms, true);
    }

    public static IMaze createFullyConnectedRooms(Integer numRooms, Boolean twoWayConnections) {
        return new ExampleMaze(Room.createFullyConnectedRooms(numRooms, twoWayConnections));
    }

    @Override
    public List<IRoom> getRooms() {
        return new ArrayList<>(rooms);
    }

    @Override
    public List<IRoom> getNeighborsOf(IRoom room) {
        return roomMap.get(room).getNeighbors();
    }
}
