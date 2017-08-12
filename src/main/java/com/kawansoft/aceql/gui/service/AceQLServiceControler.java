
package com.kawansoft.aceql.gui.service;

import com.kawansoft.aceql.gui.task.AceQLTask;
import com.kawansoft.app.parms.Parms;
import com.kawansoft.app.util.ClientLogger;
import javax.swing.JOptionPane;


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
        System.out.println(ClientLogger.formatLogMsg(AceQLServiceControler.class, "CLASSPATH: " + System.getProperty("java.class.path")));
        System.out.println("");
        
        if (arg.length != 3) {
            throw new IllegalArgumentException("Invalid Start Params number. Should be 3 is: " + arg.length);
        }
        
        String propertiesFile = arg[0];
        String host = arg[1];
        String portStr = arg[2];
        
        int port = -1;
        
        try {
            port = Integer.parseInt(portStr);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid port not numeric: " + portStr);
        }
        // Ok, launch task
        AceQLTask aceQLTask = new AceQLTask(AceQLTask.STANDARD_MODE, propertiesFile, host, port);
        aceQLTask.start();
    }

    public static void stop(String arg[]) {
        
	System.out.println(ClientLogger.formatLogMsg(AceQLServiceControler.class, "Stopping " + AceQLTask.class.getSimpleName() + "..." ));        
        System.exit(1);
    }

}
