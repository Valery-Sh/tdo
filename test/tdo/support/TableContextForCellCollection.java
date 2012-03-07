/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.support;

import tdo.service.CellCollectionServices;
import tdo.service.CellServices;
import tdo.service.ColumnServices;
import tdo.service.CoreServices;
import tdo.service.DataRowServices;
import tdo.service.DataRowViewServices;
import tdo.service.RowCollectionServices;

/**
 *
 * @author Valery
 */
public class TableContextForCellCollection implements tdo.service.TableServices{

    @Override
    public CoreServices getCoreServices() {
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

    @Override
    public CellServices getCellServices() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public CellCollectionServices getCellCollectionServices() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
