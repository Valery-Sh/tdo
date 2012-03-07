/*
 * ObjectPropertySupport.java
 *
 */

package tdo;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/**
 *
 * @author valery
 */
public class ObjectPropertySupport {
    
    private PropertyDescriptor desc;
    private Method readMethod;
    private Method writeMethod;
   // private int columnIndex;
    //private int cellIndex;
    //private String columnName;
    private boolean readOnly;
    
    
    /** Creates a new instance of ObjectPropertySupport 
     * @param prop 
     */
    public ObjectPropertySupport(PropertyDescriptor prop ) {
        this(prop,null);
    }
    public ObjectPropertySupport(PropertyDescriptor prop, DataColumn dc ) {
        desc = prop;
        readMethod = desc.getReadMethod();
        writeMethod = desc.getWriteMethod();
        readOnly = false;
        
        if ( dc != null ) {
//            columnIndex = dc.getIndex();
//            cellIndex   = dc.getCellIndex();
//            columnName  = dc.getName();
            readOnly    = dc.isReadOnly();
        }
         if ( writeMethod == null )
             readOnly = true;
    }
    
    public Method getReadMethod() {
        return this.readMethod;
    }
    public Method getWriteMethod() {
        return this.writeMethod;
    }
  /*  public int getCellIndex() {
        return this.cellIndex;
    }
   */ 
    public boolean isReadOnly() {
        return this.readOnly;
    }
    
    public PropertyDescriptor getPropertyDescriptor() {
        return this.desc;
    }
}
