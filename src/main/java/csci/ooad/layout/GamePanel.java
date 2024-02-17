package csci.ooad.layout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class GamePanel extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(GamePanel.class);

    static final Integer WIDTH = 800;
    static final Integer HEIGHT = 800;
    final Integer ROOM_WIDTH = 100;
    final Integer ROOM_HEIGHT = 100;
    Color BACKGROUND_COLOR = Color.YELLOW;
    Color ROOM_COLOR = Color.GREEN;
    Color TEXT_COLOR = Color.BLACK;
    Color CONNECTOR_COLOR = Color.BLUE;

    IMaze maze;
    IRoomLayoutStrategy roomLayoutStrategy;
    RoomShape roomShape;

    public enum RoomShape {
        CIRCLE,
        SQUARE
    }

    public GamePanel(IMaze maze, IRoomLayoutStrategy layoutStrategy) {
        this(maze, layoutStrategy, RoomShape.CIRCLE);
    }

    public GamePanel(IMaze maze, IRoomLayoutStrategy layoutStrategy, RoomShape roomShape) {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(BACKGROUND_COLOR);
        this.setDoubleBuffered(true);
        this.maze = maze;
        this.roomLayoutStrategy = layoutStrategy;
        this.roomShape = roomShape;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        logger.debug("In paintComponent. Width of parent is: " + this.getParent().getWidth());
        paintMaze(g, maze);
    }

    void paintRoomCenteredAt(Graphics g, Point roomCenter, IRoom room) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(ROOM_COLOR);
        Point upperRightCorner = new Point(roomCenter.x - ROOM_WIDTH / 2, roomCenter.y - ROOM_HEIGHT / 2);
        if (roomShape.equals(RoomShape.CIRCLE)) {
            g2.fillOval(upperRightCorner.x, upperRightCorner.y, ROOM_WIDTH, ROOM_HEIGHT);
        } else {
            g2.fillRect(upperRightCorner.x, upperRightCorner.y, ROOM_WIDTH, ROOM_HEIGHT);
        }
        g2.setColor(TEXT_COLOR);

        Font originalFont = g2.getFont();
        Font boldFont = new Font(originalFont.getName(), Font.BOLD, originalFont.getSize());
        logger.debug("Original font was: " + originalFont);

        // Print the room name
        g2.setFont(boldFont);
        Integer fontHeight = g2.getFontMetrics().getHeight();
        g2.drawString(room.getName(), upperRightCorner.x + 2, upperRightCorner.y + fontHeight);

        // Now print the contents of the room
        Integer yPosition = upperRightCorner.y + fontHeight * 2 + 2;
        Font roomContentsFont = new Font(originalFont.getName(), Font.ITALIC, 10);

        g2.setFont(roomContentsFont);
        for (String desc : room.getContents()) {
            g2.drawString(desc, upperRightCorner.x + 5, yPosition);
            yPosition += fontHeight + 2;
        }
    }

    void paintMaze(Graphics g, IMaze maze) {
        Integer currentWindowWidth = this.getParent().getWidth();
        Integer currentWindowHeight = this.getParent().getHeight();

        Map<IRoom, Point> roomLocations = roomLayoutStrategy.calculateRoomLocations(
                maze.getRooms(), currentWindowWidth, currentWindowHeight,
                ROOM_WIDTH, ROOM_HEIGHT
        );

        for (IRoom currentRoom : maze.getRooms()) {
            paintRoomCenteredAt(g, roomLocations.get(currentRoom), currentRoom);
        }
        drawRoomConnections(g, maze, roomLocations);
    }

    private void drawRoomConnections(Graphics g, IMaze maze, Map<IRoom, Point> roomLocations) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(CONNECTOR_COLOR);
        for (IRoom currentRoom : maze.getRooms()) {
            for (IRoom neighbor : maze.getNeighborsOf(currentRoom)) {
                Point roomLocation = roomLocations.get(currentRoom);
                Point neighborLocation = roomLocations.get(neighbor);
                drawArrow(g2, roomLocation, neighborLocation);
            }
        }
    }

    public void drawArrow(Graphics2D g2, Point roomLocation, Point neighborLocation) {
        double lineAngle = Math.atan2(roomLocation.y - neighborLocation.y, roomLocation.x - neighborLocation.x);

        // Adjust the starting and ending points of the line to end at the room boundary and not the center
        double deltaX = ROOM_WIDTH / 2.0 * Math.cos(lineAngle);
        double deltaY = ROOM_HEIGHT / 2.0 * Math.sin(lineAngle);
        Point roomBoundaryLocation = new Point((int)(roomLocation.x - deltaX), (int)(roomLocation.y - deltaY));
        Point neighborBoundaryLocation = new Point((int)(neighborLocation.x + deltaX), (int)(neighborLocation.y + deltaY));

        // Draw the connecting line
        g2.drawLine(roomBoundaryLocation.x, roomBoundaryLocation.y, neighborBoundaryLocation.x, neighborBoundaryLocation.y);

        // Now draw the arrow indicating how the rooms are connected
        int headAngle = 60;
        int headLength = 10;
        double offs = headAngle * Math.PI / 180.0;
        int[] xs = {neighborBoundaryLocation.x + (int) (headLength * Math.cos(lineAngle + offs)), neighborBoundaryLocation.x,
                neighborBoundaryLocation.x + (int) (headLength * Math.cos(lineAngle - offs))};
        int[] ys = {neighborBoundaryLocation.y + (int) (headLength * Math.sin(lineAngle + offs)), neighborBoundaryLocation.y,
                neighborBoundaryLocation.y + (int) (headLength * Math.sin(lineAngle - offs))};
        g2.fillPolygon(xs, ys, 3);
    }
}
