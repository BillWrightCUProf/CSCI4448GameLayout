package ooad.alternatemazeobserver;

import javax.swing.*;
import java.awt.*;

public class MazePanel extends JPanel {
    JTextArea mazeArea;

    public MazePanel(String mazeDescription) {
        super(new BorderLayout());
        mazeArea = new JTextArea(mazeDescription);
        mazeArea.setEditable(false);
        mazeArea.setLineWrap(false);

        JScrollPane scrollPane = new JScrollPane(mazeArea);
        this.add(scrollPane, BorderLayout.CENTER);
    }
}
