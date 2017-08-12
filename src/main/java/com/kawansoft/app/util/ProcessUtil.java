
package com.kawansoft.app.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;

/**
 * @author Nicolas de Pomereu
 *
 */
public class ProcessUtil {

    /**
     * 
     */
    protected ProcessUtil() {
    }

    /**
     * Says if the program name is running. Check is done independent of caps.
     * @param programName	the program name to check
     * @return			true if the program name String is contained in task lists
     * @throws IOException
     */
    public static boolean isWindowsProgramRunning(String programName) throws IOException {
	
	if (! SystemUtils.IS_OS_WINDOWS) {
	    return false;
	}
	
        if (programName == null) {
            throw new NullPointerException("programName can not be null!");
        }
        
	String line;
	String pidInfo ="";

	Process p = Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe");

	BufferedReader input =  null;
	
	try {
	    input = new BufferedReader(new InputStreamReader(p.getInputStream()));

	    while ((line = input.readLine()) != null) {
	        pidInfo+=line; 
	    }
	} finally  {
	    IOUtils.closeQuietly(input);
	}

	if(pidInfo.toLowerCase().contains(programName.toLowerCase()))
	{
	    return true;
	}	
	else {
	    return false;
	}
    }
    
}
