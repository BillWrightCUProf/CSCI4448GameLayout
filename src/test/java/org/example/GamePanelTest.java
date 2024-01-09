package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

class GamePanelTest {

    JFrame window;

    @BeforeEach
    void createFrame() {
        window = new JFrame("testOneRoomRadialLayout");
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setLocationRelativeTo(null);
    }

    @Test
    void testOneRoomRadialLayout() {
        GamePanel gamePanel = new GamePanel(
                Room.createRoomGrid(1),
                new RadialLayoutStrategy());
        paintToFile(gamePanel, "testOneRoomRadialLayout");
    }

    @Test
    void testOneRoomGridLayout() {
        GamePanel gamePanel = new GamePanel(
                Room.createRoomGrid(1),
                new GridLayoutStrategy());
        paintToFile(gamePanel, "testOneRoomGridLayout");
    }

    @Test
    void testFourRoomRadialLayout() {
        GamePanel gamePanel = new GamePanel(
                Room.createRoomGrid(2),
                new RadialLayoutStrategy());
        paintToFile(gamePanel, "testFourRoomRadialLayout");
    }

    @Test
    void testFourRoomGridLayout() {
        GamePanel gamePanel = new GamePanel(
                Room.createRoomGrid(2),
                new GridLayoutStrategy());
        paintToFile(gamePanel, "testFourRoomGridLayout");
    }

    @Test
    void testNineRoomRadialLayout() {
        GamePanel gamePanel = new GamePanel(
                Room.createRoomGrid(3),
                new RadialLayoutStrategy());
        paintToFile(gamePanel, "testNineRoomRadialLayout");
    }

    @Test
    void testNineRoomGridLayout() {
        GamePanel gamePanel = new GamePanel(
                Room.createRoomGrid(3),
                new GridLayoutStrategy());
        paintToFile(gamePanel, "testNineRoomGridLayout");
    }

    private void paintToFile(GamePanel gamePanel, String fileName) {
        window.add(gamePanel);
        window.pack();
        window.setVisible(true);

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
            e.printStackTrace();
        }
    }

}