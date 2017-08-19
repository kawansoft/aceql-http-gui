
package com.kawansoft.app.util.classpath;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author Nicolas de Pomereu
 */
public class ClasspatUtil {

    public static List<String> getOrderedClasspath() {
        String classpath = System.getProperty("java.class.path");
        
        String [] classpathArray = classpath.split(System.getProperty("path.separator"));
                
        if (classpathArray == null) {
            return null;
        }         
        
        List<String> classpathList = Arrays.asList(classpathArray);
        Collections.sort(classpathList);
        return classpathList;
        
    }
    
}
