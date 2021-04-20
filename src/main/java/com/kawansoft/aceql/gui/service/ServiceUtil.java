/*
 * This file is part of AceQL HTTP.
 * AceQL HTTP: SQL Over HTTP                                     
 * Copyright (C) 2021,  KawanSoft SAS
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
package com.kawansoft.aceql.gui.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
    public static final int AUTO_START_DELAYED = 22;
    public static final int DEMAND_START = 3;
    public static final int DISABLED = 4;


    /**
     * Protected
     */
    protected ServiceUtil() {

    }

    /**
     * Says if AceQLHTTPService is installed
     *
     * @return true if AceQLHTTPService is installed
     * @throws IOException
     */
    public static boolean isInstalled() throws IOException {
        int status = ServiceUtil.getServiceStatus(ACEQL_HTTP_SERVICE);
        return status != ServiceUtil.NOT_INSTALLED;
    }

    /**
     * Says if AceQLHTTPService is running
     *
     * @return true if AceQLHTTPService is running
     * @throws IOException
     */
    public static boolean isRunning() throws IOException {
        int status = ServiceUtil.getServiceStatus(ACEQL_HTTP_SERVICE);
        return status == ServiceUtil.RUNNING;
    }

    /**
     * Says if AceQLHTTPService is stopped
     *
     * @return true if AceQLHTTPService is stopped
     * @throws IOException
     */
    public static boolean isStopped() throws IOException {
        int status = ServiceUtil.getServiceStatus(ACEQL_HTTP_SERVICE);
        return status == ServiceUtil.STOPPED;
    }

    /**
     * Says if AceQLHTTPService is running
     *
     * @return true if AceQLHTTPService is running
     * @throws IOException
     */
    public static boolean isStarting() throws IOException {
        int status = ServiceUtil.getServiceStatus(ACEQL_HTTP_SERVICE);
        return status == ServiceUtil.STARTING;
    }

    /**
     * Start the service manager AceQLHTTPServicew.exe via a bat wrapper because
     * of Windows Authorization. The service manager AceQLHTTPServicew.exwith
     * same interface a Tomcat
     *
     * @param directory
     * @parm directory	the directory into which serviceProperties.bat is
     * installed
     * @throws IOException
     */
    public static void startServiceProperties(String directory) throws IOException {

        if (!SystemUtils.IS_OS_WINDOWS) {
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
     * Starts the SimplifyUploaderServic service via a bat wrapper because of
     * Windows Authorization.
     *
     * @parm directory	the directory into serviceProperties.bat ins install
     * @throws IOException
     */
    public static void startService() throws IOException {

        if (!SystemUtils.IS_OS_WINDOWS) {
            return;
        }

        //JOptionPane.showMessageDialog(null, "directory: " + directory);
        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C",
                "startService.bat");

        pb.directory(new File(ServiceInstaller.SERVICE_DIRECTORY));
        Process p = pb.start();

        try {
            p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops the SimplifyUploaderServic service via a bat wrapper because of
     * Windows Authorization.
     *
     * @parm directory	the directory into serviceProperties.bat ins install
     * @throws IOException
     */
    public static void stopService() throws IOException {

        if (!SystemUtils.IS_OS_WINDOWS) {
            return;
        }

        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C",
                "stopService.bat");

        pb.directory(new File(ServiceInstaller.SERVICE_DIRECTORY));
        Process p = pb.start();

        try {
            p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


     /**
     * Returns the start mode status of the passed service
     *
     * @param serviceName	the sercie name
     * @return	the start mode of the service
     * @throws IOException
     */
    public static boolean isLocalSystem(String serviceName) throws IOException {

        if (!SystemUtils.IS_OS_WINDOWS) {
            return false;
        }

        Process p = Runtime.getRuntime().exec("sc qc " + serviceName);
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader( p.getInputStream()));) {

            String line = reader.readLine();
            while (line != null) {

                if (line.trim().startsWith("SERVICE_START_NAME")) {
                    return line.contains("LocalSystem");
                }
       
                line = reader.readLine();
            }

            return false;

        } 
    }
        
    /**
     * Returns the status of the passed service
     *
     * @param serviceName	the sercie name
     * @return	the status of the service
     * @throws IOException
     */
    public static int getServiceStatus(String serviceName) throws IOException {

        if (!SystemUtils.IS_OS_WINDOWS) {
            return NOT_INSTALLED;
        }

        Process p = Runtime.getRuntime().exec("sc query " + serviceName);
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));){
            
            String line = reader.readLine();
            while (line != null) {

                if (line.trim().startsWith("STATE")) {

                    if (line.trim()
                            .substring(line.trim().indexOf(":") + 1,
                                    line.trim().indexOf(":") + 4).trim()
                            .equals("1")) //System.out.println("Stopped");
                    {
                        return STOPPED;
                    } else if (line
                            .trim()
                            .substring(line.trim().indexOf(":") + 1,
                                    line.trim().indexOf(":") + 4).trim()
                            .equals("2")) //System.out.println("Starting....");
                    {
                        return STARTING;
                    } else if (line
                            .trim()
                            .substring(line.trim().indexOf(":") + 1,
                                    line.trim().indexOf(":") + 4).trim()
                            .equals("3")) {
                        return STOPPING;
                    } else if (line
                            .trim()
                            .substring(line.trim().indexOf(":") + 1,
                                    line.trim().indexOf(":") + 4).trim()
                            .equals("4")) //System.out.println("Running");
                    {
                        return RUNNING;
                    }

                }
                line = reader.readLine();
            }

            return NOT_INSTALLED;

        } 
    }

   
    /**
     * Returns the start mode status of the passed service
     *
     * @param serviceName	the sercie name
     * @return	the start mode of the service
     * @throws IOException
     */
    public static int getServiceStartMode(String serviceName) throws IOException {

        if (!SystemUtils.IS_OS_WINDOWS) {
            return NOT_INSTALLED;
        }

        Process p = Runtime.getRuntime().exec("sc qc " + serviceName);
       
        try ( BufferedReader  reader = new BufferedReader(new InputStreamReader(p.getInputStream()));){

            String line = reader.readLine();
            while (line != null) {

                if (line.trim().startsWith("START_TYPE")) {

                    if (line.trim()
                            .substring(line.trim().indexOf(":") + 1,
                                    line.trim().indexOf(":") + 4).trim()
                            .equals("2")) {

                        if (line.contains("DELAYED")) {
                            return AUTO_START_DELAYED;
                        } else {
                            return AUTO_START;
                        }
                    } else if (line
                            .trim()
                            .substring(line.trim().indexOf(":") + 1,
                                    line.trim().indexOf(":") + 4).trim()
                            .equals("3")) {
                        return DEMAND_START;
                    } else if (line
                            .trim()
                            .substring(line.trim().indexOf(":") + 1,
                                    line.trim().indexOf(":") + 4).trim()
                            .equals("4")) {
                        return DISABLED;
                    }

                }
                line = reader.readLine();
            }

            return NOT_INSTALLED;
        } 
    }

    /**
     * Returns the start mode status of the passed service
     *
     * @param serviceName	the service name
     * @return	the start mode of the service
     * @throws IOException
     */
    public static String getServiceStartupTypeLabel(String serviceName) throws IOException {
        int startMode = getServiceStartMode(serviceName);

        if (startMode == NOT_INSTALLED) {
            return "";
        } else if (startMode == AUTO_START) {
            return "Auto";
        } else if (startMode == AUTO_START_DELAYED) {
            return "Auto (Delayed)";
        } else if (startMode == DEMAND_START) {
            return "Manual";
        } else if (startMode == DISABLED) {
            return "Disabled";
        } else {
            return "Unknown";
        }

    }
}
