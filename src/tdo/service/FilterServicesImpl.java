/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.service;

import tdo.DataRow;
import tdo.Table;

/**
 *
 * @author Valery
 */
public class FilterServicesImpl implements FilterServices{
    protected Table table;

    public FilterServicesImpl(Table table) {
        this.table = table;
    }

    @Override
    public DataRow getRow(int rowIndex) {
        return this.table.getRow(rowIndex);
    }

    @Override
    public String getTableName() {
        return this.table.getTableName();
    }

    @Override
    public Class getColumnType(String columnName) {
        return table.getColumns().get(columnName).getType();
    }

    @Override
    public int getRowCount() {
        return this.table.getRowCount();
    }
}
