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
package com.kawansoft.app.version;

/**
 * Displays the SafeJdbc product GuiVersion
 */

public class GuiVersion {

    public static final String getVersion() {
	return "" + new PRODUCT();
    }
    
    public static final String getServerVersion() {
	return "" + new PRODUCT().server();
    }

    public static final String getFullVersion() {
	String CR_LF = System.getProperty("line.separator");

	return PRODUCT.DESCRIPTION + CR_LF + getVersion() + CR_LF + "by : "
		+ new VENDOR();
    }

    public String toString() {
	return getVersion();
    }

    public static final class PRODUCT {

	public static final String NAME = "GUI Manager";
	public static final String VERSION = GuiVersionValues.VERSION;
	public static final String DESCRIPTION = "Remote SQL access over HTTP";
	public static final String TYPE_OPEN_SOURCE = "Open Source";
	public static final String TYPE_PROFESSIONAL = "Professional";
	public static final String DATE = GuiVersionValues.DATE;

	public static String TYPE = (isOpenSourceVersion()) ? TYPE_OPEN_SOURCE : TYPE_PROFESSIONAL;
	
	public String toString() {
	    return NAME + " " +  VERSION + " - " + DATE;
	}
	
	public String server() {
	    return NAME + " " + TYPE + " " + VERSION + " - " + DATE;
	}
    }

    public static final class VENDOR {
	public static final String NAME = "KawanSoft SAS";
	public static final String WEB = "http://www.kawansoft.com";
	public static final String COPYRIGHT = "Copyright &copy; 2020";
	public static final String EMAIL = "contact@kawansoft.com";

	public String toString() {
	    return VENDOR.NAME + " - " + VENDOR.WEB;
	}
    }

    /**
     * Says if the current AceQL version is Open Source or Professional
     * @return
     */
     static boolean isOpenSourceVersion() {
	try {
	    @SuppressWarnings("unused")
	    Class<?> c = Class.forName("org.kawanfw.sql.licensing.LicenseBuilder");
	    return false;
	} catch (ClassNotFoundException e) {
	    return true;
	}
    }
    
    /*
     * //Rule 8: Make your classes noncloneable public final Object clone()
     * throws java.lang.CloneNotSupportedException { throw new
     * java.lang.CloneNotSupportedException(); }
     * 
     * //Rule 9: Make your classes nonserializeable private final void
     * writeObject(ObjectOutputStream out) throws java.io.IOException { throw
     * new java.io.IOException("Object cannot be serialized"); }
     * 
     * //Rule 10: Make your classes nondeserializeable private final void
     * readObject(ObjectInputStream in) throws java.io.IOException { throw new
     * java.io.IOException("Class cannot be deserialized"); }
     */
    /**
     * MAIN
     */

    public static void main(String[] args) {
	System.out.println(getFullVersion());

	System.out.println(GuiVersion.PRODUCT.NAME);
    }
}
