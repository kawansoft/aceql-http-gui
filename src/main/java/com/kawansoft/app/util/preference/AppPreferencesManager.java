package com.kawansoft.app.util.preference;

/**
 * Allows to create a generic preferences store for all apps.
 * 
 * @author Nicolas de Pomereu
 */
public interface AppPreferencesManager {
    
    // Login info
    public static final String REMEMBER_ME = "REMEMBER_ME";
    public static final String LOGIN = "LOGIN";
    public static final String PASSWORD = "PASSWORD";
    
    
    // Proxy settings
    public static String PROXY_TYPE          = "PROXY_TYPE";
    public static int PROXY_TYPE_BROWSER_DEF = 0; // default value
    public static int PROXY_TYPE_USER_DEF    = 1;
    public static int PROXY_TYPE_DIRECT      = 2;
    
    public static final String PROXY_PORT = "PROXY_PORT";
    public static final String PROXY_ADDRESS = "PROXY_ADDRESS";
    
    
    // Authentication proxy info
    public static final String PROXY_AUTH_REMEMBER_INFO = "PROXY_AUTH_REMEMBER_INFO";
    public static final String PROXY_AUTH_USERNAME = "PROXY_AUTH_USERNAME";
    public static final String PROXY_AUTH_PASSWORD = "PROXY_AUTH_PASSWORD";

    
    public void setPreference(String prefName, String prefValue);
    public String getPreference(String prefName);
    public void removePreference(String prefName);

    public void setPreference(String prefName, boolean prefValue);
    public boolean getBooleanPreference(String prefName);

    public void setPreference(String prefName, int prefValue);
    public int getIntegerPreference(String prefName);
	
    public void setPreferenceEncrypt(int keylength, String prefName, String prefValue);
    public String getPreferenceDecrypt(String prefName);

    public void resetAll();
}
