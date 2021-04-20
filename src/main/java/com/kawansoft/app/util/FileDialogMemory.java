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

import java.awt.Dialog;
import java.awt.FileDialog;
import java.awt.Frame;
import java.util.prefs.Preferences;


/**
 * @author Nicolas de Pomereu
 *
 * A JFileChooser that saves the last accessed current directory when calling
 * showSaveDialog() or a showOpenDialog().
 *
 */
public class FileDialogMemory extends FileDialog {

    public static String DEFAULT_DIRECTORY = "DEFAULT_DIRECTORY";
    
    /**
     * Constructor Will open the JFileChooser in the saved default directory
     * @param parent
     * @param title
     * @param mode
     */
    public FileDialogMemory(Frame parent, String title, int mode) {
        super(parent, title, mode);
        String currentDirectory = getPreference(DEFAULT_DIRECTORY);

        //System.out.println("currentDirectory: " + currentDirectory);

        if (currentDirectory != null) {
            this.setDirectory(currentDirectory);
        }
    }
    
    public FileDialogMemory(Dialog parent, String title, int mode) {
        super(parent, title, mode);
        String currentDirectory = getPreference(DEFAULT_DIRECTORY);

        //System.out.println("currentDirectory: " + currentDirectory);

        if (currentDirectory != null) {
            this.setDirectory(currentDirectory);
        }
    }

    public void setVisible(boolean b) {
        super.setVisible(b);        
        setPreference(DEFAULT_DIRECTORY, super.getDirectory());
    }
    
        /**
     * Set preference
     */ 
    private void setPreference(String prefName, String prefValue)
    {       
        if (prefValue == null) {
            return;
        }
        
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());  
        prefs.put(prefName,  prefValue);
    }
        
    /**
     * Get a  preference
     */ 
    private String getPreference(String prefName)
    {
        
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());         
        return prefs.get(prefName, null);
    }
}
