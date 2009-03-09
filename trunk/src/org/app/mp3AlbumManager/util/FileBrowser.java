package org.app.mp3AlbumManager.util;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class FileBrowser extends JFrame {

    private final JFileChooser fc;
    private int dialogType;

    /**
     * Create a new JFileChooser.
     * @param dialogType the dialog type: JFileChooser.OPEN_DIALOG, JFileChooser.SAVE_DIALOG
     * @param selectType The type of files to be displayed: JFileChooser.FILES_ONLY, JFileChooser.DIRECTORIES_ONLY, JFileChooser.FILES_AND_DIRECTORIES
     */
    public FileBrowser(int dialogType,int selectType) {

        this.dialogType = dialogType;
        fc = new JFileChooser("Select directory to scan...");
        fc.setFileSelectionMode(selectType);
        setVisible( true );
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Get the selected file or directory from the JFileChooser.
     * @return The selected file or directory.
     */
    public String getTarget() {

        String fullPath = null;
        File fileName;

        int state = -1;
        if(dialogType == JFileChooser.OPEN_DIALOG)
            state = fc.showOpenDialog(this);
        else if(dialogType == JFileChooser.SAVE_DIALOG)
            state = fc.showSaveDialog(this);
        else
            System.err.println("ERROR: Problem with FileBrowser: Wrong dialog type");
        
        if ( state != JFileChooser.APPROVE_OPTION) {
            System.err.println("Warning: JFileChooser cancelled by user");
            this.dispose();
            return null;
        }

        fileName = fc.getSelectedFile();
        if ( (fileName == null) || fileName.getName().equals( "" ) ) {
            this.dispose();
            return null;
        }

        try {
            fullPath = fileName.getCanonicalPath();
        } catch (IOException e) {
            this.dispose();
            e.printStackTrace();
        }
        this.dispose();
        return fullPath;
    }

}
