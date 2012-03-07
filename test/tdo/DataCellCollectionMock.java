/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo;

import java.util.ArrayList;
import java.util.List;
import tdo.service.TableServices;

/**
 *
 * @author Valery
 */
public class DataCellCollectionMock implements DataCellCollection {



    private TableServices context;
    private List cells;

    public DataCellCollectionMock(TableServices context) {
        this.context = context;

        this.cells = new ArrayList();
            cells.add("cell0");
            cells.add("cell1");
            
    }

    /**
     *  опирует содержимое €чеек заданного р€да в €чейки текущего р€да.
     * —осто€ние, определ€емое свойством <code>state</code>, а
     * также пол€ и свойства текущего р€да не измен€ютс€.
     * @param source р€д-источник €чеек дл€ копировани€
     */
    @Override
    public void copyCells(DataCellCollection source) {
    }

    public void copyCells(Object[] source) {
    }

    public List getCells() {
        return this.cells;
    }

    protected void setCells(List cells) {
        this.cells = cells;
    }

    @Override
    public Object getValue(int columnIndex) {
        return null;
    }

    @Override
    public Object getValue(String columnName) {
        return null;
    }

    @Override
    public Object setValue(Object value, int columnIndex) {
        return null;
    }

    @Override
    public Object setValue(Object value, String columnName) {
        return null;
    }

    @Override
    public void columnAdded(int columnIndex) {
    }

    @Override
    public void columnRemoved(int columnIndex) {
    }

    @Override
    public void columnMoved(int columnIndex, int oldCellIndex, int newCellIndex) {
    }

}
