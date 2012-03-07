/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.impl;

import tdo.*;


/**
 *
 * @author valery
 */
public class SimpleInternalViewImpl implements InternalView{
    protected DataRowCollection currentRows;
    protected DataRowCollection originalRows;

    protected SimpleTable table;
    protected ViewManager viewManager;
    //public SimpleInternalViewImpl(Table table, DataRowCollection originalRows) {
    public SimpleInternalViewImpl(Table table ) {
        this(table,null);
    }
    public SimpleInternalViewImpl(Table table, DataRowCollection tableRows) {
        this.table = (SimpleTable)table;
        if ( tableRows == null ) {
            this.originalRows = new DefaultDataRowCollection();
        } else {
            this.originalRows = tableRows;
        }
        ((DefaultDataRowCollection)this.originalRows).setContext(table.getContext());
        viewManager = new InternalViewManager(table.getContext(),originalRows);
        this.currentRows = viewManager.getCurrentRows();
    }

    /**
     *
     * @return
     */
    @Override
    public DataRowCollection getCurrentRows() {
        return this.getViewManager().getCurrentRows();
    }

    @Override
    public DataRowCollection getTableRows() {
        return this.originalRows;
    }
    @Override
    public Table getTable() {
        return this.table;
    }
    @Override
    public int getRowCount() {
        return this.getCurrentRows().getCount();
    }

    @Override
    public DataRow getRow(int rowIndex) {
        return this.getCurrentRows().get(rowIndex);
    }

    @Override
    public void setRow(DataRow row, int rowIndex) {
        this.getCurrentRows().set(rowIndex,row);
    }


    @Override
    public int find(DataRow row) {
        //return ((SimpleTable)this.table).find(row,currentRows);
        return getCurrentRows().indexOf(row);
    }

    @Override
    public int addRow(DataRow row) {
        int rowIndex = getCurrentRows().add(row);
        updateOriginalRows(row);
        return rowIndex;

    }

    @Override
    public DataRow addRow() {
        if (table.isLoading()) {
            throw new IllegalArgumentException("SimpleInternalView#addRow() : Table cannot be in loading state");
        }
        DataRow row = getCurrentRows().add();
        return row;

    }

    @Override
    public void insertRow(int rowIndex) {
        if (table.isLoading()) {
            throw new IllegalArgumentException("SimpleInternalView#inserRow(int) : Table cannot be in loading state");
        }

        getCurrentRows().insert(rowIndex);
        DataRow row = getCurrentRows().get(rowIndex);

    }

    @Override
    public void insertRow(int rowIndex, DataRow row) {
        getCurrentRows().insert(rowIndex, row);
        updateOriginalRows(row);
    }

    @Override
    public DataRow deleteRow(int rowIndex) {
        DataRow row = getCurrentRows().get(rowIndex);
        RowState rs = row.getState();

        if (!table.isDeletedHidden()) {
            return row;
        }

        getCurrentRows().delete(rowIndex);
        deleteOriginalRows(row);
        return row;
    }

    @Override
    public void cancelInserting(int rowIndex) {
        getCurrentRows().delete(rowIndex);
    }

    protected void updateOriginalRows(DataRow row) {
    }
    protected void deleteOriginalRows(DataRow row) {
    }

/*    @Override
    public int getKind(DataRowCollection rows) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
*/
    @Override
    public ViewManager getViewManager() {
        return viewManager;
    }

}
