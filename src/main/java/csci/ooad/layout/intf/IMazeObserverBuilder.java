package csci.ooad.layout.intf;

import csci.ooad.layout.IRoomLayoutStrategy;

public interface IMazeObserverBuilder {
    IMazeObserverBuilder setTitle(String title);

    IMazeObserverBuilder setDimension(Integer dimension);

    IMazeObserverBuilder setRoomDimension(Integer roomDimension);

    IMazeObserverBuilder setLayoutStrategy(IRoomLayoutStrategy layoutStrategy);

    IMazeObserverBuilder setRoomShape(RoomShape roomShape);

    IMazeObserverBuilder useRadialLayoutStrategy();

    IMazeObserverBuilder useGridLayoutStrategy();

    IMazeObserverBuilder useSquareRooms();

    IMazeObserverBuilder useCircleRooms();

    IMazeObserverBuilder useImageRooms();

    IMazeObserverBuilder setDelayInSecondsAfterUpdate(Integer delayInSeconds);

    IMazeObserver build();
}
