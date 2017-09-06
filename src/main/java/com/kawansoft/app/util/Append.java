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

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Append is a tool to dump data in a file in append mode.
 * It is usefull for logs. 
 * <br>
 * The file is close immediatly closed after each append()
 */

public class Append
{
	// constants
	
	/**	The CR LF chars */
	public static final String CR_LF = System.getProperty("line.separator") ;
	
	// fields
	
	/**	The file to append to */
	private File m_fFile ;
	
	// constructor
	
	/**
	 * Creates a new append tool to write to the given file.
	 * @param	fFile	the file to append data to
	 */
	
	public Append(File fFile)
	{
		m_fFile = fFile ;
	}
	
	// services
	
	/**
	 * Appends the given line to the file.
	 * The CR+LF chars are append to the line.
	 * @param	sLine	the line to append
	 * @exception	IOException		if an i/o error occured
	 */
	
	public void appendLine(String sLine)
		throws IOException
	{
		append(sLine + CR_LF) ;
	}
	
	
	/**
	 * Appends the given line to the file.
	 * No CR or LF char is append to the line.
	 * @param	sLine	the line to append
	 * @exception	IOException		if an i/o error occured
	 */
	
	public void append(String sLine)
		throws IOException
	{
		append(sLine.getBytes()) ;
	}
	
	
	/**
	 * Appends the given bytes to the file.
	 * @param	b	the byte to append
	 * @exception	IOException		if an i/o error occured
	 */
	
	public void append(byte[] b)
		throws IOException
	{
		RandomAccessFile raf = new RandomAccessFile(m_fFile, "rw") ;
		int nSkip = 0 ;
		int nLen = (int)raf.length() ;
		while((nSkip += raf.skipBytes(nLen - nSkip)) < nLen)
		{
			// go to the end of the file
		}
		raf.write(b) ;
		raf.close() ;
	}
}
