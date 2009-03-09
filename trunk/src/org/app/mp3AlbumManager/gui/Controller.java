package org.app.mp3AlbumManager.gui;

import org.app.mp3AlbumManager.util.FileBrowser;
import org.app.mp3AlbumManager.util.LaunchBrowser;
import org.app.mp3AlbumManager.db.DBEntry;
import org.app.mp3AlbumManager.data.ReadDirectories;
import org.app.mp3AlbumManager.data.Album;
import org.app.mp3AlbumManager.data.Song;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.*;
import java.io.File;
import java.sql.*;
import java.util.Set;

public class Controller implements ActionListener {

    private final View view;
    private final Model model;
    private boolean anyRecent;
    private final boolean verbose;
    private boolean showingClose, showingNew, showingOpen, showingInfo, showingList;
    private StartupPanel startupPanel;
    private OpenDBPanel openPanel;
    private CreateDBPanel createPanel;
    private InfoDBPanel dbInfoPanel;
    private ListPanel listPanel;
    private DetailsPanel detailsPanel;
    private int nrOfInsertedSongs = 0;

    public Controller(Model m, View v, boolean recent, boolean verbose) {

        view = v;
        model = m;
        this.verbose = verbose;
        this.anyRecent = recent;

        // Add action listener to buttons in view
        view.menuActionListeners(this);
        // initialize (disable/enable) menu buttons and items
        view.initializeMenu( getRecent() );

    }

    /**
     * Set the recent value.
     * @param recent Whether found any recent database entries.
     */
    public void setRecent(boolean recent) { anyRecent = recent; }

    /**
     * Get the recent value.
     * @return Whether found any recent database entries.
     */
    public boolean getRecent() { return anyRecent; }

    /**
     * Actions for items and buttons in menu.
     * @param ae The ActionEvent
     */
    public void actionPerformed(ActionEvent ae) {

        String actionCommand=ae.getActionCommand();

        //TODO: any cleanup before quit application?
        if( actionCommand.equals("quit") ) { System.exit(0); }

        if( actionCommand.equals("close") ) {
            if( ! showingClose ) {
                // disconnect from db
                if(model.getDAO() != null) {
                    if(model.getDAO().getConnection() != null)
                        model.getDAO().disconnect();
                }

                // remove other panels
                if(showingOpen) { view.remove(openPanel); showingOpen = false; }
                if(showingNew) { view.remove(createPanel); showingNew = false; }
                if(showingInfo) { view.remove(dbInfoPanel); showingInfo = false; }
                if(showingList) { view.remove(listPanel); showingList = false; }

                // check for recent db entries
                setRecent( model.getRecentEntries() );

                //setup menu items (same as startup + ENABLE new)
                view.initializeMenu( getRecent() );
                view.enableMenuItem(view.menuItemNew);
                view.enableButton(view.newButton);

                // show the empty startup panel
                startupPanel = new StartupPanel();
                view.addPanel(startupPanel);
                showingClose = true;
            }
        }

        if( actionCommand.equals("new") ) {
            if( ! showingNew ) {
                // remove other panels
                if(showingClose) { view.remove(startupPanel); showingClose = false; }
                if(showingOpen) { view.remove(openPanel); showingOpen = false; }
                if(showingInfo) { view.remove(dbInfoPanel); showingInfo = false; }
                if(showingList) { view.remove(listPanel); showingList = false; }

                // setup menu items (same as startup + DISABLE new + ENABLE close)
                view.initializeMenu( false ); // false -> DISABLE open
                view.disableMenuItem(view.menuItemNew);
                view.disableButton(view.newButton);
                view.enableMenuItem(view.menuItemClose);
                view.enableButton(view.closeButton);

                createPanel = new CreateDBPanel();
                createPanel.createDBListener( new OpenAndCreatePanelListener() );

                createPanel.showPanel();
                view.addPanel(createPanel);
                showingNew = true;

            }

        } else if( actionCommand.equals("open") ) {
            if( ! showingOpen ) {
                // remove other panels
                if(showingClose) { view.remove(startupPanel); showingClose = false; }
                if(showingNew) { view.remove(createPanel); showingNew = false; }
                if(showingInfo) { view.remove(dbInfoPanel); showingInfo = false; }
                if(showingList) { view.remove(listPanel); showingList = false; }

                // setup menu items (same as startup + DISABLE new + ENABLE close
                view.initializeMenu( false ); // false -> DISABLE open
                view.disableMenuItem(view.menuItemNew);
                view.disableButton(view.newButton);
                view.enableMenuItem(view.menuItemClose);
                view.enableButton(view.closeButton);

                openPanel = new OpenDBPanel();
                openPanel.openDBButtonListener( new OpenAndCreatePanelListener() );
                openPanel.openDBItemListener( new OpenAndCreatePanelListener() );

                openPanel.populateComboBox( model.getNamesFromEntries() ); // populate combobox in panel with dirs from db entries
                String selectedDBName = openPanel.getSelectedDatabase(); // get the autoselected (first) item in combobox
                model.setCurrentEntry(selectedDBName); // set current entry
                // get dbDir & mp3Dir for current entry
                String dbDir = model.getCurrentEntry().getDBDir();
                String mp3Dir = model.getCurrentEntry().getMp3Dirname();
                // fill dbDirField & mp3DirField with values
                openPanel.setDBDirField(dbDir);
                openPanel.setMp3DirField(mp3Dir);

                openPanel.showPanel();
                view.addPanel(openPanel);

                showingOpen = true;
            }
        } else if( actionCommand.equals("nfo") ) {

            // get selected album in listPanel
            String selectedAlbum = (String) listPanel.albumList.getSelectedValue();
            String query = "SELECT * FROM Album WHERE title='"+selectedAlbum+"'";
            StringBuffer content = model.getNfoContent(query);

            // get a preformatted jtextarea from view
            JTextArea nfoContent = view.setTextAreaContent( content.toString() );
            // show jtextarea in a joptionpane
            int option = JOptionPane.showConfirmDialog(null, nfoContent, "Save NFO?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            // handle response from joptionpane
            if(option == JOptionPane.OK_OPTION) {
                // save the content in the selected nfo file
                FileBrowser browse = new FileBrowser(JFileChooser.SAVE_DIALOG, JFileChooser.FILES_ONLY);
                String saveNfo = browse.getTarget();
                if(saveNfo != null) model.writeFile(content, saveNfo);
            }

        } else if( actionCommand.equals("html") ) {

            // open a file browser to save the html file
            FileBrowser browse = new FileBrowser(JFileChooser.SAVE_DIALOG, JFileChooser.FILES_ONLY);
            String saveHTML = browse.getTarget();

            // get music dir
            String musicDir = model.getCurrentEntry().getMp3Dirname();

            // get search query
            String searchQuery = model.getDAO().getCurrentSearchQuery();
            if(searchQuery == null) { searchQuery = "SELECT * FROM Album"; }

            // get album list from listPanel
            String[] albums = listPanel.getElementsFromAlbumList();
            // generate queries
            int size = albums.length;
            String[] queries = new String[size];
            String title;
            for(int i = 0; i < size; i++) {
                title = albums[i];
                queries[i] = "SELECT directory, artist, title FROM Album WHERE title='" + title + "'";
            }
            // get generated html content
            StringBuffer content = model.getHtmlContent(musicDir, searchQuery, queries);

            // show jtextarea in a joptionpane
            int option = JOptionPane.showConfirmDialog(null, "Save HTML and lanuch in a browser", "Save HTML?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            // handle response from joptionpane
            if(option == JOptionPane.OK_OPTION) {
                // save the content in the selected nfo file
                if(saveHTML != null) {
                    model.writeFile(content, saveHTML);
                    LaunchBrowser browser = new LaunchBrowser(saveHTML);
                    browser.launch();
                }
            }

        } else if( actionCommand.equals("edit") ) {

            // make fields editable and show buttons
            detailsPanel.setEditableFields(true);
            detailsPanel.setVisibleButtons(true);

        } else if( actionCommand.equals("delete") ) {
            //TODO: implement actions for Delete
            // get the selected album or song
            // show optionpane confirmation dialog
            // if OK:
            //      construct the query
            //      execute query
            //      update albums and songs in listPanel
        }

    }

    /**
     * Actions for buttons and combobox in openDBPanel and createDBPanel.
      */
    private class OpenAndCreatePanelListener implements ActionListener, ItemListener {

        /**
         * Actions for buttons in openDBPanel and createDBPanel.
         * @param ae The ActionEvent.
         */
        public void actionPerformed(ActionEvent ae) {

            String actionCommand=ae.getActionCommand();

            if( actionCommand.equals("openDB") ) {
                boolean proceed = true;
                // check if an item is selected in combobox
                if( openPanel.getSelectedDatabase() == null) { openPanel.setMessageLabel("Please select database"); proceed = false; }
                // get field values for directory, username & password
                // check if any field is empty
                String mp3Dir = openPanel.getMp3DirField();
                String dbDir = openPanel.getDBDirField();
                if( mp3Dir.isEmpty() || dbDir.isEmpty() ) { openPanel.setMessageLabel("Please select database"); proceed = false; }
                String user = openPanel.getUserField();
                if( user.isEmpty() ) { openPanel.setMessageLabel("Please fill in a username"); proceed = false; }
                String pass = openPanel.getPassField();
                if( pass.isEmpty() ) { openPanel.setMessageLabel("Please fill in a password"); proceed = false; }

                //CONNECTION -> LOGIN
                Connection conn = null;
                if(proceed) {
                    boolean doCreate = false;
                    model.setDAO(doCreate);
                    if( model.initDB() ) { conn = model.getDAO().connect(user, pass); }
                }

                if(conn == null) {
                    openPanel.setMessageLabel("Problem connecting to database: Wrong username or incorrect password. Please try again.");
                } else {
                    showDBInfoPanel(mp3Dir);
                }

            } else if( actionCommand.equals("createDB") ) {
                // get field values for name, directory, username & password
                // check if any field is empty
                //TODO: check that dbname is unique, ie not found in earlier db entries
                String dbName = createPanel.getDatabaseField();
                if( dbName.isEmpty() ) { createPanel.setMessageLabel("Please fill in a database name"); return; }
                String user = createPanel.getUserField();
                if( user.isEmpty() ) { createPanel.setMessageLabel("Please fill in a username"); return; }
                String pass = createPanel.getPassField();
                if( pass.isEmpty() ) { createPanel.setMessageLabel("Please fill in a password"); return; }
                String mp3Dir = createPanel.getMp3DirField();
                if( mp3Dir.isEmpty() || ! mp3Dir.startsWith("/")) { createPanel.setMessageLabel("Please set albums directory"); return; }
                String dbDir = createPanel.getDBDirField();
                if( dbDir.isEmpty() || ! dbDir.startsWith("/")) { createPanel.setMessageLabel("Please set database directory"); return; }

                // create new entry
                DBEntry newEntry = new DBEntry(dbName, dbDir, mp3Dir);
                // add entry to set of entries
                model.addNewEntry(newEntry);
                // set current entry
                model.setCurrentEntry(dbName);

                //CONNECTION -> LOGIN & CREATE TABLES
                boolean doCreate = true;
                model.setDAO(doCreate);
                if( model.initDB() ) {
                    model.getDAO().createTables( model.getDAO().connect(user, pass) );
                } else {
                    System.err.println("ERROR: initialising db directory"); //DEBUG
                }

                // continue to database infoPanel
                showDBInfoPanel(mp3Dir);

                // actions for browse buttons in createPanel
            } else if( actionCommand.equals("browseMP3dir") ) {

                FileBrowser browse = new FileBrowser(JFileChooser.OPEN_DIALOG, JFileChooser.DIRECTORIES_ONLY);
                createPanel.setMp3DirField( browse.getTarget() );

            } else if( actionCommand.equals("browseDBdir") ) {

                FileBrowser browse = new FileBrowser(JFileChooser.OPEN_DIALOG, JFileChooser.DIRECTORIES_ONLY);
                createPanel.setDBDirField( browse.getTarget() );
            }
        }

        /**
         * Actions for combobox in openDBPanel.
         * @param ie The ItemEvent.
         */
        public void itemStateChanged(ItemEvent ie) {
            // get selected item in combobox
            JComboBox cb = (JComboBox) ie.getSource();
            String selectedDBName = (String) cb.getSelectedItem();
            // set current database entry in model
            model.setCurrentEntry(selectedDBName);
            // get dbDir & mp3Dir for current entry
            String dbDir = model.getCurrentEntry().getDBDir();
            String mp3Dir = model.getCurrentEntry().getMp3Dirname();
            // fill dbDirField & mp3DirField with values
            openPanel.setDBDirField(dbDir);
            openPanel.setMp3DirField(mp3Dir);

        }

        /**
         * Setup and show dbInfoPanel.
         * @param mp3Dir The album directory.
         */
        public void showDBInfoPanel(String mp3Dir) {

            // remove other panels
            if(showingClose) { view.remove(startupPanel); showingClose = false; }
            if(showingOpen) { view.remove(openPanel); showingOpen = false; }
            if(showingNew) { view.remove(createPanel); showingNew = false; }
            if(showingList) { view.remove(listPanel); showingList = false; }

            // setup menu items (nothing to do, same setup as in new & close)

            //CONNECTION -> SELECT QUERY
            String query = "SELECT filename FROM Song";
            ResultSet rs = model.getDAO().doSelectQuery( query, model.getDAO().getConnection(), false);
            int nrOfRecords = 0;
            try {
                while(rs.next()) {
                    nrOfRecords++;
                }
            } catch(SQLException e) { e.printStackTrace(); }

            // reset nr of mp3 files in the current entry
            model.resetMp3Entries();

            File currentMp3dir;

            // What's the difference?

            // ALT.1 get mp3 dir from current entry
            /*
            DBEntry entry = model.getCurrentEntry();
            currentMp3dir = entry.getMp3Dir();
            */
            // ALT.2 get mp3 dir from parameter
            currentMp3dir = new File(mp3Dir);

            // add found mp3 files to current entry
            model.addMp3Files(currentMp3dir);


            // format text to send to infopanel labels
            String firstLabel = String.format("%d songs in database.", nrOfRecords);
            String secondLabel = String.format("%d songs found in album directory.", model.getNrOfMp3s() );

            dbInfoPanel = new InfoDBPanel(firstLabel, secondLabel);
            dbInfoPanel.infoDBListener( new InfoPanelListener() );

            //TODO: update db option should be more sophisticated...
            //      db entry exists though file has been deleted -> deleted
            //      file exists but not found in db -> new

            // show update button if nrOfMp3s != nrOfRecords
            if(model.getNrOfMp3s() != nrOfRecords) { dbInfoPanel.showButton(dbInfoPanel.updateButton); }

            dbInfoPanel.showPanel();
            view.addPanel(dbInfoPanel);

            showingInfo = true;
        }
    }

    private class InfoPanelListener implements ActionListener {
        /**
         * Actions for buttons in dbInfoPanel.
         * @param ae The ActionEvent.
         */
        public void actionPerformed(ActionEvent ae) {

            String actionCommand=ae.getActionCommand();

            if( actionCommand.equals("next") ) {
                // remove other panels
                if(showingClose) { view.remove(startupPanel); showingClose = false; }
                if(showingOpen) { view.remove(openPanel); showingOpen = false; }
                if(showingNew) { view.remove(createPanel); showingNew = false; }
                if(showingInfo) { view.remove(dbInfoPanel); showingInfo = false; }

                // setup menu items (same as startup + DISABLE new + ENABLE close, search, html
                view.initializeMenu( false ); // false -> DISABLE open
                view.disableMenuItem(view.menuItemNew);
                view.disableButton(view.newButton);

                view.enableMenuItem(view.menuItemClose);
                view.enableButton(view.closeButton);
                view.enableMenuItem(view.menuItemSearch);
                view.enableButton(view.searchButton);
                view.enableMenuItem(view.menuItemHTML);
                view.enableButton(view.htmlButton);

                listPanel = new ListPanel();
                listPanel.selectionPanelListener( new ListPanelListener() );

                //CONNECTION -> SELECT QUERIES
                String albumsQuery = "SELECT title FROM Album ORDER BY title";
                ResultSet rs = model.getDAO().doSelectQuery(albumsQuery, model.getDAO().getConnection(), false);
                try {
                    while(rs.next()) {
                        listPanel.albumListModel.addElement( rs.getString("title") );
                    }
                } catch(SQLException e) { e.printStackTrace(); }

                String songsQuery = "SELECT title FROM Song ORDER BY title";
                rs = model.getDAO().doSelectQuery(songsQuery, model.getDAO().getConnection(), false);
                try {
                    while(rs.next()) {
                        listPanel.songListModel.addElement( rs.getString("title") );
                    }
                } catch(SQLException e) { e.printStackTrace(); }

                listPanel.showPanel();
                view.addPanel(listPanel, BorderLayout.WEST);

                //add the detailsPanel (fields empty until album or song is selected)
                detailsPanel = new DetailsPanel();
                detailsPanel.showPanel();
                view.addPanel(detailsPanel, BorderLayout.CENTER);

                showingList = true;

            } else if( actionCommand.equals("update") ) {

                // scan files in albums dir, and add new records to db
                // get current selected entry
                DBEntry entry = model.getCurrentEntry();
                File currentMp3dir = entry.getMp3Dir();

                ReadDirectories read = new ReadDirectories(currentMp3dir, true);
                read.getFiles();

                if(verbose) System.out.println("\nUPDATING DATABASE:");
                //CONNECTION -> INSERT QUERIES
                String insertAlbum = "INSERT INTO Album (directory, subdirs, tracks, artist, title, various, albumlength, " +
                        "albumyear, genre, tag, lame, bitrate, vbr, frequency, mode) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                String insertSongs = "INSERT INTO Song (filename, subdir, track, artist, title, songlength, songyear, " +
                        "genre, tag, lame, bitrate, vbr, frequency, mode) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement stmtAlbum, stmtSong;
                nrOfInsertedSongs = 0;
                try {
                    Set<Album> albumSet = read.getCollection().getAlbumSet();
                    for(Album a : albumSet) {
                        a.fillKeys();
                        a.sortByValues();
                        a.sortSongs();

                        stmtAlbum = model.getDAO().getConnection().prepareStatement(insertAlbum);
                        stmtAlbum.setString(1, a.getFilename().getPath() );
                        stmtAlbum.setObject(2, a.getSubdirs() ); // does this really work??
                        stmtAlbum.setInt(3, Integer.parseInt( a.getTrack() ) );
                        stmtAlbum.setString(4, a.getArtist() );
                        stmtAlbum.setString(5, a.getTitle() );
                        stmtAlbum.setBoolean(6, a.getVarious() );
                        stmtAlbum.setLong(7, a.getLength());
                        stmtAlbum.setString(8, a.getYear() );
                        stmtAlbum.setString(9, a.getGenre() );
                        stmtAlbum.setString(10, a.getTag() );
                        stmtAlbum.setString(11, a.getLame() );
                        stmtAlbum.setInt(12, a.getAlbumBitrate() );
                        stmtAlbum.setBoolean(13, a.getVbr() );
                        stmtAlbum.setInt(14, a.getFrequency() );
                        stmtAlbum.setString(15, a.getMode() );

                        model.getDAO().insertValues(stmtAlbum);

                        if(verbose) System.out.println( a.toString() );

                        for(Song s : a.getSonglist() ) {
                            stmtSong = model.getDAO().getConnection().prepareStatement(insertSongs);
                            stmtSong.setString(1, s.getFilename().getPath() );
                            stmtSong.setString(2, s.getSubdir() );
                            stmtSong.setString(3, s.getTrack() );
                            stmtSong.setString(4, s.getArtist() );
                            stmtSong.setString(5, s.getTitle() );
                            stmtSong.setLong(6, s.getLength() );
                            stmtSong.setString(7, s.getYear() );
                            stmtSong.setString(8, s.getGenre() );
                            stmtSong.setString(9, s.getTag() );
                            stmtSong.setString(10, s.getLame() );
                            stmtSong.setInt(11, s.getBitrate() );
                            stmtSong.setBoolean(12, s.getVbr() );
                            stmtSong.setInt(13, s.getFrequency() );
                            stmtSong.setString(14, s.getMode() );

                            model.getDAO().insertValues(stmtSong);
                            nrOfInsertedSongs++;

                            if(verbose) System.out.println(s.getTrack() + " - " + s.getTitle() );

                        }
                        if(verbose) System.out.println();
                    }

                } catch(SQLException e) { e.printStackTrace(); }

                // update text in labels
                dbInfoPanel.setDBLabelText( String.format("%d songs in database.", nrOfInsertedSongs) );
                // hide the update button
                dbInfoPanel.hideButton(dbInfoPanel.updateButton);
            }
        }
    }

    private class ListPanelListener implements ListSelectionListener {
        /**
         * Actions for lists in listPanel.
         * @param e The ListSelectionEvent.
         */
        public void valueChanged(ListSelectionEvent e) {

            if ( !e.getValueIsAdjusting() ) {

                int selectedIndex;
                JList list = (JList) e.getSource();

                if( list.getName().equals("albums") ) {

                    selectedIndex = listPanel.albumList.getSelectedIndex();

                    if(selectedIndex == -1) {
                        // no selection

                    } else {
                        // an album is selected
                        if(verbose) System.out.println("\nSelected an album: " + listPanel.albumList.getSelectedValue() );

                        // enable edit, delete, NFO in menu
                        view.enableMenuItem(view.menuItemEdit);
                        view.enableButton(view.editButton);
                        view.enableMenuItem(view.menuItemDelete);
                        view.enableButton(view.deleteButton);
                        view.enableMenuItem(view.menuItemNFO);
                        view.enableButton(view.nfoButton);

                        //TODO: ---> get album info from db and show in panel



                    }

                } else if( list.getName().equals("songs") ) {

                    selectedIndex = listPanel.songList.getSelectedIndex();

                    if(selectedIndex == -1) {
                        // no selection
                    } else {
                        // a song is selected
                        if(verbose) System.out.println("\nSelected a song: " + listPanel.songList.getSelectedValue() );
                        // enable edit, delete in menu
                        view.enableMenuItem(view.menuItemEdit);
                        view.enableButton(view.editButton);
                        view.enableMenuItem(view.menuItemDelete);
                        view.enableButton(view.deleteButton);
                        // disable NFO in menu
                        view.disableMenuItem(view.menuItemNFO);
                        view.disableButton(view.nfoButton);

                        //TODO: get song info from db and show in panel
                    }
                }
            } // end if ( !e.getValueIsAdjusting() )
        } // end method valueChanged()
    } // end class ListPanelListener

}