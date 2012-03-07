/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tdo;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import tdo.impl.ValidateException;
import tdo.impl.Validator;
import static org.junit.Assert.*;

/**
 *
 * @author Valery
 */
public abstract class SimpleTableTestBase {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    public abstract Table createTable();
    public abstract Table createNoSchemaTable();

    /**
     * test os a method addRow()
     */
    @Test
    public void testAddRow0() {
        //=====================
        // Signiture addRow()
        //=====================
        SimpleTable dt = (SimpleTable)createTable();
        assertEquals(0, dt.getRowCount());
        assertEquals(-1, dt.getActiveRowIndex());

        dt.addRow();
        assertEquals(1, dt.getRowCount());
        assertEquals("New row with index 0 must become active", 0, dt.getActiveRowIndex());
        dt.addRow();
        assertEquals(2, dt.getRowCount());
        assertEquals("New row with index 0 must become active", 1, dt.getActiveRowIndex());
        //
        // test of DataOperationException
        //
        dt.setLoading(true);
        try {
            dt.addRow();
            fail("The attemp to add a row when table is loading !!!");
        } catch (DataOperationException ex) {
            assertEquals("Table cannot be in loading state when inserting new row", ex.getMessage());
        }
        dt.setLoading(false);
        //
        // test of method ValidateException
        //
        dt.setValidationEnabled(true);
        Validator v = dt.addValidator("1=2"); // not true forever !!!

        try {
            dt.addRow();
            fail("An ERROR must occur when validate row in INSERTING state!!!");
        } catch (ValidateException ex) {
        }
        dt.removeValidator(v);

        dt.setActiveRowIndex(0); // row 0 is in INSERTED state

        v = dt.addValidator("1=2"); // not true forever !!!

        DataRow r = dt.getRow(0);
        r.beginEdit();
        r.setValue("Tom", "firstName"); // now row 0 is in UPDATING state

        try {
            dt.addRow();
            fail("An ERROR must occur when validate row in UPDATING state!!!");
        } catch (ValidateException ex) {
        }
        dt.removeValidator(v);
        dt.addRow();
    }

    /**
     * test os a method addRow(DataRow)
     */
    @Test
    public void testAddRow1() {
        //=====================
        // Signiture addRow(DataRow)
        //=====================
        SimpleTable dt = (SimpleTable)createTable();
        DataRow r = dt.createRow();
        r.setValue("Tom", "firstName");
        dt.addRow(r);
        assertEquals(1, dt.getRowCount());
        //
        // test of the very first row is active
        //
        assertEquals(0, dt.getActiveRowIndex());

        r = dt.createRow();
        r.setValue("Bill", "firstName");
        int rowIndex = dt.addRow(r);
        assertEquals(2, dt.getRowCount());
        //
        // test if the active row did not change
        //
        assertEquals(0, dt.getActiveRowIndex());

        //
        // Try to add a row which is allready there
        //
        try {
            dt.addRow(dt.getRow(rowIndex));
            fail("The attemp to add a row from the table row collection !!!");
        } catch (IllegalArgumentException ex) {
            assertEquals("The row has allready been attached", ex.getMessage());
        }

        //
        // Try to add a row which belongs to another table
        //
        SimpleTable adt = (SimpleTable)createTable();
        DataRow ar = adt.addRow();
        try {
            dt.addRow(adt.getRow(0));
            fail("The attemp to add a row from another table!!!");
        } catch (IllegalArgumentException ex) {
            assertEquals("The  row to be added doesn't match this table", ex.getMessage());
        }

        //
        // for the method with a given signiture validation doesn't occur
        // automatically
        //
        int count = dt.getRowCount();
        dt.setValidationEnabled(true);
        Validator v = dt.addValidator("1=2"); // not true forever !!!

        r = dt.createRow();
        dt.addRow(r);
        assertEquals(count + 1, dt.getRowCount());


    }

    /**
     * test os a method addRow(Object[])
     */
    @Test
    public void testAddRow2() {
        //=====================
        // Signiture addRow(Object[])
        //=====================
        SimpleTable dt = (SimpleTable)createTable();

        dt.addRow(new Object[]{"Bill", "Gates"});
        assertEquals(1, dt.getRowCount());
        //
        // test if the very first row is active
        //
        assertEquals(0, dt.getActiveRowIndex());
        assertEquals("Bill", dt.getRow(0).getValue("firstName"));
        assertEquals("Gates", dt.getRow(0).getValue("lastName"));

        dt.addRow(new Object[]{"Tom", "Cat"});
        assertEquals(2, dt.getRowCount());
        
        dt = (SimpleTable)createNoSchemaTable();

        dt.addRow(new Object[]{"Bill", "Gates"});
        assertEquals(1, dt.getRowCount());
        //
        // test if the very first row is active
        //
        assertEquals(0, dt.getActiveRowIndex());
        assertEquals("Bill", dt.getRow(0).getValue(0));
        assertEquals("Gates", dt.getRow(0).getValue(1));

        dt.addRow(new Object[]{"Tom", "Cat"});
        assertEquals(2, dt.getRowCount());
        
        //
        // test if the active row did not change
        //
        assertEquals(0, dt.getActiveRowIndex());
        //
        // for the method with a given signiture validation doesn't occur
        // automatically
        //
        int count = dt.getRowCount();
        dt.setValidationEnabled(true);
        Validator v = dt.addValidator("1=2"); // not true forever !!!

        dt.addRow(new Object[]{"Tom", "Jerry"});
        assertEquals(count + 1, dt.getRowCount());
        //
        // IllegalArgumentException when object array is null
        //
        Object[] oa = null;
        try {
            dt.addRow(oa);
            fail("Object array is null !!!");
        } catch (IllegalArgumentException ex) {
            assertEquals("addRow(Object[]) 'values' argument cannot be null", ex.getMessage());
        } 
        
        dt = (SimpleTable)createTable();
        //
        // test of type mismatching
        //
        oa = new Object[] { "Pitter","Norton","WWW" };
        try {
            dt.addRow(oa);
            fail("Not integer value assigns to the column of Integer type !!!");
        } catch (Exception ex) {
        } 
        
    }
    
    /**
     * test os a method addRow(Object[],int,int)
     */
    @Test
    public void testAddRow3() {
        //=====================
        // Signiture addRow(Object[])
        //=====================
        SimpleTable dt = (SimpleTable)createNoSchemaTable();
        
        dt.addRow(new Object[]{"1","2","Bill", "Gates",52}, 2,3);
        assertEquals(1, dt.getRowCount());
        //
        // test if the very first row is active
        //
        assertEquals(0, dt.getActiveRowIndex());
        assertEquals("Bill", dt.getRow(0).getValue(0));
        assertEquals("Gates", dt.getRow(0).getValue(1));
        assertEquals(52, dt.getRow(0).getValue(2));

        dt.addRow(new Object[]{"Tom", "Cat",5},0,3);
        assertEquals(2, dt.getRowCount());
        
        dt = (SimpleTable)createTable();
        
        dt.addRow(new Object[]{"1","2","Bill", "Gates",52}, 2,3);
        assertEquals(1, dt.getRowCount());
        assertEquals(3, dt.getColumns().getCount());
        //
        // test if the very first row is active
        //
        assertEquals(0, dt.getActiveRowIndex());
        assertEquals("Bill", dt.getRow(0).getValue(0));
        assertEquals("Gates", dt.getRow(0).getValue(1));
        assertEquals(52, dt.getRow(0).getValue(2));

        dt.addRow(new Object[]{"Tom", "Cat",5},0,3);
        assertEquals(2, dt.getRowCount());
        
        //
        // test if the active row did not change
        //
        assertEquals(0, dt.getActiveRowIndex());
        //
        // for the method with a given signiture validation doesn't occur
        // automatically
        //
        int count = dt.getRowCount();
        dt.setValidationEnabled(true);
        Validator v = dt.addValidator("1=2"); // not true forever !!!

        dt.addRow(new Object[]{"Tom", "Jerry",6},0,3);
        assertEquals(count + 1, dt.getRowCount());
        //
        // IllegalArgumentException when object array is null
        //
        dt.setValidationEnabled(false);        
        Object[] oa = null;
        try {
            dt.addRow(oa,0,1);
            fail("Object array is null !!!");
        } catch (IllegalArgumentException ex) {
            assertEquals("addRow(Object[]) 'values' argument cannot be null", ex.getMessage());
        } 
        //
        // IndexOutOfBoundsException when the argument 'count' is less than 0
        //
        oa = new Object[] { "Pitter","Norton",new Integer(50),new Integer(100),0,4 };
        try {
            dt.addRow(oa,0,-2);
            fail("startIndex and  count must be positive and startIndex < values.length !!!");
        } catch (IndexOutOfBoundsException ex) {
            assertTrue(ex.getMessage().startsWith("'startIndex' and 'count' must be positive and match the 'values' length ["));
        } 

        //
        // IndexOutOfBoundsException when the argument 'startIndex' is less than 0
        //
        oa = new Object[] { "Pitter","Norton",new Integer(50),new Integer(100),0,4 };
        try {
            dt.addRow(oa,-1,2);
            fail("startIndex and  count must be positive and startIndex < values.length !!!");
        } catch (IndexOutOfBoundsException ex) {
            assertTrue(ex.getMessage().startsWith("'startIndex' and 'count' must be positive and match the 'values' length ["));
        } 
        
    }    
    /**
     * test os a method addRow(String[],Object[])
     */
    @Test
    public void testAddRow4() {
        //=====================
        // Signiture addRow(String[],Object[])
        //=====================
/*        SimpleTable dt = (SimpleTable)createNoSchemaTable();

        dt.addRow(new String[]{"firstName","lastName"},new Object[]{"Bill", "Gates"});
        assertEquals(1, dt.getRowCount());
        assertEquals(2, dt.getColumns().getCount());
        
        //
        // test if the very first row is active
        //
        assertEquals(0, dt.getActiveRowIndex());
        assertEquals("Bill", dt.getRow(0).getValue("firstName"));
        assertEquals("Gates", dt.getRow(0).getValue("lastName"));

        dt.addRow(new String[]{"firstName","lastName"},new Object[]{"Tom", "Cat"});
        assertEquals(2, dt.getRowCount());
        assertEquals("Tom", dt.getRow(1).getValue("firstName"));
        assertEquals("Cat", dt.getRow(1).getValue("lastName"));
        //
        // test of 
        //( names != null && values != null && names.length <= values.length && names.length > 0
        // 
        dt = (SimpleTable)createNoSchemaTable();
        String[] names = null;
        try {
            dt.addRow(names,new Object[]{"Bill","Gates"});
            fail("Illegal arguments: names must not be null");
        } catch (IllegalArgumentException ex) {
            assertEquals("Illegal parameter valuue(s). " +
                   " Must be " +
                   "( names != null && values != null && names.length <= values.length && names.length !=0", ex.getMessage());
        }
        dt = (SimpleTable)createNoSchemaTable();
        Object[] values = null;
        names = new String[] { "firstName","lastName" };        
        try {
            dt.addRow(names,values);
            fail("Illegal arguments: values must not be null");
        } catch (IllegalArgumentException ex) {
            assertEquals("Illegal parameter valuue(s). " +
                   " Must be " +
                   "( names != null && values != null && names.length <= values.length && names.length !=0", ex.getMessage());
        }
        dt = createNoSchemaTable();
        values = new Object[]{"Bill"};
        names = new String[] { "firstName","lastName" };        
        try {
            dt.addRow(names,values);
            fail("Illegal arguments: names length must be less or equal to values length");
        } catch (IllegalArgumentException ex) {
            assertEquals("Illegal parameter valuue(s). " +
                   " Must be " +
                   "( names != null && values != null && names.length <= values.length && names.length !=0", ex.getMessage());
        }
  */
    }

    /**
     * test os a method insertRow(int)
     */
    @Test
    public void testInsertRow0() {
        //=====================
        // Signiture insertRow(int)
        //=====================
        SimpleTable dt = (SimpleTable)createTable();
        assertEquals(0, dt.getRowCount());
        assertEquals(-1, dt.getActiveRowIndex());

        dt.insertRow(0);
        assertEquals(1, dt.getRowCount());
        assertEquals("New row with index 0 must become active", 0, dt.getActiveRowIndex());
        dt.insertRow(1);
        assertEquals(2, dt.getRowCount());
        assertEquals("New row with index 1 must become active", 1, dt.getActiveRowIndex());
        dt.insertRow(1);
        assertEquals(3, dt.getRowCount());
        assertEquals("New row with index 1 must become active", 1, dt.getActiveRowIndex());
        
        //
        // test of DataOperationException
        //
        dt.setLoading(true);
        try {
            dt.insertRow(1);
            fail("The attemp to add a row when table is loading !!!");
        } catch (DataOperationException ex) {
            assertEquals("Table cannot be in loading state when inserting new row", ex.getMessage());
        }
        dt.setLoading(false);
        //
        // test of method ValidateException
        //
        dt.setValidationEnabled(true);
        Validator v = dt.addValidator("1=2"); // not true forever !!!

        try {
            dt.insertRow(1);
            fail("An ERROR must occur when validate row in INSERTING state!!!");
        } catch (ValidateException ex) {
        }
        dt.removeValidator(v);

    }

    /**
     * test os a method insertRow(int,DataRow)
     */
    @Test
    public void testInsertRow1() {
        //=====================
        // Signiture insertRow(int)
        //=====================
        SimpleTable dt = (SimpleTable)createTable();
        //DataRow r0 = dt.addRow(new String[]{"firstName","lastName"},new Object[]{"Bill", "Gates"});
        DataRow r0 = dt.addRow(new Object[]{"Bill", "Gates"});
        DataRow r = dt.createRowCopy(r0);
        assertEquals("Active row doesn't change", 0, dt.getActiveRowIndex());
        
        dt.insertRow(1,r);
        assertEquals(2, dt.getRowCount());
        assertEquals("Active row doesn't change", 0, dt.getActiveRowIndex());
        
        //
        // test of IndexOutOfBoundsException
        //
        r = dt.createRowCopy(r);
        
        try {
            dt.insertRow(10,r);
            fail("row index must be less or equal to the table's rowCount !!!");
        } catch (IndexOutOfBoundsException ex) {
            assertTrue(ex.getMessage().startsWith("Row Index "));
        }
    }
    
    @Test
    public void TestCreateColumn() {
        SimpleTable dt = (SimpleTable)createTable();
    }
    
}//class SimpleTableTestBase
