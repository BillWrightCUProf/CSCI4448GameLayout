package csci.ooad.layout;

import java.util.List;

import static csci.ooad.layout.Room.createRoomGrid;

public class ExampleSubject implements IMazeSubject {

    @Override
    public List<IConnectedRoom> getConnectedRooms() {
        return createRoomGrid(3);
    }

    public void updatesTheRooms() {
        // Do something to update the state of the rooms

        // Now notify all observers
        notifyObservers();
    }
}
