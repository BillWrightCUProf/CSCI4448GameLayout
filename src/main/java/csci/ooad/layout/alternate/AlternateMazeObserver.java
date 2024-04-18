package csci.ooad.layout.alternate;

import csci.ooad.layout.intf.IMaze;
import csci.ooad.layout.intf.IMazeObserver;

public class AlternateMazeObserver implements IMazeObserver {

    static {
        System.out.println("AlternateMazeObserver class is being loaded");
    }
    @Override
    public void update(IMaze maze, String statusMessage) {
        System.out.println("Alternate Maze Observer: " + statusMessage);
        for (String room : maze.getRooms()) {
            System.out.println(room + ":");
            for (String content : maze.getContents(room)) {
                System.out.println("  Content: " + content);
            }
        }
    }
}
