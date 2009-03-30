package org.app.mp3AlbumManager.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
 
/**
 * Custom Implementation of a JLabel. Ability to make the label clickable
 *
 * @author tim yates - credit for the getTextRectangle solution
 * @author Christopher Capano
 * @version $Id: $
 * @since Jun 13, 2006 12:43:25 AM
 */
public final class CLabel extends JLabel implements MouseListener
{
    /**
     * <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 8711477184577600030L;

    /**
     * Color of Text when Mouse is on the Label
     */
    private static final Color onColor = Color.BLACK;

    /**
     * Color of Text when Mouse is off the label
     */
    private static final Color offColor = Color.BLUE;

    /**
     * Color of the Text when Mouse is off the label and the label has been previously clicked
     */
    private static final Color clickedColor = new Color(85, 26, 139);

    /**
     * Action to be performed when clickable is true
     */
    private ActionListener action;

    /**
     * Icon displayed on mouse rollover
     */
    private Icon rolloverIcon;

    /**
     * Icon displayed when mouse isn't on a rollover
     */
    private Icon offIcon;

    /**
     * Has this label been clicked?
     */
    private boolean hasBeenClicked;

    /**
     * Default Constructor
     */
    public CLabel()
    {
        super();
        init();
    }

    /**
     * @param image
     */
    public CLabel(final Icon image)
    {
        super(image);
        init();
    }

    /**
     * @param text
     */
    public CLabel(final String text)
    {
        super(text);
        init();
    }

    /**
     * Sets the Labels Default Properties
     *
     * @since Jun 13, 2006 2:10:50 AM
     */
    private void init()
    {
        setOpaque(false);
        hasBeenClicked = false;
    }

    /**
     * Makes the label clickable or makes it unclickable. Clickable label features bold and underlined text
     *
     * @param clickable
     *            Is the label clickable
     * @param action
     *            Action to be performed on click
     * @param rolloverIcon
     *          Icon to display on rollover
     * @since Jun 13, 2006 2:00:57 AM
     */
    public void setClickable(final boolean clickable, final ActionListener action, final Icon rolloverIcon)
    {
        if (clickable)
        {
            //  Set the action
            this.action = action;

            //  Set the offIcon;
            this.offIcon = getIcon();
            setIcon(offIcon);

            //  Add a Mouse Listener
            addMouseListener(this);

            //  Set the text (Undelined and Bolded)
            setText(new StringBuffer("<html><b><u>").append(getText()).append("</u></b></html>").toString());

            //  Set the rollover icon
            this.rolloverIcon = rolloverIcon;

            //  Init the Color and Mouse Cursor
            setForeground(offColor);
        }
        else
        {
            // Set the action to null
            this.action = null;

            // Remove the Mouse Listener
            removeMouseListener(this);

            //  Change the text back to normal
            final StringBuffer textBuffer = new StringBuffer(getText());
            final int msgEnd = textBuffer.indexOf("</u></b></html>");
            setText(textBuffer.substring(12, msgEnd));

            //  Set the rollover icon
            this.rolloverIcon = null;
        }

        // Repaint the Label
        repaint();
    }

    /**
     * This method is taken from inside the source of JLabel (in the inner AccessibleJLabel class)
     */
    private Rectangle getTextRectangle()
    {
        final String text = getText();
        final Icon   icon = (isEnabled()) ? getIcon() : getDisabledIcon();

        if ((icon == null) && (text == null))
        {
            return null;
        }

        final Rectangle paintIconR      = new Rectangle();
        final Rectangle paintTextR      = new Rectangle();
        final Rectangle paintViewR      = new Rectangle();

        Insets paintViewInsets = new Insets(0, 0, 0, 0);
        paintViewInsets = getInsets(paintViewInsets);

        paintViewR.x      = paintViewInsets.left;
        paintViewR.y      = paintViewInsets.top;
        paintViewR.width  = getWidth()  - (paintViewInsets.left + paintViewInsets.right);
        paintViewR.height = getHeight() - (paintViewInsets.top  + paintViewInsets.bottom);

        final Graphics g = getGraphics();
        if (g == null)
        {
            return null;
        }

        SwingUtilities.layoutCompoundLabel(this,
                                          g.getFontMetrics(),
                                          text,
                                          icon,
                                          getVerticalAlignment(),
                                          getHorizontalAlignment(),
                                          getVerticalTextPosition(),
                                          getHorizontalTextPosition(),
                                          paintViewR,
                                          paintIconR,
                                          paintTextR,
                                          getIconTextGap());

        final Rectangle returnValue = new Rectangle(paintTextR);
        returnValue.add(paintIconR);

        return returnValue;
    }

    /**
     * Returns True/False if the mouse is over the text
     *
     * @param mouseEvent
     *      Originating Mouse Event
     * @return
     *      True/False
     * @since Jun 14, 2006 6:44:13 PM
     */
    private boolean isMouseOverText (final MouseEvent mouseEvent)
    {
        final Rectangle textArea      = getTextRectangle();
        boolean         mouseOverText = false;

        if (textArea != null && textArea.contains(mouseEvent.getPoint()))
        {
            mouseOverText = true;
        }

        return (mouseOverText);
    }

    /**
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(final MouseEvent e)
    {
        hasBeenClicked = true;

        // If the action is not null, perform the action
        if (action != null)
        {
            action.actionPerformed(new ActionEvent(this, 0, getText()));
        }
    }

    /**
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(final MouseEvent e)
    {
        if (isMouseOverText(e))
        {
            //  Change the Cursor to a Hand
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            //  Change The Text Color (Mimics Rollover)
            setForeground(onColor);

            //  Change the Icon
            if (rolloverIcon != null)
            {
                setIcon(rolloverIcon);
            }

            //  Repaint the Label
            repaint();
        }
    }

    /**
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(final MouseEvent e)
    {
        if (!isMouseOverText(e))
        {
            //  Change the Cursor to the Default
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            //  Change The Text Color (Mimics Rollover)
            setForeground(hasBeenClicked ? clickedColor : offColor);

            //  Change the Icon
            if (rolloverIcon != null)
            {
                setIcon(offIcon);
            }

            //  Repaint the Label
            repaint();
        }
    }

    /**
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(final MouseEvent e)
    {
        // Do Nothing
    }

    /**
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(final MouseEvent e)
    {
        // Do Nothing
    }

}

