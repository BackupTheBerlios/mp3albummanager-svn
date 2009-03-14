package org.app.mp3AlbumManager.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import com.jeta.forms.components.panel.FormPanel;

/**
 * @author Georg Wallin
 */
public class CreateDBPanel extends JPanel {

    public static final String ID_BROWSEDB = "browseDB";  //javax.swing.JButton
    public static final String ID_BROWSEMUSIC = "browseMusic";  //javax.swing.JButton
    public static final String ID_COLLECTIONFIELD = "collectionField";  //javax.swing.JTextField
    public static final String ID_USERFIELD = "userField";  //javax.swing.JTextField
    public static final String ID_PASSFIELD = "passField";  //javax.swing.JTextField
    public static final String ID_MUSICFIELD = "musicField";  //javax.swing.JTextField
    public static final String ID_DBFIELD = "dbField";  //javax.swing.JTextField
    public static final String ID_CREATEBUTTON = "createButton";  //javax.swing.JButton
    public static final String ID_MESSAGELABEL = "messageLabel";  //javax.swing.JLabel

    // the form panel
    private FormPanel createPanel;

    // panel components
    private AbstractButton browseDB;
    private AbstractButton browseMusic;
    private JTextField collectionField;
    private JTextField userField;
    private JTextField passField;
    private JTextField musicField;
    private JTextField dbField;
    private AbstractButton createButton;
    private JLabel messageLabel;

    private Color bgcolor;

    public CreateDBPanel(Color color) {

        createPanel = new FormPanel("org/app/mp3AlbumManager/forms/createPanel.jfrm");

        bgcolor = color;
        initComponents();
    }

    private void initComponents() {

        collectionField = createPanel.getTextField(ID_COLLECTIONFIELD);
        userField = createPanel.getTextField(ID_USERFIELD);
        passField = createPanel.getTextField(ID_PASSFIELD);
        musicField = createPanel.getTextField(ID_MUSICFIELD);
        dbField = createPanel.getTextField(ID_DBFIELD);
        browseMusic = createPanel.getButton(ID_BROWSEMUSIC);
        browseDB = createPanel.getButton(ID_BROWSEDB);
        createButton = createPanel.getButton(ID_CREATEBUTTON);
        messageLabel = createPanel.getLabel(ID_MESSAGELABEL);

        //======== this ========
        setBackground(bgcolor);

        //LAYOUT
        setLayout( new BorderLayout() );
        add( createPanel, BorderLayout.CENTER );

    }

    public void showPanel() {
        setVisible(true);
        super.repaint();
    }

    public void createDBListener(ActionListener al) {
        createButton.addActionListener(al);
        createButton.setActionCommand("createDB");
        browseMusic.addActionListener(al);
        browseMusic.setActionCommand("browseMP3dir");
        browseDB.addActionListener(al);
        browseDB.setActionCommand("browseDBdir");
    }

    public String getCollectionField() { return collectionField.getText(); }

    public void setMusicField(String text) { musicField.setText(text); }

    public String getMusicField() { return musicField.getText(); }

    public void setDBDirField(String text) { dbField.setText(text); }

    public String getDBDirField() { return dbField.getText(); }

    public String getUserField() { return userField.getText(); }

    public String getPassField() { return passField.getText(); }

    public void setMessageLabel(String text) { messageLabel.setText(text); }
}
