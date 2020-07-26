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
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
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

import org.apache.commons.lang3.StringUtils;



/**
 * Clipboard Manager allows to add right click pop up menu to JTable with
 * the classical cut/copy/paste/select menu.
 * <br><br>
 * This is done with two lines of code per JFrame:
 * <br> -1) public FileTableClipboardManager clipboard = null; 
 * <br> -2) clipboard = new FileTableClipboardManager(JTable);
 * <br>
 * <br> -2) Must be done <i>after</i> jFrame creation.
 *  
 * @author Nicolas de Pomereu
 *
 */
public class FileTableClipboardManagerGroupFile
{
    public static final String CR_LF = System.getProperty("line.separator") ;

    //private MessagesManager messagesManager = new  MessagesManager();

    // The Pop Up menu for Paste action
    private JPopupMenu popupMenu;

    // Clipboard lines
    private JMenuItem menuItemOpen = null;
    private JMenuItem menuItemDelete= null;

    // Futur usage
    protected UndoManager undo = new UndoManager();

    // Clipboard actions
//    private String open         = "Ouvrir";
//    private String paste        = "Copier";
//    private String select_all   = "Tout Sélectionner";  
//    private String delete       = "Supprimer";
    
    private String open = MessagesManager.get("system_open");
    private String paste = MessagesManager.get("system_paste");
    private String copy  = MessagesManager.get("system_copy");
    private String select_all = MessagesManager.get("system_select_all");
    private String delete = MessagesManager.get("system_delete");

    /** The parent Window */
    private Window parent = null;
    
    /** The JTable to add a contextual pop menu */
    private JTable jTable = null;

    /**
     * Default is that jTable is deletable
     */
    private boolean isDeletable =  true;


    /**
     * Says if the files must be really deleted
     */
    private boolean realDeleteOfFiles = false;
    
    /**
     * Constructor
     * 
     * @param parent
     * @param jtable         The JTable to add a contextual pop menu
     */

    public FileTableClipboardManagerGroupFile(Window parent, JTable jtable)
    { 
        this.parent = parent;
        this.jTable = jtable;
        createClipboard();
    }

    /**
     * Constructor
     * 
     * @param parent
     * @param jtable         The JTable to add a contextual pop menu
     * @param isDeletable    if true the files may be deleted
     */
    public FileTableClipboardManagerGroupFile(Window parent, JTable jtable,  boolean isDeletable)
    {          
        this.parent = parent;
        this.jTable = jtable;
        this.isDeletable = isDeletable;
        
        createClipboard();
    }

        
    /**
     * @return the realDeleteOfFiles
     */
    public boolean isRealDeleteOfFiles()
    {
        return realDeleteOfFiles;
    }

    /**
     * @param realDeleteOfFiles the realDeleteOfFiles to set
     */
    public void setRealDeleteOfFiles(boolean realDeleteOfFiles)
    {
        this.realDeleteOfFiles = realDeleteOfFiles;
    }

    /**
     * Main action to cre   te the Clipboard
     * 
     * @param jtable The JTable to add a contextual pop menu
     */

    public void createClipboard()
    {         
        jTable.addKeyListener(new KeyAdapter() { 
            public void keyReleased(KeyEvent e) 
            { 
                jTableKeyReleased(e);                                 
            } 
        });
        
        popupMenu = new JPopupMenu();        
        popupMenu.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

        menuItemOpen = new JMenuItem(new DefaultEditorKit.CutAction());
        menuItemOpen.setText(open);
        menuItemOpen.addActionListener((new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                jTableClipboardActionPerformed(e);
            }})); 
        popupMenu.add(menuItemOpen);   

        
        JMenuItem menuItemPaste = new JMenuItem(paste);
        menuItemPaste.addActionListener((new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                jTableClipboardActionPerformed(e);
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
                jTableClipboardActionPerformed(e);
            }}));
        menuItemDelete.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_DELETE, 0));
        menuItemDelete.setIcon(ImageParmsUtil.createImageIcon(ParmsUtil.DELETE_ICON));
        popupMenu.add(menuItemDelete);
        
        if ( !isDeletable) {
            menuItemDelete.setEnabled(false);
        }

        popupMenu.addSeparator();

        JMenuItem menuItemSelectAll = new JMenuItem(select_all);
        menuItemSelectAll.setText(select_all);
        menuItemSelectAll.addActionListener((new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                jTableClipboardActionPerformed(e);
            }})); 
        popupMenu.add(menuItemSelectAll);        

        jTable.addMouseListener(new MouseAdapter() { 

            @Override
            public void mousePressed(MouseEvent e) {
                jTableMouseReleased(e);
            }
                        
            @Override
            public void mouseReleased(MouseEvent e) 
            { 
                jTableMouseReleased(e); 
            } 
                             
        });
        
        // 
        // This external Open Source (LGPL) componant handles all drag and drop
        // See http://iharder.sourceforge.net/filedrop/
        // Note: No learning needed!
        // 
        
        //JOptionPaneCustom.showMessageDialog(null, this.getClass().getName() + " 1");
        
        
        new FileDrop(parent, new FileDrop.Listener()
        {
            public void filesDropped(File[] files)
            {
                // handle file drop  
                add(files);               
                             
            } // end filesDropped
        }); // end FileDrop.Listener
        
        
    }

    private void jTableKeyReleased(KeyEvent e) 
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
    public void jTableMouseReleased(MouseEvent e) 
    { 

        // These are disabled because the Table is not editable
        menuItemOpen.setEnabled(false);
        menuItemDelete.setEnabled(false);

        int [] selRows = jTable.getSelectedRows();

        if (selRows.length == 0) {
            menuItemOpen.setEnabled(false);
            menuItemDelete.setEnabled(false);
        } else {
            menuItemOpen.setEnabled(true);

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
    public void jTableClipboardActionPerformed(ActionEvent e)
    {        
        //System.out.println("e.getActionCommand(): " + e.getActionCommand());

        if ( e.getActionCommand().equals(select_all))
        {           
            jTable.selectAll();
        }        
        else if (e.getActionCommand().equals(copy)) {
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

        } 
        else if ( e.getActionCommand().equals(paste))
        {
            add(getFilesFromClipboard());
        }         
        else if ( e.getActionCommand().equals(open))
        {
            // Put the content of the jTable in a JTextField
            int [] selRows = jTable.getSelectedRows();

            if (selRows.length > 0) 
            {
                // get Table data
                TableModel tm = jTable.getModel();

                // Force the cast of the getValueAt into a String
               String fileStr = (String) tm.getValueAt(selRows[0], 0);       
                              
               File file = new File(fileStr);
              
               
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
             
        Set<String> filesSet = new HashSet<String>();
        
        for (int i = 0; i < jTable.getRowCount(); i++)
        {
            String fileName= (String)jTable.getValueAt(i, 0);
            filesSet.add(fileName);            
        }
                
        List <File> filesList = Arrays.asList(files);
        
        DefaultTableModel tm2 = (DefaultTableModel)jTable.getModel(); 
               
        // 09/10/08 11:55 NDP - Dismiss PDF files as input
        //boolean pdfFileAdded = false;
        
        boolean messageDone = false;
        
        boolean dropWithDuplicate = false;
        
        for (int i= 0; i < filesList.size() ; i++) 
        { 
            File file = filesList.get(i);
                        
            if (! file.getName().toLowerCase().endsWith(".pdf"))
            {
                if (! messageDone)
                {
                    JOptionPane.showMessageDialog(parent, 
                              MessagesManager.get("only_pdf_files_are_accepted"),
                              ParmsConstants.APP_NAME, JOptionPane.ERROR_MESSAGE);
                }
                    
                messageDone = true;    
                continue;
            }
            
            Set<String> filenamesOnly = new HashSet<>();
            for (Iterator<String> it = filesSet.iterator(); it.hasNext();) {
                String filename = it.next();
                filename = StringUtils.substringAfterLast(filename, File.separator);
                filenamesOnly.add(filename);
            }
            
            if (! filenamesOnly.contains(file.getName()))
            {
                Vector fileVector = new Vector();
                fileVector.add(file.toString());
                fileVector.add(file.length());
                fileVector.add(new Timestamp(file.lastModified()));
                tm2.addRow(fileVector);   
            }          
            else {
                    dropWithDuplicate = true;
            }
        }        
        
        if (dropWithDuplicate) {
            JOptionPane.showMessageDialog(parent,
                    MessagesManager.get("warning_file_name_duplicates_are_ignored"), ParmsConstants.APP_NAME, JOptionPane.INFORMATION_MESSAGE);
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
        
        int [] selRows = jTable.getSelectedRows();
        
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
        
        List<Integer> listRows = new Vector<Integer>();
        
        for (int i = 0; i < selRows.length; i++)
        {
            listRows.add(jTable.convertRowIndexToModel(selRows[i]));
        }
        
        // get Table data and remove the selected rows
        
        DefaultTableModel tm = (DefaultTableModel)jTable.getModel();
        
        for (int i= 0; i < listRows.size() ; i++) 
        {                
            Integer rowIndex = (Integer)listRows.get(i);
            
            File file = new File((String)tm.getValueAt(rowIndex.intValue(), 0));
            
            if(realDeleteOfFiles)
            {
            	file.delete();
            }
        }

        // For values ro be sorted in asc
        Collections.sort(listRows);
        
        // Remove the row(s)
        for (int i = listRows.size() -1 ; i >= 0  ; i--) 
        {            
            Integer rowIndex = listRows.get(i);
            tm.removeRow(rowIndex);
        }        
        
         jTable.setModel(tm);
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
}


