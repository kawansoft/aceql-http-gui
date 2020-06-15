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
package com.kawansoft.app.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.SystemUtils;

/**
 * @author Nicolas de Pomereu
 *
 */
public class ProcessUtil {

    
    public static int countWindowsInstanceRunning(String programName) throws IOException {
        if (!SystemUtils.IS_OS_WINDOWS) {
            return 0;
        }

        if (programName == null) {
            throw new NullPointerException("programName can not be null!");
        }

        String line;
        List<String> pidInfoSet = new ArrayList<>();

        Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");

        try (BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));) {

            while ((line = input.readLine()) != null) {

                if (line.toLowerCase().contains(programName.toLowerCase())) {
                    pidInfoSet.add(line);
                }
            }
        }

        return pidInfoSet.size();

    }

    /**
     *
     */
    protected ProcessUtil() {
    }

    /**
     * Says if the program name is running. Check is done independent of caps.
     *
     * @param programName	the program name to check
     * @return	true if the program name String is contained in task lists
     * @throws IOException
     */
    public static boolean isWindowsProgramRunning(String programName) throws IOException {

        if (!SystemUtils.IS_OS_WINDOWS) {
            return false;
        }

        if (programName == null) {
            throw new NullPointerException("programName can not be null!");
        }

        String line;
        String pidInfo = "";

        Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");

        try (BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));) {

            while ((line = input.readLine()) != null) {
                pidInfo += line;
            }
        }

        if (pidInfo.toLowerCase().contains(programName.toLowerCase())) {
            return true;
        } else {
            return false;
        }
    }

}
