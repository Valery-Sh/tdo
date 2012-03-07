/*
 * ObjectFieldSupport.java
 *
 */

package tdo;

import java.lang.reflect.Field;

/**
 *
 * @author valery
 */
public class ObjectFieldSupport {
    
    private Field field;
    
    private int columnIndex;
//    private int cellIndex;
    private String columnName;
    private boolean readOnly;
    
    /** Creates a new instance of ObjectFieldSupport */
    public ObjectFieldSupport(Field field ) {
        this(field,null);
    }
    
    public ObjectFieldSupport(Field field, DataColumn dc ) {
        this.field = field;
        readOnly = false;
        
        if ( dc != null ) {
 //           columnIndex = dc.getIndex();
//            cellIndex   = dc.getCellIndex();
            columnName  = dc.getName();
            readOnly    = dc.isReadOnly();
        }
        
    }
    public Field getField() {
        return this.field;
    }
    
  /*  public int getCellIndex() {
        return this.cellIndex;
    }
   */ 
    public boolean isReadOnly() {
        return this.readOnly;
    }
}//class
