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
    private final String dbLabelText;
    private final String albumLabelText;
    private Color bgcolor;

    public JProgressBar progressBar;

    /**
     * Create a info panel.
     * @param dbLabelText the text of the dbLabel (number of songs in the database).
     * @param albumLabelText the text of the albumLabel (number of songs in the album directory).
     * @param color the background color.
     */
    public InfoDBPanel(String dbLabelText, String albumLabelText, Color color) {

        this.dbLabelText = dbLabelText;
        this.albumLabelText = albumLabelText;

        bgcolor = color;

        initComponents();
    }

    /**
     * Intitialize components in the panel.
     */
    private void initComponents() {

        JLabel headerLabel = new JLabel("DATABASE INFO");

        dbLabel = new JLabel();
        albumLabel = new JLabel();

        updateButton = new JButton("UPDATE");
        nextButton = new JButton("NEXT");

        progressBar = new JProgressBar();
        //progressBar.setValue(0);
        progressBar.setStringPainted(true);
        //progressBar.setIndeterminate(true);
        progressBar.setVisible(false);


        //======== this ========
        setBackground(bgcolor);

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
            .add(progressBar)
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
                    .add(progressBar)
                )

        );
    }

    /**
     * Set listener and action command for the buttons.
     * @param al the listener.
     */
    public void infoDBListener(ActionListener al) {
        nextButton.addActionListener(al);
        nextButton.setActionCommand("next");
        updateButton.addActionListener(al);
        updateButton.setActionCommand("update");
    }

    /**
     * Make the panel visible.
     */
    public void showPanel() {
        setVisible(true);
        super.repaint();
    }

    /**
     * Hide a button in the panel.
     * @param button the button to hide.
     */
    public void hideButton(JButton button) {
        button.setVisible(false);
    }

    /**
     * Show a button in the panel.
     * @param button the button to show.
     */
    public void showButton(JButton button) {
        button.setVisible(true);
    }

    /**
     * Set the text of the dbLabel.
     * @param text the text.
     */
    public void setDBLabelText(String text) { dbLabel.setText(text); revalidate(); }

    /**
     * Show a progress bar at the bottom of the panel (when updating the database).
     * @param max the max value of the progress bar.
     */
    public void showProgressBar(int max) {

        progressBar.setMinimum(0);
        progressBar.setMaximum(max);
        progressBar.setVisible(true);
        //progressBar.paintImmediately( progressBar.getBounds() );
        revalidate();
    }

    public JProgressBar getTheProgressbar() { return progressBar; }
    
}
