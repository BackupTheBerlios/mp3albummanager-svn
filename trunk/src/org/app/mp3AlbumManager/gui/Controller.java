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
import java.util.List;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

/**
 * Controller for the view
 * Note on queries:
 * do NOT enclose any question marks with single quotes (query will fail)
 * TODO: cleanup code - collect similar tasks in methods, try to move db actions to AlbumDAO or Model.
 */
public class Controller implements ActionListener {

    private static final String APP_NAME = "Mp3AlbumManager";
    private static final String APP_VERSION = "alpha-02";
    private static final String APP_URL = "http://www.sb.bostream.se/lgh7339701/projects/mp3albummanager.html";
    /**
     * Constant for the All Albums item in the albums list.
     */
    private static final String ALL_ALBUMS_ITEM = "-> ALL Albums";
    private static final String SEARCH_RESULTS_ITEM = "-> SEARCH Results";
    private boolean searchForSongs;
    private final View view;
    private final Model model;
    private boolean anyRecent;
    private final boolean verbose;
    private boolean showingClose, showingNew, showingOpen, showingInfo, showingList, showingDetails, showingSearch;
    private StartupPanel startupPanel;
    private OpenDBPanel openPanel;
    private CreateDBPanel createPanel;
    private InfoDBPanel infoPanel;
    private ListPanel listPanel;
    private String selectedSongFilename, selectedAlbumDirectory, selAlbumTitle;
    private DetailsPanel detailsPanel;
    private SearchPanel searchPanel;
    private AboutPanel aboutPanel;

    private String currAlbumQuery, currSongQuery, currSearchQuery;
    private ArrayList<Object> currPrepVals;

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
                if(showingSearch) { view.remove(searchPanel); showingSearch = false; }

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
                if(showingDetails) { view.remove(detailsPanel); showingDetails = false; }

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
                if(showingDetails) { view.remove(detailsPanel); showingDetails = false; }
                if(showingSearch) { view.remove(searchPanel); showingSearch = false; }

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
            selAlbumTitle = (String) listPanel.albumList.getSelectedValue();
            // get the nfo content
            StringBuffer content = model.getNfoContent(selAlbumTitle);

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
                //TODO: NFO content should be fetched from preview
                //if(saveNfo != null) model.writeFile(content, saveNfo);
                if(saveNfo != null) model.writeFile(view.getTextAreaContent(), saveNfo);
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


            // remove the ALL ALBUMS item in the album list
            List<String> albumList = new ArrayList<String>( Arrays.asList(albums) );
            albumList.removeAll( Arrays.asList(ALL_ALBUMS_ITEM) );
            albums = albumList.toArray(new String[0]);

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
                    browser.launchFile();
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
        } else if( actionCommand.equals("search") ) {

            // remove other panels
            if(showingClose) { view.remove(startupPanel); showingClose = false; }
            if(showingNew) { view.remove(createPanel); showingNew = false; }
            if(showingInfo) { view.remove(infoPanel); showingInfo = false; }
            if(showingList) { view.remove(listPanel); showingList = false; }
            if(showingDetails) { view.remove(detailsPanel); showingDetails = false; }

            // hide all other commands (except close)
            view.initializeMenu( false );
            view.enableMenuItem(view.menuItemClose);
            view.enableButton(view.closeButton);

            searchPanel = new SearchPanel(bgcolor);
            searchPanel.setSearchButtonListener( new SearchPanelListener() );

            searchPanel.showPanel();
            view.addPanel(searchPanel);

            showingSearch = true;

        } else if( actionCommand.equals("about") ) {
            if(verbose) System.out.println("ABOUT:\n\t" + APP_NAME + " " + APP_VERSION + "\n\t" + APP_URL);
            aboutPanel = new AboutPanel(bgcolor, APP_NAME, APP_VERSION, APP_URL);
            JOptionPane.showMessageDialog(null, aboutPanel, "About", JOptionPane.PLAIN_MESSAGE);
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
                    openPanel.setMessageLabel("Problem connecting to database. Please try again.");
                } else {
                    showDBInfoPanel(mp3Dir);
                }

            } else if( actionCommand.equals("createDB") ) {
                // get field values for name, directory, username & password
                // check if any field is empty
                String dbName = createPanel.getCollectionField();
                // check if the dbName is already used
                List<String> dbNames = Arrays.asList( model.getNamesFromEntries() );
                if( dbNames.contains(dbName)) { createPanel.setMessageLabel("The database name is already in use. Select another name."); return; }
                if( dbName.isEmpty() ) { createPanel.setMessageLabel("Please fill in a database name"); return; }
                String user = createPanel.getUserField();
                if( user.isEmpty() ) { createPanel.setMessageLabel("Please fill in a username"); return; }
                String pass = createPanel.getPassField();
                if( pass.isEmpty() ) { createPanel.setMessageLabel("Please fill in a password"); return; }
                String mp3Dir = createPanel.getMusicField();
                if( mp3Dir.isEmpty() || ! mp3Dir.startsWith("/")) { createPanel.setMessageLabel("Please select album directory"); return; }
                String dbDir = createPanel.getDBDirField();
                if( dbDir.isEmpty() || ! dbDir.startsWith("/")) { createPanel.setMessageLabel("Please select database directory"); return; }

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
                    // continue to database infoPanel
                    showDBInfoPanel(mp3Dir);
                } else {
                    if(verbose) System.err.println("ERROR: initialising db directory");
                    createPanel.setMessageLabel("Collection already exists!");
                }



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

            // show update button if nrOfmp3s > 0 and nrOfmp3s != nrOfsongs
            if( model.getNrOfMp3s() > 0 && model.getNrOfMp3s() != nrOfSongsInDB) {
                infoPanel.showButton(infoPanel.updateButton, true);
            }

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
                currAlbumQuery = "SELECT title FROM Album ORDER BY title";
                currSongQuery = "SELECT title FROM Song ORDER BY title";
                model.setListContent(listPanel, currAlbumQuery, currSongQuery);

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

            } else if( actionCommand.equals("remove") ) {
                // show confirmation dialog
                int option = JOptionPane.showConfirmDialog(null, "Are you sure you want to remove this collection?", "Remove collection?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if(option == 0) {
                    // remove the line in the recent file
                    String dbDir = model.getCurrentEntry().getDBDir();
                    String dbName = model.getCurrentEntry().getDBName();
                    String path = dbDir + "/." + dbName;
                    File recent = model.getRecentFile();
                    if(verbose) System.out.println("Checking path:\n\t" + path);
                    boolean found = model.getDAO().foundInRecent(recent, path);
                    if(found) {
                        model.getDAO().removeFromRecent(recent.getAbsolutePath(), path);
                        // log out
                        model.getDAO().disconnect();
                        // remove the directory
                        boolean deleted = model.deleteDir( new File(path) );
                        if(deleted) {
                            // show dialog? (collection removed successfully)
                            JOptionPane.showMessageDialog(null, "Collection removed successfully.");
                            // remove the db entry and set current entry to empty string
                            model.removeEntry(dbName);
                            model.setCurrentEntry("");
                        } else {
                            if(verbose) System.out.println("Error: Failed to delete dir:\n\t" + path);
                        }

                    } else {
                        if(verbose) System.out.println("Error: Collection path not found:\n\t" + path);
                    }
                    // go back to startup (whether successfully removed collection or not)
                    //TODO: move this to separate method (is exactly the same as 'close'
                    // remove other panels
                    if(showingOpen) { view.remove(openPanel); showingOpen = false; }
                    if(showingNew) { view.remove(createPanel); showingNew = false; }
                    if(showingInfo) { view.remove(infoPanel); showingInfo = false; }
                    if(showingList) { view.remove(listPanel); showingList = false; }
                    if(showingDetails) { view.remove(detailsPanel); showingDetails = false; }
                    if(showingSearch) { view.remove(searchPanel); showingSearch = false; }

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
        }

        /**
         * Listener for the progress bar.
         * @param evt the change event. 
         */
        public void propertyChange(PropertyChangeEvent evt) {

            if( evt.getPropertyName().equals("progress") ) {

                int progress = (Integer) evt.getNewValue();
                infoPanel.progressBar.setValue(progress);
            }
        }

    }

    /**
     * Inner class Task is a sub-class of SwingWorker.
     * The task instance invokes the doInBackground in a separate thread.
     */
    private class Task extends SwingWorker<Void, Void> {

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

                listPanel.setMessageLabel("");
                int selectedIndex;
                JList list = (JList) e.getSource();

                if( list.getName().equals("albums") ) {

                    selectedIndex = listPanel.albumList.getSelectedIndex();

                    if(selectedIndex == -1) {
                        // no selection

                    } else {
                        //------ an album is selected --------
                        selAlbumTitle = (String) listPanel.albumList.getSelectedValue();
                        if(verbose) System.out.println("\nSelected an album: " + selAlbumTitle);

                        // empty all fields in details panel
                        detailsPanel.resetAllFields();

                        //------ ALL ALBUMS or SEARCH RESULTS is selected --------

                        if( selAlbumTitle.equals(ALL_ALBUMS_ITEM) | selAlbumTitle.equals(SEARCH_RESULTS_ITEM) ) {
                            // disable edit, delete, NFO in menu
                            view.disableMenuItem(view.menuItemEdit);
                            view.disableButton(view.editButton);
                            view.disableMenuItem(view.menuItemDelete);
                            view.disableButton(view.deleteButton);
                            view.disableMenuItem(view.menuItemNFO);
                            view.disableButton(view.nfoButton);

                            // empty the lists
                            listPanel.songListModel.removeAllElements();
                            listPanel.albumListModel.removeAllElements();
                            // add "ALL ALBUMS" & "SEARCH RESULTS" to the top of the albumlist in the listpanel
                            listPanel.albumListModel.addElement( ALL_ALBUMS_ITEM );
                            if(currSearchQuery != null) listPanel.albumListModel.addElement( SEARCH_RESULTS_ITEM );

                            //------ SEARCH RESULTS is selected --------
                            if( selAlbumTitle.equals(SEARCH_RESULTS_ITEM) ) {

                                DefaultListModel listModel = (searchForSongs) ? listPanel.songListModel : listPanel.albumListModel;
                                Set albumList = model.setListContent(listModel, currSearchQuery, currPrepVals, searchForSongs);
                                if(searchForSongs) {
                                    for(Object o: albumList) listPanel.albumListModel.addElement(o);
                                }
                                listPanel.setMessageLabel( searchPanel.getHumanReadableSearch() );
                            } else {
                                // show all albums & songs in lists
                                String albumsQuery = "SELECT * FROM Album ORDER BY title";
                                String songsQuery = "SELECT * FROM Song ORDER BY title";
                                model.setListContent(listPanel, albumsQuery, songsQuery);
                            }
                            return;

                        }


                        //------ an single album is selected --------

                        // enable edit, delete, NFO in menu
                        view.enableMenuItem(view.menuItemEdit);
                        view.enableButton(view.editButton);
                        view.enableMenuItem(view.menuItemDelete);
                        view.enableButton(view.deleteButton);
                        view.enableMenuItem(view.menuItemNFO);
                        view.enableButton(view.nfoButton);

                        // get the album properties for the selected album
                        Album selAlbum = null;
                        String prepSelect = "SELECT * FROM Album WHERE title=?";
                        try {
                            PreparedStatement stmt = model.getDAO().getConnection().prepareStatement(prepSelect);
                            stmt.setString(1, selAlbumTitle);
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
                                // set the selected album (to be used for edit command)
                                selectedAlbumDirectory = albumDirectory;
                            }
                        } catch(SQLException e2) { e2.printStackTrace(); }

                        // empty the songlist
                        listPanel.songListModel.removeAllElements();
                        // get the song properties to fill the keys of the album maps
                        Song albumSong;
                        String prepSonglist = "SELECT * FROM Song WHERE album=? ORDER BY title";
                        try {
                            PreparedStatement stmtSonglist = model.getDAO().getConnection().prepareStatement(prepSonglist);
                            stmtSonglist.setString(1, selAlbumTitle);
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

                        //------ a song is selected --------

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

                        // get the song properties
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
                                // set the selected song (to be used by edit command)
                                selectedSongFilename = rs.getString("filename");
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
                // make fields editable and show buttons
                detailsPanel.setEditableFields(false);
                detailsPanel.showEditButtons(false);
                detailsPanel.showComboBoxes(false);
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

            } else if( actionCommand.equals("updateEdit") ) {

                // read properties from textfields
                String artist = detailsPanel.getArtistTextField();
                String bitrateString = detailsPanel.getBitrateTextField();
                Integer bitrate = null;
                if( bitrateString != null || bitrateString.isEmpty() ) {
                    bitrate = Integer.parseInt(bitrateString);
                }
                String freqString = detailsPanel.getFrequencyTextField();
                Integer frequency = null;
                if( freqString != null || freqString.isEmpty() ) {
                    frequency = Integer.parseInt(freqString);
                }
                String genre = model.getGenreNr( detailsPanel.getGenreTextField() );
                String lame = detailsPanel.getLameTextField();
                String mode = detailsPanel.getModeTextField();
                String tag = detailsPanel.getTagTextField();
                String title = detailsPanel.getTitleTextField();
                String track = detailsPanel.getTrackTextField();
                Boolean various = detailsPanel.getVarious();
                Boolean vbr = detailsPanel.getVbr();
                String year = detailsPanel.getYearTextField();

                // get selected song or album and construct query
                String updateQuery;
                PreparedStatement prepStmt;
                boolean success = false;
                String type = "";
                if( listPanel.songList.getSelectedIndex() == -1 ) {
                    // an album is selected
                    if( listPanel.albumList.getSelectedIndex() == -1 ) {
                        // nothing is selected (this should not be happening)
                    } else if( listPanel.albumList.getSelectedIndex() == 0 ) {
                        // ALL ALBUMS is selected
                    } else {
                        // a single album is selected
                        if(verbose) System.out.println("Edit album: " + selectedAlbumDirectory);
                        try {
                            // execute the select song query
                            // select songs with matching album title
                            String selectMatchingSongs = "SELECT filename FROM Song WHERE album=?";
                            prepStmt = model.getDAO().getConnection().prepareStatement(selectMatchingSongs);
                            prepStmt.setString(1, selAlbumTitle);
                            ResultSet rs = model.getDAO().executePreparedStmt(prepStmt);
                            //prepStmt.close();
                            ArrayList<String> albumSongs = new ArrayList<String>();
                            while( rs.next() ) { albumSongs.add( rs.getString("filename") ); }
                            rs.close();

                            String updateMatchingSongs;
                            PreparedStatement stmt;
                            for(String filename : albumSongs) {
                                // update album title for these songs
                                updateMatchingSongs = "UPDATE Song SET album=? WHERE filename=?";
                                stmt = model.getDAO().getConnection().prepareStatement(updateMatchingSongs);
                                stmt.setString(1, title);
                                stmt.setString(2, filename);
                                model.getDAO().insertValues(stmt);
                            }
                            // query to update album
                            updateQuery = "UPDATE Album SET artist=?, bitrate=?, frequency=?, genre=?, lame=?, mode=?, tag=?, title=?, various=?, vbr=?, albumyear=? " +
                                    "WHERE directory=?";
                            // execute query to update album
                            prepStmt = model.getDAO().getConnection().prepareStatement(updateQuery);
                            prepStmt.setString(1, artist);
                            prepStmt.setInt(2, bitrate);
                            prepStmt.setInt(3, frequency);
                            prepStmt.setString(4, genre);
                            prepStmt.setString(5, lame);
                            prepStmt.setString(6, mode);
                            prepStmt.setString(7, tag);
                            prepStmt.setString(8, title);
                            prepStmt.setBoolean(9, various);
                            prepStmt.setBoolean(10, vbr);
                            prepStmt.setString(11, year);
                            prepStmt.setString(12, selectedAlbumDirectory);
                            success = model.getDAO().insertValues(prepStmt);
                            type = "Album";
                            prepStmt.close();
                        } catch (SQLException e) { e.printStackTrace(); }
                    }
                } else {
                    // a song is selected

                    if(verbose) System.out.println("Edit song: " + selectedSongFilename);
                    updateQuery = "UPDATE Song SET artist=?, bitrate=?, frequency=?, genre=?, lame=?, mode=?, tag=?, title=?, track=?, vbr=?, songyear=? " +
                            "WHERE filename=?";
                    try {
                        prepStmt = model.getDAO().getConnection().prepareStatement(updateQuery);
                        prepStmt.setString(1, artist);
                        prepStmt.setInt(2, bitrate);
                        prepStmt.setInt(3, frequency);
                        prepStmt.setString(4, genre);
                        prepStmt.setString(5, lame);
                        prepStmt.setString(6, mode);
                        prepStmt.setString(7, tag);
                        prepStmt.setString(8, title);
                        prepStmt.setString(9, track);
                        prepStmt.setBoolean(10, vbr);
                        prepStmt.setString(11, year);
                        prepStmt.setString(12, selectedSongFilename);

                        success = model.getDAO().insertValues(prepStmt);
                        type = "Song";
                        prepStmt.close();
                    } catch (SQLException e) { e.printStackTrace(); }

                }
                // show some confirmation (JOptionPane ?)
                String msg = (success) ? "Successfully updated " + type : "Failed to update " + type;
                JOptionPane.showMessageDialog(null, msg, "Edit", JOptionPane.INFORMATION_MESSAGE);

                // set fields to not editable and hide the edit buttons (like cancelEdit above)
                // make fields editable and show buttons
                detailsPanel.setEditableFields(false);
                detailsPanel.showEditButtons(false);
                detailsPanel.showComboBoxes(false);

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

                //update albumlist
                listPanel.albumListModel.removeAllElements();
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

                // update songlist
                listPanel.songListModel.removeAllElements();
                String songsQuery = "SELECT title FROM Song ORDER BY title";
                rs = model.getDAO().doSelectQuery(songsQuery, model.getDAO().getConnection(), false);
                try {
                    while(rs.next()) {
                        listPanel.songListModel.addElement( rs.getString("title") );
                    }
                } catch(SQLException e) { e.printStackTrace(); }



            }
        }
    }

    /**
     * Listener inner class for searchButton in searchPanel.
     */
    private class SearchPanelListener implements ActionListener {

        public void actionPerformed(ActionEvent ae) {

            String actionCommand=ae.getActionCommand();

            if( actionCommand.equals("search") ) {
                searchForSongs = false;
                if( searchPanel.doBeforeSearch() ) {
                    if(verbose) {
                        System.out.println( "Search query: " + searchPanel.getQuery() );
                        System.out.print("Search values: ");
                        ArrayList<Object> prepVals = searchPanel.getPrepVals();
                        for(int i = 0; i < prepVals.size(); i++) {
                            System.out.print(i+1 + "=" + prepVals.get(i) + " ");
                        }
                        System.out.println("\n");
                    }
                    // remove other panels
                    if(showingClose) { view.remove(startupPanel); showingClose = false; }
                    if(showingOpen) { view.remove(openPanel); showingOpen = false; }
                    if(showingNew) { view.remove(createPanel); showingNew = false; }
                    if(showingInfo) { view.remove(infoPanel); showingInfo = false; }
                    if(showingDetails) { view.remove(detailsPanel); showingDetails = false; }
                    if(showingList) { view.remove(listPanel); showingList = false; }
                    if(showingSearch) { view.remove(searchPanel); showingSearch = false; }

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

                    // show the listPanel
                    listPanel = new ListPanel(bgcolor);
                    listPanel.selectionPanelListener( new ListPanelListener() );

                    // add "ALL ALBUMS" & "SEARCH RESULTS" to the top of the albumlist in the listpanel
                    listPanel.albumListModel.addElement( ALL_ALBUMS_ITEM );
                    listPanel.albumListModel.addElement( SEARCH_RESULTS_ITEM );

                    //update the listPanel
                    DefaultListModel listModel;
                    searchForSongs = searchPanel.getCurrentSelect().equals("Song");
                    listModel = (searchForSongs) ? listPanel.songListModel : listPanel.albumListModel;
                    currSearchQuery = searchPanel.getQuery();
                    currPrepVals = searchPanel.getPrepVals();
                    Set albumList = model.setListContent(listModel, currSearchQuery, currPrepVals, searchForSongs);
                    if(searchForSongs) {
                        for(Object o: albumList) listPanel.albumListModel.addElement(o);
                    }
                    listPanel.setMessageLabel( searchPanel.getHumanReadableSearch() );
                    // show the listPanel
                    listPanel.showPanel();
                    view.addPanel(listPanel, BorderLayout.WEST);

                    //add the detailsPanel (fields empty until album or song is selected)
                    detailsPanel = new DetailsPanel(bgcolor);
                    detailsPanel.setDetailsButtonListener( new DetailsButtonListener() );
                    detailsPanel.showPanel();
                    view.addPanel(detailsPanel, BorderLayout.CENTER);
                    showingDetails = true;
                    showingList = true;

                } else {
                    System.out.println("Some fields are empty, will NOT continue to search results");
                }

            }
        }
    }

}