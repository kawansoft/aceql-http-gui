/**
 * 
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
	IOUtils.closeQuietly(raf);
    }
}
