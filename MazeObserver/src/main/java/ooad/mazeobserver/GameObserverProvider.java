package ooad.mazeobserver;

import ooad.gameobserver.intf.IGameObserver;
import ooad.gameobserver.intf.IGameObserverProvider;
import ooad.gameobserver.intf.IMazeSubject;
import ooad.gameobserver.intf.IServiceContext;

import java.util.Optional;

public class GameObserverProvider implements IGameObserverProvider {

    @Override
    public IGameObserver create(IServiceContext context) {
        if (context == null) {
            return new MazeObserver();
        }
        Optional<IMazeSubject> mazeSubject = context.get(IMazeSubject.class);
        if (!mazeSubject.isPresent()) {
            return new MazeObserver();
        }
        return new MazeObserver(mazeSubject.get());
    }

}
