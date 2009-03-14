package org.app.mp3AlbumManager.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;

import org.app.mp3AlbumManager.util.ViewTooltips;
import com.jeta.forms.components.panel.FormPanel;


public class ListPanel extends JPanel {

    public static final String ID_ALBUMLIST = "albumList";  //javax.swing.JList
    public static final String ID_SONGLIST = "songList";  //javax.swing.JList

    // the form panel
    private FormPanel listPanel;

    // panel components
    JList albumList;
    JList songList;
    DefaultListModel albumListModel;
    DefaultListModel songListModel;

    private Color bgcolor;

    public ListPanel(Color color) {

        listPanel = new FormPanel("org/app/mp3AlbumManager/forms/listPanel.jfrm");

        bgcolor = color;
        initComponents();

    }

    private void initComponents() {

        albumList = listPanel.getList(ID_ALBUMLIST);
        songList = listPanel.getList(ID_SONGLIST);

        // view tooltips for full name of albums and songs
        ViewTooltips.register(albumList);
        ViewTooltips.register(songList);

        //======== this ========
        setBackground(bgcolor);

        //---- albumList ----
        albumList.setName("albums");
        albumListModel = new DefaultListModel();
        albumList.setModel(albumListModel); // Why use a listmodel?

        //---- songList ----
        songList.setName("songs");
        songListModel = new DefaultListModel();
        songList.setModel(songListModel);  // Why use a listmodel?

        //LAYOUT
        setLayout( new BorderLayout() );
        add( listPanel, BorderLayout.CENTER );

    }

    public void removeNotify() {
        ViewTooltips.unregister(albumList);
        ViewTooltips.unregister(songList);
        super.removeNotify();
    }

    public void showPanel() {
        setVisible(true);
        super.repaint();
    }

    public void selectionPanelListener(ListSelectionListener lsl) {
        albumList.addListSelectionListener(lsl);
        songList.addListSelectionListener(lsl);
    }

    public void removeElementFromAlbumList(int index) { albumList.remove(index); }

    public void removeElementFromSongList(int index) { songList.remove(index); }

    public String[] getElementsFromAlbumList() {

        int size = albumListModel.getSize();
        String[] albums = new String[size];
        for(int i = 0; i < size; i++) {
            albums[i] = (String) albumListModel.getElementAt(i);
        }
        return albums;
    }
}
