package csci.ooad.layout;

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

public class MazeObserver implements IMazeObserver {
    private static final Logger logger = LoggerFactory.getLogger(MazeObserver.class);
    JFrame window;
    GamePanel gamePanel;
    RoomShape roomShape = RoomShape.IMAGE;
    IRoomLayoutStrategy layoutStrategy = new RadialLayoutStrategy();
    Integer dimension = 800;
    Integer roomDimension = 100;
    Integer delayInSecondsAfterDisplayUpdate = 1;

    public static Builder getNewBuilder(String title) {
        return new Builder(title);
    }

    private MazeObserver(String title) {
        window = new JFrame(title);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setLocationRelativeTo(null);
    }

    public static class Builder {
        MazeObserver mazeObserver;

        public Builder(String title) {
            mazeObserver = new MazeObserver(title);
        }

        public Builder setDimension(Integer dimension) {
            mazeObserver.dimension = dimension;
            return this;
        }

        public Builder setRoomDimension(Integer roomDimension) {
            mazeObserver.roomDimension = roomDimension;
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

        public MazeObserver build() {
            return mazeObserver;
        }
    }

    @Override
    public void update(IMaze maze, String statusMessage) {
        if (gamePanel != null) {
            window.remove(gamePanel);
        }
        gamePanel = new GamePanel(maze, statusMessage, layoutStrategy, roomShape, dimension, roomDimension);

        window.add(gamePanel);
        window.pack();
        window.setVisible(true);
        try {
            Thread.sleep(delayInSecondsAfterDisplayUpdate * 1000);
        } catch(InterruptedException ex) {
            logger.warn("Display was interrupted...");
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
        MazeObserver mazeObserver = MazeObserver.getNewBuilder("Arcane Adventure Game - Grid Layout")
                .useGridLayoutStrategy()
                .build();

        mazeObserver.update(ExampleMaze.createRoomGrid(2), "Created 2x2 Maze");
    }
}