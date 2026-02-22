package csci.ooad.layout.example;

import csci.ooad.layout.intf.IMaze;
import csci.ooad.layout.intf.IMazeObserver;
import csci.ooad.layout.intf.IMazeSubject;
import csci.ooad.layout.intf.MazeObserver;

import java.util.ArrayList;
import java.util.List;

public class ExampleSubject implements IMazeSubject {
    IMaze maze;

    List<IMazeObserver> observers = new ArrayList<>();

    public void attach(IMazeObserver observer) {
        observers.add(observer);
    }

    public List<IMazeObserver> getObservers() {
        return observers;
    }

    public void playGameWithChangingMazeShape() throws InterruptedException {
        maze = ExampleMaze.createRoomGrid(4);
        notifyObservers("Created 2x2 Grid Maze");
        Thread.sleep(2000);

        maze = ExampleMaze.createFullyConnectedRooms(4);
        notifyObservers("Created 4 Fully Connected Rooms");
        Thread.sleep(2000);

        maze = ExampleMaze.createFullyConnectedRooms(6, false);
        notifyObservers("Created 6 Fully Connected Rooms");
        Thread.sleep(2000);
    }

    public void playGame(Integer numRooms, String layout) throws InterruptedException {
        if (layout.equals("grid")) {
            maze = ExampleMaze.createRoomGrid(numRooms);
        } else {
            maze = ExampleMaze.createFullyConnectedRooms(numRooms);
        }

        for (int i = 0; i < 3; i++) {
            notifyObservers("Turn " + i);
        }
    }

    private IMaze getMaze() {
        return maze;
    }

    private void notifyObservers(String statusMessage) {
//        notifyObservers(List.of(statusMessage));
        for (IMazeObserver observer : getObservers()) {
            observer.update(statusMessage);
        }
    }

    private void notifyObservers(List<String> statusMessages) {
        for (IMazeObserver observer : getObservers()) {
            observer.update(getMaze(), statusMessages);
        }
    }

    static public void main(String... args) throws InterruptedException {
        ExampleSubject game = new ExampleSubject();
        MazeObserver observer = MazeObserver.getNewBuilder("Example Game")
                .useCircleRooms()
                .useRadialLayoutStrategy()
                .build();
        game.attach(observer);
        game.playGameWithChangingMazeShape();
    }
}
