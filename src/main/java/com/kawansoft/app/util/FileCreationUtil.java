//FileCreationUtil.java
//Copyright (c) SafeLogic, 2000 - 2005
//
//Last Updates: 
// 12 nov. 2008 15:32:12 Nicolas de Pomereu
// 28/11/08 09:41 ABE: add public static File createIndicedFile(String filename, String dirStr) 
//						and use it in createIndicedTempFile
// 12/12/08 15:05 NDP - Fix bug in FileCreationUtil.waitForCreationEnd() : wait was too short

package com.kawansoft.app.util;

import java.io.File;

/**
 * @author Nicolas de Pomereu
 *
 * Misc tools when creating a file:
 * <br> - Waiting for file creation.
 * <br> - Create an indice file is file is already created.
 * 
 */
public class FileCreationUtil
{

    /**
     * Constructor
     */
    public FileCreationUtil()
    {
        
    }

    /**
     * Create a temporary file (delete on exit) using the filename
     * <br>
     * If the filename exists ==> We will created an indiced filename
     * <br> aka courrier.pdf exists() ==> create courrier(1).pdf, etc.
     * 
     * @param filename      the filename
     * @param tempDirStr    the temp directory to use
     * @return
     */
    public static File createIndicedTempFile(String filename, String tempDirStr)
    {        
        File file = createIndicedFile(filename, tempDirStr);        
    
        // The file must be delete on exit
        file.deleteOnExit();
        return file;
    }

    /**
     * Create a file using the filename
     * <br>
     * If the filename exists ==> We will created an indiced filename
     * <br> aka courrier.pdf exists() ==> create courrier(1).pdf, etc.
     * 
     * @param filename      the filename
     * @param dirStr    the directory to use
     * @return
     */
    public static File createIndicedFile(String filename, String dirStr)
    {
        File file = new File(dirStr + File.separator + filename);
        
        String fileStr = file.toString();
        String extension = null;
        String prefix = null; 
        
        if (filename.lastIndexOf(".") != -1)
        {
            extension = filename.substring(filename.lastIndexOf("."));
            prefix = fileStr.substring(0, fileStr.lastIndexOf("."));
        }
        else
        {
            prefix = fileStr;
        }
        
        // If this file exists, add a indice
        int i = 0;
        
        while (file.exists())
        {
            i++;
            if (extension == null)
            {
                file = new File(prefix + "(" + i +")");
            }
            else
            {   
                file = new File(prefix + "(" + i + ")" + extension);
            }
        }      
        return file;
    }
    /**
     * 
     * Wait 10 seconds for the end of the creation of a PDF file after
     * a PDF print
     * @param pdfFile           the PDF file to wait the creation for
     * @throws InterruptedException
     */
    public static void waitForCreationEnd(File pdfFile) throws InterruptedException
    {
        final int sleepTime100MilliSecond = 100;
        int maxSecondsToWait = 100;
        
        int cpt = 0;
        while(! pdfFile.exists())
        {
            cpt++;
            Thread.sleep(sleepTime100MilliSecond);
            
            System.out.println("USING LOOP : " +  cpt);
            
            if (cpt > maxSecondsToWait * 10) break; // 100 seconds wait
        }
        
        System.out.println("waitForCreationEnd: FILE NOW EXISTS!");
    }


}
