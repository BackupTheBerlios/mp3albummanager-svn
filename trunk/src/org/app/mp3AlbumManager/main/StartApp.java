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

        SwingUtilities.invokeLater(
                new Runnable() {

                    String theme;
                    Color bgColor;
                    boolean verbose;

                    public void run() {
                        // Create an instance of HandlePropertoes to read properties from config file.
                        HandleProperties handler = new HandleProperties();
                        if( handler.getProperties() ) {
                            theme = handler.getThemePath();
                            bgColor = handler.getBgColor();
                            verbose = handler.getVerbose();
                        }
                        setTheme(theme);
                        createAndShowGUI(verbose, bgColor);
                    }
                }
        );
    }

    private static void setTheme(String theme) {
        try {
            UIManager.setLookAndFeel(theme);

        } catch (ClassNotFoundException e) { System.err.println("Error setting theme: ClassNotFoundException");
        } catch (InstantiationException e) { System.err.println("Error setting theme: InstantiationException");
        } catch (IllegalAccessException e) { System.err.println("Error setting theme: IllegalAccessException");
        } catch (UnsupportedLookAndFeelException e) { System.err.println("Error setting theme: UnsupportedLookAndFeelException");
        }
    }

    /**
     * Create the GUI for the application.
     * @param verbose Output verbose info to stdout.
     * @param bg Background color of the panels.
     */
    private static void createAndShowGUI(boolean verbose, Color bg) {

        View view = new View();
        Model model = new Model(verbose);

        boolean anyRecent = model.getRecentEntries();
        new Controller(model, view, anyRecent, verbose, bg);

    }

}
