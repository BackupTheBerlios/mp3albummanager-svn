package org.app.mp3AlbumManager.gui;

import org.app.mp3AlbumManager.util.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class View extends JFrame {

    private static final int APP_WIDTH = 1010,
                             APP_HEIGHT = 520;

    private JMenuItem menuItemQuit;
    JMenuItem menuItemNew;
    private JMenuItem menuItemOpen;
    JMenuItem menuItemClose;
    JMenuItem menuItemSearch;
    JMenuItem menuItemEdit;
    JMenuItem menuItemDelete;
    JMenuItem menuItemNFO;
    JMenuItem menuItemHTML;
    JMenuItem menuItemAbout;
    JButton newButton;
    private JButton openButton;
    JButton closeButton;
    JButton searchButton;
    JButton editButton;
    JButton deleteButton;
    JButton nfoButton;
    JButton htmlButton;

    JTextArea nfoContent;

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
        menuItemSearch = new JMenuItem();
        JMenu menuAlbum = new JMenu();
        menuItemEdit = new JMenuItem();
        menuItemDelete = new JMenuItem();
        menuItemNFO = new JMenuItem();
        menuItemHTML = new JMenuItem();
        JMenu menuHelp = new JMenu();
        menuItemAbout = new JMenuItem();

        JToolBar toolBar = new JToolBar();
        newButton = new JButton();
        openButton = new JButton();
        closeButton = new JButton();
        searchButton = new JButton();
        editButton = new JButton();
        deleteButton = new JButton();
        nfoButton = new JButton();
        htmlButton = new JButton();

        nfoContent = new JTextArea();

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
                menuItemNew.setIcon(imageNew);
                menuItemNew.setMnemonic('N');
                menuFile.add(menuItemNew);

                //---- menuItemOpen ----
                menuItemOpen.setText("Open");
                menuItemOpen.setIcon(imageOpen);
                menuItemOpen.setMnemonic('O');
                menuFile.add(menuItemOpen);

                //---- menuItemClose ----
                menuItemClose.setText("Close");
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

                //---- menuItemSearch ----
                menuItemSearch.setText("Search");
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
                menuItemEdit.setIcon(imageEdit);
                menuItemEdit.setMnemonic('E');
                menuAlbum.add(menuItemEdit);

                //---- menuItemOpen ----
                menuItemDelete.setText("Delete");
                menuItemDelete.setIcon(imageDelete);
                menuItemDelete.setMnemonic('D');
                menuAlbum.add(menuItemDelete);

                //---- menuItemNFO ----
                menuItemNFO.setText("Album NFO");
                menuItemNFO.setIcon(imageNfo);
                menuItemNFO.setMnemonic('A');
                menuAlbum.add(menuItemNFO);

                //---- menuItemHTML ----
                menuItemHTML.setText("HTML List");
                menuItemHTML.setIcon(imageHtml);
                menuItemHTML.setMnemonic('H');
                menuAlbum.add(menuItemHTML);
            }
            menuBarMain.add(menuAlbum);

            //======== menuHelp ========
            {
                menuHelp.setText("Help");
                menuHelp.setMnemonic('H');

                //---- menuItemAbout ----
                menuItemAbout.setText("About");
                menuItemAbout.setMnemonic('A');
                menuHelp.add(menuItemAbout);
            }
            menuBarMain.add(menuHelp);
        }
        setJMenuBar(menuBarMain);

        //======== toolBar ========
        {

            //---- newButton ----
            newButton.setText("New");
            newButton.setMnemonic('N');
            newButton.setIcon(imageNew);
            toolBar.add(newButton);

            //---- openButton ----
            openButton.setText("Open");
            openButton.setMnemonic('O');
            openButton.setIcon(imageOpen);
            toolBar.add(openButton);

            //---- closeButton ----
            closeButton.setText("Close");
            closeButton.setMnemonic('C');
            closeButton.setIcon(imageClose);
            toolBar.add(closeButton);

            //---- searchButton ----
            searchButton.setText("Search");
            searchButton.setMnemonic('S');
            searchButton.setIcon(imageSearch);
            toolBar.add(searchButton);

            //---- editButton ----
            editButton.setText("Edit");
            editButton.setMnemonic('E');
            editButton.setIcon(imageEdit);
            toolBar.add(editButton);

            //---- deleteButton ----
            deleteButton.setText("Delete");
            deleteButton.setMnemonic('D');
            deleteButton.setIcon(imageDelete);
            toolBar.add(deleteButton);

            //---- nfoButton ----
            nfoButton.setText("Album NFO");
            nfoButton.setMnemonic('A');
            nfoButton.setIcon(imageNfo);
            toolBar.add(nfoButton);

            //---- htmlButton ----
            htmlButton.setText("HTML List");
            htmlButton.setMnemonic('H');
            htmlButton.setIcon(imageHtml);
            toolBar.add(htmlButton);
        }
        contentPane.add(toolBar, BorderLayout.NORTH);

        setSize(APP_WIDTH, APP_HEIGHT); // 640, 800
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

        menuItemAbout.addActionListener(al);
        menuItemAbout.setActionCommand("about");

        newButton.addActionListener(al);
        newButton.setActionCommand("new");
        openButton.addActionListener(al);
        openButton.setActionCommand("open");
        closeButton.addActionListener(al);
        closeButton.setActionCommand("close");

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
        nfoContent.setText(content);
        nfoContent.setBackground(Color.black);
        nfoContent.setForeground(Color.white);
        nfoContent.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12) );
        nfoContent.setMargin(new Insets(5, 5, 5, 5));
        return nfoContent;
    }

    /**
     * Get the text of the JTextArea.
     * @return the text of the JTextArea as a StringBuffer.
     */
    public StringBuffer getTextAreaContent() {
        StringBuffer buf = new StringBuffer();
        buf.append( nfoContent.getText() );
        return buf;   
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
        disableMenuItem(menuItemSearch);
        disableMenuItem(menuItemEdit);
        disableMenuItem(menuItemDelete);
        disableMenuItem(menuItemNFO);
        disableMenuItem(menuItemHTML);
        enableMenuItem(menuItemAbout);

        disableButton(closeButton);
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
