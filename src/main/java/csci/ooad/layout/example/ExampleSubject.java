package csci.ooad.layout.example;

import csci.ooad.layout.IMaze;
import csci.ooad.layout.IMazeSubject;
import csci.ooad.layout.MazeObserver;
import csci.ooad.layout.RadialLayoutStrategy;

class ExampleSubject implements IMazeSubject {

    IMaze maze;


    public void playGame() throws InterruptedException {
        maze = ExampleMaze.createRoomGrid(2);
        notifyObservers();
        Thread.sleep(2000);

        maze = ExampleMaze.createFullyConnectedRooms(4);
        notifyObservers();
        Thread.sleep(2000);

        maze = ExampleMaze.createFullyConnectedRooms(6, false);
        notifyObservers();
        Thread.sleep(2000);
    }

    @Override
    public IMaze getMaze() {
        return maze;
    }

    static public void main(String... args) throws InterruptedException {
        ExampleSubject game = new ExampleSubject();
        MazeObserver observer = new MazeObserver("Example Game", new RadialLayoutStrategy());
        game.attach(observer);
        game.playGame();
    }
}
