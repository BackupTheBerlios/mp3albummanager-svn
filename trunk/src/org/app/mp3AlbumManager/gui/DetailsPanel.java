package org.app.mp3AlbumManager.gui;

import info.clearthought.layout.TableLayout;
import info.clearthought.layout.TableLayoutConstraints;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;


public class DetailsPanel extends JPanel implements ItemListener {

    private JTextField trackTextField, subdirTextField, lengthTextField;
    private JTextField artistTextField, titleTextField, yearTextField, genreTextField;
    private JTextField tagTextField, lameTextField, bitrateTextField, frequencyTextField, modeTextField;

    private JTextField[] editableFields;

    private JRadioButton isVariousRadioButton, isNotVariousRadioButton;
    private JRadioButton isVbrRadioButton, isNotVbrRadioButton;

    private JComboBox yearComboBox, genreComboBox, tagComboBox, lameComboBox;
    private JComboBox bitrateComboBox, frequencyComboBox, modeComboBox;

    private JButton cancelButton, updateButton;

    private Color bgcolor;

    public DetailsPanel(Color color) {

        bgcolor = color;
        initComponents();
    }

    public void showPanel() {
        setVisible(true);
        super.repaint();
    }
    
    private void initComponents() {

        JLabel detailsLabel = new JLabel();
        JLabel trackLabel = new JLabel();
        JLabel artistLabel = new JLabel();
        JLabel titleLabel = new JLabel();
        JLabel variousLabel = new JLabel();
        JLabel subdirLabel = new JLabel();
        JLabel lengthLabel = new JLabel();
        JLabel yearLabel = new JLabel();
        JLabel genreLabel = new JLabel();
        JLabel tagLabel = new JLabel();
        JLabel lameLabel = new JLabel();
        JLabel bitrateLabel = new JLabel();
        JLabel vbrLabel = new JLabel();
        JLabel frequencyLabel = new JLabel();
        JLabel modeLabel = new JLabel();

        trackTextField = new JTextField();
        artistTextField = new JTextField();
        titleTextField = new JTextField();
        subdirTextField = new JTextField();
        lengthTextField = new JTextField();
        yearTextField = new JTextField();
        genreTextField = new JTextField();
        tagTextField = new JTextField();
        lameTextField = new JTextField();
        bitrateTextField = new JTextField();
        frequencyTextField = new JTextField();
        modeTextField = new JTextField();

        editableFields = new JTextField[] { trackTextField, artistTextField, titleTextField,
            yearTextField, genreTextField, tagTextField, lameTextField, bitrateTextField,
            frequencyTextField, modeTextField };

        isVariousRadioButton = new JRadioButton();
        isNotVariousRadioButton = new JRadioButton();
        isVbrRadioButton = new JRadioButton();
        isNotVbrRadioButton = new JRadioButton();

        ButtonGroup variousButtonGroup = new ButtonGroup();
        ButtonGroup vbrButtonGroup = new ButtonGroup();

        yearComboBox = new JComboBox();
        genreComboBox = new JComboBox();
        tagComboBox = new JComboBox();
        lameComboBox = new JComboBox();
        bitrateComboBox = new JComboBox();
        frequencyComboBox = new JComboBox();
        modeComboBox = new JComboBox();

        cancelButton = new JButton();
        updateButton = new JButton();



        //TableLayout
        setLayout(
            new TableLayout(
                new double[][] {
                    {
                        TableLayout.PREFERRED,
                        TableLayout.PREFERRED,
                        TableLayout.PREFERRED,
                        TableLayout.PREFERRED,
                        TableLayout.PREFERRED
                    },
                    {
                        TableLayout.PREFERRED,
                        TableLayout.PREFERRED,
                        TableLayout.PREFERRED,
                        TableLayout.PREFERRED,
                        TableLayout.PREFERRED,
                        TableLayout.PREFERRED,
                        TableLayout.PREFERRED,
                        TableLayout.PREFERRED,
                        TableLayout.PREFERRED,
                        TableLayout.PREFERRED,
                        TableLayout.PREFERRED,
                        TableLayout.PREFERRED,
                        TableLayout.PREFERRED,
                        TableLayout.PREFERRED,
                        TableLayout.PREFERRED,
                        TableLayout.PREFERRED,
                        TableLayout.PREFERRED
                    }
                }
            )
        );

        ( (TableLayout) getLayout() ).setHGap(5);
        ( (TableLayout) getLayout() ).setVGap(5);

        //======== this ========
        setBackground(bgcolor);
        
        //---- detailsLabel ----
        detailsLabel.setText("DETAILS");
        add(detailsLabel, new TableLayoutConstraints(0, 0, 0, 0, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- trackLabel ----
        trackLabel.setText("Track number");
        add(trackLabel, new TableLayoutConstraints(0, 1, 0, 1, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- artistLabel ----
        artistLabel.setText("Artist");
        add(artistLabel, new TableLayoutConstraints(0, 3, 0, 3, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- titleLabel ----
        titleLabel.setText("Title");
        add(titleLabel, new TableLayoutConstraints(0, 2, 0, 2, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- variousLabel ----
        variousLabel.setText("Various artists");
        add(variousLabel, new TableLayoutConstraints(0, 4, 0, 4, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- subdirLabel ----
        subdirLabel.setText("Subdirectory");
        add(subdirLabel, new TableLayoutConstraints(0, 6, 0, 6, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- lengthLabel ----
        lengthLabel.setText("Length");
        lengthLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(lengthLabel, new TableLayoutConstraints(3, 6, 3, 6, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- yearLabel ----
        yearLabel.setText("Year");
        add(yearLabel, new TableLayoutConstraints(0, 7, 0, 7, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- genreLabel ----
        genreLabel.setText("Genre");
        add(genreLabel, new TableLayoutConstraints(0, 8, 0, 8, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- tagLabel ----
        tagLabel.setText("Tag version");
        add(tagLabel, new TableLayoutConstraints(0, 9, 0, 9, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- lameLabel ----
        lameLabel.setText("Lame version");
        add(lameLabel, new TableLayoutConstraints(0, 10, 0, 10, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- bitrateLabel ----
        bitrateLabel.setText("Audio bitrate");
        add(bitrateLabel, new TableLayoutConstraints(0, 11, 0, 11, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- vbrLabel ----
        vbrLabel.setText("VBR");
        add(vbrLabel, new TableLayoutConstraints(0, 12, 0, 12, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- frequencyLabel ----
        frequencyLabel.setText("Samplerate");
        add(frequencyLabel, new TableLayoutConstraints(0, 13, 0, 13, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- modeLabel ----
        modeLabel.setText("Audio mode");
        add(modeLabel, new TableLayoutConstraints(0, 14, 0, 14, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

        //---- trackTextField ----
        trackTextField.setEditable(false);
        add(trackTextField, new TableLayoutConstraints(1, 1, 1, 1, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- titleTextField ----
        titleTextField.setEditable(false);
        add(titleTextField, new TableLayoutConstraints(1, 2, 4, 2, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- artistTextField ----
        artistTextField.setEditable(false);
        add(artistTextField, new TableLayoutConstraints(1, 3, 4, 3, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- subdirTextField ----
        subdirTextField.setEditable(false);
        add(subdirTextField, new TableLayoutConstraints(1, 6, 2, 6, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- lengthTextField ----
        lengthTextField.setEditable(false);
        add(lengthTextField, new TableLayoutConstraints(4, 6, 4, 6, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- yearTextField ----
        yearTextField.setEditable(false);
        add(yearTextField, new TableLayoutConstraints(1, 7, 2, 7, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- genreTextField ----
        genreTextField.setEditable(false);
        add(genreTextField, new TableLayoutConstraints(1, 8, 2, 8, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- tagTextField ----
        tagTextField.setEditable(false);
        add(tagTextField, new TableLayoutConstraints(1, 9, 2, 9, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- lameTextField ----
        lameTextField.setEditable(false);
        add(lameTextField, new TableLayoutConstraints(1, 10, 2, 10, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- bitrateTextField ----
        bitrateTextField.setEditable(false);
        add(bitrateTextField, new TableLayoutConstraints(1, 11, 2, 11, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- frequencyTextField ----
        frequencyTextField.setEditable(false);
        add(frequencyTextField, new TableLayoutConstraints(1, 13, 2, 13, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- modeTextField ----
        modeTextField.setEditable(false);
        add(modeTextField, new TableLayoutConstraints(1, 14, 2, 14, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

        //---- yearComboBox ----
        yearComboBox.setPrototypeDisplayValue("2000");
        yearComboBox.addItemListener(this);
        add(yearComboBox, new TableLayoutConstraints(3, 7, 4, 7, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- genreComboBox ----
        genreComboBox.setPrototypeDisplayValue("Dance Hall");
        genreComboBox.addItemListener(this);
        add(genreComboBox, new TableLayoutConstraints(3, 8, 4, 8, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- tagComboBox ----
        tagComboBox.setPrototypeDisplayValue("ID3v2.40");
        tagComboBox.addItemListener(this);
        add(tagComboBox, new TableLayoutConstraints(3, 9, 4, 9, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- lameComboBox ----
        lameComboBox.setPrototypeDisplayValue("LAME3.90.3");
        lameComboBox.addItemListener(this);
        add(lameComboBox, new TableLayoutConstraints(3, 10, 4, 10, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- bitrateComboBox ----
        bitrateComboBox.setPrototypeDisplayValue("320 kbps");
        bitrateComboBox.addItemListener(this);
        add(bitrateComboBox, new TableLayoutConstraints(3, 11, 4, 11, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- frequencyComboBox ----
        frequencyComboBox.setPrototypeDisplayValue("44100 Hz");
        frequencyComboBox.addItemListener(this);
        add(frequencyComboBox, new TableLayoutConstraints(3, 13, 4, 13, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- modeComboBox ----
        modeComboBox.setPrototypeDisplayValue("Dual Channel");
        modeComboBox.addItemListener(this);
        add(modeComboBox, new TableLayoutConstraints(3, 14, 4, 14, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

        //---- isVbrRadioButton ----
        isVbrRadioButton.setText("Yes");
        isVbrRadioButton.setBackground(bgcolor);
        add(isVbrRadioButton, new TableLayoutConstraints(1, 12, 1, 12, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- isNotVbrRadioButton ----
        isNotVbrRadioButton.setText("No");
        isNotVbrRadioButton.setSelected(true);
        isNotVbrRadioButton.setBackground(bgcolor);
        add(isNotVbrRadioButton, new TableLayoutConstraints(2, 12, 2, 12, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- vbrButtonGroup ----
        vbrButtonGroup.add(isVbrRadioButton);
        vbrButtonGroup.add(isNotVbrRadioButton);

        //---- isVariousRadioButton ----
        isVariousRadioButton.setText("Yes");
        isVariousRadioButton.setBackground(bgcolor);
        add(isVariousRadioButton, new TableLayoutConstraints(1, 4, 1, 4, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- isNotVariousRadioButton ----
        isNotVariousRadioButton.setText("No");
        isNotVariousRadioButton.setSelected(true);
        isNotVariousRadioButton.setBackground(bgcolor);
        add(isNotVariousRadioButton, new TableLayoutConstraints(2, 4, 2, 4, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- variousButtonGroup ----
        variousButtonGroup.add(isVariousRadioButton);
        variousButtonGroup.add(isNotVariousRadioButton);

        //TODO: set listeners for edit buttons (cancel, update)
        //---- cancelButton ----
        cancelButton.setText("CANCEL");
        add(cancelButton, new TableLayoutConstraints(3, 16, 3, 16, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));
        //---- updateButton ----
        updateButton.setText("UPDATE");
        add(updateButton, new TableLayoutConstraints(4, 16, 4, 16, TableLayoutConstraints.FULL, TableLayoutConstraints.FULL));

        // initially hide the edit buttons and the comboboxes
        showEditButtons(false);
        showComboBoxes(false);
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
        trackTextField.setText("");
        artistTextField.setText("");
        titleTextField.setText("");
        subdirTextField.setText("");
        lengthTextField.setText("");
        yearTextField.setText("");
        genreTextField.setText("");
        tagTextField.setText("");
        lameTextField.setText("");
        bitrateTextField.setText("");
        frequencyTextField.setText("");
        modeTextField.setText("");
        setNotVarious();
        setNotVbr();
        yearComboBox.removeAllItems();
        genreComboBox.removeAllItems();
        tagComboBox.removeAllItems();
        lameComboBox.removeAllItems();
        bitrateComboBox.removeAllItems();
        frequencyComboBox.removeAllItems();
        modeComboBox.removeAllItems();
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
        yearComboBox.setVisible(value);
        genreComboBox.setVisible(value);
        tagComboBox.setVisible(value);
        lameComboBox.setVisible(value);
        bitrateComboBox.setVisible(value);
        frequencyComboBox.setVisible(value);
        modeComboBox.setVisible(value);
    }

    // LISTENER for comboboxes
    public void itemStateChanged(ItemEvent ae) {

        Object source = ae.getItemSelectable();

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

    }
}
