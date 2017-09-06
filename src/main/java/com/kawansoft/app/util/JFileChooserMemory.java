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

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;
import java.util.prefs.Preferences;

import javax.swing.JFileChooser;

/**
 * @author Nicolas de Pomereu
 *
 * A JFileChooser that saves the last accessed current directory when calling
 * showSaveDialog() or a showOpenDialog().
 *
 */
public class JFileChooserMemory extends JFileChooser {

    public static String DEFAULT_DIRECTORY = "DEFAULT_DIRECTORY";
    
    /**
     * Constructor Will open the JFileChooser in the saved default directory
     */
    public JFileChooserMemory() {

        String currentDirectory = getPreference(DEFAULT_DIRECTORY);

        //System.out.println("currentDirectory: " + currentDirectory);

        if (currentDirectory != null) {
            this.setCurrentDirectory(new File(currentDirectory));
        }

    }

    public int showSaveDialog(Component parent)
            throws HeadlessException {

        int i = super.showSaveDialog(parent);
        String currentDirectory = this.getCurrentDirectory().toString();

        setPreference(DEFAULT_DIRECTORY, currentDirectory);
        return i;
    }

    public int showOpenDialog(Component parent)
            throws HeadlessException {

        int i = super.showOpenDialog(parent);
        String currentDirectory = this.getCurrentDirectory().toString();

        setPreference(DEFAULT_DIRECTORY, currentDirectory);
        return i;
    }
    
        /**
     * Set preference
     */ 
    private void setPreference(String prefName, String prefValue)
    {       
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());  
        prefs.put(prefName,  prefValue);
    }
        
    /**
     * Get a  preference
     */ 
    public String getPreference(String prefName)
    {
        
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());         
        return prefs.get(prefName, null);
    }
}
