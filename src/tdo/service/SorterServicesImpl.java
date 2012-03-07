/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.service;

import tdo.DataColumn;
import tdo.DataRow;
import tdo.Table;

/**
 *
 * @author Valery
 */
public class SorterServicesImpl implements SorterServices{
    protected Table table;

    //protected SortServicesImpl() {
    //}

    public SorterServicesImpl(Table table) {
        this.table = table;
    }

    @Override
    public DataRow getRow(int rowIndex) {
        return this.table.getRow(rowIndex);
    }


    @Override
    public int getRowCount() {
        return this.table.getRowCount();
    }

    @Override
    public int find(DataRow row) {
        return this.table.find(row);
    }

    @Override
    public int getColumnIndex(String columnName) {
        return this.table.getColumns().find(columnName);
    }

    @Override
    public DataColumn getColumn(int columnIndex) {
        return this.table.getColumns().get(columnIndex);
    }

    @Override
    public void setRow(DataRow row, int rowIndex) {
        this.table.setRow(row, rowIndex);
    }

}
