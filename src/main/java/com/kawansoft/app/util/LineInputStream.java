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
package com.kawansoft.app.util;

import com.kawansoft.aceql.gui.util.AceQLManagerUtil;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PushbackInputStream;

/**
 * This class is to support reading CRLF terminated lines that contain only
 * US-ASCII characters from an input stream. Provides functionality that is
 * similar to the deprecated <code>DataInputStream.readLine()</code>. Expected
 * use is to read lines as String objects from a RFC822 stream.
 *
 * It is implemented as a FilterInputStream, so one can just wrap this class
 * around any input stream and read bytes from this filter.
 *
 * @author John Mani
 */
public class LineInputStream
        extends FilterInputStream {

    private char[] lineBuffer = null; // reusable byte buffer

    public LineInputStream(InputStream in) {
        super(in);
        //super(wrapStream(in));
    }

//	/**
//	 * Insures that the InputStream in use is a BufferedInputStream.
//	 * This will optimize calls to the sub-layer InputStream read() method and
//	 * prevent errors related to the PushBack mechanism (see readLine()).<br>
//	 * If the given InputStream is a BufferedInputStream, it is only casted and
//	 * returned unwrapped.
//	 * @param	isIn	the InputStream to wrap
//	 * @return	an instance of BufferedInputStream wrapping the given InputStream
//	 */
//	
//	private static BufferedInputStream wrapStream(InputStream isIn)
//	{
//		if(isIn instanceof BufferedInputStream)
//			return (BufferedInputStream) isIn ;
//		else
//			return new BufferedInputStream(isIn) ;
//	}
    /**
     * Read a line containing only ASCII characters from the input stream. A
     * line is terminated by a CR or NL or CR-NL sequence. The line terminator
     * is not returned as part of the returned String. Returns null if no data
     * is available.
     * <p>
     *
     * This class is similar to the deprecated
     * <code>DataInputStream.readLine()</code>
     */
    public String readLine()
            throws IOException {
        InputStream in = this.in;
        char[] buf = lineBuffer;

        if (buf == null) {
            buf = lineBuffer = new char[128];
        }

        int c1;
        int room = buf.length;
        int offset = 0;

        while ((c1 = in.read()) != -1) {
            if (c1 == '\n') // Got NL, outa here.
            {
                break;
            } else if (c1 == '\r') {
                // Got CR, is the next char NL ?
                int c2 = in.read();
                if (c2 != '\n') {
                    // If not NL, push it back
                    if (!(in instanceof PushbackInputStream)) {
                        in = this.in = new PushbackInputStream(in);
                    }
                    ((PushbackInputStream) in).unread(c2);
                }
                break; // outa here.
            }

            // Not CR, NL or CR-NL ...
            // .. Insert the byte into our byte buffer
            if (--room < 0) { // No room, need to grow.
                buf = new char[offset + 128];
                room = buf.length - offset - 1;
                System.arraycopy(lineBuffer, 0, buf, 0, offset);
                lineBuffer = buf;
            }
            buf[offset++] = (char) c1;
        }

        if ((c1 == -1) && (offset == 0)) {
            return null;
        }

        return String.copyValueOf(buf, 0, offset);
    }

    //Rule 8: Make your classes noncloneable
    public final Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    //Rule 9: Make your classes nonserializeable
    private final void writeObject(ObjectOutputStream out)
            throws IOException {
        AceQLManagerUtil.debugEvent(out);
        throw new IOException("Object cannot be serialized");
    }

    //Rule 10: Make your classes nondeserializeable
    private final void readObject(ObjectInputStream in)
            throws IOException {
        AceQLManagerUtil.debugEvent(in);
        throw new IOException("Class cannot be deserialized");
    }
}
