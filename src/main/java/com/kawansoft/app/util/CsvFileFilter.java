//CsvFileFilter
//Copyright (c) SafeLogic, 2000 - 2005
//
//Last Updates: 
// 25 juil. 2006 11:11:48 Nicolas de Pomereu

package com.kawansoft.app.util;

/**
 * File Filter for CSV files
 */
import java.io.File;

import javax.swing.filechooser.FileFilter;

public class CsvFileFilter extends FileFilter {
    
    @Override
    public boolean accept(File f) 
    {
        if (f.isDirectory()) {
            return true;
        }

        String name = f.getName().toLowerCase();
        
        if (name.endsWith(".csv"))
        {
            return true;
        }
       
        return false;
    }

    //The description of this filter
    @Override
    public String getDescription() {
        return "CSV Format (*.csv)";
    }
}

