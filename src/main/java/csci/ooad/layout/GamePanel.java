package csci.ooad.layout;

import csci.ooad.layout.intf.IMaze;
import csci.ooad.layout.intf.RoomShape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class GamePanel extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(GamePanel.class);


    static Font ROOM_NAME_FONT = new Font("Lucida Grande", Font.BOLD, 13);
    static Font ROOM_CONTENTS_FONT = new Font("Lucida Grande", Font.ITALIC, 13);

    Integer roomDimension;
    static Color DEFAULT_BACKGROUND_COLOR = Color.BLACK;
    static Color DEFAULT_ROOM_COLOR = Color.GREEN;
    static Color DEFAULT_TEXT_COLOR = Color.WHITE;
    static Color CONNECTOR_COLOR = Color.BLUE;

    IMaze maze;
    Map<String, Point> roomLocations;
    Map<String, Image> roomImages;

    RoomShape roomShape;
    Color roomBackgroundColor = DEFAULT_ROOM_COLOR;
    Color textColor = DEFAULT_TEXT_COLOR;


    public GamePanel(IMaze maze, Map<String, Point> roomLocations, Map<String, Image> roomImages, RoomShape roomShape, Integer width, Integer height, Integer roomRadius) {
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(DEFAULT_BACKGROUND_COLOR);
        this.setDoubleBuffered(true);
        this.maze = maze;
        this.roomLocations = roomLocations;
        this.roomImages = roomImages;
        this.roomShape = roomShape;
        this.roomDimension = roomRadius;
    }

    public void setRoomBackground(Color color) {
        roomBackgroundColor = color;
    }

    public void setTextColor(Color color) {
        textColor = color;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        logger.debug("In paintComponent. Width of parent is: {}", this.getParent().getWidth());
        paintMaze(g2);
    }

    void paintRoomCenteredAt(Graphics2D g2, Point roomCenter, String roomName) {
        g2.setColor(roomBackgroundColor);
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
        g2.setColor(textColor);
        g2.setFont(ROOM_NAME_FONT);
        Integer fontHeight = g2.getFontMetrics().getHeight();
        g2.drawString(roomName, upperRightCorner.x + 2, upperRightCorner.y + fontHeight);
        return fontHeight;
    }

    void paintImageCenteredAt(Graphics2D g2, Point upperRightCorner, String roomName) {
        Image caveRoomImage = roomImages.get(roomName);
        g2.drawImage(caveRoomImage, upperRightCorner.x, upperRightCorner.y, roomDimension, roomDimension, this);
    }

    void paintMaze(Graphics2D g2) {
        drawRoomConnections(g2);
        for (String currentRoom : roomLocations.keySet()) {
            paintRoomCenteredAt(g2, roomLocations.get(currentRoom), currentRoom);
        }
    }

    private void drawRoomConnections(Graphics2D g2) {
        g2.setColor(CONNECTOR_COLOR);
        for (String currentRoom : maze.getRooms()) {
            for (String neighbor : maze.getNeighborsOf(currentRoom)) {
                Point roomLocation = roomLocations.get(currentRoom);
                Point neighborLocation = roomLocations.get(neighbor);
                if (neighborLocation == null) {
                    continue;
                }
                drawArrow(g2, roomLocation, neighborLocation);
            }
        }
    }

    public void drawArrow(Graphics2D g2, Point roomLocation, Point neighborLocation) {
        if (neighborLocation == null || roomLocation == null) {
            return;
        }
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
