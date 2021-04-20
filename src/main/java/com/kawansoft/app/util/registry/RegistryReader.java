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
package com.kawansoft.app.util.registry;

import java.lang.reflect.Method;
import java.util.prefs.Preferences;

import javax.swing.JOptionPane;


/**
 * Method to read access the Windows RegistryReader without using JNI.
 * Please see the main() code for how to use.
 * 
 * @author Nicolas de Pomereu
 */

public class RegistryReader
{    
    // RegistryReader constants
    public static final int HKEY_CURRENT_USER = 0x80000001;
    public static final int HKEY_LOCAL_MACHINE = 0x80000002;
    public static final int KEY_QUERY_VALUE = 1;
    public static final int KEY_SET_VALUE = 2;
    public static final int KEY_READ = 0x20019;
    public static final int KEY_ALL_ACCESS = 0xf003f;
    
    // RegistryReader functions to load using java reflection
    public Method openKey;
    public Method closeKey;
    public Method winwRegCreateKeyEx;
    public Method winRegQueryValue;
    public Method winRegEnumValue;
    public Method winRegQueryInfo;
    public Method WindowsRegSetValueEx;
    public Class<? extends Preferences> clz;
    public Preferences userRoot;
    public Preferences systemRoot;
    
        /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws Exception {
        
        String subKey = "Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\FileExts\\.properties\\UserChoice";
        String value = "ProgId";
        
        RegistryReader registryReader = new RegistryReader();
        String useValue = registryReader.getCurrentUserKeyValue(subKey, value);
        
        System.out.println("userValue: " + useValue);
    }
    /**
     * Consctructor
     */
    public RegistryReader()
        throws NoSuchMethodException
    {
                
        if (! isWindowsOs())
        {
            return;
        }
        
        userRoot = Preferences.userRoot();
        systemRoot = Preferences.systemRoot();
        
        clz = systemRoot.getClass();
        
        openKey = clz.getDeclaredMethod("openKey",
                byte[].class, int.class, int.class);
        openKey.setAccessible(true);
                
        closeKey = clz.getDeclaredMethod("closeKey",
                int.class);
        closeKey.setAccessible(true);
        
        //private static native int[] WindowsRegCreateKeyEx(int hKey, byte[] subKey);
        
        winwRegCreateKeyEx  = clz.getDeclaredMethod("WindowsRegCreateKeyEx",
                int.class,  byte[].class);
        winwRegCreateKeyEx.setAccessible(true);
        
        //private static native byte[] WindowsRegQueryValueEx(int hKey, byte[] valueName); 
        
        winRegQueryValue = clz.getDeclaredMethod(
                        "WindowsRegQueryValueEx", int.class, byte[].class);
        winRegQueryValue.setAccessible(true);
        
        
        winRegEnumValue = clz.getDeclaredMethod(
                        "WindowsRegEnumValue1", int.class, int.class, int.class);
        winRegEnumValue.setAccessible(true);
        
        winRegQueryInfo = clz.getDeclaredMethod(
                        "WindowsRegQueryInfoKey1", int.class);
        winRegQueryInfo.setAccessible(true);
        
        WindowsRegSetValueEx = clz.getDeclaredMethod(
                        "WindowsRegSetValueEx1", int.class, byte[].class, byte[].class);
        WindowsRegSetValueEx.setAccessible(true);

    }

    /**
     * @return true if the OS is Windows
     */
    private boolean isWindowsOs()
    {
        return System.getProperty("os.name").contains("Windows");
    }
    
    /**
     * Extract from the Windows RegistryReader HKEY_LOCAL_MACHINE a String data value 
     * 
     * @param subKey    The registry subkey name
     * @param value     The registry subkey value
     * @return          The data value as tring
     * 
     * @throws Exception
     */
    public String getLocalMachineKeyValue(String subKey, String value)
        throws Exception
    {                
        if (! isWindowsOs())
        {
            String message = "Warning! Microsoft Windows specific getLocalMachineKeyValue() called: will return null!";
            JOptionPane.showMessageDialog(null, message);
            return null;
        }
               
        Integer hSettings = (Integer) openKey.invoke(systemRoot,
                toByteArray(subKey), KEY_READ, KEY_READ);
        
        byte[] b = (byte[]) winRegQueryValue.invoke(systemRoot, hSettings
                .intValue(), toByteArray(value));
                
        String data = b != null ? new String(b).trim() : null;
        
        closeKey.invoke(Preferences.userRoot(), hSettings);

        return data;
        
    }
    
    /**
     * Extract from the Windows RegistryReader HKEY_CURRENT_USER a String data value 
     * 
     * @param subKey    The registry subkey name
     * @param value     The registry subkey value
     * @return          The data value as String
     * 
     * @throws Exception
     */
    public String getCurrentUserKeyValue(String subKey, String value)
        throws Exception
    {
   
        if (! isWindowsOs())
        {
            String message = "Warning! Microsoft Windows specific getCurrentUserKeyValue() called: will return null!";
            JOptionPane.showMessageDialog(null, message);
            return null;
        }
        
        //openKey = clz.getDeclaredMethod("openKey",
        //        byte[].class, int.class, int.class);
        
        Integer hSettings = (Integer) openKey.invoke(userRoot,
                toByteArray(subKey), KEY_READ, KEY_READ);
        
        //winRegQueryValue = clz.getDeclaredMethod(
        //        "WindowsRegQueryValueEx", int.class, byte[].class);
        
        byte[] b = (byte[]) winRegQueryValue.invoke(userRoot, hSettings
                .intValue(), toByteArray(value));
                
        //System.out.println("hSettings: " + hSettings);        
        //System.out.println("b:" + b);        
        //System.out.println(winRegQueryValue.invoke(userRoot, hSettings.intValue(), toByteArray(value)));
        
        String data = b != null ? new String(b).trim() : null;
        
        //closeKey.invoke(Preferences.userRoot(), hSettings);

        return data;
        
    }   
    
        
    private static byte[] toByteArray(String str) {
        byte[] result = new byte[str.length() + 1];
        for (int i = 0; i < str.length(); i++) {
            result[i] = (byte) str.charAt(i);
        }
        result[str.length()] = 0;
        return result;
    }
    
    
}
