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

import com.kawansoft.app.parms.MessagesManager;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.border.BevelBorder;
import javax.swing.text.DefaultEditorKit;
import javax.swing.undo.UndoManager;


/**
 * Clipboard Manager allows to add right click pop up menu to Map content with
 * the classical cut/copy/paste/select menu.
 * <br><br>
 * This is done with two lines of code per JFrame:
 * <br> -1) public MapClipboardManager clipboard = null; 
 * <br> -2) clipboard = new MapClipboardManager(JTable);
 * <br>
 * <br> -2) Must be done <i>after</i> jFrame creation.
 *  
 * @author Nicolas de Pomereu
 *
 */
public class MapClipboardManager
{
    public static final String CR_LF = System.getProperty("line.separator") ;

    //private MessagesManager messagesManager = new  MessagesManager();

    // The Pop Up menu for Paste action
    private JPopupMenu popupMenu;

    // Clipboard lines
    private JMenuItem menuItemCancel = null;
    private JMenuItem menuItemCut = null;
    private JMenuItem menuItemCopy = null;
    private JMenuItem menuItemPaste = null;
    private JMenuItem menuItemDelete= null;
    private JMenuItem menuItemSelectAll = null;

    // Futur usage
    protected UndoManager undo = new UndoManager();

    // Clipboard actions
    private String cancel       = MessagesManager.get("system_cancel");
    private String cut          = MessagesManager.get("system_cut");
    private String copy         = MessagesManager.get("system_copy");
    private String paste        = MessagesManager.get("system_paste");
    private String select_all   = MessagesManager.get("system_select_all");
    private String delete       = MessagesManager.get("system_delete");
    
    // Clipboard actions
//    private String cancel       = "Annuler";
//    private String cut          = "Couper";
//    private String copy         = "Copier";
//    private String paste        = "Coller";
//    private String select_all   = "Tout Sélectionner"; 
//    private String delete       = "Supprimer";

    /** The parent Window */
    private Window parent = null;

    /** The Map to add a contextual pop menu */
    private Map<String, String> map = null;

    /**
     * Constructor
     * @param table The JTable to add a contextual pop menu
     */

    public MapClipboardManager(Window parent, Map<String, String> map)
    { 
        this.parent = parent;
        this.map = map;

        popupMenu = new JPopupMenu();        
        popupMenu.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        menuItemCancel = new JMenuItem(cancel);
        menuItemCancel.setEnabled(false);
        popupMenu.add(menuItemCancel);   

        popupMenu.addSeparator();

        menuItemCut = new JMenuItem(new DefaultEditorKit.CutAction());
        menuItemCut.setText(cut);
        menuItemCut.addActionListener((new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                map_clipboard_actionPerformed(e);
            }})); 
        //menuItemCut.setAccelerator(KeyStroke.getKeyStroke(
        //KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        popupMenu.add(menuItemCut);   

        menuItemCopy = new JMenuItem(new DefaultEditorKit.CopyAction());
        menuItemCopy.setText(copy);
        menuItemCopy.addActionListener((new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                map_clipboard_actionPerformed(e);
            }}));         
        //menuItemCopy.setAccelerator(KeyStroke.getKeyStroke(
        //KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        popupMenu.add(menuItemCopy);   

        menuItemPaste = new JMenuItem(new DefaultEditorKit.PasteAction());
        menuItemPaste.setText(paste);
        menuItemPaste.addActionListener((new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                map_clipboard_actionPerformed(e);
            }}));               
        //menuItemPaste.setAccelerator(KeyStroke.getKeyStroke(
        //KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        popupMenu.add(menuItemPaste);          

        menuItemDelete = new JMenuItem(delete);
        menuItemDelete.addActionListener((new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                map_clipboard_actionPerformed(e);
            }}));         
        popupMenu.add(menuItemDelete);    

        popupMenu.addSeparator();

        menuItemSelectAll = new JMenuItem(select_all);
        menuItemSelectAll.setText(select_all);
        menuItemSelectAll.addActionListener((new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                map_clipboard_actionPerformed(e);
            }})); 
        popupMenu.add(menuItemSelectAll);        

        parent.addMouseListener(new MouseAdapter() { 
            public void mouseReleased(MouseEvent e) 
            { 
                this_mouseReleased(e); 
            } 
        });
    }

    /** 
     * When the Mouse is released, clipboard action is done
     * @param e     The Mouse Eevent
     */
    public void this_mouseReleased(MouseEvent e) 
    { 

        // These are disabled because the Table is not editable
        menuItemPaste.setEnabled(false);
        menuItemCut.setEnabled(false);
        menuItemDelete.setEnabled(false);
        menuItemSelectAll.setEnabled(false);

        menuItemCopy.setEnabled(true);

        if (e.isPopupTrigger()) {
            popupMenu.show(e.getComponent(),
                    e.getX(), e.getY());
        }
    } 


    // Paste action
    public void map_clipboard_actionPerformed(ActionEvent e)
    {        
        //System.out.println("e.getActionCommand(): " + e.getActionCommand());

//        if ( e.getActionCommand().equals(select_all))
//        {           
//            //table.selectAll();
//        }         
//        
        if ( e.getActionCommand().equals(copy))
        {
            String value = "";

            Set<String> set = map.keySet();

            List keys = Arrays.asList(set.toArray());

            for (int i = 0; i < keys.size(); i++)
            {
                //System.out.println(keys.get(i) + " " + map.get(keys.get(i)));  
                
                value +=  (String) keys.get(i);
                //value += "\t";
                value += " ";
                value += (String) map.get(keys.get(i));
                value += CR_LF;                  
            }
                        
            JEditorPane jEditorPane = new JEditorPane(); 
            jEditorPane.setText(value);
            jEditorPane.selectAll();
            jEditorPane.copy();

        }            



    }    

}

