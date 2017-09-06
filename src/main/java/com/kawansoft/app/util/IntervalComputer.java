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

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Compute time interval ine Timestamp for Admin Display
 * computer methods are :
 * <br> 
 * <br> - computeToday()     : today perdiod
 * <br> - compute2000ToNow() : All times from 2000 of times to now
 * <br> - computeYear(year)  : all passed year, with year = 2005, 2006, etc.
 * <br> - computeMonth(month): all passed month in this year, with month = 01, 02, ..., 12
 * <br>
 * Result are returned with the getStartDate() and getEndDate() as Timestamp
 * 
 * @author Nicolas de Pomereu
 */
public class IntervalComputer
{

    
    /** The start date in the interval */    
    private Timestamp startDate = null;
    
    /** The end date in the interval */ 
    private Timestamp endDate = null;
    
    /**
     * Constructor
     */
    public IntervalComputer()
    {
        startDate = new Timestamp(System.currentTimeMillis());
        endDate = new Timestamp(System.currentTimeMillis());
    }

    /**
     * Compute the today interval
     */
    public void computeToday()
    {
        GregorianCalendar gcStart = new GregorianCalendar();     
        gcStart.set(Calendar.HOUR_OF_DAY, 0);  
        gcStart.set(Calendar.MINUTE, 0);
        gcStart.set(Calendar.SECOND, 1); // To be sure it's today and not tomorrow
        startDate.setTime(gcStart.getTimeInMillis());                  
        
        GregorianCalendar gcEnd = new GregorianCalendar();     
        gcEnd.set(Calendar.HOUR_OF_DAY, 23);  
        gcEnd.set(Calendar.MINUTE, 59);
        gcEnd.set(Calendar.SECOND, 59);
        endDate.setTime(gcEnd.getTimeInMillis());         
    }
    
    /**
     * Compute all times from 2000 to now 
     */
    public void compute2000ToNow()
    {
        // All times from beginning of times to now
        GregorianCalendar gcStart = new GregorianCalendar();
        gcStart.set(Calendar.YEAR, 2000);
        startDate.setTime(gcStart.getTimeInMillis());
        
        // Now
        GregorianCalendar gcEnd = new GregorianCalendar();
        endDate.setTime(gcEnd.getTimeInMillis());          
    }

    /**
     * Compute interval from beginning of year to now
     * @param year  2005, 2006, etc.
     */
    public void computeYear(int year)
    {
        
        if (year < 2003 || year > 2020)
        {
            throw new IllegalArgumentException("Year is in YYYY format. Invalid year: " + year);
        }
        
        // All jobs from the specified month in this year
        GregorianCalendar gcStart = new GregorianCalendar();
        gcStart.set(Calendar.YEAR, year);
       // gcStart.set(Calendar.MONTH, -1);
        gcStart.set(Calendar.MONTH, Calendar.JANUARY);
        gcStart.set(Calendar.DAY_OF_MONTH, 1);        
        gcStart.set(Calendar.HOUR_OF_DAY, 0);  
        gcStart.set(Calendar.MINUTE, 0);
        gcStart.set(Calendar.SECOND, 0);
        gcStart.set(Calendar.MILLISECOND, 0);
        startDate.setTime(gcStart.getTimeInMillis());           
        GregorianCalendar gcEnd = new GregorianCalendar();
        gcEnd.set(Calendar.YEAR, year);
        gcEnd.set(Calendar.MONTH, Calendar.DECEMBER);
        gcEnd.set(Calendar.DAY_OF_MONTH, gcStart.getActualMaximum(Calendar.DAY_OF_MONTH));        
        gcEnd.set(Calendar.HOUR_OF_DAY, 23);  
        gcEnd.set(Calendar.MINUTE, 59);
        gcEnd.set(Calendar.SECOND, 59);
        endDate.setTime(gcEnd.getTimeInMillis()); 
    }
    
    /**
     * Compute interval for passed month in passed year
     * @param month  1, 2, 3,..., 12
     * @param year   2005, 2006, etc.
     */
    public void computeMonth(int month, int year)
    {    
        
        if (year < 2004 || year > 2020)
        {
            throw new IllegalArgumentException("Year is in YYYY format. Invalid year: " + year);
        }
        
        if (month < 1 || month > 12)
        {
            throw new IllegalArgumentException("Month is in [01, 12]. Invalid month: " + month);
        }        
        
        // All Jobs from the specified month
        GregorianCalendar gcStart = new GregorianCalendar();
        gcStart.set(Calendar.YEAR, year);
        gcStart.set(Calendar.MONTH, month - 1);
        gcStart.set(Calendar.DAY_OF_MONTH, 1);        
        gcStart.set(Calendar.HOUR_OF_DAY, 0);  
        gcStart.set(Calendar.MINUTE, 0);
        gcStart.set(Calendar.SECOND, 1); // To be sure it's today and not tomorrow
        startDate.setTime(gcStart.getTimeInMillis());                  
        
        GregorianCalendar gcEnd = new GregorianCalendar();
        gcEnd.set(Calendar.YEAR, year);
        gcEnd.set(Calendar.MONTH, month - 1);
        gcEnd.set(Calendar.DAY_OF_MONTH, gcEnd.getActualMaximum(Calendar.DAY_OF_MONTH));        
        gcEnd.set(Calendar.HOUR_OF_DAY, 23);  
        gcEnd.set(Calendar.MINUTE, 59);
        gcEnd.set(Calendar.SECOND, 59);
        endDate.setTime(gcEnd.getTimeInMillis());         
        
        
    }

    /**
     * @return Returns the startDate.
     */
    public Timestamp getStartDate()
    {
        return startDate;
    }
    
    /**
     * @return Returns the endDate.
     */
    public Timestamp getEndDate()
    {
        return endDate;
    }


    
}
