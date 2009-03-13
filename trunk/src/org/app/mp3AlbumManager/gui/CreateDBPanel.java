/*
 * Created by JFormDesigner on Tue Feb 24 14:40:17 CET 2009
 */

package org.app.mp3AlbumManager.gui;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;


/**
 * @author Georg Wallin
 */
public class CreateDBPanel extends JPanel {

    private JTextField databaseField;
    private JTextField userField;
    private JTextField passField;
    private JButton browseMP3Button;
    private JTextField mp3DirField;
    private JButton browseDBButton;
    private JTextField dbDirField;
    private JButton createButton;
    private JLabel messageLabel;
    private Color bgcolor;
    
    public CreateDBPanel(Color color) {

        bgcolor = color;
        initComponents();
    }

    private void initComponents() {

        JLabel headerLabel = new JLabel("NEW DATABASE");
        JLabel databaseLabel = new JLabel("Database");
        JLabel userLabel = new JLabel("Username");
        JLabel passLabel = new JLabel("Password");
        databaseField = new JTextField();
        userField = new JTextField();
        passField = new JTextField();
        browseMP3Button = new JButton("Browse...");
        mp3DirField = new JTextField("<albums directory>");
        browseDBButton = new JButton("Browse...");
        dbDirField = new JTextField("<database directory>");
        createButton = new JButton("CREATE");
        messageLabel = new JLabel();
        messageLabel.setForeground(Color.red);

        //======== this ========
        setBackground(bgcolor);
        setPreferredSize( new Dimension(375, 260) );
        setMinimumSize( new Dimension(375, 260) );
        setMaximumSize( new Dimension(375, 260) );

        //---- headerLabel ----
        headerLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 14));

        //---- databaseField ----
        databaseField.setBackground(Color.white);

        //---- userField ----
        userField.setBackground(Color.white);

        //---- passField ----
        passField.setBackground(Color.white);

        //---- mp3DirField ----
        mp3DirField.setBackground(Color.white);
        mp3DirField.setEditable(false);

        //---- dbDirField ----
        dbDirField.setBackground(Color.white);
        dbDirField.setEditable(false);

        //LAYOUT
        /*
        setLayout(new GridBagLayout());
        ((GridBagLayout)getLayout()).columnWidths = new int[] {58, 199, 0, 0};
        ((GridBagLayout)getLayout()).rowHeights = new int[] {31, 29, 28, 29, 29, 29, 24, 0};
        ((GridBagLayout)getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0, 1.0E-4};
        ((GridBagLayout)getLayout()).rowWeights = new double[] {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0E-4};

        //---- headerLabel ----
        headerLabel.setText("NEW DATABASE");
        headerLabel.setFont(new Font("Dialog", Font.BOLD, 14));
        add(headerLabel, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 5, 0), 0, 0));

        //---- databaseLabel ----
        databaseLabel.setText("Database");
        databaseLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(databaseLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 5, 5), 0, 0));
        add(databaseField, new GridBagConstraints(1, 1, 2, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 5, 0), 0, 0));

        //---- userLabel ----
        userLabel.setText("Username");
        userLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(userLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 5, 5), 0, 0));
        add(userField, new GridBagConstraints(1, 2, 2, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 5, 0), 0, 0));

        //---- passLabel ----
        passLabel.setText("Password");
        passLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        add(passLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 5, 5), 0, 0));
        add(passField, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 5, 0), 0, 0));

        //---- browseMP3Bbutton ----
        browseMP3Button.setText("Browse...");
        add(browseMP3Button, new GridBagConstraints(0, 4, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 5, 5), 0, 0));
        add(mp3DirField, new GridBagConstraints(1, 4, 2, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 5, 0), 0, 0));

        //---- browseDBBbutton ----
        browseDBButton.setText("Browse...");
        add(browseDBButton, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 5, 5), 0, 0));
        add(dbDirField, new GridBagConstraints(1, 5, 2, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 5, 0), 0, 0));

        //---- createButton ----
        createButton.setText("CREATE");
        add(createButton, new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.BOTH,
            new Insets(0, 0, 0, 0), 0, 0));

        //---- messageLabel ----
        messageLabel.setText("messageLabel");
        messageLabel.setForeground(Color.red);

       //---- messageLabel ----
       add(messageLabel);
        */

        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup()
                .add(layout.createSequentialGroup()
                    .addContainerGap()
                    .add(layout.createParallelGroup(GroupLayout.TRAILING)
                        .add(createButton)
                        .add(layout.createSequentialGroup()
                            .add(layout.createParallelGroup(GroupLayout.TRAILING, false)
                                .add(GroupLayout.LEADING, browseDBButton)
                                .add(GroupLayout.LEADING, browseMP3Button)
                                .add(GroupLayout.LEADING, userLabel, GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                                .add(GroupLayout.LEADING, passLabel, GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                                .add(GroupLayout.LEADING, databaseLabel, GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                            .add(8, 8, 8)
                            .add(layout.createParallelGroup(GroupLayout.TRAILING, false)
                                .add(dbDirField)
                                .add(mp3DirField)
                                .add(databaseField)
                                .add(userField)
                                .add(GroupLayout.LEADING, passField))
                            )
                    )
                    .addContainerGap(18, Short.MAX_VALUE)
                )
                .add(GroupLayout.LEADING, layout.createSequentialGroup()
                    .addContainerGap()
                    .add(headerLabel)
                    .addContainerGap()
                )
                .add(messageLabel)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup()
                .add(layout.createSequentialGroup()
                    .addContainerGap()
                    .add(headerLabel)
                    .addPreferredGap(LayoutStyle.RELATED)
                    .add(layout.createParallelGroup(GroupLayout.BASELINE)
                        .add(databaseLabel)
                        .add(databaseField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.UNRELATED)
                    .add(layout.createParallelGroup(GroupLayout.BASELINE)
                        .add(userLabel)
                        .add(userField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.UNRELATED)
                    .add(layout.createParallelGroup(GroupLayout.BASELINE)
                        .add(passLabel)
                        .add(passField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.RELATED)
                    .add(layout.createParallelGroup(GroupLayout.TRAILING)
                        .add(browseMP3Button)
                        .add(mp3DirField, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.RELATED)
                    .add(layout.createParallelGroup(GroupLayout.TRAILING)
                        .add(browseDBButton)
                        .add(dbDirField, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.UNRELATED)
                    .add(createButton)
                    .addContainerGap(23, Short.MAX_VALUE)
                    .add(messageLabel)
            )
        );

    }

    public void showPanel() {
        setVisible(true);
        super.repaint();
    }

    public void createDBListener(ActionListener al) {
        createButton.addActionListener(al);
        createButton.setActionCommand("createDB");
        browseMP3Button.addActionListener(al);
        browseMP3Button.setActionCommand("browseMP3dir");
        browseDBButton.addActionListener(al);
        browseDBButton.setActionCommand("browseDBdir");
    }

    public String getDatabaseField() { return databaseField.getText(); }

    public void setMp3DirField(String text) { mp3DirField.setText(text); }

    public String getMp3DirField() { return mp3DirField.getText(); }

    public void setDBDirField(String text) { dbDirField.setText(text); }

    public String getDBDirField() { return dbDirField.getText(); }

    public String getUserField() { return userField.getText(); }

    public String getPassField() { return passField.getText(); }

    public void setMessageLabel(String text) { messageLabel.setText(text); }
}
