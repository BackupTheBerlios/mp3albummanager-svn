package org.app.mp3AlbumManager.main;

import org.app.mp3AlbumManager.gui.View;
import org.app.mp3AlbumManager.gui.Controller;
import org.app.mp3AlbumManager.gui.Model;
import org.app.mp3AlbumManager.util.HandleProperties;

import javax.swing.*;
import java.awt.*;

/**
 * The starting point for the application.
 */
public class StartApp {

    public static void main(String[] args) {

        // Create an instance of HandlePropertoes to read properties from config file.
        HandleProperties handler = new HandleProperties();
        String theme;
        Color bgColor;
        boolean verbose;
        
        if( handler.getProperties() ) {
            theme = handler.getThemePath();
            bgColor = handler.getBgColor();
            verbose = handler.getVerbose();
            try {
                UIManager.setLookAndFeel(theme);
            } catch (ClassNotFoundException e) { e.printStackTrace();
            } catch (InstantiationException e) { e.printStackTrace();
            } catch (IllegalAccessException e) { e.printStackTrace();
            } catch (UnsupportedLookAndFeelException e) { e.printStackTrace();
            }

            createAndShowGUI(verbose, bgColor);

        }
    }

    /**
     * Create the GUI for the application.
     * TODO: implement the color parameter
     *
     * @param verbose Output verbose info to stdout.
     * @param bg Background color of the panels.
     */
    private static void createAndShowGUI(boolean verbose, Color bg) {

        View view = new View();
        Model model = new Model(verbose);

        boolean anyRecent = model.getRecentEntries();
        new Controller(model, view, anyRecent, verbose);

    }

}
