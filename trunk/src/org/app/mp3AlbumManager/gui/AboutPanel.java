package org.app.mp3AlbumManager.gui;

import com.jeta.forms.components.panel.FormPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.net.MalformedURLException;

import org.app.mp3AlbumManager.util.LaunchBrowser;
import org.app.mp3AlbumManager.util.ImageLoader;

public class AboutPanel extends JPanel implements ActionListener {

    public static final String ID_LOGOLABEL = "logoLabel";  //com.jeta.forms.components.label.JETALabel
    public static final String ID_APPLABEL = "appLabel";  //com.jeta.forms.components.label.JETALabel

    // the form panel
    private FormPanel aboutPanel;

    // panel components
    private JLabel logoLabel;
    private JLabel appLabel;
    private CLabel clickUrlLabel;

    private static final String IMAGE_PATH = "res/graphics/about_logo.gif";

    private Color bgcolor;
    private String name;
    private String version;
    private String homepage;

    public AboutPanel(Color color, String name, String version, String homepage) {

        aboutPanel = new FormPanel("org/app/mp3AlbumManager/forms/aboutPanel.jfrm");

        bgcolor = color;
        this.name = name;
        this.version = version;
        this.homepage = homepage;

        initComponents();

    }

    private void initComponents() {

        logoLabel = aboutPanel.getLabel(ID_LOGOLABEL);
        appLabel = aboutPanel.getLabel(ID_APPLABEL);

        //======== logoLabel ========
        ImageIcon logo = new ImageIcon(IMAGE_PATH);
        logoLabel.setIcon(logo);

        //======== appLabel ========
        appLabel.setText(name + " " + version);

        //======== clickUrlLabel ========
        clickUrlLabel = new CLabel(homepage);

        clickUrlLabel.setClickable(true, this, null);


        //======== this ========
        setBackground(bgcolor);
        setVisible(true);

        //LAYOUT
        setLayout( new BorderLayout() );
        add(aboutPanel, BorderLayout.CENTER);
        add(clickUrlLabel, BorderLayout.SOUTH);


    }

    // action for clickable url label
    public void actionPerformed(ActionEvent e) {
        if( e.getSource() == clickUrlLabel ) {
            try {
                URL url = new URL(homepage);
                LaunchBrowser browser = new LaunchBrowser(url);
                browser.launchUrl();
            } catch (MalformedURLException male) {
                male.printStackTrace();
            }
        }
    }
}
