package org.app.mp3AlbumManager.gui;

import com.jeta.forms.components.panel.FormPanel;
import com.jeta.forms.gui.form.FormAccessor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

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

    public static final String ID_MESSAGELABEL = "messageLabel";  //com.jeta.forms.components.label.JETALabel

    // the form panel
    private FormPanel searchPanel;
    private FormAccessor extraLine1;
    private FormAccessor extraLine2;
    private FormAccessor extraLine3;
    private FormAccessor extraLine4;

    // panel components
    private AbstractButton searchButton;
    private JComboBox selectComboBox;
    private JComboBox propertyComboBox, operatorComboBox, valueComboBox;
    private AbstractButton addButton1;
    private JComboBox propertyComboBox1, operatorComboBox1, valueComboBox1, andComboBox1;
    private AbstractButton addButton2;
    private JComboBox propertyComboBox2, operatorComboBox2, valueComboBox2, andComboBox2;
    private AbstractButton addButton3;
    private JComboBox propertyComboBox3, operatorComboBox3, valueComboBox3, andComboBox3;
    private AbstractButton addButton4;
    private JComboBox propertyComboBox4, operatorComboBox4, valueComboBox4, andComboBox4;

    private JLabel messageLabel;

    private String select;
    private String[] albumProperties = { "albumlength", "albumyear", "artist", "bitrate", "frequency",
            "genre", "lame","mode", "subdirs", "tag", "title", "tracks", "various", "vbr"};
    private String[] songProperties = { "album", "artist", "bitrate", "frequency", "genre", "lame", "mode",
            "songlength", "songyear", "subdir", "tag", "title", "track", "vbr"};

    private static final String PROTOTYPE_DISPLAY_VALUE = "albumlength";
    private CbListener searchListener, searchListener1, searchListener2, searchListener3, searchListener4;

    private String query;
    private ArrayList<Object> prepValue;

    private Color bgcolor;


    public SearchPanel(Color color) {

        searchPanel = new FormPanel("org/app/mp3AlbumManager/forms/searchPanel.jfrm");
        //FormAccessor mainForm = searchPanel.getFormAccessor(ID_MAIN_FORM);
        extraLine1 = searchPanel.getFormAccessor(ID_EXTRA_LINE1);
        extraLine2 = searchPanel.getFormAccessor(ID_EXTRA_LINE2);
        extraLine3 = searchPanel.getFormAccessor(ID_EXTRA_LINE3);
        extraLine4 = searchPanel.getFormAccessor(ID_EXTRA_LINE4);

        showAccessor(extraLine1.getContainer(), false);
        showAccessor(extraLine2.getContainer(), false);
        showAccessor(extraLine3.getContainer(), false);
        showAccessor(extraLine4.getContainer(), false);

        query = "";
        prepValue = new ArrayList<Object>();

        bgcolor = color;

        initComponents();
    }

    public void showPanel() {
        setVisible(true);
        super.repaint();
    }

    private void showAccessor(Container c, boolean v) {
        c.setVisible(v);
    }

    private void initComponents() {

        searchButton = searchPanel.getButton(ID_SEARCHBUTTON);
        selectComboBox = searchPanel.getComboBox(ID_SELECTCOMBOBOX);

        propertyComboBox = searchPanel.getComboBox(ID_PROPERTYCOMBOBOX);
        propertyComboBox.setPrototypeDisplayValue(PROTOTYPE_DISPLAY_VALUE);
        operatorComboBox = searchPanel.getComboBox(ID_OPERATORCOMBOBOX);
        valueComboBox = searchPanel.getComboBox(ID_VALUECOMBOBOX);
        addButton1 = searchPanel.getButton(ID_ADDBUTTON1);

        propertyComboBox1 = searchPanel.getComboBox(ID_PROPERTYCOMBOBOX1);
        propertyComboBox1.setPrototypeDisplayValue(PROTOTYPE_DISPLAY_VALUE);
        operatorComboBox1 = searchPanel.getComboBox(ID_OPERATORCOMBOBOX1);
        valueComboBox1 = searchPanel.getComboBox(ID_VALUECOMBOBOX1);
        andComboBox1 = searchPanel.getComboBox(ID_ANDCOMBOBOX1);
        addButton2 = searchPanel.getButton(ID_ADDBUTTON2);

        propertyComboBox2 = searchPanel.getComboBox(ID_PROPERTYCOMBOBOX2);
        propertyComboBox2.setPrototypeDisplayValue(PROTOTYPE_DISPLAY_VALUE);
        operatorComboBox2 = searchPanel.getComboBox(ID_OPERATORCOMBOBOX2);
        valueComboBox2 = searchPanel.getComboBox(ID_VALUECOMBOBOX2);
        andComboBox2 = searchPanel.getComboBox(ID_ANDCOMBOBOX2);
        addButton3 = searchPanel.getButton(ID_ADDBUTTON3);

        propertyComboBox3 = searchPanel.getComboBox(ID_PROPERTYCOMBOBOX3);
        propertyComboBox3.setPrototypeDisplayValue(PROTOTYPE_DISPLAY_VALUE);
        operatorComboBox3 = searchPanel.getComboBox(ID_OPERATORCOMBOBOX3);
        valueComboBox3 = searchPanel.getComboBox(ID_VALUECOMBOBOX3);
        andComboBox3 = searchPanel.getComboBox(ID_ANDCOMBOBOX3);
        addButton4 = searchPanel.getButton(ID_ADDBUTTON4);

        propertyComboBox4 = searchPanel.getComboBox(ID_PROPERTYCOMBOBOX4);
        propertyComboBox4.setPrototypeDisplayValue(PROTOTYPE_DISPLAY_VALUE);
        operatorComboBox4 = searchPanel.getComboBox(ID_OPERATORCOMBOBOX4);
        valueComboBox4 = searchPanel.getComboBox(ID_VALUECOMBOBOX4);
        andComboBox4 = searchPanel.getComboBox(ID_ANDCOMBOBOX4);

        messageLabel = searchPanel.getLabel(ID_MESSAGELABEL);

        // add listener to selectComboBox
        selectComboBox.addActionListener(this);

        //======== this ========
        setBackground(bgcolor);

        //LAYOUT
        setLayout( new BorderLayout() );
        add(searchPanel, BorderLayout.CENTER);

    }

    public void setSearchButtonListener(ActionListener al) {

        searchButton.addActionListener(al);
        searchButton.setActionCommand("search");
    }

    private void fillCb(JComboBox cb, String[] vals) {
        cb.removeAllItems();
        for(String s : vals) { cb.addItem(s); }
        cb.setSelectedIndex(0);
    }

    // add listener for comboboxes
    public void addCbListeners(String[] props) {

        fillCb(propertyComboBox, props);
        searchListener = new CbListener(extraLine1, null, propertyComboBox, operatorComboBox, valueComboBox, addButton1, messageLabel);
        propertyComboBox.addActionListener(searchListener);

        fillCb(propertyComboBox1, props);
        searchListener1 = new CbListener(extraLine2, andComboBox1, propertyComboBox1, operatorComboBox1, valueComboBox1, addButton2, messageLabel);
        propertyComboBox1.addActionListener(searchListener1);

        fillCb(propertyComboBox2, props);
        searchListener2 = new CbListener(extraLine3, andComboBox2, propertyComboBox2, operatorComboBox2, valueComboBox2, addButton3, messageLabel);
        propertyComboBox2.addActionListener(searchListener2);

        fillCb(propertyComboBox3, props);
        searchListener3 = new CbListener(extraLine4, andComboBox3, propertyComboBox3, operatorComboBox3, valueComboBox3, addButton4, messageLabel);
        propertyComboBox3.addActionListener(searchListener3);

        fillCb(propertyComboBox4, props);
        searchListener4 = new CbListener(null, andComboBox4, propertyComboBox4, operatorComboBox4, valueComboBox4, null, messageLabel);
        propertyComboBox4.addActionListener(searchListener4);

    }

    public void setQuery(String q) { query = q; }

    public void addToQuery(String q) { query += q; }

    public String getQuery() { return query; }

    private void addToPrepVals(Object o) { if(o != null ) prepValue.add(o); }

    public ArrayList<Object> getPrepVals() { return prepValue; }

    public void actionPerformed(ActionEvent ae) {

        JComboBox cb = (JComboBox) ae.getSource();

        if( cb == selectComboBox ) {
            select = selectComboBox.getSelectedItem().toString();
            setQuery("SELECT * FROM " + select);
            if(select.equals("Album")) {
                fillCb(propertyComboBox, albumProperties);
                addCbListeners(albumProperties);
            } else if(select.equals("Song")) {
                fillCb(propertyComboBox, songProperties);
                addCbListeners(songProperties);
            }
        }

    }

    /**
     * Inner class listener for comboboxes.
     */
    class CbListener implements ActionListener {

        private JComboBox currAndCb;
        private JComboBox currPropCb;
        private JComboBox currOpCb;
        private JComboBox currValCb;
        private AbstractButton currBtn;
        private JLabel messageLabel;

        private String[] andValues = { " AND ", " OR " };
        private String[] stringOperators = { " = ", " != " };
        private String[] intOperators = { " = ", " != ", " > ", " < " };
        private Boolean[] boolValues = { true, false };
        private Integer[] bitrateValues = { 320, 256, 244, 192, 160, 128 };
        private Integer[] frequencyValues = { 48000, 44100, 32000 };
        private String[] lameValues = { "LAME3.98", "LAME3.97", "LAME3.96", "LAME3.95", "LAME3.93", "LAME3.92", "LAME3.91", "LAME3.90" };
        private String[] modeValues = { "Stereo", "Joint stereo", "Dual channel", "Mono" }; // 'Mono' or 'Single channel' ??
        private String[] tagValues = { "ID3v1.0", "ID3v1.1", "ID3v2.2", "ID3v2.3", "ID3v2.4" };

        private ArrayList<JComboBox> queryCbs;
        private ArrayList<JComboBox> prepValCbs;

        CbListener(FormAccessor nextAcc, JComboBox andCb, JComboBox propCb, JComboBox opCb, JComboBox valCb, AbstractButton btn, JLabel msgLabel) {

            final FormAccessor next = nextAcc;
            currAndCb = andCb;
            if(currAndCb != null) fillCb(currAndCb, andValues);
            currPropCb = propCb;
            currOpCb = opCb;
            currValCb = valCb;
            currBtn = btn;
            if(currBtn != null) {
                currBtn.setActionCommand("add");
                currBtn.addActionListener(
                        new ActionListener() {

                            public boolean checkCbs() {
                                if( getCbItem(currPropCb).isEmpty() | getCbItem(currValCb).isEmpty() ) {
                                    messageLabel.setText("Please select all fields!");
                                    return false;
                                } else {
                                    messageLabel.setText("");
                                    return true;
                                }
                            }
                            // actions for currBtn
                            public void actionPerformed(ActionEvent e) {
                                if( e.getActionCommand().equals("add") ) {

                                    if( ! checkCbs() ) return;

                                    // store the current comboboxes
                                    queryCbs.add(currAndCb);
                                    queryCbs.add(currPropCb);
                                    queryCbs.add(currOpCb);
                                    prepValCbs.add(currValCb);

                                    //TODO: add to query should be done when clicking searchButton (not addButton) otherwise is the last search line not picked up

                                    // replace the andCb item with 'WHERE' for the first search line
                                    String andOr = (currAndCb == null) ? " WHERE " : getCbItem(currAndCb);
                                    // add cb items to the query
                                    addToQuery(andOr + getCbItem(currPropCb) + getCbItem(currOpCb) + "?");
                                    // add valCb item to the prepared values
                                    addToPrepVals( getCbItem(currValCb) );

                                    // show next search line
                                    if(next != null) showAccessor(next, true);


                                }
                            }
                        }
                );
            }
            messageLabel = msgLabel;

            queryCbs = new ArrayList<JComboBox>();
            prepValCbs = new ArrayList<JComboBox>();

        }

        public void showAccessor(FormAccessor acc, boolean val) { acc.getContainer().setVisible(val); }

        // getters for comboboxes
        private String getCbItem(JComboBox cb) {
            Object item = cb.getSelectedItem();
            return (item == null) ? "" : item.toString();
        }


        public void fillCb(JComboBox cb, Object[] vals) {
            cb.removeAllItems();
            for(Object o : vals) { cb.addItem(o); }
            cb.setSelectedIndex(0);
        }


        public void actionPerformed(ActionEvent e) {

            JComboBox cb = (JComboBox) e.getSource();

            // actions for currPropCb
            if(cb == currPropCb && currPropCb.getItemCount() > 0) {

                String propValue = getCbItem(currPropCb);
                // fill operatorComboBox
                if( propValue.equals("albumlength") | propValue.equals("albumyear") |
                        propValue.equals("bitrate") | propValue.equals("frequency") |
                        propValue.equals("track") | propValue.equals("tracks") |
                        propValue.equals("songlength") | propValue.equals("songyear") ) {
                    fillCb(currOpCb, intOperators);
                } else {
                    fillCb(currOpCb, stringOperators);
                }
                // fill valuesComboBox
                if( propValue.equals("bitrate") ) {
                    fillCb(currValCb, bitrateValues);
                } else if( propValue.equals("frequency") ) {
                    fillCb(currValCb, frequencyValues);
                } else if( propValue.equals("lame") ) {
                    fillCb(currValCb, lameValues);
                } else if( propValue.equals("mode") ) {
                    fillCb(currValCb, modeValues);
                } else if( propValue.equals("tag") ) {
                    fillCb(currValCb, tagValues);
                } else if( propValue.equals("various") | propValue.equals("vbr") ) {
                    fillCb(currValCb, boolValues);
                } else {
                    currValCb.removeAllItems();
                }

            }
        }
    }
}

