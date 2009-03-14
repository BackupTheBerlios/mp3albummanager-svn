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
import java.util.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

import com.jeta.forms.components.panel.FormPanel;

public class Controller implements ActionListener {
    /**
     * Constant for the All Albums item in the albums list.
     */
    private static final String ALL_ALBUMS_ITEM = "-> ALL Albums";
    private final View view;
    private final Model model;
    private boolean anyRecent;
    private final boolean verbose;
    private boolean showingClose, showingNew, showingOpen, showingInfo, showingList, showingDetails;
    private StartupPanel startupPanel;
    private OpenDBPanel openPanel;
    private CreateDBPanel createPanel;
    private InfoDBPanel infoPanel;
    private ListPanel listPanel;
    private DetailsPanel detailsPanel;

    public int nrOfSongsInDB;

    private Task updateTask;

    private Color bgcolor;

    public Controller(Model m, View v, boolean recent, boolean verbose, Color color) {

        view = v;
        model = m;
        this.verbose = verbose;
        this.anyRecent = recent;
        bgcolor = color;

        nrOfSongsInDB = 0;

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
                if(showingInfo) { view.remove(infoPanel); showingInfo = false; }
                if(showingList) { view.remove(listPanel); showingList = false; }
                if(showingDetails) { view.remove(detailsPanel); showingDetails = false; }

                // check for recent db entries
                setRecent( model.getRecentEntries() );

                //setup menu items (same as startup + ENABLE new)
                view.initializeMenu( getRecent() );
                view.enableMenuItem(view.menuItemNew);
                view.enableButton(view.newButton);

                // show the empty startup panel
                startupPanel = new StartupPanel(bgcolor);
                view.addPanel(startupPanel);
                showingClose = true;
            }
        }

        if( actionCommand.equals("new") ) {
            if( ! showingNew ) {
                // remove other panels
                if(showingClose) { view.remove(startupPanel); showingClose = false; }
                if(showingOpen) { view.remove(openPanel); showingOpen = false; }
                if(showingInfo) { view.remove(infoPanel); showingInfo = false; }
                if(showingList) { view.remove(listPanel); showingList = false; }

                // setup menu items (same as startup + DISABLE new + ENABLE close)
                view.initializeMenu( false ); // false -> DISABLE open
                view.disableMenuItem(view.menuItemNew);
                view.disableButton(view.newButton);
                view.enableMenuItem(view.menuItemClose);
                view.enableButton(view.closeButton);

                createPanel = new CreateDBPanel(bgcolor);
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
                if(showingInfo) { view.remove(infoPanel); showingInfo = false; }
                if(showingList) { view.remove(listPanel); showingList = false; }

                // setup menu items (same as startup + DISABLE new + ENABLE close
                view.initializeMenu( false ); // false -> DISABLE open
                view.disableMenuItem(view.menuItemNew);
                view.disableButton(view.newButton);
                view.enableMenuItem(view.menuItemClose);
                view.enableButton(view.closeButton);

                openPanel = new OpenDBPanel(bgcolor);
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
                openPanel.setMusicField(mp3Dir);

                openPanel.showPanel();
                view.addPanel(openPanel);

                showingOpen = true;
            }
        } else if( actionCommand.equals("nfo") ) {

            // get selected album in listPanel
            String selectedAlbum = (String) listPanel.albumList.getSelectedValue();
            // get the nfo content
            StringBuffer content = model.getNfoContent(selectedAlbum);

            // get a preformatted jtextarea from view
            JTextArea nfoContent = view.setTextAreaContent( content.toString() );
            nfoContent.setCaretPosition(0);
            // put it in a scrollpane
            JScrollPane scrollPane = new JScrollPane(nfoContent);
            scrollPane.setPreferredSize(new Dimension(640, 800));
            // show it in a optionpane
            int option = JOptionPane.showConfirmDialog(null, scrollPane, "Save NFO?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

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

            // get generated html content
            StringBuffer content = model.getHtmlContent(musicDir, searchQuery, albums);

            // show jtextarea in a joptionpane
            int option = JOptionPane.showConfirmDialog(null, "Save HTML and launch in a browser", "Save HTML?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

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
            detailsPanel.showEditButtons(true);
            detailsPanel.showComboBoxes(true);
            // hide all other commands (except close)
            view.initializeMenu( false );
            view.enableMenuItem(view.menuItemClose);
            view.enableButton(view.closeButton);

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
                String mp3Dir = openPanel.getMusicField();
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
                String dbName = createPanel.getCollectionField();
                if( dbName.isEmpty() ) { createPanel.setMessageLabel("Please fill in a database name"); return; }
                String user = createPanel.getUserField();
                if( user.isEmpty() ) { createPanel.setMessageLabel("Please fill in a username"); return; }
                String pass = createPanel.getPassField();
                if( pass.isEmpty() ) { createPanel.setMessageLabel("Please fill in a password"); return; }
                String mp3Dir = createPanel.getMusicField();
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
                createPanel.setMusicField( browse.getTarget() );

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
            openPanel.setMusicField(mp3Dir);

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
            nrOfSongsInDB = 0;
            try {
                while(rs.next()) {
                    nrOfSongsInDB++;
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
            String firstLabel = String.format("%d songs in database.", nrOfSongsInDB);
            String secondLabel = String.format("%d songs found in album directory.", model.getNrOfMp3s() );

            infoPanel = new InfoDBPanel(firstLabel, secondLabel, bgcolor);
            infoPanel.infoDBListener( new InfoPanelListener() );

            //TODO: update db option should be more sophisticated...
            //      db entry exists though file has been deleted -> deleted
            //      file exists but not found in db -> new

            // show update button if nr of mp3s != nr of records
            if( model.getNrOfMp3s() != nrOfSongsInDB) { infoPanel.showButton(infoPanel.updateButton, true); }

            infoPanel.showPanel();
            view.addPanel(infoPanel);

            showingInfo = true;
        }
    }

    /**
     * Listener inner class for buttons in dbInfoPanel and progress bar.
     */
    private class InfoPanelListener implements ActionListener, PropertyChangeListener {
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
                if(showingInfo) { view.remove(infoPanel); showingInfo = false; }

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

                listPanel = new ListPanel(bgcolor);
                listPanel.selectionPanelListener( new ListPanelListener() );

                // add "ALL ALBUMS" to the top of the albumlist in the listpanel
                listPanel.albumListModel.addElement( ALL_ALBUMS_ITEM );

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
                detailsPanel = new DetailsPanel(bgcolor);
                detailsPanel.setDetailsButtonListener( new DetailsButtonListener() );
                detailsPanel.showPanel();
                view.addPanel(detailsPanel, BorderLayout.CENTER);
                showingDetails = true;
                showingList = true;

            } else if( actionCommand.equals("update") ) {

                // create the update database task and add a property change listener to monitor progress
                updateTask = new Task();
                updateTask.addPropertyChangeListener(this);
                updateTask.execute();

            }
        }

        /**
         * Listener for the progress bar.
         * @param evt the change event. 
         */
        public void propertyChange(PropertyChangeEvent evt) {

            if( "progress" == evt.getPropertyName() ) {

                int progress = (Integer) evt.getNewValue();
                infoPanel.progressBar.setValue(progress);
            }
        }

    }

    /**
     * Inner class Task is a sub-class of SwingWorker.
     * The task instance invokes the doInBackground in a separate thread.
     */
    class Task extends SwingWorker<Void, Void> {

        long startTimeMs;
        /**
         * This is where the update database task is actually executed.
         * <br />
         * Using a background thread instead of the event-dispatching thread prevents the user interface from freezing while the task is running. 
         */
        @Override
        public Void doInBackground() {

            if(verbose) System.out.println("\nBACKGROUND TASK: started");
            startTimeMs = System.currentTimeMillis( );

            // initialize task progress
            setProgress(0);
            // show the progressbar, max value is set to twice the number of mp3s in the entry's directory (one for reading the tags an one for inserting into database)
            infoPanel.showProgressBar( 2 * model.getNrOfMp3s() );
            // disable the nextButton in infoPanel
            infoPanel.enableButton(infoPanel.nextButton, false);

            // get current selected entry
            DBEntry entry = model.getCurrentEntry();
            // get album directory of entry
            File currentMp3dir = entry.getMp3Dir();
            // create an instance of ReadDirectories to read the mp3 files in the album directory
            ReadDirectories read = new ReadDirectories(currentMp3dir, verbose);
            // read the tags of the files, pass the progressbar along to update progress
            read.getFiles( infoPanel.getTheProgressbar() );

            // get the progress (should be equal to number of read tags
            int progressValue = infoPanel.getTheProgressbar().getValue();


            if(verbose) System.out.println("\nUPDATING DATABASE:");
            //CONNECTION -> INSERT QUERIES
            String insertAlbum = "INSERT INTO Album (directory, subdirs, tracks, artist, title, various, albumlength, " +
                    "albumyear, genre, tag, lame, bitrate, vbr, frequency, mode) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            String insertSongs = "INSERT INTO Song (filename, album, subdir, track, artist, title, songlength, songyear, " +
                    "genre, tag, lame, bitrate, vbr, frequency, mode) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmtAlbum, stmtSong;

            int inserted = 0;
            try {
                Set<Album> albumSet = read.getCollection().getAlbumSet();
                if(verbose) System.out.println("NUMBER OF ALBUMS IN SET: " + albumSet.size());
                for(Album a : albumSet) {
                    a.fillKeys();
                    a.sortByValues();
                    a.sortSongs();

                    stmtAlbum = model.getDAO().getConnection().prepareStatement(insertAlbum);
                    stmtAlbum.setString(1, a.getFilename().getPath() );
                    // generate a comma-separeted string from subdirs
                    ArrayList subdirs = a.getSubdirs();
                    String subdirsString = "";
                    for(int i = 0; i < subdirs.size(); i++) {
                        subdirsString += subdirs.get(i);
                        if(i != subdirs.size() -1) { subdirsString += ", "; }
                    }
                    stmtAlbum.setString(2, subdirsString );

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
                        stmtSong.setString(2, s.getAlbum() );
                        stmtSong.setString(3, s.getSubdir() );
                        stmtSong.setString(4, s.getTrack() );
                        stmtSong.setString(5, s.getArtist() );
                        stmtSong.setString(6, s.getTitle() );
                        stmtSong.setLong(7, s.getLength() );
                        stmtSong.setString(8, s.getYear() );
                        stmtSong.setString(9, s.getGenre() );
                        stmtSong.setString(10, s.getTag() );
                        stmtSong.setString(11, s.getLame() );
                        stmtSong.setInt(12, s.getBitrate() );
                        stmtSong.setBoolean(13, s.getVbr() );
                        stmtSong.setInt(14, s.getFrequency() );
                        stmtSong.setString(15, s.getMode() );

                        // insert song into database and increase number of inserted songs
                        model.getDAO().insertValues(stmtSong);
                        // increase number of inserted songs
                        inserted++;
                        // update progress
                        progressValue++;
                        // update progress bar
                        infoPanel.getTheProgressbar().setValue(progressValue);

                        if(verbose) System.out.println(s.getTrack() + " - " + s.getTitle() );
                        nrOfSongsInDB = inserted;
                    } // end song iteration
                    if(verbose) System.out.println();

                } // end album iteration

            } catch(SQLException e) { e.printStackTrace(); }
            return null;
        }


        /**
         * When the update database task is complete, the instance invokes the done method in the event-dispatching thread.
         * <br />
         * When done in this case the update button in the dbInfoPanel is hidden.
         */
        @Override
        public void done() {

            // denable the nextButton in infoPanel
            infoPanel.enableButton(infoPanel.nextButton, true);
            // update text in labels
            infoPanel.setDBLabelText( String.format("%d songs in database.", nrOfSongsInDB) );
            long taskTimeMs  = System.currentTimeMillis( ) - startTimeMs;
            //if(verbose) 
            System.out.println("\nTASK DONE: \nnrOfSongsInDB = " + nrOfSongsInDB + "\ntaskTime = " + taskTimeMs / 1000 + " seconds\n");
            // hide the update button
            infoPanel.showButton(infoPanel.updateButton, false);

        }
    }

    /**
     * Listener inner class for lists in listPanel.
     */
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
                        String selectedAlbum = (String) listPanel.albumList.getSelectedValue();
                        if(verbose) System.out.println("\nSelected an album: " + selectedAlbum);

                        // empty all fields in details panel
                        detailsPanel.resetAllFields();

                        // selected ALL ALBUMS
                        if( selectedAlbum.equals(ALL_ALBUMS_ITEM) ) {
                            // disable edit, delete, NFO in menu
                            view.disableMenuItem(view.menuItemEdit);
                            view.disableButton(view.editButton);
                            view.disableMenuItem(view.menuItemDelete);
                            view.disableButton(view.deleteButton);
                            view.disableMenuItem(view.menuItemNFO);
                            view.disableButton(view.nfoButton);

                            // empty the songlist
                            listPanel.songListModel.removeAllElements();
                            // show all songs in the songlist
                            String songsQuery = "SELECT title FROM Song ORDER BY title";
                            ResultSet rs = model.getDAO().doSelectQuery(songsQuery, model.getDAO().getConnection(), false);
                            try {
                                while(rs.next()) {
                                    listPanel.songListModel.addElement( rs.getString("title") );
                                }
                            } catch(SQLException e1) { e1.printStackTrace(); }

                            return;
                        }

                        // selected a single album

                        // enable edit, delete, NFO in menu
                        view.enableMenuItem(view.menuItemEdit);
                        view.enableButton(view.editButton);
                        view.enableMenuItem(view.menuItemDelete);
                        view.enableButton(view.deleteButton);
                        view.enableMenuItem(view.menuItemNFO);
                        view.enableButton(view.nfoButton);

                        // get the album fields for the selected album
                        Album selAlbum = null;
                        String prepSelect = "SELECT * FROM Album WHERE title=?";
                        try {
                            PreparedStatement stmt = model.getDAO().getConnection().prepareStatement(prepSelect);
                            stmt.setString(1, selectedAlbum);
                            ResultSet rs = model.getDAO().executePreparedStmt(stmt);
                            while( rs.next() ) {
                                String albumDirectory = rs.getString("directory");
                                String albumArtist = rs.getString("artist");
                                detailsPanel.setArtistTextField( albumArtist );
                                detailsPanel.setBitrateTextField( rs.getInt("bitrate") + "");
                                detailsPanel.setFrequencyTextField( rs.getInt("frequency") + "");
                                String genre = rs.getString("genre");
                                detailsPanel.setGenreTextField( model.getGenreName(genre) );
                                detailsPanel.setLameTextField( rs.getString("lame") );
                                int length = rs.getInt("albumlength");
                                detailsPanel.setLengthTextField( model.getLengthAsString(length) );
                                detailsPanel.setModeTextField( rs.getString("mode") );
                                detailsPanel.setSubdirTextField( rs.getString("subdirs") );
                                detailsPanel.setTagTextField( rs.getString("tag") );
                                String albumTitle = rs.getString("title");
                                detailsPanel.setTitleTextField( albumTitle );
                                detailsPanel.setTrackTextField( rs.getString("tracks") );
                                boolean variousAlbum = rs.getBoolean("various");
                                if( variousAlbum ) detailsPanel.setVarious();  else detailsPanel.setNotVarious();
                                if( rs.getBoolean("vbr") ) detailsPanel.setVbr();  else detailsPanel.setNotVbr();
                                String albumYear = rs.getString("albumyear");
                                detailsPanel.setYearTextField( albumYear );
                                // create an album instance
                                selAlbum = new Album(new File( albumDirectory ), albumArtist, albumTitle, albumYear, variousAlbum );
                            }
                        } catch(SQLException e2) { e2.printStackTrace(); }

                        // empty the songlist
                        listPanel.songListModel.removeAllElements();
                        // get the song properties to fill the keys of the album maps
                        Song albumSong;
                        String prepSonglist = "SELECT * FROM Song WHERE album=? ORDER BY title";
                        try {
                            PreparedStatement stmtSonglist = model.getDAO().getConnection().prepareStatement(prepSonglist);
                            stmtSonglist.setString(1, selectedAlbum);
                            ResultSet rs = model.getDAO().executePreparedStmt(stmtSonglist);
                            while(rs.next()) {
                                // get the song properties
                                String songFilename = rs.getString("filename");
                                String songSubdir = rs.getString("subdir");
                                String songAlbum = rs.getString("album");
                                String songTrack = rs.getString("track");
                                String songArtist = rs.getString("artist");
                                String songTitle = rs.getString("title");
                                String songYear = rs.getString("songyear");
                                int songLength = rs.getInt("songlength");
                                String songGenre = rs.getString("genre");
                                String songTag = rs.getString("tag");
                                String songLame = rs.getString("lame");
                                int songBitrate = rs.getInt("bitrate");
                                boolean songVbr = rs.getBoolean("vbr");
                                int songFrequency = rs.getInt("frequency");
                                String songMode = rs.getString("mode");

                                // create a song instance
                                albumSong = new Song(new File(songFilename), songSubdir, songTrack, songArtist,
                                        songTitle, songLength, songAlbum, songYear, songGenre, songTag, songLame,
                                        songBitrate, songVbr, songFrequency, songMode);
                                // add the song to the album instance
                                if(selAlbum != null) selAlbum.addSong(albumSong);

                                // update the songlist in the listpanel
                                listPanel.songListModel.addElement( songTitle );
                            }
                            // fill the keys of the album maps
                            Map<String, Integer> stringMap;
                            Map<Integer, Integer> integerMap;
                            if(selAlbum != null) {
                                selAlbum.fillKeys();
                                selAlbum.sortByValues();
                                // year
                                stringMap = selAlbum.getYearsMap();
                                for( Map.Entry<String, Integer> entry : stringMap.entrySet() ) {
                                    detailsPanel.addToYearComboBox( entry.getKey() );
                                }
                                //genre
                                stringMap = selAlbum.getGenresMap();
                                for( Map.Entry<String, Integer> entry : stringMap.entrySet() ) {
                                    detailsPanel.addToGenreComboBox( model.getGenreName( entry.getKey() ) );
                                }
                                //tag
                                stringMap = selAlbum.getTagsMap();
                                for( Map.Entry<String, Integer> entry : stringMap.entrySet() ) {
                                    detailsPanel.addToTagComboBox( entry.getKey() );
                                }
                                // lame
                                stringMap = selAlbum.getLameMap();
                                for( Map.Entry<String, Integer> entry : stringMap.entrySet() ) {
                                    detailsPanel.addToLameComboBox( entry.getKey() );
                                }
                                // bitrate
                                integerMap = selAlbum.getCbrsMap();
                                for( Map.Entry<Integer, Integer> entry : integerMap.entrySet() ) {
                                    detailsPanel.addToBitrateComboBox( entry.getKey() + "");
                                }
                                integerMap = selAlbum.getVbrsMap();
                                for( Map.Entry<Integer, Integer> entry : integerMap.entrySet() ) {
                                    detailsPanel.addToBitrateComboBox( "~" + entry.getKey());
                                }
                                // frequency
                                integerMap = selAlbum.getFreqsMap();
                                for( Map.Entry<Integer, Integer> entry : integerMap.entrySet() ) {
                                    detailsPanel.addToFrequencyComboBox( "" + entry.getKey());
                                }
                                // mode
                                stringMap = selAlbum.getModesMap();
                                for( Map.Entry<String, Integer> entry : stringMap.entrySet() ) {
                                    detailsPanel.addToModeComboBox( entry.getKey() );
                                }
                            }

                        } catch(SQLException e4) { e4.printStackTrace(); }

                    }

                } else if( list.getName().equals("songs") ) {

                    selectedIndex = listPanel.songList.getSelectedIndex();

                    if(selectedIndex == -1) {
                        // no selection
                    } else {
                        // a song is selected
                        String selectedSong = (String) listPanel.songList.getSelectedValue();
                        if(verbose) System.out.println("\nSelected a song: " + selectedSong);

                        // empty all fields in details panel
                        detailsPanel.resetAllFields();

                        // enable edit, delete in menu
                        view.enableMenuItem(view.menuItemEdit);
                        view.enableButton(view.editButton);
                        view.enableMenuItem(view.menuItemDelete);
                        view.enableButton(view.deleteButton);
                        // disable NFO in menu
                        view.disableMenuItem(view.menuItemNFO);
                        view.disableButton(view.nfoButton);

                        // get the song fields
                        String prepSelect = "SELECT * FROM Song WHERE title=?";
                        try {
                            PreparedStatement stmt = model.getDAO().getConnection().prepareStatement(prepSelect);
                            stmt.setString(1, selectedSong);
                            ResultSet rs = model.getDAO().executePreparedStmt(stmt);
                            while( rs.next() ) {
                                detailsPanel.setArtistTextField( rs.getString("artist") );
                                detailsPanel.setBitrateTextField( rs.getInt("bitrate") + "");
                                detailsPanel.setFrequencyTextField( rs.getInt("frequency") + "");
                                String genre = rs.getString("genre");
                                detailsPanel.setGenreTextField( model.getGenreName(genre) );
                                detailsPanel.setLameTextField( rs.getString("lame") );
                                int length = rs.getInt("songlength");
                                detailsPanel.setLengthTextField( model.getLengthAsString(length) );
                                detailsPanel.setModeTextField( rs.getString("mode") );
                                detailsPanel.setSubdirTextField( rs.getString("subdir") );
                                detailsPanel.setTagTextField( rs.getString("tag") );
                                detailsPanel.setTitleTextField( rs.getString("title") );
                                detailsPanel.setTrackTextField( rs.getString("track") );
                                if( rs.getBoolean("vbr") ) detailsPanel.setVbr();  else detailsPanel.setNotVbr();
                                detailsPanel.setYearTextField( rs.getString("songyear") );
                            }
                        } catch(SQLException e5) { e5.printStackTrace(); }
                    }
                }
            } // end if
        } // end method

    } // end class ListPanelListener

    /**
     * Listener inner class for buttons in detailsPanel.
     */
    private class DetailsButtonListener implements ActionListener {
        /**
         * Actions for the buttons in detailsPanel.
         * @param ae the action event.
         */
        public void actionPerformed(ActionEvent ae) {
            String actionCommand=ae.getActionCommand();

            if( actionCommand.equals("cancelEdit") ) {
                //TODO: implement actions for cancel button
            } else if( actionCommand.equals("updateEdit") ) {
                //TODO: implement actions for update button
            }
        }
    }
}