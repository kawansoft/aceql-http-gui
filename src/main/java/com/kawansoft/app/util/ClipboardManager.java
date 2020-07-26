/*
 * This file is part of AceQL HTTP.
 * AceQL HTTP: SQL Over HTTP                                     
 * Copyright (C) 2017,  KawanSoft SAS
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

import com.kawansoft.aceql.gui.util.AceQLManagerUtil;
import com.kawansoft.app.parms.MessagesManager;
import com.kawansoft.app.parms.util.ImageParmsUtil;
import com.kawansoft.app.parms.util.ParmsUtil;
import java.awt.Component;
import java.awt.Container;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import com.swing.util.SwingUtil;


/**
 * Clipboard Manager allows to add right click pop up menu to all text fields with
 * the classical cut/copy/paste/select menu.
 * <br><br>
 * This is done with two lines of code per JFrame:
 * <br> -1) public ClipboardManager clipboard = null; 
 * <br> -2) clipboard = new ClipboardManager(jFrame);
 * <br>
 * <br> -2) Must be done <i>after</i> jFrame creation.
 *  
 * @author Nicolas de Pomereu
 *
 */
public class ClipboardManager
{
     public boolean DEBUG = false;

    // The Pop Up menu for Paste action
    private JPopupMenu popupMenu;

    // Clipboard lines
    private JMenuItem menuItemCut = null;
    private JMenuItem menuItemCopy = null;
    private JMenuItem menuItemPaste = null;
    private JMenuItem menuItemDelete= null;
    private JMenuItem menuItemSelectAll = null;

    // Futur usage
    protected UndoManager undo = new UndoManager();

    // Clipboard actions
//    private String cancel       = "Annuler";
//    private String cut          = "Couper";
//    private String copy         = "Copier"; 
//    private String paste        = "Coller";
//    private String select_all   = "Tout Sélectionner";
//    private String delete       = "Supprimer";
    
    private String cancel       = MessagesManager.get("system_cancel");
    private String cut          = MessagesManager.get("system_cut");
    private String copy         = MessagesManager.get("system_copy");
    private String paste        = MessagesManager.get("system_paste");
    private String select_all   = MessagesManager.get("system_select_all");
    private String delete       = MessagesManager.get("system_delete");
    
    protected JTextComponent textComponent = null;

    /** The container */
    private Container container = null;
    private static boolean LOCKED = false;
    private boolean canDelete = true;

        /**
     * 
     * @param container
     */
    public ClipboardManager(Container container, boolean canDelete)
    {
        this.canDelete = canDelete;
        init(container);   
    }
    /**
     * 
     * @param container
     */
    public ClipboardManager(Container container)
    {
        init(container);    
    }

    public void init(Container container1) {        
        this.container = container1;
        popupMenu = new JPopupMenu();
        popupMenu.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        JMenuItem menuItemCancel =  new JMenuItem(cancel);
        menuItemCancel.setEnabled(false);
        popupMenu.add(menuItemCancel);
        popupMenu.addSeparator();
        menuItemCut = new JMenuItem(new DefaultEditorKit.CutAction());
        menuItemCut.setText(cut);
        menuItemCut.setIcon(ImageParmsUtil.createImageIcon(ParmsUtil.CUT_ICON));
        //menuItemCut.setAccelerator(KeyStroke.getKeyStroke(
        //KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        popupMenu.add(menuItemCut);
        menuItemCopy = new JMenuItem(new DefaultEditorKit.CopyAction());
        menuItemCopy.setText(copy);
        menuItemCopy.setIcon(ImageParmsUtil.createImageIcon(ParmsUtil.COPY_ICON));
        //menuItemCopy.setAccelerator(KeyStroke.getKeyStroke(
        //KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        popupMenu.add(menuItemCopy);
        menuItemPaste = new JMenuItem();
        menuItemPaste.setText(paste);
        menuItemPaste.setIcon(ImageParmsUtil.createImageIcon(ParmsUtil.PASTE_ICON));
        //menuItemPaste.setAccelerator(KeyStroke.getKeyStroke(
        //KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        menuItemPaste.addActionListener((new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jTextComponentClipboardActionPerformed(e);
            }
        }));
        popupMenu.add(menuItemPaste);
        menuItemDelete = new JMenuItem(delete);
        menuItemDelete.addActionListener((new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                jTextComponentClipboardActionPerformed(e);
            }}));         
        
        if (canDelete) {
            menuItemDelete.setIcon(ImageParmsUtil.createImageIcon(ParmsUtil.DELETE_ICON));
            popupMenu.add(menuItemDelete);
            popupMenu.addSeparator();
        }
        
        menuItemSelectAll = new JMenuItem(select_all);
        menuItemSelectAll.setText(select_all);
        menuItemSelectAll.addActionListener((new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                jTextComponentClipboardActionPerformed(e);
            }}));
        popupMenu.add(menuItemSelectAll);
        // Done! Now add the listeners
        addMouseListenerToTextComponents();
    }

    public static synchronized void lockUndoableListerner() {
        LOCKED = true;
    }
    
    public static synchronized void unlockUndoableListerner() {
        LOCKED = false;
    }
    
    
    /**
     * Add a Mouse Listener to all Text Components that is dedicated to clipboard
     * actions
     */
    private void addMouseListenerToTextComponents()
    {
        List c = SwingUtil.getAllComponants(container);

        for (int i = 0; i < c.size(); i++)
        {
            Component component = (Component) c.get(i);

            //System.out.println("component: " + component);

            if (component instanceof JTextComponent)
            {                
                JTextComponent textComponent = (JTextComponent)component;

                textComponent.addMouseListener(new MouseAdapter() {

                    @Override
                    public void mousePressed(MouseEvent e) {
                        jTextComponentMouseReleased(e);
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        jTextComponentMouseReleased(e);
                    }

                });


                // Do not allow undo/redo on non editable components!
                if (! textComponent.isEditable())
                {
                    continue;
                }

                // Undo Manager
                final UndoManager undo = new UndoManager();
                undo.setLimit(-1);
                //System.out.println("UndoManager:" + undo.getLimit());
                Document doc = textComponent.getDocument();

                // Listen for undo and redo events
                doc.addUndoableEditListener(new UndoableEditListener() {
                    @Override
                    public void undoableEditHappened(UndoableEditEvent evt) {
                        //debug("LOCKED :" + LOCKED);
                        addEdiIfNotLocked(undo, evt);
                    }
                });
                
                // Create an undo action and add it to the text component
                textComponent.getActionMap().put("Undo",
                        new AbstractAction("Undo") {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        AceQLManagerUtil.debugEvent(evt);
                        try {
                            if (undo.canUndo()) {
                                undo.undo();
                            }
                        } catch (CannotUndoException e) {
                        }
                    }
                });

                // Bind the undo action to ctl-Z
                textComponent.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");

                // Create a redo action and add it to the text component
                textComponent.getActionMap().put("Redo",
                        new AbstractAction("Redo") {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        try {
                            AceQLManagerUtil.debugEvent(evt);
                            if (undo.canRedo()) {
                                undo.redo();
                            }
                        } catch (CannotRedoException e) {
                        }
                    }
                });

                // Bind the redo action to ctl-Y
                textComponent.getInputMap().put(KeyStroke.getKeyStroke("control Y"), "Redo");          

            }
        }       

    }
    
    private static synchronized void addEdiIfNotLocked(UndoManager undo, UndoableEditEvent evt ) {
        if (! LOCKED) {
            undo.addEdit(evt.getEdit());
        }
    }

    /** 
     * When the Mosue is released, clipboard action is done
     * @param e     The Mouse Eevent
     */
    public void jTextComponentMouseReleased(MouseEvent e) 
    { 
        //System.out.println("\njTextFieldCode_mouseReleased(MouseEvent e) called."); 

        if (e.getComponent() instanceof JTextComponent)
        {
            textComponent = (JTextComponent) e.getComponent();

            textComponent.requestFocusInWindow();

            //System.out.println("Text Component: " +  textComponent);
            //System.out.println("Text Content  : " +  textComponent.getText());

            if (textComponent.isEditable() && 
                    textComponent.isEnabled() && 
                    isTextDataAvailableForPaste())
            {
                menuItemPaste.setEnabled(true);
            }
            else
            {
                menuItemPaste.setEnabled(false);
            }

            if (textComponent.getText() == null || 
                    textComponent.getText().equals(""))               
            {
                menuItemSelectAll.setEnabled(false);
            }
            else
            {           

                if (textComponent.isEnabled())
                {
                    menuItemSelectAll.setEnabled(true);
                }
                else
                {
                    menuItemSelectAll.setEnabled(false);
                }
            }

            if (textComponent.getSelectedText() == null)
            {
                menuItemCopy.setEnabled(false);
                menuItemCut.setEnabled(false);
                menuItemDelete.setEnabled(false);
            }
            else
            {
                menuItemCopy.setEnabled(true);

                if (textComponent.isEditable())
                {
                    menuItemCut.setEnabled(true);                
                    menuItemDelete.setEnabled(true);
                }
                else
                {
                    menuItemCut.setEnabled(false);                
                    menuItemDelete.setEnabled(false);
                }
            }                      
        }

        if (e.isPopupTrigger()) {
            popupMenu.show(e.getComponent(),
                    e.getX(), e.getY());
        }
    } 


    // Paste action
    public void jTextComponentClipboardActionPerformed(ActionEvent e)
    {        
        
        if ( e.getActionCommand().equals(select_all))
        {           
            textComponent.selectAll();
        }   
        else if (e.getActionCommand().equals(paste)) {
            try {
                
                String data = (String) Toolkit.getDefaultToolkit() 
                        .getSystemClipboard().getData(DataFlavor.stringFlavor);

                int pos = textComponent.getCaretPosition();
                String text = textComponent.getText();
                                
                debug("pos : " + pos);
                debug("text: " + text);
                debug("data: " + data);
                
                if (text == null || text.isEmpty()) {
                    textComponent.setText(data);
                    return;
                }
                
                int selBegin = textComponent.getSelectionStart();
                int selEnd = textComponent.getSelectionEnd();
                
                try {
                    if (selEnd - selBegin == 0) { // No selection
                        
                        String beginText = text.substring(0, pos);
                        String endText = text.substring(pos);
                        
                        debug("selBegin : " + selBegin);
                        debug("selEnd   : " + selEnd);
                        
                        debug("");
                        debug("beginText : " + beginText);
                        debug("endText   : " + endText);
                        
                        textComponent.setText(beginText + data + endText);
                        
                    } else {
                        // The paste removes the selected text and replace if by the clipboard buffer
                        String beginText = text.substring(0, selBegin);
                        String endText = text.substring(selEnd);
                        
                        textComponent.setText(beginText + data + endText);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    // Just for security reasons, trap Exceptions and copy all buffer
                    textComponent.setText(data);
                }

                
            } catch (Exception ex) {
                
            }
        }             
        else if ( e.getActionCommand().equals(delete))
        {
            textComponent.setText(null);
        } 

    }    

    /**
     * return true if the data is available for paste from the clipboard
     * @return 
     */
    public boolean isTextDataAvailableForPaste()
    {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        //odd: the Object param of getContents is not currently used
        Transferable contents = clipboard.getContents(null);

        boolean hasTransferableFiles = (contents != null) &&
        contents.isDataFlavorSupported(DataFlavor.stringFlavor);

        return hasTransferableFiles;
    }

           /**
     * debug tool
     */
    private void debug(String s) {
        if (DEBUG) {
            System.out.println(s);
            // System.out.println(this.getClass().getName() + " " + new Date() +
            // " " + s);
        }
    }
    
}
