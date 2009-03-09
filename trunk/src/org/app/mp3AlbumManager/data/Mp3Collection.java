package org.app.mp3AlbumManager.data;

import java.util.Set;
import java.util.HashSet;

public class Mp3Collection {

    private final Set<Album> albumList;

    Mp3Collection() {

        albumList = new HashSet<Album>();

    }

    /**
     * Get the set of albums.
     * @return the set of albums.
     */
    public Set<Album> getAlbumSet() { return albumList; }

    /**
     * Add an Album to the set of albums.
     * @param album the Album.
     */
    public void addAlbum(Album album) { albumList.add(album); }

    /**
     * Remove an Album from the set of albums.
     * @param album the Album.
     */
    public void removeAlbum(Album album) { if( albumList.contains(album) ) { albumList.remove(album); } }

}
