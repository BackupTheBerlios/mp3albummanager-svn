/*
 * Created by JFormDesigner on Tue Feb 24 14:59:19 CET 2009
 */

package org.app.mp3AlbumManager.gui;

import org.app.mp3AlbumManager.util.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * The main view of the GUI.
 */
public class View extends JFrame {

    private JMenuItem menuItemQuit;
    JMenuItem menuItemNew;
    private JMenuItem menuItemOpen;
    JMenuItem menuItemClose;
    private JMenuItem menuItemList;
    JMenuItem menuItemSearch;
    JMenuItem menuItemEdit;
    JMenuItem menuItemDelete;
    JMenuItem menuItemNFO;
    JMenuItem menuItemHTML;
    JButton newButton;
    private JButton openButton;
    JButton closeButton;
    private JButton listButton;
    JButton searchButton;
    JButton editButton;
    JButton deleteButton;
    JButton nfoButton;
    JButton htmlButton;

    public View() {

        initComponents();
    }

    /**
     * Intitialize components.
     */
    private void initComponents() {

        JMenuBar menuBarMain = new JMenuBar();
        JMenu menuFile = new JMenu();
        menuItemQuit = new JMenuItem();
        menuItemNew = new JMenuItem();
        menuItemOpen = new JMenuItem();
        menuItemClose = new JMenuItem();
        JMenu menuDatabase = new JMenu();
        menuItemList = new JMenuItem();
        menuItemSearch = new JMenuItem();
        JMenu menuAlbum = new JMenu();
        menuItemEdit = new JMenuItem();
        menuItemDelete = new JMenuItem();
        menuItemNFO = new JMenuItem();
        menuItemHTML = new JMenuItem();

        JToolBar toolBar = new JToolBar();
        newButton = new JButton();
        openButton = new JButton();
        closeButton = new JButton();
        listButton = new JButton();
        searchButton = new JButton();
        editButton = new JButton();
        deleteButton = new JButton();
        nfoButton = new JButton();
        htmlButton = new JButton();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("mp3AlbumManager");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        // Load images
        ImageLoader loader = new ImageLoader();
        ImageIcon imageNew = loader.getImage("/res/graphics/icons/database_add.png");
        ImageIcon imageOpen = loader.getImage("/res/graphics/icons/database_go.png");
        ImageIcon imageClose = loader.getImage("/res/graphics/icons/database_delete.png");
        ImageIcon imageList = loader.getImage("/res/graphics/icons/application_view_list.png");
        ImageIcon imageSearch = loader.getImage("/res/graphics/icons/find.png");
        ImageIcon imageEdit = loader.getImage("/res/graphics/icons/cd_edit.png");
        ImageIcon imageDelete = loader.getImage("/res/graphics/icons/cd_delete.png");
        ImageIcon imageNfo = loader.getImage("/res/graphics/icons/page_white_text.png");
        ImageIcon imageHtml = loader.getImage("/res/graphics/icons/html.png");

        //======== menuBarMain ========
        {
            //======== menuFile ========
            {
                menuFile.setText("File");
                menuFile.setMnemonic('F');

                //---- menuItemNew ----
                menuItemNew.setText("New");
                //menuItemNew.setIcon(new ImageIcon("/res/graphics/icons/database_add.png"));
                menuItemNew.setIcon(imageNew);
                menuItemNew.setMnemonic('N');
                menuFile.add(menuItemNew);

                //---- menuItemOpen ----
                menuItemOpen.setText("Open");
                //menuItemOpen.setIcon(new ImageIcon("/res/graphics/icons/database_go.png"));
                menuItemOpen.setIcon(imageOpen);
                menuItemOpen.setMnemonic('O');
                menuFile.add(menuItemOpen);

                //---- menuItemClose ----
                menuItemClose.setText("Close");
                //menuItemClose.setIcon(new ImageIcon("/res/graphics/icons/database_delete.png"));
                menuItemClose.setIcon(imageClose);
                menuItemClose.setMnemonic('C');
                menuFile.add(menuItemClose);

                //---- menuItemQuit ----
                menuItemQuit.setText("Quit");
                menuItemQuit.setMnemonic('Q');
                menuFile.add(menuItemQuit);
            }
            menuBarMain.add(menuFile);

            //======== menuDatabase ========
            {
                menuDatabase.setText("Database");
                menuDatabase.setMnemonic('B');

                //---- menuItemList ----
                menuItemList.setText("List");
                //menuItemList.setIcon(new ImageIcon("/res/graphics/icons/application_view_list.png"));
                menuItemList.setIcon(imageList);
                menuItemList.setMnemonic('L');
                menuDatabase.add(menuItemList);

                //---- menuItemSearch ----
                menuItemSearch.setText("Search");
                //menuItemSearch.setIcon(new ImageIcon("/res/graphics/icons/find.png"));
                menuItemSearch.setIcon(imageSearch);
                menuItemSearch.setMnemonic('S');
                menuDatabase.add(menuItemSearch);
            }
            menuBarMain.add(menuDatabase);

            //======== menuAlbum ========
            {
                menuAlbum.setText("Album");
                menuAlbum.setMnemonic('A');

                //---- menuItemEdit ----
                menuItemEdit.setText("Edit");
                //menuItemEdit.setIcon(new ImageIcon("/res/graphics/icons/cd_edit.png"));
                menuItemEdit.setIcon(imageEdit);
                menuItemEdit.setMnemonic('E');
                menuAlbum.add(menuItemEdit);

                //---- menuItemOpen ----
                menuItemDelete.setText("Delete");
                //menuItemDelete.setIcon(new ImageIcon("/res/graphics/icons/cd_delete.png"));
                menuItemDelete.setIcon(imageDelete);
                menuItemDelete.setMnemonic('D');
                menuAlbum.add(menuItemDelete);

                //---- menuItemNFO ----
                menuItemNFO.setText("Album NFO");
                //menuItemNFO.setIcon(new ImageIcon("/res/graphics/icons/page_white_text.png"));
                menuItemNFO.setIcon(imageNfo);
                menuItemNFO.setMnemonic('A');
                menuAlbum.add(menuItemNFO);

                //---- menuItemHTML ----
                menuItemHTML.setText("HTML List");
                //menuItemHTML.setIcon(new ImageIcon("/res/graphics/icons/html.png"));
                menuItemHTML.setIcon(imageHtml);
                menuItemHTML.setMnemonic('H');
                menuAlbum.add(menuItemHTML);
            }
            menuBarMain.add(menuAlbum);
        }
        setJMenuBar(menuBarMain);

        //======== toolBar ========
        {

            //---- newButton ----
            newButton.setText("New");
            newButton.setMnemonic('N');
            //newButton.setIcon(new ImageIcon("/res/graphics/icons/database_add.png"));
            newButton.setIcon(imageNew);
            toolBar.add(newButton);

            //---- openButton ----
            openButton.setText("Open");
            openButton.setMnemonic('O');
            //openButton.setIcon(new ImageIcon("/res/graphics/icons/database_go.png"));
            openButton.setIcon(imageOpen);
            toolBar.add(openButton);

            //---- closeButton ----
            closeButton.setText("Close");
            closeButton.setMnemonic('C');
            //closeButton.setIcon(new ImageIcon("/res/graphics/icons/database_delete.png"));
            closeButton.setIcon(imageClose);
            toolBar.add(closeButton);

            //---- listButton ----
            listButton.setText("List");
            listButton.setMnemonic('L');
            //listButton.setIcon(new ImageIcon("/res/graphics/icons/application_view_list.png"));
            listButton.setIcon(imageList);
            toolBar.add(listButton);

            //---- searchButton ----
            searchButton.setText("Search");
            searchButton.setMnemonic('S');
            //searchButton.setIcon(new ImageIcon("/res/graphics/icons/find.png"));
            searchButton.setIcon(imageSearch);
            toolBar.add(searchButton);

            //---- editButton ----
            editButton.setText("Edit");
            editButton.setMnemonic('E');
            //editButton.setIcon(new ImageIcon("/res/graphics/icons/cd_edit.png"));
            editButton.setIcon(imageEdit);
            toolBar.add(editButton);

            //---- deleteButton ----
            deleteButton.setText("Delete");
            deleteButton.setMnemonic('D');
            //deleteButton.setIcon(new ImageIcon("/res/graphics/icons/cd_delete.png"));
            deleteButton.setIcon(imageDelete);
            toolBar.add(deleteButton);

            //---- nfoButton ----
            nfoButton.setText("Album NFO");
            nfoButton.setMnemonic('A');
            //nfoButton.setIcon(new ImageIcon("/res/graphics/icons/page_white_text.png"));
            nfoButton.setIcon(imageNfo);
            toolBar.add(nfoButton);

            //---- htmlButton ----
            htmlButton.setText("HTML List");
            htmlButton.setMnemonic('H');
            //htmlButton.setIcon(new ImageIcon("/res/graphics/icons/html.png"));
            htmlButton.setIcon(imageHtml);
            toolBar.add(htmlButton);
        }
        contentPane.add(toolBar, BorderLayout.NORTH);

        setSize(825, 500); // 640, 800
        setLocationRelativeTo(getOwner());
        setVisible(true);
    }

    /**
     * Add an ActionListener to components.
     * @param al The ActionListener.
     */
    public void menuActionListeners(ActionListener al) {

        menuItemNew.addActionListener(al);
        menuItemNew.setActionCommand("new");
        menuItemOpen.addActionListener(al);
        menuItemOpen.setActionCommand("open");
        menuItemClose.addActionListener(al);
        menuItemClose.setActionCommand("close");
        menuItemQuit.addActionListener(al);
        menuItemQuit.setActionCommand("quit");

        menuItemList.addActionListener(al);
        menuItemList.setActionCommand("list");
        menuItemSearch.addActionListener(al);
        menuItemSearch.setActionCommand("search");
        menuItemEdit.addActionListener(al);
        menuItemEdit.setActionCommand("edit");
        menuItemDelete.addActionListener(al);
        menuItemDelete.setActionCommand("delete");

        menuItemNFO.addActionListener(al);
        menuItemNFO.setActionCommand("nfo");
        menuItemHTML.addActionListener(al);
        menuItemHTML.setActionCommand("html");
        
        newButton.addActionListener(al);
        newButton.setActionCommand("new");
        openButton.addActionListener(al);
        openButton.setActionCommand("open");
        closeButton.addActionListener(al);
        closeButton.setActionCommand("close");

        listButton.addActionListener(al);
        listButton.setActionCommand("list");
        searchButton.addActionListener(al);
        searchButton.setActionCommand("search");
        editButton.addActionListener(al);
        editButton.setActionCommand("edit");
        deleteButton.addActionListener(al);
        deleteButton.setActionCommand("delete");

        nfoButton.addActionListener(al);
        nfoButton.setActionCommand("nfo");
        htmlButton.addActionListener(al);
        htmlButton.setActionCommand("html");

    }

    /**
     * Add a panel to the view.
     * @param p The panel.
     */
    public void addPanel(JPanel p) {
        add(p, BorderLayout.CENTER);
        validate();
    }

    /**
     * Add a panel to the view.
     * Use BorderLayout constraints to place several panels.
     * @param p The panel.
     * @param constraints The BorderLayout constraint.
     */
    public void addPanel(JPanel p, Object constraints) {
        add(p, constraints);
        validate();
    }

    /**
     * Get a preformatted JTextArea with supplied text.
     * @param content The text content.
     * @return The JTextArea component.
     */
    public JTextArea setTextAreaContent(String content) {
        JTextArea nfoContent = new JTextArea();
        nfoContent.setText(content);
        nfoContent.setBackground(Color.black);
        nfoContent.setForeground(Color.white);
        nfoContent.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12) );
        nfoContent.setMargin(new Insets(5, 5, 5, 5));
        return nfoContent;
    }

    /**
     * Intialize menu items and buttons.
     * @param anyRecent Whether any recent database entries exists.
     */
    public void initializeMenu(boolean anyRecent) {

        if( ! anyRecent) {
            disableMenuItem(menuItemOpen);
            disableButton(openButton);
        } else {
            enableMenuItem(menuItemOpen);
            enableButton(openButton);
        }
        disableMenuItem(menuItemClose);
        disableMenuItem(menuItemList);
        disableMenuItem(menuItemSearch);
        disableMenuItem(menuItemEdit);
        disableMenuItem(menuItemDelete);
        disableMenuItem(menuItemNFO);
        disableMenuItem(menuItemHTML);

        disableButton(closeButton);
        disableButton(listButton);
        disableButton(searchButton);
        disableButton(editButton);
        disableButton(deleteButton);
        disableButton(nfoButton);
        disableButton(htmlButton);
    }

    /**
     * Disable a menu item.
     * @param item The menu item.
     */
    public void disableMenuItem(JMenuItem item) { item.setEnabled(false); }

    /**
     * Enable a menu item.
     * @param item The menu item.
     */
    public void enableMenuItem(JMenuItem item) { item.setEnabled(true); }

    /**
     * Disable a button.
     * @param button The button.
     */
    public void disableButton(JButton button) { button.setEnabled(false); }

    /**
     * Enable a button.
     * @param button The button.
     */
    public void enableButton(JButton button) { button.setEnabled(true); }
    
}
