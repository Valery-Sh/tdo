/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tdo;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;
import tdo.event.TableEvent.TableEventCause;
import tdo.event.*;
import tdo.impl.*;
import tdo.service.DefaultTableServices;
import tdo.service.TableServices;

/**
 *
 * @author valery
 */
public class SimpleTable<T> implements Table {
    
    protected List<T> objectList;

    protected List<ActiveRowListener> activeRowListeners;
    protected List<TableListener> dataModelListeners;
    protected List<PendingEditingListener> pendingListeners;

    protected PropertyChangeSupport changeSupport;
    protected InternalView view;
    protected DataColumnCollectionHandler columnsHandler;

    protected DataColumnCollection columns;

    protected int updatePolicy;
    protected boolean loading;
    protected boolean deletedHidden = false;

    protected ValidationManager validationManager;

    private boolean validationEnabled;
    protected boolean editProhibited;
    protected int activeRow = -1;
    private String tableName = "table1";
    protected List<TableListener> tableListeners;
    
    protected TableModel tableModel;
    
    protected EditObserver editObserver;

    public SimpleTable() {
        init();
    }
    protected void init() {
        this.tableModel = new TableTableModel(this);
        deletedHidden = false;
        updatePolicy = Table.ALLOW_UPDATE_ALL_ROWS;
        loading = false;
        validationEnabled = true;
        tableName = "Table1";

        this.editProhibited = false;

        this.context = new DefaultTableServices(this);

        this.createInternalView();

        columnsHandler = new DataColumnCollectionHandler(this,view.getTableRows());
        this.setColumns(new DefaultDataColumnCollection());

        //this.rows = new DefaultDataRowCollection();

        //((DefaultDataRowCollection) this.rows).setContext(getContext());
        //this.view = this.createInternalView();
        this.rowProvider = this.createRowProvider();


    }
    protected InternalView createInternalView(DataRowCollection rc) {
        view = new SimpleInternalViewImpl(this, rc);
        return view;
    }

    protected InternalView createInternalView() {
        view = new SimpleInternalViewImpl(this);
        return view;
    }
    protected DataRowProvider createRowProvider() {
       return new DefaultDataRowProvider(this.context);
    }

    @Override
    public void clear() {
        this.view.getCurrentRows().clear();
        this.view.getTableRows().clear();
    }

    @Override
    public void setColumns(DataColumnCollection cols) {
        DataColumnCollection old = this.columns;

        columns = cols;
        this.updateCellIndexes();
        ((DataColumnCollection) this.columns).addDataColumnCollectionListener(columnsHandler);
        this.firePropertyChange("columns", old, this.columns);

    }
    protected void updateCellIndexes() {
        int n = 0;

        for ( int i=0; i < getColumns().getCount(); i++ ) {
            DataColumn dc = getColumns().get(i);

            if ( dc.getKind() == DataColumn.DATA_KIND ) {
                dc.setCellIndex(n);
                n++;
            } else {
                dc.setCellIndex(-1);
            }
        }

    }
    protected List getObjectList() {
        return null;
    }
    /**
     * ������� � ��������� � ����� ������� ����� ��� �� ���������� ����� ��
     * ���������, ��� ����������  ��������� �����
     * <oce>tdo.DataColumnCollection</code> � ���������� ���.
     *
     * &nbsp;&nbsp;&nbsp;&nbsp;����� ����������� �� ����������, ���� ��������
     * �������  <code>editProhibited</code> ����������� � <code>true</code>.
     * <p>����� �� ���������� ��������� �������� ������� <code>loading</code>
     * �, ���� ��� �������� ����� <code>true</code>, �� ������������� ����������
     * <code>tdo.DataOperationException</code>.
     * <p>���� ��������� ������������ �������� ��������� ���� ������� ���������
     * � ��������� "��������", �� ��� ������ ���� �������� �����
     * {@link tdo.DataRow#endEdit()}. ��� � ���� ������� ����� �������� �
     * ���������� {@link tdo.impl.ValidateException}, �������� ���������� ������.
     * <p> ���������� ���������� ������������ ������
     * {@link tdo.impl.InternalViewImpl#addRow()} .
     * <p>����������� ��� ���������� <i>��������</i> �����, �.�. ��������
     * ������� <code>activeRow</code>  ���������� ������ ������� ������������ ����.
     *
     * @return �����, ����������� ���
     * @throws tdo.DataOperationException
     * @throws tdo.impl.ValidateException
     * @see #insertRow(int)
     * @see #isEditProhibited()
     * @see #isLoading()
     * @see #getActiveRowIndex()
     * @see #setActiveRowIndex(int)
     */

    @Override
    public DataRow addRow() {
        if (this.isEditProhibited()) {
            return null;
        }
        if (isLoading()) {
            throw new DataOperationException("Table cannot be in loading state when inserting new row");
        }
        int oldActiveRow = this.activeRow;
        DataRow oldRow;//My 06.03.2012 = null;
        if (activeRow >= 0) {
            oldRow = view.getRow(activeRow);
            fireActiveRowChanging(activeRow, view.getRowCount() - 1);
            oldRow.endEdit(true);
        }

        DataRow r = view.addRow();
        int rowIndex = view.getRowCount() - 1;
        //DataRow row = view.getRow(rowIndex);

        //fireDataModelInsertRow(this, rowIndex);
        fireRowInserted(this, rowIndex);
        activeRow = rowIndex;

        fireActiveRowChange(oldActiveRow, activeRow);
        // fireDataTableNewRow(rowIndex); // invokes fireDataTableEvent
        fireRowInserting(rowIndex);
/*        if ( getObjectList() != null )
            objectList.add( (T) getRow(activeRow).getCellCollection().getObject());
        objectListChanged(r);
 */
        //return getRowCount() - 1;
        return r;

    }
    protected void notifyExternal(int kind, DataRow row, int columnInex) {
    }
    
    protected void objectListChanged(DataRow row) {
        
    }
    /**
     * ������� ��� ��������� ������� ����� ��� � ��������� ��� � ��������� �����
     * �������.
     *
     * ������ ����� ����������� ��� ����� ��������� �������, �� ������� ��
     * ������ ���� � � ������� ��� �� ���������� ����� �������, �.� �����
     * {@link #getColumnCount()} ���������� 0. � ���� ������ ����� �������
     * ��������� �� ������ ����� ��������� ������� �������������.
     * <p>����� ����� ���� �������� � �������, ��� ������� ���� ��� ����� �����
     * �/��� � ������� ������ �������. � ����� ������, ����� ������� ��
     * ��������� � �� ����������. ������� ��������� ��������� ����� � �������
     * � ������� ������, � ���, ����� �������� ������� �� ����� ���������������
     * ��������.
     * <p>��������� ����� ��� � ��� ������ ����������� ������� �� �������.
     * ��� ����, �������� �������� �������� ������� �������������
     * ������� ������� � <code><i>i-��</i></code> ������������� i-��
     * �������.
     * <p>���� ����� ����� ������� ������ ���������� ������� �� ���������������
     * ������ ������<code><i>columnCount</i></code> �������.
     * <p>����� ����������� �� ����������, ���� ��������
     * <code>editProhibited</code> ������� ����������� � <code>true</code>.
     * <p>����������� ��� �� �������� ������� ��������� ����, �.�. ��������
     * <code>activeRow</code> �������� �������, �� ���������� ��������, �����
     * ����������� ��� - ��� ������������ ��� � �������.
     * <p>��� ���� �� ���������� ������������� �������� �� ����������
     * <p>����� ����������� ���������� � �������, ����� ������ ��������
     * <code>values></code> ����� <code>null</code>.
     *
     *
     * @param values �������� ������ �������� ����� ������ ����
     * @return ����� ���, ����������� � �������
     * @throws IllegalArgumentException ���� �������� ���������
     *      <code>values</code> ����� <code>null</code>
     * @see addRow(DataRow row)
     * @see addRow(Object[],int,int)
     */
    @Override
    public DataRow addRow(Object[] values) {
        if (isEditProhibited()) {
            return null;
        }
        if (values == null) {
            throw new IllegalArgumentException("addRow(Object[]) 'values' argument cannot be null");
        }
        DataRow r = addRow(values, 0, values.length);
        if ( this.activeRow < 0 )
            this.activeRow = 0;
        if ( getObjectList() != null )
            objectList.add( (T) r.getCellCollection().getObject());
        objectListChanged(r);
        return r;
    }
    /**
     * ������� ��� ��������� ��������� �������� ��������� ������� ����� ��� �
     * ��������� ��� � ��������� ����� �������.
     * ������ ����� ����������� ��� ����� ��������� �������, �� ������� ��
     * ������ ���� � � ������� ��� �� ���������� ����� �������, �.� �����
     * {@link #getColumnCount()} ���������� 0. � ���� ������ ����� �������
     * ��������� �� ������ ����� ��������� ������� �������������.
     * <p>����� ����� ���� �������� � �������, ��� ������� ���� ��� ����� �����
     * �/��� � ������� ������ �������. � ����� ������, ����� ������� ��
     * ��������� � �� ����������. ������� ��������� ��������� ����� � �������
     * � ������� ������, � ���, ����� �������� ������� �� ����� ���������������
     * ��������.
     * <p>��������� ����� ��� � ��� ������ ����������� ������� �� �������.
     * ��� ����, �������� ��������� <code><i>startIndex</i></code> �������������
     * ������� ������� � <code><i>startIndex+i</i></code> ������������� i-��
     * �������.
     * <p>���� �������� <code>count</code> ������, ���
     * <code><i>startIndex + count > values.length</i></code> �� � ��������
     * count �������  <code><i>values.length-startIndex</i></code>.
     * <p>���� ����� �������� <code>count</code> ������, ���
     * <code><i>startIndex + count > columnCount</i></code> �� � ��������
     * count �������  <code><i>columnCount</i></code>.
     * <p>����� ����������� �� ����������, ���� ��������
     * <code>editProhibited</code> ������� ����������� � <code>true</code>.
     * <p>����������� ��� �� �������� ������� ��������� ����, �.�. ��������
     * <code>activeRow</code> �������� �������, �� ���������� ��������, �����
     * ����������� ��� - ��� ������������ ��� � �������.
     * <p>��� ���� �� ���������� ������������� �������� �� ����������
     * <p>����� ����������� ���������� � �������, ����� startIndex ������ �����
     * ������� �������� ��� ����������� ��� <code>count</code> ������ ���� ���
     * <code>values></code> ����� <code>null</code>
     *
     *
     * @param values �������� ������ �������� ����� ������ ����
     * @param startIndex ��������� ������ � �������
     * @param count ���������� ���������, ������� ���������� � �������� ����
     * @return ����� ���, ����������� � �������
     * @throws IllegalArgumentException ���� �������� ���������
     *      <code>values</code> ����� <code>null</code>
     * @throws IndexOutOfBoundsException ���� �������� ���������
     *   <code>count</code> ������ 0 ��� �������� ��������� <code>startIndex</code>
     *   ������ 0 ��� ������ ����� ������� ��������.
     * @see addRow(DataRow row)
     * @see addRow(Object[])
     */

    protected DataRow addRow(Object[] values, int startIndex, int count) {
        if (isEditProhibited()) {
            return null;
        }
        if (values == null) {
            throw new IllegalArgumentException("addRow(Object[]) 'values' argument cannot be null");
        }
        if (count < 0 || startIndex < 0 || startIndex >= values.length) {
            throw new IndexOutOfBoundsException("'startIndex' and 'count' must be positive and match the 'values' length [" +
                    startIndex + "," + count + "]");
        }
        int c = startIndex + count > values.length ? values.length - startIndex : count;
        //DataColumnCollection columns = this.getColumns();
        if (getColumns().getCount() == 0) {
            for (int i = startIndex; i < startIndex + c; i++) {
                DataColumn dc = getColumns().add(values[i].getClass());
            }

        } else {
            c = startIndex + c > this.getColumns().getCount() ? this.getColumns().getCount() : c;
        }

        DataRow row;//My 06.03.2012 = null;
        row = createRow();
        for (int i = startIndex; i < startIndex + c; i++) {
            row.setValue(values[i], i - startIndex);
        }
        addRow(row);
        return row;
    }
    /**
     * ��������� �������� ��� � ��������� ����� �������.
     * <p>����� ����� ����������� ����������, �� �� ���� ��� ��� ����� ��������.
     *
     * <p>����� ����������� �� ����������, ���� �������� �������
     * <code>editProhibited</code> ����������� � <code>true</code>.
     *
     * <p> ���������� ���������� ������������ ������
     * {@link tdo.impl.InternalViewImpl#addRow(DataRow)} .
     *
     * <p>����������� ��� �� �������� ������� ��������� ����, �.�. ��������
     * <code>activeRow</code> �������� �������, �� ���������� ��������, �����
     * ����������� ��� - ��� ������������ ��� � �������.
     * <p>��� ���� �� ���������� ������������� �������� �� ����������. �����
     * ��������� ��� ���������� ������� ��� ����, �� ��� ����� ���������� �
     * �������, ��������:
     *
     * <code><pre>
     *   DataTable dt = new DataTable();
     *   DataRow r = dt.cteateRow;
     *   ............................
     *   ...... ��������� ���........
     *   ............................
     *
     *   if ( dt.validate(r) ) {
     *      dt.addRow(r);
     *   } else {
     *     ...............................
     *     ...... ��������� ������ .......
     *     ...............................
     *   }
     *
     * </pre></code>
     *
     * @param row
     * @return ������ ������������ ���� ��� -1, ���� ������� ��������� �
     *   ��������� <code>editProhibited==true</code>.
     *
     * @throws java.lang.IllegalArgumentException � ���� �������:
     * <ol>
     *   <li>����������� ��� <i>��</i> ����������� ������ �������.</li>
     *   <li>��������� ������������ ���� ��������� � ��������� �������� ��
     *       <code>DETACHED</code>
     *   </li>
     * </ol>
     * @see #isEditProhibited()
     * @see #isLoading()
     * @see #getActiveRowIndex()
     * @see #setActiveRowIndex(int)
     */

    @Override
    public int addRow(DataRow row) {

        if ( isLoading() ) {
            return view.getTableRows().add(row);
        }
        if (isEditProhibited()) {
            return -1;
        }

        if (row.getContext().getCoreServices().getTable() != this) {
            throw new IllegalArgumentException("The  row to be added doesn't match this table");
        }

        if (!row.getState().isDetached()) {
            throw new IllegalArgumentException("The row has allready been attached");
        }

        int rowIndex = view.addRow(row);

        if ( this.activeRow < 0 )
            this.activeRow = 0;
        insertRowInternal(rowIndex);

        return rowIndex;
    }

    private int insertRowInternal(int rowIndex) {

        DataRow row = view.getRow(rowIndex);
        fireRowInserted(this, rowIndex, row);
/*        if ( getObjectList() != null )
            objectList.add( (T) row.getCellCollection().getObject());
        objectListChanged(row);
 */
        return rowIndex;
    }

    @Override
    public Object calculateColumnValue(int rowIndex, int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object calculateColumnValue(DataRow row, int columnIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    ///���������� ������ ����� �� BaseTable

    @Override
    public DataRow createRow() {
        return this.rowProvider.createRow();
    }

    @Override
    public Table createTable() {
        return new SimpleTable();
    }

/*    @Override
    public Table createTreeTable() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
*/
    @Override
    public int find(DataRow row) {
        return this.view.find(row);
    }
/*
    @Override
    public void fireValidate(DataRow row) {
        if (!this.validationEnabled) {
            return;
        }
        String cause = "postRow";
        int rowIndex = this.find(row);
        editProhibited = true;

        if (validateList != null && validateList.size() > 0) {
            ValidateEvent e = new ValidateEvent(this, rowIndex, cause);
            for (int i = 0; i < validateList.size(); i++) {
                ((ValidateListener) validateList.elementAt(i)).validateRow(e);
            }
        }

        if (validatorCollection != null && validatorCollection.size() > 0) {
            for (int i = 0; i < validatorCollection.size(); i++) {
                boolean v = ((Validator) validatorCollection.elementAt(i)).validate(row);
                if (!v) {
                    throw new ValidateException("fireValidate(DataRow) ERROR ",rowIndex,i);
                }
            }
        }

        editProhibited = false;
    }
*/

    /**
     * !!!! �������� ����� {@link #fireRowEditing(tdo.event.RowEditingEvent) }.
     * � �������-����������� ���������  ����� ���������������� � �������� ���
     * getTableLinker().fireRowEditing(e);
     * @param kind
     * @param row
     */
/*    @Override
    public void fireRowEditing(int kind, DataRow row) {
        RowEditingEvent e = new RowEditingEvent(this, kind, row, -1);
        fireRowEditing(e);

    }
*/
    /**
     * !!!! �������� ����� {@link #fireRowEditing(tdo.event.RowEditingEvent) }.
     * � �������-����������� ���������  ����� ���������������� � �������� ���
     * getTableLinker().fireRowEditing(e);
     * @param kind
     * @param row
     * @param columnIndex
     */
    @Override
    public boolean processRowEditing(int kind, DataRow row, int columnIndex) {

        //RowEditingEvent e = new RowEditingEvent(this, kind, row, columnIndex);
        if (columnIndex < 0) {
            dispatchRowEditing(kind, row);
        } else {
            dispatchRowEditing(kind, row, columnIndex);
        }
        return true;
        //fireRowEditing(e);
    }
    protected void dispatchRowEditing(int kind, DataRow row, int columnIndex) {
        if (kind == DataRow.SETVALUE_INSERTING) {
            //endSetValueInserting(columnIndex);
            fireRowCellValueChanged(activeRow, columnIndex);
        } else if (kind == DataRow.SETVALUE) {
            endSetValue(row, columnIndex);
        } 
        notifyExternal(kind,row,columnIndex);
    }

    protected void dispatchRowEditing(int kind, DataRow row) {
        switch (kind) {
            
            case DataRow.CANCELEDIT_RESET_ROW_VERSIONS:
                //TODO
                break;
            case DataRow.BEGINEDIT:
                //TODO
                break;
            case DataRow.CANCELEDIT_INSERTING:
                cancelInserting();
                break;
            case DataRow.CANCELEDIT:
                cancelEditing(row);
                break;
            case DataRow.ENDEDIT_INSERTING:
                endInserting(row);
                break;
            case DataRow.ENDEDIT_RESET_ROW_VERSIONS:
                break;
            case DataRow.ENDEDIT:
//                endEditing(row);
                TableEvent e = new TableEvent(this, -1,TableEvent.ALL_COLUMNS, TableEventCause.stoppending);
                fireStopEditing(e);
                
                break;
            case DataRow.DELETE:
                endDeleting(row);
            case DataRow.ENDEDIT_BEFORE:
                //forceValidate(row);
                break;
        } //switch
        notifyExternal(kind,row,-1);

    }
    
    /**
     * !!!! ���������� �� ������� {@link #fireRowEditing(int, tdo.DataRow)  } ���
     * {@link #fireRowEditing(int, tdo.DataRow, int) .
     * � �������-����������� ���������  ����� ���������������� � �������� ���
     * getTableLinker().fireRowEditing(e);
     * @param e
     */
/*    protected void fireRowEditing(RowEditingEvent e) {
        if (e.getColumnIndex() < 0) {
            processRowEditing(e.getKind(), e.getRow());
        } else {
            processRowEditing(e.getKind(), e.getRow(), e.getColumnIndex());
        }

    }
*/
    //���������� ������ ����� �� AbstractTable

    @Override
    public DataColumnCollection getColumns() {
        return this.columns;
    }

    @Override
    public DataRow getRow(int rowIndex) {
        return this.view.getRow(rowIndex);
    }

    @Override
    public int getRowCount() {
        return this.view.getRowCount();
    }

    @Override
    public String getTableName() {
        return this.tableName;
    }

    @Override
    public int getUpdatePolicy() {
        return this.updatePolicy;
    }
    protected InternalView getView() {
        return this.view;
    }

    @Override
    public void insertRow(int rowIndex) {
        if (isEditProhibited()) {
            return;
        }
        if (isLoading()) {
            throw new DataOperationException("Table cannot be in loading state when inserting new row");
        }
        if (rowIndex > this.getRowCount()) {
            throw new IndexOutOfBoundsException("Row Index " + rowIndex + " out of bounds " + getRowCount());
        }
        
        int oldActive = this.activeRow;
//        DataRow oldRow = null;
        if (activeRow >= 0 && activeRow != rowIndex) {
            fireActiveRowChanging(oldActive, rowIndex);
            getRow(activeRow).endEdit(true); // can throw ValidateException
        }

        view.insertRow(rowIndex);

        //fireDataModelInsertRow(this, rowIndex);
        fireRowInserted(this, rowIndex);
        activeRow = rowIndex;

        fireActiveRowChange(oldActive, activeRow);
        //fireDataTableNewRow(rowIndex); // invokes fireDataTableEvent
        fireRowInserting(rowIndex);
/*        if ( getObjectList() != null )
            objectList.add( (T) getRow(activeRow).getCellCollection().getObject());
        objectListChanged(getRow(activeRow));
*/        
    }

    @Override
    public void insertRow(int rowIndex, DataRow row) {
        if (isEditProhibited()) {
            return;
        }

        if (rowIndex > this.getRowCount()) {
            throw new IndexOutOfBoundsException("Row Index " + rowIndex + " out of bounds " + getRowCount());
        }

        if (row.getContext().getCoreServices().getTable() != this) {
            throw new IllegalArgumentException("The  row to be added doesn't match this table");
        }

        if (!row.getState().isDetached()) {
            throw new IllegalArgumentException("The row has allready been attached");
        }


        view.insertRow(rowIndex, row);

        fireRowInserted(this, rowIndex, row);
        if ( getObjectList() != null )
            objectList.add( (T) row.getCellCollection().getObject());
        objectListChanged(row);
        
    }

    /**
     * �������� ��� ������������ ���
     *
     * @param table
     * @param rowIndex
     * @param row
     */
    @Override
    public void fireRowInserted(Table table, int rowIndex, DataRow row) {
        fireTable(new TableEvent(this, rowIndex, TableEvent.ALL_COLUMNS, TableEventCause.insert));
    }

    /**
     * �������� ����� ������ ���
     * @param table
     * @param rowIndex
     */
   // @Override
    public void fireRowInserted(Table table, int rowIndex) {
        fireDataModel(new TableEvent(this, rowIndex, TableEvent.ALL_COLUMNS, TableEventCause.newrow));
    }

    @Override
    public void fireRowInserting(int rowIndex) {
        fireTable(new TableEvent(this, rowIndex, TableEvent.ALL_COLUMNS, TableEventCause.newrow));
    }

    @Override
    public boolean isCellEditable(int columnIndex) {
        boolean result = (!this.getColumns().get(columnIndex).isReadOnly()) &&
                this.getColumns().get(columnIndex).getKind() == DataColumn.DATA_KIND;

        //result = ! this.getColumns().getColumn( columnIndex).isReadOnly();
        return result;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return isCellEditable(columnIndex);
    }

    @Override
    public boolean isEditProhibited() {
        return this.editProhibited;
    }

    @Override
    public boolean isLoading() {
        return this.loading;
    }

    @Override
    public boolean isValidationEnabled() {
        return this.validationEnabled;
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setTableName(String tableName) {
        String old = this.tableName;
        this.tableName = tableName;
        firePropertyChange("tableName", old, this.tableName);
    }

    @Override
    public void setUpdatePolicy(int updatePolicy) {
        int old = updatePolicy;
        this.updatePolicy = updatePolicy;
        firePropertyChange("updatePolicy", old, this.updatePolicy);
    }

    @Override
    public void setRow(DataRow row, int rowIndex) {
        view.setRow(row, rowIndex);
    }
    /**
     * ������������� �������� �������� ������������� ���������� ��� ��� �������
     * ������� ���������. <p>
     * ���� ��������� ���������, �.�. �������� ����������� � <code>false</code>, 
     * ������ ��������� ����������� �� ���������. ��� ���� �����-���� ����������
     * �� �������������.
     * 
     * @param value �������� <code>true</code>, ������������ �� ���������, 
     * ����������, ��� ��������� ��������. <code>false</code> - ��������, ��� 
     * ����� ��������� ���������.
     */
    @Override
    public void setValidationEnabled(boolean value) {
        boolean old = this.validationEnabled;
        this.validationEnabled = value;
        firePropertyChange("validationEnabled", old, this.validationEnabled);
    }
    /**
     * ������������ ��������� ����� ������� � �������� ���������� �������
     * ���������� � ������������ ���������� �� ������. 
     * <p>����� ���������� ���������� ������� ���� {@link tdo.impl.ValidationManager}
     * �������, ������������ ����� ������������� �������. ��� ����, ���� ���� 
     * ������ ��� �� ������, �� �� ���������.
     * 
     * <p>����� ���� ����������������  ����� ���������� ����������� ����. 
     * ������  <code>addRowValidator</code> ����� ���� 
     * ������ ��  ����� ����������. ��������� ���� ����������� � ������ 
     * ����������� ����. ���������� �����������  �� �������� ������������ �Ȼ. 
     * ��� ��������, ���, ���� ����� <code>validate</code> ���������� � �������� 
     * <code>b>i</b></code> ���������� ������� �������� <code>false</code>, �.�.  
     * ���������� ������ � ������, �� ���������� ���������� ����������� � 
     * ��������� �������� <code>b>i</b></code> �� ���������� � ��� ��������� 
     * <code>invalid</code>.
     * 
     * @param expression ��������� ��������� ��� ���������, ����������� � 
     *   ����������, ������������ ��� ������� ���������� ����. 
     * @param msg ������, ���������� ������� ������������ ��� ����������� 
     *  ������ �������������� �����������
     * @return ��������� �� �������� ���������� ������ ���� <code>Validator</code>
     * @see #addRowValidator(tdo.impl.Validator, java.lang.String)}
     * @see tdo.impl.ValidationManager#addRowValidator(java.lang.String, java.lang.String) 
     */
    public Validator addRowValidator(String expression,String msg) {
        if ( this.validationManager == null )
            this.validationManager = new ValidationManager(this.context);
        return this.validationManager.addRowValidator(expression, msg);
    }
    /**
     * ������������ ��������� ����� ������� � �������� ��������, ������������ �������
     * ���������� � ������������ ���������� �� ������. 
     * <p>����� ���������� ���������� ������� ���� {@link tdo.impl.ValidationManager}
     * �������, ������������ ����� ������������� �������. ��� ����, ���� ���� 
     * ������ ��� �� ������, �� �� ���������.
     * 
     * <p>����� ���� ����������������  ����� ���������� ����������� ����. 
     * ������  <code>addRowValidator</code> ����� ���� 
     * ������ ��  ����� ����������. ��������� ���� ����������� � ������ 
     * ����������� ����. ���������� �����������  �� �������� ������������ �Ȼ. 
     * ��� ��������, ���, ���� ����� <code>validate</code> ���������� � �������� 
     * <code>b>i</b></code> ���������� ������� �������� <code>false</code>, �.�.  
     * ���������� ������ � ������, �� ���������� ���������� ����������� � 
     * ��������� �������� <code>b>i</b></code> �� ���������� � ��� ��������� 
     * <code>invalid</code>.
     * 
     * @param validator ��������, ������������ ������� ���������� ����
     * @param msg ������, ���������� ������� ������������ ��� ����������� 
     *  ������ �������������� �����������
     * @return ��������� �� �������� ���������� ������ ���� <code>Validator</code>
     * @see #addRowValidator( java.lang.String, java.lang.String)}
     * @see tdo.impl.ValidationManager#addRowValidator(tdo.impl.Validator, java.lang.String) 
     */
    public void addRowValidator(Validator validator,String msg) {
        if ( this.validationManager == null )
            this.validationManager = new ValidationManager(this.context);
        this.validationManager.addRowValidator(validator, msg);
    }
   /**
     * ������� �������� ��������� ����.
     *
     * @param validator ��������� ���������
     * @see tdo.impl.ValidationManager#removeRowValidators(tdo.impl.Validator) 
     */
    public void removeRowValidator(Validator validator) {
        if (validationManager == null) {
            return;
        }
        validationManager.removeRowValidator(validator);
    }
    /**
     * ������������ ��������� ��� �������� ������� � �������� ���������� �������
     * ���������� � ������������ ���������� �� ������. 
     * <p>����� ���������� ���������� ������� ���� {@link tdo.impl.ValidationManager}
     * �������, ������������ ����� ������������� �������. ��� ����, ���� ���� 
     * ������ ��� �� ������, �� �� ���������.
     * 
     * <p>��� ����� ������� ������� ����� ���� ����������������  ����� ����������
     * ����������� �������. ������  <code>addColumnValidator</code> ����� ���� 
     * ������ ��  ����� ����������. ��������� ������� ����������� � ������ 
     * �����������. ��� ������� ����� ������� ��������� ���� ������. 
     * ���������� �����������  �� �������� ������������ �Ȼ. ��� ��������, ���, 
     * ���� ����� <code>validate</code> ���������� � �������� 
     * <code>b>i</b></code> ���������� ������� �������� <code>false</code>, �.�.  
     * ���������� ������ � ������, �� ���������� ���������� ����������� � 
     * ��������� �������� <code>b>i</b></code> �� ���������� � ��� ��������� 
     * <code>invalid</code>.
     * 
     * @param columnName ��� �������, ��� ������� �������������� ���������.
     * @param expression ��������� ��������� �� ���������, ����������� � 
     *   ����������, ������������ ��� ������� ���������� �������. ��������� �����
     *   ��������� ����������� �������� :value, � ������� ���������� ����� 
     *   �������� �������.
     * @param msg ������, ���������� ������� ������������ ��� ����������� 
     *  ������ �������������� �����������
     * @return ��������� �� �������� ���������� ������ ���� <code>Validator</code>
     * @see #addColumnValidator(java.lang.String, tdo.impl.Validator, java.lang.String)}
     * @see tdo.impl.ValidationManager#addColumnValidator(java.lang.String, java.lang.String, java.lang.String) 
     */
    public Validator addColumnValidator(String columnName, String expression,String msg) {
        if ( this.validationManager == null )
            this.validationManager = new ValidationManager(this.context);
        return this.validationManager.addColumnValidator(columnName,expression, msg);
    }
    /**
     * ������������ ��������� ��� �������� ������� � �������� ��������
     * ���������� ��� �������� ���� <code>tdo.impl.Validator</code> � 
     * ������������ ���������� �� ������. 
     * <p>����� ���������� ���������� ������� ���� {@link tdo.impl.ValidationManager}
     * �������, ������������ ����� ������������� �������. ��� ����, ���� ���� 
     * ������ ��� �� ������, �� �� ���������.
     * 
     * <p>��� ����� ������� ������� ����� ���� ����������������  ����� ����������
     * ����������� �������. ������  <code>addColumnValidator</code> ����� ���� 
     * ������ ��  ����� ����������. ��������� ������� ����������� � ������ 
     * �����������. ��� ������� ����� ������� ��������� ���� ������. 
     * ���������� �����������  �� �������� ������������ �Ȼ. ��� ��������, ���, 
     * ���� ����� <code>validate</code> ���������� � �������� 
     * <code>b>i</b></code> ���������� ������� �������� <code>false</code>, �.�.  
     * ���������� ������ � ������, �� ���������� ���������� ����������� � 
     * ��������� �������� <code>b>i</b></code> �� ���������� � ��� ��������� 
     * <code>invalid</code>.
     * 
     * @param columnName ��� �������, ��� ������� �������������� ���������.
     * @param validator ������, ������������ ������� ���������� �������.
     * @param msg ������, ���������� ������� ������������ ��� ����������� 
     *  ������ �������������� �����������
     * @return ��������� �� �������� ���������� ������ ���� <code>Validator</code>
     * @see #addColumnValidator(java.lang.String, java.lang.String, java.lang.String)}
     * @see tdo.impl.ValidationManager#addColumnValidator(java.lang.String, tdo.impl.Validator, java.lang.String) 
     */
    
    public void addColumnValidator(String columnName, Validator validator,String msg) {
        if ( this.validationManager == null )
            this.validationManager = new ValidationManager(this.context);
        this.validationManager.addColumnValidator(columnName,validator, msg);
    }
   /**
     * ������� ��� ���������� �������, ��������������� ��������
     * ����� �������.
     * <p>���� ��������� ��� ������ ������� ������� ���� ���� ���������, �� 
     * ���� ��������������� ������� {@link tdo.impl.ValidationManager#removeColumnValidator(java.lang.String, tdo.impl.Validator) }
     *
     * @param columnName �������, ��� ������� ��������� ����������
     * @see tdo.impl.ValidationManager#removeColumnValidators(java.lang.String) 
     * @see tdo.impl.ValidationManager#removeColumnValidator(java.lang.String, tdo.impl.Validator) 
     */
    public void removeColumnValidator(String columnName) {
        if (validationManager == null) {
            return;
        }
        validationManager.removeColumnValidators(columnName);

    }
    /**
     * ������������ ���������� ������� �������� (validate) ������.<p>
     * <p>����� ���������� ���������� ������� ���� {@link tdo.impl.ValidationManager}
     * �������, ������������ ����� ������������� �������. ��� ����, ���� ���� 
     * ������ ��� �� ������, �� �� ���������.
     *
     * @param l - ����������� ���������� ������� <code>ValidateEvent</code> ��� �������.
     * @see #removeColumnValidateListener(tdo.event.ValidateListener) 
     */
    public void addColumnValidateListener(ValidateListener l) {
        if ( this.validationManager == null )
            this.validationManager = new ValidationManager(this.context);
        this.validationManager.addColumnValidateListener(l);
    }

    /**
     * ������� ���������� ������� �������� ������.<p>
     * <p>����� ���������� ���������� ������� ���� {@link tdo.impl.ValidationManager}
     * �������, ������������ ����� ������������� �������.
     * @param l - ��������� ���������� ������� <code>ValidateEvent</code> ��� �������.
     */
    public void removeColumnValidateListener(ValidateListener l) {
        if ( this.validationManager == null )
            return;
        this.validationManager.removeColumnValidateListener(l);
    }

    /**
     * ������������ ���������� ������� �������� (validate) ������ ����.<p>
     * <p>����� ���������� ���������� ������� ���� {@link tdo.impl.ValidationManager}
     * �������, ������������ ����� ������������� �������. ��� ����, ���� ���� 
     * ������ ��� �� ������, �� �� ���������.
     *
     * @param l - ����������� ���������� ������� <code>ValidateEvent</code> ��� ����.
     * @see #removeRowValidateListener(tdo.event.ValidateListener) 
     */
    public void addRowValidateListener(ValidateListener l) {
        if ( this.validationManager == null )
            this.validationManager = new ValidationManager(this.context);
        this.validationManager.addRowValidateListener(l);
    }

    /**
     * ������� ���������� ������� <code>ValidateEvent</code>.<p>
     * @param l - ��������� ���������� ������� <code>ValidateEvent</code> ��� ����.
     */
    public void removeRowValidateListener(ValidateListener l) {
        if ( this.validationManager == null )
            return;
        this.validationManager.removeRowValidateListener(l);
    }

    /**
     * ��������� ��������� ��������� ���� ���������� ��� ��� ����������, � 
     * ����������� �� ��������� �����. 
     * <p>���������� �� ���� ������������� ���������� ��� ���, ��� ����������� 
     * ������ ���� �� ����� ������������������ ����������� ����������� ��� 
     * ������������������ ����������� ������� {@link tdo.event.ValidateEvent}.
     * ����������� ������� ����� ��� {@link tdo.event.ValidateListener}. 
     * @param row ���, ��� �������� ���������� ���������.
     * @param throwException �������� <code>true</code> �������� � ������� �������
     *  ���������� {@link tdo.impl.ValidateException } ��� ����������� ������ 
     *  ���� �� ����� ������������������ �����������. ���� �������� ����� 
     * <code>false</code>, �� ���������� �� �������������.
     * @return <code>true</code> ���� �� ���� �� ������������������ �����������
     *   �� ��������� ������. <code>false</code> - � ��������� ������.
     * @see #validate(tdo.DataRow) 
     * @see #validate(tdo.DataRow, java.lang.String, java.lang.Object) 
     * @see #addRowValidateListener(tdo.event.ValidateListener) 
     * @see #addColumnValidateListener(tdo.event.ValidateListener) 
     * 
     */
 /*   @Override
    public boolean validate(DataRow row, boolean throwException) {
        if (! validationEnabled ) {
            return true;
        }

        return this.validationManager == null ? true : this.validationManager.validate(row, throwException);
    }
*/
    /**
     * ��������� ��������� ��������� ����.
     *  
     * <p>����� �� ����������� ����������, ��������� ���������� ������ 
     * {@link #validate(tdo.DataRow, boolean) } �� �������� ������� ��������� 
     * ������ <code>false</code>.
     * <p>��� ����������� ������ ���� �� ����� ������������������ ����������� 
     * ����������� ��� ������������������ ����������� ������� 
     * {@link tdo.event.ValidateEvent}. ����������� ������� ����� ��� 
     * {@link tdo.event.ValidateListener}.
     * 
     * @param row ���, ��� �������� ���������� ���������.
     * @return <code>true</code> ���� �� ���� �� ������������������ �����������
     *   �� ��������� ������. <code>false</code> - � ��������� ������.
     * @see #validate(tdo.DataRow, boolean) 
     * @see #validate(tdo.DataRow, java.lang.String, java.lang.Object) 
     * @see #addRowValidateListener(tdo.event.ValidateListener) 
     * @see #addColumnValidateListener(tdo.event.ValidateListener) 
     */
/*    public boolean validate(DataRow row) {
        if (! validationEnabled ) {
            return true;
        }
       return this.validationManager == null ? true : this.validationManager.validate(row, false);
    }
 */
    /**
     * ��������� ��������� ��� ��������� ����, �������� ������� � ������ 
     * �������� �������.
     *  
     * <p>��� ����������� ������ ���� �� ����� ������������������ �����������
     * ������� ����������� ��� ������������������ ����������� ������� 
     * {@link tdo.event.ValidateEvent}. ����������� ������� ����� ��� 
     * {@link tdo.event.ValidateListener}.
     * 
     * @param row ���, ��� �������� ���������� ���������.
     * @param columnName ��� ������� ��� ������� ����������� ���������
     * @param value ����� �������� �������, ��� ������� ���������� ���������
     * 
     * @return <code>true</code> ���� �� ���� �� ������������������ �����������
     *   �� ��������� ������. <code>false</code> - � ��������� ������.
     * @see #validate(tdo.DataRow, boolean) 
     * @see #validate(tdo.DataRow, java.lang.String, java.lang.Object) 
     * @see #addRowValidateListener(tdo.event.ValidateListener) 
     * @see #addColumnValidateListener(tdo.event.ValidateListener) 
     *
     */
/*    @Override
    public boolean validate(DataRow row, String columnName, Object value) {
        if (! validationEnabled ) {
            return true;
        }
       return this.validationManager == null ? true : this.validationManager.validate(row, columnName, value);
    }
*/
    @Override
    public synchronized void addTableListener(TableListener l) {
        if (this.tableListeners == null) {
            this.tableListeners = new ArrayList<TableListener>(10);
        }
        this.tableListeners.add(l);
    }

    @Override
    public synchronized void removeTableListener(TableListener l) {
        if (this.tableListeners != null) {
            this.tableListeners.remove(l);
        }
    }
    @Override
    public void addDataModelListener(TableListener l) {
        if (this.dataModelListeners == null) {
            this.dataModelListeners = new ArrayList<TableListener>(10);
        }
        this.dataModelListeners.add(l);
    }

    @Override
    public void removeDataModelListener(TableListener l) {
        if (this.dataModelListeners != null) {
            this.dataModelListeners.remove(l);
        }
    }

    @Override
    public void addPendingEditingListener(PendingEditingListener l) {
        if (this.pendingListeners == null) {
            this.pendingListeners = new ArrayList<PendingEditingListener>(10);
        }
        this.pendingListeners.add(l);

    }

    @Override
    public void removePendingEditingListener(PendingEditingListener l) {
        if (this.pendingListeners != null) {
            this.pendingListeners.remove(l);
        }

    }

    @Override
    public void fireTable(TableEvent e) {
        if (tableListeners == null || tableListeners.isEmpty()) {
            return;
        }
        for (TableListener listener : tableListeners) {
            listener.tableChanged(e);
        }
    }
    @Override
    public void fireDataModel(TableEvent e) {
        if (dataModelListeners == null  || dataModelListeners.isEmpty()) {
            return;
        }
        for (TableListener listener : dataModelListeners) {
            listener.tableChanged(e);
        }
    }

    /**
     *
     * @param loading
     */
    @Override
    public void setLoading(boolean loading) {
        boolean old = this.loading;
        this.loading = loading;
        firePropertyChange("loading", old, this.loading);


    }


    protected void endSetValue(DataRow row, int columnIndex) {
    }

    protected void endSetValueInserting(int columnIndex) {
        fireRowCellValueChanged(activeRow, columnIndex);
    }

    /**
     * !!! ������� �������� ��� ���� ��������� � ������ ����������� �����������
     * �����. ����� ����������� ���� ����� ������ <i>cn</i> � JTable.
     * ����� �� �������� ������ <i>i</i> �, ��� ����, <b>i+1 < cn</b>.
     * ����� �� ������� �� ������ <i>i+1</i>, ������� ����� ����� <i>cn</i>.
     * ����� ������ ������ i+1 �� ����� ������������ !!!, ���� ������� �� �������
     * �� ��� ���� �������.
     *
     * @param rowIndex
     * @param columnIndex
     */
    protected void fireRowCellValueChanged(int rowIndex, int columnIndex) {
        int columnInterval = columnIndex < 0 ? TableEvent.ALL_COLUMNS : columnIndex;

        if (columnIndex < 0 || this.getColumns().getCount(DataColumn.CALC_KIND) > 0) {
            //fireDataModelUpdateRow(this, rowIndex);
            TableEvent e = new TableEvent(this, rowIndex, TableEvent.ALL_COLUMNS, TableEventCause.update);
            fireDataModel( e );
        } else {
            //fireDataModelUpdateColumn(this, rowIndex, columnIndex);
            TableEvent e = new TableEvent(this, rowIndex, columnIndex, TableEventCause.update);
            fireDataModel( e );
        }
        fireTable(new TableEvent(this, rowIndex, columnInterval, TableEventCause.update) );
    }


    protected void cancelInserting() {
        int rowIndex = activeRow;
        view.cancelInserting(rowIndex);
        //fireDataModelDeleteRow(this, rowIndex);
        TableEvent e = new TableEvent(this, rowIndex,TableEvent.ALL_COLUMNS, TableEventCause.delete);
        fireDataModel(e);
        if (rowIndex < getRowCount() && rowIndex >= 0) {
            moveToAfterDelete(rowIndex);
        } else if (rowIndex > 0) {
            moveToAfterDelete(rowIndex - 1);
        } else if (rowIndex == 0 && this.getRowCount() > 0) {
            moveToAfterDelete(0);
        } else if (rowIndex == 0 && this.getRowCount() == 0) {
            moveToAfterDelete(-1);
        }
        //this.fireDataTableDeleted(rowIndex);
        fireTable(e);
    }
    protected void moveToAfterDelete(int newIndex) {
        this.activeRow = newIndex;
        fireActiveRowChange(-1, newIndex);
    }
    protected void cancelEditing(DataRow row) {
    }
    protected void endInserting(DataRow row) {
    }
/*    protected void endEditing(DataRow row) {
        TableEvent e = new TableEvent(this, -1,TableEvent.ALL_COLUMNS, TableEventCause.stoppending);
        fireStopEditing(e);
    }
 */
    protected void fireStopEditing(TableEvent e) {
        if (pendingListeners == null || pendingListeners.isEmpty()) {
            return;
        }
        for (TableListener listener : tableListeners) {
            listener.tableChanged(e);
        }
    }

    protected void endDeleting(DataRow row) {
        RowState sState = row.getState();

        int rowIndex = activeRow;
        DataRow aRow = rowIndex < 0 ? null : getRow(rowIndex); // activeRow

        if (aRow == null || aRow != row) {
            rowIndex = find(row);
        }

        if (isDeletedHidden() && rowIndex >= 0) {

            DataRow delRow = view.deleteRow(rowIndex);
            delRow.getState().copyFrom(sState);

            // fireDataModelDeleteRow(this, rowIndex);
            TableEvent e = new TableEvent(this, rowIndex,TableEvent.ALL_COLUMNS, TableEventCause.delete);
            fireDataModel(e);

            if (rowIndex <= activeRow) {
                if (rowIndex < getRowCount() && rowIndex >= 0) {
                    moveToAfterDelete(rowIndex);
                } else if (rowIndex > 0) {
                    moveToAfterDelete(rowIndex - 1);
                } else if (rowIndex == 0 && getRowCount() > 0) {
                    moveToAfterDelete(0);
                } else if (rowIndex == 0 && getRowCount() == 0) {
                    moveToAfterDelete(-1);
                }
            }
            //fireDataTableDeleted(rowIndex);
            fireTable(e);

        } else {

            if (rowIndex >= 0) {
                getRow(rowIndex).getState().copyFrom(sState);
                //fireDataModelUpdateRow(this, rowIndex);
                TableEvent e = new TableEvent(this, rowIndex, TableEvent.ALL_COLUMNS, TableEventCause.update);
                fireDataModel( e );

                if (activeRow >= 0) {
                    moveToAfterDelete(activeRow); // for active row change event only

                }
            }
        }
    }

    public boolean isDeletedHidden() {
        return this.deletedHidden;
    }

    /////////////////////////////////////////////////////////////////////////
    // New Func �� BaseTable
    /////////////////////////////////////////////////////////////////////////
    protected DataRowProvider rowProvider;
    protected TableServices context;

    public void setRowProvider(DataRowProvider p) {
        DataRowProvider old = this.rowProvider;
        this.rowProvider = p;
        firePropertyChange("rowProvider", old, this.rowProvider);
    }

    protected DataRowProvider getRowProvider() {
        return this.rowProvider;
    }

    @Override
    public TableServices getContext() {
        return this.context;
    }

    protected void setContext(TableServices context) {
        TableServices old = this.context;
        this.context = context;
        firePropertyChange("context", old, this.context);
    }

    protected DataColumnCollection createColumns() {
        return new DefaultDataColumnCollection();
    }

    public DataRow createRowCopy(DataRow row) {
        return this.rowProvider.createRowCopy(row);
    }


    public DataRow getActiveRow(){
        return this.activeRow < 0 ? null : getRow(activeRow);
    }

    @Override
    public int getActiveRowIndex(){
        return this.activeRow;
    }
    /**
     * ������������� ����� �������� ������� ��������� ����.
     * @param rowIndex ������ ������ ��������� ����
     * @throws ValidateException
     */
    @Override
    public void setActiveRowIndex(int rowIndex) throws ValidateException {
        if (activeRow == rowIndex ) {
            return;
        }
        int oldActive = this.activeRow;
        if (activeRow >= 0 && activeRow != rowIndex ) {
            fireActiveRowChanging(oldActive, rowIndex);
            getRow(activeRow).endEdit(true); // can throw ValidateException
        }
        this.activeRow = rowIndex;
        fireActiveRowChange(oldActive, activeRow);
        firePropertyChange("activeRow", new Integer(oldActive), new Integer(activeRow));
    }

    protected void setEditProhibited(boolean editProhibited) {
        boolean old = this.editProhibited;
        this.editProhibited = editProhibited;
        firePropertyChange("editPronibited", old, this.editProhibited);
    }

    /**
     * ��������� ���������� ������� ��������� ������� ��������� ����.
     *   &nbsp; &nbsp; 
     *   ������� ����� ������������ <i>�����</i> ��� <i>�����</i> ���������
     * ������� (��������) ������� �������� ������, ��� ������������ ��������
     * ������� {@link tdo.event.ActiveRowEvent} .
     *
     * @param listener ����������� ���������� �������.
     * @see #removeActiveRowListener
     * @see #addActiveRowChangingListener
     */
    @Override
    public synchronized void addActiveRowListener(ActiveRowListener listener) {
        if (activeRowListeners == null) {
            activeRowListeners = new ArrayList<ActiveRowListener>(10);
        }
        activeRowListeners.add(listener);
    }
    protected void clearActiveRowListeners() {
        if (activeRowListeners != null) {
            activeRowListeners.clear();

        }
    }
    protected void clearTableListeners() {
        if (tableListeners != null) {
            tableListeners.clear();

        }
    }
    protected void clearPendingListeners() {
        if (this.pendingListeners != null) {
            pendingListeners.clear();

        }
    }

    /**
     * ������� ������ �� ������ ������������������ ������������.<p>
     *
     * @param listener ��������� ����������.
     * @see #addActiveRowListener
     */
    @Override
    public synchronized void removeActiveRowListener(ActiveRowListener listener) {
        activeRowListeners.remove(listener);
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        if (changeSupport == null) {
            changeSupport = new PropertyChangeSupport(this);
        }
        changeSupport.addPropertyChangeListener(listener);
    }

    public synchronized void removePropertyChangeListener(PropertyChangeListener listener) {
        if (changeSupport != null) {
            changeSupport.removePropertyChangeListener(listener);
        }
    }

    protected void fireActiveRowChange(int oldValue, int newValue) {
        ActiveRowEvent e = null;
        if (activeRowListeners == null || activeRowListeners.isEmpty()) {
            e = new ActiveRowEvent(this, oldValue, newValue);
            this.notifyLinked(e);
            return;
        }
        if ( e == null )
            e = new ActiveRowEvent(this, oldValue, newValue);
        for (ActiveRowListener l : activeRowListeners) {
            l.activeRowChange(e);
        }
        this.notifyLinked(e);
    }

    protected void notifyLinked(ActiveRowEvent e) {
    }
    
    private void fireActiveRowChanging(int oldValue, int newValue) {

        if (activeRowListeners == null || activeRowListeners.isEmpty()) {
            return;
        }
        ActiveRowEvent e = new ActiveRowEvent(this, oldValue, newValue, ActiveRowEvent.CHANGING);
        for (ActiveRowListener l : activeRowListeners) {
            l.activeRowChange(e);
        }
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if (changeSupport != null) {
            changeSupport.firePropertyChange(propertyName, oldValue, newValue);
        }
    }

    @Override
    public ValidationManager getValidationManager() {
        return this.validationManager;
    }

    @Override
    public int getColumnCount() {
        return this.tableModel.getColumnCount();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return this.tableModel.getColumnName(columnIndex);
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return this.tableModel.getColumnClass(columnIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return this.tableModel.getValueAt(rowIndex,columnIndex);
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        this.tableModel.setValueAt(aValue,rowIndex,columnIndex);
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        this.tableModel.addTableModelListener(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        this.tableModel.removeTableModelListener(l);
    }

    public static class TableTableModel extends AbstractTableModel {

        private Table table;
        private HashMap<TableModelListener,TableTableModelListener> tmMap;
        public TableTableModel(Table table) {
            this.table = table;
            tmMap = new HashMap<TableModelListener,TableTableModelListener>();
        }

        @Override
        public int getRowCount() {
            return this.table.getRowCount();
        }

        @Override
        public int getColumnCount() {
            return this.table.getColumns().getCount();
        }

        @Override
        public String getColumnName(int columnIndex) {
            return this.table.getColumns().get(columnIndex).getName();
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return this.table.getColumns().get(columnIndex).getType();

        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return this.table.isCellEditable(rowIndex, columnIndex);
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return this.table.getRow(rowIndex).getValue(columnIndex);
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            this.table.getRow(rowIndex).beginEdit();
            this.table.getRow(rowIndex).setValue(aValue, columnIndex);
        }

        @Override
        public void addTableModelListener(TableModelListener l) {
            super.addTableModelListener(l);
            TableTableModelListener ttl = new TableTableModelListener(this,l);
            table.addDataModelListener(ttl);
            this.tmMap.put(l, ttl);

        }

        @Override
        public void removeTableModelListener(TableModelListener l) {
            super.removeTableModelListener(l);
            TableTableModelListener ttl = this.tmMap.get(l);
            table.removeDataModelListener(ttl);
            tmMap.remove(l);
        }
    }//class

    public static class TableTableModelListener implements TableListener {

        TableModelListener modelListener;
        TableModel tableModel;
        public TableTableModelListener(TableModel tableModel,TableModelListener l) {
            this.modelListener = l;
            this.tableModel = tableModel;
        }

        @Override
        public void tableChanged(TableEvent e) {
            TableModelEvent tme = new TableModelEvent(tableModel,
                    e.getChangedRow(),
                    e.getChangedRow(),
                    e.getChangedColumns(),
                    getType(e.getCause()));
            modelListener.tableChanged(tme);
        }


        protected int getType(TableEventCause cause) {
            if (cause == TableEventCause.insert) {
                return TableModelEvent.INSERT;
            }
            if (cause == TableEventCause.update) {
                return TableModelEvent.UPDATE;
            }
            if (cause == TableEventCause.delete) {
                return TableModelEvent.DELETE;
            }
            return -1;


        }
    }//class
    
}//class SimpleTable
