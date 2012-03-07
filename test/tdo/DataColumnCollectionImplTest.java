/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo;

import tdo.support.DataColumnProviderTst;
import junit.framework.TestCase;

/**
 *
 * @author Valery
 */
public class DataColumnCollectionImplTest extends TestCase{
    public DefaultDataColumnCollection newInstance() {
        return new DefaultDataColumnCollection(new DataColumnProviderTst());
    }
    public void testGetCount() {
        DefaultDataColumnCollection instance = newInstance();
        assertEquals(0,instance.getCount());
        instance.add(String.class, "testColumnName1");
        assertEquals(1,instance.getCount());        
        instance.remove("testColumnName1");
        assertEquals(0,instance.getCount());
        instance.add(String.class, "testColumnName1");
        instance.addCalculated(String.class,"testColumnName2");
        assertEquals(1,instance.getCount(DataColumn.DATA_KIND));
        assertEquals(1,instance.getCount(DataColumn.CALC_KIND));
        
        assertTrue(instance.isValid());
    }
    public void testAdd() {
        DefaultDataColumnCollection instance = newInstance();
        assertEquals(0,instance.getCount());

        instance.add(String.class);
        assertEquals(1,instance.getCount());        
        
        assertTrue(instance.isValid());
        
        instance.add(java.sql.Types.CHAR);
        assertEquals(2,instance.getCount());        
        assertEquals(0,instance.get(0).getCellIndex());        

        
        assertTrue(instance.isValid());        
        
        instance.add(String.class, "testColumnName1");
        assertEquals(3,instance.getCount());        
     
        
        
        DataColumn dc1 = instance.createColumn(String.class,"testColumnName2" );
        instance.add(dc1);
        assertEquals(4,instance.getCount());        
        
        
        instance.addCalculated(String.class,"testColumnName4");
        assertEquals(5,instance.getCount());        
        
        // Dublicate columnName
        try {
            DataColumn dc2 = instance.createColumn(String.class,"testColumnName1" );
            instance.add(dc2);
            fail("ColumnName testColumnName1 already exists");
        } catch(IllegalArgumentException iae ) {
            assertEquals("Dublicate columnName 'TESTCOLUMNNAME1'", iae.getMessage());
            assertEquals(5,instance.getCount());
        }
        
        try {
            instance.add(String.class,"testColumnName1");
            fail("ColumnName testColumnName1 already exists");
        } catch(IllegalArgumentException iae ) {
            assertEquals("Dublicate columnName 'TESTCOLUMNNAME1'", iae.getMessage());
            assertEquals(5,instance.getCount());
        }
        
        
        
    }

    
    public void testInsert() {
        DefaultDataColumnCollection instance = newInstance();

        instance.insert(0, String.class, "testColumnName1");
        assertEquals(1,instance.getCount());        

        instance.insertCalculated(1, String.class, "testColumnName2");
        assertEquals(2,instance.getCount());        

        instance.insert(0, String.class, "testColumnName3");
        assertEquals(3,instance.getCount());        

        instance.insertCalculated(0, String.class, "testColumnName4");
        assertEquals(4,instance.getCount());        
        
        instance.insert(4, String.class, "testColumnName5");
        assertEquals(5,instance.getCount());        
        
        assertTrue(instance.isValid());
        
        // Insert position > columnCount
        
        try {
            instance.insert(6, String.class, "testColumnName6");
            fail("Insert posision is greater then column count");        
        } catch( IndexOutOfBoundsException ioe ) {
            assertEquals(5,instance.getCount());        
            assertTrue(ioe.getMessage().startsWith("Insert position out of bounds ( insert("));            
        }
        
        try {
            DataColumn dc1 = instance.createColumn(String.class,"testColumnName6" );
            instance.insert(6, dc1);
            fail("Insert posision is greater then column count");        
        } catch( IndexOutOfBoundsException ioe ) {
            assertEquals(5,instance.getCount());        
            assertTrue(ioe.getMessage().startsWith("Insert position out of bounds ( insert("));            
        }
        try {
            instance.insertCalculated(6, String.class, "testColumnName6");
            fail("Insert posision is greater then column count");        
        } catch( IndexOutOfBoundsException ioe ) {
            assertEquals(5,instance.getCount());        
            assertTrue(ioe.getMessage().startsWith("Insert position out of bounds ( insert("));            
        }

        //assertTrue(instance.isValid());        
        
        // Insert position < 0
        
        try {
            instance.insert(-1, String.class, "testColumnName6");
            fail("Insert posision is less then zero");        
        } catch( IndexOutOfBoundsException ioe ) {
            assertEquals(5,instance.getCount());        
            assertTrue(ioe.getMessage().startsWith("Insert position out of bounds ( insert("));            
        }
        
        try {
            DataColumn dc1 = instance.createColumn(String.class,"testColumnName6" );
            instance.insert(-1, dc1);
            fail("Insert posision is less then zero");        
        } catch( IndexOutOfBoundsException ioe ) {
            assertEquals(5,instance.getCount());        
            assertTrue(ioe.getMessage().startsWith("Insert position out of bounds ( insert("));            
        }
        try {
            instance.insertCalculated(-1, String.class, "testColumnName6");
            fail("Insert posision is less then zero");        
        } catch( IndexOutOfBoundsException ioe ) {
            assertEquals(5,instance.getCount());        
            assertTrue(ioe.getMessage().startsWith("Insert position out of bounds ( insert("));            
        }

        //assertTrue(instance.isValid());        
        
        // Dublicate columnName
        
        try {
            DataColumn dc2 = instance.createColumn(String.class,"testColumnName1" );
            instance.insert(0,dc2);
            fail("ColumnName testColumnName1 already exists");
        } catch(IllegalArgumentException iae ) {
            assertEquals("Dublicate columnName 'TESTCOLUMNNAME1'", iae.getMessage());
            assertEquals(5,instance.getCount());
        }
        
        try {
            instance.insert(0, String.class,"testColumnName2");
            fail("ColumnName testColumnName2 already exists");
        } catch(IllegalArgumentException iae ) {
            assertEquals("Dublicate columnName 'TESTCOLUMNNAME2'", iae.getMessage());
            assertEquals(5,instance.getCount());
        }
        
        try {
            instance.insertCalculated(0, String.class,"testColumnName3");
            fail("ColumnName testColumnName3 already exists");
        } catch(IllegalArgumentException iae ) {
            assertEquals("Dublicate columnName 'TESTCOLUMNNAME3'", iae.getMessage());
            assertEquals(5,instance.getCount());
        }
        
        //assertTrue(instance.isValid());
        
    }

    public void testRemove() {
        DefaultDataColumnCollection instance = newInstance();

        instance.add(String.class);
        instance.add(java.sql.Types.CHAR);
        instance.add(String.class, "testColumnName1");
        DataColumn dc1 = instance.createColumn(String.class,"testColumnName2" );
        instance.add(dc1);
        instance.add(java.sql.Types.CHAR,"testColumnName3");
        instance.addCalculated(String.class,"testColumnName4");
        instance.addCalculated(String.class,"testColumnName5");
        instance.addCalculated(String.class,"testColumnName6");
        int count = instance.getCount();
        assertEquals(3,instance.remove(dc1));
        assertEquals(count-1,instance.getCount());
        
        assertTrue(instance.find("testColumnName2") < 0 );
        assertTrue(instance.indexOf(dc1) < 0 );
        //assertTrue(instance.isValid());
        count--;
        instance.remove(0);
        assertEquals(count-1,instance.getCount());
        //assertTrue(instance.isValid());

        count--;
        instance.remove(instance.getCount()-1);
        assertEquals(count-1,instance.getCount());
        //assertTrue(instance.isValid());
        
        count--;
        instance.remove("testcolumnname5");
        assertEquals(count-1,instance.getCount());
        //assertTrue(instance.isValid());
        count--;
        try {
            instance.remove(100);
            fail("columnIndex is greater than column count!");
        } catch( IndexOutOfBoundsException e ) {
            
        }
        try {
            instance.remove(-100);
            fail("columnIndex is less than zero !");
        } catch( IndexOutOfBoundsException e ) {
            
        }
        
        while ( count > 0 ) {
            DataColumn dc = instance.get(count-1);
            instance.remove(dc);
            assertEquals(count-1,instance.getCount());
            //assertTrue(instance.isValid());
            count--;            
        }
        assertEquals(0,instance.getCount());
        //assertTrue(instance.isValid());
    }    

    public void testMove() {
        DefaultDataColumnCollection instance = newInstance();

        DataColumn dc0 = instance.add(String.class);
        DataColumn dc1 = instance.add(java.sql.Types.CHAR);
        
        instance.add(String.class, "testColumnName1");
        DataColumn dc2 = instance.createColumn(String.class,"testColumnName2" );
        instance.add(dc2);
        instance.add(String.class,"testColumnName3");
        instance.addCalculated(String.class,"testColumnName4");
        instance.addCalculated(String.class,"testColumnName5");
        instance.addCalculated(String.class,"testColumnName6");
        int count = instance.getCount();
        
        // Moving doesn'n happen ( same columns)
        instance.move(0, 0);
        
        assertEquals(count,instance.getCount());
        //assertTrue(instance.isValid());
        
        // Moving doesn'n happen ( 1 before 2 produce no result  )
        String dc0Name = instance.get(0).getName();
        String dc1Name = instance.get(1).getName();
        instance.move(1, 2);
        DataColumn c = instance.get(1);
        

        assertTrue(c.getName().equals(dc1Name));
        assertEquals(count,instance.getCount());
        //assertTrue(instance.isValid());

        String dc2Name = instance.get(2).getName();
        instance.move(2, 1);
        c = instance.get(1);
        

        assertTrue(c.getName().equals(dc2Name));
        assertEquals(count,instance.getCount());
        //assertTrue(instance.isValid());

        // Column 0 moves after last column
        
        dc0Name = instance.get(0).getName();
        instance.move(0, count);
        c = instance.get(count-1);
        

        assertTrue(c.getName().equals(dc0Name));
        assertEquals(count,instance.getCount());
        //assertTrue(instance.isValid());

        // last column moves before the very first column
        
        String dcLastName = instance.get(count-1).getName();
        instance.move(count-1,0);
        c = instance.get(0);
        

        assertTrue(c.getName().equals(dcLastName));
        assertEquals(count,instance.getCount());
        //assertTrue(instance.isValid());
        
    }    

    public void testSet() {
        DefaultDataColumnCollection instance = newInstance();

        DataColumn dc0 = instance.add(String.class,"testColumnName0");
        DataColumn dc1 = instance.add(java.sql.Types.CHAR,"testColumnName1");
        
        DataColumn dc2 = instance.add(String.class, "testColumnName2");
        DataColumn dc3 = instance.add(String.class, "testColumnName3");
        int count = instance.getCount();
        
        // setting doesn'n happen ( same columns)
        instance.set(2,dc2);
        assertEquals(count,instance.getCount());
        assertEquals(0,instance.indexOf(dc0));
        assertEquals(1,instance.indexOf(dc1));
        assertEquals(2,instance.indexOf(dc2));
        assertEquals(3,instance.indexOf(dc3));
        
        // new index less than old
        
        instance.set(0,dc2);
        
        assertEquals(count,instance.getCount());
        assertEquals(0,instance.indexOf(dc2));
        assertEquals(1,instance.indexOf(dc0));
        assertEquals(2,instance.indexOf(dc1));
        assertEquals(3,instance.indexOf(dc3));

        // new index greater than old
        
        instance.set(3,dc2);
        
        assertEquals(count,instance.getCount());
        assertEquals(0,instance.indexOf(dc0));
        assertEquals(1,instance.indexOf(dc1));
        assertEquals(2,instance.indexOf(dc3));
        assertEquals(3,instance.indexOf(dc2));
        
        // Insert column when collection doesn't contain one

        DataColumn newdc = instance.createColumn(String.class,"newColumnName0" );
        instance.set(0,newdc);
        count++;
        assertEquals(count,instance.getCount());
        assertEquals(0,instance.indexOf(newdc));
        assertEquals(1,instance.indexOf(dc0));
        assertEquals(2,instance.indexOf(dc1));
        assertEquals(3,instance.indexOf(dc3));
        assertEquals(4,instance.indexOf(dc2));

        // Insert column as last element when collection doesn't contain one
        
        
        DataColumn newdc1 = instance.createColumn(String.class,"newColumnName1" );
        instance.set(count,newdc1);
        count++;
        assertEquals(count,instance.getCount());
        assertEquals(0,instance.indexOf(newdc));
        assertEquals(1,instance.indexOf(dc0));
        assertEquals(2,instance.indexOf(dc1));
        assertEquals(3,instance.indexOf(dc3));
        assertEquals(4,instance.indexOf(dc2));
        assertEquals(5,instance.indexOf(newdc1));
        
        ////////////// EXCEPTIONS /////////////////////
        
        try {
            instance.set(-1,newdc); 
            fail( "New index has negative value!");
        } catch( IndexOutOfBoundsException e ) {
            assertEquals("Invalid constraint 'new index < 0 or  > column count'",e.getMessage());
        }
        try {
            instance.set(count+1,newdc);
            fail( "New index value is greater than column count!");
        } catch( IndexOutOfBoundsException e ) {
            assertEquals("Invalid constraint 'new index < 0 or  > column count'",e.getMessage());
        }
        
    }    
    
    public void testFind() {
        DefaultDataColumnCollection instance = newInstance();

        DataColumn dc0 = instance.add(String.class);
        DataColumn dc1 = instance.add(java.sql.Types.CHAR);
        
        instance.add(String.class, "testColumnName1");
        DataColumn dc2 = instance.createColumn(String.class,"testColumnName2" );
        instance.add(dc2);
        instance.add(java.sql.Types.CHAR,"testColumnName3");
        instance.addCalculated(String.class,"testColumnName4");
        instance.addCalculated(String.class,"testColumnName5");
        instance.addCalculated(String.class,"testColumnName6");
        int count = instance.getCount();
        
        assertEquals(2,instance.find("testColumnName1"));
        assertEquals(count-1,instance.find("testColumnName6"));        
        assertEquals(-1,instance.find("testCCColumnName6"));        
    }
    
    public void testIndexOf() {
        DefaultDataColumnCollection instance = newInstance();

        DataColumn dc0 = instance.add(String.class);
        DataColumn dc1 = instance.add(java.sql.Types.CHAR);
        
        instance.add(String.class, "testColumnName1");
        DataColumn dc2 = instance.createColumn(String.class,"testColumnName2" );
        instance.add(dc2);
        instance.add(java.sql.Types.CHAR,"testColumnName3");
        int count = instance.getCount();
        assertEquals(3,instance.indexOf(dc2));
    }    
    
    public void testClear() {
        DefaultDataColumnCollection instance = newInstance();

        DataColumn dc0 = instance.add(String.class);
        DataColumn dc1 = instance.add(java.sql.Types.CHAR);
        instance.clear();
        assertEquals(0,instance.getCount());
    }   
    
    public void testCopyTo() {
        DefaultDataColumnCollection instance = newInstance();
        DefaultDataColumnCollection target = newInstance();

        DataColumn dc0 = instance.add(String.class);
        DataColumn dc1 = instance.add(java.sql.Types.CHAR);
        instance.copyTo(target);
        assertEquals(instance.getCount(),target.getCount());
    }   
    public void testIsEmpty() {
        DefaultDataColumnCollection instance = newInstance();
        assertTrue(instance.isEmpty());
        instance.add(String.class);        
        assertFalse(instance.isEmpty());
        

    }    
    
    public void testGet() {
        DefaultDataColumnCollection instance = newInstance();

        DataColumn dc0 = instance.add(String.class);
        DataColumn dc1 = instance.add(java.sql.Types.CHAR);
        
        instance.add(String.class, "testColumnName1");
        DataColumn dc2 = instance.createColumn(String.class,"testColumnName2" );
        instance.add(dc2);
        instance.add(java.sql.Types.CHAR,"testColumnName3");
        instance.addCalculated(String.class,"testColumnName4");
        instance.addCalculated(String.class,"testColumnName5");
        instance.addCalculated(String.class,"testColumnName6");
        int count = instance.getCount();
        
        DataColumn gdc = instance.get(3);
        assertSame(dc2,instance.get(3));
        assertSame(dc2,instance.get("TESTCOLUMNNAME2"));
        assertNull(instance.get("test3"));
        
        try {
            instance.get(1000);
            fail("Index 1000 > " + count  + " (out of bounds)");
        } catch(IndexOutOfBoundsException e  ) {
            
        }
        try {
            instance.get(-1);
            fail("Index -1 > " + count  + " (out of bounds)");
        } catch(IndexOutOfBoundsException e  ) {
            
        }
        
    }    
    
    public void testCreate() {
        DefaultDataColumnCollection instance = newInstance();
        DataColumnCollection created = instance.create();
        assertEquals(DefaultDataColumnCollection.class, created.getClass());
    }    
}//class DataColumnCollectionImplTest
