package org.example;

import javax.swing.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        System.out.println("Hello world!");

        logger.debug("Debug log message");
        logger.info("Info log message");
        logger.error("Error log message");

        JFrame window = new JFrame("Arcane Adventure Game");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setLocationRelativeTo(null);

        List<Room> caveRooms = Room.createRoomGrid(3);
        GamePanel gamePanel = new GamePanel(caveRooms);
        window.add(gamePanel);
        window.pack();
        window.setVisible(true);

        gamePanel.startGameThread();
    }
}