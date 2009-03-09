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

import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.nio.channels.FileChannel;
import java.nio.ByteBuffer;

public class ReadDirectories {

    private boolean verbose = true;
    private final String path;
    private String subdir = "";

    private Song song;
    private Album currentAlbum;
    private File currentAlbumdir;
    private final Mp3Collection collection;

    public ReadDirectories(File dir, boolean verbose) {

        this.verbose = verbose;
        path = dir.getPath();
        song = null;
        currentAlbum = null;
        currentAlbumdir = null;
        collection = new Mp3Collection();

        //ScriptRunner runThis = new ScriptRunner();
    }

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
     */
    public void getFiles() {

        if(verbose) { System.out.println("\nREADING FILES:"); }

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

        MP3File mp3file = null;
        MP3AudioHeader audioHeader = null;
        Tag mp3tag = null;
        ID3v1Tag v1tag;
        AbstractID3v2Tag v2tag = null;
        boolean foundID3v1 = false, foundID3v2 = false;
        String track, artist, title, album, year, genre, tag, lame;
        String mode;
        Integer length, bitrate, frequency;
        Boolean vbr;


        if(verbose) System.out.println("FILE: " + file);
        try {
            mp3file = new MP3File(file);
            audioHeader = mp3file.getMP3AudioHeader();
            mp3tag = mp3file.getTag();

            tag = "";

            if( mp3file.hasID3v1Tag() ) {
                v1tag = (ID3v1Tag) mp3tag;
                tag += v1tag.getIdentifier();
                foundID3v1 = true;
            }
            if( mp3file.hasID3v2Tag() ) {
                v2tag  = mp3file.getID3v2Tag();
                if(foundID3v1) tag += ", ";
                tag += v2tag.getIdentifier();
                foundID3v2 = true;
            }

            if(foundID3v2) {

                track = v2tag.getFirst(ID3v24Frames.FRAME_ID_TRACK);
                artist = v2tag.getFirst(ID3v24Frames.FRAME_ID_ARTIST);
                title = v2tag.getFirst(ID3v24Frames.FRAME_ID_TITLE);
                album = v2tag.getFirst(ID3v24Frames.FRAME_ID_ALBUM);
                year = v2tag.getFirst(ID3v24Frames.FRAME_ID_YEAR);
                genre = v2tag.getFirst(ID3v24Frames.FRAME_ID_GENRE);
            } else {
                System.err.println("ERROR: No ID3v2 tag found:\n\t" + file);
                return;
            }

            length = audioHeader.getTrackLength(); //seconds

            // get lame frame
            new MP3AudioHeader(file).getEncoder();  // -> NULL

            FileChannel fc = null;
            ByteBuffer bb = null;

            fc = new FileInputStream(file).getChannel();
            //TODO: fix get lame string, need to find start position of lame header 
            bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, file.length() );
            String id = Utils.getString(bb, 0, 4 , TextEncoding.CHARSET_UTF_8);

            if(verbose) System.out.println("======> LAME ID: " + id);
            bb.rewind();
            if (id.equals("LAME")) { lame = Utils.getString(bb, 0, 9, TextEncoding.CHARSET_UTF_8); }
            else lame = "";
            

            String bitrateAsString = "" + audioHeader.getBitRateAsNumber(); // kbps
            bitrate = Integer.parseInt(bitrateAsString);
            vbr = (Boolean) audioHeader.isVariableBitRate();
            frequency = audioHeader.getSampleRateAsNumber(); // Hz

            mode = audioHeader.getChannels(); // Stereo, Mono, etc

            // create Song instance song
            song = new Song(file, getSubdir(), track, artist, title, length, album, year,
                    genre, tag, lame, bitrate, vbr, frequency, mode);

            if(verbose) System.out.println( song.toString() );

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

            // check if this is a new album or not
            if( currentAlbum == null || ! currentAlbumdir.equals(currentAlbum.getFilename() ) ) {
                //this is a new album, create it (and set albumdir & subdir, if any)
                Album newAlbum = new Album(currentAlbumdir);
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
            System.out.println("ERROR: IOException reading file\n\t" + file);
        } catch (TagException e) {
            System.out.println("ERROR: TagException reading file\n\t" + file);
        } catch (ReadOnlyFileException e) {
            System.out.println("ERROR: ReadOnlyFileException reading file\n\t" + file);
        } catch (InvalidAudioFrameException e) {
            System.out.println("ERROR: InvalidAudioFrameException reading file\n\t" + file);
        }
    }

}
