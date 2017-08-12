//UserPreferencesManager.java
//Copyright (c) SafeLogic, 2000 - 2005
//
//Last Updates: 
// 22/06/06 16:25 NDP - Creation
// 26/06/06 12:20 NDP - Add getUserHome();
package com.kawansoft.app.util.preference;

import com.kawansoft.app.util.crypto.PbeAes;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;


/**
 * Default Preferences manager. Will be common to all applications.
 * <br>
 * Each app shoud uses it's own version.
 * Stores the User Preferences
 *
 * @author Nicolas de Pomereu
 */
public class DefaultAppPreferencesManager implements AppPreferencesManager {


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
     */
     //@Override
    public void setPreferenceEncrypt(int keyLength, String prefName, String prefValue) {
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());

        String prefValueHex = null;
        if (prefValue != null) {
            try {
                PbeAes pbe = new PbeAes();
                prefValueHex = pbe.encryptToHexa(keyLength, prefValue, DefaultParmsCrypto.ENCRYPTION_PASSWORD.toCharArray());
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new IllegalArgumentException("Impossible to encrypt preference " + prefName + ":" + ex.toString());
            }
        }

        prefs.put(prefName, prefValueHex);
    }

    /**
     * Get a preference
     */
    //@Override
    public String getPreferenceDecrypt(String prefName) {
        Preferences prefs = Preferences.userNodeForPackage(this.getClass());

        String prefValue = prefs.get(prefName, null);

        if (prefValue != null) {
            try {
                PbeAes pbe = new PbeAes();
                prefValue = pbe.decryptFromHexa(prefValue, DefaultParmsCrypto.ENCRYPTION_PASSWORD.toCharArray());
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
        String value = prefs.get(prefName, "1");
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
        new DefaultAppPreferencesManager().resetAll();
    }

}

