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

import java.awt.Component;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.filechooser.FileSystemView;


public class FilesListCellRenderer extends JLabel
implements ListCellRenderer {
  
    
    /** If true, all files have same extensions and we set the icon once */
    public static boolean SAME_EXT_FOR_ALL_FILES = true;

    private FileSystemView fsv = FileSystemView.getFileSystemView();
    private Icon icon = null;
    
    public FilesListCellRenderer() 
    {
        setOpaque(true);
    }

    /**
     * 
     * @param list
     * @param value
     * @param index
     * @param isSelected
     * @param cellHasFocus
     * @return 
     */
    @Override
    public Component getListCellRendererComponent(
            JList list,
            Object value,
            int index,
            boolean isSelected,
            boolean cellHasFocus) 
    {
                
        if (isSelected) {
            setBackground(list.getSelectionBackground());
            setForeground(list.getSelectionForeground());
        } else {
            setBackground(list.getBackground());
            setForeground(list.getForeground());
        }
                
        //setForeground(URL_COLOR);

        File file = (File)value;
        
        if (file != null)
        {
            setText(file.getName() + " ");
            
            if (SAME_EXT_FOR_ALL_FILES) {
                if (icon == null) {
                    icon = fsv.getSystemIcon(file);
                }
                setIcon(icon);
            }
            else {
                icon = fsv.getSystemIcon(file);
                setIcon(icon);
            }

            
            
        }

        return this;
    }
}