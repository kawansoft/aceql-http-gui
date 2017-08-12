/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kawansoft.app.util.crypto;

import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;

/**
 *
 * @author Nicolas de Pomereu
 */
public class CryptoUtil {

    public static boolean isAdmin() {
        String groups[] = (new com.sun.security.auth.module.NTSystem()).getGroupIDs();
        for (String group : groups) {
            if (group.equals("S-1-5-32-544")) {
                return true;
            }
        }
        return false;
    }
        
    /**
     * Returns the key length: 128 or 256 depending of installaton of
     * Unlimited Strength Java(TM) Cryptography Extension Policy Files
     * @return the key length: 128 or 256
     */
    public static int getMaximumKeyLength() {
        int keyLength = 128;
        try {
            if (Cipher.getMaxAllowedKeyLength("RC5") >= 256) {
                keyLength = 256;
            }
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return keyLength;
    }
    
}
