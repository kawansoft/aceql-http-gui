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

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.apache.commons.io.IOUtils;

/**
 * 
 * Class to store strings at end of a file.
 * 
 * @author Nicolas de Pomereu
 *
 */
public class AppendStore  implements Closeable{
    	
    /**	The CR LF chars */
    public static final String CR_LF = System.getProperty("line.separator") ;
	
    /** The Random access file to use */
    private RandomAccessFile raf = null;

    /**
     * Constructor
     * @param file	the file to use as store
     * @throws java.io.IOException
     */
    public AppendStore(File file) throws IOException {
	super();
	
	raf = new RandomAccessFile(file, "rw");
	long length = raf.length();
	raf.seek(length);
    }
    
    /**
     * Appends a string at the end of the store
     * 
     * @param s	 the text top append
     * @throws IOException
     */
    public synchronized void append(String s) throws IOException {
	 raf.writeBytes(s + CR_LF);
    }
    
    /**
     * Returns the store length
     * @return the store length
     * @throws IOException
     */
    public synchronized long length() throws IOException {
	return raf.length();
    }
    
    /**
     * Closes the file
     **/
    @Override
    public synchronized void close() throws IOException {
	//IOUtils.closeQuietly(raf);
        if (raf != null) {
            try {
                raf.close();
            } catch (Exception exception) {
            }
        }
    }
}
