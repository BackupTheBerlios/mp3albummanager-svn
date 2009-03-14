package org.app.mp3AlbumManager.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

import com.jeta.forms.components.panel.FormPanel;

/**
 * @author Georg Wallin
 */
public class OpenDBPanel extends JPanel {

    public static final String ID_DBFIELD = "dbField";  //javax.swing.JTextField
    public static final String ID_MUSICFIELD = "musicField";  //javax.swing.JTextField
    public static final String ID_PASSFIELD = "passField";  //javax.swing.JTextField
    public static final String ID_USERFIELD = "userField";  //javax.swing.JTextField
    public static final String ID_COLLECTIONBOX = "collectionBox";  //javax.swing.JComboBox
    public static final String ID_OPENBUTTON = "openButton";  //javax.swing.JButton
    public static final String ID_MESSAGELABEL = "messageLabel";  //com.jeta.forms.components.label.JETALabel

    // the form panel
    private FormPanel openPanel;

    // panel components
    private JTextField dbField;
    private JTextField musicField;
    private JTextField passField;
    private JTextField userField;
    private JComboBox collectionBox;
    private AbstractButton openButton;
    private JLabel messageLabel;

    private Color bgcolor;

    public OpenDBPanel(Color color) {

        openPanel = new FormPanel("org/app/mp3AlbumManager/forms/openPanel.jfrm");

        bgcolor = color;
        initComponents();
    }

    private void initComponents() {

        userField = openPanel.getTextField(ID_USERFIELD);
        passField = openPanel.getTextField(ID_PASSFIELD);
        musicField = openPanel.getTextField(ID_MUSICFIELD);
        dbField = openPanel.getTextField(ID_DBFIELD);
        collectionBox = openPanel.getComboBox(ID_COLLECTIONBOX);
        openButton = openPanel.getButton(ID_OPENBUTTON);
        messageLabel = openPanel.getLabel(ID_MESSAGELABEL);
       
        //======== this ========
        setBackground(bgcolor);

        //LAYOUT
        setLayout( new BorderLayout() );
        add( openPanel, BorderLayout.CENTER );
    }

    public void showPanel() {
        setVisible(true);
        super.repaint();
    }

    public void populateComboBox(String[] arr) {
        // populate combobox with string array
        //collectionBox.setMaximumRowCount(arr.length);
        for(String val : arr) { collectionBox.addItem(val); }
        // autoselect the first item
        collectionBox.setSelectedIndex(0);
    }

    public String getSelectedDatabase() { return (String) collectionBox.getSelectedItem(); }

    public void setMusicField(String text) { musicField.setText(text); }

    public String getMusicField() { return musicField.getText(); }

    public void setDBDirField(String text) { dbField.setText(text); }

    public String getDBDirField() { return dbField.getText(); }

    public String getUserField() { return userField.getText(); }

    public String getPassField() { return passField.getText(); }

    public void setMessageLabel(String text) { messageLabel.setText(text); }

    public void openDBButtonListener(ActionListener al) {
        openButton.addActionListener(al);
        openButton.setActionCommand("openDB");
    }

    public void openDBItemListener(ItemListener il) { collectionBox.addItemListener(il); }

}