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
package com.kawansoft.app.util.classpath;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Tributes to:
 * - https://community.oracle.com/message/5205737#5203737
 * - http://stackoverflow.com/users/342852/sean-patrick-floyd in:
 *   http://stackoverflow.com/questions/3580752/java-dynamically-loading-a-class/3581598#3581598
 */

public class ClassPathHacker{

    private static final Class<URLClassLoader> URLCLASSLOADER =
        URLClassLoader.class;
    private static final Class<?>[] PARAMS = new Class[] { URL.class };

    public static void addFile(final String s) throws IOException{
        addFile(new File(s));
    }

    public static void addFile(final File f) throws IOException{
        addURL(f.toURI().toURL());
    }

    public static void addURL(final URL u) throws IOException{

        final URLClassLoader urlClassLoader = getUrlClassLoader();

        try{
            final Method method = getAddUrlMethod();
            method.setAccessible(true);
            method.invoke(urlClassLoader, new Object[] { u });
        } catch(final Exception e){
            throw new IOException(
                "Error, could not add URL to system class loader");
        }

    }

    private static Method getAddUrlMethod()
        throws NoSuchMethodException{
        if(addUrlMethod == null){
            addUrlMethod =
                URLCLASSLOADER.getDeclaredMethod("addURL", PARAMS);
        }
        return addUrlMethod;
    }

    private static URLClassLoader urlClassLoader;
    private static Method addUrlMethod;

    private static URLClassLoader getUrlClassLoader(){
        if(urlClassLoader == null){
            final ClassLoader sysloader = 
                ClassLoader.getSystemClassLoader();
            if(sysloader instanceof URLClassLoader){
                urlClassLoader = (URLClassLoader) sysloader;
            } else{
                throw new IllegalStateException(
                    "Not an UrlClassLoader: "
                    + sysloader);
            }
        }
        return urlClassLoader;
    }

}
