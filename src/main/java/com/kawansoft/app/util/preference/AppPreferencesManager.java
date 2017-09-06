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
package com.kawansoft.app.util.preference;
/**
 * Allows to create a generic preferences store for all apps.
 * 
 * @author Nicolas de Pomereu
 */
public interface AppPreferencesManager {
    public static final String REMEMBER_ME = "REMEMBER_ME";
    public static final String LOGIN = "LOGIN";
    public static final String PASSWORD = "PASSWORD";
    public static String PROXY_TYPE          = "PROXY_TYPE";
    public static int PROXY_TYPE_BROWSER_DEF = 0; // default value
    public static int PROXY_TYPE_USER_DEF    = 1;
    public static int PROXY_TYPE_DIRECT      = 2;
    public static final String PROXY_PORT = "PROXY_PORT";
    public static final String PROXY_ADDRESS = "PROXY_ADDRESS";
    public static final String PROXY_AUTH_REMEMBER_INFO = "PROXY_AUTH_REMEMBER_INFO";
    public static final String PROXY_AUTH_USERNAME = "PROXY_AUTH_USERNAME";
    public static final String PROXY_AUTH_PASSWORD = "PROXY_AUTH_PASSWORD";
    public void setPreference(String prefName, String prefValue);
    public String getPreference(String prefName);
    public void removePreference(String prefName);
    public void setPreference(String prefName, boolean prefValue);
    public boolean getBooleanPreference(String prefName);
    public void setPreference(String prefName, int prefValue);
    public int getIntegerPreference(String prefName);
    public void setPreferenceEncrypt(int keylength, String prefName, String prefValue);
    public String getPreferenceDecrypt(String prefName);
    public void resetAll();
}
