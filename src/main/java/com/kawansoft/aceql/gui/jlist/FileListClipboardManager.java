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
package com.kawansoft.aceql.gui.jlist;

import com.kawansoft.app.parms.MessagesManager;
import com.kawansoft.app.parms.ParmsConstants;
import com.kawansoft.app.parms.util.ImageParmsUtil;
import com.kawansoft.app.parms.util.ParmsUtil;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.ListModel;
import javax.swing.border.BevelBorder;
import javax.swing.text.DefaultEditorKit;
import javax.swing.undo.UndoManager;
import java.util.ArrayList;
import org.apache.commons.io.FileUtils;




/**
 * Clipboard Manager allows to add right click pop up menu to JList with
 * the classical cut/copy/paste/select menu.
 * <br><br>
 * This is done with two lines of code per JFrame:
 * <br> -1) public FileListClipboardManager clipboard = null; 
 * <br> -2) clipboard = new FileListClipboardManager(JList);
 * <br>
 * <br> -2) Must be done <i>after</i> jFrame creation.
 *  
 * @author Nicolas de Pomereu
 *
 */
public class FileListClipboardManager
{
    public static boolean DEBUG = false;
        
    public static final String CR_LF = System.getProperty("line.separator") ;

    //private MessagesManager messagesManager = new  MessagesManager();

    // The Pop Up menu for Paste action
    private JPopupMenu popupMenu;

    // Clipboard lines
    private JMenuItem menuItemOpen = null;
    private JMenuItem menuItemPaste = null; 
    private JMenuItem menuItemDelete= null;
    private JMenuItem menuItemSelectAll = null;

    // Futur usage
    protected UndoManager undo = new UndoManager();
    
    private String open = MessagesManager.get("system_open");
    private String paste = MessagesManager.get("system_paste");
    private String copy  = MessagesManager.get("system_copy");
    private String select_all = MessagesManager.get("system_select_all");
    private String delete = MessagesManager.get("system_delete");

    /** The parent Window */
    private Window parent = null;
    
    /** The JTable to add a contextual pop menu */
    private JList jList = null;

    /**
     * Default is that jList is deletable
     */
    private boolean isDeletable =  true;


    /**
     * Says if the files must be really deleted
     */
    private boolean realDeleteOfFiles = true;
    
    /**
     * Constructor
     * 
     * @param parent
     * @param jList         The JList to add a contextual pop menu
     */

    public FileListClipboardManager(Window parent, JList jList)
    { 
        this.parent = parent;
        this.jList = jList;
        createClipboard();
    }

    /**
     * Constructor
     * 
     * @param parent
     * @param jList         The JList to add a contextual pop menu
     * @param isDeletable   if true the files may be deleted
     */
    public FileListClipboardManager(Window parent, JList jList, boolean isDeletable)
    {          
        this.parent = parent;
        this.jList = jList;
        this.isDeletable = isDeletable;
        
        createClipboard();
    }

        
//    /**
//     * @return the realDeleteOfFiles
//     */
//    public boolean isRealDeleteOfFiles()
//    {
//        return realDeleteOfFiles;
//    }
//
//    /**
//     * @param realDeleteOfFiles the realDeleteOfFiles to set
//     */
//    public void setRealDeleteOfFiles(boolean realDeleteOfFiles)
//    {
//        this.realDeleteOfFiles = realDeleteOfFiles;
//    }

    /**
     * Main action to cre   te the Clipboard
     */

    public void createClipboard()
    {         
        jList.addKeyListener(new KeyAdapter() { 
            @Override
            public void keyReleased(KeyEvent e) 
            { 
                jListKeyReleased(e);                                 
            } 
        });
        
        popupMenu = new JPopupMenu();        
        popupMenu.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        menuItemOpen = new JMenuItem(new DefaultEditorKit.CutAction());
        menuItemOpen.setText(open);
        menuItemOpen.addActionListener((new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                jListClipboardActionPerformed(e);
            }})); 
        popupMenu.add(menuItemOpen);   
        
        menuItemPaste = new JMenuItem(paste);
        menuItemPaste.addActionListener((new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jListClipboardActionPerformed(e);
            }}));
        
        //menuItemPaste.setAccelerator(KeyStroke.getKeyStroke(
        //        KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        
        menuItemPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        
        menuItemPaste.setIcon(ImageParmsUtil.createImageIcon(ParmsUtil.PASTE_ICON));
        popupMenu.add(menuItemPaste);        
        
        menuItemDelete = new JMenuItem(delete);
        menuItemDelete.addActionListener((new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jListClipboardActionPerformed(e);
            }}));
        menuItemDelete.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_DELETE, 0));
        menuItemDelete.setIcon(ImageParmsUtil.createImageIcon(ParmsUtil.DELETE_ICON));
        popupMenu.add(menuItemDelete);
        
        if ( !isDeletable) {
            menuItemDelete.setEnabled(false);
        }

        popupMenu.addSeparator();

        menuItemSelectAll = new JMenuItem(select_all);
        menuItemSelectAll.setText(select_all);
        menuItemSelectAll.addActionListener((new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                jListClipboardActionPerformed(e);
            }})); 
        popupMenu.add(menuItemSelectAll);        

        menuItemOpen.setEnabled(false);
        
        jList.addMouseListener(new MouseAdapter() { 

            @Override
            public void mousePressed(MouseEvent e) {
                jListMouseReleased(e);
            }
                        
            @Override
            public void mouseReleased(MouseEvent e) 
            { 
                jListMouseReleased(e); 
            } 
                             
        });
        
        
    }

    private void jListKeyReleased(KeyEvent e) 
    {        
        //System.out.println("this_keyReleased(KeyEvent e) " + e.getComponent().getName()); 
        int id = e.getID();
        if (id == KeyEvent.KEY_RELEASED) 
        { 
            int keyCode = e.getKeyCode();
                      
            // Trap the special ctrl-v for file copy into attach files zone            
            if (keyCode == KeyEvent.VK_V && e.getModifiers() == ActionEvent.CTRL_MASK)
            {                            
                add(getFilesFromClipboard());
            }
            
            if (keyCode == KeyEvent.VK_DELETE)
            {
                delete();   
            }
 
        }       
    } 
    
    /** 
     * When the Mouse is released, clipboard action is done
     * @param e     The Mouse Eevent
     */
    public void jListMouseReleased(MouseEvent e) 
    { 

        // These are disabled because the Table is not editable
        menuItemOpen.setEnabled(false);
        menuItemDelete.setEnabled(false);

        int [] selRows = jList.getSelectedIndices();

        if (selRows.length == 0) {
            menuItemOpen.setEnabled(false);
            menuItemDelete.setEnabled(false);
        } else {
            //menuItemOpen.setEnabled(true);

            if (isDeletable) {
                menuItemDelete.setEnabled(true);
            }
            else
            {
                menuItemDelete.setEnabled(false);
            }
        }

        if (e.isPopupTrigger()) {
            popupMenu.show(e.getComponent(),
                    e.getX(), e.getY());
        }
    } 


    // Paste action
    public void jListClipboardActionPerformed(ActionEvent e)
    {        
        //System.out.println("e.getActionCommand(): " + e.getActionCommand());

        if (e.getActionCommand().equals(select_all)) {
            int start = 0;
            int end = jList.getModel().getSize() - 1;
            if (end >= 0) {
                jList.setSelectionInterval(start, end);
            }
        }      
        else if (e.getActionCommand().equals(copy)) {
            // Put the content of the jList in a JTextField
            int[] selRows = jList.getSelectedIndices();

            if (selRows.length > 0) {
                String value = "";

                List<Integer> listRows = new Vector<Integer>();

                // Because sort may change order...
                //for (int i = 0; i < selRows.length; i++) {
                //    listRows.add(jList.convertRowIndexToModel(selRows[i]));
                //}
        
                for (int i = 0; i < listRows.size(); i++) {
                    // get Table data
                    ListModel tm = jList.getModel();

                    for (int j = 0; j < tm.getSize(); j++) {
                        // Force the cast of the getValueAt into a String
                        Object oValue = tm.getElementAt(j);

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

        } 
        else if ( e.getActionCommand().equals(paste))
        {
            add(getFilesFromClipboard());
        }         
        else if ( e.getActionCommand().equals(open))
        {
            // Put the content of the jList in a JTextField
            int [] selRows = jList.getSelectedIndices();

            if (selRows.length > 0) 
            {
                // get List data
                ListModel listModel = jList.getModel();

                // Force the cast of the getValueAt into a String
                File file  = (File) listModel.getElementAt(selRows[0]);
              
               try
               {
                   parent.setAlwaysOnTop(false);
                   Desktop dekstop = Desktop.getDesktop();
                   dekstop.open(file); 
                   parent.setVisible(false);
                   parent.setVisible(true);
               }
               catch (Exception ex)
               {
                   ex.printStackTrace();
                   JOptionPane.showMessageDialog(parent, 
                           "Impossible to read the file: " + ex + "\n(" + ex.toString() + ")" );
               }                                      
            }        
        }
        else if ( e.getActionCommand().equals(delete))
        {            
            delete();            
        }
    }    
    
    public void add(List<File> files) {
        
        File[] filesArray = new File[files.size()];
        filesArray = files.toArray(filesArray);
        add(filesArray);
    }
    
    /**
     * Add files to the AbstractFileListManager
     * <br>
     * may be called by outside program
     * @param files     Files to add
     */
    @SuppressWarnings("unchecked")
    public void add(File [] files)
    {
        if (files == null || files.length == 0)
        {
            return;
        }
        
        
    }
    
   
    /**
     * Delete the files
     */
    private void delete()
    {
       
        if (! isDeletable) {
            return;
        }
        
        int [] selRows = jList.getSelectedIndices();
        
        if (selRows.length == 0) 
        {
            return;
        }
        
        if (realDeleteOfFiles)
        {
            int response = JOptionPane.showConfirmDialog(parent, 
                    MessagesManager.get("are_you_sure_to_delete_these_files"), //"Voulez-vous vraiment supprimer du disque dur les fichiers sélectionnés ?",
                    ParmsConstants.APP_NAME,
                    JOptionPane.YES_NO_OPTION);
            
            if (response != JOptionPane.YES_OPTION)
            {
                return;
            }       
                         
        }
        
        // Get JList data and remove the selected rows
        
        List<File> nonDeletableList = new ArrayList<>();
                        
        ListModel listModel = jList.getModel();
        for (int i= 0; i < selRows.length ; i++) 
        {                
            
            File file = (File) listModel.getElementAt(selRows[i]);
            debug("");
            debug("File to delete: " + file);
                        
            if(realDeleteOfFiles)
            {
                //file.delete();
                try {
                    FileUtils.forceDelete(file);
                    debug("File deleted " + file);
                } catch (IOException ex) {
                     nonDeletableList.add(file);
                }                
            }
        }
                        
        if (!nonDeletableList.isEmpty()) {
            String message = CR_LF;
            for (File nonDeletable : nonDeletableList) {
                message += nonDeletable.getName() + CR_LF;
            }
            JOptionPane.showMessageDialog(parent, "Stop server instance & restart this program in order to uninstall/delete the Driver(s): " + message, ParmsConstants.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }
                
               
    }
    
    /**
     * If the user has selected files using Explorer : get the files names
     * 
     * @return  the file names selected by user to copy into attach area
     */
    public static File [] getFilesFromClipboard()
    {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        
        //odd: the Object param of getContents is not currently used
        Transferable contents = clipboard.getContents(null);
                    
        boolean hasTransferableFiles = (contents != null) &&
               contents.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
        
        //debug("hasTransferableFiles: " + hasTransferableFiles);
        
        List<File> listFiles = null;
        
        if ( hasTransferableFiles ) 
        {
            try 
            {
                listFiles = (List<File>)contents.getTransferData(DataFlavor.javaFileListFlavor);
            }
            catch (UnsupportedFlavorException ex){
                //highly unlikely since we are using a standard DataFlavor
                System.out.println(ex);
                ex.printStackTrace();
            }
            catch (IOException ex) {
                System.out.println(ex);
                ex.printStackTrace();
            }
        }
        
        if (listFiles == null)
        {
            return null;
        }
        
        File [] files = new File[listFiles.size()];
        for (int i = 0; i < listFiles.size(); i++)
        {
            files[i] = listFiles.get(i);
        }
        
        return files;
    }
    
    /**
     * Displays the specified message if the DEBUG flag is set.
     *
     * @param sMsg the debug message to display
     */
    protected void debug(String sMsg) {
        if (DEBUG) {
            System.out.println(sMsg);
        }
    }
}


