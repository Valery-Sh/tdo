/*
 * Table.java
 *
 */
package tdo;

import javax.swing.table.TableModel;
import tdo.event.ActiveRowListener;
import tdo.event.PendingEditingListener;
import tdo.event.TableEvent;
import tdo.event.TableListener;
import tdo.impl.ValidationManager;
import tdo.service.TableServices;

public interface Table extends TableModel{

    static final int ALLOW_UPDATE_ACTIVE_ROW = 0;
    static final int ALLOW_UPDATE_ALL_ROWS = 1;

    DataRow addRow();

    DataRow addRow(Object[] values);

    int addRow(DataRow row);

    void addActiveRowListener(ActiveRowListener listener);

    void removeActiveRowListener(ActiveRowListener listener);

    void addTableListener(TableListener l);

    void removeTableListener(TableListener l);

    void addDataModelListener(TableListener l);

    void removeDataModelListener(TableListener l);

    void addPendingEditingListener(PendingEditingListener l);

    void removePendingEditingListener(PendingEditingListener l);

    Object calculateColumnValue(int rowIndex, int columnIndex);

    Object calculateColumnValue(DataRow row, int columnIndex);

    void clear();

    DataRow createRow();

    Table createTable();

    //Table createTreeTable();
    int find(DataRow row);

    /**
     * ¬озвращает индекс заданного р€да в в заданной коллекции.
     * 
     * @param row р€д, индекс которого определ€етс€.
     *  
     */
//    int find(DataRow row,DataRowCollection rc);
    //void fireValidate(DataRow row);

    //void fireRowEditing(int kind, DataRow row);

    boolean processRowEditing(int kind, DataRow row, int columnIndex);

    //void fireRowInserted(Table table, int rowIndex);

    void fireRowInserted(Table table, int rowIndex, DataRow row);

    void fireTable(TableEvent e);

    void fireDataModel(TableEvent e);

    /**
     * ќповещает, что добавлен или вставлен новый пустой р€д. <p>
     * ¬ отличии от методов fireRowInserted, которые предназначены дл€
     * оповещени€ моделей данных визуальных компонент 
     * !!!!! TODO
     *  
     * @param rowIndex
     */
    void fireRowInserting(int rowIndex);

    //Class getColumnClass(int columnIndex);
    DataColumnCollection getColumns();

    void setColumns(DataColumnCollection columns);

    int getActiveRowIndex();

    void setActiveRowIndex(int arow);

    DataRow getRow(int rowIndex);

    int getRowCount();

    String getTableName();

    int getUpdatePolicy();

    void insertRow(int rowIndex);

    void insertRow(int rowIndex, DataRow row);

    boolean isCellEditable(int columnIndex);

    /**
     * Returns true if the cell at <code>rowIndex</code> and
     * <code>columnIndex</code>
     * is editable.  Otherwise, <code>setValueAt</code> on the cell will not
     * change the value of that cell.
     *
     * @param	rowIndex	the row whose value to be queried
     * @param	columnIndex	the column whose value to be queried
     * @return	true if the cell is editable
     * @see #setValueAt
     */
    boolean isCellEditable(int rowIndex, int columnIndex);

    boolean isEditProhibited();

    boolean isLoading();

    void setLoading(boolean loading);

    public boolean isValidationEnabled();

    void reset();

    void setTableName(String tableName);

    public void setUpdatePolicy(int updatePolicy);

    void setRow(DataRow row, int rowIndex);

    public void setValidationEnabled(boolean value);

    //public boolean validate();
    //boolean validate(int rowIndex, boolean throwEx);

/*    boolean validate(DataRow row, boolean throwEx);

    boolean validate(DataRow row, String columnName, Object value);
 */
    ValidationManager getValidationManager();
    TableServices getContext();


} //class