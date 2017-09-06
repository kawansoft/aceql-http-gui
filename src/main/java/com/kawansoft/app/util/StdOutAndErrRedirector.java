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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

/**
 * @author Nicolas de Pomereu
 *
 */
public class StdOutAndErrRedirector {

    /**
     * Static class
     */
    protected StdOutAndErrRedirector() {

    }
    
    /**
     * Redirects the Java stdout and sterr to directory/stdout.txt & directory/stderr.txt
     * @param directory
     * @throws IOException
     */
    public static void redirect(File directory) throws IOException {
	
	File fileOut = new File(directory.toString() + File.separator + "stdout.txt");
	File fileErr =  new File(directory.toString() + File.separator + "stderr.txt");
	
	PrintStream out = new PrintStream(new FileOutputStream(fileOut));
	PrintStream err = new PrintStream(new FileOutputStream(fileErr));
	
	System.setOut(out);
	System.setErr(err);
    }

}
