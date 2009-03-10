package org.app.mp3AlbumManager.gui;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;
import org.app.mp3AlbumManager.util.ViewTooltips;


public class ListPanel extends JPanel {

    JList albumList, songList;
    DefaultListModel albumListModel,songListModel;
    private JPanel emptyPanel;
    //String[] albumlistData, songlistData;
    private GroupLayout layout;

    private Color bgcolor;

    public ListPanel(Color color) {

        bgcolor = color;
        initComponents();

    }

    private void initComponents() {

        JLabel albumListLabel = new JLabel("ALBUMS");
        JLabel songListLabel = new JLabel("SONGS");
        JScrollPane albumScrollPane = new JScrollPane();
        JScrollPane songScrollPane = new JScrollPane();

        // Why use a listmodel?
        //albumList = new JList();
        albumListModel = new DefaultListModel();
        albumList = new JList(albumListModel);
        albumList.setName("albums");

        // Why use a listmodel?
        //songList = new JList();
        songListModel = new DefaultListModel();
        songList = new JList(songListModel);
        songList.setName("songs");
        
        // view tooltips for full name of albums and songs
        ViewTooltips.register(albumList);
        ViewTooltips.register(songList);

        //layout manager
        layout = new GroupLayout(this);

        // hidden empty panel, to be replaced på albumInfoPanel / songInfoPanel
        // like this: layout.replace(emptyPanel, albumInfoPanel)
        emptyPanel = new JPanel();
        emptyPanel.setVisible(false);
        layout.setHonorsVisibility(emptyPanel, false);
        
        //======== this ========
        setBackground(bgcolor);

        //---- albumListLabel ----
        albumListLabel.setLabelFor(albumList);

        //---- songListLabel ----
        songListLabel.setLabelFor(songList);

        //======== albumScrollPane ========
        {

            //---- albumList ----
            albumList.setFont(new Font("Dialog", Font.PLAIN, 12));
            albumList.setPrototypeCellValue("MINIMUM WIDTH FOR AN ITEM");
            albumList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            albumList.setVisibleRowCount(16);
            albumScrollPane.setViewportView(albumList);
        }

        //======== songScrollPane ========
        {

            //---- songList ----
            songList.setFont(new Font("Dialog", Font.PLAIN, 12));
            songList.setPrototypeCellValue("MINIMUM WIDTH FOR AN ITEM");
            songList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            songList.setVisibleRowCount(16);
            songList.setFixedCellHeight(17);
            songScrollPane.setViewportView(songList);
        }

        //LAYOUT
        setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup()
                .add(layout.createSequentialGroup()
                    .addContainerGap()
                    .add(layout.createParallelGroup()
                        .add(albumListLabel)
                        .add(albumScrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    )
                    .addPreferredGap(LayoutStyle.RELATED)
                    .add(layout.createParallelGroup()
                        .add(songScrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .add(songListLabel)
                    )
                    .addPreferredGap(LayoutStyle.RELATED)
                    .add(emptyPanel)
                    .addContainerGap()
            )
        );
        layout.setVerticalGroup(
            layout.createParallelGroup()
                .add(layout.createSequentialGroup()
                    .add(layout.createParallelGroup(GroupLayout.BASELINE)
                        .add(albumListLabel)
                        .add(songListLabel)
                    )
                    .addPreferredGap(LayoutStyle.RELATED)
                    .add(layout.createParallelGroup()
                        .add(songScrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .add(albumScrollPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    )
                    .addPreferredGap(LayoutStyle.RELATED)

                )
                .add(layout.createSequentialGroup()
                    .add(emptyPanel)
                    .addPreferredGap(LayoutStyle.RELATED)
                    //.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                )


        );

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
