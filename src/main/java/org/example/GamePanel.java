package org.example;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class GamePanel extends JPanel {
    private static final Logger logger = LogManager.getLogger(GamePanel.class);

    static final Integer WIDTH = 800;
    static final Integer HEIGHT = 800;
    final Integer ROOM_WIDTH = 100;
    final Integer ROOM_HEIGHT = 100;
    Color BACKGROUND_COLOR = Color.YELLOW;
    Color ROOM_COLOR = Color.GREEN;
    Color TEXT_COLOR = Color.BLACK;
    Color CONNECTOR_COLOR = Color.BLUE;

    List<Room> caveRooms;
    RoomLayoutStrategy roomLayoutStrategy;

    public GamePanel(List<Room> rooms, RoomLayoutStrategy layoutStrategy) {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(BACKGROUND_COLOR);
        this.setDoubleBuffered(true);
        this.caveRooms = rooms;
        this.roomLayoutStrategy = layoutStrategy;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        logger.info("In paintComponent. Width of parent is: " + this.getParent().getWidth());
        paintRooms(g, caveRooms);
    }

    void paintRoomCenteredAt(Graphics g, Point roomCenter, Room room) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(ROOM_COLOR);
        Point upperRightCorner = new Point(roomCenter.x - ROOM_WIDTH / 2, roomCenter.y - ROOM_HEIGHT / 2);
        g2.fillRect(upperRightCorner.x, upperRightCorner.y, ROOM_WIDTH, ROOM_HEIGHT);
        g2.setColor(TEXT_COLOR);

        Font originalFont = g2.getFont();
        Font boldFont = new Font(originalFont.getName(), Font.BOLD, originalFont.getSize());

        // Print the room name
        g2.setFont(boldFont);
        Integer fontHeight = g2.getFontMetrics().getHeight();
        g2.drawString(room.getName(), upperRightCorner.x + 2, upperRightCorner.y + fontHeight);

        // Now print the contents of the room
        Integer yPosition = upperRightCorner.y + fontHeight * 2 + 2;
        g2.setFont(originalFont);
        for (String desc : room.getContents()) {
            g2.drawString(desc, upperRightCorner.x + 10, yPosition);
            yPosition += fontHeight + 2;
        }
    }

    void paintRooms(Graphics g, List<Room> rooms) {

        Integer currentWindowWidth = this.getParent().getWidth();
        Integer currentWindowHeight = this.getParent().getHeight();

        Map<Room, Point> roomLocations = roomLayoutStrategy.calculateRoomLocations(
                rooms, currentWindowWidth, currentWindowHeight,
                ROOM_WIDTH, ROOM_HEIGHT
        );

        for (Room currentRoom : rooms) {
            paintRoomCenteredAt(g, roomLocations.get(currentRoom), currentRoom);
        }
        drawRoomConnections(g, rooms, roomLocations);
    }

//    void paintRoomsRadially(Graphics2D g2, List<Room> rooms) {
//        Map<Room, Point> roomLocations = new HashMap<>();
//
//        Integer currentWindowWidth = this.getParent().getWidth();
//        Integer currentWindowHeight = this.getParent().getHeight();
//        Point center = new Point(currentWindowWidth/2, currentWindowHeight/2);
//
//        Integer layoutRadius = Math.min(currentWindowWidth, currentWindowHeight) / 2 - ROOM_WIDTH;
//        Double radialInterval = 2 * Math.PI / rooms.size();
//        Double currentAngle = 0.0;
//        for (Room currentRoom : rooms) {
//            Integer x = Math.toIntExact(Math.round(center.x + layoutRadius * Math.cos(currentAngle)));
//            Integer y = Math.toIntExact(Math.round(center.y - layoutRadius * Math.sin(currentAngle)));
//            Point roomLocation = new Point(x, y);
//            paintRoomCenteredAt(g2, roomLocation, currentRoom);
//            roomLocations.put(currentRoom, roomLocation);
//            currentAngle += radialInterval;
//        }
//
//        drawRoomConnections(g2, rooms, roomLocations);
//    }
//
//    void paintRoomsOnAGrid(Graphics2D g2, List<Room> rooms) {
//        Map<Room, Point> roomLocations = new HashMap<>();
//        Integer currentWindowWidth = this.getParent().getWidth();
//        Integer currentWindowHeight = this.getParent().getHeight();
//
//        Integer dimension = Math.toIntExact(Math.round(Math.sqrt(rooms.size())));
//        Integer rowSpacing = (currentWindowHeight - ROOM_HEIGHT * 2) / (dimension - 1);
//        Integer colSpacing = (currentWindowWidth - ROOM_WIDTH * 2) / (dimension - 1);
//        Point currentLocation = new Point(ROOM_WIDTH, ROOM_HEIGHT);
//        Integer roomCount = 0;
//        for (Room currentRoom : rooms) {
//            paintRoomCenteredAt(g2, currentLocation, currentRoom);
//            roomLocations.put(currentRoom, currentLocation);
//            roomCount += 1;
//            if (roomCount % dimension == 0) {
//                currentLocation = new Point(ROOM_WIDTH, currentLocation.y + rowSpacing);
//            } else {
//                currentLocation = new Point(currentLocation.x + colSpacing, currentLocation.y);
//            }
//        }
//
//        drawRoomConnections(g2, rooms, roomLocations);
//    }

    private void drawRoomConnections(Graphics g, List<Room> rooms, Map<Room, Point> roomLocations) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(CONNECTOR_COLOR);
        for (Room currentRoom : rooms) {
            for (Room neighbor : currentRoom.getNeighbors()) {
                Point roomLocation = roomLocations.get(currentRoom);
                Point neighborLocation = roomLocations.get(neighbor);
                g2.drawLine(roomLocation.x, roomLocation.y, neighborLocation.x, neighborLocation.y);
            }
        }
    }
}
