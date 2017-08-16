
package com.kawansoft.aceql.gui.task;

import com.kawansoft.aceql.gui.AceQLManager;
import static com.kawansoft.aceql.gui.AceQLManager.DEFAULT_HOST;
import static com.kawansoft.aceql.gui.AceQLManager.DEFAULT_PORT;
import com.kawansoft.aceql.gui.util.UserPreferencesManager;
import com.kawansoft.app.util.ClientLogger;
import java.io.File;
import java.io.FileNotFoundException;
import org.kawanfw.sql.api.server.web.WebServerApi;

/**
 *
 * @author Nicolas de Pomereu
 */
public class AceQLTask extends Thread implements Runnable {

    public static final int STANDARD_MODE = 1;
    public static final int SERVICE_MODE = 2;
        
    private final int mode;   
    private String propertiesFile = null;
    private String host = null;
    private int port = -1;

   /**
    * Constructor
    * @param mode STANDARD_MODE or SERVICE_MODE
     * @param propertiesFile
     * @param host
     * @param port
    */    
    public AceQLTask(int mode, String propertiesFile, String host, int port) {
        
        if (mode != STANDARD_MODE && mode != SERVICE_MODE ) {
            throw new IllegalArgumentException("Invalid mode: " + mode);
        }
        
        if (propertiesFile == null) {
            throw new NullPointerException("propertiesFile is null!");
        }
         
        if (! new File(propertiesFile).exists()) {
              throw new IllegalArgumentException("propertiesFile does not exist: " + propertiesFile);
        }
        
        if (host == null) {
            throw new NullPointerException("host is null!");
        }
        
        // Replace the ! by #, if there were any, because deamon uses # as args separator
        propertiesFile = propertiesFile.replace("!", "#");
        
        this.mode = mode;
        this.propertiesFile = propertiesFile;
        this.host = host;
        this.port = port;
    }
        
     /**
     * Run the AceQL Server in stnadard mode or service mode
     */
    @Override
    public synchronized void run() {

        try {
            setStandardStartingStatus();
                                   
            // Start Server
	    WebServerApi webServerApi = new WebServerApi();
            setStandardStartedStatus();
            webServerApi.startServer(host, port, new File(propertiesFile));
        }
        catch (Exception e) {
            setStandardStoppedStatus();
            e.printStackTrace(System.err);
        }

    }
 
    private void setStandardStartingStatus() {
        if (mode == STANDARD_MODE) {
            AceQLManager.STANDARD_STATUS = AceQLManager.STANDARD_STARTING;
        }
    }
    
    private void setStandardStoppedStatus() {
        if (mode == STANDARD_MODE) {
            AceQLManager.STANDARD_STATUS = AceQLManager.STANDARD_STOPPED;
        }
    }
    
    private void setStandardStartedStatus() {
        if (mode == STANDARD_MODE) {
            AceQLManager.STANDARD_STATUS = AceQLManager.STANDARD_RUNNING;
        }
    }

        
    /**
     * Trace for Windows Service log
     *
     * @param s
     */
    public void serviceTrace(String s) {
        if (mode == SERVICE_MODE) {
            System.out.println(ClientLogger.formatLogMsg(AceQLTask.class, s));
        }

    }



    
}
