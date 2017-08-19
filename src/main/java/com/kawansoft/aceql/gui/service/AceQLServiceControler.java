
package com.kawansoft.aceql.gui.service;

import com.kawansoft.aceql.gui.task.AceQLTask;
import com.kawansoft.aceql.gui.util.ConfigurationUtil;
import com.kawansoft.app.util.ClientLogger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author Nicolas de Pomereu
 *
 */
public class AceQLServiceControler {

    public static final String CR_LF = System.getProperty("line.separator");

    /**
     * Constructor
     */
    public AceQLServiceControler() {

    }
    
    public static void start(String arg[]) {

        System.out.println(ClientLogger.formatLogMsg(AceQLServiceControler.class, "Starting " + AceQLTask.class.getSimpleName() + "..."));
        
        String aceqlProperties = null;
        String host = null;
        int port = 0;

        File configurationProperties = ConfigurationUtil.getConfirurationProperties();
        
        if (! configurationProperties.exists()) {
            throw new IllegalArgumentException(new FileNotFoundException("The configuration file that stores properties file name, host and port does not exist: " + configurationProperties));
        }
        
        ConfigurationUtil configurationUtil = new ConfigurationUtil();
        try {
            configurationUtil.load();
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
        
       aceqlProperties = configurationUtil.getAceqlProperties();
       host = configurationUtil.getHost();
       port = configurationUtil.getPort();
      
        // Ok, launch task
        AceQLTask aceQLTask = new AceQLTask(AceQLTask.SERVICE_MODE, aceqlProperties, host, port);
        aceQLTask.start();
    }

    public static void stop(String arg[]) {
        
	System.out.println(ClientLogger.formatLogMsg(AceQLServiceControler.class, "Stopping " + AceQLTask.class.getSimpleName() + "..." ));        
        System.exit(1);
    }

}
