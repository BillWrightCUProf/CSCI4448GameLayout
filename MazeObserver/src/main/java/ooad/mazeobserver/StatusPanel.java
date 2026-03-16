package ooad.mazeobserver;

import javax.swing.*;
import java.awt.*;

public class StatusPanel extends JPanel {
    public static final Font STATUS_PANEL_FONT = new Font("Arial", Font.BOLD, 18);

    JTextArea statusArea;

    public StatusPanel(String statusMessage) {
        super(new BorderLayout());
        statusArea = new JTextArea(statusMessage);
        statusArea.setFont(STATUS_PANEL_FONT);
        statusArea.setEditable(false);
        statusArea.setLineWrap(true);
        statusArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(statusArea);
        this.add(scrollPane, BorderLayout.CENTER);

    }

    public void setStatus(String statusMessage) {
        statusArea.setText(statusMessage);
        statusArea.setCaretPosition(0);
        revalidate();
        repaint();
    }
}
