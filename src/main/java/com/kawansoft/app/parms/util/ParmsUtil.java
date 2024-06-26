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
package com.kawansoft.app.parms.util;

import com.kawansoft.app.parms.ParmsConstants;
import java.awt.Color;
import java.io.File;
import java.util.Date;
import org.apache.commons.lang3.SystemUtils;

/**
 *
 * @author Nicolas de Pomereu
 */
public class ParmsUtil {
    
    // Size helpers
    public static final int KB = 1024;
    public static final int MB = KB * KB;
    
    public static String CR_LF = System.getProperty("line.separator");
    public static final String CHARSET_UTF_8 = "UTF-8";
    public static final String CHARSET_ISO_8859_1 = "ISO-8859-1";
    
    /** To use for background selection */
    public static Color LIGHT_BLUE = new Color(243, 243, 255);
    public static Color URL_COLOR = new Color(51, 0, 255);

    public static String CUT_ICON       = "images/cut.png";
    public static String PASTE_ICON     = "images/paste.png";
    public static String COPY_ICON      = "images/copy.png";
    public static String DELETE_ICON    = "images/delete.png";
    
    /** Internet status images */
    public static final String IMAGES_BULLET_BALL_YELLOW_PNG = "images/bullet_ball_yellow.png";
    public static final String IMAGES_BULLET_BALL_GREEN_PNG = "images/bullet_ball_green.png";
    public static final String IMAGES_BULLET_BALL_RED_PNG = "images/bullet_ball_red.png";
    public static final String IMAGES_BULLET_BALL_GREY_PNG = "images/bullet_ball_grey.png";
    
    // To display a different look & feel
    public static String LOOK_AND_FEEL_TXT = ParmsUtil.getDebugDir() + File.separator + "lookAndFeel.txt";
    
    public static String fillAppNameAndLanguage(final String templateFinal,
	    String appName, String language) {
        String template = templateFinal.replace("{0}", appName);
	template = template.replace("{1}", language);
	return template;
    }
        
//    public static boolean isAceQLPro() {
//        //return org.kawanfw.sql.version.Version.PRODUCT.TYPE.equals(org.kawanfw.sql.version.Version.PRODUCT.TYPE_ENTERPRISE);
//        return true;
//    }
    
    
    public static String getInstallAceQLDir() {
        return SystemUtils.USER_DIR + File.separator + "AceQL";
    }

    
    
    /**
     * Returns the base dir
     * @return c:\.DOT_APP_DIR or /usr/local/APP_NAME.
     * In case there is a aceql.service.dir property , it returns the di contained in the directory
     */
    public static String getBaseDir() {
                
        // Case the user has hacked the .bat for service cration and has added the aceql.service.dir property to
        // define a special base dir
        String forceUserHome = System.getProperty("is.windows.service");
        if (forceUserHome != null && Boolean.parseBoolean(forceUserHome) && SystemUtils.IS_OS_WINDOWS) {
            System.out.println(new Date() + " " + "is.windows.service property is true. Force user.home directory for base dir configuration: " + SystemUtils.USER_HOME);
            File userHome = new File(SystemUtils.USER_HOME);
            if (!userHome.exists()) {
                userHome.mkdirs();
            }
            if (! userHome.exists()) {
                throw new IllegalArgumentException("Can not created the designated user.home " + userHome);
            }
            return userHome.toString();
        }
        
        File baseDir = null;
        
        if (SystemUtils.IS_OS_WINDOWS) {
            baseDir = new File("c:\\" + ParmsConstants.DOT_APP_DIR);
        }
        else {
            baseDir = new File("/usr/local/" + ParmsConstants.DOT_APP_DIR);
        }
        
        if (!baseDir.exists()) {
            baseDir.mkdirs();
        }
        return baseDir.toString();
    }

    /**
     * Returns the debug dir
     * @return c:\.DOT_APP_DIR/debug or /usr/local/AceQL/debug
     */
    public static String getDebugDir() {
        File logDir = new File(getBaseDir() + File.separator + "debug");
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
        return logDir.toString();
    }


    /**
     * The temp dir including final separator
     * @return
     */
    public static String getTempDir() {
        String tempDirStr = System.getProperty("java.io.tmpdir") + File.separator + ParmsConstants.DOT_APP_DIR + File.separator;
        File tempDir = new File(tempDirStr);
        tempDir.mkdir();
        return tempDirStr;
    }



    
    
    
}
