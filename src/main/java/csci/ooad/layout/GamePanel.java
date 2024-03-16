package csci.ooad.layout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Map;

public class GamePanel extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(GamePanel.class);

    static final Integer DEFAULT_WIDTH = 800;
    static final Integer DEFAULT_ROOM_WIDTH = 100;

    static Font ROOM_NAME_FONT = new Font("Lucida Grande", Font.BOLD, 13);
    static Font ROOM_CONTENTS_FONT = new Font("Lucida Grande", Font.ITALIC, 13);
    static Font STATUS_MESSAGE_FONT = new Font("Lucida Grande", Font.ITALIC, 15);

    Integer roomDimension;
    String statusMessage;
    Color BACKGROUND_COLOR = Color.YELLOW;
    Color ROOM_COLOR = Color.GREEN;
    Color TEXT_COLOR = Color.WHITE;
    Color CONNECTOR_COLOR = Color.BLUE;

    IMaze maze;
    IRoomLayoutStrategy roomLayoutStrategy;
    RoomShape roomShape;

    public GamePanel(IMaze maze, String statusMessage, IRoomLayoutStrategy layoutStrategy) {
        this(maze, statusMessage, layoutStrategy, RoomShape.CIRCLE, DEFAULT_WIDTH, DEFAULT_ROOM_WIDTH);
    }

    public GamePanel(IMaze maze, String statusMessage, IRoomLayoutStrategy layoutStrategy, RoomShape roomShape, Integer panelDimension, Integer roomRadius) {
        this.setPreferredSize(new Dimension(panelDimension, panelDimension));
        this.setBackground(BACKGROUND_COLOR);
        this.setDoubleBuffered(true);
        this.maze = maze;
        this.roomLayoutStrategy = layoutStrategy;
        this.roomShape = roomShape;
        this.roomDimension = roomRadius;
        this.statusMessage = statusMessage;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        logger.debug("In paintComponent. Width of parent is: " + this.getParent().getWidth());
        paintStatusMessage(g2);
        paintMaze(g2, maze);
    }

    private void paintStatusMessage(Graphics2D g2) {
        g2.setFont(STATUS_MESSAGE_FONT);
        Integer centerRight = this.getParent().getWidth() / 4;
        g2.drawString(statusMessage, centerRight, 20);
    }

    void paintRoomCenteredAt(Graphics2D g2, Point roomCenter, String roomName) {
        g2.setColor(ROOM_COLOR);
        Point upperRightCorner = new Point(roomCenter.x - roomDimension / 2, roomCenter.y - roomDimension / 2);
        switch (roomShape) {
            case SQUARE:
                g2.fillRect(upperRightCorner.x, upperRightCorner.y, roomDimension, roomDimension);
                break;
            case CIRCLE:
                g2.fillOval(upperRightCorner.x, upperRightCorner.y, roomDimension, roomDimension);
                break;
            case IMAGE:
                paintImageCenteredAt(g2, upperRightCorner, roomName);
                break;
        }

        paintRoomName(g2, roomName, upperRightCorner);
        paintRoomContents(g2, roomName, upperRightCorner);
    }

    private void paintRoomContents(Graphics2D g2, String roomName, Point upperRightCorner) {
        Integer fontHeight = g2.getFontMetrics().getHeight();
        Integer yPosition = upperRightCorner.y + fontHeight * 2 + 2;
        g2.setFont(ROOM_CONTENTS_FONT);
        for (String desc : maze.getContents(roomName)) {
            g2.drawString(desc, upperRightCorner.x + 5, yPosition);
            yPosition += fontHeight + 2;
        }
    }

    private Integer paintRoomName(Graphics2D g2, String roomName, Point upperRightCorner) {
        g2.setColor(TEXT_COLOR);
        g2.setFont(ROOM_NAME_FONT);
        Integer fontHeight = g2.getFontMetrics().getHeight();
        g2.drawString(roomName, upperRightCorner.x + 2, upperRightCorner.y + fontHeight);
        return fontHeight;
    }

    void paintImageCenteredAt(Graphics2D g2, Point upperRightCorner, String roomName) {
        // I want to draw a jpg image from my resources/images directory at the roomCenter
        // I will use the ImageIO.read method to read the image from the file system
        // I will then use the drawImage method to draw the image at the roomCenter
        Image caveRoomImage = null;
        try {
            File file = new File("resources/images/cave1-small.jpg");
            caveRoomImage = javax.imageio.ImageIO.read(getClass().getResourceAsStream("/images/cave1-small.jpg"));
        } catch (java.io.IOException e) {
            logger.error("Error reading image file: " + e.getMessage());
        }

        // I want to scale the image to fit into a circular radius
        g2.drawImage(caveRoomImage, upperRightCorner.x, upperRightCorner.y, roomDimension, roomDimension, this);
    }

    void paintMaze(Graphics2D g2, IMaze maze) {
        Integer currentWindowWidth = this.getParent().getWidth();

        Map<String, Point> roomLocations = roomLayoutStrategy.calculateRoomLocations(
                maze.getRooms(), currentWindowWidth, roomDimension);

        for (String currentRoom : maze.getRooms()) {
            paintRoomCenteredAt(g2, roomLocations.get(currentRoom), currentRoom);
        }
        drawRoomConnections(g2, maze, roomLocations);
    }

    private void drawRoomConnections(Graphics2D g2, IMaze maze, Map<String, Point> roomLocations) {
        g2.setColor(CONNECTOR_COLOR);
        for (String currentRoom : maze.getRooms()) {
            for (String neighbor : maze.getNeighborsOf(currentRoom)) {
                Point roomLocation = roomLocations.get(currentRoom);
                Point neighborLocation = roomLocations.get(neighbor);
                drawArrow(g2, roomLocation, neighborLocation);
            }
        }
    }

    public void drawArrow(Graphics2D g2, Point roomLocation, Point neighborLocation) {
        double lineAngle = Math.atan2(roomLocation.y - neighborLocation.y, roomLocation.x - neighborLocation.x);

        // Adjust the starting and ending points of the line to end at the room boundary and not the center
        double deltaX = roomDimension / 2.0 * Math.cos(lineAngle);
        double deltaY = roomDimension / 2.0 * Math.sin(lineAngle);
        Point roomBoundaryLocation = new Point((int)(roomLocation.x - deltaX), (int)(roomLocation.y - deltaY));
        Point neighborBoundaryLocation = new Point((int)(neighborLocation.x + deltaX), (int)(neighborLocation.y + deltaY));

        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(roomBoundaryLocation.x, roomBoundaryLocation.y, neighborBoundaryLocation.x, neighborBoundaryLocation.y);
        g2.setStroke(oldStroke);

        // Now draw the arrow indicating how the rooms are connected
        int headAngle = 60;
        int headLength = 20;
        double offs = headAngle * Math.PI / 180.0;
        int[] xs = {neighborBoundaryLocation.x + (int) (headLength * Math.cos(lineAngle + offs)), neighborBoundaryLocation.x,
                neighborBoundaryLocation.x + (int) (headLength * Math.cos(lineAngle - offs))};
        int[] ys = {neighborBoundaryLocation.y + (int) (headLength * Math.sin(lineAngle + offs)), neighborBoundaryLocation.y,
                neighborBoundaryLocation.y + (int) (headLength * Math.sin(lineAngle - offs))};
        g2.fillPolygon(xs, ys, 3);
    }
}
