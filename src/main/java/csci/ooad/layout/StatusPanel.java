package csci.ooad.layout;

import javax.swing.*;
import java.awt.*;

public class StatusPanel extends JPanel {
    JTextArea statusArea;

    public StatusPanel(String statusMessage) {
        super(new BorderLayout());
        statusArea = new JTextArea("no status yet");
        statusArea.setText(statusMessage);
        statusArea.setEditable(false);
        statusArea.setLineWrap(true);
        statusArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(statusArea);
        this.add(scrollPane, BorderLayout.CENTER);
    }

}
