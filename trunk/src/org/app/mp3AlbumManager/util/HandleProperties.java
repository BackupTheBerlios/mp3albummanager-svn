package org.app.mp3AlbumManager.util;

import javax.swing.*;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;
import java.awt.*;

/**
 * Class to read the configuration properties from a file.
 */
public class HandleProperties {

    private static final String CONFIG_FILE = "res/config/config.properties"; // path to the configuration file
    private static final String VERBOSE_PROPERTY_KEY = "Verbose.output";
    private static final String THEME_PROPERTY_KEY = "UI.look";
    private static final String[] THEMES = {
            "GTKLookAndFeel",
            "MotifLookAndFeel",
            "NimbusLookAndFeel",
            "Default" };
    private static final Color[] BACKGROUND_COLORS = {
            new Color(201,197,189),
            new Color(174,178,195),
            new Color(214,217,223),
            new Color(238,238,238) };
    private int currentTheme; // the current theme index
    private boolean verboseValue; // verbose console ouput
    private final String themeValue; // UI look and feel

    public HandleProperties() {
        // set default values for verbose & look
        verboseValue = false;
        themeValue = "Default";
        setCurrentTheme(themeValue);
    }

    /**
     * Get the verbose value.
     * @return The verbose value.
     */
    public boolean getVerbose() { return verboseValue; }

    /**
     * Set the verbose value for the application..
     * @param val Whether to output verbose info.
     */
    public void setVerbose(boolean val) { verboseValue = val; }

    /**
     * Get the theme value.
     * @return The theme value.
     */
    public String getTheme() { return themeValue; }

    /**
     * Set the theme for the application.
     * @param name The name of the theme.
     */
    public void setCurrentTheme(String name) {
        if(name.equals(THEMES[0])) { currentTheme = 0; }
        else if(name.equals(THEMES[1])) { currentTheme = 1; }
        else if(name.equals(THEMES[2])) { currentTheme = 2; }
        else currentTheme = 3;
    }

    /**
     * Get the java path for the theme.
     * @return The path.
     */
    public String getThemePath() {
        String name = THEMES[currentTheme];
        String prefix = "";
        if(name.equals(THEMES[0])) { prefix = "com.sun.java.swing.plaf.gtk."; }
        else if(name.equals(THEMES[1])) { prefix = "com.sun.java.swing.plaf.motif."; }
        else if(name.equals(THEMES[2])) { prefix = "com.sun.java.swing.plaf.nimbus."; }
        else if(name.equals(THEMES[3]) ){ return UIManager.getCrossPlatformLookAndFeelClassName(); }
        return prefix+name;
    }

    /**
     * Get the background color of the current theme.
     * @return The background color.
     */
    public Color getBgColor() { return BACKGROUND_COLORS[currentTheme]; }

    /**
     * Get the properties from a configuration file.
     * @return Whether retrieving was successful.
     */
    public boolean getProperties() {
        // read from configuration file
        Properties configuration = new Properties();

        try {
            configuration.load(new FileInputStream(CONFIG_FILE));
            // get verbose value
            String setting = configuration.getProperty(VERBOSE_PROPERTY_KEY);
            if(setting == null) {

                // if value not found, use default value
                System.err.println("\nWarning: Missing value ("+ VERBOSE_PROPERTY_KEY +") in configuration file. Using default: "
                        + VERBOSE_PROPERTY_KEY + "=" + getVerbose() + "\n");
            } else if( setting.equalsIgnoreCase("true") ) {
                setVerbose(true);
            }
            // get look and feel value
            setting = configuration.getProperty(THEME_PROPERTY_KEY);
            boolean found = false;
            if(setting != null) {
                for(String v : THEMES) {
                    if( v.equals(setting) ) {
                        setCurrentTheme(setting);
                        found = true;
                    }
                }
            }
            // if value not found, use default value
            if(! found) {
                System.err.println("\nWarning: Missing value ("+ THEME_PROPERTY_KEY +") in configuration file. Using default: "
                        + THEME_PROPERTY_KEY + "=" + getTheme() + "\n");
            }

            return true;

        } catch(IOException e) {
            // exit if config file is not found
            System.err.println("\nError: Configuration file not found. Application will exit.\n");
            return false;
        }
    }


}
