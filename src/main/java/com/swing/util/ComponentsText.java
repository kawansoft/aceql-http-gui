/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.swing.util;

import com.kawansoft.app.parms.LanguageManager;
import com.kawansoft.app.parms.MessagesManager;
import com.kawansoft.app.parms.Parms;
import com.kawansoft.app.parms.util.ParmsUtil;
import com.kawansoft.app.util.AppendStore;
import java.awt.Window;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import javax.swing.JOptionPane;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Allows to write and load Swing Components text/title from resources file.
 * See technique used in Awt1.
 */
public class ComponentsText {
    
    public static boolean DEBUG = false;

    private static File WRITE_PROPERTIES_FILE = new File(ParmsUtil.getBaseDir()+ File.separator + "Swing_messages_fr.properties");

    /** Resource Bundle instance */
    private static ResourceBundle RESOURCE_BUNDLE = null;
    
    public static String[] COMPONENTS
            = {"JButton", "JCheckBox", "JFormattedTextField", "JLabel", "JRadioButton", "JToggleButton", "JMenu", "JMenuItem", "JCheckBoxMenuItem", "JRadioButtonMenuItem", "JXTitledSeparator", "JTitledBorder"};


    /**
     * Loads all properties names/values from a properie file and put it in a Set.
     * Methd mus be used when creatin the write file
     * @return the set containing names/values
     * @throws IOException 
     */
    private static Set<String> getWritePropertiesName() throws IOException {

        Set<String> propertiesName = new HashSet<>();

        Properties prop = new Properties();

        if (WRITE_PROPERTIES_FILE.exists()) {
            prop.load(new FileReader(WRITE_PROPERTIES_FILE));
        }

        Enumeration keys = prop.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            propertiesName.add(key);
        }

        return propertiesName;

    }
    
    private static void loadResourcesBundle() {
        Locale locale = new Locale(LanguageManager.getLanguage());

        // Messages are contained in:
        // com.kawansoft.app.parms.helpfiles.Messages_fr.properties
        // com.kawansoft.app.parms.helpfiles.Messages_en.properties
        // Etc.
        String baseName = MessagesManager.MESSAGE_FILES_PACKAGE + ".Swing_messages";

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
     * Calls the setText() of the Swing components using text defined in a properties file.
     * Do also the setToolTiptext() if necessary
     * @param window JFrame or JDialog containing component
     */
    public static void setComponentsTextFromPropertiesFile(Window window) throws IOException {
        
        // Nothing for French: all is done directly on Swing interface via NetBeans
        if(LanguageManager.getLanguage().equals(Locale.FRENCH.getLanguage())) {
            return;
        }
        
        if (RESOURCE_BUNDLE == null) {
            loadResourcesBundle();
        }
       
        Set<String> typesSet = new HashSet<String>(Arrays.asList(COMPONENTS));
                
        // loop through all of the class fields on that form
        for (Field field : window.getClass().getDeclaredFields()) {

            try {
                // let us look at private fields, please
                field.setAccessible(true);                
                String theType = null;
                if (field.getGenericType().toString().contains(".")) {
                    theType = StringUtils.substringAfterLast(field.getGenericType().toString(), ".");
                } else {
                    continue;
                }

                debug("theType: " + theType + ":");
                if (!typesSet.contains(theType)) {
                    continue;
                }

                String thePropertyName = window.getClass().getSimpleName() + "." + field.getName();
                
                String text = null;
                
                boolean propertyExists = false;
                try {
                    text = RESOURCE_BUNDLE.getString(thePropertyName);  
                    propertyExists = true;
                } catch (MissingResourceException e) {
                    // Propert does not exists
                    
                }
                
                if (propertyExists) {
                    //JButton jButton = (JButton) field.get(window);
                    Object object = field.get(window);
                    Class clazz = object.getClass();

                    Method m = null;

                    if (theType.equals("JXTitledSeparator") || theType.equals("JTitledBorder")) {
                        m = clazz.getMethod("setTitle", String.class);
                    } else {
                        m = clazz.getMethod("setText", String.class);
                    }

                    m.invoke(object, text);
                }

                propertyExists = false;
                String thePropertyNameTooltipText =  thePropertyName + ".toolTiptext";
                try {
                    text = RESOURCE_BUNDLE.getString(thePropertyNameTooltipText);  
                    propertyExists = true;
                } catch (MissingResourceException e) {
                    // Propert does not exists
                    
                }
                
                if (propertyExists) {
                    
                    Object object = field.get(window);
                    Class clazz = object.getClass();

                    Method m = clazz.getMethod("setToolTipText", String.class);
                    m.invoke(object, text);
                     
                }


            } catch (Exception e) {
               // ignore but print exceptions
                e.printStackTrace();

            }

        }
    }
        
        
    
    /**
     * attempts to retrieve a component from a JFrame or JDialog using the name
     * of the private variable that NetBeans (or other IDE) created to refer to
     * it in code.
     *
     * @param window JFrame or JDialog containing component
     */
    @SuppressWarnings("unchecked")
    public static void writeComponentsInPropertiesFile(Window window) throws IOException {

        if (!ComponentsTextOptions.DO_WRITE_COMPONENTS_IN_PROPERTIES_FILE) {
            return;
        }
        
        Set<String> typesSet = new HashSet<String>(Arrays.asList(COMPONENTS));
        Set<String> propertiesName = getWritePropertiesName();

        // loop through all of the class fields on that form
        for (Field field : window.getClass().getDeclaredFields()) {

            try {
                // let us look at private fields, please
                field.setAccessible(true);

//                // compare the variable name to the name passed in
//                if (name.equals(field.getName())) {
//
//                    // get a potential match (assuming correct <T>ype)
//                    final Object potentialMatch = field.get(window);
//
//                    // cast and return the component
//                    return (T) potentialMatch;
//                }

                
                String theType = null;
                if (field.getGenericType().toString().contains(".")) {
                    theType = StringUtils.substringAfterLast(field.getGenericType().toString(), ".");
                } else {
                    continue;
                }

                debug("theType: " + theType + ":");
                if (!typesSet.contains(theType)) {
                    continue;
                }

                //JButton jButton = (JButton) field.get(window);
                Object object = field.get(window);
                Class clazz = object.getClass();

                Method m = null;

                if (theType.equals("JXTitledSeparator") || theType.equals("JTitledBorder")) {
                    m = clazz.getMethod("getTitle");
                } else {
                    m = clazz.getMethod("getText");
                }

                String text = (String) m.invoke(object);

                m = clazz.getMethod("getToolTipText");
                String toolTipText = (String) m.invoke(object);

                /*
                    System.out.println();
                    System.out.println("Class: " + window.getClass().getSimpleName());
                    System.out.println("Name : " + field.getName());
                    System.out.println("Type : " + field.getGenericType());
                    System.out.println("text : " + jButton.getText());
                 */
                if (text == null || text.isEmpty() && (toolTipText == null || toolTipText.isEmpty())) {
                    continue;
                }

                AppendStore appendStore = null;
                try {

                    String thePropertyName = window.getClass().getSimpleName() + "." + field.getName();

                    if (propertiesName.contains(thePropertyName)) {
                        continue;
                    }
                    
                    appendStore = new AppendStore(WRITE_PROPERTIES_FILE);
                    
                    if (text != null || ! text.isEmpty()) {
                        String iniLine = window.getClass().getSimpleName() + "." + field.getName() + " = " + text;

                        debug(iniLine);
                        appendStore.append(iniLine);
                    }

                    if (toolTipText != null && !toolTipText.isEmpty()) {
                        String iniToolTipLine = window.getClass().getSimpleName() + "." + field.getName() + ".toolTiptext = " + toolTipText;

                        debug(iniToolTipLine);
                        appendStore.append(iniToolTipLine);
                    }
                } finally {
                    IOUtils.closeQuietly(appendStore);
                }

            } catch (Exception e) {
               // ignore but print exceptions
                e.printStackTrace();

            }

        }
    }

    /**
     * debug tool
     */
    private static void debug(String s) {
        if (DEBUG) {
            System.out.println(s);
            // System.out.println(this.getClass().getName() + " " + new Date() +
            // " " + s);
        }
    }

}
