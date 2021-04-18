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

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Windows settings saver and loader.
 * @author Nicolas de Pomereu
 */

public class WindowSettingMgr
{

    
    /** Not visible constructor */
    protected WindowSettingMgr()
    {
        
    }

    /**
     * Store the JFrame setting
     * @param window
     */
    public static void save(Window window)
    {        
        // KEEP THIS COMMENT - Taille des fenetres : Slide 72 : 1:1 / 4:5 / 4:3 / 16:9
        // KEEP THIS COMMENT - 1  / 0,80 / 1,33 / 1,78
        
        String jframeName = window.getClass().getName();
        WindowSettingMgr windowSettingMgr = new WindowSettingMgr();
        
        int width = window.getWidth();   
        int height = window.getHeight();   
        
        if (WindowSettingMgrDebugConstants.DEBUG)
        {
            System.out.println();
            System.out.println("Best Ratio: 1 / 0,80 / 1,33 / 1,78");
            System.out.println("window size : (" + width + ", " + height + ")");
            System.out.println("ratio w/h   : " + (float)width/height);
            System.out.println("ratio h/w   : " + (float) height/width);
        }
        
        Preferences prefs = Preferences.userNodeForPackage(windowSettingMgr.getClass());
        prefs.put("x_" + jframeName, "" + window.getX());
        prefs.put("y_" + jframeName, "" + window.getY());
        prefs.put("w_" + jframeName, "" + window.getWidth());
        prefs.put("h_" + jframeName, "" + window.getHeight());        
    }
    
    /**
     * Load the JFrame settting at stored position.
     * Defaults to center of Window.
     */
    public static void load(Window window)
    {
              
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        
        Point middlePoint = new Point((dim.width/2)-(window.getWidth()/2) , 
                                      (dim.height/2)-(window.getHeight()/2));
        
        load(window, middlePoint);
    }
        
    /** 
     * Load the JFrame settting at stored location. Defaults to Passed Point
     * 
     * @param defaultPoint the default position if the location is not stored
     */
    public static void load(Window window, Point defaultPoint)
    {        
//        try {
//            ComponentsText.writeComponentsInPropertiesFile(window);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//
//        try {
//            ComponentsText.setComponentsTextFromPropertiesFile(window);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
        
        window.setLocation(defaultPoint);
        
        String jframeName = window.getClass().getName();        
        
        WindowSettingMgr windowSettingMgr = new WindowSettingMgr();
        
        Preferences prefs = Preferences.userNodeForPackage( windowSettingMgr.getClass());
        String frame_x = prefs.get("x_" + jframeName, "0");
        String frame_y = prefs.get("y_" + jframeName, "0");
        String frame_w = prefs.get("w_" + jframeName, "0");
        String frame_h = prefs.get("h_" + jframeName, "0");    
        
        int x = 0;
        int y = 0;
        int w = 0;
        int h = 0;
        
        try
        {
            x = Integer.parseInt(frame_x);
            y = Integer.parseInt(frame_y);
            w = Integer.parseInt(frame_w);
            h = Integer.parseInt(frame_h);
        }
        catch (NumberFormatException e)
        {
            e.printStackTrace();
            return;
        }
        
        if (x == 0 && y == 0 && w == 0 && h == 0)
        {
            return;
        }
         
        window.setLocation(x,y);
        window.setPreferredSize(new Dimension(w,h));         
        
    }    
    
    /**
     * Realod the JFrame settting
     */
    public static void reload(Window window)
    {  
        save(window);
        load(window);
    }
    
    /**
     * Reset all frames
     */
    public static void resetAll()
    {
        WindowSettingMgr windowSettingMgr = new WindowSettingMgr();
        Preferences prefs = Preferences.userNodeForPackage( windowSettingMgr.getClass());
        try
        {
            prefs.clear();
        }
        catch (BackingStoreException e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Displays the specified message if the DEBUG flag is set.
     * @param   sMsg    the debug message to display
     */

    protected void debug(String sMsg)
    {
        if(WindowSettingMgrDebugConstants.DEBUG)
            System.out.println("DBG> " + sMsg) ;
    }    
    
    public static void main(String[] args)
    {
        WindowSettingMgr.resetAll();
    }
    
}

