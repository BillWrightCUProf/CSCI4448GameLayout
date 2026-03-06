package csci.ooad.layout.example;

import csci.ooad.layout.intf.*;

import java.util.ArrayList;
import java.util.List;

public class ExampleSubject implements IGame, IMazeSubject {
    IMaze maze;

    List<IGameObserver> observers = new ArrayList<>();

    public void attach(IGameObserver observer) {
        observers.add(observer);
    }

    public List<IGameObserver> getObservers() {
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

    @Override
    public IMaze getMaze() {
        return maze;
    }

    private void notifyObservers(String statusMessage) {
        for (IGameObserver observer : getObservers()) {
            observer.update(statusMessage);
        }
    }

    static public void main(String... args) throws InterruptedException {
        ExampleSubject game = new ExampleSubject();
        MazeObserver observer = MazeObserver.getNewBuilder(game,"Example Game")
                .useCircleRooms()
                .useRadialLayoutStrategy()
                .build();
        game.attach(observer);
        game.playGameWithChangingMazeShape();
    }

    static public ExampleSubject createRoomGrid(Integer numRooms) {
        ExampleSubject game = new ExampleSubject();
        game.maze = ExampleMaze.createRoomGrid(numRooms);
        return game;
    }
}
