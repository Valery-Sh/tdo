/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.service;

import java.io.Serializable;
import tdo.DataColumn;
import tdo.DataRow;
import tdo.Table;

/**
 *
 * @author valery
 */
public class ColumnServicesImpl implements ColumnServices, Serializable{
    protected Table table; 
    
    public ColumnServicesImpl(Table table) {
        this.table = table;
    }
    @Override
    public boolean isCellEditable(int columnIndex) {
        return table.isCellEditable(columnIndex);
    }

    @Override
    public boolean isNullable(int columnIndex) {
        return table.getColumns().get(columnIndex).isNullable();
    }

    @Override
    public boolean isCalculated(int columnIndex) {
        return table.getColumns().get(columnIndex).getKind() != DataColumn.DATA_KIND;
    }

//    Object calculateColumnValue(DataRow row, int columnIndex);

    @Override
    public int getColumnIndex(String columnName) {
        return table.getColumns().find(columnName);
    }

    @Override
    public int getColumnCount(int kind) {
        return table.getColumns().getCount(kind);
    }
    @Override
    public int getColumnCount() {
        return table.getColumns().getCount();
    }

    @Override
    public DataColumn columns(int columnIndex) {
        return table.getColumns().get(columnIndex);
    }

    @Override
    public DataColumn columns(String columnName) {
        return table.getColumns().get(columnName);
    }

    @Override
    public Object calculateColumnValue(DataRow row, int columnIndex) {
        return table.calculateColumnValue(row, columnIndex);
    }

    @Override
    public boolean hasColumns() {
        return table.getColumns()==null ? false : true;
    }

    
}
