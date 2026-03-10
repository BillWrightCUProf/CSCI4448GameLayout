package ooad.alternatemazeobserver;

import ooad.gameobserver.intf.IGameObserver;
import ooad.gameobserver.intf.IMaze;
import ooad.gameobserver.intf.IMazeSubject;

import javax.swing.*;
import java.awt.*;

public class AlternateGraphicalMazeObserver implements IGameObserver {
    public static final int MINIMUM_WIDTH = 500;
    private static final String GAME_END_MESSAGE = "game end";
    public static int DEFAULT_UPDATE_DELAY_IN_SECONDS = 1;

    static {
        System.out.println("MazeObserver class is being loaded");
    }

    private final IMazeSubject mazeSubject;

    JFrame window;
    MazePanel mazePanel = new MazePanel("");
    StatusPanel statusPanel = new StatusPanel("");
    JPanel mainPanel = new JPanel(new GridLayout(2, 1));

    Integer delayInSecondsAfterDisplayUpdate = DEFAULT_UPDATE_DELAY_IN_SECONDS;
    Color backgroundColor;
    Color textColor;

    public AlternateGraphicalMazeObserver() {
        this(null, "Adventure Game");
    }

    public AlternateGraphicalMazeObserver(IMazeSubject mazeSubject) {
        this(mazeSubject, "Adventure Game");
    }

    public AlternateGraphicalMazeObserver(IMazeSubject mazeSubject, String title) {
        this.mazeSubject = mazeSubject;
        window = new JFrame(title);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setLocationRelativeTo(null);
        mainPanel.add(statusPanel, BorderLayout.NORTH);
        statusPanel.setSize(window.getWidth(), 100);
        mainPanel.add(mazePanel, BorderLayout.SOUTH);
        mazePanel.setSize(window.getWidth(), window.getHeight() - 110);
    }

    void setTitle(String title) {
        window.setTitle(title);
    }

    @Override
    public void update(String statusMessage) {
        if (statusPanel != null) {
            window.remove(statusPanel);
        }
        statusPanel = new StatusPanel(statusMessage);
        window.add(statusPanel, BorderLayout.NORTH);

        if (mazeSubject != null && mazeSubject.getMaze() != null) {
            updateMazePanel(mazeSubject.getMaze());
            window.add(mazePanel, BorderLayout.CENTER);
        }

        window.pack();
        window.setVisible(true);

        // Ensure some white space around the maze for better readability
        int width = Math.max((int) (window.getWidth() * 1.2), MINIMUM_WIDTH);
        window.setSize(width, (int) (window.getHeight() * 1.2));

        try {
            Thread.sleep(delayInSecondsAfterDisplayUpdate * 1000);
        } catch (InterruptedException ex) {
            System.out.println("Display was interrupted...");
        }

        if (statusMessage.toLowerCase().contains(GAME_END_MESSAGE)) {
            JOptionPane.showMessageDialog(null, "Click OK to continue");
        }
    }

    private void updateMazePanel(IMaze maze) {
        if (mazePanel != null) {
            window.remove(mazePanel);
        }
        mazePanel = new MazePanel(maze.toString());

        if (backgroundColor != null) {
            mazePanel.setBackground(backgroundColor);
        }
        if (textColor != null) {
            mazePanel.setForeground(textColor);
        }
    }
}
