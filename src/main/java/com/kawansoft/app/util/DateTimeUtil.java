package com.kawansoft.app.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {

    public DateTimeUtil() {

    }

    /**
     * Format a clean date/hour for a file
     *
     * @param file
     *            the file to format
     * @return the clean formated date/hour
     */
    public static String formatFileDate(File file) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm");
        Date date = new Date(file.lastModified());
        String output = formatter.format(date);
        return output;
    }
    
    /**
     * Returns now in 19-07-2015 17:46:54 format 
     * @return  now in 19-07-2015 17:46:5 format 
     */
    public static String getNowFormatedDayFirst()  {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date now = new Date();
        return formatter.format(now);
    }
    

}
