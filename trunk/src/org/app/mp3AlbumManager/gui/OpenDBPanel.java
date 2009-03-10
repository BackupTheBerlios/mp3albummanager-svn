/*
 * Created by JFormDesigner on Tue Feb 24 14:48:09 CET 2009
 */

package org.app.mp3AlbumManager.gui;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

/**
 * @author Georg Wallin
 */
public class OpenDBPanel extends JPanel {

    private JTextField userField, passField, mp3DirField, dbDirField;
    private JComboBox databaseBox;
    private JButton openButton;
    private JLabel messageLabel;

    private Color bgcolor;

    public OpenDBPanel(Color color) {

        bgcolor = color;
        initComponents();
    }

    private void initComponents() {

        JLabel headerLabel = new JLabel("OPEN DATABASE");
        JLabel userLabel = new JLabel("Username");
        JLabel passLabel = new JLabel("Password");
        JLabel mp3DirLabel = new JLabel("Albums directory");
        JLabel databaseLabel = new JLabel("Database");
        JLabel dbDirLabel = new JLabel("Database directory");
        messageLabel = new JLabel();
        messageLabel.setForeground(Color.red);

        userField = new JTextField();
        passField = new JTextField();
        databaseBox = new JComboBox();

        mp3DirField = new JTextField();
        dbDirField = new JTextField();
        openButton = new JButton("OPEN");

        //======== this ========
        setBackground(bgcolor);

        //---- headerLabel ----
        headerLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 14));

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
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup()
                .add(layout.createSequentialGroup()
                    .addContainerGap()
                    .add(layout.createParallelGroup(GroupLayout.TRAILING)
                        .add(openButton)
                        .add(layout.createSequentialGroup()
                            .add(layout.createParallelGroup(GroupLayout.TRAILING, false)
                                .add(GroupLayout.LEADING, dbDirLabel)
                                .add(GroupLayout.LEADING, mp3DirLabel)
                                .add(GroupLayout.LEADING, userLabel, GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                                .add(GroupLayout.LEADING, passLabel, GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                                .add(GroupLayout.LEADING, databaseLabel, GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                            )
                            .add(8, 8, 8)
                            .add(layout.createParallelGroup(GroupLayout.TRAILING, false)
                                .add(dbDirField)
                                .add(mp3DirField)
                                .add(databaseBox, GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                                .add(userField)
                                .add(GroupLayout.LEADING, passField))
                            )
                    )
                    .addContainerGap(18, Short.MAX_VALUE))
                .add(GroupLayout.LEADING, layout.createSequentialGroup()
                    .addContainerGap() // GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE
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
                        .add(databaseBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    )
                    .addPreferredGap(LayoutStyle.UNRELATED)
                    .add(layout.createParallelGroup(GroupLayout.BASELINE)
                        .add(userLabel)
                        .add(userField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    )
                    .addPreferredGap(LayoutStyle.UNRELATED)
                    .add(layout.createParallelGroup(GroupLayout.BASELINE)
                        .add(passLabel)
                        .add(passField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    )
                    .addPreferredGap(LayoutStyle.RELATED)
                    .add(layout.createParallelGroup(GroupLayout.TRAILING)
                        .add(mp3DirLabel)
                        .add(mp3DirField, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
                    )
                    .add(layout.createParallelGroup(GroupLayout.TRAILING)
                        .add(dbDirLabel)
                        .add(dbDirField, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
                    )
                    .addPreferredGap(LayoutStyle.UNRELATED)
                    .add(openButton)
                    .addContainerGap(23, Short.MAX_VALUE)
                    .add(messageLabel)
                )
        );
    }

    public void showPanel() {
        setVisible(true);
        super.repaint();
    }



    public void populateComboBox(String[] arr) {
        // populate combobox with string array
        databaseBox.setMaximumRowCount(arr.length);
        for(String val : arr) { databaseBox.addItem(val); }
        // autoselect the first item
        databaseBox.setSelectedIndex(0);
    }

    public String getSelectedDatabase() {
        return (String) databaseBox.getSelectedItem();
    }

    public void setMp3DirField(String text) { mp3DirField.setText(text); }

    public String getMp3DirField() { return mp3DirField.getText(); }

    public void setDBDirField(String text) { dbDirField.setText(text); }

    public String getDBDirField() { return dbDirField.getText(); }

    public String getUserField() { return userField.getText(); }

    public String getPassField() { return passField.getText(); }

    public void setMessageLabel(String text) { messageLabel.setText(text); }

    public void openDBButtonListener(ActionListener al) {
        openButton.addActionListener(al);
        openButton.setActionCommand("openDB");
    }
    public void openDBItemListener(ItemListener il) {
        databaseBox.addItemListener(il);
    }


}
