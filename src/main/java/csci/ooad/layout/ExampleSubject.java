package csci.ooad.layout;

import java.util.List;

public class ExampleSubject implements IMazeSubject {

    List<IConnectedRoom> rooms;

    @Override
    public List<IConnectedRoom> getConnectedRooms() {
        return rooms;
    }

    public void playGame() throws InterruptedException {
        rooms = Room.createRoomGrid(2);
        notifyObservers();
        Thread.sleep(2000);

        rooms = Room.createFullyConnectedRooms(4);
        notifyObservers();
        Thread.sleep(2000);

        rooms = Room.createFullyConnectedRooms(6, false);
        notifyObservers();
        Thread.sleep(2000);
    }

    static public void main(String... args) throws InterruptedException {
        ExampleSubject game = new ExampleSubject();
        MazeObserver observer = new MazeObserver("Example Game", new RadialLayoutStrategy());
        game.attach(observer);
        game.playGame();
    }
}
