/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tdo;

import tdo.DataColumnTestBase;
import java.sql.ResultSetMetaData;

/**
 *
 * @author valery
 */
public class PDBStringColumnTest extends DataColumnTestBase {

    @Override
    protected DataColumn createDataColumn() {
        DataColumn dc = new DataColumn.PDBStringColumn();
        dc.setType(String.class);
        dc.setDefaultValue("");
        return dc;
    }

    @Override
    public void testGetType() {
        System.out.println("getType");
        DataColumn instance = createDataColumn();
        Class result = instance.getType();
        assertEquals(String.class,result);
    }
    
    /**
     * Test of createBlankObject method, of class DataColumn.
     */
    @Override
    public void testCreateBlankObject() {
        System.out.println("createBlankObject");
        DataColumn instance = createDataColumn();
        
        instance.setNullable(ResultSetMetaData.columnNullable);
        Object result = instance.createBlankObject();
        assertNull(result);
        
        instance = createDataColumn();
        instance.setNullable(ResultSetMetaData.columnNoNulls);
        result = instance.createBlankObject();
        assertNotNull(result);  
        
        String defaultValue = (String)instance.getDefaultValue();
        assertEquals(defaultValue,result);        
        
    }
    

    /**
     * Test of compareObjects method, of class DataColumn.
     */
    @Override
    public void testCompareObjects_3args() {
        System.out.println("compareObjects");
        // Both param are null
        Object obj = null;
        Object anotherObj = null;
        boolean nullMin = true;
        DataColumn instance = createDataColumn();
        int expResult = 0;
        int result = instance.compareObjects(obj, anotherObj, nullMin);
        assertEquals(expResult, result);
        
        // first param == null and second != null. null considers as minimum
        obj = null;
        anotherObj = "value2";
        instance = createDataColumn();
        expResult = -1;
        result = instance.compareObjects(obj, anotherObj, nullMin);
        assertEquals(expResult, result);

        // first == null and second != null. null considers as maximum
        nullMin = false;
        expResult = 1;
        
        result = instance.compareObjects(obj, anotherObj, nullMin);
        assertEquals(expResult, result);

        // first == second 
        expResult = 0;
        obj = "value2";
        result = instance.compareObjects(obj, anotherObj, nullMin);
        assertEquals(expResult, result);

        // first < second 
        expResult = -1;
        obj = "value1";
        result = instance.compareObjects(obj, anotherObj, nullMin);
        assertEquals(expResult, result);

        // first > second 
        expResult = 1;
        obj = "value3";
        result = instance.compareObjects(obj, anotherObj, nullMin);
        assertEquals(expResult, result);
        
    }
    
    /**
     * Test of cloneFrom method, of class DataColumn.
     */
    @Override
    public void testDefaultValueInstance() {
        System.out.println("defaultValueInstance");
        Object obj = null;
        DataColumn instance = createDataColumn();
        Object expResult = "";
        Object result = instance.blankValueInstance();
        assertEquals(expResult, result);
        
        obj = "testDefaultValueInstance";
        instance = createDataColumn();
        instance.setDefaultValue(obj);
        expResult = "testDefaultValueInstance";
        result = instance.blankValueInstance();
        assertEquals(expResult, result);
        
    }

}//class PDBStringColumnTest
