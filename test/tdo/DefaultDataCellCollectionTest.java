/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo;

import tdo.service.CellCollectionServices;
import tdo.service.TableServices;
import java.util.HashMap;
import java.util.Map;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import tdo.service.CellServices;
import static org.junit.Assert.*;

/**
 * Test of the class {@link tdo.DefaultDataCellCollection}.
 * <code>tdo.DefaultDataCellCollection</code> object invokes some 
 * <code>tdo.Table</code> methods through services defined in 
 * {@link tdo.service.TableServices#getCellServices()}.
 * <p>For the test to execute we have created two internal classes that
 * emulate a real table object behavior:
 * <ol>
 *   <li>{@link DefaultDataCellCollectionTest.DefaultDataCellCollectionTableContext}</li>
 *   <li>{@link DefaultDataCellCollectionTest.CellServicesImpl}</li>
 * </ol>
 
 */
public class DefaultDataCellCollectionTest {
    

    TableServices context;
    CellServices cellService;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        context = (TableServices) new DefaultDataCellCollectionTableContext();
        cellService = (CellServices) context.getCellServices();
    }
    public DefaultDataCellCollection createCells() {
        return new DefaultDataCellCollection(this.context);
    }
    
    @Test
    public void testCopyCells() {
        //
        // signiture: copyCells(DataRow)
        //
        DefaultDataCellCollection cells = createCells();
        int count = cells.size();
        DefaultDataCellCollection source = createCells();
        source.setValue("cell0m", 0);
        cells.copyCells(source);
        assertEquals(count,cells.size());
        assertEquals("cell0m",cells.getValue(0));        
        //
        // signiture: copyCells(Object[])
        //
        cells = createCells();
        Integer[] isource = new Integer[count];
        for ( int i=0; i< isource.length; i++) {
            isource[i] = i;
        }
        cells.copyCells(isource);
        assertEquals(0,cells.getValue(0));
        assertEquals(1,cells.getValue(1));        
        assertEquals(2,cells.getValue(2));        
        assertEquals(3,cells.getValue(4));   // 4 -> 3     
        /* !! as specified by DataCeilCollection for calculated columns
           getValue() must not be invoked
         */
    }
    @Test
    public void testGetValue() {
        //
        // signiture: getValue(int)
        //
        DefaultDataCellCollection cells = createCells();
        assertEquals("cell0",cells.getValue(0));
        assertEquals("cell1",cells.getValue(1));
        assertEquals("cell2",cells.getValue(2));        
        assertEquals("cell4",cells.getValue(4)); 
        //
        // signiture: getValue(String)
        //
        cells = createCells();
        assertEquals("cell0",cells.getValue("C0"));
        assertEquals("cell1",cells.getValue("C1"));
        assertEquals("cell2",cells.getValue("C2"));        
        assertEquals("cell4",cells.getValue("C4")); 
        
        
    }    
    @Test
    public void testSetValue() {
        //
        // signiture: setValue(Object,int)
        //
        DefaultDataCellCollection cells = createCells();
        cells.setValue("cell0m", 0);
        assertEquals("cell0m",cells.getValue(0));
        cells.setValue("cell1m", 1);
        assertEquals("cell1m",cells.getValue(1));
        cells.setValue("cell2m", 2);
        assertEquals("cell2m",cells.getValue(2));        
        cells.setValue("cell4m", 4);
        assertEquals("cell4m",cells.getValue(4)); 
        //
        // signiture: getValue(String)
        //
        cells = createCells();
        cells.setValue("cell0m", "C0");
        assertEquals("cell0m",cells.getValue("C0"));
        cells.setValue("cell1m", "C1");
        assertEquals("cell1m",cells.getValue("C1"));
        cells.setValue("cell2m", "C2");
        assertEquals("cell2m",cells.getValue("C2"));        
        cells.setValue("cell4m", "C4");
        assertEquals("cell4m",cells.getValue("C4")); 
        
        
    }    
    @Test
    public void testColumnAdded() {
        //
        // Remember that columnIndex = 3 specifies a calculated column
        // and is not contained by cells
        //
        DefaultDataCellCollection cells = createCells();
        cells.columnAdded(5);
        assertEquals("cell5",cells.getValue(5));
    }    
    @Test
    public void testColumnRemoved() {
        //
        // Remember that columnIndex = 3 specifies a calculated column
        // and is not contained by cells
        //
        DefaultDataCellCollection cells = createCells();
        cells.columnAdded(5);
        assertEquals(5,cells.size());        
        cells.columnRemoved(5);
        assertEquals(4,cells.size());
    }    
    @Test
    public void testColumnMoved() {
        //
        // Remember that columnIndex = 3 specifies a calculated column
        // and is not contained by cells
        //
        DefaultDataCellCollection cells = createCells();
        cells.columnMoved(2, 2, 0); // the column 2 moved before 0 column
        assertEquals("cell2",cells.getValue(0));        
        assertEquals("cell0",cells.getValue(1));        
        assertEquals("cell1",cells.getValue(2));        
        
    }    
    
   public class DefaultDataCellCollectionTableContext extends tdo.service.AbstractTableServices {

        boolean isLoading;
        boolean isEditProhibited;
        boolean[] editableCells = new boolean[]{true, true, false, true, true};
        boolean[] nullableCells = new boolean[]{true, true, true, false, true};
        Map<Integer, Integer> calcColumns;
        Map<String, Integer> columnNameMap;
        HashMap<DataRow, Integer> rowMap;
        int columnCount;
        private CellServices cellService;
        
        public DefaultDataCellCollectionTableContext() {

            columnNameMap = new HashMap<String, Integer>();
            columnNameMap.put("C0", 0);
            columnNameMap.put("C1", 1);
            columnNameMap.put("C2", 2);
            columnNameMap.put("C3", 3);
            columnNameMap.put("C4", 4);

            isLoading = true;
            isEditProhibited = false;

            columnCount = 5;


            rowMap = new HashMap<DataRow, Integer>();
            calcColumns = new HashMap<Integer, Integer>();
            calcColumns.put(0, DataColumn.DATA_KIND);
            calcColumns.put(1, DataColumn.DATA_KIND);
            calcColumns.put(2, DataColumn.DATA_KIND);            
            calcColumns.put(3, DataColumn.CALC_KIND);            
            calcColumns.put(4, DataColumn.DATA_KIND);            
            this.cellService = new CellServicesImpl(this);
        }

        @Override
        public CellServices getCellServices() {
            return (CellServices) this.cellService;
        }

        @Override
        public CellCollectionServices getCellCollectionServices() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }//class DefaultDataCellCollectionTableContext
   public class CellServicesImpl implements tdo.service.CellServices {

        DefaultDataCellCollectionTableContext context;

        public CellServicesImpl(TableServices context) {
            this.context = (DefaultDataCellCollectionTableContext)context;
        }

        @Override
        public int getCellIndex(int columnIndex) {
            int r = columnIndex;
            if ( columnIndex == 3  )
                r = -1;
            else if ( columnIndex == 4  ) {
                r = 3;
            } else if ( columnIndex > 4 ) {
                r = columnIndex - 1;
            }
            return r;
        }

        @Override
        public int getColumnIndex(String columnName) {
            return context.columnNameMap.get(columnName);
        }

        @Override
        public int getColumnCount() {
            return this.context.columnCount;
        }

        @Override
        public boolean isDataKind(int columnIndex) {
           return ! (context.calcColumns.containsKey(columnIndex) && 
                    context.calcColumns.get(columnIndex) == DataColumn.CALC_KIND) ;
        }

        @Override
        public String getPropertyName(int columnIndex) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String getFieldName(int columnIndex) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Object createBlankObject(int columnIndex) {
            return "cell" + columnIndex;
        }
    }//class CellServicesImpl

}//class DefaultDataCellCollectionTest
