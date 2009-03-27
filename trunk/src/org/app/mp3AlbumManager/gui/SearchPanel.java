package org.app.mp3AlbumManager.gui;

import com.jeta.forms.components.panel.FormPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;


public class SearchPanel extends JPanel implements ActionListener {

    public static final String ID_SELECTCB = "selectCB";  //javax.swing.JComboBox
    public static final String ID_PROPCB = "propCB";  //javax.swing.JComboBox
    public static final String ID_OPCB = "opCB";  //javax.swing.JComboBox
    public static final String ID_VALCB = "valCB";  //javax.swing.JComboBox
    public static final String ID_SEARCHBTN = "searchBtn";  //javax.swing.JButton
    public static final String ID_MESSAGELABEL = "messageLabel";  //com.jeta.forms.components.label.JETALabel

    // the form panel
    private FormPanel searchPanel;

    // panel components
    private JComboBox selectCB, propCB, opCB, valCB;
    private AbstractButton searchBtn;
    private JLabel messageLabel;

    // constant field names
    private static final String SELECT_ALBUM_NAME = "Album";
    private static final String PROP_LENGTH_NAME = "Length";
    private static final String PROP_YEAR_NAME = "Year";
    private static final String PROP_SONG_NAME = "Song";
    private static final String PROP_SONGTRACK_NAME = "Track";
    private static final String PROP_ALBUMTRACKS_NAME = "Tracks";
    private static final String PROP_ALBUM_NAME = "Album";
    private static final String PROP_VARIOUS_NAME = "Various Artists";
    private static final String PROP_ARTIST_NAME = "Artist";
    private static final String PROP_TITLE_NAME = "Title";
    private static final String PROP_BITRATE_NAME = "Bitrate";
    private static final String PROP_VBR_NAME = "VBR";
    private static final String PROP_FREQ_NAME = "Samplerate";
    private static final String PROP_GENRE_NAME = "Genre";
    private static final String PROP_LAME_NAME = "Lame version";
    private static final String PROP_MODE_NAME = "Audio mode";
    private static final String PROP_TAG_NAME = "Tag version";
    private static final String OP_IS_NAME = "is";
    private static final String OP_ISNOT_NAME = "is not";
    private static final String OP_CONTAINS_NAME = "contains";
    private static final String OP_CONTAINSNOT_NAME = "does not contain";
    private static final String OP_MORE_NAME = "is more than";
    private static final String OP_LESS_NAME = "is less than";
    private static final String OP_HAS_NAME = "has one";
    private static final String OP_HASNOT_NAME = "none";

    // select fields
    private static final String SELECT_PROTO_VALUE = SELECT_ALBUM_NAME;
    private Map<String, String> selectMap;
    private static final int SELECT_LENGTH = 2;
    private String[] selectKeys = { SELECT_ALBUM_NAME, PROP_SONG_NAME};
    private String[] selectVals = { "SELECT * FROM Album WHERE ", "SELECT * FROM Song WHERE " };

    // property fields
    private static final String PROP_PROTO_VALUE = PROP_VARIOUS_NAME;
    private Map<String, String> albumPropMap;
    private static final int PROP_LENGTH = 13;
    private String[] albumPropKeys = { PROP_ARTIST_NAME, PROP_MODE_NAME, PROP_BITRATE_NAME, PROP_GENRE_NAME,
            PROP_LENGTH_NAME, PROP_LAME_NAME, PROP_FREQ_NAME, PROP_TAG_NAME, PROP_TITLE_NAME,
            PROP_ALBUMTRACKS_NAME, PROP_VARIOUS_NAME, PROP_VBR_NAME, PROP_YEAR_NAME };
    private String[] albumPropVals = { "artist", "mode", "bitrate", "genre", "albumlength",  "lame",
            "frequency", "tag", "title", "tracks", "various", "vbr", "albumyear" };
    private Map<String, String> songPropMap;
    private String[] songPropKeys = { PROP_ALBUM_NAME, PROP_ARTIST_NAME, PROP_MODE_NAME, PROP_BITRATE_NAME,
            PROP_GENRE_NAME, PROP_LAME_NAME, PROP_LENGTH_NAME, PROP_FREQ_NAME, PROP_TAG_NAME, PROP_TITLE_NAME,
            PROP_SONGTRACK_NAME, PROP_VBR_NAME, PROP_YEAR_NAME };
    private String[] songPropVals = { "album", "artist", "mode", "bitrate", "genre", "lame",
            "songlength", "frequency", "tag", "title", "track", "vbr", "songyear" };

    // operator fields
    private static final String OP_PROTO_VALUE = OP_CONTAINSNOT_NAME;
    private Map<String, String> stringOpMap;
    private static final int STR_OP_LENGTH = 4;
    private String[] stringOpKeys = { OP_CONTAINS_NAME, OP_CONTAINSNOT_NAME, OP_IS_NAME, OP_ISNOT_NAME };
    // ADD WILDCARDS TO PREPVAL '%val%' FOR LIKE/NOT LIKE
    private String[] stringOpVals = { " LIKE ?", " NOT LIKE ?",  " = ?", " != ?" };
    private Map<String, String> intOpMap;
    private static final int INT_OP_LENGTH = 4;
    private String[] intOpKeys = { OP_IS_NAME, OP_ISNOT_NAME, OP_MORE_NAME, OP_LESS_NAME };
    private String[] intOpVals = { " = ?", " != ?", " > ?", " < ?" };
    private Map<String, String> enumOpMap;
    private static final int ENUM_OP_LENGTH = 4;
    private String[] enumOpKeys = { OP_IS_NAME, OP_ISNOT_NAME, OP_HASNOT_NAME, OP_HAS_NAME };
    // <--SET PREPVAL TO 'null' FOR none/has one
    private String[] enumOpVals = { " = ?", " != ?", " = ?", " != ?" };
    private Map<String, String> boolOpMap;
    private static final int BOOL_OP_LENGTH = 2;
    private String[] boolOpKeys = { OP_IS_NAME, OP_ISNOT_NAME };
    private String[] boolOpVals = { " = ?", " != ?" };  // <--SET PREPVAL TO 'true'

    // value fields
    private static final String VAL_PROTO_VALUE = "Contemporary Christian";
    private String[] stringVals = { "" };
    private Integer[] intVals = { 0 };
    private Boolean[] boolVals = { true };
    private String[] existsVals = { "null" };
    private Integer[] bitrateVals = { 320, 256, 244, 192, 160, 128 };
    private Integer[] freqVals = { 48000, 44100, 32000 };
    private String[] lameVals = { "LAME3.98", "LAME3.97", "LAME3.96", "LAME3.95", "LAME3.93", "LAME3.92", "LAME3.91", "LAME3.90" };
    private String[] modeVals = { "Stereo", "Joint stereo", "Dual channel", "Mono" }; // 'Mono' or 'Single channel' ??
    private String[] tagVals = { "ID3v2.4",  "ID3v2.3", "ID3v2.2", "ID3v1.1", "ID3v1.0" };
    private final String[] genreVals = { "Blues", "Classic Rock", "Country", "Dance", "Disco", "Funk", "Grunge", "Hip-Hop", "Jazz",
            "Metal", "New Age", "Oldies", "Other", "Pop", "R&B", "Rap", "Reggae", "Rock", "Techno", "Industrial",
            "Alternative", "Ska", "Death Metal", "Pranks", "Soundtrack", "Euro-Techno", "Ambient", "Trip-Hop", "Vocal",
            "Jazz+Funk", "Fusion", "Trance", "Classical", "Instrumental", "Acid", "House", "Game", "Sound Clip", "Gospel",
            "Noise", "Alternative Rock", "Bass", "Soul", "Punk", "Space", "Meditative", "Instrumental Pop", "Instrumental Rock",
            "Ethnic", "Gothic", "Darkwave", "Techno-Industrial", "Electronic", "Pop-Folk", "Eurodance", "Dream", "Southern Rock",
            "Comedy", "Cult", "Gangsta", "Top 40", "Christian Rap", "Pop/Funk", "Jungle", "Native US", "Cabaret", "New Wave",
            "Psychadelic", "Rave", "Showtunes", "Trailer", "Lo-Fi", "Tribal", "Acid Punk", "Acid Jazz", "Polka", "Retro",
            "Musical", "Rock & Roll", "Hard Rock", "Folk", "Folk-Rock", "National Folk", "Swing", "Fast Fusion", "Bebob", "Latin",
            "Revival", "Celtic", "Bluegrass", "Avantgarde", "Gothic Rock",  "Progressive Rock", "Psychedelic Rock",
            "Symphonic Rock", "Slow Rock", "Big Band",  "Chorus", "Easy Listening", "Acoustic", "Humour", "Speech", "Chanson",
            "Opera", "Chamber Music", "Sonata", "Symphony", "Booty Bass", "Primus", "Porn Groove", "Satire", "Slow Jam",
            "Club", "Tango", "Samba", "Folklore", "Ballad", "Power Ballad", "Rhytmic Soul", "Freestyle", "Duet", "Punk Rock",
            "Drum Solo", "Acapella", "Euro-House", "Dance Hall", "Goa", "Drum & Bass", "Club-House", "Hardcore", "Terror", "Indie",
            "BritPop", "Negerpunk", "Polsk Punk", "Beat", "Christian Gangsta", "Heavy Metal", "Black Metal", "Crossover",
            "Contemporary Christian", "Christian Rock", "Merengue", "Salsa", "Thrash Metal", "Anime", "JPop", "SynthPop" };

    private String currSelect;
    private Map<String, String> currPropMap;
    private Map<String, String> currOpMap;

    // query fields
    private String query;
    private ArrayList<Object> prepValue;

    private String humanReadableSearch;

    private Color bgcolor;

    public SearchPanel(Color color) {

        bgcolor = color;

        query = "";
        prepValue = new ArrayList<Object>();
        humanReadableSearch = "";

        selectMap = new LinkedHashMap<String, String>();
        for(int i = 0; i < SELECT_LENGTH; i++) { selectMap.put( selectKeys[i], selectVals[i] ); }
        albumPropMap = new LinkedHashMap<String, String>();
        for(int i = 0; i < PROP_LENGTH; i++) { albumPropMap.put( albumPropKeys[i], albumPropVals[i] ); }
        songPropMap = new LinkedHashMap<String, String>();
        for(int i = 0; i < PROP_LENGTH; i++) { songPropMap.put( songPropKeys[i], songPropVals[i] ); }
        stringOpMap = new LinkedHashMap<String, String>();
        for(int i = 0; i < STR_OP_LENGTH; i++) { stringOpMap.put( stringOpKeys[i], stringOpVals[i] ); }
        intOpMap = new LinkedHashMap<String, String>();
        for(int i = 0; i < INT_OP_LENGTH; i++) { intOpMap.put( intOpKeys[i], intOpVals[i] ); }
        enumOpMap = new LinkedHashMap<String, String>();
        for(int i = 0; i < ENUM_OP_LENGTH; i++) { enumOpMap.put( enumOpKeys[i], enumOpVals[i] ); }
        boolOpMap = new LinkedHashMap<String, String>();
        for(int i = 0; i < BOOL_OP_LENGTH; i++) { boolOpMap.put( boolOpKeys[i], boolOpVals[i] ); }

        searchPanel = new FormPanel("org/app/mp3AlbumManager/forms/searchPanel.jfrm");
        
        initComponents();
    }

    private void initComponents() {

        selectCB = searchPanel.getComboBox(ID_SELECTCB);
        selectCB.setPrototypeDisplayValue(SELECT_PROTO_VALUE);
        fillCb(selectCB, selectKeys);
        selectCB.addActionListener(this);

        propCB = searchPanel.getComboBox(ID_PROPCB);
        propCB.setPrototypeDisplayValue(PROP_PROTO_VALUE);
        fillCb(propCB, albumPropKeys);
        propCB.addActionListener(this);

        opCB = searchPanel.getComboBox(ID_OPCB);
        opCB.setPrototypeDisplayValue(OP_PROTO_VALUE);
        fillCb(opCB, stringOpKeys);
        opCB.addActionListener(this);

        valCB = searchPanel.getComboBox(ID_VALCB);
        valCB.setPrototypeDisplayValue(VAL_PROTO_VALUE);
        fillCb(valCB, stringVals);

        searchBtn = searchPanel.getButton(ID_SEARCHBTN);
        messageLabel = searchPanel.getLabel(ID_MESSAGELABEL);

        //======== this ========
        setBackground(bgcolor);

        //LAYOUT
        setLayout( new BorderLayout() );
        add(searchPanel, BorderLayout.CENTER);

    }

    public void showPanel() {
        setVisible(true);
        super.repaint();
    }

    public void setSearchButtonListener(ActionListener al) {
        searchBtn.addActionListener(al);
        searchBtn.setActionCommand("search");
    }


    private void fillCb(JComboBox cb, String[] vals) {
        cb.removeAllItems();
        for(String s : vals) { cb.addItem(s); }
        cb.setSelectedItem(vals[0]);
    }

    private void setValCb(Object[] vals, boolean editable) {
        valCB.removeAllItems();
        for(Object o : vals) { valCB.addItem(o); }
        valCB.setSelectedItem(vals[0]);
        valCB.setEditable(editable);
    }

    // getters for comboboxes and textfields
    private String getCbItem(JComboBox cb) {
        Object item = cb.getSelectedItem();
        return (item == null) ? "" : item.toString();
    }

    public void setMessage(boolean ok) {
        String msg = (ok) ? "" : "Please select all fields!";
        messageLabel.setText(msg);
    }

    public void setQuery(String q) { query = q; }

    public void addToQuery(String q) { query += q; }

    public String getQuery() { return query; }

    private void resetPrepVals() { prepValue.clear(); }

    private void addToPrepVals(Object o) { if(o != null ) prepValue.add(o); }

    public ArrayList<Object> getPrepVals() { return prepValue; }

    public String getCurrentSelect() { return currSelect; }

    public String getHumanReadableSearch() { return humanReadableSearch; }

    public void setHumanReadableSearch(String search) { humanReadableSearch = search; }

    public void addToHumanReadableSearch(String search) { humanReadableSearch += search; }

    public boolean doBeforeSearch() {

        resetPrepVals();

        String query = "";
        String select, prop, oper, val;
        // get the select
        select = getCbItem(selectCB);
        for( Map.Entry<String, String> entry: selectMap.entrySet() ) {
            if( select.equals( entry.getKey() ) ) query = entry.getValue();
        }
        setQuery(query);
        currSelect = select;
        String humanSearch = ( currSelect.equals(SELECT_ALBUM_NAME) ) ? "Search albums where" : "Search songs where";
        setHumanReadableSearch(humanSearch);
        // get the property
        prop = getCbItem(propCB);
        for( Map.Entry<String, String> entry: currPropMap.entrySet() ) {
            if( prop.equals( entry.getKey() ) ) query = entry.getValue();
        }
        addToQuery(query);
        addToHumanReadableSearch(" " + prop);
        // get the operator
        oper = getCbItem(opCB);
        for( Map.Entry<String, String> entry: currOpMap.entrySet() ) {
            if( oper.equals( entry.getKey() ) ) query = entry.getValue();
        }
        addToQuery(query);
        addToHumanReadableSearch(" " + oper);
        // check for empty query & value
        if( getQuery().isEmpty() | getCbItem(valCB).isEmpty() ) {
            setMessage(false);
            return false;
        }

        // get the value
        if( oper.equals(OP_CONTAINS_NAME) | select.equals(OP_CONTAINSNOT_NAME) ) {
            // add wildcards to prepvalue
            val = "%" + getCbItem(valCB) + "%";
        } else {
            val = getCbItem(valCB);
        }
        addToPrepVals(val);
        addToHumanReadableSearch(" " + val);
        return true;
    }

    public void actionPerformed(ActionEvent e) {

        JComboBox cb = (JComboBox) e.getSource();

        String select;

        if( cb == selectCB ) {
            select = getCbItem(selectCB);
            if( select.equals(SELECT_ALBUM_NAME) ) {
                fillCb(propCB, albumPropKeys);
                currPropMap = albumPropMap;
            } else {
                fillCb(propCB, songPropKeys);
                currPropMap = songPropMap;
            }
        } else if(cb == propCB) {
            select = getCbItem(propCB);
            if( select.equals(PROP_ALBUM_NAME) | select.equals(PROP_ARTIST_NAME)
                    | select.equals(PROP_TITLE_NAME)  ) {
                fillCb(opCB, stringOpKeys);
                setValCb(stringVals, true);
                currOpMap = stringOpMap;
            } else if( select.equals(PROP_VARIOUS_NAME) | select.equals(PROP_VBR_NAME) ) {
                fillCb(opCB, boolOpKeys);
                setValCb(boolVals, false);
                currOpMap = boolOpMap;
            } else if( select.equals(PROP_MODE_NAME) | select.equals(PROP_GENRE_NAME)
                    | select.equals(PROP_LAME_NAME) | select.equals(PROP_TAG_NAME) ) {
                fillCb(opCB, enumOpKeys);
                currOpMap = enumOpMap;
                if( select.equals(PROP_MODE_NAME) ) setValCb(modeVals, false);
                if( select.equals(PROP_GENRE_NAME) ) setValCb(genreVals, false);
                if( select.equals(PROP_LAME_NAME) ) setValCb(lameVals, false);
                if( select.equals(PROP_TAG_NAME) ) setValCb(tagVals, false);
            } else {
                fillCb(opCB, intOpKeys);
                currOpMap = intOpMap;
                if( select.equals(PROP_BITRATE_NAME) ) setValCb(bitrateVals, false);
                else if( select.equals(PROP_FREQ_NAME) ) setValCb(freqVals, false);
                else setValCb(intVals, true);
            }
        } else if(cb == opCB) {
            select = getCbItem(opCB);
            if( select.equals(OP_HAS_NAME) | select.equals(OP_HASNOT_NAME) ) {
                setValCb(existsVals, false);
            }
        }
    }


}
