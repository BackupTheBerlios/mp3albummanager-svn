package org.app.mp3AlbumManager.data;

import org.app.mp3AlbumManager.util.ValueSortMap;

import java.util.*;
import java.io.File;

public class Album extends CollectionItem implements Comparator {

    private Boolean variousArtists = false;

    private Integer sumOfCbrValues;
    private Integer sumOfVbrValues;
    public int nrOfCbrValues;
    public int nrOfVbrValues;
    private Integer firstCbrValue;
    //private Boolean firstVBR;

    private final List<Song> songList;
    private final ArrayList<String> subdirs;

    private final Map<String, Integer> artistsMap;
    //private final Map<String, Integer> albumsMap;
    private final Map<String, Integer> yearsMap;
    private final Map<String, Integer> genresMap;
    private final Map<String, Integer> tagsMap;
    private final Map<String, Integer> lameMap;
    private final Map<Integer, Integer> freqsMap;
    private final Map<Integer, Integer> constantBitratesMap;
    private final Map<String, Integer> modesMap;
    private final Map<Boolean, Integer> vbrsMap;

    /**
     * Create a new Album.
     * @param dir the album directory.
     */
    public Album(File dir) {

        setFilename(dir);
        songList = new ArrayList<Song>();
        subdirs = new ArrayList<String>();
        sumOfCbrValues = 0;
        nrOfCbrValues = 0;
        sumOfVbrValues = 0;
        nrOfVbrValues = 0;

        setTrack("0");
        String[] artistTitleYear = getDirArtistTitleYear(dir);
        setArtist(artistTitleYear[0]);
        // initially set variousArtists to true if artist is equal to 'Various Artists'
        if( getArtist().equals("Various Artists") ) { setVarious(true); }
        setTitle(artistTitleYear[1]);
        setYear(artistTitleYear[2]);
        setLength(0);

        artistsMap = new HashMap<String, Integer>();
        //albumsMap = new HashMap<String, Integer>();
        yearsMap = new HashMap<String, Integer>();
        genresMap = new HashMap<String, Integer>();
        tagsMap = new HashMap<String, Integer>();
        lameMap = new HashMap<String, Integer>();
        //encoderMap = new HashMap<String, Integer>();
        constantBitratesMap = new HashMap<Integer, Integer>();
        vbrsMap = new HashMap<Boolean, Integer>();
        freqsMap = new HashMap<Integer, Integer>();
        modesMap = new HashMap<String, Integer>();
    }

    /**
     * Get the list of songs.
     * @return the list of songs.
     */
    public List<Song> getSonglist() { return songList; }

    /**
     * Add a song to the list of songs.
     * @param song the song.
     */
    public void addSong(Song song) { songList.add(song); }

    /**
     * Remove a song from the list of songs.
     * @param song the song.
     */
    public void removeSong(Song song) { if( songList.contains(song) ) { songList.remove(song); } }

    /**
     * Sort the list of songs using Comparator.
     * @see Song.compareTo(Song s)
     */
    public void sortSongs() { Collections.sort(songList, this); }

    /**
     * Get the sub directories.
     * @return the sub directories.
     */
    public ArrayList<String> getSubdirs() { return subdirs; }

    /**
     * Add a sub directory.
     * @param dir the sub directory.
     */
    public void addSubdir(String dir) {
        if( ! subdirs.contains(dir) ) {
            subdirs.add(dir);
            Collections.sort(subdirs);
        }
    }

    /**
     * Get album artist, album title and album release year from album directory name.
     * Hard-coded for dirname: "Artist - Album (Year)"
     * TODO: figure out if to use title, artist, year from dirname or from tag (for now I'm using dirname)
     * @param dir the album directory.
     * @return album artist, album title and album release year.
     */
    public String[] getDirArtistTitleYear(File dir) {

        boolean errArtist=false, errTitle=false, errYear=false;
        String[] ret = new String[3];
        String dirname = dir.getName(), artist, title, year;
        int startDelim, endDelim;
        // dir has structure: Artist - Album (Year)
        // extract these:    ^------^ ^-----^^----^

        // get artist
        startDelim = 0;
        endDelim = dirname.indexOf('-') - 2;
        if (endDelim > startDelim) {
            artist = dirname.substring(startDelim, endDelim + 1);
        } else {
            errArtist = true;
           artist = dirname; //return dir name if title is not found
        }
        // get title
        startDelim = dirname.indexOf('-') + 2;
        endDelim = dirname.indexOf('(') - 2;
        if (startDelim >= 0 && endDelim > startDelim) {
            title = dirname.substring(startDelim, endDelim + 1);
        } else {
            errTitle = true;
            title = dirname; //return dir name if artist is not found
        }
        // get year
        startDelim = dirname.indexOf('(') + 1;
        endDelim = dirname.indexOf(')') - 1;
        if (startDelim >= 0 && endDelim > startDelim) {
            year = dirname.substring(startDelim, endDelim + 1);
        } else {
            errYear = true;
            year = ""; //return empty string if aear is not found
        }
        //warnings
        if(errArtist)  System.err.println("Warning: Missing delimiters for extracting album ARTIST from directory name\n" + dir);
        if(errTitle) System.err.println("Warning: Missing delimiters for extracting album TITLE from directory name\n" + dir);
        if(errYear) System.err.println("Warning: Missing delimiters for extracting album YEAR from directory name\n" + dir);

        // 4. return array
        ret[0] = artist; ret[1] = title; ret[2] = year;
        return ret;
    }

    /**
     * Get the various artist value.
     * @return the various artist value.
     */
    public Boolean getVarious() { return variousArtists; }

    /**
     * Set the various artist value.
     * @param v the various artist value.
     */
    public void setVarious(Boolean v) { variousArtists = v; }

    /**
     * Add a song length to the album length.
     * @param l the song length.
     */
    public void addSongLength(Integer l) {
        Integer currentLength = getLength();
        Integer newLength = currentLength + l;
        setLength(newLength);
    }

    /**
     * Calculate the album bitrate.
     * TODO: decide how to deal with album with both CBRs and VBRs (for now I'm using majority rule)
     * @return the album bitrate.
     */
    public Integer getAlbumBitrate() {

        Integer albumBitrate;
        int nrOfTracks = Integer.parseInt( getTrack() );

        // ALT 1 (Majority rule): if nr of VBRs >= nr of CBRs -> treat album as a VBR, else as CBR
        if(nrOfVbrValues >= nrOfCbrValues) { // MAJORITY of tracks are VBRs -> treat album as a VBR
            albumBitrate = (sumOfVbrValues + sumOfCbrValues) / nrOfTracks;
            //round up to closest in cases where divisor is devisable by 2
            if ( (sumOfVbrValues + sumOfCbrValues) % nrOfTracks >= nrOfTracks / 2 )
                albumBitrate++;

        } else {
            albumBitrate = getFirstCbrValue();
        }

        // ALT 2: only if ALL tracks are VBRs -> treat album as a VBR, else as CBR
        /*
        if(nrOfCbrValues == 0) { // ALL tracks are VBRs -> treat album as a VBR
            albumBitrate = sumOfVbrValues / track;
            //round up to closest in cases where divisor is devisable by 2
            if ( sumOfVbrValues % track >= track / 2 )
                albumBitrate++;
        } else { // some or all tracks are CBRs -> treat album as CBR
            albumBitrate = getFirstCbrValue();
        }
        */
        return albumBitrate;
    }

    /**
     * Fill the maps with keys of album properties.
     */
    public void fillKeys() {

        setTrack( songList.size() + "" );
        for(Song song: songList) {
            addSongLength( song.getLength() );
            addToArtistsMap( song.getArtist() );
            //addToAlbumsMap( song.getAlbum() );
            addToYearsMap( song.getYear() );
            addToGenresMap( song.getGenre() );
            addToTagsMap( song.getTag() );
            addToModesMap( song.getMode() );
            addToLameMap( song.getLame() );
            addToFreqsMap( song.getFrequency() );
            addToVbrsMap( song.getVbr() );
            if( song.getVbr() ) {
                nrOfVbrValues++;
                sumOfVbrValues += song.getBitrate();
            } else {
                addToCbrsMap( song.getBitrate() );
                nrOfCbrValues++;
                sumOfCbrValues += song.getBitrate();
            }
        }
        setVbr(nrOfVbrValues >= nrOfCbrValues);

    }

    /**
     * Sort the maps by values.
     */
    public void sortByValues() {

        // Variable used for sorting HashMaps by values
        ValueSortMap sortUtil = new ValueSortMap();

        // string maps
        String genre = ( getGenresMap().size() == 0 ) ? "" : getFirstStringKeyInMap(sortUtil, getGenresMap() );
        setGenre(genre);
        String tag = ( getTagsMap().size() == 0 ) ? "" : getFirstStringKeyInMap(sortUtil, getTagsMap() );
        setTag(tag);
        String lame = ( getLameMap().size() == 0 ) ? "" : getFirstStringKeyInMap(sortUtil, getLameMap() );
        setLame(lame);
        String mode = ( getModesMap().size() == 0 ) ? "" : getFirstStringKeyInMap(sortUtil, getModesMap() );
        setMode(mode);

        /*
        DISABLED
        //String album = getAlbumsMap();
        //String artist = getArtistsMap();
        //String year = getYearsMap();

        USE THESE INSTEAD: getTitle(), getArtist(), getYear()
        */

        // integer maps
        Integer cbrValue = ( getCbrsMap().size() == 0 ) ? 0 : getFirstIntegerKeyInMap(sortUtil, getCbrsMap() );
        setFirstCbrValue(cbrValue);
        Integer freq = ( getFreqsMap().size() == 0 ) ? 0 : getFirstIntegerKeyInMap(sortUtil, getFreqsMap() );
        setFrequency(freq);

        // boolean maps
        //Boolean vbr = ( getVbrsMap().size() == 0 ) ? false : getFirstBooleanKeyInMap(sortUtil, getVbrsMap() );
        //setFoundVbrValue(vbr);

    }

    /**
     * Get the first key in a string map.
     * @param sortUtil the helper class to sort a map by values.
     * @param map the map to sort.
     * @return the first key in a string map.
     */
    public String getFirstStringKeyInMap (ValueSortMap sortUtil, Map<String, Integer> map) {
        LinkedHashMap sortedMap = sortUtil.sortMapByValue( map, false );
        String[] foundStringValues = getSortedStringElements(sortedMap);
        return foundStringValues[0];
    }

    /**
     * Get the first key in a integer map.
     * @param sortUtil the helper class to sort a map by values.
     * @param map the map to sort.
     * @return the first key in a integer map.
     */
    public Integer getFirstIntegerKeyInMap (ValueSortMap sortUtil, Map<Integer, Integer> map) {
        LinkedHashMap sortedMap = sortUtil.sortMapByValue( map, false );
        Integer[] foundIntegerValues = getSortedIntegerElements(sortedMap);
        return foundIntegerValues[0];
    }

    /**
     * Get the first key in a boolean map.
     * @param sortUtil the helper class to sort a map by values.
     * @param map the map to sort.
     * @return the first key in a boolean map.
     */
    public Boolean getFirstBooleanKeyInMap (ValueSortMap sortUtil, Map<Boolean, Integer> map) {
        LinkedHashMap sortedMap = sortUtil.sortMapByValue( map, false );
        Boolean[] foundBooleanValues = getSortedBooleanElements(sortedMap);
        return foundBooleanValues[0];
    }

    //############ Map methods #################

    //--------------ARTISTS-------------------

    /**
     * Get the map with album artists.
     * @return the artists map.
     */
    public Map<String, Integer> getArtistsMap() { return artistsMap; }

    /**
     * Add an artist to the artists map.
     * @param artist the artist.
     */
    public void addToArtistsMap(String artist) {
        if( artistsMap.containsKey(artist) ) {
            artistsMap.put( artist, artistsMap.get(artist) + 1 );
        } else {
            artistsMap.put(artist, 1);

        }
    }

    //--------------ALBUMS-------------------
    /**
     * Get the map with album titles.
     * @return the album titles map
     */
    //public Map<String, Integer> getAlbumsMap() { return albumsMap; }

    /**
     * Add an album title to the albums map.
     * @param album the album title.
     */
    /*
    public void addToAlbumsMap(String album) {
        if( albumsMap.containsKey(album) ) {
            albumsMap.put( album, albumsMap.get(album) + 1 );
        } else {
            albumsMap.put(album, 1);
        }
    }
    */
    //--------------YEARS-------------------
    /**
     * Get the years map.
     * @return the years map.
     */
    public Map<String, Integer> getYearsMap() { return yearsMap; }

    /**
     * Add a year to the years map.
     * @param year the year.
     */
    public void addToYearsMap(String year) {
        if( yearsMap.containsKey(year) ) {
            yearsMap.put( year, yearsMap.get(year) + 1 );
        } else {
            yearsMap.put(year, 1);
        }
    }

    //--------------GENRES-------------------
    /**
     * Get the genres map.
     * @return the genres map.
     */
    public Map<String, Integer> getGenresMap() { return genresMap; }

    /**
     * Add a genre to the genres map.
     * @param genre the genre.
     */
    public void addToGenresMap(String genre) {
        if( genresMap.containsKey(genre) ) {
            genresMap.put( genre, genresMap.get(genre) + 1 );
        } else {
            genresMap.put(genre, 1);
        }
    }

    //--------------TAGS-------------------
    /**
     * Get the tags map.
     * @return the tags map.
     */
    public Map<String, Integer> getTagsMap() { return tagsMap; }

    /**
     * Add a tag to the tags map.
     * @param tag the tag.
     */
    public void addToTagsMap(String tag) {
        if( tagsMap.containsKey(tag) ) {
            tagsMap.put( tag, tagsMap.get(tag) + 1 );
        } else {
            tagsMap.put(tag, 1);
        }
    }

    //--------------MODES-------------------
    /**
     * Get the modes map.
     * @return the modes map.
     */
    public Map<String, Integer> getModesMap() { return modesMap; }

    /**
     * Add a mode to the modes map.
     * @param mode the mode.
     */
    public void addToModesMap(String mode) {
        if( modesMap.containsKey(mode) ) {
            modesMap.put( mode, modesMap.get(mode) + 1 );
        } else {
            modesMap.put(mode, 1);
        }
    }

    //--------------LAMES-------------------
    /**
     * Get the lames map.
     * @return the lames map.
     */
    public Map<String, Integer> getLameMap() { return lameMap; }

    /**
     * Add a lame to the lames map.
     * @param lame the lame.
     */
    public void addToLameMap(String lame) {

        if( lameMap.containsKey(lame) ) {
            lameMap.put( lame, lameMap.get(lame) + 1 );
        } else {
            lameMap.put(lame, 1);
        }
    }

    //--------------FREQS-------------------
    /**
     * Get the sample rates map.
     * @return  the sample rates map.
     */
    public Map<Integer, Integer> getFreqsMap() { return freqsMap; }

    /**
     * Add a sample rate to the sample rates map.
     * @param freq the sample rate.
     */
    public void addToFreqsMap(Integer freq) {
        if( freqsMap.containsKey(freq) ) {
            freqsMap.put( freq, freqsMap.get(freq) + 1 );
        } else {
            freqsMap.put(freq, 1);
        }
    }

    //--------------CBR VALUES-------------------
    /**
     * Get the cbr values map.
     * @return the cbr values map.
     */
    public Map<Integer, Integer> getCbrsMap() { return constantBitratesMap; }

    /**
     * Add a cbr value to the cbr values map.
     * @param bitrate the cbr value.
     */
    public void addToCbrsMap(Integer bitrate) {
        if( constantBitratesMap.containsKey(bitrate) ) {
            constantBitratesMap.put( bitrate, constantBitratesMap.get(bitrate) + 1 );
        } else {
            constantBitratesMap.put(bitrate, 1);
        }
    }

    /**
     * Set the first cbr value.
     * @param bitrate the cbr value.
     */
    public void setFirstCbrValue(Integer bitrate) { firstCbrValue = bitrate; }

    /**
     * Get the first cbr value.
     * @return the first cbr value.
     */
    public Integer getFirstCbrValue() { return firstCbrValue; }

    //--------------VBRs-------------------
    /**
     * Get the vbr values map.
     * @return vbr values map.
     */
    public Map<Boolean, Integer> getVbrsMap() { return vbrsMap; }

    /**
     * Add a vbr value to the vbr values map.
     * @param vbr the vbr value.
     */
    public void addToVbrsMap(Boolean vbr) {
        if( vbrsMap.containsKey(vbr) ) {
            vbrsMap.put( vbr, vbrsMap.get(vbr) + 1 );
        } else {
            vbrsMap.put(vbr, 1);
        }
    }

    /**
     * Set the flag whether there is a vbr value in the vbr values map..
     * @param vbr the flag value.
     */
    //public void setFoundVbrValue(Boolean vbr) { firstVBR = vbr; }

    //############ Map sorting methods #################
    /**
     * Get a string array of sorted map keys.
     * @param map the map.
     * @return the sorted string elements.
     */
    public String[] getSortedStringElements(LinkedHashMap map) {
        int mapSize = map.size();
        String[] elements = new String[mapSize];
        Object[] sortedArray = map.entrySet().toArray();
        for (int i = 0; i < mapSize; i++) {
            Map.Entry e = (Map.Entry) sortedArray[i];
            elements[i] = (String) e.getKey();
        }
        return elements;
    }

    /**
     * Get an integer array of sorted map keys.
     * @param map the map.
     * @return the sorted integer elements.
     */
    public Integer[] getSortedIntegerElements(LinkedHashMap map) {
        int mapSize = map.size();
        Integer[] elements = new Integer[mapSize];
        Object[] sortedArray = map.entrySet().toArray();
        for (int i = 0; i < mapSize; i++) {
            Map.Entry e = (Map.Entry) sortedArray[i];
            elements[i] = (Integer) e.getKey();
        }
        return elements;
    }

    /**
     * Get a boolean array of sorted map keys.
     * @param map the map.
     * @return the sorted boolean elements.
     */
    public Boolean[] getSortedBooleanElements(LinkedHashMap map) {
        int mapSize = map.size();
        Boolean[] elements = new Boolean[mapSize];
        Object[] sortedArray = map.entrySet().toArray();
        for (int i = 0; i < mapSize; i++) {
            Map.Entry e = (Map.Entry) sortedArray[i];
            elements[i] = (Boolean) e.getKey();
        }
        return elements;
    }

    /**
     * Implementation of Comparator to sort songs comparing a songs sub directory and track number with another song..
     * @param o1 the first song.
     * @param o2 the second song.
     * @return compare result.
     */
    public int compare(Object o1, Object o2) {
        Song c1, c2;
        String s1="", s2="";
        if(o1 instanceof Song && o2 instanceof Song) {
            c1 = (Song) o1;
            c2 = (Song) o2;
            s1 = c1.getSubdir() + c1.getTrack();
            s2 = c2.getSubdir() + c2.getTrack();
        }

        return s1.compareTo(s2);
    }

    /**
     * Return the Album as a string.
     * @return the string.
     */
    public String toString() {
        StringBuffer ret = new StringBuffer();
        ret.append("\n").append( super.toString() )
            .append("\ndirectory =").append(getFilename())
            .append("\nsubdirs   =").append(getSubdirs())
            .append("\ntracks    =").append(getTrack())
            .append("\nartist    =").append(getArtist())
            .append("\ntitle     =").append(getTitle())
            .append("\nlength    =").append(getLength())
            .append("\ntime      =").append( getLengthAsString( getLength() ) )
            .append("\nvarious   =").append(getVarious())
            .append("\nyear      =").append(getYear())
            .append("\ngenre     =").append(getGenre())
            .append("\ntag       =").append(getTag())
            .append("\nlame      =").append(getLame())
            .append("\nbitrate   =").append(getAlbumBitrate())
            .append("\nvbr       =").append(getVbr())
            .append("\nfrequency =").append(getFrequency())
            .append("\nmode      =").append(getMode())
            .append("\n");
        return ret.toString();
    }

}
