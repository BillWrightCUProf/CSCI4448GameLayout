package ooad.audibleobserver;

import ooad.gameobserver.intf.IGameObserver;
import ooad.gameobserver.intf.IGameObserverProvider;
import ooad.gameobserver.intf.IServiceContext;

public class AudibleObserverProvider implements IGameObserverProvider {

    @Override
    public IGameObserver create(IServiceContext context) {
        return new AudibleObserver(new AudiblePlayer());
    }
}
