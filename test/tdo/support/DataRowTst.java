/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.support;

import tdo.*;
import tdo.service.TableServices;

/**
 *
 * @author Valery
 */
public class DataRowTst implements DataRow{
    //private TableServices context;
    Object testValue;
    @Override
    public TableServices getContext() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getIndex() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DataCellCollection getCellCollection() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RowState getState() {
        return null;
    }

    @Override
    public void setReadOnly(boolean readOnly) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isReadOnly() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void attach() {
    }

    @Override
    public void attachNew() {
    }

    @Override
    public Object getValue(int columnIndex) {
        return this.testValue;
    }

    @Override
    public Object getValue(String columnName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setValue(Object value, int columnIndex) {
        this.testValue = value;
    }

    @Override
    public void setValue(Object value, String columnName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void beginEdit() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void endEdit() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void cancelEdit() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void delete() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void copyFrom(DataRow row) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void copyFrom(Object[] source) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DataRow createCopy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DataRow createCopyOf(DataRow row) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
