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
package com.kawansoft.aceql.gui;

import static com.kawansoft.aceql.gui.AceQLManager.LOOK_AND_FEEL_FLAT_DARCULA;
import static com.kawansoft.aceql.gui.AceQLManager.LOOK_AND_FEEL_FLAT_INTELLIJ;
import static com.kawansoft.aceql.gui.AceQLManager.LOOK_AND_FEEL_TO_USE;
import com.kawansoft.aceql.gui.util.UserPreferencesManager;
import java.awt.Color;

/**
 * Manager for Themes.
 * @author ndepo
 */
public class ThemeUtil {
        
    public static Color HYPERLINK_LIGHT =  new Color(38, 117, 191);
    public static Color HYPERLINK_DARK_MODE =  new Color(88, 157, 246);
    
        
    public static Color getHyperLinkColor() {
        UserPreferencesManager userPreferencesManager = new UserPreferencesManager();
        String lookAndFeelToUse = userPreferencesManager.getPreference(LOOK_AND_FEEL_TO_USE);

        // Use default if null
        if (lookAndFeelToUse == null) {
            return HYPERLINK_LIGHT;
        }
        
        if (lookAndFeelToUse.equals(LOOK_AND_FEEL_FLAT_INTELLIJ)) {
            return HYPERLINK_LIGHT;
        } else {
            return HYPERLINK_DARK_MODE;
        }
    }
    
    public static boolean isFlatLight() {
        UserPreferencesManager userPreferencesManager = new UserPreferencesManager();
        String lookAndFeel = userPreferencesManager.getPreference(LOOK_AND_FEEL_TO_USE);
        
        if (lookAndFeel == null) {
            lookAndFeel = LOOK_AND_FEEL_FLAT_INTELLIJ;
        }
        
        if (lookAndFeel.equals(LOOK_AND_FEEL_FLAT_INTELLIJ)) {
            return true;
        }
        else if (lookAndFeel.equals(LOOK_AND_FEEL_FLAT_DARCULA)) {
            return false;
        }
        else {
            throw new IllegalArgumentException("Illegal Look 1 Feel: " + lookAndFeel);
        }
    }
   
    public static boolean isFlatDark() {
        return !(isFlatLight());
    }
        
    public static void storeFlatLight() {
        UserPreferencesManager userPreferencesManager = new UserPreferencesManager();
        userPreferencesManager.setPreference(LOOK_AND_FEEL_TO_USE, LOOK_AND_FEEL_FLAT_INTELLIJ);
    }
    
    public static void storeDarkLight() {
        UserPreferencesManager userPreferencesManager = new UserPreferencesManager();
        userPreferencesManager.setPreference(LOOK_AND_FEEL_TO_USE, LOOK_AND_FEEL_FLAT_DARCULA);
    } 
}
