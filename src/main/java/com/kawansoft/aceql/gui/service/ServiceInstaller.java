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
 * Utility class to installService or uninstallService the AceQLHTTPService
 * service
 *
 * @author Nicolas de Pomereu
 *
 */
public class ServiceInstaller {

    public static boolean DEBUG = false;

    public static final String CR_LF = System.getProperty("line.separator");

    public static final String SERVICE_DIRECTORY = ParmsUtil.getInstallAceQLDir()  + File.separator + "service";
    private static String USER_DIR_WITH_QUOTES = "\"" + SystemUtils.USER_DIR + "\"";
    private static String USER_HOME_SYSTEM = ParmsUtil.getBaseDir();


    /**
     * Protected
     */
    protected ServiceInstaller() {

    }

    
    /**
     * Installs the AceQLHTTPService.exe service in specified directory. Call is
     * done via via a bat wrapper because of Windows Authorization.
     *
     * @throws IOException
     */
    public static void installService() throws IOException {

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
                "installService.bat", USER_DIR_WITH_QUOTES, USER_HOME_SYSTEM);
        pb.directory(new File(SERVICE_DIRECTORY));
        Process p = pb.start();

        //printProcessDisplay(p);
        try {
            p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void updateServiceClasspath() throws IOException {

        if (!SystemUtils.IS_OS_WINDOWS) {
            return;
        }

        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C",
                "updateServiceClasspath.bat", USER_DIR_WITH_QUOTES);
        pb.directory(new File(SERVICE_DIRECTORY));
        Process p = pb.start();

        //printProcessDisplay(p);
        try {
            p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static void updateServiceUserHomeSystem() throws IOException {
        if (!SystemUtils.IS_OS_WINDOWS) {
            return;
        }

        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C",
                "updateServiceUserHomeSystem.bat", USER_DIR_WITH_QUOTES, USER_HOME_SYSTEM);
        pb.directory(new File(SERVICE_DIRECTORY));
        Process p = pb.start();

        //printProcessDisplay(p);
        try {
            p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public static void updateServiceUserHomeNormal() throws IOException {
        if (!SystemUtils.IS_OS_WINDOWS) {
            return;
        }

        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C",
                "updateServiceUserHomeNormal.bat", USER_DIR_WITH_QUOTES);
        pb.directory(new File(SERVICE_DIRECTORY));
        Process p = pb.start();

        //printProcessDisplay(p);
        try {
            p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * Installs the AceQLHTTPService.exe service in specified directory. Call is
     * done via via a bat wrapper because of Windows Authorization.
     *
     * @throws IOException
     */
    public static void uninstallService() throws IOException {

        if (!SystemUtils.IS_OS_WINDOWS) {
            return;
        }

        ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C",
                "uninstallService.bat");
        pb.directory(new File(SERVICE_DIRECTORY));
        Process p = pb.start();

        try {
            p.waitFor();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prints on System.out the result of the process
     *
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


}
