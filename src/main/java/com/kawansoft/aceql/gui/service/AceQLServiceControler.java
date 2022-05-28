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
package com.kawansoft.aceql.gui.service;

import com.kawansoft.aceql.gui.task.AceQLTask;
import com.kawansoft.aceql.gui.util.AceQLManagerUtil;
import com.kawansoft.aceql.gui.util.ConfigurationUtil;
import com.kawansoft.app.util.ClientLogger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.kawanfw.sql.servlet.AceQLLicenseFileFinder;


/**
 * @author Nicolas de Pomereu
 *
 */
public class AceQLServiceControler {

    public static final String CR_LF = System.getProperty("line.separator");

    
    public static void start(String arg[]) {

        AceQLLicenseFileFinder.reset();
        System.out.println(ClientLogger.formatLogMsg(AceQLServiceControler.class, "Starting " + AceQLTask.class.getSimpleName() + "..."));
        
        String aceqlProperties = null;
        String host = null;
        int port = 0;

        File configurationProperties = ConfigurationUtil.getConfirurationPropertiesFile();
        
        if (! configurationProperties.exists()) {
            throw new IllegalArgumentException(new FileNotFoundException("The configuration file that stores properties file name, host and port does not exist: " + configurationProperties));
        }
        
        ConfigurationUtil configurationUtil = new ConfigurationUtil();
        try {
            configurationUtil.load();
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
        
       aceqlProperties = configurationUtil.getAceqlProperties();
       host = configurationUtil.getHost();
       port = configurationUtil.getPort();
      
        // Ok, launch task
        AceQLTask aceQLTask = new AceQLTask(AceQLTask.SERVICE_MODE, aceqlProperties, host, port);
        aceQLTask.start();
    }

    public static void stop(String arg[]) {
        
	System.out.println(ClientLogger.formatLogMsg(AceQLServiceControler.class, "Stopping " + AceQLTask.class.getSimpleName() + "..." ));        
        AceQLManagerUtil.systemExitWrapper();
    }

}
