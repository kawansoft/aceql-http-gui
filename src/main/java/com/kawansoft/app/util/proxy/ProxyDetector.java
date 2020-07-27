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

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;

/**
 * 
 * Class to detect the HTTP(s) system proxy in use 
 * 
 * @author SafeLogic
 *
 */
public class ProxyDetector
{
    /** Boolean value */
    public static boolean DEBUG = false;
    
    /** The Proxy itself */
    private Proxy proxy = null;
    
    /** The Proxy host name */
    private String hostName = null;
    
    /** The Proxy port */
    private int port = 0;
    
    /**
     * Constructor
     */
    public ProxyDetector()
    {
        detectProxy();
    }
    
    
    /**
     * @return the proxy
     */
    public Proxy getProxy()
    {
        return proxy;
    }

    /**
     * @return the hostName
     */
    public String getHostName()
    {
        return hostName;
    }

    /**
     * @return the port
     */
    public int getPort()
    {
        return port;
    }

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        /*
        Example:
        proxy type     : HTTP
        proxy hostName : www.cgeep.com
        proxy port     : 80     
         */
        
        ProxyDetector proxyDetector = new ProxyDetector();
        
        System.out.println("hostName :" + proxyDetector.getHostName() + ":");
        System.out.println("port     :" + proxyDetector.getPort() + ":");
    }

       
    /**
     * Detect the HTTP (or SOCKS) proxy and set the field values
     */
    private void detectProxy()
    {
        System.setProperty("java.net.useSystemProxies", "true");
        
        List<Proxy> listProxy = null;
        try {
          listProxy = ProxySelector.getDefault().select(new URI("http://www.google.com"));
        }
        catch (URISyntaxException e) {
          e.printStackTrace();
          return;
        }

        if (listProxy != null) 
        {
           for (Iterator<Proxy> iter = listProxy.iterator(); iter.hasNext();) 
           {
              Proxy proxy = (Proxy) iter.next();
                                                       
              Type type = proxy.type();
              debug("");
              debug("proxy type : " + type);
              
              if (type == Type.HTTP)
              {
                  InetSocketAddress addr = (InetSocketAddress) proxy.address();
                  
                  if (addr == null) {
                    debug("No Proxy");
                    return;
                  }
                  else {
                    debug("proxy hostname : " + addr.getHostName());
                    debug("proxy port : " + addr.getPort());
                    
                    this.proxy = proxy;
                    this.hostName = addr.getHostName();
                    this.port = addr.getPort();
                    return;
                  }
              }
              
           }
        }
    }
    
    /**
     * debug tool
     */
    private void debug(String s)
    {
        if (DEBUG)
        {
            System.out.println("debug> " + s);
        }
    }
    
}

/**
 * 
 */
