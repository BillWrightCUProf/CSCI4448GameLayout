package ooad.alternatemazeobserver;

import ooad.gameobserver.intf.IGameObserver;
import ooad.gameobserver.intf.IGameObserverProvider;
import ooad.gameobserver.intf.IMazeSubject;
import ooad.gameobserver.intf.IServiceContext;

import java.util.Optional;

public class AlternateMazeObserverProvider implements IGameObserverProvider {

    @Override
    public IGameObserver create(IServiceContext context) {
        Optional<IMazeSubject> mazeSubject = context.get(IMazeSubject.class);
        if (!mazeSubject.isPresent()) {
            throw new RuntimeException("MazeSubject not found in context");
        }
        return new AlternateMazeObserver(mazeSubject.get());
    }
}
