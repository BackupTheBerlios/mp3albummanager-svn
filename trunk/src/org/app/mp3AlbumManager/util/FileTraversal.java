package org.app.mp3AlbumManager.util;

import java.io.File;
import java.io.IOException;
import java.io.FileFilter;

/**
 * This example by Torsten Curdt, see his blog http://vafer.org/blog/20071112204524
 */
public class FileTraversal {

    public final void traverse(final File f) throws IOException {
        Mp3Filter filter = new Mp3Filter();
        if( f.isDirectory() ) {
            onDirectory(f);
            final File[] childs = f.listFiles(filter);
            for( File child : childs ) {
                traverse(child);
            }
            return;
        }
        onFile(f);
    }

    public void onDirectory(final File d) { }

    public void onFile(final File f) { }

}

class Mp3Filter implements FileFilter {
    public boolean accept(File f) {
        if( f.isDirectory() ) {
            return true;
        }
        String name = f.getName().toLowerCase();
        return name.endsWith("mp3");
    }
}
