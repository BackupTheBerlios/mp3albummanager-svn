package org.app.mp3AlbumManager.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;

import com.jeta.forms.components.panel.FormPanel;


public class DetailsPanel extends JPanel implements ItemListener {

    public static final String ID_TRACKTEXTFIELD = "trackTextField";  //javax.swing.JTextField
    public static final String ID_TITLETEXTFIELD = "titleTextField";  //javax.swing.JTextField
    public static final String ID_ARTISTTEXTFIELD = "artistTextField";  //javax.swing.JTextField
    public static final String ID_LENGTHTEXTFIELD = "lengthTextField";  //javax.swing.JTextField
    public static final String ID_SUBDIRTEXTFIELD = "subdirTextField";  //javax.swing.JTextField
    public static final String ID_YEARTEXTFIELD = "yearTextField";  //javax.swing.JTextField
    public static final String ID_GENRETEXTFIELD = "genreTextField";  //javax.swing.JTextField
    public static final String ID_TAGTEXTFIELD = "tagTextField";  //javax.swing.JTextField
    public static final String ID_LAMETEXTFIELD = "lameTextField";  //javax.swing.JTextField
    public static final String ID_BITRATETEXTFIELD = "bitrateTextField";  //javax.swing.JTextField
    public static final String ID_FREQUENCYTEXTFIELD = "frequencyTextField";  //javax.swing.JTextField
    public static final String ID_MODETEXTFIELD = "modeTextField";  //javax.swing.JTextField

    public static final String ID_ISVARIOUSRADIOBUTTON = "isVariousRadioButton";  //javax.swing.JRadioButton
    public static final String ID_ISNOTVARIOUSRADIOBUTTON = "isNotVariousRadioButton";  //javax.swing.JRadioButton
    public static final String ID_ISVBRRADIOBUTTON = "isVbrRadioButton";  //javax.swing.JRadioButton
    public static final String ID_ISNOTVBRRADIOBUTTON = "isNotVbrRadioButton";  //javax.swing.JRadioButton

    public static final String ID_MODECOMBOBOX = "modeComboBox";  //javax.swing.JComboBox
    public static final String ID_FREQUENCYCOMBOBOX = "frequencyComboBox";  //javax.swing.JComboBox
    public static final String ID_BITRATECOMBOBOX = "bitrateComboBox";  //javax.swing.JComboBox
    public static final String ID_LAMECOMBOBOX = "lameComboBox";  //javax.swing.JComboBox
    public static final String ID_TAGCOMBOBOX = "tagComboBox";  //javax.swing.JComboBox
    public static final String ID_GENRECOMBOBOX = "genreComboBox";  //javax.swing.JComboBox
    public static final String ID_YEARCOMBOBOX = "yearComboBox";  //javax.swing.JComboBox

    public static final String ID_CANCELBUTTON = "cancelButton";  //javax.swing.JButton
    public static final String ID_UPDATEBUTTON = "updateButton";  //javax.swing.JButton

    // the form panel
    private FormPanel detailsPanel;

    // panel components
    private JTextField trackTextField, subdirTextField, lengthTextField;
    private JTextField artistTextField, titleTextField, yearTextField, genreTextField;
    private JTextField tagTextField, lameTextField, bitrateTextField, frequencyTextField, modeTextField;
    private JTextField[] editableFields;
    private JRadioButton isVariousRadioButton, isNotVariousRadioButton;
    private JRadioButton isVbrRadioButton, isNotVbrRadioButton;
    private JComboBox yearComboBox, genreComboBox, tagComboBox, lameComboBox;
    private JComboBox bitrateComboBox, frequencyComboBox, modeComboBox;
    private JComboBox[] comboBoxes;
    private AbstractButton cancelButton, updateButton;

    private Color bgcolor;

    public DetailsPanel(Color color) {

        detailsPanel = new FormPanel("org/app/mp3AlbumManager/forms/detailsPanel.jfrm");

        bgcolor = color;
        initComponents();
    }

    public void showPanel() {
        setVisible(true);
        super.repaint();
    }
    
    private void initComponents() {

        trackTextField = detailsPanel.getTextField(ID_TRACKTEXTFIELD);
        artistTextField = detailsPanel.getTextField(ID_ARTISTTEXTFIELD);
        titleTextField = detailsPanel.getTextField(ID_TITLETEXTFIELD);
        subdirTextField = detailsPanel.getTextField(ID_SUBDIRTEXTFIELD);
        lengthTextField = detailsPanel.getTextField(ID_LENGTHTEXTFIELD);
        yearTextField = detailsPanel.getTextField(ID_YEARTEXTFIELD);
        genreTextField = detailsPanel.getTextField(ID_GENRETEXTFIELD);
        tagTextField = detailsPanel.getTextField(ID_TAGTEXTFIELD);
        lameTextField = detailsPanel.getTextField(ID_LAMETEXTFIELD);
        bitrateTextField = detailsPanel.getTextField(ID_BITRATETEXTFIELD);
        frequencyTextField = detailsPanel.getTextField(ID_FREQUENCYTEXTFIELD);
        modeTextField = detailsPanel.getTextField(ID_MODETEXTFIELD);

        editableFields = new JTextField[] { trackTextField, artistTextField, titleTextField,
            yearTextField, genreTextField, tagTextField, lameTextField, bitrateTextField,
            frequencyTextField, modeTextField };

        isVariousRadioButton = detailsPanel.getRadioButton(ID_ISVARIOUSRADIOBUTTON);
        isNotVariousRadioButton = detailsPanel.getRadioButton(ID_ISNOTVARIOUSRADIOBUTTON);
        isVbrRadioButton = detailsPanel.getRadioButton(ID_ISVBRRADIOBUTTON);
        isNotVbrRadioButton = detailsPanel.getRadioButton(ID_ISNOTVBRRADIOBUTTON);

        yearComboBox = detailsPanel.getComboBox(ID_YEARCOMBOBOX);
        genreComboBox = detailsPanel.getComboBox(ID_GENRECOMBOBOX);
        tagComboBox = detailsPanel.getComboBox(ID_TAGCOMBOBOX);
        lameComboBox = detailsPanel.getComboBox(ID_LAMECOMBOBOX);
        bitrateComboBox = detailsPanel.getComboBox(ID_BITRATECOMBOBOX);
        frequencyComboBox = detailsPanel.getComboBox(ID_FREQUENCYCOMBOBOX);
        modeComboBox = detailsPanel.getComboBox(ID_MODECOMBOBOX);

        comboBoxes = new JComboBox[] {yearComboBox, genreComboBox, tagComboBox, lameComboBox,
            bitrateComboBox, frequencyComboBox, modeComboBox};
        cancelButton = detailsPanel.getButton(ID_CANCELBUTTON);
        updateButton = detailsPanel.getButton(ID_UPDATEBUTTON);

        // set fields initially to not editable
        setEditableFields(false);
        subdirTextField.setEditable(false);
        lengthTextField.setEditable(false);

        // add listener to comboboxes
        for(JComboBox c : comboBoxes) { c.addItemListener(this); }

        //---- isVbrRadioButton ----
        isVbrRadioButton.setBackground(bgcolor);

        //---- isNotVbrRadioButton ----
        isNotVbrRadioButton.setSelected(true);
        isNotVbrRadioButton.setBackground(bgcolor);

        //---- isVariousRadioButton ----
        isVariousRadioButton.setBackground(bgcolor);

        //---- isNotVariousRadioButton ----
        isNotVariousRadioButton.setSelected(true);
        isNotVariousRadioButton.setBackground(bgcolor);

        // initially hide the edit buttons and the comboboxes
        showEditButtons(false);
        showComboBoxes(false);

        //======== this ========
        setBackground(bgcolor);

        //LAYOUT
        setLayout( new BorderLayout() );
        add( detailsPanel, BorderLayout.CENTER );
    }

    public void setDetailsButtonListener(ActionListener al) {

        cancelButton.addActionListener(al);
        cancelButton.setActionCommand("cancelEdit");
        updateButton.addActionListener(al);
        updateButton.setActionCommand("updateEdit");
    }

    // GETTERS

    public String getTrackTextField() { return trackTextField.getText(); }

    public String getArtistTextField() { return artistTextField.getText(); }

    public String getTitleTextField() { return titleTextField.getText(); }

    public String getYearTextField() { return yearTextField.getText(); }

    public String getGenreTextField() { return genreTextField.getText(); }

    public String getTagTextField() { return tagTextField.getText(); }

    public String getLameTextField() { return lameTextField.getText(); }

    public String getBitrateTextField() { return bitrateTextField.getText(); }

    public String getFrequencyTextField() { return frequencyTextField.getText(); }

    public String getModeTextField() { return modeTextField.getText(); }

    public boolean getVarious() { return isVariousRadioButton.isSelected(); }

    public boolean getVbr() { return isVbrRadioButton.isSelected(); }


    public String getYearComboBox() { return (String) yearComboBox.getSelectedItem(); }

    public String getGenreComboBox() { return (String) genreComboBox.getSelectedItem(); }

    public String getTagComboBox() { return (String) tagComboBox.getSelectedItem(); }

    public String getLameComboBox() { return (String) lameComboBox.getSelectedItem(); }

    public String getBitrateComboBox() { return (String) bitrateComboBox.getSelectedItem(); }

    public String getFrequencyComboBox() { return (String) frequencyComboBox.getSelectedItem(); }

    public String getModeComboBox() { return (String) modeComboBox.getSelectedItem(); }

    // SETTERS

    public void setTrackTextField(String text) { trackTextField.setText(text); }

    public void setArtistTextField(String text) { artistTextField.setText(text); }

    public void setTitleTextField(String text) { titleTextField.setText(text); }

    public void setSubdirTextField(String text) { subdirTextField.setText(text); }

    public void setLengthTextField(String text) { lengthTextField.setText(text); }

    public void setYearTextField(String text) { yearTextField.setText(text); }

    public void setGenreTextField(String text) { genreTextField.setText(text); }

    public void setTagTextField(String text) { tagTextField.setText(text); }

    public void setLameTextField(String text) { lameTextField.setText(text); }

    public void setBitrateTextField(String text) { bitrateTextField.setText(text); }

    public void setFrequencyTextField(String text) { frequencyTextField.setText(text); }

    public void setModeTextField(String text) { modeTextField.setText(text); }

    public void setVbr() { isVbrRadioButton.setSelected(true); }

    public void setNotVbr() { isNotVbrRadioButton.setSelected(true); }

    public void setVarious() { isVariousRadioButton.setSelected(true); }

    public void setNotVarious() { isNotVariousRadioButton.setSelected(true); }

    public void addToYearComboBox(String s) { yearComboBox.addItem(s); }

    public void addToGenreComboBox(String s) { genreComboBox.addItem(s); }

    public void addToTagComboBox(String s) { tagComboBox.addItem(s); }

    public void addToLameComboBox(String s) { lameComboBox.addItem(s); }

    public void addToBitrateComboBox(String s) { bitrateComboBox.addItem(s); }

    public void addToFrequencyComboBox(String s) { frequencyComboBox.addItem(s); }

    public void addToModeComboBox(String s) { modeComboBox.addItem(s); }

    public void resetAllFields() {

        for(JTextField f : editableFields) { f.setText(""); }
        subdirTextField.setText("");
        lengthTextField.setText("");
        setNotVarious();
        setNotVbr();
        for(JComboBox c : comboBoxes) { c.removeAllItems(); }
    }

    // set fields in array to editable
    public void setEditableFields(boolean value) {
        for(JTextField f : editableFields) {
            f.setEditable(value);
        }
    }

    public void showEditButtons(boolean value) {
        cancelButton.setVisible(value);
        updateButton.setVisible(value);
    }

    public void showComboBoxes(boolean value) {

        for(JComboBox c : comboBoxes) { c.setVisible(value); }
    }

    // LISTENER for comboboxes
    public void itemStateChanged(ItemEvent ie) {

        Object source = ie.getItemSelectable();

        if( source == yearComboBox ) {
            yearTextField.setText( getYearComboBox() );
        } else if( source == genreComboBox ) {
            genreTextField.setText( getGenreComboBox() );
        } else if( source == tagComboBox ) {
            tagTextField.setText( getTagComboBox() );
        } else if( source == lameComboBox ) {
            lameTextField.setText( getLameComboBox() );
        } else if( source == bitrateComboBox ) {
            String comboValue = getBitrateComboBox();
            if(comboValue == null) return;
            // set both vbr & bitrate
            if(comboValue.startsWith("~")) {
                bitrateTextField.setText( comboValue.substring(1) ); // omit the ~
                setVbr();
            } else {
                setNotVbr();
                bitrateTextField.setText( comboValue );
            }

        } else if( source == frequencyComboBox ) {
            frequencyTextField.setText( getFrequencyComboBox() );
        } else if( source == modeComboBox ) {
            modeTextField.setText( getModeComboBox() );
        }

    } // end listener method
}