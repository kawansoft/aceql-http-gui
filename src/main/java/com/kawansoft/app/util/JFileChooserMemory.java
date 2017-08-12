//JFileChooserMemory.java
//Copyright (c) SafeLogic, 2000 - 2005
//
//Last Updates: 
// 1 f�vr. 2009 18:33:12 Nicolas de Pomereu
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