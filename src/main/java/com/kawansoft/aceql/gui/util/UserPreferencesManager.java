package com.kawansoft.aceql.gui.util;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;


import com.kawansoft.app.util.preference.AppPreferencesManager;
import com.kawansoft.app.util.crypto.PbeAes;

/**
 * Peferences Manager for KawanDoc Https Upload
 * <br>
 * Stores the User Preferences
 *
 * @author Nicolas de Pomereu
 */
public class UserPreferencesManager implements AppPreferencesManager {

    /**
     * Set preference
     */
    @Override
    public void setPreference(String prefName, String prefValue) {;
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        prefs.put(prefName, prefValue);
    }

    /**
     * Get a preference
     */
    @Override
    public String getPreference(String prefName) {
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        return prefs.get(prefName, null);
    }

    /**
     * Set preference
     * @param keyLength the value of keylength
     * @param prefName
     * @param prefValue the value of prefValue
     */
   @Override
    public void setPreferenceEncrypt(int keyLength, String prefName, String prefValue) {
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());

        String prefValueHex = null;
        if (prefValue != null) {
            try {
                PbeAes pbe = new PbeAes();
                prefValueHex = pbe.encryptToHexa(keyLength, prefValue, ParmsCrypto.ENCRYPTION_PASSWORD.toCharArray());
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new IllegalArgumentException("Impossible to encrypt preference " + prefName + ":" + ex.toString());
            }
        }

        prefs.put(prefName, prefValueHex);
    }

    /**
     * Get a preference
     * @param prefName
     * @return 
     */
    @Override
    public String getPreferenceDecrypt(String prefName) {
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());

        String prefValue = prefs.get(prefName, null);

        if (prefValue != null) {
            try {
                PbeAes pbe = new PbeAes();
                prefValue = pbe.decryptFromHexa(prefValue, ParmsCrypto.ENCRYPTION_PASSWORD.toCharArray());
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new IllegalArgumentException("Impossible to decrypt preference " + prefName + ":" + ex.toString());
            }
        }

        return prefValue;
    }

    /**
     * Remove a preference
     */
    @Override
    public void removePreference(String prefName) {
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        prefs.remove(prefName);
    }

    /**
     * Get a boolean preference
     *
     * @param prefName the preference name
     * @return the preference value
     */
    @Override
    public boolean getBooleanPreference(String prefName) {
        String preferenceStr = getPreference(prefName);

        boolean preference = false;

        try {
            preference = Boolean.parseBoolean(preferenceStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return preference;
    }

    /**
     * @param prefName the preference name
     * @param prefValue the preference value
     */
    @Override
    public void setPreference(String prefName, boolean prefValue) {
        setPreference(prefName, Boolean.toString(prefValue));
    }

    /**
     * Get a integer preference
     */
    @Override
    public int getIntegerPreference(String prefName) {
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        String value = prefs.get(prefName, "0");
        return Integer.valueOf(value);
    }

    /**
     * Set preference
     */
    @Override
    public void setPreference(String prefName, int prefValue) {
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());
        prefs.put(prefName, Integer.toString(prefValue));
    }

 
    /**
     * Reset all frames
     */
    @Override
    public void resetAll() {
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());

        try {
            prefs.clear();
        } catch (BackingStoreException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
            throws Exception {
        new UserPreferencesManager().resetAll();
    }

}

