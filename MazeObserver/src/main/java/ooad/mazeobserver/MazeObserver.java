package ooad.mazeobserver;

import ooad.gameobserver.intf.IGameObserver;
import ooad.gameobserver.intf.IMaze;
import ooad.gameobserver.intf.IMazeSubject;
import ooad.mazeobserver.strategy.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static ooad.mazeobserver.ImageFactory.normalizeImageName;

public class MazeObserver implements IGameObserver {
    private static final Logger logger = LoggerFactory.getLogger(MazeObserver.class);
    public static final int DEFAULT_WIDTH = 800;
    public static final int DEFAULT_HEIGHT = 800;
    public static final int DEFAULT_UPDATE_DELAY_IN_SECONDS = 1;
    private static final String GAME_END_MESSAGE = "game end";

    static {
        System.out.println("MazeObserver class is being loaded");
    }

    private final IMazeSubject mazeSubject;

    JFrame window;
    GamePanel gamePanel;
    StatusPanel statusPanel;

    RoomShape roomShape = RoomShape.IMAGE;
    IRoomLayoutStrategy layoutStrategy = new RadialLayoutStrategy();
    Integer width = DEFAULT_WIDTH;
    Integer height = DEFAULT_HEIGHT;
    Integer roomWidth;
    Double ratioOfSpacingToRoomWidth = 1.0;
    Integer delayInSecondsAfterDisplayUpdate = DEFAULT_UPDATE_DELAY_IN_SECONDS;
    Color backgroundColor;
    Color roomBackgroundColor;
    Color textColor;
    Map<String, Point> roomLocations = new HashMap<>();
    Map<String, Image> roomImages = new HashMap<>();
    Map<String, Image> characterImages = new HashMap<>();
    ImageFactory imageFactory;

    public static Builder getNewBuilder(IMazeSubject mazeSubject, String title) {
        return new Builder(mazeSubject, title);
    }

    public MazeObserver() {
        this(null, "Adventure Game");
    }

    public MazeObserver(IMazeSubject mazeSubject) {
        this(mazeSubject, "Adventure Game");
    }

    public MazeObserver(IMazeSubject mazeSubject, String title) {
        this.mazeSubject = mazeSubject;
        window = new JFrame(title);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(false);
//        window.setLocationRelativeTo(null);
        window.setLayout(new BorderLayout());
        statusPanel = new StatusPanel("Welcome to the game!");
        window.add(statusPanel, BorderLayout.NORTH);
        imageFactory = ImageFactory.getInstance();
    }

    public static class Builder {
        MazeObserver mazeObserver;

        private Builder(IMazeSubject mazeSubject, String title) {
            mazeObserver = new MazeObserver(mazeSubject, title);
        }

        public Builder setDimension(Integer dimension) {
            mazeObserver.width = dimension;
            mazeObserver.height = dimension;
            return this;
        }

        public Builder setWidth(Integer width) {
            mazeObserver.width = width;
            return this;
        }

        public Builder setHeight(Integer height) {
            mazeObserver.height = height;
            return this;
        }

        public Builder setRoomBackgroundColor(Color roomBackgroundColor) {
            mazeObserver.roomBackgroundColor = roomBackgroundColor;
            return this;
        }

        public Builder setTextColor(Color textColor) {
            mazeObserver.textColor = textColor;
            return this;
        }

        public Builder setRoomDimension(Integer roomDimension) {
            mazeObserver.roomWidth = roomDimension;
            return this;
        }

        public Builder setRatioOfSpacingToRoomWidth(Double ratioOfSpacingToRoomWidth) {
            mazeObserver.ratioOfSpacingToRoomWidth = ratioOfSpacingToRoomWidth;
            return this;
        }

        public Builder setTitle(String title) {
            mazeObserver.setTitle(title);
            return this;
        }

        public Builder setLayoutStrategy(IRoomLayoutStrategy layoutStrategy) {
            mazeObserver.layoutStrategy = layoutStrategy;
            return this;
        }

        public Builder setRoomShape(RoomShape roomShape) {
            mazeObserver.roomShape = roomShape;
            return this;
        }

        public Builder useRadialLayoutStrategy() {
            mazeObserver.layoutStrategy = new RadialLayoutStrategy();
            return this;
        }

        public Builder useGridLayoutStrategy() {
            mazeObserver.layoutStrategy = new GridLayoutStrategy();
            return this;
        }

        public Builder useInLineLayoutStrategy() {
            mazeObserver.layoutStrategy = new LineLayoutStrategy();
            return this;
        }

        public Builder useSquareRooms() {
            mazeObserver.roomShape = RoomShape.SQUARE;
            return this;
        }

        public Builder useCircleRooms() {
            mazeObserver.roomShape = RoomShape.CIRCLE;
            return this;
        }

        public Builder useImageRooms() {
            mazeObserver.roomShape = RoomShape.IMAGE;
            return this;
        }

        public Builder setDelayInSecondsAfterUpdate(Integer delayInSeconds) {
            mazeObserver.delayInSecondsAfterDisplayUpdate = delayInSeconds;
            return this;
        }

        public Builder setBackgroundColor(Color backgroundColor) {
            mazeObserver.backgroundColor = backgroundColor;
            return this;
        }

        public MazeObserver build() {
            return mazeObserver;
        }

        public Builder useSmartLayoutStrategy() {
            this.setLayoutStrategy(new MixedLayoutStrategy());
            return this;
        }
    }

    void setTitle(String title) {
        window.setTitle(title);
    }

    @Override
    public void update(String statusMessage) {
        // This needs to be invoked on the event dispatch thread for thread safety.
        Runnable uiUpdate = getRunnableUIUpdate(statusMessage);

        if (SwingUtilities.isEventDispatchThread()) {
            uiUpdate.run();
        } else {
            try {
                SwingUtilities.invokeAndWait(uiUpdate);
            } catch (Exception e) {
                logger.error("Failed to update maze UI", e);
            }
        }

        sleepAFewSecondsSoUserCanObserveMaze(delayInSecondsAfterDisplayUpdate);
    }

    private void sleepAFewSecondsSoUserCanObserveMaze(int sleepSeconds) {
        try {
            Thread.sleep(sleepSeconds * 1000L);
        } catch (InterruptedException ex) {
            logger.warn("Display was interrupted...");
            Thread.currentThread().interrupt();
        }
    }

    private Runnable getRunnableUIUpdate(String statusMessage) {
        Runnable uiUpdate = () -> {
            statusPanel.setStatus(statusMessage);

            if (mazeSubject != null) {
                IMaze maze = mazeSubject.getMaze();
                prepareMazeDisplay(maze);

                if (gamePanel == null) {
                    gamePanel = createGamePanel(maze);
                    window.add(gamePanel, BorderLayout.CENTER);
                } else {
                    updateGamePanel(maze);
                }
            }

            window.pack();
            window.setVisible(true);
        };
        return uiUpdate;
    }

    private void prepareMazeDisplay(IMaze maze) {
        setRoomLocations(maze);
        setRoomImages(maze);
        setCharacterImages(maze);
    }

    private GamePanel createGamePanel(IMaze maze) {
        GamePanel panel = new GamePanel(maze, roomLocations, roomImages, characterImages, roomShape, width, height, roomWidth);
        applyPanelColors(panel);
        return panel;
    }

    private void updateGamePanel(IMaze maze) {
        gamePanel.updateMaze(maze, roomLocations, roomImages, characterImages, roomShape, width, height, roomWidth);
    }

    private void applyPanelColors(GamePanel panel) {
        if (backgroundColor != null) {
            panel.setBackground(backgroundColor);
        }
        if (roomBackgroundColor != null) {
            panel.setRoomBackground(roomBackgroundColor);
        }
        if (textColor != null) {
            panel.setTextColor(textColor);
        }
    }

    private void setRoomLocations(IMaze maze) {
        if (roomLocations.isEmpty()) {
            if (roomWidth == null) {
                // The room width is set so that the distance between the rooms is spacing-to-room-width-ratio times the room width
                // If spacing-to-room-width-ratio = 1.5, then:
                // panelWidth = roomWidth*numRooms + (roomWidth*1.5)*(numRooms-1)
                // or, panelWidth = rW*nR + 1.5 * rW *nR - 1.5*rW*nR
                // roomWidth = panelWidth/(n + 1.5(n-1))
                // roomWidth = panelWidth/(2.5n - 1.5)
                roomWidth = Math.toIntExact(Math.round(width / (((ratioOfSpacingToRoomWidth + 1) * maze.getRoomNames().size()) - 1.5)));
                roomWidth = Math.min(roomWidth, width);
            }
            roomLocations = layoutStrategy.calculateRoomLocations(
                    maze, width, height, roomWidth);
        }
    }

    private void setRoomImages(IMaze maze) {
        if (roomImages.isEmpty() && roomShape == RoomShape.IMAGE) {
            for (String room : maze.getRoomNames()) {
                roomImages.put(room, imageFactory.getNextRoomImage(room));
            }
        }
    }

    private void setCharacterImages(IMaze maze) {
        if (characterImages.isEmpty()) {
            for (String room : maze.getRoomNames()) {
                for (String characterName : maze.getCharacters(room)) {
                    String normalizedName = normalizeImageName(characterName);
                    Image image = imageFactory.getNextCharacterImage(normalizedName);
                    if (image != null) {
                        characterImages.put(normalizedName, image);
                    } else {
                        logger.warn("No image found for character: {}", characterName);
                    }
                }
            }
        }
    }

    public void paintToFile(String fileName) {
        if (gamePanel == null) {
            logger.warn("Game panel is null. Cannot save image.");
            return;
        }
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        gamePanel.paintComponent(g2d);
        g2d.dispose();

        // Save the image to a file
        try {
            File outputFile = new File(fileName + ".png");
            ImageIO.write(image, "png", outputFile);
            System.out.println("Image saved successfully.");
        } catch (IOException e) {
            logger.error(String.join("\n", Arrays.stream(e.getStackTrace()).map(Object::toString).toList()));
        }
    }

//    public static void main(String[] args) {
//        JFrame frame = new JFrame("Example");
//        StatusPanel statusPanel = new StatusPanel();
//        StatusPanel statusPanel2 = new StatusPanel();
//
//        // Add the panel to the frame
//        frame.setSize(500, 800);
//        frame.add(statusPanel);
////        frame.add(new GamePanel());
//
//        // Set frame properties
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setLocationRelativeTo(null);
////        frame.pack();
//        frame.setVisible(true);
//
//        try {
//            Thread.sleep(3 * 1000);
//            statusPanel.setStatus(Arrays.asList("new message 1", "new message two"));
//        } catch (InterruptedException ex) {
//            logger.warn("Display was interrupted...");
//        }
//    }
//
//     Create an animation to move a character component (create
//     this class) from its current location to a new location. It
//     should move along a straight line between the points.
//     The animation stops when the character is at the ending position.
//    def createAnimation(Component characterComponent,
//                        Location ending,
//                        Integer durationMilliseconds, Integer updateIntervalMilliseconds) {
//        Timer timer = new Timer(updateIntervalMilliseconds, new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                x += 5;
//                repaint();
//            }
//        });
//        timer.start();
//        timer.stop();
//
//    }
}