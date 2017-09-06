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
package com.kawansoft.app.parms;

import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;



/**
 * Class to dynamically get messages from fields names.
 * <br>
 * The class uses basic reflection technique to get directly a message value from the field
 * name.
 */
public class MessagesManager
{   
    public static final boolean FORCE_ENGLISH_LOCALE = true;
    
    /** The Package that contains all properties messages & html files */
    public static String MESSAGE_FILES_PACKAGE = "com.kawansoft.app.parms.helpfiles";
        
    /** Resource Bundle instance */
    private static ResourceBundle RESOURCE_BUNDLE = null;
    
    /**
     * Constructor
     */
    public MessagesManager()
    {
        
    }
        
    public void loadResourcesBundle() {
        Locale locale = null;

        // Force English Locale if necessary
        if (FORCE_ENGLISH_LOCALE) {
            locale = Locale.ENGLISH;
        }
        else {
            locale = new Locale(LanguageManager.getLanguage());
        }
        
        System.out.println("locale: " + locale);
        
        // Messages are contained in:
        // com.kawansoft.app.parms.helpfiles.Messages_fr.properties
        // com.kawansoft.app.parms.helpfiles.Messages_en.properties
        // Etc.
        String baseName = MESSAGE_FILES_PACKAGE + ".Messages";

        //System.out.println("baseName: " + baseName);
        try {
            //
            // Note: We support only fr and en languages in Version 1.00
            //            
            RESOURCE_BUNDLE = ResourceBundle.getBundle(baseName, locale);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.toString(), Parms.APP_NAME, JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * New method that uses ResourceBundle(s)
     * 
     * returns the full message associated with a message parameter
     * 
     * @param messageParam      the message parameter
     * 
     * @return                  the message value in the desired language
     */
    public static String get(String messageParam)
    {
        if (messageParam == null)
        {
            return "null";            
        }
        
        if (RESOURCE_BUNDLE == null) {
            MessagesManager  messagesManager =  new MessagesManager();
            messagesManager.loadResourcesBundle();
        }
        
        // Always ask for the Lower case parameter
        messageParam = messageParam.toLowerCase();
        messageParam = messageParam.trim();
                
        String messageValue = null;    
                
        try
        {
            messageValue = RESOURCE_BUNDLE.getString(messageParam);            
            return messageValue;
        }
        catch (Exception e)
        {
            // We choose to quietly send back the messageParam as messageValue
            return messageParam;
        }
                
    }    
    
    
    
    
    
}
