/*
 * This file is part of AceQL HTTP.
 * AceQL HTTP: SQL Over HTTP                                     
 * Copyright (C) 2021,  KawanSoft SAS
 * (http://www.kawansoft.com). All rights reserved.                                
 *                                                                               
 * AceQL HTTP is free software; you can redistribute it and/or                 
 * modify it under the terms of the GNU Lesser General Public                    
 * License as published by the Free Software Foundation; either                  
 * version 2.1 of the License, or (at your option) any later version.            
 *                                                                               
 * AceQL HTTP is distributed in the hope that it will be useful,               
 * but WITHOUT ANY WARRANTY; without even the implied warranty of                
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU             
 * Lesser General Public License for more details.                               
 *                                                                               
 * You should have received a copy of the GNU Lesser General Public              
 * License along with this library; if not, write to the Free Software           
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  
 * 02110-1301  USA
 * 
 * Any modifications to this file must keep this entire header
 * intact.
 */
package com.kawansoft.app.util;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dialog;
import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

/**
 * Works a JOptionPane.showMessageDialog(), but allows to pass a message with HTML fomrat and 
 * and clickable URL 
 * 
 * @author Nicolas de Pomereu
 *
 */
public class JOptionPaneHtml
{

    /** The Editor Pane to use */
    private JEditorPane editorPane = new JEditorPane();
    
    /** The parent Component */
    private Component parentComponent;

    /**
     * protected Constructor
     * @param parentComponent       the Parent Component
     */

    protected JOptionPaneHtml(Component parentComponent)
    {
        this.parentComponent = parentComponent;
    }

    
    
    public static void showMessageDialog(Component parentComponent,
            Object message)
    throws HeadlessException
    {
        JOptionPaneHtml JOptionPaneHtml = new JOptionPaneHtml(parentComponent);
        JOptionPaneHtml.setEditorPane(message);

        JOptionPane.showMessageDialog(parentComponent, 
                new JScrollPane(JOptionPaneHtml.editorPane));
    }        
    
    /**
     * Brings up a dialog where the number of choices is determined
     * by the <code>optionType</code> parameter.
     * 
     * @param parentComponent determines the <code>Frame</code> in which the
     *          dialog is displayed; if <code>null</code>,
     *          or if the <code>parentComponent</code> has no
     *          <code>Frame</code>, a 
     *                  default <code>Frame</code> is used
     * @param message   the <code>Object</code> to display
     * @param title     the title string for the dialog
     * @param optionType an int designating the options available on the dialog:
     *                  <code>YES_NO_OPTION</code>, or
     *          <code>YES_NO_CANCEL_OPTION</code>
     * @return an int indicating the option selected by the user
     * @exception HeadlessException if
     *   <code>GraphicsEnvironment.isHeadless</code> returns
     *   <code>true</code>
     * @see java.awt.GraphicsEnvironment#isHeadless
     */
    public static int showConfirmDialog(Component parentComponent,
                                         Object message, 
                                         String title, 
                                         int messageType)
    throws HeadlessException
    {
        
        JOptionPaneHtml JOptionPaneHtml = new JOptionPaneHtml(parentComponent);
        JOptionPaneHtml.setEditorPane(message);

        int result = JOptionPane.showConfirmDialog(parentComponent, 
                                                   new JScrollPane(JOptionPaneHtml.editorPane), 
                                                   title,
                                                   messageType);
        
        return result;

    }


    /**
     * prepare the Editor Pane with the message
     * @param message
     */
    private void setEditorPane(Object message)
    {
        // Turn anti-aliasing on
        System.setProperty("awt.useSystemAAFontSettings", "on");

        // Enable use of custom set fonts
        editorPane.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES,
                Boolean.TRUE);
        
        //editorPane.setFont(new Font("Arial", Font.BOLD, 13));
        //editorPane.setPreferredSize(new Dimension(520, 180));
                
        editorPane.setMargin(new Insets(5, 5, 5, 5));
        
        editorPane.setEditable(false);
        //editorPane.setFocusable(false);
        editorPane.setContentType("text/html");
        
        //editorPane.setOpaque(false);
        //editorPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        
        // TIP: Make the JOptionPane resizable using the HierarchyListener
        editorPane.addHierarchyListener(new HierarchyListener()
        {
            public void hierarchyChanged(HierarchyEvent e)
            {
                Window window = SwingUtilities.getWindowAncestor(editorPane);
                if (window instanceof Dialog)
                {
                    Dialog dialog = (Dialog) window;
                    if (!dialog.isResizable())
                    {
                        dialog.setResizable(true);
                    }
                }
            }
        });

        // TIP: Add Hyperlink listener to process hyperlinks
        editorPane.addHyperlinkListener(new HyperlinkListener()
        {
            public void hyperlinkUpdate(final HyperlinkEvent e)
            {
                if (e.getEventType() == HyperlinkEvent.EventType.ENTERED)
                {
                    EventQueue.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            // TIP: Show hand cursor
                            SwingUtilities
                            .getWindowAncestor(editorPane)
                            .setCursor(
                                    Cursor
                                    .getPredefinedCursor(Cursor.HAND_CURSOR));
                            // TIP: Show URL as the tooltip
                            editorPane.setToolTipText(e.getURL()
                                    .toExternalForm());
                        }
                    });
                }
                else if (e.getEventType() == HyperlinkEvent.EventType.EXITED)
                {
                    EventQueue.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            // Show default cursor
                            SwingUtilities.getWindowAncestor(editorPane)
                            .setCursor(Cursor.getDefaultCursor());

                            // Reset tooltip
                            editorPane.setToolTipText(null);
                        }
                    });
                }
                else if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
                {
                    Desktop desktop = Desktop.getDesktop();   
                    
                    try
                    {
                        desktop.browse(e.getURL().toURI());
                    }
                    catch (Exception exception)
                    {
                        exception.printStackTrace();
                        JOptionPane.showMessageDialog(parentComponent, 
                                "Impossible d\'accéder au site Internet " + e.getURL().toString() + "\n(" + exception.toString() + ")" );
                    }  
                }
            }
        });

        editorPane.setText(message.toString());

    }

}
