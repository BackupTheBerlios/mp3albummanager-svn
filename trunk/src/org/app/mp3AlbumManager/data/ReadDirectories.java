package org.app.mp3AlbumManager.data;

import org.app.mp3AlbumManager.util.FileTraversal;
import org.jaudiotagger.audio.mp3.*;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.generic.Utils;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.ID3v1Tag;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.ID3v24Frames;
import org.jaudiotagger.tag.id3.valuepair.TextEncoding;

import javax.swing.*;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;

public class ReadDirectories {

    private boolean verbose;
    private final String path;
    private String albumArtist, albumTitle, albumYear;
    private Boolean variousAlbum;
    private String subdir = "";

    private Song song;
    private Album currentAlbum;
    private File currentAlbumdir;
    private final Mp3Collection collection;

    private int nrOfMp3s;

    JProgressBar theProgressBar;

    public ReadDirectories(File dir, boolean verbose) {

        this.verbose = verbose;
        path = dir.getPath();
        song = null;
        currentAlbum = null;
        currentAlbumdir = null;
        collection = new Mp3Collection();

        nrOfMp3s = 0;

        //ScriptRunner runThis = new ScriptRunner();
    }

    /**
     * Set the album artist.
     * @param a the album artist.
     */
    public void setAlbumArtist(String a) { albumArtist = a; }

    /**
     * Get the album artist.
     * @return the album artist.
     */
    public String getAlbumArtist() { return albumArtist; }

    /**
     * Set the album title.
     * @param t the album title.
     */
    public void setAlbumTitle(String t) { albumTitle = t; }

    /**
     * Get the album title.
     * @return the album title.
     */
    public String getAlbumTitle() { return albumTitle; }

    /**
     * Set the album release year.
     * @param y the album release year
     */
    public void setAlbumYear(String y) { albumYear = y; }

    /**
     * Get the album release year.
     * @return the album release year.
     */
    public String getAlbumYear() { return albumYear; }


    /**
     * Get the subdirectory.
     * @return The subdirectory.
     */
    public String getSubdir() { return subdir; }

    /**
     * Set the sub directory.
     * @param dir The subdirectory.
     */
    public void setSubdir(String dir) { subdir = dir; }

    /**
     * Get the collection.
     * @return The collection.
     */
    public Mp3Collection getCollection() { return collection; }

    /**
     * Traverse files in path and send to scanFile().
     * @see ReadDirectories.scanFile()
     * @param theProgressBar the progress bar, to monitor progress.
     */
    public void getFiles(JProgressBar theProgressBar) {

        if(verbose) { System.out.println("\nREADING FILES:"); }
        this.theProgressBar = theProgressBar;

        try {
            new FileTraversal() {

                public void onDirectory(final File d) {
                    //if(verbose) { System.out.println("DIR    : " + d.getName() ); }
                }
                public void onFile(final File f) {
                    // get tag properties for mp3 file
                    scanFile(f);
                }
            }.traverse(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(verbose) { System.out.println("[OK]"); }
    }

    /**
     * Scan file and get tag properties, and create Album and Song instances.
     * @param file The file to scan.
     */
    void scanFile(File file) {

        if(verbose) System.out.println("FILE: " + file);

        boolean foundID3v1 = false, foundID3v2 = false;
        String track, artist, title, year, genre, tag, lame;
        String mode;
        Integer length, bitrate, frequency;
        Boolean vbr;

        //----------- get the tag -----------------
        AbstractID3v2Tag v2tag = null;
        try {
            MP3File  mp3file = new MP3File(file);
            MP3AudioHeader audioHeader = mp3file.getMP3AudioHeader();
            Tag mp3tag = mp3file.getTag();

            tag = "";

            if( mp3file.hasID3v1Tag() ) {
                ID3v1Tag v1tag = (ID3v1Tag) mp3tag;
                tag += v1tag.getIdentifier();
                foundID3v1 = true;
            }
            if( mp3file.hasID3v2Tag() ) {
                v2tag  = mp3file.getID3v2Tag();
                if(foundID3v1) tag += ", ";
                tag += v2tag.getIdentifier();
                foundID3v2 = true;
            }

            //----------- get the tag values -----------------

            if(foundID3v2) {
                track = v2tag.getFirst(ID3v24Frames.FRAME_ID_TRACK);
                artist = v2tag.getFirst(ID3v24Frames.FRAME_ID_ARTIST);
                title = v2tag.getFirst(ID3v24Frames.FRAME_ID_TITLE);
                //album = v2tag.getFirst(ID3v24Frames.FRAME_ID_ALBUM);
                year = v2tag.getFirst(ID3v24Frames.FRAME_ID_YEAR);
                genre = v2tag.getFirst(ID3v24Frames.FRAME_ID_GENRE);
            } else {
                if(verbose) System.err.println("ERROR: No ID3v2 tag found:\n\t" + file);
                return;
            }

            // get length
            length = audioHeader.getTrackLength(); //seconds

            // get lame frame
            lame = audioHeader.getEncoder();

            // get bitrate
            String bitrateAsString = "" + audioHeader.getBitRateAsNumber(); // kbps
            bitrate = Integer.parseInt(bitrateAsString);
            vbr = audioHeader.isVariableBitRate();
            frequency = audioHeader.getSampleRateAsNumber(); // Hz

            // get audio mode
            mode = audioHeader.getChannels(); // Stereo, Joint Stereo, Dual or Mono

            //----------- set album dir and subdir -----------------

            // get the parent dir
            File parentDir = file.getParentFile();
            // check if parent dirname is a subdir (ie dirname starts with 'cd')
            String parentDirname = parentDir.getName();
            if(parentDirname.toLowerCase().startsWith("cd")) {
                // this is a subdir, set current album to PARENT of parent dir
                currentAlbumdir = parentDir.getParentFile();
                if(verbose) System.out.println("Found subdir. Setting current dir to parent: " + currentAlbumdir);
                // set subdir to parent dir
                setSubdir(parentDirname);
            } else {
                // this is not a subdir, set current album to parent dir (and subdir to empty)
                currentAlbumdir = parentDir;
                if(verbose) System.out.println("Current dir: " + currentAlbumdir);
                setSubdir("");
            }

            //----------- get album artist, title, year -----------------
            if(currentAlbumdir != null) {
                String[] artistTitleYear = getDirArtistTitleYear(currentAlbumdir);
                setAlbumArtist(artistTitleYear[0]);
                setAlbumTitle(artistTitleYear[1]);
                setAlbumYear(artistTitleYear[2]);
            } else {
                if(verbose) System.out.println("ERROR: Failed to get album artist, title, year (Album dir is null)\n");
            }

            //----------- create the song -----------------

            // create Song instance song
            song = new Song(file, getSubdir(), track, artist, title, length, getAlbumTitle(), year,
                    genre, tag, lame, bitrate, vbr, frequency, mode);

            if(verbose) System.out.println( song.toString() );
            // update number of songs and set the progress
            nrOfMp3s++;
            theProgressBar.setValue(nrOfMp3s);

            //----------- create the album -----------------

            // check if this is a new album or not
            if( currentAlbum == null || ! currentAlbumdir.equals(currentAlbum.getFilename() ) ) {
                //this is a new album, create it (and set albumdir & subdir, if any)
                Album newAlbum = new Album(currentAlbumdir, getAlbumArtist(), getAlbumTitle(), getAlbumYear(), getAlbumArtist().equals("Various Artists") );
                newAlbum.setFilename(currentAlbumdir);
                // add song to new album
                newAlbum.addSong(song);
                if(getSubdir() != null) { newAlbum.addSubdir( getSubdir() ); }
                // add album to mp3 collection
                collection.addAlbum(newAlbum);
                // set current album (and albumdir) to the new album
                currentAlbum = newAlbum;
                currentAlbumdir = newAlbum.getFilename();
                if(verbose) System.out.println("Adding NEW album to collection.");
                if(verbose) System.out.println("Adding song: " + song.getTitle() );
                if(verbose) System.out.println("Album dir: " + currentAlbumdir);
            } else {
                // NOT a new album, add song to current album
                // remove the album from the collection
                collection.removeAlbum(currentAlbum);
                if(verbose) System.out.println("OLD album.");
                if(verbose) System.out.println("Adding song: " + song.getTitle() );
                // add song to the current album
                currentAlbum.addSong(song);
                // add subdir to album (if not already done so)
                if( ! currentAlbum.getSubdirs().contains( getSubdir() ) ) {
                    currentAlbum.addSubdir( getSubdir() );
                    if(verbose) System.out.println("Adding subdir: " + getSubdir() );
                }
                // add the album to the collection
                collection.addAlbum(currentAlbum);

            }
            if(verbose) System.out.println();

        } catch (IOException e) {
            if(verbose) System.err.println("ERROR: IOException reading file\n\t" + file);
        } catch (TagException e) {
            if(verbose) System.err.println("ERROR: TagException reading file\n\t" + file);
        } catch (ReadOnlyFileException e) {
            if(verbose) System.err.println("ERROR: ReadOnlyFileException reading file\n\t" + file);
        } catch (InvalidAudioFrameException e) {
            if(verbose) System.err.println("ERROR: InvalidAudioFrameException reading file\n\t" + file);
        }
    }

    /**
     * Get album artist, album title and album release year from album directory name.
     * Somewhat hard-coded for dirname: "Artist - Album (Year)"
     * TODO: figure out if to use title, artist, year from dirname or from tag (for now I'm using dirname)
     * @param dir the album directory.
     * @return a string array with album artist, album title and album release year.
     */
    public String[] getDirArtistTitleYear(File dir) {

        String dirname = dir.getName();
        String[] ret = new String[3];

        String rest, artist, title, year;
        boolean foundArtist, foundStartYear, foundEndYear;
        int firstpos, lastpos;

        // dir has structure: Artist - Album (Year)
        // extract these:    ^------^ ^-----^^----^

        //------------- first delim (Artist) -------------
        foundArtist = dirname.contains(" - ");
        firstpos = dirname.indexOf('-');

        if(foundArtist) {
            artist = dirname.substring(0, firstpos - 1);
            rest = dirname.substring(firstpos + 2);
        } else {
            artist = "UNKNOWN";
            rest = dirname;
        }
        //------------- second delim (Album) -------------
        foundStartYear = rest.contains("(");
        firstpos = rest.indexOf('(');
        lastpos = rest.lastIndexOf('(');

        if(foundStartYear) {
            if(firstpos == lastpos) {
                title = rest.substring(0, firstpos - 1);
                rest = rest.substring(firstpos + 1);
            } else {
                title = rest.substring(0, lastpos - 1);
                rest = rest.substring(lastpos + 1);
            }
        } else {
            title = rest;
        }
        //------------- third delim (Year) -------------
        foundEndYear = rest.contains(")");
        firstpos = rest.indexOf(')');
        lastpos = rest.lastIndexOf(')');

        if(foundStartYear && foundEndYear) {
            if(firstpos == lastpos) {
                year = rest.substring(0, firstpos);
            } else {
                year = rest.substring(0, lastpos);
            }
        } else {
            year = "0";
        }

        // set and return array
        ret[0] = artist; ret[1] = title; ret[2] = year;
        if(verbose) System.out.println("\nDIRNAME: " + dirname + "\n\tARTIST: " + artist + "\n\tTITLE: " + title + "\n\tYEAR: " + year);
        return ret;
    }

}
