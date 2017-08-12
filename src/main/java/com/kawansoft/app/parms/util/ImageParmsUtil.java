
package com.kawansoft.app.parms.util;

import com.kawansoft.app.parms.Parms;
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
        java.net.URL imgURL = Parms.class.getResource(path);
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
        return ImageParmsUtil.createImageIcon(Parms.ICON).getImage();
    }
    
     /**
     * Returns the Tray Icon to use
     * @return  the Tray Icon to use
     */
    public static Image getTrayIcon() {
        return ImageParmsUtil.createImageIcon(Parms.TRAY_ICON).getImage();
    }
    
    /**
     * Returns the Big Logo Icon to use
     * @return  the Big Logo to use
     */
    public static Icon getBigLogo() {
        return ImageParmsUtil.createImageIcon(Parms.LOGO_BIG);
    }
    
    /**
     * Returns the Big Logo Icon to use
     * @return  the Big Logo to use
     */
    public static Icon getSmallLogo() {
        return ImageParmsUtil.createImageIcon(Parms.LOGO_SMALL);
    }
   
}
