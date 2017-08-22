package com.kawansoft.aceql.gui.task;

import com.kawansoft.aceql.gui.AceQLManager;
import static com.kawansoft.aceql.gui.AceQLManager.CR_LF;
import com.kawansoft.app.util.ClientLogger;
import com.kawansoft.app.util.classpath.ClasspatUtil;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.List;
import org.apache.commons.lang3.SystemUtils;
import org.kawanfw.sql.api.server.DatabaseConfigurationException;
import org.kawanfw.sql.api.server.web.WebServerApi;
import org.kawanfw.sql.util.SqlTag;

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
     *
     * @param mode STANDARD_MODE or SERVICE_MODE
     * @param propertiesFile
     * @param host
     * @param port
     */
    public AceQLTask(int mode, String propertiesFile, String host, int port) {

        if (mode != STANDARD_MODE && mode != SERVICE_MODE) {
            throw new IllegalArgumentException("Invalid mode: " + mode);
        }

        if (propertiesFile == null) {
            throw new NullPointerException("propertiesFile is null!");
        }

        if (!new File(propertiesFile).exists()) {
            throw new IllegalArgumentException("propertiesFile does not exist: " + propertiesFile);
        }

        if (host == null) {
            throw new NullPointerException("host is null!");
        }

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

            if (mode == SERVICE_MODE) {
                System.out.println(ClientLogger.formatLogMsg(null, "OS Name / Version: " + System.getProperty("os.name") + " / " + System.getProperty("os.version")));
                System.out.println(ClientLogger.formatLogMsg(null, "OS Architecture  : " + System.getProperty("os.arch")));
                System.out.println(ClientLogger.formatLogMsg(null, "Java Vendor      : " + System.getProperty("java.vendor")));
                System.out.println(ClientLogger.formatLogMsg(null, "Java Home        : " + System.getProperty("java.home")));
                System.out.println(ClientLogger.formatLogMsg(null, "Java Version     : " + System.getProperty("java.runtime.version")));
                System.out.println(ClientLogger.formatLogMsg(null, "user.name        : " + System.getProperty("user.name")));
                System.out.println(ClientLogger.formatLogMsg(null, "user.home        : " + SystemUtils.getUserHome()));
                System.out.println(ClientLogger.formatLogMsg(null, "user.dir         : " + SystemUtils.getUserDir()));
            }

            List<String> classpathList = ClasspatUtil.getOrderedClasspath();

            String classpathCrLf = "";
            for (String classpathElement : classpathList) {
                classpathCrLf += classpathElement + System.getProperty("path.separator") + CR_LF;
            }

            System.out.println(ClientLogger.formatLogMsg(null, "CLASSPATH        : " + CR_LF + classpathCrLf));

            // Start Server
            WebServerApi webServerApi = new WebServerApi();
            setStandardStartedStatus();
            webServerApi.startServer(host, port, new File(propertiesFile));

        } catch (DatabaseConfigurationException e) {
            System.err.println(SqlTag.SQL_PRODUCT_START_FAILURE + " "
                    + SqlTag.USER_CONFIGURATION_FAILURE + " "
                    + e.getMessage());
            System.err.println();
        } catch (ConnectException e) {
            System.err.println(SqlTag.SQL_PRODUCT_START_FAILURE + " "
                    + e.getMessage());
            System.err.println();
        } catch (IOException e) {

            if (e instanceof UnknownHostException) {
                System.err.println(SqlTag.SQL_PRODUCT_START_FAILURE + " "
                        + "Unknow host: " + e.getMessage());
            } else {
                System.err.println(SqlTag.SQL_PRODUCT_START_FAILURE + " "
                        + e.getMessage());
            }

            if (e.getCause() != null) {
                e.printStackTrace();
            }

            System.err.println();
        } catch (Exception e) {
            System.err.println(SqlTag.SQL_PRODUCT_START_FAILURE);
            e.printStackTrace();
            System.err.println();
        }
        finally {
             setStandardStoppedStatus();
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
