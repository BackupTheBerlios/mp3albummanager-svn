package org.app.mp3AlbumManager.gui;

import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.LayoutStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class InfoDBPanel extends JPanel {


    JButton updateButton;
    private JButton nextButton;
    private JLabel dbLabel;
    private JLabel albumLabel;
    private JLabel messageLabel;
    private final String dbLabelText;
    private final String albumLabelText;

    public InfoDBPanel(String dbLabelText, String albumLabelText) {

        this.dbLabelText = dbLabelText;
        this.albumLabelText = albumLabelText;

        initComponents();
    }

    private void initComponents() {

        JLabel headerLabel = new JLabel("DATABASE INFO");

        dbLabel = new JLabel();
        albumLabel = new JLabel();

        updateButton = new JButton("UPDATE");
        nextButton = new JButton("NEXT");
        messageLabel = new JLabel();
        messageLabel.setForeground(Color.red);


        //======== this ========
        setBackground(Color.lightGray);

        //---- headerLabel ----
        headerLabel.setFont(new Font("DejaVu Sans", Font.BOLD, 14));

        //---- dbLabel ----
        dbLabel.setText(dbLabelText);

        //---- albumLabel ----
        albumLabel.setText(albumLabelText);

        //---- updateButton ----
        updateButton.setVisible(false);


        //LAYOUT
        GroupLayout layout = new GroupLayout(this);
        // provide space for invisible synch button
        layout.setHonorsVisibility(updateButton, false);

        setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup()
                .add(layout.createSequentialGroup()
                    .addContainerGap()
                    .add(layout.createParallelGroup()
                        .add(headerLabel)
                        .add(dbLabel)
                        .add(layout.createParallelGroup(GroupLayout.TRAILING)
                            .add(layout.createSequentialGroup()
                                .add(updateButton)
                                .addPreferredGap(LayoutStyle.RELATED)
                                .add(nextButton))
                            .add(albumLabel)
                        )
                    )
                    .addContainerGap(208, Short.MAX_VALUE)
            )
            .add(messageLabel)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup()
                .add(layout.createSequentialGroup()
                    .addContainerGap()
                    .add(headerLabel)
                    .addPreferredGap(LayoutStyle.RELATED)
                    .add(dbLabel)
                    .addPreferredGap(LayoutStyle.RELATED)
                    .add(albumLabel)
                    .addPreferredGap(LayoutStyle.RELATED)
                    .add(layout.createParallelGroup(GroupLayout.BASELINE)
                        .add(nextButton)
                        .add(updateButton))
                    .addContainerGap(204, Short.MAX_VALUE)
                    )
                    .add(messageLabel)
        );
    }

    public void showPanel() {
        setVisible(true);
        super.repaint();
    }

    public void infoDBListener(ActionListener al) {
        nextButton.addActionListener(al);
        nextButton.setActionCommand("next");
        updateButton.addActionListener(al);
        updateButton.setActionCommand("update");
    }

    public void hideButton(JButton button) {
        button.setVisible(false);
    }
    public void showButton(JButton button) {
        button.setVisible(true);
    }

    public void setDBLabelText(String text) { dbLabel.setText(text); }

}
