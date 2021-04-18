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
package com.kawansoft.app.util;

import java.io.File;
import java.io.IOException;
import org.apache.commons.lang3.exception.ExceptionUtils;



/**
 * Class to log in append mode events into a log All events are also printed on
 * console. <br>
 * log methods are synchronized
 */
public class ClientLogger {

    private static String CR_LF = System.getProperty("line.separator");
    
    /**
     * The debug flag
     */
    protected boolean CM_DEBUG = false;
    /**
     * The log FileDownloadTest in append mode
     */
    protected Append m_apLogFile = null;

    private Class<?> clazz = null;
    
    /**
     * Constructor
     * @param clazz TODO
     * @param sLogFile Log FileDownloadTest name
     */
    public ClientLogger(File logFile, Class<?> clazz) throws IOException {
        m_apLogFile = new Append(logFile);
        this.clazz = clazz;
    }

    /**
     * <br>
     * 1) log Message getServletContext() <br>
     * 2) Print the Message on system console (DBMON)
     *
     * @param sMessage Message to log
     */
    public synchronized void log(String sMessage) throws IOException {
        // 1) Format the message
        String sMessageFull = formatLogMsg(clazz, sMessage);

        // 2) Log it
        logWriteln(sMessageFull);
    }

    /**
     * <br>
     * 1) log Message and Exception using getServletContext() <br>
     * 2) Print the Message and the Exception Stack Trace on system console
     * (DBMON)
     *
     * @param sMessage Message to log
     * @param e Exception caught in Servlet
     */
    public void log(String sMessage, Exception e) throws Exception {
        log(sMessage);
        log("Exception: " + e.toString());
        log("Stack    : " + ExceptionUtils.getStackTrace(e));
    }

    /**
     * Write the log message into a FileDownloadTest in append mode
     *
     * @param sMsg the debug message to display
     */
    private void logWriteln(String sMsg) throws IOException {
        this.m_apLogFile.append(sMsg);
        this.m_apLogFile.append(CR_LF);
        System.out.println(sMsg);
    }
    
    /**
     * Format a log message as 2015-07-19 18:21:34 UploaderService - Starting UploaderTask.
     * @param clazz	the calling class instance
     * @param s		the mesage 
     * @return
     */
    public static String formatLogMsg(Class<?> clazz, String s) {
        
        String logMessage = null;
        
        if (clazz == null) {
           logMessage = DateTimeUtil.getNowFormatedYearFirst() + " " + s;
        }
        else {
           logMessage = DateTimeUtil.getNowFormatedYearFirst() + " " + clazz.getSimpleName() + " " + s;
        }

	return logMessage;
    }

}

// End
