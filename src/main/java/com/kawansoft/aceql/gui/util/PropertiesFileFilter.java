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
package com.kawansoft.aceql.gui.util;
/**
 * File Filter for PDF Like files
 */

import java.io.File;

import javax.swing.filechooser.FileFilter;


public class PropertiesFileFilter extends FileFilter {

    /** The array of Word like extensions (common known usage) */
    public static final String[] PROPERTIES_LIKE_EXTENSIONS = 
    {   
        ".properties",      
    };

    /**
     * Accept all directories and all Word Like extensions
     */
    @Override
    public boolean accept(File f) 
    {
        if (f.isDirectory())
        {
            return true;
        }
        return PropertiesFileFilter.isPropertiesLikeExtension(f);

    }

    /**
     * @return The description of this filter
     */
    
    @Override
    public String getDescription() 
    {
        String extensions = "";
        for (int i = 0; i < PropertiesFileFilter.PROPERTIES_LIKE_EXTENSIONS.length; i++)
        {
            extensions += "*" + PropertiesFileFilter.PROPERTIES_LIKE_EXTENSIONS[i];
            
            if ( i != PropertiesFileFilter.PROPERTIES_LIKE_EXTENSIONS.length - 1)
            {
                extensions += ", ";
            }
        }
        
        return "Propertie: (" + extensions + ")";
               
    }

    /**
     * @param file  the Properties file
     * @return  true if the extension is one of a Word type : .properties
     */    
    public static boolean isPropertiesLikeExtension(File file)
    {
        if (file == null)
        {
            throw new IllegalArgumentException("File is null!");
        }
        
        String fileName = file.getName();
        fileName = fileName.toLowerCase();
        
        for (int i = 0; i < PROPERTIES_LIKE_EXTENSIONS.length; i++)
        {
            if (fileName.endsWith(PROPERTIES_LIKE_EXTENSIONS[i]))
            {
                return true;
            }
        }
        
        return false;
    }
}

