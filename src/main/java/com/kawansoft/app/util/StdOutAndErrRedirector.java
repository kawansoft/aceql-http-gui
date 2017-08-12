/**
 * 
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
