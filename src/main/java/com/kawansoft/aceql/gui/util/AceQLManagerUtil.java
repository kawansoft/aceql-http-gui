/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kawansoft.aceql.gui.util;

import java.awt.event.InputEvent;

/**
 *
 * @author Nicolas de Pomereu
 */
public class AceQLManagerUtil {
    
    public static void systemExitWrapper() {
        System.exit(0);
        
    }

    public static void printEvent(Object obj) {
        boolean print = false;
        if (print) {
            System.out.println(obj);
        }
    }
    
    
}
