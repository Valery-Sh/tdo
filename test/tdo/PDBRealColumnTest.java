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
public class PDBRealColumnTest extends DataColumnTestBase {

    @Override
    protected DataColumn createDataColumn() {
        DataColumn dc = new DataColumn.PDBRealColumn();
        return dc;
    }

    @Override
    public void testGetType() {
        System.out.println("getType");
        DataColumn instance = createDataColumn();
        Class result = instance.getType();
        assertEquals(Float.class, result);
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

        Float defaultValue = (Float) instance.getDefaultValue();
        assertEquals(defaultValue, result);

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

        // first  == null and second != null. null considers as minimum
        obj = null;
        anotherObj = new Float("50");
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
        obj = new Float("50");
        result = instance.compareObjects(obj, anotherObj, nullMin);
        assertEquals(expResult, result);

        // first < second 
        expResult = -1;
        obj = new Float("49");
        result = instance.compareObjects(obj, anotherObj, nullMin);
        assertEquals(expResult, result);

        // first > second 
        expResult = 1;
        obj = new Float("50");
        anotherObj = new Float("49");        
        result = instance.compareObjects(obj, anotherObj, nullMin);
        assertEquals(expResult, result);

    }

    /**
     * Test of cloneFrom method, of class DataColumn.
     */
    @Override
    public void testDefaultValueInstance() {
        System.out.println("defaultValueInstance");
        
        DataColumn instance = createDataColumn();
        Object expResult = new Float(0);
        Object result = instance.blankValueInstance();
        assertEquals(expResult, result);

        Object obj = new Float("50");
        instance = createDataColumn();
        instance.setDefaultValue(obj);
        expResult = new Float("50");
        result = instance.blankValueInstance();
        assertEquals(expResult, result);
        
    }
    
    /**
     * Test of toColumnType method, of class DataColumn.
     */
    @Override
    public void testToColumnType() {
        System.out.println("toColumnType");
        Object obj = null;
        DataColumn instance = createDataColumn();
        Object expResult = null;
        Object result = instance.toColumnType(obj);
        assertEquals(expResult, result);
        
        obj = new Float("50");
        instance = createDataColumn();
        result = instance.toColumnType(obj);
        assertNotNull(result);
        assertEquals(Float.class, result.getClass());
        int b = ((Float)obj).compareTo((Float)result);
        assertEquals(0,b);
        
        
    }
    
}//class PDBRealColumnTest
