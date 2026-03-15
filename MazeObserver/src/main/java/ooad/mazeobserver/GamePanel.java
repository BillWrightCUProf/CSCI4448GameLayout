package ooad.mazeobserver;

import ooad.gameobserver.intf.IMaze;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static ooad.mazeobserver.ImageFactory.normalizeImageName;

public class GamePanel extends JPanel {
    private static final Logger logger = LoggerFactory.getLogger(GamePanel.class);

    static Font ROOM_NAME_FONT = new Font("Lucida Grande", Font.BOLD, 13);
    static Font ROOM_CONTENTS_FONT = new Font("Lucida Grande", Font.ITALIC, 13);
    static Font CHARACTER_LIST_FONT = new Font("Lucida Grande", Font.BOLD, 18);

    Integer roomDimension;
    static Color DEFAULT_BACKGROUND_COLOR = Color.BLACK;
    static Color DEFAULT_ROOM_COLOR = Color.GREEN;
    static Color DEFAULT_TEXT_COLOR = Color.WHITE;
    static Color CONNECTOR_COLOR = Color.BLUE;

    IMaze maze;
    Map<String, Point> roomLocations;
    Map<String, Image> roomImages;
    Map<String, Image> characterImages;

    RoomShape roomShape;
    Color roomBackgroundColor = DEFAULT_ROOM_COLOR;
    Color textColor = DEFAULT_TEXT_COLOR;

    private final JList<String> activeCharactersList = new JList<>();
    private final JScrollPane activeCharactersScrollPane = new JScrollPane(activeCharactersList);

    public GamePanel(IMaze maze, Map<String, Point> roomLocations, Map<String, Image> roomImages, Map<String, Image> characterImages,
                     RoomShape roomShape, Integer width, Integer height, Integer roomRadius) {
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(DEFAULT_BACKGROUND_COLOR);
        this.setDoubleBuffered(true);
        this.setLayout(new BorderLayout());

        this.maze = maze;
        this.roomLocations = roomLocations;
        this.roomImages = roomImages;
        this.characterImages = characterImages;
        this.roomShape = roomShape;
        this.roomDimension = roomRadius;

        configureActiveCharactersList(width, height);
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
        paintMaze(g2);
    }

    private void configureActiveCharactersList(Integer panelWidth, Integer panelHeight) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String characterName : getSortedActiveCharacterNames()) {
            listModel.addElement(characterName);
        }

        activeCharactersList.setModel(listModel);
        activeCharactersList.setFont(CHARACTER_LIST_FONT);
        activeCharactersList.setFocusable(false);

        activeCharactersScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        activeCharactersScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        Dimension listSize = getActiveCharactersListPreferredSize(panelWidth, panelHeight);
        activeCharactersScrollPane.setPreferredSize(listSize);
        activeCharactersScrollPane.setMaximumSize(listSize);

        JPanel topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        topRightPanel.setOpaque(false);
        topRightPanel.add(activeCharactersScrollPane);

        this.add(topRightPanel, BorderLayout.NORTH);
    }

    private List<String> getSortedActiveCharacterNames() {
        Set<String> sortedNames = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        for (String roomName : maze.getRoomNames()) {
            sortedNames.addAll(maze.getCharacters(roomName));
        }
        return sortedNames.stream().toList();
    }

    private Dimension getActiveCharactersListPreferredSize(Integer panelWidth, Integer panelHeight) {
        FontMetrics metrics = activeCharactersList.getFontMetrics(activeCharactersList.getFont());

        int maxTextWidth = 0;
        ListModel<String> model = activeCharactersList.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            maxTextWidth = Math.max(maxTextWidth, metrics.stringWidth(model.getElementAt(i)));
        }

        int widthPadding = 24;
        int scrollBarAllowance = 18;
        int preferredWidth = maxTextWidth + widthPadding + scrollBarAllowance;

        int rowHeight = Math.max(metrics.getHeight(), activeCharactersList.getFixedCellHeight() > 0
                ? activeCharactersList.getFixedCellHeight()
                : metrics.getHeight());
        int preferredHeight = Math.max(rowHeight + 8, model.getSize() * rowHeight + 8);
        int maxHeight = Math.max(panelHeight / 3, rowHeight + 8);

        return new Dimension(
                Math.min(preferredWidth, panelWidth / 2),
                Math.min(preferredHeight, maxHeight)
        );
    }

    void paintRoomCenteredAt(Graphics2D g2, Point roomCenter, String roomName, int numNeighbors) {
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

        String nameWithNeighborCount = roomName + " (" + numNeighbors + ")";
        paintRoomName(g2, nameWithNeighborCount, upperRightCorner);
        paintRoomContents(g2, roomName, upperRightCorner);
        paintCharacters(g2, roomName, new Point(roomCenter));
    }

    private void paintCharacters(Graphics2D g2, String roomName, Point imageLocation) {
        Integer fontHeight = g2.getFontMetrics().getHeight();
        Point upperRightCorner = new Point(imageLocation.x + roomDimension / 2, imageLocation.y - roomDimension / 2);

        for (String characterName : maze.getCharacters(roomName)) {
            String imageKey = normalizeImageName(characterName);
            logger.info("Trying to find an image with key: {}", imageKey);
            Image characterImage = characterImages.get(imageKey);
            if (characterImage == null) {
                logger.info("No image found for character: {}", characterName);
                paintCharacterName(g2, characterName, upperRightCorner);
                upperRightCorner.y += fontHeight + 2;
            } else {
                g2.drawImage(characterImage, imageLocation.x, imageLocation.y, 50, 50, this);
                imageLocation.x -= 50;
            }
        }
    }

    private void paintCharacterName(Graphics2D g2, String name, Point upperRightCorner) {
        Integer fontHeight = g2.getFontMetrics().getHeight();
        Integer yPosition = upperRightCorner.y + fontHeight * 2 + 2;
        g2.setFont(ROOM_CONTENTS_FONT);
        g2.drawString(name, upperRightCorner.x - 25, yPosition);
    }

    private void paintRoomContents(Graphics2D g2, String roomName, Point upperLeftCorner) {
        Integer fontHeight = g2.getFontMetrics().getHeight();
        Integer yPosition = upperLeftCorner.y + fontHeight * 2 + 2;
        g2.setFont(ROOM_CONTENTS_FONT);
        for (String desc : maze.getArtifacts(roomName)) {
            g2.drawString(desc, upperLeftCorner.x + 5, yPosition);
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
        Image roomImage = roomImages.get(roomName);
        g2.drawImage(roomImage, upperRightCorner.x, upperRightCorner.y, roomDimension, roomDimension, this);
    }

    void paintMaze(Graphics2D g2) {
        drawRoomConnections(g2);
        for (String currentRoom : roomLocations.keySet()) {
            int numNeighbors = maze.getNeighborsOf(currentRoom).size();
            paintRoomCenteredAt(g2, roomLocations.get(currentRoom), currentRoom, numNeighbors);
        }
    }

    private void drawRoomConnections(Graphics2D g2) {
        g2.setColor(CONNECTOR_COLOR);
        for (String currentRoom : maze.getRoomNames()) {
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
        Point roomBoundaryLocation = new Point((int) (roomLocation.x - deltaX), (int) (roomLocation.y - deltaY));
        Point neighborBoundaryLocation = new Point((int) (neighborLocation.x + deltaX), (int) (neighborLocation.y + deltaY));

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
