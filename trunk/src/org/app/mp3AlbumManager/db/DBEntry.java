package org.app.mp3AlbumManager.db;

import java.io.File;

public class DBEntry implements Comparable {

    private File mp3Dir, dbDir;
    private String dbName;

    /**
     * Create a new database entry.
     *
     * @param dbName The database name.
     * TODO: fix check for database name. The database (H2) restricts database names to be at least 3 characters long
     * @param dbDirname The database directory.
     * @param mp3Dirname The album directory.
     */
    public DBEntry(String dbName, String dbDirname, String mp3Dirname) {
        this.dbName = dbName;
        this.dbDir = new File(dbDirname);
        this.mp3Dir = new File(mp3Dirname);
    }

    /**
     * Get the database name.
     * @return The database name.
     */
    public String getDBName() { return dbName; }

    /**
     * Get the album directory name.
     * @return The album directory name.
     */
    public String getMp3Dirname() { return mp3Dir.getPath(); }

    /**
     * Get the album directory.
     * @return The album directory.
     */
    public File getMp3Dir() { return mp3Dir; }

    /**
     * Get the database directory.
     * @return The database directory.
     */
    public String getDBDir() { return dbDir.getPath(); }

    /**
     * Implementation of Comparable to sort the database entries.
     * @param obj
     * @return
     */
    public int compareTo(Object obj) {
        DBEntry e = (DBEntry) obj;
        String s1 = getDBName();
        String s2 = e.getDBName();
        return s1.compareTo(s2);
    }
}
