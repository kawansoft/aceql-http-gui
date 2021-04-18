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
package com.kawansoft.app.util.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


/**
 * Basic Table Cell Renderer for Files:
 * 
 * <br> - Will be used to alternate background colors.
 * <br> - The second column (aka number 1) is right justified because it's the size value.
 * <br> - The third  column (aka number 2) is center justified (date modified)
 */

public class BasicFilesTableCellRenderer extends  DefaultTableCellRenderer  {

    /** To use for background selection */
    public static Color LIGHT_BLUE = new Color(243, 243, 255);
        
    /** The icon to use for files */
    private Icon icon = null;
    
    /**
     * Constructor    
     * @param icon the icon to set on the filename (none if null)
     */
    public BasicFilesTableCellRenderer(Icon icon)
    {
        this.icon = icon;
    }
     
    public Component getTableCellRendererComponent(
                            JTable table, Object value,
                            boolean isSelected, boolean hasFocus,
                            int row, int column) 
    {
        
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		                    
        // The icon to set on filename (PDF)
        if (column == 0 && icon != null)
        {
            setIcon(icon);
        }
                          
        if (column == 1)
        {
            setHorizontalAlignment(JLabel.RIGHT);
        }
        else if (column == 2)
        {
            setHorizontalAlignment(JLabel.CENTER);
        }

        // This is done to set alternate colors on table background
        // Always set if no rows selected
        if (row % 2 == 0)
        {
            c.setBackground(LIGHT_BLUE);
        }
        else
        {
            c.setBackground(Color.white);
        }
        
        c.setForeground(Color.black);
        
        int [] selRows = table.getSelectedRows();
        if (selRows.length != 0)
        {
          // Is the row inside the selected rows ?
          for (int i = 0; i < selRows.length; i++)
          {
              if (row == selRows[i] ) 
              {
                   c.setBackground(table.getSelectionBackground());      
                   c.setForeground(Color.white);     
              }               
          }            
        }
 
        // Important if there are splitted lines
         int height = c.getPreferredSize ().height;
         if (height > table.getRowHeight (row))
         {
             table.setRowHeight (row, height);
         }        
        
        return c;                
    }

}


