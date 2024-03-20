package csci.ooad.layout.inttest;

import csci.ooad.layout.example.ExampleSubject;
import csci.ooad.layout.intf.MazeObserver;
import org.junit.jupiter.api.Test;

class ExampleSubjectTest {

    @Test
    void testPlayGame() throws InterruptedException {
        // The arguments here aren't used, but show an example of how you could customize the running of an app
        ExampleSubject.main("--shape", "Circle");
    }

    @Test
    void testPlayGameOneRoomMaze() throws InterruptedException {
        ExampleSubject game = new ExampleSubject();
        MazeObserver observer = MazeObserver.getNewBuilder("Example Game")
                .useCircleRooms()
                .useGridLayoutStrategy()
                .build();
        game.attach(observer);
        game.playGame(1, "grid");
    }

    @Test
    void testPlayGameTwoRoomMaze() throws InterruptedException {
        ExampleSubject game = new ExampleSubject();
        MazeObserver observer = MazeObserver.getNewBuilder("Example Game")
                .useCircleRooms()
                .setDelayInSecondsAfterUpdate(2)
                .useGridLayoutStrategy()
                .build();
        game.attach(observer);
        game.playGame(2, "fully");
    }

    @Test
    void testPlayGameThreeRoomMaze() throws InterruptedException {
        ExampleSubject game = new ExampleSubject();
        MazeObserver observer = MazeObserver.getNewBuilder("Example Game")
                .useCircleRooms()
                .useGridLayoutStrategy()
                .build();
        game.attach(observer);
        game.playGame(3, "fully");
    }

    @Test
    void testPlayGameFiveRoomMaze() throws InterruptedException {
        ExampleSubject game = new ExampleSubject();
        MazeObserver observer = MazeObserver.getNewBuilder("Example Game")
                .useCircleRooms()
                .useGridLayoutStrategy()
                .setDimension(1200)
                .setRoomDimension(150)
                .build();
        game.attach(observer);
        game.playGame(5, "fully");
    }
    @Test
    void testPlayGame4x4RoomMaze() throws InterruptedException {
        ExampleSubject game = new ExampleSubject();
        MazeObserver observer = MazeObserver.getNewBuilder("Example Game")
                .useCircleRooms()
                .useGridLayoutStrategy()
                .setDimension(1200)
                .setRoomDimension(150)
                .setDelayInSecondsAfterUpdate(1)
                .build();
        game.attach(observer);
        game.playGame(16, "grid");
    }
    @Test
    void testPlayGameFiveRoomRadialMaze() throws InterruptedException {
        ExampleSubject game = new ExampleSubject();
        MazeObserver observer = MazeObserver.getNewBuilder("Example Game")
                .useImageRooms()
                .useRadialLayoutStrategy()
                .setDimension(1200)
                .setRoomDimension(200)
                .setDelayInSecondsAfterUpdate(10)
                .build();
        game.attach(observer);
        game.playGame(9, "fully");
    }

}