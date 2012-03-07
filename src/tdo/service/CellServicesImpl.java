/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.service;

import tdo.DataColumn;
import tdo.Table;

/**
 *
 * @author valery
 */
public class CellServicesImpl implements CellServices{
    protected Table table; 
    
    public CellServicesImpl(Table table) {
        this.table = table;
    }

    @Override
    public int getCellIndex(int columnIndex) {
        return table.getColumns().get(columnIndex).getCellIndex();
    }

    @Override
    public Object createBlankObject(int columnIndex) {
        return table.getColumns().get(columnIndex).createBlankObject();
    }

    @Override
    public int getColumnIndex(String columnName) {
        return table.getColumns().find(columnName);
    }

    @Override
    public int getColumnCount() {
        return table.getColumns().getCount();
    }

    @Override
    public boolean isDataKind(int columnIndex) {
        return table.getColumns().get(columnIndex).getKind() == DataColumn.DATA_KIND;
    }

    @Override
    public String getPropertyName(int columnIndex) {
        return table.getColumns().get(columnIndex).getPropertyName();
    }

    @Override
    public String getFieldName(int columnIndex) {
        return table.getColumns().get(columnIndex).getFieldName();
    }

    @Override
    public boolean copyCell(int columnIndex, Object source, Object target) {
        return table.getColumns().get(columnIndex).copy(source, target);
    }

    @Override
    public int columnIndexByCell(int cellIndex) {
        int c = -1;
        for ( int i=0; i < table.getColumns().getCount(); i++ ) {
            if ( table.getColumns().get(i).getCellIndex() == cellIndex ) {
                c = i;
                break;
            }
        }
        return c;
    }

}
