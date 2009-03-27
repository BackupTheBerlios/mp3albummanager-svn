package org.app.mp3AlbumManager.db;

import java.io.*;
import java.util.Properties;
import java.sql.*;

/**
 * The album Data Access Object class.
 * Note on queries:
 * do NOT enclose any question marks with single quotes (query will fail)
 */
public class AlbumDAO {
    /**
     * The database driver.
     */
    private static final String DB_DRIVER = "org.h2.Driver";
    /**
     * The JDBC url.
     */
    private static final String JDBC_URL = "jdbc:h2:";
    /**
     * The properties file for a database entry.
     */
    public static final String DB_PROPERTIES_FILE = "database.properties";

    private Properties dbProperties;
    private final DBEntry currentEntry;
    private final boolean doCreate;
    private boolean isConnected;
    private final boolean verbose;

    private Connection connection;
    private Statement stmt;
    private final PreparedStatement prepStmt;

    private String currentSearchQuery;

    /**
     * Create a new data access object instance.
     * @param entry The database entry.
     * @param newDB Whether to create a new database.
     * @param verbose Whether to output verbose info.
     */
    public AlbumDAO(DBEntry entry, boolean newDB, boolean verbose) {
        currentEntry = entry;
        doCreate = newDB;
        isConnected = false;
        this.verbose = verbose;

        connection = null;
        stmt = null;
        prepStmt = null;

        currentSearchQuery = null;

    }

    /**
     * Initialize the database.
     * Load the configuration properties, set the database driver, and load the database driver.
     * @return Whether initializing was successful.
     */
    public boolean initDB() {

        boolean propSuccess;
        propSuccess = setProperties(doCreate);

        if(propSuccess) {
            setBasedir();
            loadDriver();
            return true;

        } else {
            System.err.println("Error: Problem with properties file. Login will halt!");
            return false;
        }

    }

    //############ Properties methods #################
    /**
     * Initializing the configuration properties.
     * If it's a new database, create the properties file and proceed to write properties.
     * If it's not a new database, check if properties file exists and proceed to load the properties.
     * @param doCreate Whether creating a new database.
     * @return Whether successfully setting properties.
     */
    private boolean setProperties(boolean doCreate) {

        String dirname = currentEntry.getDBDir() + "/." + currentEntry.getDBName();
        File dbDir = new File(dirname);
        String filename = dirname  + "/" + DB_PROPERTIES_FILE;
        File propertiesFile = new File(filename);

        if(doCreate) {
            // create database directory
            if( dbDir.exists() ) { if(verbose) System.err.println("Warning: Directory already exists\n\t" + dbDir); }
            if( ! dbDir.mkdir() ) {
                if(verbose) System.err.println("Error: Failed to create directory\n\t" + dbDir);
                return false;
            }
            // create properties file
            try {
                if( ! propertiesFile.createNewFile() ) {
                    if(verbose) System.err.println("Warning: File already exists\n\t" + propertiesFile);
                    return false;
                }
            } catch (IOException e) {
                if(verbose) System.err.println("Error: Failed to create file\n\t" + propertiesFile);
                return false;
            }
            // write the properties to the file
            writeNewProperties(propertiesFile);

        } else {
            // check if database directory and properties file exists
            if( ! dbDir.exists() ) {
                if(verbose) System.err.println("Error: Directory not found\n\t" + dbDir);
                return false;
            }
            else if( ! propertiesFile.exists() ) {
                if(verbose) System.err.println("Error: File not found\n\t" + propertiesFile);
                return false;
            }
        }
        // load the properties
        dbProperties = loadProperties(propertiesFile);

        return true;
    }

    /**
     * Load the properties from a properties file.
     * @param propFile The properties file.
     * @return The properties.
     */
    private Properties loadProperties(File propFile) {

        Properties props = new Properties();
        try {
            FileReader stream = new FileReader(propFile);
            props.load(stream);
            stream.close();

        } catch (FileNotFoundException e) {
            if(verbose) System.err.println("Error: File not found\n\t" + propFile);
            return null;
        } catch (IOException ex) {
            if(verbose) System.err.println("Error: IOException when loading properties from file\n\t" + propFile);
            return null;
        }
        return props;
    }

    /**
     * Write new properties to a properties file.
     * @param propFile The properties file.
     */
    private void writeNewProperties(File propFile) {

        String dbDir = currentEntry.getDBDir();
        String dbName = currentEntry.getDBName();
        String mp3Dir = currentEntry.getMp3Dirname();

        Properties props = new Properties();
        props.setProperty("database.name", dbName);
        props.setProperty("database.home", dbDir);
        props.setProperty("mp3.dir", mp3Dir);

        BufferedWriter bw = null;
        try {
            // write properties to propFile
            props.store( new FileWriter(propFile), null );
            if(verbose) System.out.println("Writing properties to file:\n\t" + propFile);

            // write the database path to a recent file in application directory
            File appDir = new File( System.getProperty("user.home") + "/.mp3MusicManager" ); // the application directory
            File recentFile = new File( appDir + "/recent"); // the recent file
            // make the directory
            if( appDir.mkdir() ) { if(verbose) System.out.println("Creating application directory:\n\t" + appDir); }
            // if the path is not already written to the recent file
            if( ! foundInRecent(recentFile, dbDir) ) {
                // append the path to the recent file
                bw = new BufferedWriter( new FileWriter(recentFile, true) );
                bw.write(dbDir + "/." + dbName);
                bw.newLine();
                bw.flush();
                if(verbose) System.out.println("Appending path to recent file:\n\t" + recentFile);
            } else {
                if(verbose) System.out.println("Path already exists in recent file:\n\t" + recentFile);
            }

        } catch (IOException e) {
            System.err.println("Error: IOException when writing properties to file\n" + propFile + "\n");
        } finally {
            try {
                if(bw != null) bw.close();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Check if path is found in the recent file.
     * TODO: this check supposes database directory is unique for each db entry
     * @param recentFile The recent file.
     * @param path The path.
     * @return Whether path was found in recent file.
     */
    private boolean foundInRecent(File recentFile, String path) {

        boolean ret = false;
        String row;
        try {
            if(verbose) System.out.println("Checking for path in recent file:\n\t" + recentFile);
            FileReader fr = new FileReader(recentFile);
            BufferedReader br = new BufferedReader(fr);
            // if file is empty
            if( br.read() == -1) {
                if(verbose) System.out.println("Path not found. Recent file is empty");
                return false;
            }
            while( ( row = br.readLine() ) != null ) {
                if( row.equals(path) ) {
                    if(verbose) System.out.println("Path found in recent file.");
                    ret = true;
                }

            }
            
        } catch(IOException e) { e.printStackTrace(); }

        return ret;
    }

    /**
     * Get the database directory from the properties.
     * @return The database directory.
     */
    private String getDBDir() { return dbProperties.getProperty("database.home"); }

    /**
     * Get the database name from the properties.
     * @return The database name.
     */
    private String getDBName() { return dbProperties.getProperty("database.name"); }

    /**
     * Set the database base directory to <database directory>/.<database name>.
     */
    private void setBasedir() { System.setProperty("h2.baseDir", getDBDir() + "/." + getDBName() ); }

    //############ Database methods #################
    /**
     * Load database driver.
     */
    private void loadDriver() {

        try {
            Class.forName( DB_DRIVER );
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Connect to database.
     * @param user The user name.
     * @param pass The password.
     * @return The connection.
     */
    public Connection connect(String user, String pass) {

        String subname = currentEntry.getDBName();
        try {
            connection = DriverManager.getConnection(JDBC_URL + subname + ";USER=" + user + ";PASSWORD=" + pass, dbProperties);

            isConnected = connection != null;

        } catch (SQLException e) {
            System.err.println("Error: Problem connecting to database!");
            isConnected = false;
        }
        if(verbose) {
            String output = (connection != null) ? "\nCONNECTING -> " + connection.toString() : "\nCONNECTING -> null";
            System.out.println(output);
        }
        return connection;
    }

    /**
     * Disconnect from the database.
     */
    public void disconnect() {

        try {
            if (stmt != null) { stmt.close(); }
            if (prepStmt != null) { prepStmt.close(); }
        } catch(SQLException se) {
            se.printStackTrace();
        }

        if(isConnected) {
            if(verbose) System.out.println("\nDISCONNECTING -> " + connection.toString() );
            try {
                connection.close();
            } catch (SQLException ex) { ex.printStackTrace(); }

            isConnected = false;
        } else {
            if(verbose) System.out.println("\nDISCONNECTING -> [NOT CONNECTED]");
        }
    }

    /**
     * Get the connection to the database.
     * @return The connection.
     */
    public Connection getConnection() { return connection; }

    /**
     * Execute a select query.
     * @param query The query.
     * @param conn The connection.
     * @param isSearchQuery Whether this is query from the Search command. This setting is used for the Save HTML command.
     * @return The ResultSet.
     */
    public ResultSet doSelectQuery(String query, Connection conn, boolean isSearchQuery) {

        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);
            if(isSearchQuery) setCurrentSearchQuery(query);
        } catch(SQLException e) { e.printStackTrace(); }
        return rs;
    }

    /**
     * Set the current search query.
     * @param query The search query.
     */
    public void setCurrentSearchQuery(String query) { currentSearchQuery = query; }

    /**
     * Get the current search query.
     * @return The search query.
     */
    public String getCurrentSearchQuery() { return currentSearchQuery; }

    /**
     * Execute a update query.
     * @param query The query.
     * @return The ResultSet.
     */
    public ResultSet doUpdateQuery(String query) {

        Statement stmt;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rs = stmt.executeQuery(query);
        } catch(SQLException e) { e.printStackTrace(); }
        return rs;
    }

    /**
     * Create the database tables.
     * @param conn The connection.
     */
    public void createTables(Connection conn)  {

        String strCreateAlbumTable =
                "CREATE TABLE IF NOT EXISTS Album (" +
                        "   directory   VARCHAR(256) PRIMARY KEY," +
                        "   subdirs     VARCHAR(256), " +
                        "   tracks      INTEGER, " +
                        "   artist      VARCHAR_IGNORECASE(64), " +
                        "   title       VARCHAR_IGNORECASE(128), " +
                        "   various     BOOLEAN, " +
                        "   albumlength INTEGER, " +
                        "   albumyear   VARCHAR(4), " +
                        "   genre       VARCHAR(32), " +
                        "   tag         VARCHAR(24), " +
                        "   lame        VARCHAR(16), " +
                        "   bitrate     INTEGER, " +
                        "   vbr         BOOLEAN, " +
                        "   frequency   INTEGER, " +
                        "   mode        VARCHAR(32) " +
                        ")";
        String strCreateSongTable =
                "CREATE TABLE IF NOT EXISTS Song (" +
                        "   filename    varchar(256) PRIMARY KEY, " +
                        "   album      varchar_IGNORECASE(128), " +
                        "   subdir      varchar(128), " +
                        "   track       VARCHAR(8), " +
                        "   artist      VARCHAR_IGNORECASE(64), " +
                        "   title       VARCHAR_IGNORECASE(128), " +
                        "   songlength  INTEGER, " +
                        "   songyear    VARCHAR(4), " +
                        "   genre       VARCHAR(32), " +
                        "   tag         VARCHAR(24), " +
                        "   lame        varchar(16),  " +
                        "   bitrate     INTEGER, " +
                        "   vbr         BOOLEAN, " +
                        "   frequency   INTEGER, " +
                        "   mode        VARCHAR(32)" +
                        ")";

        try {
            stmt = conn.createStatement();
            stmt.execute(strCreateAlbumTable);
            stmt.execute(strCreateSongTable);
            stmt.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    public ResultSet executePreparedStmt(PreparedStatement prepStmt) {
        try {
            return prepStmt.executeQuery();
         } catch(SQLException e) {
            e.getMessage();
            if(verbose)  {
                System.out.println("ERROR: SQLException message: " + e.getMessage() );
                System.out.println("       Statement:\n\t" + prepStmt.toString() );
            }
            return null;
        }
    }

    /**
     * Execute an insert, update, or delete query with a prepared statement query.
     * @param prepStmt the PreparedStatement.
     * @return whether successfully executed query.
     */
    public boolean insertValues(PreparedStatement prepStmt) {
        
        try {
            if( prepStmt.executeUpdate() == 0) {
                if(verbose) System.err.println("INSERT FAILED:\n");
                return false;
            } else {
                return true;
            }

            //prepStmt.close();

        } catch(SQLException se) {
            se.printStackTrace();
        }
        return false;
    }

}
