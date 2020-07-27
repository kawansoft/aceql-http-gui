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
package com.kawansoft.app.util.table;

import com.kawansoft.app.parms.MessagesManager;
import com.kawansoft.app.parms.util.ImageParmsUtil;
import com.kawansoft.app.parms.util.ParmsUtil;
import java.awt.Cursor;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.DefaultEditorKit;
import javax.swing.undo.UndoManager;

/**
 * Clipboard Manager allows to add right click pop up menu to JTable with the
 * classical cut/copy/paste/select menu.
 * <br><br>
 * This is done with two lines of code per JFrame:
 * <br> -1) public TableClipboardManager clipboard = null;
 * <br> -2) clipboard = new TableClipboardManager(JTable);
 * <br>
 * <br> -2) Must be done <i>after</i> jFrame creation.
 *
 * @author Nicolas de Pomereu
 *
 */
public class TableClipboardManager {

    public static final String CR_LF = System.getProperty("line.separator");

    //private MessagesManager messagesManager = new  MessagesManager();
    // The Pop Up menu for Paste action
    protected JPopupMenu popupMenu;

    // Clipboard lines
    protected JMenuItem menuItemCancel = null;
    protected JMenuItem menuItemCut = null;
    protected JMenuItem menuItemCopy = null;
    protected JMenuItem menuItemPaste = null;
    protected JMenuItem menuItemDelete = null;
    protected JMenuItem menuItemSelectAll = null;

    // Futur usage
    protected UndoManager undo = new UndoManager();

    // Clipboard actions 
    private String cancel = MessagesManager.get("system_cancel");
    private String cut = MessagesManager.get("system_cut");
    private String copy = MessagesManager.get("system_copy");
    private String paste = MessagesManager.get("system_paste");
    private String select_all = MessagesManager.get("system_select_all");
    private String delete = MessagesManager.get("system_delete");

    // Clipboard actions 
//    protected String cancel = "Annuler";
//    private String cut = "Couper";
//    protected String copy = "Copier";
//    protected String paste = "Coller";
//    protected String select_all = "Tout Sélectionner";
//    protected String delete = "Supprimer";
    /**
     * The JTable to add a contextual pop menu
     */
    protected JTable jTable = null;
    protected final Window parent;

    /**
     * Constructor
     *
     * @param parent the value of parent
     * @param jTable The JTable to add a contextual pop menu
     * @param isEditable if true, jTable is editable
     */
    public TableClipboardManager(Window parent, JTable jTable, boolean isEditable) {

        this.parent = parent;
        this.jTable = jTable;

        init1();
        init2(isEditable, jTable);

    }

        private void init1() {
        popupMenu = new JPopupMenu();
        popupMenu.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        menuItemCancel = new JMenuItem(cancel);
        menuItemCancel.setEnabled(false);
        popupMenu.add(menuItemCancel);

        popupMenu.addSeparator();

        menuItemCut = new JMenuItem(new DefaultEditorKit.CutAction());
        menuItemCut.setText(cut);
        menuItemCut.addActionListener((new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jTableClipboardActionPerformed(e);
            }
        }));
        menuItemCut.setIcon(ImageParmsUtil.createImageIcon(ParmsUtil.CUT_ICON));
        //menuItemCut.setAccelerator(KeyStroke.getKeyStroke(
        //KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        popupMenu.add(menuItemCut);

        menuItemCopy = new JMenuItem(new DefaultEditorKit.CopyAction());
        menuItemCopy.setText(copy);
        menuItemCopy.addActionListener((new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jTableClipboardActionPerformed(e);
            }
        }));

        menuItemCopy.setIcon(ImageParmsUtil.createImageIcon(ParmsUtil.COPY_ICON));
        //menuItemCopy.setAccelerator(KeyStroke.getKeyStroke(
        //KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        popupMenu.add(menuItemCopy);

        menuItemPaste = new JMenuItem(new DefaultEditorKit.PasteAction());
        menuItemPaste.setText(paste);
        menuItemPaste.addActionListener((new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jTableClipboardActionPerformed(e);
            }
        }));
        menuItemPaste.setIcon(ImageParmsUtil.createImageIcon(ParmsUtil.PASTE_ICON));
        //menuItemPaste.setAccelerator(KeyStroke.getKeyStroke(
        //KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        popupMenu.add(menuItemPaste);

    }
        
    private void init2(boolean isEditable, JTable jTable1) {
        if (isEditable) {
            menuItemDelete = new JMenuItem(delete);
            menuItemDelete.addActionListener((new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    jTableClipboardActionPerformed(e);
                }
            }));
            menuItemDelete.setAccelerator(KeyStroke.getKeyStroke(
                    KeyEvent.VK_DELETE, 0));
            menuItemDelete.setIcon(ImageParmsUtil.createImageIcon(ParmsUtil.DELETE_ICON));
            popupMenu.add(menuItemDelete);
        }
        popupMenu.addSeparator();
        menuItemSelectAll = new JMenuItem(select_all);
        menuItemSelectAll.setText(select_all);
        menuItemSelectAll.addActionListener((new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jTableClipboardActionPerformed(e);
            }
        }));
        popupMenu.add(menuItemSelectAll);
        jTable1.addMouseListener(new MouseAdapter() {
            
            public void mousePressed(MouseEvent e) {
                jTableMouseReleased(e);
            }

            public void mouseReleased(MouseEvent e) {
                jTableMouseReleased(e);
            }

        });
        if (isEditable) {
            jTable1.addKeyListener(new KeyAdapter() {
                public void keyReleased(KeyEvent e) {
                    int id = e.getID();
                    if (id == KeyEvent.KEY_RELEASED) {
                        int keyCode = e.getKeyCode();

                        if (keyCode == KeyEvent.VK_DELETE) {
                            delete();
                        }
                    }
                }
            });
        }
    }



    /**
     * When the Mouse is released, clipboard action is done
     *
     * @param e The Mouse Eevent
     */
    public void jTableMouseReleased(MouseEvent e) {

        // These are disabled because the Table is not editable
        menuItemPaste.setEnabled(false);
        menuItemCut.setEnabled(false);
        menuItemCopy.setEnabled(false);

        //menuItemDelete.setEnabled(true);
        int[] selRows = jTable.getSelectedRows();

        if (selRows.length == 0) {
            menuItemCopy.setEnabled(false);
        } else {
            menuItemCopy.setEnabled(true);
        }

        if (e.isPopupTrigger()) {
            popupMenu.show(e.getComponent(),
                    e.getX(), e.getY());
        }
    }

    // Paste action
    public void jTableClipboardActionPerformed(ActionEvent e) {
        //System.out.println("e.getActionCommand(): " + e.getActionCommand());

        if (e.getActionCommand().equals(select_all)) {
            jTable.selectAll();
        } else if (e.getActionCommand().equals(copy)) {
            // Put the content of the jTable in a JTextField
            int[] selRows = jTable.getSelectedRows();

            if (selRows.length > 0) {
                String value = "";

                List<Integer> listRows = new Vector<Integer>();

                // Because sort may change order...
                for (int i = 0; i < selRows.length; i++) {
                    listRows.add(jTable.convertRowIndexToModel(selRows[i]));
                }

                for (int i = 0; i < listRows.size(); i++) {
                    // get Table data
                    TableModel tm = jTable.getModel();

                    for (int j = 0; j < tm.getColumnCount(); j++) {
                        // Force the cast of the getValueAt into a String
                        Object oValue = tm.getValueAt(listRows.get(i), j);

                        // Case column is hidden
                        if (oValue != null) {
                            value += (String) oValue.toString();
                            value += "\t";
                        }
                    }

                    value += CR_LF;
                }

                JEditorPane jEditorPane = new JEditorPane();
                jEditorPane.setText(value);
                jEditorPane.selectAll();
                jEditorPane.copy();
            }

        } else if (e.getActionCommand().equals(delete)) {
            delete();
        }
    }

    /**
     * Delete the files
     */
    private void delete() {

        int[] selRows = jTable.getSelectedRows();

        if (selRows.length == 0) {
            return;
        }
        String text = MessagesManager.get("are_you_sure_to_delete_these_elements");
        String title = MessagesManager.get("warning"); // "Attention";

        int result = JOptionPane.showConfirmDialog(parent, text, title, JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.NO_OPTION) {
            return;
        }

        parent.setCursor(Cursor
                .getPredefinedCursor(Cursor.WAIT_CURSOR));

        List<Integer> listRows = new ArrayList<>();

        for (int i = 0; i < selRows.length; i++) {
            listRows.add(jTable.convertRowIndexToModel(selRows[i]));
        }

        // get Table data and remove the selected rows
        DefaultTableModel tm = (DefaultTableModel) jTable.getModel();

        // For values row be sorted in asc
        Collections.sort(listRows);

        // Remove the row(s)
        for (int i = listRows.size() - 1; i >= 0; i--) {
            Integer rowIndex = listRows.get(i);
            tm.removeRow(rowIndex);
        }

        jTable.setModel(tm);

        parent.setCursor(Cursor
                .getDefaultCursor());
    }

}

/**
 *
 */
