package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.util.List;

public class GameApp {
    private static final Logger logger = LogManager.getLogger(GameApp.class);

    JFrame window;
    GamePanel gamePanel;

    public GameApp(String title, List<Room> rooms, RoomLayoutStrategy layoutStrategy) {
        System.out.println("Hello world!");

        logger.debug("Debug log message");
        logger.info("Info log message");
        logger.error("Error log message");

        window = new JFrame(title);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setLocationRelativeTo(null);

        gamePanel = new GamePanel(rooms, layoutStrategy);
        window.add(gamePanel);
        window.pack();
        window.setVisible(true);
    }

    public static void main(String[] args) {
        new GameApp(
                "Arcane Adventure Game",
                Room.createRoomGrid(1),
                new GridLayoutStrategy());
//                new RadialLayoutStrategy());
    }
}