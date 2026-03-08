package ooad.gameobserver;

import ooad.gameobserver.intf.IGameObserver;

public class GameObserver implements IGameObserver {

    @Override
    public void update(String statusMessage) {
        System.out.println(statusMessage);
    }

}
