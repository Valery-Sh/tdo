/*
 * DataRowCollection.java
 * 
 */

package tdo;

import java.util.ArrayList;
import java.util.List;
import tdo.service.TableServices;

/**
 * ��������� ��������� {@link tdo.DataRowCollection}.
 * ������ ������ ��������� �������, ���������, ���������, ������� � 
 * �������� �������� ���� {@link tdo.DataRow}. ������ � ����� ��������� �����
 * �������������� �� ������� (�������) ����.<p>
 * ���������� ��������� ������ ������������ ����� ������ ���� 
 * <code>java.util.ArrayList</code>. 
 * <p>�������� <code>context</code> ��������� ��������� � ����������
 * ��������.
 */
public class DefaultDataRowCollection<T> implements DataRowCollection{

    /**
     * ��������� ������.<p>
     */
    private List<DataRow> rows = null;

    private List<T> objectList;

    private TableServices context;
    /**
     * ������� ����� ��������� ��������� ����� ��������� ������.
     * �������� ����������� �� ��������� ��������� ������ 10.
     * @param capacity ������� ���������, ����������� ���������
     *   ����� ���� ��� ����������� ����������� ���������
     */
    public DefaultDataRowCollection(int capacity) {
        rows = new ArrayList<DataRow>(capacity);
    }
    /**
     * ������� ����� ��������� ��������� �����.
     * �������� ����������� �� ��������� ��������� ������ 10.
     */
    public DefaultDataRowCollection() {
        this(20);
    }
    /**
     * ������� � ��������� ��� � "�������" ���������� � ����� ��������� ������. 
     * ��������� ��� ����������� � ��������� {@link tdo.RowState#INSERTING}
     * ������� ������ {@link tdo.DataRow#attachNew() }.
     * @return ����������� ���.
     * @see tdo.DataRow
     * @see tdo.DefaultDataRow
     */
    @Override
    public DataRow add() {
        DataRow row = context.getRowCollectionServices().createRow();
        rows.add(row);        
        row.attachNew();
        if ( objectList != null )
            objectList.add((T) row.getCellCollection().getObject());
        return row;
    }

    /**
     * ��������� �������� ���  � ����� ���������.
     * ��������� ����� {@link tdo.DataRow#attach} � ������ ���� ��� �����������,
     * ���� ����������, ��������� ����.
     * @param row ����������� ���.
     * @return ������ ������������ ����
     * @see tdo.DataRow
     * @see tdo.DefaultDataRow
     */
    @Override
    public int add(DataRow row) {
        rows.add(row);
        row.attach();
        if ( objectList != null )
            objectList.add((T) row.getCellCollection().getObject());
        return rows.size()-1;
    }
    /**
     * ������� ��� �������� �� ��������� �����.
     */
    @Override
    public void clear() {
        rows.clear();
        if ( objectList != null )
            objectList.clear();
        rows = new ArrayList<DataRow>(100);
    }
    /**
     * ������ ������� �������� ��������� ����� ����������� � ����� �������
     * ���������.
     * 
     * @param fromRows ���������, ���� ������� ����������� � �������
     */
    @Override
    public void copyFrom(DataRowCollection fromRows) {
       // DefaultDataRowCollection drc = (DefaultDataRowCollection)rows;
        for (int i = 0; i < fromRows.getCount(); i++) {
            this.rows.add( fromRows.get(i));
        }
    }
    @Override
    public void copyFrom(DataRow[] fromRows) {
        for (int i = 0; i < fromRows.length; i++) {
            this.rows.add( fromRows[i]);
        }

    }

    /**
     * ������� � ���������� ����� ��������� �����, ��� ������� ��������� � 
     * ����� ������� ���������.
     * @return ����� ��������� ���� <code>DefaultDataRowCollection</code>
     */
    @Override
    public DataRowCollection create() {
        return new DefaultDataRowCollection();
    }
    /**
     * ������� � ���������� ����� ��������� �����, ��� ������� ��������� � 
     * ����� ������� ���������.
     * ����� ��������� ����������� �������� ��������. ���� �������� ���������
     * <code>populate</code> ����� <code>true</code>, �� �������� �������
     * ��������� ����������� � ����� ���������.
     * @param ctx ��������, ��� �������� ��������� ��������� �����
     * @param populate , ���� ����� <code>true</code>, �� �������� �������
     * ��������� ����������� � ����� ���������. <code>false</code> �����
     * ��������� �� ����������.
     * 
     * @return ����� ��������� ����� � �������� ���������� 
     * @see #create()
     * @see #create(TableServices)
     */
    @Override
    public DataRowCollection create(TableServices ctx, boolean populate) {
        DefaultDataRowCollection drc = (DefaultDataRowCollection)create();
        drc.setContext(ctx);
        if ( populate ) {
            drc.copyFrom(this);
        }
        return drc;
    }
    
    /**
     * ������� � ���������� ����� ��������� ����� ��� ��������� ���������
     * � �����, ��� ��������� ���� � ���� ���������� ���������.
     * ����� ������� � ������, ���� �� ���������� ��������� ��������� �
     * ���������� � ��� �������� ������ ���������.
     * @param ctx �������� �������, ��� �������� ��������� ���������
     * @return ����� ���������, ���������� ��������� ����� ������� ���������
     *    � ������� ����������
     * @see tdo.impl.AbstractTable#createTableViewSource(java.lang.String, java.lang.String) 
     */
    @Override
    public DataRowCollection createShared(TableServices ctx) {
        DataRowCollection newds = create(ctx,false);
        ((DefaultDataRowCollection) newds).importData(this,objectList);
        return newds;
    }
    /**
     * ������� ��� � ������� ��������� �������.
     * 
     * @param rowIndex ��� <code>int</code> - ������ ���������� ����.
     * @return ��������� ���
     * <code><pre>rowIndex < 0 || rowIndex >= getCount()</pre></code> 
     */
    @Override
    public DataRow delete(int rowIndex) {
        
        DataRow r = rows.remove(rowIndex);
        if ( objectList != null )
            objectList.remove(r.getCellCollection().getObject());
        return r;

    }
    /**
     * ������� � ������� �������� ��� �� ���������.
     * 
     * @param row ��������� ���.
     * @return ������ ���������� ���. -1, ���� ��� ����������� � ���������
     */
    @Override
    public int delete(DataRow row) {
        int rowIndex = this.rows.indexOf(row);
        if (rowIndex >= 0) {
            delete(rowIndex);
        }
        return rowIndex;
    }

    /**
     * ���������� ��� � �������� ��������.
     * @param rowIndex ������ �������� ����
     * @return ��� � �������� ��������
     * @throws IndexOutOfBoundsException ���� 
     * <code><pre>rowIndex < 0 || rowIndex >= getCount()</pre></code>
     * @see #set
     */
    @Override
    public DataRow get(int rowIndex) {
        return rows.get(rowIndex);
    }

    /**
     * @return �������� ��������� �������
     */
    public TableServices getContext() {
        return context;
    }
    /**
     * ���������� ���������� ����� � ���������.
     * @return ���������� ����� � ���������. ������ ���� <pre> >= 0 </pre>
     */
    @Override
    public int getCount() {
        return this.rows.size();
    }
    /**
     * ��������� ���������� <code>rows</code> �������� ���������� 
     * <code>rows</code> �������� ���������.
     * @param sourceRows
     */
    private void importData(DataRowCollection sourceRows,List<T> sourceObjectList ) {
        this.rows = ((DefaultDataRowCollection) sourceRows).rows;
        this.objectList = sourceObjectList;
    }
    /**
     * 
     * ���������� ������� ��������� ���� � ���������.
     * @param row ���, ��� �������� ������������ ������. ����� ���� <code>null</code>.
     * @return �������� ������� ��� ������ 0, ���� ��� ���������� � ���������.
     *   -1, � ��������� ������. ����� ������������ -1, ���� �������� ��������� 
     *    ����� <code>null</code>
     */
    @Override
    public int indexOf(DataRow row) {
        return rows.indexOf(row);
    }    
   /**
     * ������� � ��������� ��� � "�������" ���������� � �������� �������. 
     * ��������� ��� ����������� � ��������� {@link tdo.RowState#INSERTING}
     * ������� ������ {@link tdo.DataRow#attachNew() }.
     * @param rowIndex ������� ������� ������ ����
     * @return ����������� ���.
     * @see #insert(int,DataRow)
     * @see #add()
     * @see tdo.DataRow
     * @see tdo.DefaultDataRow
     * @throws IndexOutOfBoundsException ���� 
     * <code><pre>rowIndex < 0 || rowIndex > getCount()</pre></code>, �.� 
     *          ����������� 0 � getCount()
     * 
     * 
     */
    @Override
    public DataRow insert(int rowIndex) {
        DataRow row = context.getRowCollectionServices().createRow();
        rows.add(rowIndex, row);
        row.attachNew();
        if ( objectList != null )
            objectList.add((T) row.getCellCollection().getObject());
        return row;
    }
   /**
     * ��������� �������� ��� � �������� �������. 
     * @param rowIndex ������� ������� ������ ����
     * @param row ����������� ���
     * @see #insert(int)
     * @see #add(DataRow)
     * @see tdo.DataRow
     * @see tdo.DefaultDataRow
     * 
     * @throws IndexOutOfBoundsException ���� 
     *   <code><pre>rowIndex < 0 || rowIndex > getCount()</pre></code>, �.� 
     *          ����������� 0 � getCount()
     */
    @Override
    public void insert(int rowIndex,DataRow row) {
      rows.add(rowIndex, row);
      row.attach();
     if ( objectList != null )
         objectList.add((T) row.getCellCollection().getObject());
    }

    /**
     * �������������, �������� �� ��������� ���� �� ���� ���. 
     *
     * @return ��� <code>boolean</code> <i>true</i> - ��������� �� �������� �����;
     *                             <i>false</i> - ��������� �������� ���� ��
     *                             ���� ���.
     */
    @Override
    public boolean isEmpty() {
        return rows.isEmpty();        
    }

    /**
     * �������� ��� � �������� ������� �������� �����.
     * 
     * @param row ���������� ���
     * @param rowIndex ������� ����������� ����
     * @throws IndexOutOfBoundsException ���� 
     * <code><pre>rowIndex < 0 || rowIndex >= getCount()</pre></code>
     */
    @Override
    public void set(int rowIndex,DataRow row) {
        rows.set(rowIndex, row);
    }

    
    /**
     * ������������� �������� ��������� �������.
     * @param context �������� ���������
     */
    public void setContext(TableServices context) {
        this.context = context;
    }

    @Override
    public List getObjectList() {
        return this.objectList;
    }

    @Override
    public void setObjectList(List olist) {
        this.objectList = olist;
    }


} //class DefaultDataRowCollection
