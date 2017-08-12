//LockerTest.java
//Copyright (c) SafeLogic, 2000 - 2005
//
//Last Updates: 
// 27 oct. 06 17:13:12 Nicolas de Pomereu

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
 

