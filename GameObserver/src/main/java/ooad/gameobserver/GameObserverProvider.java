package ooad.gameobserver;

import ooad.gameobserver.intf.IGameObserver;
import ooad.gameobserver.intf.IGameObserverProvider;
import ooad.gameobserver.intf.IServiceContext;

public class GameObserverProvider implements IGameObserverProvider {

    @Override
    public IGameObserver create(IServiceContext context) {
        return new GameObserver();
    }
}
