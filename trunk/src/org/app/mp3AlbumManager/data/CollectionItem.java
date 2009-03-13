package org.app.mp3AlbumManager.data;

import java.io.File;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public abstract class CollectionItem {

    private File filename;
    private String track, artist, title, year, genre, tag, lame, mode;
    private Integer length, frequency;
    private Boolean vbr;


    /**
     * Get the filename.
     * @return the filename.
     */
    public File getFilename() { return filename; }

    /**
     * Get the track number.
     * @return the track number.
     */
    public String getTrack() { return track; }

    /**
     * Get the artist.
     * @return the artist.
     */
    public String getArtist() { return artist; }

    /**
     * Get the title.
     * @return the title.
     */
    public String getTitle() { return title; }

    /**
     * Get the length in seconds.
     * @return the length in seconds.
     */
    public Integer getLength() { return length; }

    /**
     * Get formatted length.
     * @param timeInSeconds the length in seconds.
     * @return the length.
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
     * Get the release year.
     * @return the year.
     */
    public String getYear() { return year; }

    /**
     * Get the genre.
     * @return the genre.
     */
    public String getGenre() { return genre; }

    /**
     * Get the tag version.
     * @return the tag version.
     */
    public String getTag() { return tag; }

    /**
     * Get the lame version.
     * @return the lame version.
     */
    public String getLame() { return lame; }

    /**
     * Get the sample rate.
     * @return the sample rate.
     */
    public Integer getFrequency() { return frequency; }

    /**
     * Get the audio mode.
     * @return the audio mode.
     */
    public String getMode() { return mode; }

    /**
     * Get the vbr value.
     * @return vbr value.
     */
    public Boolean getVbr() { return vbr; }

    /**
     * Set the filename.
     * @param filename the filename.
     */
    public void setFilename(File filename) { this.filename = filename; }

    /**
     * Set the track number.
     * Output warning if not a valid number.
     * @param t the track number.
     */
    public void setTrack(String t) {
        try {
            Integer.parseInt(t);
        } catch (NumberFormatException nfe) {
            System.err.println("WARNING: Not a valid track number: " + t);
        }
        track = t;
    }

    /**
     * Set the artist.
     * @param a the artist.
     */
    public void setArtist(String a) { artist = a; }

    /**
     * Set the title.
     * @param t the title.
     */
    public void setTitle(String t) { title = t; }

    /**
     * Set the release year.
     * Output warning if not a valid year.
     * @param y the release year
     */
    public void setYear(String y) {

        if( ! checkYearInput(y) ) { System.err.println("WARNING: Not a valid year: " + y); }
        year = y;
    }

    /**
     * Checks whether input string is a valid year.
     * @param y the input string.
     * @return whether input string is a valid year.
     */
    private boolean checkYearInput(String y) {

        if (y == null) return false;
        
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        if (y.trim().length() != yearFormat.toPattern().length()) return false;
        try {
            yearFormat.parse( y.trim() );
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    /**
     * Set the length in seconds.
     * @param l the length in seconds.
     */
    public void setLength(Integer l) { length = l; }

    /**
     * Set the genre.
     * @param g the genre.
     */
    public void setGenre(String g) { genre = g; }

    /**
     * Set the tag version.
     * @param t the tag version.
     */
    public void setTag(String t) { tag = t; }

    /**
     * Set the lame version.
     * @param l the lame version.
     */
    public void setLame(String l) { lame = l; }

    /**
     * Set the audio mode.
     * @param m the audio mode.
     */
    public void setMode(String m) { mode = m; }

    /**
     * Set the sample rate.
     * @param f the sample rate.
     */
    public void setFrequency(Integer f) { frequency = f; }

    /**
     * Set the vbr value.
     * @param v the vbr value.-
     */
    public void setVbr (Boolean v) { vbr = v; }

}
