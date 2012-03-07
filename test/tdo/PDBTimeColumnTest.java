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
public class PDBTimeColumnTest extends DataColumnTestBase {

    @Override
    protected DataColumn createDataColumn() {
        DataColumn dc = new DataColumn.PDBTimeColumn();
        return dc;
    }

    @Override
    public void testGetType() {
        System.out.println("getType");
        DataColumn instance = createDataColumn();
        Class result = instance.getType();
        assertEquals(java.sql.Time.class, result);
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

        java.sql.Time defaultValue = (java.sql.Time) instance.getDefaultValue();
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
        anotherObj = new java.sql.Time(2);
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
        obj = new java.sql.Time(2);
        result = instance.compareObjects(obj, anotherObj, nullMin);
        assertEquals(expResult, result);

        // first < second 
        expResult = -1;
        obj = new java.sql.Time(1);
        result = instance.compareObjects(obj, anotherObj, nullMin);
        assertEquals(expResult, result);

        // first > second 
        expResult = 1;
        obj = new java.sql.Time(3);
        anotherObj = new java.sql.Time(1);
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
        Object expResult = new java.sql.Time(0);;
        Object result = instance.blankValueInstance();
        assertEquals(expResult, result);

        Object obj = new java.sql.Time(1000);
        instance = createDataColumn();
        instance.setDefaultValue(obj);
        expResult = new java.sql.Time(1000);
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
        
        obj = new java.sql.Time(0);
        instance = createDataColumn();
        result = instance.toColumnType(obj);
        assertNotNull(result);
        //assertEquals(instance.getType(),result.getClass());
        assertEquals(obj,result);
        
        
    }
    
}//class PDBTimeColumnTest
