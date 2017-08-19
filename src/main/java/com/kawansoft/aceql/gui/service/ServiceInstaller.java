
package com.kawansoft.aceql.gui.service;

import static com.kawansoft.aceql.gui.AceQLManager.getJdbcDrivers;
import com.kawansoft.app.parms.util.ParmsUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang3.SystemUtils;
import javax.swing.JOptionPane;



/**
 * Utility class to install or uninstall the AceQLHTTPService service
 * 
 * @author Nicolas de Pomereu
 *
 */

public class ServiceInstaller {
    
    public static boolean DEBUG = false;
    
    public static final String CR_LF = System.getProperty("line.separator");
        
    /**
     * Protected
     */
    protected ServiceInstaller() {
	
    }

   
    /**
     * Installs the AceQLHTTPService.exe service in specified directory.
     * Call is done via  via a bat wrapper because of Windows Authorization.
     * @param serviceDirectory	the directory into which install the service
     * @param userDirectory the value of user.dir to set
     * @param homeDirectory the value of user.home to set
     * @throws IOException
     */
    public static void install(String serviceDirectory, String userDirectory, String homeDirectory) throws IOException {
	
        if (!SystemUtils.IS_OS_WINDOWS) {
            return;
        }
        
        /*
        @echo off
        start /min AceQLHTTPService.exe //IS//AceQLHTTPService  ^
         --DisplayName="AceQL HTTP Server" ^
         --Install="%CD%"\AceQLHTTPService.exe ^
         --Description="AceQL HTTP Server - SQL Over HTTP - https://www.aceql.com" ^
         --Jvm=auto ^
         --JvmOptions=-Xrs;-Xms128m;-Xmx256m;-Duser.dir=%1;-Duser.home=%2 ^
         --Classpath=%1\AceQL\lib-server/*;%1\AceQL\lib-jdbc/*;%CLASSPATH% ^
         --StartMode=jvm ^
         --StartClass=com.kawansoft.aceql.gui.service.AceQLServiceControler ^
         --StartMethod=start ^
         --StopMode=jvm ^
         --StopClass=com.kawansoft.aceql.gui.service.AceQLServiceControler ^
         --StopMethod=stop ^
         --LogPath=%2\windows-service-logs ^
         --StdOutput=auto ^
         --StdError=auto ^
         --Startup=auto ^
         --ServiceUser=System ^ 
         exit
        */
        
	ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C", 
                "installService.bat", userDirectory, homeDirectory);
        pb.directory(new File(serviceDirectory));
	Process p = pb.start();
        
        //printProcessDisplay(p);
        
        try {
	    p.waitFor();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
        
    }
    
    public static void updateService(String serviceDirectory) throws IOException {

        if (!SystemUtils.IS_OS_WINDOWS) {
            return;
        }
 
        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C", 
                "updateService.bat");
        pb.directory(new File(serviceDirectory));
	Process p = pb.start();
        
        //printProcessDisplay(p);
        
        try {
	    p.waitFor();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
        
    }
        

    /**
     * Installs the AceQLHTTPService.exe service in specified directory.
     * Call is done via  via a bat wrapper because of Windows Authorization.
     * @param directory	the directory into which install the service
     * @throws IOException
     */
    public static void uninstall(String directory) throws IOException {
	
        if ( !SystemUtils.IS_OS_WINDOWS) {
            return;
        }
        
	ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C",
                "uninstallService.bat");
        pb.directory(new File(directory));
	Process p= pb.start();
	
        try {
	    p.waitFor();
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
    }
    
    /**
     * Prints on System.out the result of the process
     * @param p	the process to display the result for
     */
    @SuppressWarnings("unused")
    private static void printProcessDisplay(Process p) {
	BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String ligne = "";
        try {
            while ((ligne = br.readLine()) != null) {
                System.out.println(ligne);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

 
    
    /**
     * Return the directory into which is installed the service
     *
     * @return
     */
    public static String getServiceDirectory() {

       String directory = SystemUtils.USER_DIR + File.separator + "AceQL" + File.separator + "service";
       return directory;
    }
    

}
