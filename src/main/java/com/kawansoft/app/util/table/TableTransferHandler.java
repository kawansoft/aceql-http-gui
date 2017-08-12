//TableTransferHandler.java
//Copyright (c) SafeLogic, 2000 - 2005
//
//Last Updates: 
// 30/07/08 13:10 NDP - Fix remaining bugs

package com.kawansoft.app.util.table;

/*
 * TableTransferHandler.java is used by the 1.4
 * ExtendedDnDDemo.java example.
 */

import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TableTransferHandler extends StringTransferHandler {
    
    private int[] rows = null;
    
    //private int addIndex = -1; //Location where items were added
    //private int addCount = 0;  //Number of items added.

    private boolean isDropable = false;
    
    /** The Set of all first columns values */
    private static Set firstColumns = new HashSet();
    
    private static boolean importDone = false;
    
    public TableTransferHandler()
    {
        isDropable = false;
    }
        
    /**
     * @param isDropable if true, Table with be dropable
     */
    public TableTransferHandler(boolean isDropable)
    {
        this.isDropable = isDropable;
    }
    
    protected String exportString(JComponent c) {
        
        importDone = false;
        
        JTable table = (JTable)c;
        rows = table.getSelectedRows();
        int colCount = table.getColumnCount();
        
        StringBuffer buff = new StringBuffer();
       
        for (int i = 0; i < rows.length; i++) {
            for (int j = 0; j < colCount; j++) {
                Object val = table.getValueAt(rows[i], j);
                buff.append(val == null ? "" : val.toString());
                
                if (j != colCount - 1) 
                {
                    buff.append(",");
                }
            }
          
            if (i != rows.length - 1) {
                buff.append("\n");
            }
           
        }
        
        //JOptionPane.showMessageDialog(null, buff);
        
        return buff.toString();
    }

    protected void importString(JComponent c, String str) {
        JTable target = (JTable)c;
        DefaultTableModel model = (DefaultTableModel)target.getModel();
        int index = target.getSelectedRow();

        if (! isDropable)
        {
            //JOptionPane.showMessageDialog(null, "TableTransferHandler.isDropable = " + isDropable);
            return;
        }
        
        //Prevent the user from dropping data back on itself.
        //For example, if the user is moving rows #4,#5,#6 and #7 and
        //attempts to insert the rows after row #5, this would
        //be problematic when removing the original rows.
        //So this is not allowed.
        if (rows != null && index >= rows[0] - 1 &&
              index <= rows[rows.length - 1]) {
            rows = null;
            return;
        }

        int max = model.getRowCount();
        if (index < 0) {
            index = max;
        } else {
            index++;
            if (index > max) {
                index = max;
            }
        }
        
        //addIndex = index;
        
        String[] values = str.split("\n");
        
        //JOptionPane.showMessageDialog(null, values);
        
        //addCount = values.length;
        
        int colCount = target.getColumnCount();
                            
        // Compute first columns  valurs for each row      
        firstColumns = new HashSet();
        for (int i = 0; i < model.getRowCount() ; i++) 
        {
            Object firstColumn = model.getValueAt(i, 0);                
            firstColumns.add(firstColumn);
        }
        
        //for (int i = 0; i < values.length && i < colCount; i++) 
        for (int i = 0; i < values.length; i++) 
        {   
            String[] columns = values[i].split(",");
            
            // Do not add existing row, based on index
            if (firstColumns.contains(columns[0]))
            {
                continue;
            }
            
            model.insertRow(index++, values[i].split(","));
        }
        
        target.updateUI();
        importDone = true;
    }
    
    protected void cleanup(JComponent c, boolean remove) 
    {
        //JOptionPane.showConfirmDialog(null, "remove! in cleanup!");
        
        if (! importDone)
        {
            rows = null;
            return;
        }
              
        JTable source = (JTable)c;
        
        System.out.println();
        System.out.println("rows.length: " + rows.length);
        for (int i = 0; i < rows.length; i++)
        {
            System.out.println("rows[" + i + "] = " +rows[i]);
        }
        
        if (remove && rows != null) 
        {
            DefaultTableModel model =
                 (DefaultTableModel)source.getModel();
            
            for (int i = rows.length - 1; i >= 0; i--) {
                model.removeRow(rows[i]);
            }                        
        }
        
        rows = null;
        
        /* OLD CODE: DID NOT WORK
        remove = true; // SafeLogic HACK
        
        JTable source = (JTable)c;
                
        source.updateUI();
                
        if (remove && rows != null) {
            DefaultTableModel model =
                 (DefaultTableModel)source.getModel();

            //If we are moving items around in the same table, we
            //need to adjust the rows accordingly, since those
            //after the insertion point have moved.
            
            
            if (addCount > 0) {
                for (int i = 0; i < rows.length; i++) {
                    if (rows[i] > addIndex) {
                        rows[i] += addCount;
                    }
                }
            }
            
            
            System.out.println();
            System.out.println("rows.length: " + rows.length);
            
            for (int i = 0; i < rows.length; i++)
            {
                System.out.println("rows[" + i + "] = " +rows[i]);
            }
            
            for (int i = rows.length - 1; i >= 0; i--) {
                model.removeRow(rows[i]);
            }
            
        }
        
        rows = null;
        addCount = 0;
        addIndex = -1;
        */
    }
}
