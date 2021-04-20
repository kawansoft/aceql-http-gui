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

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class LockerTest {
   public static void main(String arsg[])
       throws IOException {
     
       File f = new File("c:\\test\\test.wav");
       if (f.exists())
       {
           System.out.println(f + " exists!");
       }
       
       if (f.canWrite())
       {
           System.out.println(f + " canWrite!");
       }
           
       RandomAccessFile raf =
       new RandomAccessFile("c:\\test\\test.wav", "rw");
     
     //if (true) return;
     
     FileChannel channel = raf.getChannel();
     
     FileLock lock = channel.lock();
     try {
       System.out.println("Got lock!!!");
       System.out.println("Press ENTER to continue");
       
       System.in.read(new byte[10]);

       System.out.println("Ok, Done...");
       
     } finally {
       lock.release();
     }
   }
}
 

