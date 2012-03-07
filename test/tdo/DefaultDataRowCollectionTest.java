/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo;

import tdo.support.TableContextForRowCollection;
import tdo.support.DataRowTst;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import tdo.service.TableServices;
import static org.junit.Assert.*;

/**
 *
 * @author Valery
 */
public class DefaultDataRowCollectionTest {
    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        context = new TableContextForRowCollection();
    }

    @After
    public void tearDown() {
    }
    TableServices context;
    
    protected DefaultDataRowCollection create() {
        DefaultDataRowCollection drc = new DefaultDataRowCollection();
        drc.setContext(context);
        return drc;
    }
    /**
     * Test of clear method, of class DefaultDataRowCollection.
     */
    @Test
    public void testClear() {
        DefaultDataRowCollection c = create();
        c.add();
        assertEquals(1,c.getCount());
        c.clear();
        assertEquals(0,c.getCount());
    }
    /**
     * Test of clear method, of class DefaultDataRowCollection.
     */
    @Test
    public void testAdd() {
        DefaultDataRowCollection c = create();
        c.add();
        assertEquals(1,c.getCount());
        c.clear();
        assertEquals(0,c.getCount());
        c.add();
        assertEquals(1,c.getCount());
        c.add();
        assertEquals(2,c.getCount());
        c = create();
        DataRow r = new DataRowTst();
        c.add(r);
        assertEquals(1,c.getCount());
    }
    /**
     * Test of copyFrom method of class DefaultDataRowCollection
     */
    @Test
    public void testCopyFrom() {
        DefaultDataRowCollection c = create();
        DefaultDataRowCollection c1 = create();        
        assertEquals(0,c1.getCount());        
        c.add();
        c1.copyFrom(c);
        assertEquals(1,c1.getCount());
        c.add();
        c1.copyFrom(c);
        assertEquals(3,c1.getCount());
        
        
    }
    /**
     * Test of create method of class DefaultDataRowCollection
     */
    @Test
    public void testCreate() {
        DefaultDataRowCollection c = create();
        Object c1 = c.create();
        
        assertTrue(c1 instanceof DefaultDataRowCollection );        
        int count1 = ((DataRowCollection)c1).getCount();
        
        assertEquals(0,count1);
        //
        // overloaded method with TableServices parameter
        //
        c1 = c.create(context,false);
        assertTrue(c1 instanceof DefaultDataRowCollection );        
        assertEquals(0,count1);        
        //
        // overloaded method with TableServices and boolean parameter
        //
        c.add();
        c1 = c.create(context,true);
        assertTrue(c1 instanceof DefaultDataRowCollection );        
        count1 = ((DataRowCollection)c1).getCount();
        assertEquals(1,count1);        
        
    }
    /**
     * Test of createShared method of class DefaultDataRowCollection
     */
    @Test
    public void testCreateShared() {
        DefaultDataRowCollection c = create();
        c.add();
        DataRowCollection c1 = c.createShared(c.getContext());
        assertSame(c.get(0),c1.get(0));
        c1.add();
        assertSame(c.get(1),c1.get(1));
    }
    /**
     * Test of delete method of class DefaultDataRowCollection
     */
    @Test
    public void testDelete() {
        DefaultDataRowCollection c = create();
        //
        // test delete(int)
        //
        DataRow r = c.add();
        DataRow r1 = c.delete(0);
        assertEquals(0,c.getCount());
        assertSame(r,r1);
        //
        // test delete(DataRow)
        //
        c = create();
        r = c.add();
        int rowIndex  = c.delete(r);
        assertEquals(0,rowIndex);
        
        try {
            c.delete(-1);
            fail("Row Index is negative !!!");
        } catch(IndexOutOfBoundsException e) {
            
        }
        try {
            c.delete(10);
            fail("Row Index is greater than getCount()-1!!!");
        } catch(IndexOutOfBoundsException e) {
            
        }
        
    }
    
    /**
     * Test of get method of class DefaultDataRowCollection
     */
    @Test
    public void testGet() {
        DefaultDataRowCollection c = create();
        //
        // test get(int)
        //
        DataRow r = c.add();
        DataRow r1 = c.get(0);
        assertSame(r,r1);
        try {
            c.get(-1);
            fail("Method get(int). Row Index is negative !!!");
        } catch(IndexOutOfBoundsException e) {
            
        }
        try {
            c.get(100);
            fail("Method get(int). Row Index is greater than getCount()-1 !!!");
        } catch(IndexOutOfBoundsException e) {
            
        }
        
        c.clear();
        try {
            c.get(0);
            fail("Method get(int) when collection is empty!!!");
        } catch(IndexOutOfBoundsException e) {
            
        }
        
    }    
    /**
     * Test of set method of class DefaultDataRowCollection
     */
    @Test
    public void testSet() {
        DefaultDataRowCollection c = create();
        //
        // test get(int)
        //
        DataRow r = c.add();
        DataRow r1 = new DataRowTst();
        c.set(0, r1);
        assertEquals(1,c.getCount());
        assertSame(c.get(0),r1);
        try {
            DataRow r2 = new DataRowTst();
            c.set(-1,r2);
            fail("Method set(int,DataRow). Row Index is negative !!!");
        } catch(IndexOutOfBoundsException e) {
            
        }
        try {
            DataRow r2 = new DataRowTst();
            c.set(c.getCount(),r2);
            fail("Method set(int,DataRow). Row Index is greater than getCount()-1 !!!");
        } catch(IndexOutOfBoundsException e) {
            
        }
        
        c.clear();
        try {
            DataRow r2 = new DataRowTst();
            c.set(0,r2);
            fail("Method set(int,DataRow) when collection is empty!!!");
        } catch(IndexOutOfBoundsException e) {
            
        }
        
    }    
    
    /**
     * Test of getCount method of class DefaultDataRowCollection
     */
    @Test
    public void testGetCount() {
        DefaultDataRowCollection c = create();
        assertEquals(0,c.getCount());
        DataRow r = c.add();
        assertEquals(1,c.getCount());
        
    }
    /**
     * Test of indexOf method of class DefaultDataRowCollection
     */
    @Test
    public void testIndexOf() {
        DefaultDataRowCollection c = create();
        DataRow r = c.add();
        DataRow r1 = c.add();
        assertEquals(0,c.indexOf(r));
        assertEquals(1,c.indexOf(r1));
        DataRow r2 = new DataRowTst();
        //
        // the collection doesn't contain a row
        //
        assertEquals(-1,c.indexOf(r2));
        //
        // try to extract index with null parameter
        //
        assertEquals(-1,c.indexOf(null));
    }    
    /**
     * Test of indexOf method of class DefaultDataRowCollection
     */
    @Test
    public void testInsert() {
        //
        // test insert(int)
        //
        DefaultDataRowCollection c = create();
        DataRow r = c.add();
        DataRow r1 = c.add();
        // first row
        DataRow newRow = c.insert(0);
        assertSame(c.get(0),newRow);
        assertSame(c.get(1),r);    
        assertSame(c.get(2),r1);  
        // middle row    
        DataRow newRow1 = c.insert(1);
        assertSame(c.get(0),newRow);
        assertSame(c.get(1),newRow1);    
        assertSame(c.get(2),r);  
        assertSame(c.get(3),r1);  
        // last row
        DataRow newRow2 = c.insert(3);
        assertSame(c.get(0),newRow);
        assertSame(c.get(1),newRow1);    
        assertSame(c.get(2),r);  
        assertSame(c.get(3),newRow2);  
        assertSame(c.get(4),r1);  
        
        // after last row
        DataRow newRow3 = c.insert(5);
        assertSame(c.get(0),newRow);
        assertSame(c.get(1),newRow1);    
        assertSame(c.get(2),r);  
        assertSame(c.get(3),newRow2);  
        assertSame(c.get(4),r1);  
        assertSame(c.get(5),newRow3);  
        
        try {
            c.insert(-1);
            fail("Method insert(int). Row Index is negative !!!");
        } catch(IndexOutOfBoundsException e) {
            
        }
        try {
            c.insert(100);
            fail("Method insert(int). Row Index is greater than getCount()-1 !!!");
        } catch(IndexOutOfBoundsException e) {
            
        }
        
        
        //
        // test insert(int,DataRow)
        //
        c = create();
        r = c.add();
        r1 = c.add();
        assertTrue(c.get(0)==r);
        
        DataRow r2 = new DataRowTst(); 
        //firt position
        c.insert(0,r2);
        assertSame(c.get(0),r2);
        assertSame(c.get(1),r);    
        assertSame(c.get(2),r1);  
        
        //middle position
        DataRow r3 = new DataRowTst(); 
        c.insert(1,r3);
        assertSame(c.get(0),r2);
        assertSame(c.get(1),r3);    
        assertSame(c.get(2),r);  
        assertSame(c.get(3),r1);  
        //last position
        DataRow r4 = new DataRowTst(); 
        c.insert(3,r4);
        assertSame(c.get(0),r2);
        assertSame(c.get(1),r3);    
        assertSame(c.get(2),r);  
        assertSame(c.get(3),r4);  
        assertSame(c.get(4),r1);  

        //after last position
        DataRow r5 = new DataRowTst(); 
        c.insert(5,r5);
        assertSame(c.get(0),r2);
        assertSame(c.get(1),r3);    
        assertSame(c.get(2),r);  
        assertSame(c.get(3),r4);  
        assertSame(c.get(4),r1);  
        assertSame(c.get(5),r5);  
        //
        // IndexOutOfBoundsException
        //
        try {
            DataRow r6 = new DataRowTst(); 
            c.insert(-1);
            fail("Method insert(int,DataRow). Row Index is negative !!!");
        } catch(IndexOutOfBoundsException e) {
            
        }
        try {
            c.insert(100);
            DataRow r6 = new DataRowTst(); 
            fail("Method insert(int,DataRow). Row Index is greater than getCount()-1 !!!");
        } catch(IndexOutOfBoundsException e) {
            
        }
        
    }    
    
    @Test
    public void testIsEmpty() {
        DefaultDataRowCollection c = create();
        assertTrue(c.isEmpty());
        DataRow r = c.add();
        assertFalse(c.isEmpty());
    }    
}
