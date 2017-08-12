
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
