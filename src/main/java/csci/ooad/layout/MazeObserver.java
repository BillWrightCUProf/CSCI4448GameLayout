package csci.ooad.layout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class MazeObserver implements IMazeObserver {
    private static final Logger logger = LoggerFactory.getLogger(MazeObserver.class);
    JFrame window;
    GamePanel gamePanel;
    GamePanel.RoomShape roomShape;
    IRoomLayoutStrategy layoutStrategy;

    public MazeObserver(String title, IRoomLayoutStrategy layoutStrategy) {
        this(title, layoutStrategy, GamePanel.RoomShape.CIRCLE);
    }

    public MazeObserver(String title, IRoomLayoutStrategy layoutStrategy, GamePanel.RoomShape roomShape) {
        window = new JFrame(title);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setLocationRelativeTo(null);

        this.layoutStrategy = layoutStrategy;
        this.roomShape = roomShape;
    }

    @Override
    public void update(List<IConnectedRoom> rooms) {
        if (gamePanel != null) {
            window.remove(gamePanel);
        }
        gamePanel = new GamePanel(rooms, layoutStrategy);
        window.add(gamePanel);
        window.pack();
        window.setVisible(true);
    }

    public void paintToFile(String fileName) {
        BufferedImage image = new BufferedImage(GamePanel.WIDTH, GamePanel.HEIGHT, BufferedImage.TYPE_INT_ARGB);
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
        MazeObserver mazeObserver = new MazeObserver(
                "Arcane Adventure Game",
                new GridLayoutStrategy());

        mazeObserver.update(Room.createRoomGrid(1));
    }
}