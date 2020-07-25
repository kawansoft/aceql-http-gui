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
package com.kawansoft.aceql.gui.util;

import com.kawansoft.app.parms.util.ParmsUtil;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.TreeSet;
import org.kawanfw.sql.tomcat.TomcatStarterUtil;

/**
 *
 * @author Nicolas de Pomereu
 */
public class ConfigurationUtil {

    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 9090;

    public static final String PORT = "port";
    public static final String HOST = "host";
    public static final String ACEQL_PROPERTIES = "aceql_properties";

    private String aceqlProperties = null;
    private String host = null;
    private int port = 0;

    public static File getConfirurationPropertiesFile() {
        File confDir = new File(ParmsUtil.getBaseDir() + File.separator + "conf");
        if (!confDir.exists()) {
            confDir.mkdirs();
        }
        File configurationProperties = new File(confDir.toString() + File.separator + "configuration.properties");
        return configurationProperties;
    }

    public void load() throws IOException {

        File configurationProperties = getConfirurationPropertiesFile();

        if (!configurationProperties.exists()) {
            return;
        }

        Properties properties = TomcatStarterUtil.getProperties(configurationProperties);

        aceqlProperties = properties.getProperty(ACEQL_PROPERTIES);
        host = properties.getProperty(HOST);
        String portStr = properties.getProperty(PORT);

        if (portStr != null) {
            try {
                port = Integer.parseInt(portStr);
            } catch (NumberFormatException numberFormatException) {
                numberFormatException.printStackTrace();
            }
        }

    }

    public void store(String aceqlProperties, String host, int port) throws IOException {

        if (aceqlProperties == null) {
            throw new NullPointerException("aceqlProperties is null!");
        }
        if (host == null) {
            throw new NullPointerException("host is null!");
        }
        if (port == 0) {
            throw new NullPointerException("port is zero!");
        }

        if (!new File(aceqlProperties).exists()) {
            throw new FileNotFoundException("aceql properties file does not exists: " + aceqlProperties);
        }

        File configuationProperties = getConfirurationPropertiesFile();
        try (final OutputStream out = new FileOutputStream(configuationProperties)) {
            
            Properties properties = new Properties();
            properties.setProperty(ACEQL_PROPERTIES, aceqlProperties);
            properties.setProperty(HOST, host);
            properties.setProperty(PORT, "" + port);
            
            Properties sortedProp = new Properties() {
                @Override
                public synchronized Enumeration<Object> keys() {
                    return Collections.enumeration(new TreeSet<Object>(super.keySet()));
                }
            };
            sortedProp.putAll(properties);

            sortedProp.store(out, "AceQL HTTP properties - do not edit");
        }
    }

    /**
     * @return the aceqlProperties
     */
    public String getAceqlProperties() {
        return aceqlProperties;
    }

    /**
     * @return the host
     */
    public String getHost() {
        if (host == null) {
            host = DEFAULT_HOST;
        }
        return host;
    }

    /**
     * @return the port
     */
    public int getPort() {
        if (port == 0) {
            port = DEFAULT_PORT;
        }
        return port;
    }

}
