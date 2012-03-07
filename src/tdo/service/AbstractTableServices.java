/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.service;

/**
 *
 * @author valery
 */
public abstract class AbstractTableServices implements TableServices{

    @Override
    public CoreServices getCoreServices() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CellServices getCellServices() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ColumnServices getColumnServices() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DataRowServices getDataRowServices() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public DataRowViewServices getDataRowViewServices() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RowCollectionServices getRowCollectionServices() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
