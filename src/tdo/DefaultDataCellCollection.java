/*
 */

package tdo;

import java.util.ArrayList;
import java.util.List;
import tdo.service.TableServices;

/**
 *      ����� ������������ ������ �����, ������� �������� � ������ ����
 * <code>java.util.ArrayList</code>. 
 * ������ ���, ������������  {@link tdo.Table} �������� ����������� ���������
 * <code>DataCellCollection</code>.
 * 
 * <p>���� ������� {@link tdo.Table} ����� <code><b>n</b></code> ������� �� �������
 * <code><b>k</b></code> ������� ����� �������� �������� <code>kind</code>
 * ������ {@link tdo.DataColumn#DATA_KIND}, �� ������ ����� ����� ����� 
 * <code><b>k</b></code>. �� ���������, ����������� ������� ������������ �������
 * ������� � ��������� ������� ������� ������ � ��������� �����:
 * <ol>
 *   <li>���� ��� ������ � �������� <code><i>columnIndex</i></code>
 *       ��������� ������� getKind() == DataColumn.CALC_KIND, �� ��� ��� ���
 *       ��������������� ������ � ��������� �����. 
 *   </li>
 *   <li>���� ��� ������� � �������� <code><i>columnIndex</i></code>
 *       ��������� ������� getKind() == DataColumn.DATA_KIND, �� �� 
 *       ������������� ������ ������ <code><i>columnIndex - calcCount</i><code>,
 *       ��� <i><code>calcKind</code></i> - ��� ���������� �������, ������
 *       ������� ������ <code>columnIncex</code> � ��� ������� 
 *       getKind() != DataColumn.DATA_KIND.
 *   </li>
 * </ol>
 * 
 * <p>����� �������, ����������� ������� �� ����� ���� ��������� ������.
 * 
 * <p>��������� ����� ������� � �������� <code>tdo.Table</code>. ��� ����� 
 * ����������� ����� ������������� �������� 
 * {@link tdo.service.TableServices#getCellServices()).
 */
public class DefaultDataCellCollection implements DataCellCollection{
    /**
     * ��������� ����� ����
     */
    protected List cells;

    /**
     * �������� �������.
     */
    protected TableServices context;
    /**
     * ���, ��� �������� ������� ��������� �����.
     */
    //protected DataRow row;

    private DefaultDataCellCollection() {
    }
    
    /**
     * ������� ��������� ������ ��� ��������� ���������.
     * 
     * @param context
     */
    public DefaultDataCellCollection(TableServices context) {
        this.context = context;
        //this.row = row;
        int cc = context.getCellServices().getColumnCount();
        this.cells = new ArrayList(cc);
        for (int i = 0; i < cc; ++i) {
            if ( context.getCellServices().isDataKind(i) ) {
                cells.add(context.getCellServices().createBlankObject(i));
            }
        }
    }

    /**
     * ������� ��������� ������ ��� ��������� ���� � ��������� ��������� � 
     * ��������� ������ ���������� �� ��������� �������, ��������� ��������� � 
     * �������� �������.
     * ������ <code>values</code> ������ ����� ������ ������ ����� ������� �
     * ��������� ������� <code>tdo.Table</code> � <i>code>i-�</code></i> �������
     * ������� ������������� <i>code>i-�</code></i> �������.
     * 
     * @param context �������� �������� �������. 
     * @param row ���, ��� �������� ��������� ��������� �����
     * @param values ������ �������� �����
     * @param start ��������� ������ �������
     * @param end �������� ������ �������
     */
/*    protected DefaultDataCellCollection(TableServices context, DataRow row, Object[] values, int start, int end) {
        //this.table = table;
        //this.table = table;
        this.context = context;        
        this.row = row;
        
        this.cells = new ArrayList(context.getColumnServices().getColumnCount(DataColumn.DATA_KIND));

        for (int i = start; i < end; ++i) {
            int kind = context.getColumnServices().columns(i - start).getKind();
            if (kind == DataColumn.DATA_KIND) {
                cells.add(values[i]);
            }
        }
    }
 */ 
    /**
     * �������� ���������� ����� ��������� ���� � ������ ������� ��������� ����.
     * ���������, ������������ ��������� <code>state</code>, �
     * ����� ���� � �������� ���� <i><code>to</code</i> �� ����������.
     * 
     * @param target ��� ���������� ��� �����������
     * @param source ���-�������� ����� ��� �����������
     * @see #copyCells(DataRow)
     */
/*    private void copyCells(DataRow target, DataRow source) {
        DefaultDataCellCollection dto = (DefaultDataCellCollection) ((DataRow) target).getCellCollection();
        DefaultDataCellCollection dfrom = (DefaultDataCellCollection) ((DataRow) source).getCellCollection();
        for (int i = 0; i < dfrom.getCells().size(); i++) {
            dto.getCells().set(i, dfrom.getCells().get(i));
        }
    }
 */ 
    /**
     * �������� ���������� ����� �������� ��������� � ������ ������� ���������.
     * @param source ���������-�������� ����� ��� �����������. ����� �������
     *      ������ ���� <code>DefaultDataCellCollection</code> ��� �����������
     *      �� ����.
     */
    @Override
    public void copyCells(DataCellCollection source) {
/*        DefaultDataCellCollection dfrom = (DefaultDataCellCollection)source;        
        for (int i = 0; i < dfrom.getCells().size(); i++) {
            Object from = dfrom.getCells().get(i);
            Object to = getCells().get(i);
            if ( ! context.getCellServices().copyCell(from,to));
                getCells().set(i, dfrom.getCells().get(i));
        }
 */ 

        for (int i = 0; i < context.getCellServices().getColumnCount(); i++) {
            if (context.getCellServices().isDataKind(i)) {
            if ( ! context.getCellServices().copyCell(i,source.getValue(i),getValue(i)) )
                this.setValue(source.getValue(i), i);
            }
        }

    }
    /**
     * �������� ��������� ��������� ������� �������� �������� � ��������� �����.
     * i-� ������� ������� ������������ �� ����� i-�� �������� ������ �����. 
     * ��������� ������ �� ����������.
     * @param source ������-�������� ������� �����
     */
    @Override
    public void copyCells(Object[] source) {
      
        for (int i = 0; i < source.length; i++) {
            int columnIndex = context.getCellServices().columnIndexByCell(i);
            if ( ! context.getCellServices().copyCell(columnIndex, source[i], getValue(i)))
                 getCells().set(i, source[i]);
        }
    }

    /**
     * @return ������ �����.
     */
    protected List getCells() {
        return this.cells;
    }
    /**
     * ������������� ������ �����
     * @param cells ����� ������
     */
    protected void setCells(List cells) {
        this.cells = cells;
    }
    /**
     * ���������� �������� ������ �� ��������� ������� �������.
     * ������� <code>columnIndex</code> �� ������ ���� �����������, 
     * � ��������� ������ ����� ��� �������� ���������� ��� ��������� �����
     * ��������� �� �������������. ��� ��������������� ������ 
     * <code>DataRow</code> �� ��������� ����� �����.
     * @param columnIndex ������ �������, ��� ������� ������������ �������� 
     * ������.
     * @return �������� ������
     */
    @Override
    public Object getValue(int columnIndex) {
/*        int kind = context.getColumnServices().columns(columnIndex).getKind();
        if (kind == DataColumn.CALC_KIND) {
            return context.getColumnServices().calculateColumnValue(row, columnIndex);
        }
*/
        int mappedIndex = context.getCellServices().getCellIndex(columnIndex);
        return cells.get(mappedIndex);
    }
    /**
     * ���������� �������� ������ �� ��������� ����� �������.
     * ������� � ������ <code>columnName</code> �� ������ ���� �����������, 
     * � ��������� ������ ����� ��� �������� ���������� ��� ��������� �����
     * ��������� �� �������������. ��� ��������������� ������ 
     * <code>DataRow</code> �� ��������� ����� �����.
     * 
     * 
     * @param columnName ��� �������, ��� ������� ������������ �������� 
     *    ������.
     * @return �������� ������
     */
    @Override
    public Object getValue(String columnName) {
        return getValue( context.getCellServices().getColumnIndex(columnName) );
    }

    /**
     * ������������� ����� �������� ������ �� ��������� ������� �������.
     * ������� <code>columnIndex</code> �� ������ ���� �����������.
     * @param value ����� �������� ������
     * @param columnIndex ������ �������, ��� ������� ������������ �������� 
     *    ������.
     * @return ���������� �������� ������ �� ��������� ������
     */
    @Override
    public Object setValue(Object value, int columnIndex) {

        int mapped = context.getCellServices().getCellIndex(columnIndex);
        if (mapped == -1) {
            return null;
        }
        Object oldValue = cells.get(mapped);
        cells.set(mapped, value);
        return oldValue;
    }

    /**
     * ������������� ����� �������� ������ �� ��������� ����� �������.
     * ������� <code>columnName</code> �� ������ ���� �����������.
     * @param value ����� �������� ������
     * @param columnName ��� �������, ��� ������� ������������ �������� 
     *    ������.
     * @return ���������� �������� ������ �� ��������� ������
     */
    @Override
    public Object setValue(Object value, String columnName) {
        return setValue(value,context.getCellServices().getColumnIndex(columnName));
    }    
    /**
     * ����� ����������� � ����� �� ���������� ������� � ��������� �������.
     * ����� ������� ����������� � ���������, �� ���������� ��������������
     * ��������� �����. � ���������� ������� ������ � ������ ����� �����������
     * ����� ������, ���������� ��������, ��� ���������� ������� 
     * {@link tdo.DataColumn#createBlankObject() }.
     * <p>���� ���, ��� �������� ������� ��������� ����� ������������ ���������
     * ����, �� ������ ���� {@link tdo.RowState} ���� ����������� �����
     * ������� ������ {@link tdo.RowState#columnAdded(tdo.DataColumn)}.
     * <p><b>����������.</b> ��� ������� �� ������������ ������ 
     *      {@link tdo.DataRow#getState() } ����� ������ ���������� 
     *      <code>null</code>, ���� ��������� ���� �� ��������������.
     * @param columnIndex 
     */
    @Override
    public void columnAdded(int columnIndex) {
        int cellIndex = context.getCellServices().getCellIndex(columnIndex);
        
        Object o = context.getCellServices().createBlankObject(columnIndex);

        cells.add(cellIndex, o);
//        if ( row.getState() != null )
//            row.getState().columnAdded(column);
    }

    /**
     * ����� ����������� � ����� �� �������� ������� �� ��������� �������.
     * ����� ������� ��������� �� ���������, �� ���������� ��������������
     * ��������� �����. � ���������� ������� ������ �� ������ ����� ���������
     * ������, ��������������� �������� �������.
     * <p>���� ���, ��� �������� ������� ��������� ����� ������������ ���������
     * ����, �� ������ ���� {@link tdo.RowState} ���� ����������� �����
     * ������� ������ {@link tdo.RowState#columnRemoved(tdo.DataColumn)}.
     * <p><b>����������.</b> ��� ������� �� ������������ ������ 
     *      {@link tdo.DataRow#getState() } ����� ������ ���������� 
     *      <code>null</code>, ���� ��������� ���� �� ��������������.
     * @param columnIndex 
     */
    @Override
    public void columnRemoved(int columnIndex) {
        int cellIndex = context.getCellServices().getCellIndex(columnIndex);
        cells.remove(cellIndex);
    }

    /**
     * ����� ����������� � ����� �� ����������� ������� ������ ��������� �������.
     * ���� �������� ������� �������� �����������, �� ����� ����������� �� 
     * ��������� �� ����� ��������.
     * <p>����� ������� ������������, �� ���������� ��������������
     * ��������� �����. � ���������� ������� ������ �� ������ ����� ���������
     * ������ � �������� �������� <code>oldCellIndex</code> � ����������� �����
     * ������ �� ����� �������� <code>newCellIndex</code>. �������� � ������
     * <code>oldCellIndex</code> ������������ � ������ <code>newCellIndex</code>.
     * 
     * <p>���� ���, ��� �������� ������� ��������� ����� ������������ ���������
     * ����, �� ������ ���� {@link tdo.RowState} ���� ����������� �����
     * ������� ������ {@link tdo.RowState#columnMoved(tdo.DataColumn, int, int, int) )}.
     * <p><b>����������.</b> ��� ������� �� ������������ ������ 
     *      {@link tdo.DataRow#getState() } ����� ������ ���������� 
     *      <code>null</code>, ���� ��������� ���� �� ��������������.
     * @param columnIndex ������ ������� ������ �� �����������
     * @param oldCellIndex ������ ������ ������, ��������������� �������
     * @param newCellIndex ����� ������ ������ ��������������� �������
     * @see tdo.impl.AbstractTable#columnsHandler
     */
    @Override
    public void columnMoved(int columnIndex, int oldCellIndex, int newCellIndex) {
//if (column.getKind() != DataColumn.DATA_KIND) {
//            return;
//        }
//        if ( context.getColumnServices().isCalculated(columnIndex) )
//            return;
        Object o = cells.get(oldCellIndex);

        cells.remove(oldCellIndex);
        cells.add(newCellIndex, o);
        
//        if ( row.getState() != null )
//            row.getState().columnMoved(column, fromColumnIndex, oldCellIndex, newCellIndex);
    }

    public int size() {
        return this.cells.size();
    }

    @Override
    public Object getObject() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}//class DefaultDataCellCollection
