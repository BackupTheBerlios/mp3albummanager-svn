package org.app.mp3AlbumManager.gui;

import javax.swing.*;
import java.awt.*;

public class StartupPanel extends JPanel {

    private Color bgcolor;

    StartupPanel(Color color) {

        bgcolor = color;
        initComponents();
    }

    private void initComponents() {
        setBackground(bgcolor);
    }
}
