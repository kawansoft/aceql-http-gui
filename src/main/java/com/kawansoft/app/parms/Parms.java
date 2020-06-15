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
package com.kawansoft.app.parms;

/**
 * Application parameters The implementations are in 
 *
 * @author Nicolas de Pomereu
 */
public class Parms {
    
    // BEGIN COMMON TO ALL APPS
    public static String EXE_NAME  = "AceQLHTTP.exe"; 

    public static String APP_NAME = "AceQL HTTP";
    
    // Tray and default App Icon 
    public static final String TRAY_ICON = "images/data_up_20.png";

    public static final String ICON = "images/data_up_24.png";
    
    public static final String LOGO_BIG = "images/logos/logo-AceQL.png";
    public static final String LOGO_SMALL = "images/logos/logo-AceQL_48.png";
        
    public static final String DOT_APP_DIR = ".aceql-http";
    
    // For automatic install of new versions:    
//    public static final int INSTALLER_APPROX_SIZE = ParmsGetter.getInt("installer_approx_size") * ParmsUtil.MB;
//    public static final String INSTALLER_WINDOWS = ParmsGetter.getFromPropFile("installer_windows");
//    public static final String INSTALLER_OSX =  ParmsGetter.getFromPropFile("installer_osx"); 
//    public static final String INSTALLER_LINUX = ParmsGetter.getFromPropFile("installer_linux");
   
    // About 
    public static final String ABOUT_WEB_SITE = "www.aceql.com";
    public static final String ABOUT_EMAIL_SUPPORT = "support@kawansoft.com";
    public static final String ABOUT_EMAIL_SALES = "contact@kawansoft.com";
       
   // Says if server support non ascii chars for file upload
   //public static final boolean SUPPORT_NON_ASCII_CHARS_IN_FILE = ParmsGetter.getBoolean("support_non_ascii_chars_in_file");
   
    // For cover page propaganda
    public static final String CONTACT_AND_INFORMATION = "www.aceql.com";
   
    
}
