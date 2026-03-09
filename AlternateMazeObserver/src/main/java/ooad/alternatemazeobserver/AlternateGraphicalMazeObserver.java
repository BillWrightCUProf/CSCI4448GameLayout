package ooad.alternatemazeobserver;

import ooad.gameobserver.intf.IGameObserver;
import ooad.gameobserver.intf.IMaze;
import ooad.gameobserver.intf.IMazeSubject;

import javax.swing.*;
import java.awt.*;

public class AlternateGraphicalMazeObserver implements IGameObserver {
    public static final int DEFAULT_WIDTH = 800;
    public static final int DEFAULT_HEIGHT = 800;
    public static int DEFAULT_UPDATE_DELAY_IN_SECONDS = 1;

    static {
        System.out.println("MazeObserver class is being loaded");
    }

    private final IMazeSubject mazeSubject;

    JFrame window;
    MazePanel mazePanel = new MazePanel("");
    StatusPanel statusPanel = new StatusPanel("");
    JPanel mainPanel = new JPanel(new GridLayout(2, 1));

    Integer width = DEFAULT_WIDTH;
    Integer height = DEFAULT_HEIGHT;
    Integer delayInSecondsAfterDisplayUpdate = DEFAULT_UPDATE_DELAY_IN_SECONDS;
    Color backgroundColor;
    Color textColor;

    public AlternateGraphicalMazeObserver() {
        this(null,"Adventure Game");
    }

    public AlternateGraphicalMazeObserver(IMazeSubject mazeSubject) {
        this(mazeSubject,"Adventure Game");
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
        mazePanel.setSize(window.getWidth(), window.getHeight()-110);
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
        try {
            Thread.sleep(delayInSecondsAfterDisplayUpdate * 1000);
        } catch (InterruptedException ex) {
            System.out.println("Display was interrupted...");
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
