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
        boolean useClientProperty = false;
        if (useClientProperty)
        {
            // Special Light File Chooser 
            JFileChooser saveTo = new JFileChooser() {
                public void updateUI() {
                    putClientProperty("FileChooser.useShellFolder", Boolean.TRUE);
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

