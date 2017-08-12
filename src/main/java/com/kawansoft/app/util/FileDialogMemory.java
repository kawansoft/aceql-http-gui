//JFileChooserMemory.java
//Copyright (c) SafeLogic, 2000 - 2005
//
//Last Updates: 
// 1 f�vr. 2009 18:33:12 Nicolas de Pomereu
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