package csci.ooad.layout.alternate;

import csci.ooad.layout.intf.IMaze;
import csci.ooad.layout.intf.IMazeObserver;

import java.util.List;

public class AlternateMazeObserver implements IMazeObserver {

    static {
        System.out.println("AlternateMazeObserver class is being loaded");
    }
    @Override
    public void update(IMaze maze, List<String> statusMessages) {
        for (String msg : statusMessages) {
            System.out.println(msg);
        }
        for (String room : maze.getRooms()) {
            System.out.println(room + ":");
            for (String content : maze.getContents(room)) {
                System.out.println("  Content: " + content);
            }
        }
    }
}
