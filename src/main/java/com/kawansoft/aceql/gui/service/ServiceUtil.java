/**
 * 
 */
package com.kawansoft.aceql.gui.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;

/**
 * Helper class for windows services.
 * 
 * @author Nicolas de Pomereu
 *
 */
public class ServiceUtil {

    public static final String ACEQL_HTTP_SERVICE = "AceQLHTTPService";
        
    public static final int NOT_INSTALLED = -1;
    public static final int STOPPED = 1;
    public static final int STARTING = 2;
    public static final int STOPPING = 3;
    public static final int RUNNING = 4;
    
    public static final int AUTO_START = 2;
    public static final int DEMAND_START = 3;
    public static final int DISABLED = 4;
    
    /**
     * Protected
     */
    protected ServiceUtil() {

    }


    /**
     * Says if AceQLHTTPService is installed
     * @return true if AceQLHTTPService is installed
     * @throws IOException
     */
    public static boolean isInstalled() throws IOException {
        int status = ServiceUtil.getServiceStatus(ACEQL_HTTP_SERVICE);
        if (status == ServiceUtil.NOT_INSTALLED) {
            return false;
        } else {
            return true;
        }
    }
    
     /**
     * Says if AceQLHTTPService is running
     * @return true if AceQLHTTPService is running
     * @throws IOException
     */
    public static boolean isRunning() throws IOException {
        int status = ServiceUtil.getServiceStatus(ACEQL_HTTP_SERVICE);
        if (status == ServiceUtil.RUNNING){
            return true;
        } else {
            return false;
        }
    }

    
    /**
    * Says if AceQLHTTPService is stopped
    * @return true if AceQLHTTPService is stopped
    * @throws IOException
    */
   public static boolean isStopped() throws IOException {
       int status = ServiceUtil.getServiceStatus(ACEQL_HTTP_SERVICE);
       if (status == ServiceUtil.STOPPED){
           return true;
       } else {
           return false;
       }
   }
    
    /**
    * Says if AceQLHTTPService is running
    * @return true if AceQLHTTPService is running
    * @throws IOException
    */
   public static boolean isStarting() throws IOException {
       int status = ServiceUtil.getServiceStatus(ACEQL_HTTP_SERVICE);
       if (status == ServiceUtil.STARTING){
           return true;
       } else {
           return false;
       }
   }
    
    /**
     * Start the service manager AceQLHTTPServicew.exe via a bat wrapper because of Windows Authorization.
     * The service manager AceQLHTTPServicew.exwith same interface a Tomcat
     * @param directory
     * @parm directory	the directory into which serviceProperties.bat is installed
     * @throws IOException
     */
    public static void startServiceProperties(String directory) throws IOException {
	
        if ( !SystemUtils.IS_OS_WINDOWS) {
            return;
        }
        
	ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C", 
                "serviceProperties.bat");
	
        pb.directory(new File(directory));
	Process p = pb.start();
        
        try {
	    p.waitFor();
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    /**
     * Starts the SimplifyUploaderServic service via a bat wrapper because of Windows Authorization.
     * @param directory
     * @parm directory	the directory into serviceProperties.bat ins install
     * @throws IOException
     */
    public static void startService(String directory) throws IOException {
	
        if ( !SystemUtils.IS_OS_WINDOWS) {
            return;
        }
        
	ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C", 
                "startService.bat");
	
        pb.directory(new File(directory));
	Process p = pb.start();
        
        try {
	    p.waitFor();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
        
        
//        long start = System.currentTimeMillis();
//        while (true) {
//            try {
//		Thread.sleep(20);
//	    } catch (InterruptedException e) {
//		e.printStackTrace();
//	    }
//            long now = System.currentTimeMillis();
//            
//            if (now - start > TWENTY_SECONDS) {
//        	return false;
//            }
//            
//            if (ServiceUtil.isRunning()) {
//        	return true;
//            }
//        }
    }
    
    /**
     * Stops the SimplifyUploaderServic service via a bat wrapper because of Windows Authorization.
     * @param directory
     * @parm directory	the directory into serviceProperties.bat ins install
     * @throws IOException
     */
    public static void stopService(String directory) throws IOException {
	
        if ( !SystemUtils.IS_OS_WINDOWS) {
            return;
        }
        
	ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C", 
                "stopService.bat");
	
        pb.directory(new File(directory));
	Process p = pb.start();
        
        try {
	    p.waitFor();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
        
//        long start = System.currentTimeMillis();
//        while (true) {
//            try {
//		Thread.sleep(20);
//	    } catch (InterruptedException e) {
//		e.printStackTrace();
//	    }
//            long now = System.currentTimeMillis();
//            
//            if (now - start > TWENTY_SECONDS) {
//        	return false;
//            }
//            
//            if (ServiceUtil.isStopped()) {
//        	return true;
//            }
//        }
    }
    
    
    /**
     * Returns the status of the passed service
     * @param serviceName	the sercie name
     * @return	the status of the service
     * @throws IOException
     */
    public static int getServiceStatus(String serviceName) throws IOException {

        if ( !SystemUtils.IS_OS_WINDOWS) {
            return NOT_INSTALLED;
        }
        
	BufferedReader reader = null;

	try {
	    Process p = Runtime.getRuntime().exec("sc query " + serviceName);

	    reader = new BufferedReader(new InputStreamReader(
		    p.getInputStream()));

	    String line = reader.readLine();
	    while (line != null) {
		
		if (line.trim().startsWith("STATE"))
		{

		    if (line.trim()
			    .substring(line.trim().indexOf(":") + 1,
				    line.trim().indexOf(":") + 4).trim()
			    .equals("1"))
			//System.out.println("Stopped");
			return STOPPED;
		    else if (line
			    .trim()
			    .substring(line.trim().indexOf(":") + 1,
				    line.trim().indexOf(":") + 4).trim()
			    .equals("2"))
			//System.out.println("Starting....");
			return STARTING;
		    else if (line
			    .trim()
			    .substring(line.trim().indexOf(":") + 1,
				    line.trim().indexOf(":") + 4).trim()
			    .equals("3"))
			return STOPPING;
		    else if (line
			    .trim()
			    .substring(line.trim().indexOf(":") + 1,
				    line.trim().indexOf(":") + 4).trim()
			    .equals("4"))
			//System.out.println("Running");
			return RUNNING;

		}
		line = reader.readLine();
	    }
	    
	    return NOT_INSTALLED;

	} finally {
	    IOUtils.closeQuietly(reader);
	}
    }
    
    /**
     * Returns the start mode status of the passed service
     * @param serviceName	the sercie name
     * @return	the start mode of the service
     * @throws IOException
     */
    public static int getServiceStartMode(String serviceName) throws IOException {

        if ( !SystemUtils.IS_OS_WINDOWS) {
            return NOT_INSTALLED;
        }
        
	BufferedReader reader = null;

	try {
	    Process p = Runtime.getRuntime().exec("sc qc " + serviceName);

	    reader = new BufferedReader(new InputStreamReader(
		    p.getInputStream()));

	    String line = reader.readLine();
	    while (line != null) {
		
		if (line.trim().startsWith("START_TYPE"))
		{

		    if (line.trim()
			    .substring(line.trim().indexOf(":") + 1,
				    line.trim().indexOf(":") + 4).trim()
			    .equals("2"))
			//System.out.println("Stopped");
			return AUTO_START;
		    else if (line
			    .trim()
			    .substring(line.trim().indexOf(":") + 1,
				    line.trim().indexOf(":") + 4).trim()
			    .equals("3"))
			//System.out.println("Starting....");
			return DEMAND_START;
		    else if (line
			    .trim()
			    .substring(line.trim().indexOf(":") + 1,
				    line.trim().indexOf(":") + 4).trim()
			    .equals("4"))
			return DISABLED;

		}
		line = reader.readLine();
	    }
	    
	    return NOT_INSTALLED;

	} finally {
	    IOUtils.closeQuietly(reader);
	}
    }        

    /**
     * Returns the start mode status of the passed service
     * @param serviceName	the service name
     * @return	the start mode of the service
     * @throws IOException
     */
    public static String getServiceStartModeLabel(String serviceName) throws IOException {
        int startMode = getServiceStartMode(serviceName);
        
        if (startMode == NOT_INSTALLED) return "";
        else if (startMode == AUTO_START) return "Auto";
        else if (startMode == DEMAND_START) return "Manual";
        else if (startMode == DISABLED) return "Disabled";
        else return "Unknown";
        
    }
}
