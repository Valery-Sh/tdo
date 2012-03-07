/*
 */

package tdo;

import java.io.FileInputStream;
import java.util.Properties;

/**
 *
 * @author valery
 */
public abstract class DataSet {
    private static Properties properties = null;
    public static Properties getProperties() {
        if ( properties == null ) {
            properties = new Properties();
            try {
                properties.load(new FileInputStream("tdo-udf.properties"));
            } catch(Exception e) {
               System.out.println("DataSet ERROR. File 'tdo-udf.properties' doesn't exist"); 
            }    
            
        }
        return properties;
    }
    public static String getFunctionClass(String fname) {
        return getProperties().getProperty(fname);
    }


}//class DataSet
