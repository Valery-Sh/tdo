/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tdo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import tdo.service.CellCollectionServices;
import tdo.service.CellServices;
import tdo.service.TableServices;
import static org.junit.Assert.*;
import static tdo.RowState.*;

/**
 *
 * @author valery
 */
public class DefaultDataRowTest {

    TableServices context;
    DataRowServices rowServices;
    DefaultDataCellCollectionStub cellList;
    DefaultDataRowProviderTestImpl rowProvider;

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {


        context = new DataRowTableContext();
        rowServices = ((DataRowServices) context.getDataRowServices());
    /*
    calcColumns = new HashMap<Integer, Integer>();
    cellList = new DefaultDataCellCollectionStub(context, columnCount, calcColumns, columnNameMap);
    rowProvider = new DefaultDataRowProviderTestImpl(context, columnCount, calcColumns, columnNameMap);
    
    rowServices.initTestData(isLoading, false, editableCells, nullableCells,
    columnNameMap, rowMap, rowProvider);
     */
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getContext method, of class DefaultDataRow.
     */
    @Test
    public void testGetContext() {
    }

    /**
     * Test of getState method, of class DefaultDataRow.
     */
    @Test
    public void testAttach() {
        rowServices.initTestData(true, false);
        DataRow r = rowServices.createRow();
        assertEquals(RowState.DETACHED, r.getState().getEditingState());

        r.attach();
        assertEquals(RowState.LOADED, r.getState().getEditingState());
        assertEquals(RowState.LOADED, r.getState().getOriginalState());
        r.attach(); // must keep state

        assertEquals(RowState.LOADED, r.getState().getEditingState());
        assertEquals(RowState.LOADED, r.getState().getOriginalState());

        // when isLoading == false
        rowServices.initTestData(false, false);
        r = rowServices.createRow();

        r.attach();
        assertEquals(RowState.MANMADE, r.getState().getEditingState());
        assertEquals(RowState.MANMADE, r.getState().getOriginalState());

        r.attach(); // must keep state

        assertEquals(RowState.MANMADE, r.getState().getEditingState());
        assertEquals(RowState.MANMADE, r.getState().getOriginalState());

    }

    @Test
    public void testAttachNew() {
        // isLoading == true
        rowServices.initTestData(true, false);
        DataRow r = rowServices.createRow();
        assertEquals(RowState.DETACHED, r.getState().getEditingState());
        try {
            r.attachNew();
            fail("row editingState is loading. attachNew must not be invoked");
        } catch (DataOperationException e) {
            assertEquals("attachNew() : The table cannot be in loading state", e.getMessage());
        }
        //
        // isLoading == false
        //
        rowServices.initTestData(false, false);
        r = rowServices.createRow();
        r.attachNew();
        assertEquals(RowState.INSERTING, r.getState().getEditingState());
        assertEquals(RowState.MANMADE, r.getState().getOriginalState());

    }

    @Test
    public void testSetReadOnly() {
        DataRow r = rowServices.createRow();
        r.setReadOnly(true);
        assertTrue(r.isReadOnly());

        r.setReadOnly(false);
        assertFalse(r.isReadOnly());
    }

    @Test
    public void testSetValue() {
        //
        // The row can't be modified emulation when Table.isEditProhibeted==true
        // 
        //
        rowServices.initTestData(false, true);
        DataRow r = rowServices.createRow();
        r.attach();

        String oldValue = (String) r.getValue(0); // equals to "cell0"

        r.setValue("cell0m", 0);
        assertEquals("cell0", r.getValue(0));

        //
        // The row can't be modified emulation when Table.isCellEditable==false
        // We've defined a column which index equals to 2 as not editable
        //
        rowServices.initTestData(false, false);
        r = rowServices.createRow();
        r.attach();
        oldValue = (String) r.getValue(2); // equals to "cell2"

        r.setValue("cell2m", 2);
        assertEquals("cell2", r.getValue(2));

        //
        // The row can't be modified emulation when deleted
        //
        rowServices.initTestData(false, false);
        r = rowServices.createRow();
        r.attach();
        r.delete();
        oldValue = (String) r.getValue(0); // equals to "cell0"

        r.setValue("cell0m", 0);
        assertEquals("cell0", r.getValue(0));

        //
        // The row can't be modified emulation when:
        //   value == null && !state.beginEditMode 
        // and specified column cannot accept null values 
        // We've defined a column which index equals to 3 as not nullable
        //
        rowServices.initTestData(false, false);
        r = rowServices.createRow();
        r.attach();
        oldValue = (String) r.getValue(3); // equals to "cell3"

/*        try {
            r.setValue(null, 3);
            fail("Trying to assign null value to a not nullable column");
        } catch (ValidateException e) {
            String s = "Value cannot be NULL";
            assertTrue("Trying to assign null value to a not nullable column",
                    e.getMessage().startsWith(s));
        }
 */
        //
        // The row can be modified
        //
        rowServices.initTestData(false, false);
        r = rowServices.createRow();
        r.attach();
        r.setValue("cell0m", 0);
        assertEquals("cell0m", r.getValue(0));
    }

    @Test
    public void testUpdateRowVersions() {
        //
        // if row state is LOADED then RowSate.originalRow holds
        // row's copy
        //
        rowServices.initTestData(true, false);
        DataRow r = rowServices.createRow();
        r.attach();
        DataRow r1 = r.createCopy();
        r.setValue("cell0m", 0);
        assertNull(r.getState().getUpdatingRow());
        boolean b = compare(r1, r.getState().getOriginalRow());
        assertTrue(b);
        //
        // if row state is MANMADE then RowState.updatingRow holds
        // row's copy and RowState.originalRow == null
        //
        rowServices.initTestData(false, false);
        r = rowServices.createRow();
        r.attach();
        r1 = r.createCopy();
        r.setValue("cell0m", 0);
        assertNull(r.getState().getOriginalRow());
        b = compare(r1, r.getState().getUpdatingRow());
        assertTrue(b);


    }

    @Test
    public void testUpdateState() {
        //
        // This is a case when a row added to a table which isLoading
        // state is equal to true. The row now has LOADED state.
        // Then we invoked setValue method. Before actual changing a cell
        // updateRowVersion metod execute saving original data. So, we must
        // set editingState as UPDATED
        //
        rowServices.initTestData(true, false);
        DataRow r = rowServices.createRow();
        r.attach();
        r.setValue("cell0m", 0);
        assertEquals("edditingState must be UPDATED", UPDATED, r.getState().getEditingState());
        //
        // INSERTING state must be retained. We set a table's loading state to false
        //
        rowServices.initTestData(false, false);
        r = rowServices.createRow();
        r.attachNew();
        r.setValue("cell0m", 0);
        assertEquals("edditingState must be INSERTING", INSERTING, r.getState().getEditingState());
        //
        // The method beginEdit() has been applied to the row. It's editing
        // state must be UPDATING after setValue call
        //
        rowServices.initTestData(false, false);
        r = rowServices.createRow();
        r.attach();
        r.beginEdit();
        r.setValue("cell0m", 0);
        assertEquals("edditingState must be UPDATING", UPDATING, r.getState().getEditingState());
        //
        // Table.isLoading == false
        // Method beginEdit() has not been applied to the row.
        // Row's editing state equals neither INSERTING nor MANMADE.
        // New editing state must be set to UPDATED
        //
        rowServices.initTestData(true, false);
        r = rowServices.createRow();
        r.attach();
        rowServices.setLoading(false);
        r.setValue("cell0m", 0);
        assertEquals("edditingState must be UPDATED", UPDATED, r.getState().getEditingState());

    }

    @Test
    public void testBeginEdit() {
        //
        // Table is loading => beginEdit() does nothing
        //
        rowServices.initTestData(true, false);
        DataRow r = rowServices.createRow();
        r.attach();
        r.beginEdit();
        assertEquals("beginEditMode can't be changed", false, r.getState().isBeginEditMode());
        //
        // Table.isLoading == false, but the row is DETACHED => beginEdit does nothing
        //
        rowServices.initTestData(false, false);
        r = rowServices.createRow();
        r.beginEdit();
        assertEquals("RowState is DETACHED. beginEditMode can't be changed", false, r.getState().isBeginEditMode());
        //
        // Table.isLoading == false, the row is either UPDATING or INSERTING  => beginEdit does nothing
        //
        rowServices.initTestData(false, false);
        r = rowServices.createRow();
        r.beginEdit();
        r.setValue("cell0m", 0);
        r.beginEdit();
        assertEquals("RowState is UPDATING. beginEditMode can't be changed", false, r.getState().isBeginEditMode());

        rowServices.initTestData(false, false);
        r = rowServices.createRow();
        r.attachNew();
        assertEquals("RowState is INSERTING. beginEditMode can't be changed", false, r.getState().isBeginEditMode());
        //
        // the row is DELETED  => beginEdit does nothing
        //
        rowServices.initTestData(false, false);
        r = rowServices.createRow();
        r.attach();
        r.delete();
        r.beginEdit();
        assertEquals("RowState is DELETED. beginEditMode can't be changed", false, r.getState().isBeginEditMode());

        //
        // RowState.beginEditMode changes 
        //
        rowServices.initTestData(false, false);
        r = rowServices.createRow();
        r.attach();
        r.beginEdit();
        assertEquals("RowState.beginEditMode must be changed", true, r.getState().isBeginEditMode());
    }

    @Test
    public void testEndEdit() {
        //
        // The row state is LOADED and RowState.beginEditMode == true
        // then originalRow and updatingRow are 
        // both set to null
        //
        rowServices.initTestData(true, false);
        DataRow r = rowServices.createRow();
        r.attach();
        rowServices.setLoading(false);
        r.beginEdit();
        r.endEdit();
        assertNull(r.getState().getOriginalRow());
        assertNull(r.getState().getUpdatingRow());
        assertEquals("Row editing state didn't change", LOADED, r.getState().getEditingState());
        assertEquals("Row iriginal state didn't change", LOADED, r.getState().getOriginalState());

        //
        // The row state is MANMADE and RowState.beginEditMode == true
        // then originalRow and updatingRow are 
        // both set to null
        //
        rowServices.initTestData(false, false);
        r = rowServices.createRow();
        r.attach();
        r.beginEdit();
        r.endEdit();
        assertNull(r.getState().getOriginalRow());
        assertNull(r.getState().getUpdatingRow());
        assertEquals("Row editing state didn't change", MANMADE, r.getState().getEditingState());
        assertEquals("Row iriginal state didn't change", MANMADE, r.getState().getOriginalState());
        //
        // The row state is either LOADED or MANMADE and RowState.beginEditMode == false
        // then method does nothing
        //
        rowServices.initTestData(true, false);
        r = rowServices.createRow();
        r.attach();
        rowServices.setLoading(false);
        r.endEdit();
        assertNull(r.getState().getOriginalRow());
        assertNull(r.getState().getUpdatingRow());
        assertEquals("Row editing state didn't change", LOADED, r.getState().getEditingState());
        assertEquals("Row iriginal state didn't change", LOADED, r.getState().getOriginalState());

        rowServices.initTestData(false, false);
        r = rowServices.createRow();
        r.attach();
        r.endEdit();
        assertNull(r.getState().getOriginalRow());
        assertNull(r.getState().getUpdatingRow());
        assertEquals("Row editing state didn't change", MANMADE, r.getState().getEditingState());
        assertEquals("Row iriginal state didn't change", MANMADE, r.getState().getOriginalState());
        //
        // The row state is INSERTING => editingState becomes MANMADE.
        // beginEditMode == false
        //
        rowServices.initTestData(false, false);
        r = rowServices.createRow();
        r.attachNew();
        r.endEdit();
        assertNull(r.getState().getOriginalRow());
        assertNull(r.getState().getUpdatingRow());
        assertEquals("Row editing state must be MANMADE", MANMADE, r.getState().getEditingState());
        assertEquals("Row oiriginal state must be MANMADE", MANMADE, r.getState().getOriginalState());
        assertFalse(r.getState().isBeginEditMode());
        //
        // The row state is UPDATING and originalState is MANMADE => 
        // editingState becomes MANMADE
        // beginEditMode == false
        //
        rowServices.initTestData(false, false);
        r = rowServices.createRow();
        r.attach();
        r.beginEdit();
        r.setValue("cell0m", 0);
        r.endEdit();
        assertNull(r.getState().getOriginalRow());
        assertNull(r.getState().getUpdatingRow());
        assertEquals("Row editing state must be MANMADE", MANMADE, r.getState().getEditingState());
        assertEquals("Row oiriginal state must be MANMADE", MANMADE, r.getState().getOriginalState());
        assertFalse(r.getState().isBeginEditMode());
        //
        // The row state is UPDATING and originalState is LOADED => 
        // editingState becomes UPDATED
        // beginEditMode == false
        // originalRow != null
        //
        rowServices.initTestData(true, false);
        r = rowServices.createRow();
        r.attach();
        rowServices.setLoading(false);
        r.beginEdit();
        r.setValue("cell0m", 0);
        r.endEdit();
        assertNotNull(r.getState().getOriginalRow());
        assertNull(r.getState().getUpdatingRow());
        assertEquals("Row editing state must be UPDATED", UPDATED, r.getState().getEditingState());
        assertEquals("Row oiriginal state must be LOADED", LOADED, r.getState().getOriginalState());
        assertFalse(r.getState().isBeginEditMode());

    }

    @Test
    public void testCancelEdit() {
        //
        // The row state is LOADED and RowState.beginEditMode == true
        // then originalRow and updatingRow are 
        // both set to null
        //
        rowServices.initTestData(true, false);
        DataRow r = rowServices.createRow();
        r.attach();
        rowServices.setLoading(false);
        r.beginEdit();
        r.cancelEdit();
        assertNull(r.getState().getOriginalRow());
        assertNull(r.getState().getUpdatingRow());
        assertEquals("Row editing state didn't change", LOADED, r.getState().getEditingState());
        assertEquals("Row iriginal state didn't change", LOADED, r.getState().getOriginalState());

        //
        // The row state is MANMADE and RowState.beginEditMode == true
        // then originalRow and updatingRow are 
        // both set to null
        //
        rowServices.initTestData(false, false);
        r = rowServices.createRow();
        r.attach();
        r.beginEdit();
        r.cancelEdit();
        assertNull(r.getState().getOriginalRow());
        assertNull(r.getState().getUpdatingRow());
        assertEquals("Row editing state didn't change", MANMADE, r.getState().getEditingState());
        assertEquals("Row iriginal state didn't change", MANMADE, r.getState().getOriginalState());
        //
        // The row state is either LOADED or MANMADE and RowState.beginEditMode == false
        // then method does nothing
        //
        rowServices.initTestData(true, false);
        r = rowServices.createRow();
        r.attach();
        rowServices.setLoading(false);
        r.cancelEdit();
        assertNull(r.getState().getOriginalRow());
        assertNull(r.getState().getUpdatingRow());
        assertEquals("Row editing state didn't change", LOADED, r.getState().getEditingState());
        assertEquals("Row iriginal state didn't change", LOADED, r.getState().getOriginalState());

        rowServices.initTestData(false, false);
        r = rowServices.createRow();
        r.attach();
        r.cancelEdit();
        assertNull(r.getState().getOriginalRow());
        assertNull(r.getState().getUpdatingRow());
        assertEquals("Row editing state didn't change", MANMADE, r.getState().getEditingState());
        assertEquals("Row iriginal state didn't change", MANMADE, r.getState().getOriginalState());
        //
        // The row state is INSERTING => editingState and originalState both 
        // become DETACHED.
        // beginEditMode == false
        //
        rowServices.initTestData(false, false);
        r = rowServices.createRow();
        r.attachNew();
        r.cancelEdit();
        assertNull(r.getState().getOriginalRow());
        assertNull(r.getState().getUpdatingRow());
        assertEquals("Row editing state must be DETACHED", DETACHED, r.getState().getEditingState());
        assertEquals("Row oiriginal state must be DETACHED", DETACHED, r.getState().getOriginalState());
        assertFalse(r.getState().isBeginEditMode());
        //
        // The row state is UPDATING, originalState is LOADED and
        // updatingRow == null => editingState becomes LOADED. originalRow and
        // updatingRow are both set to null.
        // current row is restored from originalRow
        // beginEditMode == false
        //
        rowServices.initTestData(true, false);
        r = rowServices.createRow();
        r.attach();
        rowServices.setLoading(false);
        r.beginEdit();
        r.setValue("cell0m", 0);
        DataRow r1 = r.getState().getOriginalRow().createCopy();
        r.cancelEdit();
        assertNull(r.getState().getOriginalRow());
        assertNull(r.getState().getUpdatingRow());
        assertEquals("Row editing state must be LOADED", LOADED, r.getState().getEditingState());
        assertEquals("Row oiriginal state must be LOADED", LOADED, r.getState().getOriginalState());
        assertFalse(r.getState().isBeginEditMode());
        assertTrue("Current row is restored from the original row", compare(r1, r));
        //
        // The row state is UPDATING, originalState is LOADED and
        // updatingRow != null => editingState becomes UPDATED. 
        // updatingRow is set to null.
        // current row is restored from updatingRow
        // beginEditMode == false
        //
        rowServices.initTestData(true, false);
        r = rowServices.createRow();
        r.attach();
        rowServices.setLoading(false);
        r.beginEdit();
        r.setValue("cell0m", 0);
        r.endEdit();

        r.beginEdit();
        r.setValue("cell0m1", 0);
        r1 = r.getState().getUpdatingRow().createCopy();

        r.cancelEdit();
        assertNotNull(r.getState().getOriginalRow());
        assertNull(r.getState().getUpdatingRow());
        assertEquals("Row editing state must be UPDATED", UPDATED, r.getState().getEditingState());
        assertEquals("Row oiriginal state must be LOADED", LOADED, r.getState().getOriginalState());
        assertFalse(r.getState().isBeginEditMode());
        assertTrue("Current row is restored from the original row", compare(r1, r));

        //
        // The row state is UPDATING, originalState is MANMADEl => editingState 
        // becomes MANMADE. 
        // updatingRow is set to null.
        // current row is restored from updatingRow
        // beginEditMode == false
        //
        rowServices.initTestData(false, false);
        r = rowServices.createRow();
        r.attach();
        //rowServices.setLoading(false);
        r.beginEdit();
        r.setValue("cell0m", 0);
        r1 = r.getState().getUpdatingRow().createCopy();
        r.cancelEdit();

        assertNull(r.getState().getOriginalRow());
        assertNull(r.getState().getUpdatingRow());
        assertEquals("Row editing state must be MANMADE", MANMADE, r.getState().getEditingState());
        assertEquals("Row oiriginal state must be MANMADE", MANMADE, r.getState().getOriginalState());
        assertFalse(r.getState().isBeginEditMode());
        assertTrue("Current row is restored from the original row", compare(r1, r));

    }

    @Test
    public void testResetRowVersions() {
        //
        // When cancelEdit().
        // The row state is LOADED. originalRow and updatingRow 
        // are both set to null.
        // beginEditMode == false
        //
        rowServices.initTestData(true, false);
        DataRow r = rowServices.createRow();
        r.attach();
        rowServices.setLoading(false);
        r.cancelEdit();
        assertNull("cancelEdit(). originalRow. editingState is LOADED", r.getState().getOriginalRow());
        assertNull("cancelEdit(). updatingRow. editingState uis LOADED", r.getState().getUpdatingRow());
        //
        // When endEdit().
        // The row state is LOADED. originalRow and updatingRow 
        // are both set to null.
        // beginEditMode == false
        //
        rowServices.initTestData(true, false);
        r = rowServices.createRow();
        r.attach();
        rowServices.setLoading(false);
        r.endEdit();
        assertNull("endEdit(). originalRow. editingState is LOADED", r.getState().getOriginalRow());
        assertNull("endEdit(). updatingRow. editingState uis LOADED", r.getState().getUpdatingRow());
        //
        // When cancelEdit().
        // The row state is MANMADE. updatingRow 
        // is set to null.
        // beginEditMode == false
        //
        rowServices.initTestData(false, false);
        r = rowServices.createRow();
        r.attach();
        //rowServices.setLoading(false);
        r.cancelEdit();
        //assertNull("cancelEdit(). originalRow. editingState is MANMADE",r.getState().getOriginalRow());
        assertNull("cancelEdit(). updatingRow. editingState is MANMADE", r.getState().getUpdatingRow());
        //
        // When endEdit().
        // The row state is MANMADE. updatingRow 
        // is set to null.
        // beginEditMode == false
        //
        rowServices.initTestData(false, false);
        r = rowServices.createRow();
        r.attach();
        //rowServices.setLoading(false);
        r.endEdit();
        //assertNull("endEdit(). originalRow. editingState is MANMADE",r.getState().getOriginalRow());
        assertNull("endEdit(). updatingRow. editingState is MANMADE", r.getState().getUpdatingRow());
        //
        // When cancelEdit().
        // The row state is UPDATED. updatingRow 
        // is set to null.
        // beginEditMode == false
        //
        rowServices.initTestData(true, false);
        r = rowServices.createRow();
        r.attach();
        rowServices.setLoading(false);
        r.beginEdit();
        r.setValue("cell0m", 0);
        r.endEdit();
        r.beginEdit();
        r.setValue("cell0m1", 0);
        r.cancelEdit();


        assertNotNull("cancelEdit(). originalRow. editingState is LOADED", r.getState().getOriginalRow());
        assertNull("cancelEdit(). updatingRow. editingState uis LOADED", r.getState().getUpdatingRow());

        //
        // When endEdit().
        // The row state is UPDATED. updatingRow 
        // is set to null.
        // beginEditMode == false
        //
        rowServices.initTestData(true, false);
        r = rowServices.createRow();
        r.attach();
        rowServices.setLoading(false);
        r.beginEdit();
        r.setValue("cell0m", 0);
        r.endEdit();
        r.beginEdit();
        r.setValue("cell0m1", 0);
        r.endEdit();


        assertNotNull("cancelEdit(). originalRow. editingState is LOADED", r.getState().getOriginalRow());
        assertNull("cancelEdit(). updatingRow. editingState uis LOADED", r.getState().getUpdatingRow());
    }

    @Test
    public void testDelete() {
        //
        // The row state is DETACHED => exception is thrown
        //
        rowServices.initTestData(true, false);
        DataRow r = rowServices.createRow();
        try {
            r.delete();
            fail("The row is allready deleted()");
        } catch (DataOperationException e) {
            assertEquals("delete{} method can't be applied to a row in DETACHED state", e.getMessage());
        }
        //
        // Row state is INSERTING => state becomes DELETED
        //
        rowServices.initTestData(false, false);
        r = rowServices.createRow();
        r.attachNew();
        r.delete();
        assertEquals("delete when INSERTING", DETACHED, r.getState().getEditingState());
        //
        // Row state is not INSERTING => state becomes DETACHED
        //
        rowServices.initTestData(false, false);
        r = rowServices.createRow();
        r.attach();
        r.delete();
        assertEquals("delete when not INSERTING", DELETED, r.getState().getEditingState());

        rowServices.initTestData(true, false);
        r = rowServices.createRow();
        r.attach();
        rowServices.setLoading(false);
        r.delete();
        assertEquals("delete when not INSERTING", DELETED, r.getState().getEditingState());

        rowServices.initTestData(true, false);
        r = rowServices.createRow();
        r.attach();
        rowServices.setLoading(false);
        r.beginEdit();
        r.delete();
        assertEquals("delete when not INSERTING", DELETED, r.getState().getEditingState());

        rowServices.initTestData(true, false);
        r = rowServices.createRow();
        r.attach();
        rowServices.setLoading(false);
        r.beginEdit();
        r.setValue("cell0m", 0);
        r.delete();
        assertEquals("delete when not INSERTING", DELETED, r.getState().getEditingState());

        rowServices.initTestData(true, false);
        r = rowServices.createRow();
        r.attach();
        rowServices.setLoading(false);
        r.beginEdit();
        r.setValue("cell0m", 0);
        r.endEdit();
        r.delete();
        assertEquals("delete when not INSERTING", DELETED, r.getState().getEditingState());

    }

    public boolean compare(DataRow r1, DataRow r2) {
        boolean result = true;
        if (r1 == null && r2 == null) {
            result = true;
        } else if (r1 == null || r2 == null) {
            result = false;
        } else {
            DefaultDataCellCollectionStub pr1 = (DefaultDataCellCollectionStub) ((DefaultDataRow) r1).getCellCollection();
            DefaultDataCellCollectionStub pr2 = (DefaultDataCellCollectionStub) ((DefaultDataRow) r2).getCellCollection();
            List l1 = pr1.getCells();
            List l2 = pr2.getCells();
            for (int i = 0; i < l1.size(); i++) {
                if (l1.get(i) == null && l2.get(i) == null) {
                    continue;
                }
                if (l1.get(i) == null || l2.get(i) == null) {
                    result = false;
                    break;
                }

                if (!l1.get(i).equals(l2.get(i))) {
                    result = false;
                    break;
                }
            }

        }
        return result;
    }

    protected void printRow(DataRow r) {
        DefaultDataCellCollectionStub pr1 = (DefaultDataCellCollectionStub) ((DefaultDataRow) r).getCellCollection();
        List l1 = pr1.getCells();

        String s = "";
        for (int i = 0; i < l1.size(); i++) {
            s += "[" + l1.get(i) + "],";
        }

    }

    public class DataRowTableContext extends tdo.service.AbstractTableServices {

        boolean isLoading;
        boolean isEditProhibited;
        boolean[] editableCells = new boolean[]{true, true, false, true, true};
        boolean[] nullableCells = new boolean[]{true, true, true, false, true};
        Map<Integer, Integer> calcColumns;
        Map<String, Integer> columnNameMap;
        HashMap<DataRow, Integer> rowMap;
        int columnCount;
        private DataRowServices dataRowService;
        private CellServices cellService;
        DefaultDataRowProviderTestImpl rowProvider;

        public DataRowTableContext() {

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
            dataRowService = new DataRowServices(this);
            //rowProvider = new DefaultDataRowProviderTestImpl(this, columnCount, calcColumns, columnNameMap);
            rowProvider = new DefaultDataRowProviderTestImpl(this);
            this.cellService = new CellServicesTstImpl(this);
        }

        @Override
        public DataRowServices getDataRowServices() {
            return (DataRowServices) this.dataRowService;
        }

        @Override
        public CellServices getCellServices() {
            return (CellServices) this.cellService;
        }

        @Override
        public CellCollectionServices getCellCollectionServices() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public class DataRowServices implements tdo.service.DataRowServices {

        DataRowTableContext context;

        public DataRowServices(DataRowTableContext context) {
            
            this.context = context;
        }

        public void initTestData(boolean isLoading, boolean isEditProhibited) {
            context.isLoading = isLoading;
            context.isEditProhibited = isEditProhibited;
        }

        public void initTestData(boolean isLoading, boolean isEditProhibited, boolean[] editableCells, boolean[] nullableCells, Map<String, Integer> columnNameMap,
                Map<DataRow, Integer> rowMap) {
            context.isLoading = isLoading;
            context.isEditProhibited = isEditProhibited;
            context.editableCells = editableCells;
            context.nullableCells = nullableCells;
            context.columnNameMap = columnNameMap;
            context.rowMap = (HashMap) rowMap;
        }

        public void initTestData(boolean isLoading, boolean isEditProhibited, boolean[] editableCells, boolean[] nullableCells, Map<String, Integer> columnNameMap,
                Map<DataRow, Integer> rowMap, DataRowProvider rowProvider) {
            this.initTestData(isLoading, isEditProhibited, editableCells, nullableCells, columnNameMap, rowMap);
            context.rowProvider = (DefaultDataRowProviderTestImpl) rowProvider;

        }

        public void setLoading(boolean loading) {
            context.isLoading = loading;
        }

        @Override
        public boolean isLoading() {
            return context.isLoading;
        }

        @Override
        public boolean isEditProhibited() {
            return context.isEditProhibited;
        }

        @Override
        public boolean isCellEditable(int columnIndex) {
            return context.editableCells[columnIndex];
        }

        @Override
        public boolean isNullable(int columnIndex) {
            return context.nullableCells[columnIndex];
        }

        @Override
        public int getColumnIndex(String columnName) {
            return context.columnNameMap.get(columnName);
        }

        @Override
        public int getRowIndex(DataRow row) {

            return 0;
        }

        @Override
        public void fireRowEditing(int cause, DataRow row, int columnIndex) {
        }

        @Override
        public void fireRowEditing(int cause, DataRow row) {
        }


        @Override
        public boolean isCalculated(int columnIndex) {
            return false;
        }

        @Override
        public Object calculateColumnValue(DataRow row, int columnIndex) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public Object toColumnType(Object value, int columnIndex) {
            return value;
        }

        @Override
        public DataRow createRow() {
            DataRow r = context.rowProvider.createRow();
            return r;
        }

        @Override
        public boolean validate(DataRow row, int columnIndex, Object value) {
            return true;
        }

        @Override
        public boolean validate(DataRow row) {
            return true;
        }
    }

    public class CellServicesTstImpl implements tdo.service.CellServices {

        DataRowTableContext context;

        public CellServicesTstImpl(TableServices context) {
            this.context = (DataRowTableContext)context;
        }

        @Override
        public int getCellIndex(int columnIndex) {
            return columnIndex;
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
    }
}