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

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Formated dtaes as trings for API classes.
 *
 * @author Nicolas de Pomereu
 *
 */
public class ApiDateUtil {

    /**
     * Get the formated date as "yyyy-mm"
     *
     * @return the date as "yyyy-mm"
     */
    public static String getMonth() {
        String sDate;

        int nMonth = new GregorianCalendar().get(Calendar.MONTH) + 1;
        sDate = "" + new GregorianCalendar().get(Calendar.YEAR);

        if (nMonth < 10) {
            sDate += "-0" + nMonth;
        } else {
            sDate += "-" + nMonth;
        }

        return sDate;
    }

    /**
     * Get the formated date as "yyyy-mm-dd"
     *
     * @return the date as "yyyy-mm-dd"
     */
    public static String getDateReverse() {
        String sDate;

        int nDay = new GregorianCalendar().get(Calendar.DAY_OF_MONTH);
        int nMonth = new GregorianCalendar().get(Calendar.MONTH) + 1;

        sDate = "" + new GregorianCalendar().get(Calendar.YEAR);

        if (nMonth < 10) {
            sDate += "-0" + nMonth;
        } else {
            sDate += "-" + nMonth;
        }

        if (nDay < 10) {
            sDate += "-0" + nDay;
        } else {
            sDate += "-" + nDay;
        }

        return sDate;
    }
}
