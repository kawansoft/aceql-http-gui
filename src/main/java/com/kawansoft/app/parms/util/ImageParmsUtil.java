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
package com.kawansoft.app.parms.util;

import com.kawansoft.app.parms.ParmsConstants;
import java.awt.Image;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Tools to build icons.
 * @author Nicolas de Pomereu
 */
public class ImageParmsUtil {
     
    /**
     * Returns an ImageIcon, or null if the path was invalid.
     * @return an ImageIcon, or null if the path was invalid
     */
    public static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = ParmsConstants.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
    
    /**
     * Returns the App Icon to use
     * @return  the App Icon to use
     */
    public static Image getAppIcon() {
        return ImageParmsUtil.createImageIcon(ParmsConstants.ICON).getImage();
    }
    
     /**
     * Returns the Tray Icon to use
     * @return  the Tray Icon to use
     */
    public static Image getTrayIcon() {
        return ImageParmsUtil.createImageIcon(ParmsConstants.TRAY_ICON).getImage();
    }
    
    /**
     * Returns the Big Logo Icon to use
     * @return  the Big Logo to use
     */
    public static Icon getBigLogo() {
        return ImageParmsUtil.createImageIcon(ParmsConstants.LOGO_BIG);
    }
    
    /**
     * Returns the Big Logo Icon to use
     * @return  the Big Logo to use
     */
    public static Icon getSmallLogo() {
        return ImageParmsUtil.createImageIcon(ParmsConstants.LOGO_SMALL);
    }
   
}
