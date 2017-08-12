/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kawansoft.app.util;

import javax.swing.UIManager;

/**
 * Utility class for Look & feels
 * @author Nicolas de Pomereu
 */
public class UI_Util {
    
    /**
     * Says if UI is Oracle Nimbus
     * @return 
     */
    public static boolean isNimbus() {
        if (UIManager.getLookAndFeel().getName().contains("Nimbus")   ) {
            return true;
        }
        else {
            return false;
        }            
    }
    
    /**
     * Says if UI is Oracle Nimbus
     * @return 
     */
    public static boolean isSynthetica() {
        if (UIManager.getLookAndFeel().getName().toLowerCase().contains("synthetica")) {
            return true;
        }        
        else {
            return false;
        }
    }    
    
    
}
