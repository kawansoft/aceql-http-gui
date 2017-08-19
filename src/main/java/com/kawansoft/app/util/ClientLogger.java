// ClientLogger.java	: ConfiMail - system logger
// Copyright (c) SafeLogic, 2000 - 2001
//
// Last Updates: 
// 25/03/05 15:50 NDP - creation
// 25/05/05 11:00 NDP - log methods are now synchronized
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