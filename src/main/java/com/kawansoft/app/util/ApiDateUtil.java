//ApiDateUtil.java
//Copyright (c) SafeLogic, 2000 - 2005
//
//Last Updates: 
// 25 juin 2006 11:51:32 Nicolas de Pomereu
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
        String sDate = new String();

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
        String sDate = new String();

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
