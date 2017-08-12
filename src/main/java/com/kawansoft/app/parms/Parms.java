package com.kawansoft.app.parms;

import com.kawansoft.app.parms.util.ParmsUtil;

/**
 * Application parameters The implementations are in 
 *
 * @author Nicolas de Pomereu
 */
public class Parms {
    
    // BEGIN COMMON TO ALL APPS
    public static final String APP_NAME = "AceQL HTTP Windows Manager";

    // Tray and default App Icon 
    public static final String TRAY_ICON = "images/aceql_rocket_24.png";

    public static final String ICON = "images/aceql_rocket_24.png";
    
    public static final String LOGO_BIG = "images/logos/logo-AceQL.png";
    public static final String LOGO_SMALL = "images/logos/logo-AceQL_48.png";
        
    public static final String DOT_APP_DIR = ".aceql-http-windows-manager";

    public static final String INSTALLER_DIR = "https://www.aceql.com/download";
    
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
