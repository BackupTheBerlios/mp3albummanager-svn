package org.app.mp3AlbumManager.gui;

import org.app.mp3AlbumManager.util.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class View extends JFrame {

    private static final int APP_WIDTH = 1025,
                             APP_HEIGHT = 550;
    private static final String
            ICON_NEW_16 = "/res/graphics/icon_new-16x16.png",
            ICON_OPEN_16 = "/res/graphics/icon_open-16x16.png",
            ICON_CLOSE_16 = "/res/graphics/icon_close-16x16.png",
            ICON_SEARCH_16 = "/res/graphics/icon_search-16x16.png",
            ICON_EDIT_16 = "/res/graphics/icon_edit-16x16.png",
            ICON_DELETE_16 = "/res/graphics/icon_delete-16x16.png",
            ICON_HTML_16 = "/res/graphics/icon_html-16x16.png",
            ICON_NFO_16 = "/res/graphics/icon_nfo-16x16.png",
            ICON_ABOUT_16 = "/res/graphics/icon_about-16x16.png",
            ICON_NEW_32 = "/res/graphics/icon_new-32x32.png",
            ICON_OPEN_32 = "/res/graphics/icon_open-32x32.png",
            ICON_CLOSE_32 = "/res/graphics/icon_close-32x32.png",
            ICON_SEARCH_32 = "/res/graphics/icon_search-32x32.png",
            ICON_EDIT_32 = "/res/graphics/icon_edit-32x32.png",
            ICON_DELETE_32 = "/res/graphics/icon_delete-32x32.png",
            ICON_HTML_32 = "/res/graphics/icon_html-32x32.png",
            ICON_NFO_32 = "/res/graphics/icon_nfo-32x32.png",
            ICON_ABOUT_32 = "/res/graphics/icon_about-32x32.png";
    private JMenuItem menuItemQuit;
    JMenuItem menuItemNew;
    private JMenuItem menuItemOpen;
    JMenuItem menuItemClose;
    JMenuItem menuItemFind;
    JMenuItem menuItemEdit;
    JMenuItem menuItemDelete;
    JMenuItem menuItemNFO;
    JMenuItem menuItemHTML;
    JMenuItem menuItemAbout;
    JButton newButton;
    private JButton openButton;
    JButton closeButton;
    JButton findButton;
    JButton editButton;
    JButton deleteButton;
    JButton nfoButton;
    JButton htmlButton;
    JButton aboutButton;

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
        JMenu menuEdit = new JMenu();
        menuItemEdit = new JMenuItem();
        menuItemDelete = new JMenuItem();
        JMenu menuSearch = new JMenu();
        menuItemFind = new JMenuItem();
        JMenu menuTools = new JMenu();
        menuItemNFO = new JMenuItem();
        menuItemHTML = new JMenuItem();
        JMenu menuHelp = new JMenu();
        menuItemAbout = new JMenuItem();

        JToolBar toolBar = new JToolBar();
        newButton = new JButton();
        openButton = new JButton();
        closeButton = new JButton();
        findButton = new JButton();
        editButton = new JButton();
        deleteButton = new JButton();
        nfoButton = new JButton();
        htmlButton = new JButton();
        aboutButton = new JButton();

        nfoContent = new JTextArea();

        //======== this ========
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("mp3AlbumManager");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        // Load images
        ImageLoader loader = new ImageLoader();
        ImageIcon imageNew16 = loader.getImage(ICON_NEW_16);
        ImageIcon imageOpen16 = loader.getImage(ICON_OPEN_16);
        ImageIcon imageClose16 = loader.getImage(ICON_CLOSE_16);
        ImageIcon imageSearch16 = loader.getImage(ICON_SEARCH_16);
        ImageIcon imageEdit16 = loader.getImage(ICON_EDIT_16);
        ImageIcon imageDelete16 = loader.getImage(ICON_DELETE_16);
        ImageIcon imageNfo16 = loader.getImage(ICON_NFO_16);
        ImageIcon imageHtml16 = loader.getImage(ICON_HTML_16);
        ImageIcon imageAbout16 = loader.getImage(ICON_ABOUT_16);
        ImageIcon imageNew32 = loader.getImage(ICON_NEW_32);
        ImageIcon imageOpen32 = loader.getImage(ICON_OPEN_32);
        ImageIcon imageClose32 = loader.getImage(ICON_CLOSE_32);
        ImageIcon imageSearch32 = loader.getImage(ICON_SEARCH_32);
        ImageIcon imageEdit32 = loader.getImage(ICON_EDIT_32);
        ImageIcon imageDelete32 = loader.getImage(ICON_DELETE_32);
        ImageIcon imageNfo32 = loader.getImage(ICON_NFO_32);
        ImageIcon imageHtml32 = loader.getImage(ICON_HTML_32);
        ImageIcon imageAbout32 = loader.getImage(ICON_ABOUT_32);
        //======== menuBarMain ========
        {
            //======== menuFile ========
            {
                menuFile.setText("File");
                menuFile.setMnemonic('F');

                //---- menuItemNew ----
                menuItemNew.setText("New");
                menuItemNew.setIcon(imageNew16);
                menuItemNew.setMnemonic('N');
                // CTRL + N
                menuItemNew.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_N, Event.CTRL_MASK) );
                menuFile.add(menuItemNew);

                //---- menuItemOpen ----
                menuItemOpen.setText("Open");
                menuItemOpen.setIcon(imageOpen16);
                menuItemOpen.setMnemonic('O');
                // CTRL + O
                menuItemOpen.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_O, Event.CTRL_MASK) );
                menuFile.add(menuItemOpen);

                //---- menuItemClose ----
                menuItemClose.setText("Close");
                menuItemClose.setIcon(imageClose16);
                menuItemClose.setMnemonic('C');
                // CTRL + W
                menuItemClose.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_W, Event.CTRL_MASK) );
                menuFile.add(menuItemClose);

                //---- menuItemQuit ----
                menuItemQuit.setText("Quit");
                menuItemQuit.setMnemonic('Q');
                // ALT + F4
                menuItemQuit.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_F4, Event.ALT_MASK) );
                menuFile.add(menuItemQuit);
            }
            menuBarMain.add(menuFile);

            //======== menuEdit ========
            {
                menuEdit.setText("Edit");
                menuEdit.setMnemonic('E');

                //---- menuItemEdit ----
                menuItemEdit.setText("Edit");
                menuItemEdit.setIcon(imageEdit16);
                menuItemEdit.setMnemonic('T');
                menuEdit.add(menuItemEdit);

                //---- menuItemOpen ----
                menuItemDelete.setText("Delete");
                menuItemDelete.setIcon(imageDelete16);
                menuItemDelete.setMnemonic('D');
                menuEdit.add(menuItemDelete);
            }
            menuBarMain.add(menuEdit);

            //======== menuSearch ========
            {
                menuSearch.setText("Search");
                menuSearch.setMnemonic('S');

                //---- menuItemFind ----
                menuItemFind.setText("Find");
                menuItemFind.setIcon(imageSearch16);
                menuItemFind.setMnemonic('F');
                // CTRL + F
                menuItemFind.setAccelerator( KeyStroke.getKeyStroke(KeyEvent.VK_F, Event.CTRL_MASK) );
                menuSearch.add(menuItemFind);
            }
            menuBarMain.add(menuSearch);

            //======== menuTools ========
            {
                menuTools.setText("Tools");
                menuTools.setMnemonic('T');

                //---- menuItemNFO ----
                menuItemNFO.setText("Create NFO");
                menuItemNFO.setIcon(imageNfo16);
                menuItemNFO.setMnemonic('R');
                menuTools.add(menuItemNFO);

                //---- menuItemHTML ----
                menuItemHTML.setText("HTML List");
                menuItemHTML.setIcon(imageHtml16);
                menuItemHTML.setMnemonic('L');
                menuTools.add(menuItemHTML);

            }
            menuBarMain.add(menuTools);

            //======== menuHelp ========
            {
                menuHelp.setText("Help");
                menuHelp.setMnemonic('H');

                //---- menuItemAbout ----
                menuItemAbout.setText("About");
                menuItemAbout.setMnemonic('A');
                menuItemAbout.setIcon(imageAbout16);
                menuHelp.add(menuItemAbout);
            }
            menuBarMain.add(menuHelp);
        }
        setJMenuBar(menuBarMain);

        //======== toolBar ========
        {

            //---- newButton ----
            newButton.setText("New");
            newButton.setIcon(imageNew32);
            toolBar.add(newButton);

            //---- openButton ----
            openButton.setText("Open");
            openButton.setIcon(imageOpen32);
            toolBar.add(openButton);

            //---- closeButton ----
            closeButton.setText("Close");
            closeButton.setIcon(imageClose32);
            toolBar.add(closeButton);

            //---- findButton ----
            findButton.setText("Find");
            findButton.setIcon(imageSearch32);
            toolBar.add(findButton);

            //---- editButton ----
            editButton.setText("Edit");
            editButton.setIcon(imageEdit32);
            toolBar.add(editButton);

            //---- deleteButton ----
            deleteButton.setText("Delete");
            deleteButton.setIcon(imageDelete32);
            toolBar.add(deleteButton);

            //---- nfoButton ----
            nfoButton.setText("Create NFO");
            nfoButton.setIcon(imageNfo32);
            toolBar.add(nfoButton);

            //---- htmlButton ----
            htmlButton.setText("HTML List");
            htmlButton.setIcon(imageHtml32);
            toolBar.add(htmlButton);

            //---- aboutButton ----
            aboutButton.setText("About");
            aboutButton.setIcon(imageAbout32);
            toolBar.add(aboutButton);
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

        menuItemFind.addActionListener(al);
        menuItemFind.setActionCommand("search");
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

        findButton.addActionListener(al);
        findButton.setActionCommand("search");
        editButton.addActionListener(al);
        editButton.setActionCommand("edit");
        deleteButton.addActionListener(al);
        deleteButton.setActionCommand("delete");

        nfoButton.addActionListener(al);
        nfoButton.setActionCommand("nfo");
        htmlButton.addActionListener(al);
        htmlButton.setActionCommand("html");

        aboutButton.addActionListener(al);
        aboutButton.setActionCommand("about");

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
        disableMenuItem(menuItemFind);
        disableMenuItem(menuItemEdit);
        disableMenuItem(menuItemDelete);
        disableMenuItem(menuItemNFO);
        disableMenuItem(menuItemHTML);
        enableMenuItem(menuItemAbout);

        disableButton(closeButton);
        disableButton(findButton);
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
