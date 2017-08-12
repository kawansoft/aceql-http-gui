package com.kawansoft.app.util.crypto;

import java.lang.reflect.Field;
import java.security.Permission;
import java.security.PermissionCollection;
import java.util.Map;

import org.apache.commons.lang3.SystemUtils;

/**
 * Class to install full encryption policy dynamically.
 *
 * @author Nicolas de Pomereu
 *
 */
public class PolicyInstaller {


    /**
     * Constructor
     */
    protected PolicyInstaller() {

    }

 
    private static boolean cryptographyRestrictionsApplied = false;
    
    public static void setCryptographyRestrictions() {
        
        if (cryptographyRestrictionsApplied) {
            return;
        }
        
        cryptographyRestrictionsApplied = true;
        
        if (!isRestrictedCryptography()) {
            //logger.fine("Cryptography restrictions removal not needed");
            return;
        }
        
        // Does not work in java 6
        if (SystemUtils.IS_JAVA_1_6) {
            return;
        }
                
        try {
            /*
             * Do the following, but with reflection to bypass access checks:
             *
             * JceSecurity.isRestricted = false;
             * JceSecurity.defaultPolicy.perms.clear();
             * JceSecurity.defaultPolicy.add(CryptoAllPermission.INSTANCE);
             */
            final Class<?> jceSecurity = Class.forName("javax.crypto.JceSecurity");
            final Class<?> cryptoPermissions = Class.forName("javax.crypto.CryptoPermissions");
            final Class<?> cryptoAllPermission = Class.forName("javax.crypto.CryptoAllPermission");

            final Field isRestrictedField = jceSecurity.getDeclaredField("isRestricted");
            isRestrictedField.setAccessible(true);
            isRestrictedField.set(null, false);

            final Field defaultPolicyField = jceSecurity.getDeclaredField("defaultPolicy");
            defaultPolicyField.setAccessible(true);
            final PermissionCollection defaultPolicy = (PermissionCollection) defaultPolicyField.get(null);

            final Field perms = cryptoPermissions.getDeclaredField("perms");
            perms.setAccessible(true);
            ((Map<?, ?>) perms.get(defaultPolicy)).clear();

            final Field instance = cryptoAllPermission.getDeclaredField("INSTANCE");
            instance.setAccessible(true);
            defaultPolicy.add((Permission) instance.get(null));

           // logger.fine("Successfully removed cryptography restrictions");
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isRestrictedCryptography() {
        // This simply matches the Oracle JRE, but not OpenJDK.
        return "Java(TM) SE Runtime Environment".equals(System.getProperty("java.runtime.name"));
    }

}
