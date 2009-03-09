package org.app.mp3AlbumManager.util;

import java.io.*;
import java.util.Properties;

/*
    This class runs a bash script on a UNIX system
    to get some additional info of a MP3 file.

    The script file (guessenc.sh) should be in the scripts folder.

    Requirements:
        - GNU Bash (http://www.gnu.org/software/bash)
        - mp3guessenc (http://www.rarewares.org/debian/packages/unstable)
 */

public class ScriptRunner {

    private static final String configfile = "res/config/config.properties";
    private static final String SCRIPT_PATH = "res/scripts/guessenc.sh";

    private int exitCode = -1;
    private String errorMessage;
    private String successMessage;

    private String bashPath = "/bin/bash";

    public ScriptRunner() {

        // read bash path from configuration file
        Properties configuration = new Properties();
        String bashProp = "Bash.path";

        try {
            configuration.load(new FileInputStream(configfile));
            String setting = configuration.getProperty(bashProp);
            // if value not found, set default
            if(setting == null) {
                System.err.println("\nWarning: Missing value ("+bashProp+") in configuration file. Using default: "
                        + bashProp + "=" + bashPath + "\n");
                bashPath = setting;
            }
        // exit if config file is not found
        } catch(IOException e) {
            System.err.println("\nError: Configuration file not found. Application will exit.\n");
            System.exit(1);
        }

    }
    // get the result from the script command
    public String getResult(String filename, String param) {

        String ret = null;
        String mp3file = escapeChars(filename);
        String[] scriptline;

        if( param.equals("lame") ) {
            scriptline = new String[] {bashPath, "-c", SCRIPT_PATH + " -l " + mp3file};
            processCommand(scriptline);
        } else if( param.equals("guess") ) {
            scriptline = new String[] {bashPath, "-c", SCRIPT_PATH + " -g " + mp3file};
            processCommand(scriptline);
        } else {
            System.err.println("Error [ScriptRunner.getValue] -> Wrong parameter: " + param);
            return null;
        }
        int result = getExitCode();
        if( result == 0 ) {
            ret = getSuccessMessage();
        } else {
            System.err.println("Error: [ScriptRunner.getValue] -> Exit code: " + result);
            System.out.println("Script error: " + getErrorMessage() );
            System.out.print("Script command: ");
            for(String c : scriptline) { System.out.print(c + " "); }
            System.out.println("");
        }
        return ret;

    }
    // escape special characters in filename
    public String escapeChars(String string) {
        int len = string.length();
        StringBuffer sb = new StringBuffer(len);
        char c;
        for (int i = 0; i < len; i++) {
            c = string.charAt(i);
            switch (c) {
                case ' ': sb.append("\\ "); break;
                case '&': sb.append("\\&"); break;
                case '(': sb.append("\\("); break;
                case ')': sb.append("\\)"); break;
                case '\'': sb.append("\\'"); break;
                default : sb.append(c); break;
            }
        }
        return sb.toString();
    }
    // actual processing of the command
    public void processCommand(String[] command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            errorMessage = processStream(process.getErrorStream());
            successMessage = processStream(process.getInputStream());
            exitCode = process.waitFor();
        } catch (Exception e) { e.printStackTrace();
        }
    }
    // processing the message from the command
    private String processStream(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder bs = new StringBuilder();
        while (true) {
            String c = br.readLine();
            if (c == null)
                break;
            bs.append(c);
            bs.append("\n");
        }
        return bs.toString().trim();
    }
    // getter methods
    public int getExitCode() { return exitCode; }

    public String getErrorMessage() { return errorMessage; }

    public String getSuccessMessage() { return successMessage; }
}