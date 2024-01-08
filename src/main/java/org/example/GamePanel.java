package org.example;


import javax.swing.JPanel;
import java.awt.Point;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GamePanel extends JPanel implements Runnable {

    final Integer WIDTH = 800;
    final Integer HEIGHT = 800;
    final Integer ROOM_WIDTH = 100;
    final Integer ROOM_HEIGHT = 100;
    final Point CENTER = new Point(WIDTH/2, HEIGHT/2);
    Color BACKGROUND_COLOR = Color.YELLOW;
    Color ROOM_COLOR = Color.GREEN;
    Thread gameThread;

    List<Room> caveRooms;

    public GamePanel(List<Room> rooms) {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(BACKGROUND_COLOR);
        this.setDoubleBuffered(true);
        this.caveRooms = rooms;
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override
    public void run() {
        while (gameThread != null) {
//            System.out.println("The game is running");
            update();
            repaint();
        }
    }

    public void update() {

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(ROOM_COLOR);
//        Point roomCenter = new Point(CENTER.x - ROOM_WIDTH/2, CENTER.y - ROOM_HEIGHT/2);
//        paintRoomCenteredAt(g2, roomCenter, new Room("Starting Room"));

//        paintRoomsRadially(g2, caveRooms);
        paintRoomsOnAGrid(g2, caveRooms);
    }

    void paintRoomCenteredAt(Graphics2D g2, Point roomCenter, Room room) {
        g2.setColor(Color.GREEN);
        Point upperRightCorner = new Point(roomCenter.x - ROOM_WIDTH/2, roomCenter.y - ROOM_HEIGHT/2);
        g2.fillRect(upperRightCorner.x, upperRightCorner.y, ROOM_WIDTH, ROOM_HEIGHT);
        g2.setColor(Color.BLACK);
        Integer fontHeight = g2.getFontMetrics().getHeight();
        g2.drawString(room.getName(), upperRightCorner.x + 2, upperRightCorner.y + fontHeight);
        Integer yPosition = upperRightCorner.y + fontHeight*2+ 2;
        for (String desc : room.getContents()) {
            g2.drawString(desc, upperRightCorner.x + 10, yPosition);
            yPosition += fontHeight + 2;
        }
    }

    void paintRoomsRadially(Graphics2D g2, List<Room> rooms) {
        Map<Room, Point> roomLocations = new HashMap<>();

        Integer layoutRadius = Math.min(WIDTH, HEIGHT) / 2 - ROOM_WIDTH;
        Double radialInterval = 2 * Math.PI / rooms.size();
        Double currentAngle = 0.0;
        for (Room currentRoom : rooms) {
            Integer x = Math.toIntExact(Math.round(CENTER.x + layoutRadius * Math.cos(currentAngle)));
            Integer y = Math.toIntExact(Math.round(CENTER.y - layoutRadius * Math.sin(currentAngle)));
            Point roomLocation = new Point(x, y);
            paintRoomCenteredAt(g2, roomLocation, currentRoom);
            roomLocations.put(currentRoom, roomLocation);
            currentAngle += radialInterval;
        }

        drawRoomConnections(g2, rooms, roomLocations);
    }

    void paintRoomsOnAGrid(Graphics2D g2, List<Room> rooms) {
        Map<Room, Point> roomLocations = new HashMap<>();

        Integer dimension = Math.toIntExact(Math.round(Math.sqrt(rooms.size())));
        Integer rowSpacing = (HEIGHT - ROOM_HEIGHT * 2) / (dimension - 1);
        Integer colSpacing = (WIDTH - ROOM_WIDTH * 2) / (dimension - 1);
        Point currentLocation = new Point(ROOM_WIDTH, ROOM_HEIGHT);
        Integer roomCount = 0;
        for (Room currentRoom : rooms) {
            paintRoomCenteredAt(g2, currentLocation, currentRoom);
            roomLocations.put(currentRoom, currentLocation);
            roomCount += 1;
            if (roomCount % dimension == 0) {
                currentLocation = new Point(ROOM_WIDTH, currentLocation.y + rowSpacing);
            } else {
                currentLocation = new Point(currentLocation.x + colSpacing, currentLocation.y);
            }
        }

        drawRoomConnections(g2, rooms, roomLocations);
    }

    private static void drawRoomConnections(Graphics2D g2, List<Room> rooms, Map<Room, Point> roomLocations) {
        for (Room currentRoom : rooms) {
            for (Room neighbor : currentRoom.getNeighbors()) {
                Point roomLocation = roomLocations.get(currentRoom);
                Point neighborLocation = roomLocations.get(neighbor);
                g2.drawLine(roomLocation.x, roomLocation.y, neighborLocation.x, neighborLocation.y);
            }
        }
    }
}
