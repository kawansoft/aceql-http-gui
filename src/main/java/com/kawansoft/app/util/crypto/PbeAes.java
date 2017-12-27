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
package com.kawansoft.app.util.crypto;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.codec.binary.Hex;

import org.apache.commons.io.IOUtils;

public class PbeAes {

    public static final String KAWANFW_ENCRYPTED = "*!aw!*";

    /**
     *
     * Encrypt a string into a an hexa format using 128 bits keyLength.
     *
     * @param text the string to encrypt or Decrypt. if to decrypt: string must
     * be Hex encoded
     * @param password the password to use
     * @return the encrypted string in hexa format.
     *
     * @throws Exception
     */
    public String encryptToHexa(String text, char[] password) throws Exception {
        int keyLength = 128;
        return encryptToHexa(keyLength, text, password);
    }

    /**
     *
     * Encrypt a string into a an hexa format using the specified keyLength
     * value
     *
     * @param keyLength the keyLength must be 128, 192 or 256.
     * @param text the string to encrypt or Decrypt. if to decrypt: string must
     * be Hex encoded
     * @param password the password to use
     * @return the encrypted string in hexa format.
     *
     * @throws Exception
     */
    public String encryptToHexa(int keyLength, String text, char[] password) throws Exception {
        byte[] salt = AES.generateSalt(AES.SALT_LENGTH);
        return encryptToHexa(keyLength, text, password, salt);
    }

    /**
     *
     * Encrypt a string into a an hexa format using the specified keyLength
     * value
     *
     * @param keyLength the keyLength must be 128, 192 or 256.
     * @param text the string to encrypt or Decrypt. if to decrypt: string must
     * be Hex encoded
     * @param password the password to use
     * @return the encrypted string in hexa format.
     *
     * @throws Exception
     */
    public String encryptToHexa(int keyLength, String text, char[] password, byte[] salt) throws Exception {

        if (text == null) {
            throw new NullPointerException("text is null!");
        }

        if (password == null) {
            throw new NullPointerException("password is null!");
        }

        ByteArrayInputStream in = new ByteArrayInputStream(text.getBytes("UTF-8"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        AES.encrypt(keyLength, password, in, out, salt);
        byte[] bytes = out.toByteArray();

        String hexa = new String(Hex.encodeHex(bytes));
        return hexa;
    }

    /**
     *
     * Decrypt an hexa string created/encrypted with encryptToHexa
     *
     * @param text the string to be decrypted in hexa format
     * @param password the password to use
     * @return the decrypted string in clear readable format
     *
     * @throws Exception
     */
    public String decryptFromHexa(String text, char[] password) throws Exception {

        byte[] bytesEncrypted = Hex.decodeHex(text.toCharArray());

        ByteArrayInputStream in = new ByteArrayInputStream(bytesEncrypted);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        AES.decrypt(password, in, out);
        String decryptedText = out.toString("UTF-8");
        return decryptedText;

    }

    /**
     * Encrypt a file with AES and 128 bits key length.
     *
     * @param fileIn the file to encrypt
     * @param fileOut the encrypted file after operation
     * @param password the password to use
     * @throws java.lang.Exception
     */
    public void encryptFile(File fileIn, File fileOut, char[] password)
            throws Exception {

        if (fileIn == null) {
            throw new IllegalArgumentException("fileIn is null!");
        }

        if (fileOut == null) {
            throw new IllegalArgumentException("fileOut is null!");
        }

        if (password == null) {
            throw new IllegalArgumentException("password is null!");
        }

        try (InputStream in = new BufferedInputStream(new FileInputStream(fileIn));
                OutputStream out = new BufferedOutputStream(new FileOutputStream(fileOut));) {

            int keyLength = 128;

            byte[] salt = AES.generateSalt(AES.SALT_LENGTH);

            AES.encrypt(keyLength, password, in, out, salt);
        } finally {
            //IOUtils.closeQuietly(in);
            //IOUtils.closeQuietly(out);
        }

    }

    /**
     * Decrypt a file ancryped with encryptFile method.
     *
     * @param fileIn the file to decrypt
     * @param fileOut the decrypted file after operation
     * @param password the password to use
     */
    public void decryptFile(File fileIn, File fileOut, char[] password)
            throws Exception {

        if (fileIn == null) {
            throw new IllegalArgumentException("fileIn is null!");
        }

        if (fileOut == null) {
            throw new IllegalArgumentException("fileOut is null!");
        }

        if (password == null) {
            throw new IllegalArgumentException("password is null!");
        }

        try (InputStream in = new BufferedInputStream(new FileInputStream(fileIn));
                OutputStream out = new BufferedOutputStream(new FileOutputStream(fileOut));) {

            AES.decrypt(password, in, out);
        } finally {
            //IOUtils.closeQuietly(in);
            //IOUtils.closeQuietly(out);
        }
    }

}
