//JFileChooserFactory.java
//Copyright (c) SafeLogic, 2000 - 2005
//
//Last Updates: 
// 5 juin 2006 23:49:16 Nicolas de Pomereu

package com.kawansoft.app.util;

import javax.swing.JFileChooser;

/**
 * Wrapper class to JFileChooser
 * <br>
 * Done because there is a bug in Java 1.5.0_06 that prevents file selection if there is
 * a recursiv shortcut in the directory to choose in.
 * 
 * @author Nicolas de Pomereu
 *
 */
public class JFileChooserFactory
{
    /**
     * Constructor
     */
    protected JFileChooserFactory()
    {

    }
    
    /**
     * Get the JFileChooser instance
     * <br>
     * Will load a normal JFileChooser, except if the file use_shell_folder.txt
     * exisre in the pGeep keys Directory
     * @return
     */
    public static JFileChooser getInstance()
    {
        
        if (false)
        {
            // Special Light File Chooser 
            JFileChooser saveTo = new JFileChooser() {
                public void updateUI() {
                    putClientProperty("FileChooser.useShellFolder", new Boolean(true));
                    super.updateUI();
                }
            };
            
            return saveTo;
        }
        else
        {
            // Normal File Chooser 
            JFileChooser saveTo = new JFileChooser();        
            return saveTo;
        }

    }
}

