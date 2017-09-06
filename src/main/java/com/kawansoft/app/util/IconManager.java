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

import com.kawansoft.app.parms.Parms;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.swing.Icon;
import javax.swing.ImageIcon;

import org.apache.commons.io.IOUtils;


/**
 * @author Nicolas de Pomereu
 * 
 *  Displays the Icon per file type
 */
public class IconManager
{

    /** the PDF icon */
    public static Icon pdfIcon = null;
    
    /** The Word Icon */
    public static Icon wordIcon = null;
    
    /**
     * protected
     */
    protected IconManager()
    {
    }

    /**
     * @return the PDF Icon
     */
    public static Icon getPdfIcon()
    {
        if (pdfIcon == null)
        {
            pdfIcon = getFileExtensionIcon(".pdf");
        }
        
        return pdfIcon;
    }
    
    /**
     * @return the PDF Icon
     */
    public static Icon getWordIcon()
    {
        if (wordIcon == null)
        {
            wordIcon = getFileExtensionIcon(".doc");
        }
        
        return wordIcon;
    }    
    
    /**
     * 
     * @param extension  the  generic extension with the dot. example ".pdf", ".doc";
     * @return
     */
    public static Icon getFileExtensionIcon(String extension)
    {
        if (extension == null)
        {
            throw new IllegalArgumentException("Extension can not be null!");
        }
        
        if (!extension.toLowerCase().startsWith("."))
        {
            throw new IllegalArgumentException("Extension must start with a dot");
        }
        
        InputStream examplePdfIn = null;
        OutputStream examplePdfOut = null;
        
        String exampleFile = "example" + extension;
        
        // Get a PDF icon in static memory
        try {
            examplePdfIn = Parms.class.getResourceAsStream("images/" + exampleFile);
            
            String tmpDir = System.getProperty("java.io.tmpdir");
            
            File examplePdf =  new File(tmpDir + File.separator + exampleFile);
            examplePdfOut = new FileOutputStream(examplePdf);
            IOUtils.copy(examplePdfIn, examplePdfOut);
            sun.awt.shell.ShellFolder sf = sun.awt.shell.ShellFolder.getShellFolder(examplePdf);
            
            return new ImageIcon(sf.getIcon(false));
            
        } catch (Exception e) {
            e.printStackTrace();
        } 
        finally
        {
            IOUtils.closeQuietly(examplePdfIn);
            IOUtils.closeQuietly(examplePdfOut);
        }
        
        return null;
    }        
    

}
