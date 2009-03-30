package org.app.mp3AlbumManager.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.awt.Desktop;
import java.io.IOException;

/**
 * Class to lauch a web browser.
 */
public class LaunchBrowser {

    private String file;
    private URL url;


    /**
     * Create a new launch browser object.
     * @param file The local file to open in web browser.
     */
    public LaunchBrowser(String file) {

        this.file = file;
    }

    /**
     * Create a new launch browser object.
     * @param url The url to open in web browser.
     */
    public LaunchBrowser(URL url) {

        this.url = url;
    }

    /**
     * Launch the browser and open the local file.
     */
    public void launchFile() {

        try {
            if(file != null) Desktop.getDesktop().browse(new URI("file://"+ file));
        } catch (IOException e) { e.printStackTrace();
        } catch (URISyntaxException e) { e.printStackTrace();
        }
        
    }

    public void launchUrl() {
        if(url != null) try {
            Desktop.getDesktop().browse( url.toURI() );
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (URISyntaxException urie) {
            urie.printStackTrace();
        }
    }
}
