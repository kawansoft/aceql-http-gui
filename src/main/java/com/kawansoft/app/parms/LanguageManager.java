
package com.kawansoft.app.parms;

import java.util.List;
import java.util.Locale;
import java.util.Vector;
import java.util.prefs.Preferences;




/**
 * Allows to define as static a language to use for the complete JVM life.
 * <br>
 * If the static stored language is null, i.e. not set, the language returned will
 * be the System Property "user.language"
 * 
 */
public class LanguageManager
{
    /** The Preferences Key for the Language in use with the App*/
    private static String LANGUAGE_KEY = "LANGUAGE_KEY";
    
    /** Language defined by the setLanguage() method */
    private static String LANGUAGE = null;
    
    /** Contains all implemented Locales */
    public static final Locale [] AVAILABLE_LOCALES = {
        Locale.ENGLISH,
        Locale.FRENCH,
    };
   
    /**
     * stati methods only.
     */
    protected LanguageManager()
    {      
    }
    
    /**
     * Return the language in use.
     * <br>
     * If language has  been never set 
     * ==> value will be DEFAULT_LANGUAGE (the System Property "user.language")
     * 
     * @return Returns the LANGUAGE.
     */
    public static String getLanguage()
    {
        
        if (MessagesManager.FORCE_ENGLISH_LOCALE) {
            return Locale.ENGLISH.getLanguage();
        }
        
        if (LANGUAGE == null)
        {           
            // if no Language in memory ==> Load from Preferences
            LanguageManager.loadLanguage();          
        }
        
        return LANGUAGE;
    }
    
    /**
     * @param language The LANGUAGE to set.
     */
    public static void setLanguage(String language)
    {
        LANGUAGE = language;
    }
    
    /**
     * Store the language in use as a Preference
     */
    public static void storeLanguage()
    {
        Preferences prefs = Preferences.userNodeForPackage(LanguageManager.class);
        prefs.put(LANGUAGE_KEY, LANGUAGE);       
    }
    
    /**
     * Load the language in use as a Preference
     * <br>
     * If no prefered language is found, use user.language.
     * if user.language is not available use en (English)
     */
    public static void loadLanguage()
    {
        Preferences prefs = Preferences.userNodeForPackage(LanguageManager.class);
        
        String defaultLanguage = System.getProperty("user.language");
        String language = prefs.get(LANGUAGE_KEY, defaultLanguage); 
        
        //System.out.println("Parms.MAX_RECIPIENTS : " + Parms.MAX_RECIPIENTS);
        //System.out.println("AVAILABLE_LOCALES  : " + AVAILABLE_LOCALES);
        
        List<String> availableLanguages = new Vector<String>();
        for (int i = 0; i < AVAILABLE_LOCALES.length; i++)
        {
            availableLanguages.add(AVAILABLE_LOCALES[i].getLanguage());
        }
        
        // if the found language is not available in cGeep ==> Use English!
        if (! availableLanguages.contains(language))
        {
            language = AVAILABLE_LOCALES[0].getLanguage();
        }
        
        // Set the language in memory
        setLanguage(language);
    }
    
    
}
