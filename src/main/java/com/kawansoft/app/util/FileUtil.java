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

import com.kawansoft.app.parms.LanguageManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;

import java.text.DecimalFormat;
import java.util.Locale;

/**
 * Misc file utilities
 *
 * @author Nicolas de Pomereu
 *
 */
public class FileUtil {

    /**
     * The DEBUG flag
     */
    private boolean DEBUG = false;

    public static String CR_LF = System.getProperty("line.separator");

    /**
     * List of files
     */
    private List<File> m_fileList = new Vector<File>();

    /**
     * Constructor
     */
    public FileUtil() {
        super();
    }

    /**
     * Return true if the fils exists, is readable and is not locked by another
     * process
     * <br>
     * New version that works because it uses RandomAccessFile and will throw an
     * exception if another file has locked the file
     */
    public boolean isUnlockedForRead(File file) {
        if (file.exists() && file.canRead()) {
            //if (file.isDirectory())
            //{
            //    return true;
            //}

            try {
                RandomAccessFile raf
                        = new RandomAccessFile(file, "r");

                raf.close();

                debug(new Date() + " " + file + " OK!");

                return true;
            } catch (Exception e) {
                debug(new Date() + " " + file + " LOCKED! " + e.getMessage());
            }
        } else {
            debug(new Date() + " " + file + " LOCKED! File exists(): "
                    + file.exists() + " File canWrite: " + file.canWrite());
        }

        return false;

    }

    /**
     * Return true if the fils exists and is readable and writable not locked by
     * another process
     * <br>
     * New version that works because it uses RandomAccessFile and will throw an
     * exception if another file has locked the file
     */
    public boolean isUnlockedForWrite(File file) {
        if (file.exists() && file.canWrite()) {

            //if (file.isDirectory())
            //{
            //    return true;
            //}
            try {
                RandomAccessFile raf
                        = new RandomAccessFile(file, "rw");

                //raf.write("ok".getBytes());
                raf.close();

                debug(new Date() + " " + file + " OK!");

                return true;
            } catch (Exception e) {
                debug(new Date() + " " + file + " LOCKED! " + e.getMessage());
            }
        } else {
            debug(new Date() + " " + file + " LOCKED! File exists(): "
                    + file.exists() + " File canWrite: " + file.canWrite());
        }

        return false;

    }

    /**
     * Recuse research of a files. Result is stored in m_fileList
     *
     * @param fileList the initial list
     * @param recurse if true, search inside directories
     */
    private void searchFiles(File[] fileList, boolean recurse) {
        if (fileList == null) {
            throw new IllegalArgumentException("File list can't be null!");
        }

        for (int i = 0; i < fileList.length; i++) {
            m_fileList.add(fileList[i]);

            if (fileList[i].isDirectory() && recurse) {
                searchFiles(fileList[i].listFiles(), recurse);
            }
        }
    }

    /**
     * Extract all files from a list of files, with recurse options
     *
     * @param fileList the initial list
     * @param recurse if true, search inside directories
     *
     * @return the extracted all files
     */
    public List<File> listAllFiles(List<File> fileList, boolean recurse) {

        if (fileList == null) {
            throw new IllegalArgumentException("File list can't be null!");
        }

        if (fileList.isEmpty()) {
            return new Vector<File>();
        }

        File[] files = new File[fileList.size()];

        for (int i = 0; i < fileList.size(); i++) {
            files[i] = fileList.get(i);
        }

        searchFiles(files, recurse);
        return m_fileList;
    }

    /**
     *
     * Extract the files that are not directories from a file list
     *
     * @param fileList the initial list (firt level only)
     * @return the file list
     */
    public List<File> extractFilesOnly(List<File> fileList) {
        if (fileList == null) {
            throw new IllegalArgumentException("File list can't be null!");
        }

        if (fileList.isEmpty()) {
            return new Vector<File>();
        }

        List<File> files = new Vector<File>();

        for (int i = 0; i < fileList.size(); i++) {
            if (!fileList.get(i).isDirectory()) {
                files.add(fileList.get(i));
            }
        }

        return files;

    }

    /**
     *
     * Extract the directories only from a file list
     *
     * @param fileList the initial list (firt level only)
     * @return the file list
     */
    public List<File> extractDirectoriesOnly(List<File> fileList) {
        List<File> directories = new Vector<File>();

        for (int i = 0; i < fileList.size(); i++) {
            if (fileList.get(i).isDirectory()) {
                directories.add(fileList.get(i));
            }
        }

        return directories;

    }

    /**
     * Put the content of a file as HTML into a String
     * <br>
     * No carriage returns will be included in output String
     *
     * @param fileIn The HTML text file
     * @return The content in text
     * @throws IOException
     */
    public String getHtmlContent(File fileIn)
            throws IOException {
        if (fileIn == null) {
            throw new IllegalArgumentException("File name can't be null!");
        }

        StringBuilder text = new StringBuilder();

        try (BufferedReader br = new BufferedReader(new FileReader(fileIn));) {

            String line = null;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append(CR_LF);
            }
        } finally {
            //IOUtils.closeQuietly(br);
        }

        return text.toString();
    }

    /**
     * Return true if the filename is a Window possible Filename
     *
     * @param filename the filename to test
     * @return true if the filename is a Window Filename
     */
    public static boolean isPossibleWindowFilename(String filename) {
        if (filename.indexOf("\\") != -1
                || filename.indexOf("/") != -1
                || filename.indexOf(":") != -1
                || filename.indexOf("*") != -1
                || filename.indexOf("?") != -1
                || filename.indexOf("\"") != -1
                || filename.indexOf("\"") != -1
                || filename.indexOf("<") != -1
                || filename.indexOf(">") != -1
                || filename.indexOf("|") != -1) {
            return false;
        } else {
            return true;
        }
    }

    private void debug(String sMsg) {
        if (DEBUG) {
            System.out.println("DBG> " + sMsg);
        }
    }

    /**
     * Force the creation of a directory and display error message before
     * leavinf application if necessary
     *
     * @param directory the directory to create
     * @return true
     */
    public static boolean createDirectory(String directory) {
        File fileDir = new File(directory);

        // Ok, best case
        if (fileDir.isDirectory()) {
            return true;
        }

        // if the file with same name as directory exists, delete it before creating the directory
        if (fileDir.exists()) {
            try {
                FileUtils.forceDelete(fileDir);
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Impossible de supprimer le fichier : " + fileDir + " " + e.toString());
                return false;

            }
        }

        //
        // Create all necessaries up directories
        //
        new File(directory).mkdirs();

        if (new File(directory).isDirectory()) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Impossible de créer le répertoire : " + fileDir);
            return false;
        }

    }

    /**
     * Compute the files length in clean display in Ko size
     *
     * @param files
     * @return a clean display of the files size in Ko, with blank separator for
     * thousands
     */
    public static String getCleanFilesLengthInKo(List<File> files) {
        long fileLength = 0;

        for (File file : files) {
            fileLength += file.length();
        }

        String fileLengthStr = getCleanFileLengthInKo(fileLength);

        return fileLengthStr;
    }

    /**
     * Compute the file length in clean display in Ko size
     *
     * @param file the file to get the size of
     * @return a clean display of the file size in Ko, with blank separator for
     * thousands
     */
    public static String getCleanFileLengthInKo(File file) {
        long fileLength = file.length();
        String fileLengthStr = getCleanFileLengthInKo(fileLength);

        return fileLengthStr;
    }

    /**
     * Compute the file length in clean display in Ko size
     *
     * @param fileLength the length of the file
     * @return a clean display of the file size in Ko, with blank separator for
     * thousands
     */
    public static String getCleanFileLengthInKo(long fileLength) {
        long fileLengthKo = fileLength / 1024;

        DecimalFormat myFormatter = new DecimalFormat("###,###");
        String fileLengthStr = myFormatter.format(fileLengthKo);
        return fileLengthStr + " Ko";
    }

    /**
     * Display a clean format for a file last modified
     *
     * @param file File to clean last modified info
     * @return a clean display of the file size in Ko, with blank separator for
     * thousands
     */
    public static String getCleanLastModifed(File file) {
        DateFormat dateFormat = getLocaleDateFormat();
        String cleanDateFormat = dateFormat.format(new Date(file.lastModified()));
        return cleanDateFormat;
    }

    /**
     * Display a clean format for a timestamp
     *
     * @param ts timestamp to clean
     * @return a clean display of the file size in Ko, with blank separator for
     * thousands
     */
    public static String getCleanLastModifed(Timestamp ts) {
        DateFormat dateFormat = getLocaleDateFormat();
        String cleanDateFormat = dateFormat.format(ts);
        return cleanDateFormat;
    }

    private static DateFormat getLocaleDateFormat() {

        DateFormat dateFormat = null;
        if (LanguageManager.getLanguage().equals(Locale.FRENCH.getLanguage())) {
            dateFormat = new SimpleDateFormat("dd/MM/yy HH':'mm");
        } else {
            dateFormat = new SimpleDateFormat("MM/dd/yy HH':'mm");
        }
        return dateFormat;
    }
}
