package tdo;

import tdo.event.DataColumnCollectionEvent;
import tdo.event.DataColumnCollectionListener;
import tdo.event.TableEvent;
import tdo.event.TableEvent.TableEventCause;

public class DataColumnCollectionHandler implements DataColumnCollectionListener {
    private Table table;
    private DataRowCollection rows;

    /** Creates a new instance of DataColumnCollectionHandler
     * @param table
     * @param rows
     */
    public DataColumnCollectionHandler(Table table, DataRowCollection rows) {
        this.table = table;
        this.rows = rows;
    }

    @Override
    public void columnsChanged(DataColumnCollectionEvent e) {


//            if (rows == null) {
//                return;
//            }

        if (e.getColumnsAction() == DataColumnCollectionEvent.COLUMN_ADDED) {
            DataColumn column = e.getColumn();
            if (column.getKind() == DataColumn.DATA_KIND) {
                columnAdded(e.getColumnIndex());
            }
        }

        if (e.getColumnsAction() == DataColumnCollectionEvent.COLUMN_REMOVED) {
            DataColumn column = e.getColumn();
            if (column.getKind() == DataColumn.DATA_KIND) {
                columnRemoved(e.getColumnIndex());
            }
        }
        if (e.getColumnsAction() == DataColumnCollectionEvent.COLUMN_MOVED) {
            DataColumn column = e.getColumn();
            if (column.getKind() == DataColumn.DATA_KIND) {
                columnMoved(column, e.getColumnIndex());
            }
        }
        if (!table.isLoading()) {
            //fireDataModelStructure(AbstractTable.this);
            TableEvent te = new TableEvent(table, TableEvent.ALL_ROWS,TableEvent.ALL_COLUMNS, TableEventCause.schema);
            table.fireDataModel(te);

        }
    }

    protected void columnAdded(int columnIndex) {

        updateCellIndexes();
        // ¬о все р€ды хранилища добавл€ем элемент, соответствующий типу добавленной колонки
        if (rows != null && rows.getCount() > 1) {
            adjustRowsOnInsertColumn(columnIndex);
        }
    }

    protected void adjustRowsOnInsertColumn(int columnIndex) {

        DataRow bRow;
        for (int i = 0; i < rows.getCount(); i++) {
            bRow = rows.get(i);
            bRow.getCellCollection().columnAdded(columnIndex);
            bRow.getState().columnAdded(columnIndex);

        } //for

    }

    protected void columnRemoved(int columnIndex) {
        updateCellIndexes();
        if (rows != null && rows.getCount() > 1) {
            adjusRowstOnRemoveColumn(columnIndex);
        }
    }

    private void adjusRowstOnRemoveColumn(int columnIndex) {

        DataRow bRow;
        for (int i = 0; i < rows.getCount(); i++) {
            bRow = rows.get(i);
            bRow.getCellCollection().columnRemoved(columnIndex);
            bRow.getState().columnRemoved(columnIndex);
        } //for

    }

    protected void columnMoved(DataColumn column, int newColumnIndex) {
        int oldCellIndex = column.getCellIndex();
        updateCellIndexes();
        int newCellIndex = column.getCellIndex();
        // ƒл€ всех р€дов хранилища копируем элемент, соответствующий перемещаемой колонки
        if (rows != null && rows.getCount() > 1) {
            adjustRowsOnMoveColumn(column, newColumnIndex, oldCellIndex, newCellIndex);
        }

    }

    private void adjustRowsOnMoveColumn(DataColumn column, int newColumnIndex, int oldCellIndex, int newCellIndex) {

        DataRow bRow;
        for (int i = 0; i < rows.getCount(); i++) {
            bRow = rows.get(i);
            bRow.getCellCollection().columnMoved(newColumnIndex, oldCellIndex, newCellIndex);
            bRow.getState().columnMoved(newColumnIndex, oldCellIndex, newCellIndex);
        } //for

    }
    protected void updateCellIndexes() {
        int n = 0;

        for ( int i=0; i < table.getColumns().getCount(); i++ ) {
            DataColumn dc = table.getColumns().get(i);

            if ( dc.getKind() == DataColumn.DATA_KIND ) {
                dc.setCellIndex(n);
                n++;
            } else {
                dc.setCellIndex(-1);
            }
        }

    }

} //class
