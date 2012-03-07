/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.support;

import tdo.*;

/**
 *
 * @author Valery
 */
public class ColumnCollectionServicesTst implements tdo.service.ColumnServices{
    
    public ColumnCollectionServicesTst() {
        
    }
    
    @Override
    public boolean isCellEditable(int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isNullable(int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isCalculated(int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object calculateColumnValue(DataRow row, int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getColumnIndex(String columnName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getColumnCount(int kind) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getColumnCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasColumns() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DataColumn columns(int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DataColumn columns(String columnName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
