/*
 * This file is part of AceQL HTTP.
 * AceQL HTTP: SQL Over HTTP                                     
 * Copyright (C) 2017,  KawanSoft SAS
 * (http://www.kawansoft.com). All rights reserved.                                
 *                                                                               
 * AceQL HTTP is free software; you can redistribute it and/or                 
 * modify it under the terms of the GNU Lesser General Public                    
 * License as published by the Free Software Foundation; either                  
 * version 2.1 of the License, or (at your option) any later version.            
 *                                                                               
 * AceQL HTTP is distributed in the hope that it will be useful,               
 * but WITHOUT ANY WARRANTY; without even the implied warranty of                
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU             
 * Lesser General Public License for more details.                               
 *                                                                               
 * You should have received a copy of the GNU Lesser General Public              
 * License along with this library; if not, write to the Free Software           
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  
 * 02110-1301  USA
 * 
 * Any modifications to this file must keep this entire header
 * intact.
 */
package com.kawansoft.aceql.gui.task;

import com.kawansoft.aceql.gui.AceQLManager;
import static com.kawansoft.aceql.gui.AceQLManager.CR_LF;
import com.kawansoft.app.util.ClientLogger;
import com.kawansoft.app.util.classpath.ClasspathUtil;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.SystemUtils;
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
            Objects.requireNonNull(propertiesFile, "propertiesFile cannot be null!");
        }

        if (!new File(propertiesFile).exists()) {
            throw new IllegalArgumentException("propertiesFile does not exist: " + propertiesFile);
        }

        this.mode = mode;
        this.propertiesFile = Objects.requireNonNull(propertiesFile, "propertiesFile cannot be null!");
        this.host = Objects.requireNonNull(host, "host cannot be null!");
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

                List<String> classpathList = ClasspathUtil.getClasspath();
                Collections.sort(classpathList);

                String classpathCrLf = "";
                for (String classpathElement : classpathList) {
                    classpathCrLf += classpathElement + System.getProperty("path.separator") + CR_LF;
                }

                System.out.println(ClientLogger.formatLogMsg(null, "CLASSPATH        : " + CR_LF + classpathCrLf));
            }
            // Start Server
            WebServerApi webServerApi = new WebServerApi();
            setStandardStartedStatus();
            webServerApi.startServer(host, port, new File(propertiesFile));

        } catch (IllegalArgumentException e) {
            System.err.println(SqlTag.SQL_PRODUCT_START_FAILURE + " "
                    + SqlTag.USER_CONFIGURATION_FAILURE + " "
                    + e.getMessage());

            if (e.getCause() == null) {
                e.printStackTrace();
            } else {
                e.getCause().printStackTrace();
            }

            System.err.println();
        } catch (ConnectException e) {
            System.err.println(SqlTag.SQL_PRODUCT_START_FAILURE + " "
                    + e.getMessage());
            e.printStackTrace();
            System.err.println();
        } catch (UnknownHostException e) {
            System.err.println(SqlTag.SQL_PRODUCT_START_FAILURE + " "
                    + "Unknow host: " + e.getMessage());
            if (e.getCause() == null) {
                e.printStackTrace();
            } else {
                e.getCause().printStackTrace();
            }

            System.err.println();
        } catch (IOException e) {
            System.err.println(SqlTag.SQL_PRODUCT_START_FAILURE + " "
                    + e.getMessage());

            if (e.getCause() == null) {
                e.printStackTrace();
            } else {
                e.getCause().printStackTrace();
            }

            System.err.println();
        } catch (Exception e) {
            System.err.println(SqlTag.SQL_PRODUCT_START_FAILURE);
            e.printStackTrace();
            System.err.println();
        } finally {
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
