
package tdo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import tdo.service.TableServices;

/**
 *
 */
public class DefaultDataCellCollectionStub implements tdo.DataCellCollection {

    private TableServices context;
    private List cells;
    private Map<Integer, Integer> calcColumns;
    private Map<String, Integer> columnNameMap;
    private int columnCount;

    public DefaultDataCellCollectionStub(TableServices context, int columnCount,
            Map<Integer, Integer> calcColumns, Map<String, Integer> columnNameMap) {
        this.context = context;
        this.columnCount = columnCount;
        this.calcColumns = calcColumns;
        this.columnNameMap = columnNameMap;

        this.cells = new ArrayList();
        for (int i = 0; i < columnCount; ++i) {
            cells.add("cell" + i);
        }
    }

    /**
     *  опирует содержимое €чеек заданного р€да в €чейки текущего р€да.
     * —осто€ние, определ€емое свойством <code>state</code>, а
     * также пол€ и свойства текущего р€да не измен€ютс€.
     * @param source р€д-источник €чеек дл€ копировани€
     */
    @Override
    public void copyCells(DataCellCollection source) {
        //DefaultDataCellCollectionStub dfrom = (DefaultDataCellCollectionStub) ((DefaultDataRow) source).getCellCollection();
        DefaultDataCellCollectionStub dfrom = (DefaultDataCellCollectionStub) source;
        for (int i = 0; i < dfrom.getCells().size(); i++) {
            getCells().set(i, dfrom.getCells().get(i));
        }

    }

    public void copyCells(Object[] source) {

        for (int i = 0; i < source.length; i++) {
            getCells().set(i, source[i]);
        }

    }

    public List getCells() {
        return this.cells;
    }

    protected void setCells(List cells) {
        this.cells = cells;
    }

    @Override
    public Object getValue(int columnIndex) {
        if (!context.getCellServices().isDataKind(columnIndex)) {
            return new Integer(columnIndex * 2);
        }
        return cells.get(columnIndex);
    }

    @Override
    public Object getValue(String columnName) {
        return getValue(context.getCellServices().getColumnIndex(columnName));
    }

    @Override
    public Object setValue(Object value, int columnIndex) {
        Object oldValue = cells.get(columnIndex);
        if (this.calcColumns.containsKey(columnIndex) && this.calcColumns.get(columnIndex) == DataColumn.CALC_KIND) {
            return null;
        }
        cells.set(columnIndex, value);
        return oldValue;
    }

    @Override
    public Object setValue(Object value, String columnName) {
        return setValue(value, context.getColumnServices().getColumnIndex(columnName));
    }

    @Override
    public void columnAdded(int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void columnRemoved(int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void columnMoved(int columnIndex, int oldCellIndex, int newCellIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
