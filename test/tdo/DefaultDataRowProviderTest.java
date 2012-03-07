/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tdo;

/**
 *
 * @author Valery
 */
import tdo.service.CellCollectionServices;
import tdo.service.TableServices;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import tdo.service.CellServices;
import static org.junit.Assert.*;

/**
 * Test of the class {@link tdo.DefaultDataRowProvider}.
 * <code>tdo.DefaultDataRowProvider</code> object invokes some 
 * <code>tdo.Table</code> methods through services defined in 
 * {@link tdo.service.TableServices#getCellCollectionServices()}.
 * <p>For the test to execute we have created two internal classes that
 * emulate a real table object behavior:
 * <ol>
 *   <li>{@link DefaultDataRowProviderTest.TableContextImpl}</li>
 *   <li>{@link DefaultDataRowProviderTest.CellServicesImpl}</li>
 * </ol>
 * Additionally class uses mock class <code>DataCellCollectionMock</code>.
 */
public class DefaultDataRowProviderTest {

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
    }
    public DefaultDataRowProvider create() {
        return new DefaultDataRowProvider( new TableContextImpl());
    }
    
    @Test 
    public void testCreateRow() {
        DefaultDataRowProvider p = create();
        DataRow r = p.createRow();
        assertTrue( r instanceof DefaultDataRow);
        assertNotNull(r.getCellCollection());

        p = create();
        r = p.createRow(this);
        assertTrue( r instanceof DefaultDataRow);
        assertNotNull(r.getCellCollection());
        
    }
    @Test 
    public void testCreateRowCopy() {
        DefaultDataRowProvider p = create();
        DataRow r = p.createRow();
        DataRow r1 = p.createRowCopy(r);
        assertTrue( r1 instanceof DefaultDataRow);
        assertNotNull(r1.getCellCollection());
    }
    
    /* ====================================================================
     *  Inner Service Classes
     * ==================================================================== */
    public class TableContextImpl extends tdo.service.AbstractTableServices {
        CellCollectionServices cellCollectionServices;
        public TableContextImpl() {
            this.cellCollectionServices = new CellCollectionServicesImpl(context);
        }
        @Override
        public CellCollectionServices getCellCollectionServices() {
            return this.cellCollectionServices;
        }
        
    }//class TableContextImpl
    public class CellCollectionServicesImpl implements CellCollectionServices{
        TableServices context;
        public CellCollectionServicesImpl(TableServices context) {
            this.context = context;
        }
        @Override
        public DataCellCollection createCellCollection() {
            return this.createCellCollection(null);
        }

        @Override
        public DataCellCollection createCellCollection(Object bean) {
            DataCellCollectionMock dcc = new DataCellCollectionMock(context);
            return dcc;
        }

    }
}//class DefaultDataRowProviderTest