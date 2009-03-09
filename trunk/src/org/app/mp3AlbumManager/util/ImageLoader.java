package org.app.mp3AlbumManager.util;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class ImageLoader {

    public ImageIcon getImage(String path) {
        URL imgURL = getClass().getResource(path);
        if(imgURL == null) {
            System.err.println("Error: Finding image path\n\t" + path);
            return null;
        }

        return new ImageIcon( Toolkit.getDefaultToolkit().getImage(imgURL) );
    }
}
