package csci.ooad.layout.example;

import csci.ooad.layout.intf.IMaze;
import csci.ooad.layout.intf.IMazeSubject;
import csci.ooad.layout.intf.MazeObserver;

public class ExampleSubject implements IMazeSubject {

    IMaze maze;


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
