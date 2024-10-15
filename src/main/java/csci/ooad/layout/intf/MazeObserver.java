package csci.ooad.layout.intf;

import csci.ooad.layout.*;
import csci.ooad.layout.example.ExampleMaze;
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
import java.util.Set;

public class MazeObserver implements IMazeObserver {
    private static final Logger logger = LoggerFactory.getLogger(MazeObserver.class);

    static {
        System.out.println("MazeObserver class is being loaded");
    }

    JFrame window;
    GamePanel gamePanel;
    RoomShape roomShape = RoomShape.IMAGE;
    IRoomLayoutStrategy layoutStrategy = new RadialLayoutStrategy();
    Integer dimension = 800;
    Integer roomDimension = 100;
    Integer delayInSecondsAfterDisplayUpdate = 1;
    Color backgroundColor;
    Color roomBackgroundColor;
    Color textColor;
    Map<String, Point> roomLocations = new HashMap<>();
    Map<String, Image> roomImages = new HashMap<>();
    ImageFactory imageFactory;

    public static Builder getNewBuilder(String title) {
        return new Builder(title);
    }

    public MazeObserver() {
        this("Adventure Game");
    }

    public MazeObserver(String title) {
        window = new JFrame(title);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setLocationRelativeTo(null);
        imageFactory = ImageFactory.getInstance();
    }

    public static class Builder implements IMazeObserverBuilder {
        MazeObserver mazeObserver;

        public Builder() {
            mazeObserver = new MazeObserver("Adventure Game");
        }

        public Builder(String title) {
            mazeObserver = new MazeObserver(title);
        }

        public Builder setDimension(Integer dimension) {
            mazeObserver.dimension = dimension;
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
            mazeObserver.roomDimension = roomDimension;
            return this;
        }

        @Override
        public Builder setTitle(String title) {
            mazeObserver.setTitle(title);
            return this;
        }

        @Override
        public Builder setLayoutStrategy(IRoomLayoutStrategy layoutStrategy) {
            mazeObserver.layoutStrategy = layoutStrategy;
            return this;
        }

        @Override
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
    }

    void setTitle(String title) {
        window.setTitle(title);
    }

    @Override
    public void update(IMaze maze, java.util.List<String> statusMessages) {
        setRoomLocations(maze);
        setRoomImages(maze);

        if (gamePanel != null) {
            window.remove(gamePanel);
        }
        gamePanel = new GamePanel(maze, roomLocations, roomImages, statusMessages, roomShape, dimension, roomDimension);

        if (backgroundColor != null) {
            gamePanel.setBackground(backgroundColor);
        }
        if (roomBackgroundColor != null) {
            gamePanel.setRoomBackground(roomBackgroundColor);
        }
        if (textColor != null) {
            gamePanel.setTextColor(textColor);
        }

        window.add(gamePanel);
        window.pack();
        window.setVisible(true);
        try {
            Thread.sleep(delayInSecondsAfterDisplayUpdate * 1000);
        } catch (InterruptedException ex) {
            logger.warn("Display was interrupted...");
        }
    }

    private void setRoomLocations(IMaze maze) {
        if (roomLocations.isEmpty()) {
            roomLocations = layoutStrategy.calculateRoomLocations(
                    maze.getRooms(), dimension, roomDimension);
        }
    }

    private void setRoomImages(IMaze maze) {
        if (roomImages.isEmpty() && roomShape == RoomShape.IMAGE) {
            for (String room : maze.getRooms()) {
                roomImages.put(room, imageFactory.getNextImage(room));
            }
        }
    }

    public void paintToFile(String fileName) {
        BufferedImage image = new BufferedImage(dimension, dimension, BufferedImage.TYPE_INT_ARGB);
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

    public static void main(String[] args) {
        MazeObserver mazeObserver = MazeObserver.getNewBuilder("Adventure Game - Grid Layout")
                .useGridLayoutStrategy()
                .build();

        mazeObserver.update(ExampleMaze.createRoomGrid(2), "Created 2x2 Maze");
    }
}