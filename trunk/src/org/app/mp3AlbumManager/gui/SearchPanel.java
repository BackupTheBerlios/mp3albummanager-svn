package org.app.mp3AlbumManager.gui;

import com.jeta.forms.components.panel.FormPanel;
import com.jeta.forms.gui.form.FormAccessor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class SearchPanel extends JPanel implements ActionListener {

    public static final String ID_MAIN_FORM = "main.form";  //javax.swing.JPanel
    public static final String ID_SELECTCOMBOBOX = "selectComboBox";  //javax.swing.JComboBox
    public static final String ID_SEARCHBUTTON = "searchButton";  //javax.swing.JButton
    public static final String ID_PROPERTYCOMBOBOX = "propertyComboBox";  //javax.swing.JComboBox
    public static final String ID_OPERATORCOMBOBOX = "operatorComboBox";  //javax.swing.JComboBox
    public static final String ID_VALUECOMBOBOX = "valueComboBox";  //javax.swing.JComboBox

    public static final String ID_EXTRA_LINE1 = "extra.line1";  //javax.swing.JPanel
    public static final String ID_ANDCOMBOBOX1 = "andComboBox1";  //javax.swing.JComboBox
    public static final String ID_PROPERTYCOMBOBOX1 = "propertyComboBox1";  //javax.swing.JComboBox
    public static final String ID_OPERATORCOMBOBOX1 = "operatorComboBox1";  //javax.swing.JComboBox
    public static final String ID_VALUECOMBOBOX1 = "valueComboBox1";  //javax.swing.JComboBox
    public static final String ID_ADDBUTTON1 = "addButton1";  //javax.swing.JToggleButton

    public static final String ID_EXTRA_LINE2 = "extra.line2";  //javax.swing.JPanel
    public static final String ID_ANDCOMBOBOX2 = "andComboBox2";  //javax.swing.JComboBox
    public static final String ID_PROPERTYCOMBOBOX2 = "propertyComboBox2";  //javax.swing.JComboBox
    public static final String ID_OPERATORCOMBOBOX2 = "operatorComboBox2";  //javax.swing.JComboBox
    public static final String ID_VALUECOMBOBOX2 = "valueComboBox2";  //javax.swing.JComboBox
    public static final String ID_ADDBUTTON2 = "addButton2";  //javax.swing.JToggleButton

    public static final String ID_EXTRA_LINE3 = "extra.line3";  //javax.swing.JPanel
    public static final String ID_ANDCOMBOBOX3 = "andComboBox3";  //javax.swing.JComboBox
    public static final String ID_PROPERTYCOMBOBOX3 = "propertyComboBox3";  //javax.swing.JComboBox
    public static final String ID_OPERATORCOMBOBOX3 = "operatorComboBox3";  //javax.swing.JComboBox
    public static final String ID_VALUECOMBOBOX3 = "valueComboBox3";  //javax.swing.JComboBox
    public static final String ID_ADDBUTTON3 = "addButton3";  //javax.swing.JToggleButton

    public static final String ID_EXTRA_LINE4 = "extra.line4";  //javax.swing.JPanel
    public static final String ID_ANDCOMBOBOX4 = "andComboBox4";  //javax.swing.JComboBox
    public static final String ID_PROPERTYCOMBOBOX4 = "propertyComboBox4";  //javax.swing.JComboBox
    public static final String ID_OPERATORCOMBOBOX4 = "operatorComboBox4";  //javax.swing.JComboBox
    public static final String ID_VALUECOMBOBOX4 = "valueComboBox4";  //javax.swing.JComboBox
    public static final String ID_ADDBUTTON4 = "addButton4";  //javax.swing.JToggleButton
    
    // the form panel
    private FormPanel searchPanel;
    private FormAccessor mainForm, extraLine1, extraLine2, extraLine3, extraLine4;

    // panel components
    private AbstractButton searchButton;
    private JComboBox selectComboBox;
    private JComboBox propertyComboBox, operatorComboBox, valueComboBox;
    private AbstractButton addButton1;
    private JComboBox propertyComboBox1, operatorComboBox1, valueComboBox1;
    private AbstractButton addButton2;
    private JComboBox propertyComboBox2, operatorComboBox2, valueComboBox2;
    private AbstractButton addButton3;
    private JComboBox propertyComboBox3, operatorComboBox3, valueComboBox3;
    private AbstractButton addButton4;
    private JComboBox propertyComboBox4, operatorComboBox4, valueComboBox4;

    private JComboBox[] comboBoxes;
    private String select, property;
    private String[] albumProperties = { "albumlength", "albumyear", "artist", "bitrate", "frequency",
            "genre", "lame","mode", "subdirs", "tag", "title", "tracks", "various", "vbr"};
    private String[] songProperties = { "album", "artist", "bitrate", "frequency", "genre", "lame", "mode",
            "songlength", "songyear", "subdir", "tag", "title", "track", "vbr"};
    private String[] stringOperators = { "=", "!=" };
    private String[] intOperators = { "=", "!=", ">", "<" };
    private String[] boolValues = { "true", "false" };
    private String[] bitrateValues = { "320", "256", "244", "192", "160", "128" };
    private String[] frequencyValues = { "48000", "44100", "32000" };
    private String[] lameValues = { "LAME3.98", "LAME3.97", "LAME3.96", "LAME3.95", "LAME3.93", "LAME3.92", "LAME3.91", "LAME3.90" };
    private String[] modeValues = { "Stereo", "Joint stereo", "Dual channel", "Mono" }; // 'Mono' or 'Single channel' ??
    private String[] tagValues = { "ID3v1.0", "ID3v1.1", "ID3v2.2", "ID3v2.3", "ID3v2.4" };
    private AbstractButton[] buttons;

    private Color bgcolor;

    public SearchPanel (Color color) {

        searchPanel = new FormPanel("org/app/mp3AlbumManager/forms/searchPanel.jfrm");
        mainForm = searchPanel.getFormAccessor(ID_MAIN_FORM);
        extraLine1 = searchPanel.getFormAccessor(ID_EXTRA_LINE1);
        extraLine2 = searchPanel.getFormAccessor(ID_EXTRA_LINE2);
        extraLine3 = searchPanel.getFormAccessor(ID_EXTRA_LINE3);
        extraLine4 = searchPanel.getFormAccessor(ID_EXTRA_LINE4);

        showAccessor(extraLine1.getContainer(), false);
        showAccessor(extraLine2.getContainer(), false);
        showAccessor(extraLine3.getContainer(), false);
        showAccessor(extraLine4.getContainer(), false);

        bgcolor = color;

        initComponents();
    }

    public void showPanel() {
        setVisible(true);
        super.repaint();
    }

    public void showAccessor(Container c, boolean v) {
        c.setVisible(v);
    }

    private void initComponents() {

        searchButton = searchPanel.getButton(ID_SEARCHBUTTON);
        selectComboBox = searchPanel.getComboBox(ID_SELECTCOMBOBOX);

        propertyComboBox = searchPanel.getComboBox(ID_PROPERTYCOMBOBOX);
        operatorComboBox = searchPanel.getComboBox(ID_OPERATORCOMBOBOX);
        valueComboBox = searchPanel.getComboBox(ID_VALUECOMBOBOX);
        addButton1 = searchPanel.getButton(ID_ADDBUTTON1);

        propertyComboBox1 = searchPanel.getComboBox(ID_PROPERTYCOMBOBOX1);
        operatorComboBox1 = searchPanel.getComboBox(ID_OPERATORCOMBOBOX1);
        valueComboBox1 = searchPanel.getComboBox(ID_VALUECOMBOBOX1);
        addButton2 = searchPanel.getButton(ID_ADDBUTTON2);

        propertyComboBox2 = searchPanel.getComboBox(ID_PROPERTYCOMBOBOX2);
        operatorComboBox2 = searchPanel.getComboBox(ID_OPERATORCOMBOBOX2);
        valueComboBox2 = searchPanel.getComboBox(ID_VALUECOMBOBOX2);
        addButton3 = searchPanel.getButton(ID_ADDBUTTON3);

        propertyComboBox3 = searchPanel.getComboBox(ID_PROPERTYCOMBOBOX3);
        operatorComboBox3 = searchPanel.getComboBox(ID_OPERATORCOMBOBOX3);
        valueComboBox3 = searchPanel.getComboBox(ID_VALUECOMBOBOX3);
        addButton4 = searchPanel.getButton(ID_ADDBUTTON4);

        propertyComboBox4 = searchPanel.getComboBox(ID_PROPERTYCOMBOBOX4);
        operatorComboBox4 = searchPanel.getComboBox(ID_OPERATORCOMBOBOX4);
        valueComboBox4 = searchPanel.getComboBox(ID_VALUECOMBOBOX4);

         // add listener to comboboxes
        comboBoxes = new JComboBox[] {selectComboBox, propertyComboBox, operatorComboBox, valueComboBox,
            propertyComboBox1, operatorComboBox1, valueComboBox1,
            propertyComboBox2, operatorComboBox2, valueComboBox2,
            propertyComboBox3, operatorComboBox3, valueComboBox3,
            propertyComboBox4, operatorComboBox4, valueComboBox4};
        for(JComboBox c : comboBoxes) { c.addActionListener(this); }

        // add listener to buttons
        buttons = new AbstractButton[] {addButton1, addButton2, addButton3, addButton4};
        for(AbstractButton b : buttons) { b.addActionListener( new SearchButtonListener() ); }

        //======== this ========
        setBackground(bgcolor);

        //LAYOUT
        setLayout( new BorderLayout() );
        add( searchPanel, BorderLayout.CENTER );

    }

    public void setSearchButtonListener(ActionListener al) {

        searchButton.addActionListener(al);
        searchButton.setActionCommand("search");
    }


    public void actionPerformed(ActionEvent ae) {

        JComboBox cb = (JComboBox) ae.getSource();

        if( cb == selectComboBox ) {
            select = selectComboBox.getSelectedItem().toString();
            if(select.equals("Album")) {
                propertyComboBox.removeAllItems();
                for(String s : albumProperties) { propertyComboBox.addItem(s); }
            } else if(select.equals("Song")) {
                propertyComboBox.removeAllItems();
                for(String s : songProperties) { propertyComboBox.addItem(s); }
            }
        }
        if( cb == propertyComboBox && propertyComboBox.getItemCount() > 0) {
            property = cb.getSelectedItem().toString();
            // fill operatorComboBox
            if( property.equals("album") | property.equals("artist") | property.equals("genre")
                    | property.equals("mode") | property.equals("subdir") | property.equals("subdirs")
                    | property.equals("tag") | property.equals("title") | property.equals("various")
                    | property.equals("vbr") ) {
                operatorComboBox.removeAllItems();
                for(String s : stringOperators) { operatorComboBox.addItem(s); }
                operatorComboBox.setSelectedIndex(0);
            } else {
                operatorComboBox.removeAllItems();
                for(String s : intOperators) { operatorComboBox.addItem(s); }
                operatorComboBox.setSelectedIndex(0);
            }
            // fill valuesComboBox
            if( property.equals("bitrate") ) {
                valueComboBox.removeAllItems();
                for(String s : bitrateValues) { valueComboBox.addItem(s); }
            } else if( property.equals("frequency") ) {
                valueComboBox.removeAllItems();
                for(String s : frequencyValues) { valueComboBox.addItem(s); }
            } else if( property.equals("lame") ) {
                valueComboBox.removeAllItems();
                for(String s : lameValues) { valueComboBox.addItem(s); }
            } else if( property.equals("mode") ) {
                valueComboBox.removeAllItems();
                for(String s : modeValues) { valueComboBox.addItem(s); }
            } else if( property.equals("tag") ) {
                valueComboBox.removeAllItems();
                for(String s : tagValues) { valueComboBox.addItem(s); }
            } else if( property.equals("various") | property.equals("various") ) {
                valueComboBox.removeAllItems();
                for(String s : boolValues) { valueComboBox.addItem(s); }
            } else {
                valueComboBox.removeAllItems();   
            }

        }
    }

    class SearchButtonListener implements ActionListener {

        public void actionPerformed(ActionEvent ae) {

            String query = "SELECT * FROM ";
            String op = " " + operatorComboBox.getSelectedItem().toString() + " ";
            String val = "";
            if(valueComboBox.getSelectedItem() != null) { val = valueComboBox.getSelectedItem().toString(); }
            AbstractButton b = (AbstractButton) ae.getSource();

            if(b == addButton1) {
                query += select + " WHERE " + property + op + val;


                System.out.println(query);
            }

        }
    }
}
