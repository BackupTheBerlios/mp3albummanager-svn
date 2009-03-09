package org.app.mp3AlbumManager.util;

import java.io.*;

/**
 * Create a new TemplateWriter object.
 */
public class TemplateWriter {
    /**
     * Create the content to write to a new file using a file template.
     * @param templateFilename The template filename.
     * @param oldPatterns Patterns in the template file to be replaced.
     * @param replPatterns The new patterns.
     * @return The content.
     */
    public StringBuffer readReplace(String templateFilename, String[] oldPatterns, String[] replPatterns) {
        String line;
        StringBuffer sb = new StringBuffer();
        int nrOfPatterns = oldPatterns.length;
        // checks
        if( nrOfPatterns < 1 || replPatterns.length < 1 ) { return null; }
        if( nrOfPatterns != replPatterns.length ) { return null; }


        try {
            FileInputStream fis = new FileInputStream(templateFilename);
            BufferedReader reader=new BufferedReader( new InputStreamReader(fis));
            while((line = reader.readLine()) != null) {
                for(int i = 0; i < nrOfPatterns; i++) {
                    line = line.replaceAll(oldPatterns[i], replPatterns[i]);
                }
                sb.append(line).append("\n");
            }
            reader.close();
        } catch (Throwable e) { e.printStackTrace(); }

        return sb;
    }

}