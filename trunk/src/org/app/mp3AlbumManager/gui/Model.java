package org.app.mp3AlbumManager.gui;

import org.app.mp3AlbumManager.db.DBEntry;
import org.app.mp3AlbumManager.db.AlbumDAO;
import org.app.mp3AlbumManager.util.TemplateWriter;

import javax.swing.*;
import java.util.*;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

public class Model {
    /**
     * The path to the nfo template file.
     */
    private static final String NFO_TEMPLATE = "/config/template.nfo";
    /**
     * The path to the html template file.
     */
    private static final String HTML_TEMPLATE = "/config/template.html";
    /**
     * The list of the genre names (149 genres).
     */
    private final String[] genres = { "Blues", "Classic Rock", "Country", "Dance", "Disco", "Funk", "Grunge", "Hip-Hop", "Jazz",
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
    /**
     * The application directory.
     * Is a sub directory of the user home directory, set in the configuration properties.
     */
    private static final String APPLICATION_DIRECTORY = "/.mp3MusicManager";

    private final boolean verbose;

    private final Set<DBEntry> entries;
    private DBEntry currentEntry;

    private AlbumDAO dao;
    Connection conn;

    private final Set<String[]> mp3entries;
    private final List<String[]> sortedMp3entries;
    private final Set<String> albumEntries;

    private String selectedSongFilename, selectedAlbumDirectory, selectedAlbumTitle;

    public Model(boolean verbose) {

        this.verbose = verbose;
        entries = new TreeSet<DBEntry>();

        mp3entries = new HashSet<String[]>();
        sortedMp3entries = new ArrayList<String[]>();
        albumEntries = new HashSet<String>();

    }

    //############ DBEntry methods #################
    /**
     * Checks the recent file for any earlier entries in the database.
     * @return Whether found any recent entries.
     */
    public boolean getRecentEntries() {

        boolean ret = false;
        if(verbose) System.out.println("\nCHECKING RECENT ENTRIES:");
        File recentFile = getRecentFile();

        FileInputStream fis = null;
        BufferedReader br = null;
        Properties props = new Properties();
        String path;
        File propFile;

        try {
            br = new BufferedReader( new FileReader(recentFile) );
            // if file is empty
            if( br.read() == -1) {
                if(verbose) { System.out.println("Recent file is empty!"); }
                br.close();
                return false;
            }
            String propPath, db, dir, mp3dir;
            DBEntry entry;
            // read the rows in the recent file
            while ((path = br.readLine()) != null) {
                // NOTE: readLine() seems to skip the first forward slash, so add it:
                propPath = "/" + path + "/" + AlbumDAO.DB_PROPERTIES_FILE;
                propFile = new File(propPath);
                fis = new FileInputStream(propFile);
                props.load(fis);
                db = props.getProperty("database.name");
                dir = props.getProperty("database.home");
                mp3dir = props.getProperty("mp3.dir");
                entry = new DBEntry(db, dir, mp3dir);
                addNewEntry(entry);
                //if we've come this far we can set return value to true, ie found valid entries
                if(verbose) { System.out.println("Found recent entry:\n\t" + path); }
                ret = true;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if(fis != null) fis.close();
                if(br != null) br.close();
            } catch (IOException e) { e.printStackTrace(); }
        }
        return ret;
    }

    /**
     * Get the recent file.
     * By default the recent file is called 'recent' and is found in the application directory.
     * @return The recent file.
     */
    public File getRecentFile() {

        File appDir = new File(System.getProperty("user.home") + APPLICATION_DIRECTORY);

        File recentFile = new File(appDir + "/recent");
        if( ! recentFile.isFile() ) {
            try {
                if( recentFile.createNewFile() ) {
                    if(verbose) { System.out.println("Successfully created recent file:\n\t" + recentFile); }
                }
            } catch(IOException e) { e.printStackTrace(); }
        } else {
            if(verbose) { System.out.println("Recent file already exists:\n\t" + recentFile); }
        }
        return recentFile;
    }

    /**
     * Remove a database entry.
     * @param name the name of the entry to remove.
     * @return whether successfully removed entry.
     */
    public boolean removeEntry(String name) {
        boolean ret = false;
        for(DBEntry e :entries) {
            if( e.getDBName().equals(name) ) { ret = entries.remove(e); }
        }
        return ret;
    }

    /**
     * Set the current database entry with the supplied database entry name..
     * @param name The name of the database entry.
     * @return If the entry was found in the set of database entries..
     */
    public boolean setCurrentEntry(String name) {

        for(DBEntry e :entries) {
            if( e.getDBName().equals(name) ) {
                currentEntry = e;
                return true;
            }
        }
        return false;
    }

    /**
     * Get the current database entry.
     * @return The current database entry.
     */
    public DBEntry getCurrentEntry() { return currentEntry; }

    /**
     * Add a new database entry to the set of database entries.
     * @param e The database entry.
     */
    public void addNewEntry(DBEntry e) { entries.add(e); }

    /**
     * Get the number of entries in the set of database entries.
     * @return The number of entries.
     */
    public int getNrOfEntries() { return entries.size(); }

    /**
     * Get the database names in the set of database entries.
     * @return The database names.
     */
    public String[] getNamesFromEntries() {
        String[] ret = new String[getNrOfEntries()];
        int i = 0;
        for(DBEntry e : entries) {
            ret[i] = e.getDBName();
            i++;
        }
        return ret;
    }

    /**
     * Set the data access object by creating a new instance of AlbumDAO.
     * @see AlbumDAO
     *
     * @param create Whether to create a new database entry.
     */
    public void setDAO(boolean create) { dao = new AlbumDAO(getCurrentEntry(), create, verbose); }

    /**
     * Get the data access object.
     * @return The data access object.
     */
    public AlbumDAO getDAO() { return dao; }

    /**
     * Initialize the database.
     * @return Whether initializing was successful.
     */
    public boolean initDB() { return getDAO().initDB(); }

    /**
     * Clear the set of mp3 entries.
     */
    public void resetMp3Entries() { mp3entries.clear(); }

    /**
     * Add an entry to the set of mp3 entries.
     * @param dir the entry to add.
     */
    public void addMp3Files(File dir) {

        Mp3Filter filter = new Mp3Filter();
        if( dir.isDirectory() ) {
            File[] children = dir.listFiles(filter);
            for (File aChildren : children) {
                addMp3Files( new File( dir, aChildren.getName() ) );
            }
        } else if ( dir.isFile() ){
            String filename, subdir = "", album;
            // get the filename
            filename = dir.getName();
            // get the parent dirname
            File parentDir = new File(dir.getParent());
            String parentDirname = parentDir.getName();
            // check if parent dirname is a subdir (ie dirname starts with 'cd')
            if(parentDirname.toLowerCase().startsWith("cd")) {
                // if subdir, set album name to parent dirname of the subdir
                album = parentDir.getParentFile().getName();
                // set subdir name
                subdir = parentDirname;
            } else {
                // this is not a subdir, set album name to dirname (and subdir to null)
                album = parentDirname;
            }
            // create an array with filename, subdir and album name
            String[] mp3entry = new String[]{filename, subdir, album};
            // and add the array to a set
            mp3entries.add(mp3entry);
        }
    }

    /**
     * Get the number of entries in the set of mp3 entries.
     * @return the number of entries.
     */
    public int getNrOfMp3s() { return mp3entries.size(); }

    /**
     * Get the set of mp3 entries.
     * @return The set of mp3 entries.
     */
    public Set<String[]> getMp3entries() { return mp3entries; }

    /**
     * Get a sorted list of mp3 entries.
     * @return the sorted list of mp3 entries.
     */
    public List<String[]> getSortedMp3entries() {
        sortedMp3entries.addAll(mp3entries);
        Collections.sort(sortedMp3entries, new ListSorter() );
        return sortedMp3entries;
    }

    /**
     * Get number of entries in the sorted list of mp3 entries.
     * @return the number of entries.
     */
    public int getNrOfAlbums() {
        for(String[] dirname : getSortedMp3entries() ) {
            albumEntries.add(dirname[2]);
        }
        return albumEntries.size();
    }


    //############ Album / Song methods #################
    /**
     * Get the length formatted as hh:mm:ss
     * @param timeInSeconds The time in seconds.
     * @return The formatted time.
     */
    public String getLengthAsString(int timeInSeconds) {

        int remdr, hours, mins, secs;
        hours =  timeInSeconds / (60 * 60);
        remdr = timeInSeconds % (60 * 60);
        mins  = ( remdr / 60 );
        remdr %= (60);
        secs = remdr;
        // only return hours if hours > 0
        return (hours > 0) ? String.format("%1$02d:%2$02d:%3$02d", hours, mins, secs) : String.format("%1$02d:%2$02d", mins, secs);
    }

    /**
     * Get the genre name from a genre number.
     * Return the genre number if it fails.
     * @param genrenr the genre number.
     * @return the genre name.
     */
    public String getGenreName(String genrenr) {

        try {
            int nr = Integer.parseInt(genrenr);
            if(nr < 0 || nr > 149) {
                if(verbose) System.out.println("\nFailed to parse genre as a number: " + genrenr);
                return genrenr;
            } else {
                return genres[nr];
            }
        } catch (NumberFormatException nfe) {
            if(verbose) System.out.println("\nFailed to parse genre as a number: " + genrenr);
            return genrenr;
        }
    }

    /**
     * Get the genre number from a genre name.
     * Return the genre name if it fails.
     * @param genrename the genre name.
     * @return the genre number.
     */
    public String getGenreNr(String genrename) {

        try {
            int nr = Integer.parseInt(genrename);
            if(nr < 0 || nr > 149) {
                if(verbose) System.out.println("\nFailed to parse genre as a number: " + genrename);
                return genrename;
            } else {
                List<String> genrelist = Arrays.asList(genres);
                if( genrelist.contains(genrename) ) {
                    return "" + genrelist.indexOf(genrename);
                } else {
                    return genrename;
                }

            }
        } catch (NumberFormatException nfe) {
            if(verbose) System.out.println("\nFailed to parse genre as a number: " + genrename);
            return genrename;
        }
    }

    //############ TemplateWriter methods #################
    /**
     * Get the html content for the Save as HTML command.
     * @param musicDir The album directory.
     * @param searchQuery The search query (if any).
     * @param queries The queries to the database, to get the replace patterns to generate the nfo content.
     * @param albumTitles The album titles.
     * @return The html content.
     */
    public StringBuffer getHtmlContent(String musicDir, String searchQuery, String[] albumTitles) {

        int nrOfTitles = albumTitles.length;

        // generate prepared statements
        // not string queries, to avoid problems with special characters in titles
        String prepSelect = "SELECT directory, artist, title FROM Album WHERE title=?";

        // CONNECTION -> SELECT ALBUM
        // store the link lines in an array to sort them later
        String[] links = new String[nrOfTitles];
        for(int i = 0; i < nrOfTitles; i++)  {
            try {
                PreparedStatement stmt = getDAO().getConnection().prepareStatement(prepSelect);
                stmt.setString(1, albumTitles[i]);
                ResultSet rs = getDAO().executePreparedStmt(stmt);
                while( rs.next() ) {
                    String directory = rs.getString("directory");
                    String artist = rs.getString("artist");
                    String title = rs.getString("title");
                    links[i] = "<p><a href='" + directory + "'>" + artist + " - " + title + "</a></p>\n";
                }
            } catch(SQLException e) { e.printStackTrace(); }
        }
        // sort the array of links
        Arrays.sort(links);
        // output the links to a string buffer
        StringBuffer buf = new StringBuffer();
        for(String line : links) { buf.append(line); }

        String[] oldPatterns = new String[] { "#music_dir#", "#search_query#", "#html_list#" };
        String[] replPatterns = new String[] { musicDir, searchQuery, buf.toString() };

        // send properties to template writer and get the nfo content
        TemplateWriter template = new TemplateWriter();
        return template.readReplace(HTML_TEMPLATE, oldPatterns, replPatterns);
    }

    /**
     * Get the nfo content for the Save as NFO command.
     * @param albumTitle The query to the database, to get the replace patterns to generate the html content.
     * @return The nfo content.
     */
    public StringBuffer getNfoContent(String albumTitle) {

        TemplateWriter template = new TemplateWriter();

        String[] oldPatterns = new String[] {
                "#get_artist#",
                "#get_album#",
                "#get_year#",
                "#get_genre#",
                "#get_tagversion#",
                "#get_lame#",
                "#get_bitrate#",
                "#get_samplerate#",
                "#get_mode#",
                "#get_tracklist#",
                "#get_totlength#"
        };
        // CONNECTION -> SELECT ALBUM
        String prepSelect =  "SELECT * FROM Album WHERE title=?";

        String[] replPatterns = null;
        try {
            PreparedStatement stmt = getDAO().getConnection().prepareStatement(prepSelect);
            stmt.setString(1, albumTitle);
            ResultSet rs = getDAO().executePreparedStmt(stmt);
            while( rs.next() ) {
                String bitratePrefix = ( rs.getBoolean("vbr") ) ? "VBR ~" : "CBR ";
                String bitrate = ( rs.getInt("bitrate") != 0 ) ? bitratePrefix + rs.getInt("bitrate") + " kbps" : "";
                String artist = ( rs.getString("artist") != null) ? rs.getString("artist") : "";
                String title = ( rs.getString("title") != null) ? rs.getString("title") : "";
                String albumyear = ( rs.getString("albumyear") != null) ? rs.getString("albumyear") : "";
                String genre = parseGenre( rs.getString("genre") );
                String tag = ( rs.getString("tag") != null) ? rs.getString("tag") : "";
                String lame = ( rs.getString("lame") != null ) ? rs.getString("lame") : "";
                String frequency = ( rs.getInt("frequency") != 0 ) ? rs.getInt("frequency") + " Hz" : "";
                String mode = ( rs.getString("mode") != null) ? rs.getString("mode") : "";
                Boolean various = rs.getBoolean("various");
                String subdirs = rs.getString("subdirs");
                String tracklist = getTracklisting(albumTitle, various, subdirs);
                int length = rs.getInt("albumlength");
                String albumlength = ( length != 0) ? getLengthAsString(length) : "";
                replPatterns = new String[] {
                        artist,
                        title,
                        albumyear,
                        genre,
                        tag,
                        lame,
                        bitrate,
                        frequency,
                        mode,
                        tracklist,
                        albumlength
                };
            }
        } catch(SQLException e) { e.printStackTrace(); }

        // send properties to template writer and get the nfo content
        return template.readReplace(NFO_TEMPLATE, oldPatterns, replPatterns);
    }

    /**
     * Generate the track listing.
     * <br />
     * Get song track, title, artist, length from database and construct the track listing lines.
     * Add artist to the lines if it's a various artists album.
     * Add subdir if it's a multiple cd album.
     * Right align the song length info at each line.
     * @param albumTitle the album title.
     * @param various whether a various artists album.
     * @param subdirs sub directories of the album.
     * @return the track listing.
     */
    public String getTracklisting(String albumTitle, Boolean various, String albumSubdirs) {

        // split album subdirs (if any) into an array
        String[] subdirs = null;
        if (albumSubdirs != null){
            if( ! albumSubdirs.isEmpty() ) subdirs = albumSubdirs.split("\\, ");
        }

        ArrayList<String> trackTitlelines = new ArrayList<String>();
        ArrayList<String> lengthlines = new ArrayList<String>();
        StringBuffer ret = new StringBuffer();

        // get the max length of 'track ( - artist) - title' line
        int maxlength = calculateLineLength(albumTitle, various);

        // no subdirs
        if(subdirs == null) {
            String prepSelect =  "SELECT * FROM Song WHERE album=?";
            try {
                PreparedStatement stmt = getDAO().getConnection().prepareStatement(prepSelect);
                stmt.setString(1, albumTitle);
                ResultSet rs = getDAO().executePreparedStmt(stmt);
                while( rs.next() ) {
                    StringBuffer tracklist = new StringBuffer();
                    tracklist.append( rs.getString("track") ).append(" - ");
                    if(various) { tracklist.append( rs.getString("artist") ).append(" - "); }
                    tracklist.append( rs.getString("title") );
                    // store track + (artist) + title in arraylist
                    trackTitlelines.add( tracklist.toString() );
                    // store length in a separate arraylist, to format right alignment
                    lengthlines.add( " (" + getLengthAsString( rs.getInt("songlength") ) + ")\n");
                }
                ret = buildTracklisting(trackTitlelines, lengthlines, maxlength);
            } catch(SQLException e) { e.printStackTrace(); }
        // subdirs
        } else {
            // iterate over subdirs
            for(String s : subdirs) {
                String prepSelect =  "SELECT * FROM Song WHERE album=? AND subdir=?";
                try {
                    PreparedStatement stmt = getDAO().getConnection().prepareStatement(prepSelect);
                    stmt.setString(1, albumTitle);
                    stmt.setString(2, s);
                    ResultSet rs = getDAO().executePreparedStmt(stmt);
                    StringBuffer listing = new StringBuffer();
                    trackTitlelines.clear();
                    lengthlines.clear();
                    // add the subdir header
                    listing.append(s).append("\n");
                    while( rs.next() ) {
                        StringBuffer tracklist = new StringBuffer();
                        tracklist.append( rs.getString("track") ).append(" - ");
                        if(various) { tracklist.append( rs.getString("artist") ).append(" - "); }
                        tracklist.append( rs.getString("title") );
                        // store track + (artist) + title in arraylist
                        trackTitlelines.add( tracklist.toString() );
                        // store length in a separate arraylist, to format right alignment
                        lengthlines.add( " (" + getLengthAsString( rs.getInt("songlength") ) + ")\n");
                    }
                    // add the subdir track list
                    listing.append( buildTracklisting(trackTitlelines, lengthlines, maxlength) );
                    listing.append("\n");
                    // add the list to the album tracklisting
                    ret.append( listing.toString() );
                } catch(SQLException e) { e.printStackTrace(); }
            }
        }
        
        return ret.toString();
    }

    /**
     * Calculate the longest line length of 'track - title' or if various artists 'track - artist - title'.
     * @param albumTitle the title of the album.
     * @param various whether various artists album.
     * @return the longest line length.
     */
    public int calculateLineLength(String albumTitle, Boolean various) {
        int maxlength = 0;
        String prepSelect =  "SELECT * FROM Song WHERE album=?";
        try {
            PreparedStatement stmt = getDAO().getConnection().prepareStatement(prepSelect);
            stmt.setString(1, albumTitle);
            ResultSet rs = getDAO().executePreparedStmt(stmt);
            while( rs.next() ) {
                StringBuffer tracklist = new StringBuffer();
                tracklist.append( rs.getString("track") ).append(" - ");
                if(various) { tracklist.append( rs.getString("artist") ).append(" - "); }
                tracklist.append( rs.getString("title") );
                // keep track of the longest trackTitle line
                int linelength = tracklist.toString().length();
                if(linelength > maxlength) maxlength = linelength;
            }

        } catch(SQLException e) { e.printStackTrace(); }

        return maxlength;
    }

    /**
     * Build the track listing.
     * @param trackTitlelines ArrayList of track/title lines.
     * @param lengthlines ArrayList of song length lines.
     * @param maxlength the longest track/title line length.
     * @return the track listing.
     */
    public StringBuffer buildTracklisting(ArrayList<String> trackTitlelines, ArrayList<String> lengthlines, int maxlength) {
        StringBuffer ret = new StringBuffer();
        // iterate over trackTitle lines and append spaces to the length of the longest line (maxlength)
        for(int i = 0; i < trackTitlelines.size(); i++) {
            int spaces = maxlength - trackTitlelines.get(i).length();
            if(spaces > 0) {
                StringBuilder build = new StringBuilder( maxlength + trackTitlelines.get(i).length() );
                build.append( trackTitlelines.get(i) );
                for(int j = 0; j < spaces; j++) build.append(' ');
                trackTitlelines.set( i, build.toString() );
            }
            ret.append( trackTitlelines.get(i) ).append( lengthlines.get(i) );
        }

        return ret;
    }

    /**
     * Get the genre name.
     * @param input The input genre, could be an integer or a string.
     * @return The genre name.
     */
    public String parseGenre(String input) {
        String ret = "";
        if(input == null) return ret;
        if( ! input.isEmpty() )
            try {
                ret = genres[ Integer.parseInt(input) ];
            } catch(NumberFormatException ne) { ret = input; }
        return ret;
    }

    //############ File methods #################

    /**
     * Write a file with supplied content.
     * @param newContent The content.
     * @param filename The file.
     */
    public void writeFile(StringBuffer newContent, String filename) {
        try {
            BufferedWriter out=new BufferedWriter( new FileWriter(filename));
            out.write(newContent.toString());
            out.close();
        } catch (Throwable e) { e.printStackTrace(); }
    }

    /**
     * Delete a directory recursively.
     * @param dir the directory.
     * @return whether successfully deleted directory.
     */
    public boolean deleteDir(File dir) {
        if ( dir.isDirectory() ) {
            String[] children = dir.list();
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir( new File(dir, children[i]) );
                if ( ! success) {
                    return false;
                } else {
                    if(verbose) System.out.println("Deleting dir: " + dir);
                }
            }
        } else {
            if(verbose) System.out.println("Error: Removing collection dir, is not a directory:\n\t" + dir);
        }

        // The directory is now empty so delete it
        return dir.delete();
    }


    //############ Panel methods #################

    /**
     * Set list content for both albumlist and songlist.
     * @param listPanel the list panel.
     * @param albumsQuery select query for the albumlist.
     * @param songsQuery select query for the songlist.
     */
    public void setListContent(ListPanel listPanel, String albumsQuery, String songsQuery) {

        ResultSet rs = getDAO().doSelectQuery(albumsQuery, getDAO().getConnection(), false);
        try {
            while(rs.next()) {
                listPanel.albumListModel.addElement( rs.getString("title") );
            }
        } catch(SQLException e) { e.printStackTrace(); }

        rs = getDAO().doSelectQuery(songsQuery, getDAO().getConnection(), false);
        try {
            while(rs.next()) {
                listPanel.songListModel.addElement( rs.getString("title") );
            }
        } catch(SQLException e) { e.printStackTrace(); }
    }

    /**
     * Get the list content for albumlist or songlist.
     * @param listModel the list model.
     * @param selectQuery the select query.
     * @param vals the values for the prepared statement.
     * @param getSonglist whether to get songlist or not.
     * @return the list content as a Set.
     */
    public Set getListContent(DefaultListModel listModel, String selectQuery, ArrayList<Object> vals, boolean getSonglist) {

        ArrayList<String> results = new ArrayList<String>();
        Set<String> ret = new HashSet<String>();
        PreparedStatement prepStmt;
        try {
            prepStmt = getDAO().getConnection().prepareStatement(selectQuery);
            for(int i = 0; i < vals.size(); i++) {
                prepStmt.setObject(i + 1, vals.get(i));
            }
            ResultSet rs = getDAO().executePreparedStmt(prepStmt);
            while(rs.next()) {
                results.add( rs.getString("title") );
                if(getSonglist) ret.add( rs.getString("album") );
            }
            Collections.sort(results);
            for(String s: results) listModel.addElement(s);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public String getSelectedSongFilename() { return selectedSongFilename; }

    public void setSelectedSongFilename(String filename) { selectedSongFilename = filename; }

    public String getSelectedAlbumDirectory() { return selectedAlbumDirectory; }

    public void setSelectedAlbumDirectory(String directory) { selectedAlbumDirectory = directory; }

    public String getSelectedAlbumTitle() { return selectedAlbumTitle; }

    public void setSelectedAlbumTitle(String title) { selectedAlbumTitle = title; }
}

//############ Inner classes #################
/**
 * FileFilter accepting directories and files with .mp3 extension.
 */
class Mp3Filter implements FileFilter {
    public boolean accept(File f) {
        if( f.isDirectory() ) { return true; }
        String name = f.getName().toLowerCase();
        return name.endsWith("mp3");
    }
}

/**
 * Comparator to sort the album directories.
 */
class ListSorter implements Comparator {

    public int compare(Object o1, Object o2) {

        String[] c1 = (String[]) o1;
        String[] c2 = (String[]) o2;

        String s1 = "";
        if(c1.length == 3) s1 = c1[2] + c1[1] + c1[0];
        String s2 = "";
        if(c2.length == 3) s2 = c2[2] + c2[1] + c2[0];

        return s1.compareTo(s2);
    }
}