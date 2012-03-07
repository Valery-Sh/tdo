/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tdo;

import java.sql.ResultSetMetaData;
import junit.framework.TestCase;

/**
 *
 * @author valery
 */
public abstract class DataColumnTestBase extends TestCase {

    private DataColumn dataColumn;

    protected abstract DataColumn createDataColumn();

    /*    public DataColumnTest(String testName) {
    super(testName);
    }
     */
//    public static Test suite() {
//        TestSuite suite = new TestSuite(DataColumnTest.class);
//        return suite;
//        return null;
//    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        dataColumn = createDataColumn();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of getName method, of class DataColumn.
     */
    public void testGetName() {
        System.out.println("getName");
        DataColumn instance = createDataColumn();
        String expResult = null;
        String result = instance.getName();
        assertEquals(expResult, result);
        instance = createDataColumn();

        expResult = "UPPERCASENAME";
        instance.setName("UpperCaseName");
        result = instance.getName();
        assertEquals(expResult, result);

    }

    /**
     * Test of setName method, of class DataColumn.
     */
    public void testSetName() {
        System.out.println("setName");
        DataColumn instance = createDataColumn();
        String expResult = "UPPERCASENAME";
        instance.setName("UpperCaseName");
        assertEquals(expResult, instance.getName());
        
        //Since label value is null method assigns by default parameter value
        assertEquals("UpperCaseName", instance.getLabel());

        instance = createDataColumn();
        expResult = "testLabel";
        instance.setName("testName,testLabel");
        assertEquals(expResult, instance.getLabel());

        
        try {
            instance.setName(null);
            fail("Null columnName argument! That's undefined!");
        } catch (IllegalArgumentException expected) {
            assertEquals("Column name cannot be null and length must be  > 0 ", expected.getMessage());
        }
        
        try {
            instance.setName("");
            fail("columnName argument length == 0 ! That's undefined!");
        } catch (IllegalArgumentException expected) {
            assertEquals("Column name cannot be null and length must be  > 0 ", expected.getMessage());
        }

        try {
            instance.setName(",startsWithComma");
            fail("The name property starts with comma");
        } catch (IllegalArgumentException expected) {
            assertEquals("First letter cannot be comma", expected.getMessage());
        }
        
    }

    /**
     * Test of getExpression method, of class DataColumn.
     */
    public void testGetExpression() {
        System.out.println("getExpression");
        //After column creation expression must be null
        DataColumn instance = createDataColumn();
        String expResult = null;
        String result = instance.getExpression();
        assertEquals(expResult, result);
        
        instance = createDataColumn();
        instance.setExpression("col2 * 2");
        expResult = "col2 * 2";
        result = instance.getExpression();
        assertEquals(expResult, result);        
    }

    /**
     * Test of setExpression method, of class DataColumn.
     */
    public void testSetExpression() {
        System.out.println("setExpression");
        String expr = null;
        DataColumn instance = createDataColumn();
        instance.setExpression(expr);
        assertEquals(null, instance.getExpression());    
        //columnExpression also must be set to null
        assertEquals(null, instance.getColumnExpression());        
        
        expr = "  ";
        instance = createDataColumn();
        instance.setExpression(expr);
        assertEquals(null, instance.getExpression());    
        //columnExpression also must be set to null
        assertEquals(null, instance.getColumnExpression());        
        
        
    }

    /**
     * Test of getColumnExpression method, of class DataColumn.
     *  
     */
    public void testGetColumnExpression() {
        System.out.println("getColumnExpression");
        DataColumn instance = createDataColumn();
        
        instance.setExpression("col1 * 2");
        
        ColumnExpressionContext result = instance.getColumnExpression();
        assertNotNull(result);
        
        instance = createDataColumn();
        instance.setExpression(null);
        
        result = instance.getColumnExpression();
        assertNull(result);
        
    }

    /**
     * Test of getType method, of class DataColumn.
     */
    public abstract void testGetType();


    /**
     * Test of setReadOnly method, of class DataColumn.
     */
    public void testSetReadOnly() {
        System.out.println("setReadOnly");
        DataColumn instance = createDataColumn();
        instance.setReadOnly(true);
        assertTrue(instance.isReadOnly());
        
        instance = createDataColumn();
        instance.setReadOnly(false);
        assertFalse(instance.isReadOnly());
        
    }


    /**
     * Test of setAutoIncrement method, of class DataColumn.
     */
    public void testSetAutoIncrement() {
        System.out.println("setAutoIncrement");
        DataColumn instance = createDataColumn();
        instance.setAutoIncrement(true);
        assertTrue(instance.isAutoIncrement() );

        instance = createDataColumn();
        instance.setAutoIncrement(false);
        assertFalse(instance.isAutoIncrement() );
        
        
    }

    /**
     * Test of setCaseSensitive method, of class DataColumn.
     */
    public void testSetCaseSensitive() {
        System.out.println("setCaseSensitive");
        DataColumn instance = createDataColumn();
        instance.setCaseSensitive(true);
        assertTrue(instance.isCaseSensitive() );

        instance = createDataColumn();
        instance.setCaseSensitive(false);
        assertFalse(instance.isCaseSensitive() );
    }


    /**
     * Test of setCurrency method, of class DataColumn.
     */
    public void testSetCurrency() {
        System.out.println("setCurrency");
        DataColumn instance = createDataColumn();
        instance.setCurrency(true);
        assertTrue(instance.isCurrency() );

        instance = createDataColumn();
        instance.setCurrency(false);
        assertFalse(instance.isCurrency() );
    }


    /**
     * Test of setSigned method, of class DataColumn.
     */
    public void testSetSigned() {
        System.out.println("setSigned");
        DataColumn instance = createDataColumn();
        
        instance.setSigned(true);
        assertTrue(instance.isSigned() );

        instance = createDataColumn();
        instance.setSigned(false);
        assertFalse(instance.isSigned() );
    }

    /**
     * Test of setSearchable method, of class DataColumn.
     */
    public void testSetSearchable() {
        System.out.println("setSearchable");
        DataColumn instance = createDataColumn();
        
        instance.setSearchable(true);
        assertTrue(instance.isSearchable() );

        instance = createDataColumn();
        instance.setSearchable(false);
        assertFalse(instance.isSearchable() );
    }

    /**
     * Test of setLabel method, of class DataColumn.
     */
    public void testSetLabel() {
        System.out.println("setLabel");
        String label = "testLabel";
        DataColumn instance = createDataColumn();
        instance.setLabel(label);
        assertEquals(label,instance.getLabel());
    }

    /**
     * Test of setTableName method, of class DataColumn.
     */
    public void testSetTableName() {
        System.out.println("setTableName");
        String tableName = "testTableName";
        DataColumn instance = createDataColumn();
        instance.setTableName(tableName);
        assertEquals("testTableName",instance.getTableName());
        
    }

    /**
     * Test of setSchemaName method, of class DataColumn.
     */
    public void testSetSchemaName() {
        System.out.println("setSchemaName");
        String schemaName = "testSchemaName";
        DataColumn instance = createDataColumn();
        instance.setSchemaName(schemaName);
        assertEquals("testSchemaName",instance.getSchemaName());
    }

    /**
     * Test of getKind method, of class DataColumn.
     */
    public void testGetKind() {
        System.out.println("getKind");
        DataColumn instance = createDataColumn();
        int expResult = DataColumn.DATA_KIND;
        int result = instance.getKind();
        assertEquals(expResult, result);
    }

    /**
     * Test of setKind method, of class DataColumn.
     */
    public void testSetKind() {
        System.out.println("setKind");
        int kind = DataColumn.DATA_KIND;;
        DataColumn instance = createDataColumn();
        instance.setKind(kind);
        assertEquals(kind, instance.getKind());
        
        kind = DataColumn.CALC_KIND;;
        instance = createDataColumn();
        instance.setKind(kind);
        assertEquals(kind, instance.getKind());
        
        kind = DataColumn.LOOKUP_KIND;;
        instance = createDataColumn();
        instance.setKind(kind);
        int expValue = DataColumn.DATA_KIND;
        assertEquals(expValue, instance.getKind());
        
    }

    /**
     * Test of createBlankObject method, of class DataColumn.
     */
    public abstract void testCreateBlankObject();
    /**
     * Test of setIndex method, of class DataColumn.
     */
/*    public void testSetIndex() {
        System.out.println("setIndex");
        int index = 0;
        DataColumn instance = createDataColumn();
        instance.setIndex(index);
        assertEquals(0,instance.getIndex());
    }
*/

    /**
     * Test of setCellIndex method, of class DataColumn.
     */
    public void testSetCellIndex() {
        System.out.println("setCellIndex");
        int index = 0;
        DataColumn instance = createDataColumn();
        instance.setCellIndex(index);
        assertEquals(0,instance.getCellIndex());
    }


    /**
     * Test of setNullable method, of class DataColumn.
     */
    public void testSetNullable() {
        System.out.println("setNullable");
        int nullable = ResultSetMetaData.columnNoNulls;
        DataColumn instance = createDataColumn();
        instance.setNullable(nullable);
        assertFalse(instance.isNullable());
        
        nullable = ResultSetMetaData.columnNullable;
        instance = createDataColumn();
        instance.setNullable(nullable);
        assertTrue(instance.isNullable());
        
        nullable = ResultSetMetaData.columnNullableUnknown;
        instance = createDataColumn();
        instance.setNullable(nullable);
        assertTrue(instance.isNullable());
        
        
    }


    /**
     * Test of clone method, of class DataColumn.
     */
    public void testClone() {
        System.out.println("clone");
        DataColumn instance = createDataColumn();
        instance.setName("col1");
        instance.setTableName("t1");
        instance.setPropertyName("p1");        
        instance.setFieldName("f1");        
        instance.setSchemaName("sc1");
        instance.setExpression("col1 * 2");
        
        Object expResult = null;
        DataColumn dc = (DataColumn)instance.clone();
        boolean b = 
            dc.getName().equalsIgnoreCase(instance.getName())    &&
            dc.getKind() == instance.getKind()   &&
            dc.getScale() == instance.getScale() &&
            dc.getSize() == instance.getSize()   &&         
            dc.getSize() == instance.getSize()   &&        
            dc.getPrecision() == instance.getPrecision()   &&        
            dc.getSqlType() == instance.getSqlType()   &&                    
            dc.getCellIndex() == instance.getCellIndex()   &&                    
            dc.getType().equals(instance.getType()) &&
            
            dc.isReadOnly() == instance.isReadOnly() &&
            dc.isNullable() == instance.isNullable() &&
            dc.isAutoIncrement() == instance.isAutoIncrement() &&
            dc.isCaseSensitive() == instance.isCaseSensitive() &&
            dc.isCurrency() == instance.isCurrency() &&
            dc.isSigned() == instance.isSigned() &&
            dc.isSearchable() == instance.isSearchable() &&
            dc.getLabel().equals(instance.getLabel()) &&
            dc.getTableName().equalsIgnoreCase(instance.getTableName()) &&
            dc.getSchemaName().equalsIgnoreCase(instance.getSchemaName()) &&            
            dc.getCellIndex() == instance.getCellIndex() &&
            dc.getPropertyName().equals(instance.getPropertyName()) &&
            dc.getFieldName().equals(instance.getFieldName()) &&
            //dc.getIndex() == instance.getIndex() &&
            dc.getExpression().equalsIgnoreCase(instance.getExpression());
        
        assertTrue(b);
    }

    /**
     * Test of toColumnType method, of class DataColumn.
     */
    public void testToColumnType() {
        System.out.println("toColumnType");
        Object obj = null;
        DataColumn instance = createDataColumn();
        Object expResult = null;
        Object result = instance.toColumnType(obj);
        assertEquals(expResult, result);
        
        obj = new Object();
        instance = createDataColumn();
        result = instance.toColumnType(obj);
        assertNotNull(result);
        assertEquals(instance.getType(),result.getClass());
        
    }


    /**
     * Test of compareObjects method, of class DataColumn.
     */
    public abstract void testCompareObjects_3args();

    /**
     * Test of setPropertyName method, of class DataColumn.
     */
    public void testSetPropertyName() {
        System.out.println("setPropertyName");
        String prop = "testProperty";
        DataColumn instance = createDataColumn();
        instance.setPropertyName(prop);
        assertEquals(prop,instance.getPropertyName());
    }


    /**
     * Test of setFieldName method, of class DataColumn.
     */
    public void testSetFieldName() {
        System.out.println("setFieldName");
        String f = "testField";
        DataColumn instance = createDataColumn();
        instance.setFieldName(f);
        assertEquals(f,instance.getFieldName());
    }

    /**
     * Test of cloneFrom method, of class DataColumn.
     */
    public abstract void testDefaultValueInstance();

    /**
     * Test of getDefaultValue method, of class DataColumn.
     */
    public void testGetDefaultValue() {
    }

    /**
     * Test of setDefaultValue method, of class DataColumn.
     */
    public void testSetDefaultValue() {
    }
}
