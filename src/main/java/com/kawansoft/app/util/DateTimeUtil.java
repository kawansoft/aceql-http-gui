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
    
    /**
     * Returns now in 2015-19-07 17:46:54 format 
     * @return  now in 2015-19-07 17:46:54 format
     */
    public static String getNowFormatedYearFirst()  {
        SimpleDateFormat formatter;
        formatter = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        Date now = new Date();
        return formatter.format(now);
    }
    

}
