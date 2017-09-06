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
package com.kawansoft.app.util.proxy;

import com.kawansoft.app.util.preference.AppPreferencesManager;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * @author Nicolas de Pomereu
 * 
 * Allows to detecdt and set the address & port of a proxy
 * to HttpNetworkParameters instance
 */
public class ProxyUtil
{

    /**
     * Returns the Proxy to use from the fields defined in FramePreferences
     *
     * @param userPreferencesManager
     * @return the Proxy to use from the fields defined in FramePreferences
     */
    public static Proxy getProxy(AppPreferencesManager userPreferencesManager) {
        
        int proxyType= userPreferencesManager.getIntegerPreference(AppPreferencesManager.PROXY_TYPE);
               
        if (proxyType == AppPreferencesManager.PROXY_TYPE_BROWSER_DEF)
        {
            ProxyDetector proxyDetector = new ProxyDetector();
            String address  = proxyDetector.getHostName();
            int port = proxyDetector.getPort();
            
            if (address == null) {
                return null;
            }
           
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
                    address, port));
            return proxy;
        }
        else if (proxyType == AppPreferencesManager.PROXY_TYPE_USER_DEF)
        {
            String address = userPreferencesManager.getPreference(AppPreferencesManager.PROXY_ADDRESS);
            String portStr = userPreferencesManager.getPreference(AppPreferencesManager.PROXY_PORT);
            int port = Integer.parseInt(portStr);

            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
                    address, port));
            return proxy;
        }
        else if (proxyType == AppPreferencesManager.PROXY_TYPE_DIRECT)
        {
            return null;
        }
        else
        {
            throw new IllegalArgumentException("Proxy Type is invalid: " + proxyType);
        }
        
    }
      
}
