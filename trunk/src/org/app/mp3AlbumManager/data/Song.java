package org.app.mp3AlbumManager.data;

import java.io.File;

public class Song extends CollectionItem implements Comparable<Song> {

    private String album;
    private String subdir;
    private Integer bitrate;

    /**
     * Create a new Song.
     * @param filename the filename
     * @param subdir the sub directory.
     * @param track the track number.
     * @param artist the artist.
     * @param title the title.
     * @param length the length in seconds.
     * @param album the album title.
     * @param year the release year.
     * @param genre the genre.
     * @param tag the tag version.
     * @param lame the lame version.
     * @param bitrate the bitrate value.
     * @param vbr the vbr value.
     * @param frequency the sample rate.
     * @param mode the audio mode.
     */
    public Song(File filename, String subdir, String track, String artist, String title, Integer length, String album, String year,
                String genre, String tag, String lame, Integer bitrate, Boolean vbr,
                Integer frequency, String mode) {

        setFilename(filename);
        setTrack(track);
        setArtist(artist);
        setTitle(title);
        setLength(length);
        setAlbum(album);
        setYear(year);
        setGenre(genre);
        setTag(tag);
        setLame(lame);
        setFrequency(frequency);
        setMode(mode);

        setSubdir(subdir);
        this.bitrate = bitrate;
        setVbr(vbr);

    }

    /**
     * Set the album title.
     * @param a the album title.
     */
    public void setAlbum(String a) { album = a; }

    /**
     * Get the album title.
     * @return the album title.
     */
    public String getAlbum() { return album; }

    /**
     * Set the sub directory.
     * @param subdir the sub directory.
     */
    public void setSubdir(String subdir) { this.subdir = subdir; }

    /**
     * Get the sub directory.
     * @return the sub directory.
     */
    public String getSubdir() { return subdir; }

    /**
     * Get the bitrate value.
     * @return the bitrate value.
     */
    public Integer getBitrate() { return bitrate; }

    /**
     * Return the Song as a string.
     * @return the string.
     */
    public String toString() {
        StringBuffer ret = new StringBuffer();
        ret.append("\n").append( super.toString() )
                .append("\nfilename =").append(getFilename())
                .append("\nsubdir   =").append(getSubdir())
                .append("\ntrack    =").append(getTrack())
                .append("\nartist   =").append(getArtist())
                .append("\ntitle    =").append(getTitle())
                .append("\nlength   =").append(getLength())
                .append("\ntime     =").append(getLengthAsString( getLength() ))
                .append("\nalbum    =").append(getAlbum())
                .append("\nyear     =").append(getYear())
                .append("\ngenre    =").append(getGenre())
                .append("\ntag      =").append(getTag())
                .append("\nlame     =").append(getLame())
                .append("\nbitrate  =").append(getBitrate())
                .append("\nvbr      =").append(getVbr())
                .append("\nfrequency=").append(getFrequency())
                .append("\nmode     =").append(getMode())
                .append("\n");
        return ret.toString();
    }


    /**
     * Implementation of Comparable to sort songs (is this necessary?)
     * @param s The song.
     * @return compare result.
     */
    public int compareTo(Song s) {
        return ( this.getTrack() + this.getTitle() ).compareTo( s.getTrack() + s.getTitle() );
    }

}
