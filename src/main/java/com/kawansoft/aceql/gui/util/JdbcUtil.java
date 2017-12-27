package com.kawansoft.aceql.gui.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JdbcUtil {

    /**
     * Says if the passed ZIP entry is JDBC Driver.
     * <br>
     * A JDBC Driver should contain a "META-INF/services/java.sql.Driver" entry
     *
     * @param filePath
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static boolean isJdbcDriver(String filePath)
            throws FileNotFoundException, IOException {
        
        if (filePath == null) {
            throw new NullPointerException("filePath is null!");
        }
        
        File file  = new File(filePath);
        
        if (! file.exists()) {
            return false;
        }
        
        try (ZipInputStream zipIs = new ZipInputStream(
                        new BufferedInputStream(new FileInputStream(filePath)))) {

            ZipEntry zEntry = null;
            while ((zEntry = zipIs.getNextEntry()) != null) {
                String entryName = zEntry.getName();
                if (entryName.endsWith("/java.sql.Driver")) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String a[]) throws IOException {

        boolean isJdbcDriver = JdbcUtil.isJdbcDriver(
                "I:\\_dev_awake\\aceql-http-main\\aceql-http\\lib\\sqljdbc41.jar");
        System.out.println("isJdbcDriver: " + isJdbcDriver);

    }

}
