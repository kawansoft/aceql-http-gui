
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

        if (PropertiesFileFilter.isPropertiesLikeExtension(f))   
        {
            return true;
        }
       
        return false;
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

