package ooad.gameobserver.intf;

public interface IGameObserverProvider {
    IGameObserver create(IServiceContext context);
}
