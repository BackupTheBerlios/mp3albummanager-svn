package org.app.mp3AlbumManager.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.awt.Desktop;
import java.io.IOException;

/**
 * Class to lauch a web browser.
 */
public class LaunchBrowser {

    private String url;

    /**
     * Create a new launch browser object.
     * @param url The url to open in web browser.
     */
    public LaunchBrowser(String url) {

        this.url = url;
    }

    /**
     * Launch the browser.
     */
    public void launch() {

        try {
            Desktop.getDesktop().browse(new URI("file://"+url));
        } catch (IOException e) { e.printStackTrace();
        } catch (URISyntaxException e) { e.printStackTrace();
        }
        
    }
}
