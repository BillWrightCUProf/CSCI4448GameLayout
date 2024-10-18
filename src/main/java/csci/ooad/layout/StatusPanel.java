package csci.ooad.layout;

import javax.swing.*;
import java.awt.*;

public class StatusPanel extends JPanel {
    JTextArea statusArea;

    public StatusPanel(java.util.List<String> statusMessages) {
        super(new BorderLayout());
        statusArea = new JTextArea("no status yet");
        setStatus(statusMessages);
        statusArea.setEditable(false);
        statusArea.setLineWrap(true);
        statusArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(statusArea);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void setStatus(java.util.List<String> statusMessages) {
        statusArea.setText(String.join("\n", statusMessages));
    }

}
