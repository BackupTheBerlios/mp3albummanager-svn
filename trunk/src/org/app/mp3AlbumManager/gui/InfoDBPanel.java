package org.app.mp3AlbumManager.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import com.jeta.forms.components.panel.FormPanel;

public class InfoDBPanel extends JPanel {

    public static final String ID_MUSICLABEL = "musicLabel";  //com.jeta.forms.components.label.JETALabel
    public static final String ID_DBLABEL = "dbLabel";  //com.jeta.forms.components.label.JETALabel
    public static final String ID_NEXTBUTTON = "nextButton";  //javax.swing.JButton
    public static final String ID_UPDATEBUTTON = "updateButton";  //javax.swing.JButton
    public static final String ID_REMOVEBUTTON = "removeButton";  //javax.swing.JButton
    public static final String ID_PROGRESSBAR = "progressbar";  //javax.swing.JProgressBar

    // the form panel
    private FormPanel infoPanel;

    // panel components
    private JLabel musicLabel;
    private JLabel dbLabel;
    AbstractButton nextButton, updateButton, removeButton;
    public JProgressBar progressBar;

    private final String dbLabelText;
    private final String musicLabelText;
    private Color bgcolor;

    public InfoDBPanel(String dbLabelText, String musicLabelText, Color color) {

        infoPanel = new FormPanel("org/app/mp3AlbumManager/forms/infoPanel.jfrm");

        this.dbLabelText = dbLabelText;
        this.musicLabelText = musicLabelText;

        bgcolor = color;

        initComponents();
    }

    /**
     * Intitialize components in the panel.
     */
    private void initComponents() {

        dbLabel = infoPanel.getLabel(ID_DBLABEL);
        musicLabel = infoPanel.getLabel(ID_MUSICLABEL);
        updateButton = infoPanel.getButton(ID_UPDATEBUTTON);
        nextButton = infoPanel.getButton(ID_NEXTBUTTON);
        removeButton = infoPanel.getButton(ID_REMOVEBUTTON);
        progressBar = infoPanel.getProgressBar(ID_PROGRESSBAR);

        //======== this ========
        setBackground(bgcolor);

        //---- dbLabel ----
        dbLabel.setText(dbLabelText);

        //---- musicLabel ----
        musicLabel.setText(musicLabelText);

        //---- updateButton ----
        updateButton.setVisible(false);

        //---- progressBar ----
        progressBar.setVisible(false);

        //LAYOUT
        setLayout( new BorderLayout() );
        add( infoPanel, BorderLayout.CENTER );
    }

    /**
     * Set listener and action command for the buttons.
     * @param al the listener.
     */
    public void infoDBListener(ActionListener al) {
        nextButton.addActionListener(al);
        nextButton.setActionCommand("next");
        updateButton.addActionListener(al);
        updateButton.setActionCommand("update");
        removeButton.addActionListener(al);
        removeButton.setActionCommand("remove");
    }

    /**
     * Get the form panel.
     * @return the form panel.
     */
    public FormPanel getForm() { return infoPanel; }

    /**
     * Make the panel visible.
     */
    public void showPanel() {
        setVisible(true);
        super.repaint();
    }

    /**
     * Show or hide a button in the panel.
     * @param button the button to show.
     * @param value whether enabled or disabled.
     */
    public void showButton(AbstractButton button, boolean value) {
        button.setVisible(value);
    }

    /**
     * Enable or disable a button in the panel.
     * @param button the button to hide.
     * @param value whether enabled or disabled.
     */
    public void enableButton(AbstractButton button, boolean value) {
        button.setEnabled(value);
    }

    /**
     * Set the text of the dbLabel.
     * @param text the text.
     */
    public void setDBLabelText(String text) { dbLabel.setText(text); revalidate(); }

    /**
     * Show a progress bar at the bottom of the panel (when updating the database).
     * @param max the max value of the progress bar.
     */
    public void showProgressBar(int max) {

        progressBar.setMinimum(0);
        progressBar.setMaximum(max);
        progressBar.setVisible(true);
        //progressBar.paintImmediately( progressBar.getBounds() );
        revalidate();
    }

    /**
     * Get the progress bar.
     * @return the progress bar.
     */
    public JProgressBar getTheProgressbar() { return progressBar; }
    
}