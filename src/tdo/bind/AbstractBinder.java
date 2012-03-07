/*
 * AbstractBinder.java
 *
 * Created on 1 Ноябрь 2006 г., 15:09
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.bind;


import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import tdo.DataColumn;
import tdo.Table;
import tdo.event.ActiveRowEvent;
import tdo.event.TableEvent;
import tdo.event.TableEvent.TableEventCause;


/**
 *
 * @author Valera
 */
public abstract class AbstractBinder implements tdo.bind.Binder, ActionListener{
    private IPositionManager positionManager;
    private Component component;
    private String columnName;
    /** Creates a new instance of JTextFieldBinder */
    public AbstractBinder() {
    }
    @Override
    public IPositionManager getPositionManager() {
        return this.positionManager;
    }
    @Override
    public void setPositionManager( IPositionManager positionManager)  {
        this.positionManager = positionManager;
    }
    @Override
    public Component getComponent() {
        return this.component;
    }
    @Override
    public void setComponent(Component comp) {
        this.component = comp;
        initBinder();
    }
    @Override
    public String getColumnName() {
        return this.columnName;
    }
    @Override
    public void setColumnName(String columnName) {
        if ( columnName == null)
            return;
        this.columnName = columnName.toUpperCase();
    }
    
    /**
     * Слушает событие изменения активной записи. <p>
     * @param e
     */
    @Override
    public void activeRowChange( ActiveRowEvent e) {
        Table dataTable = this.positionManager.getTable();
        if ( dataTable == null )
            return;
        int rowNo = dataTable.getActiveRowIndex();
        if ( rowNo < 0 )
            return;
        
        Object obj = null;
        DataColumn column = null;
        if ( columnName != null ) {
            obj = dataTable.getRow(rowNo).getValue(columnName);
            column = dataTable.getColumns().get(columnName);
        }
        if ( e.isChanging())
            componentChanging( obj, column, rowNo);
        else
            componentChange( obj, column, rowNo);
    }
/*    @Override
    public void activeRowChanging( ActiveRowEvent e) {
        AbstractTable dataTable = this.positionManager.getTable();
        if ( dataTable == null || dataTable.isClosed() )
            return;
        int rowNo = dataTable.getActiveRowIndex();
        if ( rowNo < 0 )
            return;

        Object obj = null;
        DataColumn column = null;
        if ( columnName != null ) {
            obj = dataTable.getRow(rowNo).getValue(columnName);
            column = dataTable.getColumns().get(columnName);
        }
        componentChanging( obj, column, rowNo);

    }
*/
    /**
     * Обновление состояния компонента в результате изменения позиции активной записи
     * BaseDataTable. <p>
     * Классы наследники переопределяют метод для изменения состояния компонента
     * и приведения его в соответстие с новым активным рядом BaseDataTable.
     * <p>
     *
     *
     *
     * @param value - тип Object. Новое значение из нового активного ряда rowIndex PDatatable,
     * в ячейке, определяемой параметром column.
     * @param column - тип DataColumn. Колонка, связанная с компонентом.
     * @param rowIndex - тип int. Новый активный ряд BaseDataTable.
     */

    protected void componentChange( Object value,  DataColumn column, int rowIndex ) {
    }
    /**
     * Обновление состояния компонента перед изменения позиции активной записи
     * BaseDataTable. <p>
     * Классы наследники переопределяют метод для изменения состояния
     * компонента. Например, ряд <code>javax.swing.JTable</code>, может
     * находится  в состоянии редактирования, т.е.
     * <code>JTable.getEditingRow >= 0</code>. В таком случае, необходимо
     * принудительно установить значение ячейки ряда методом
     * <code>DataRow.setValue</code>.
     *
     * @param value - тип Object. Новое значение из нового активного ряда rowIndex PDatatable,
     * в ячейке, определяемой параметром column.
     * @param column - тип DataColumn. Колонка, связанная с компонентом.
     * @param rowIndex - тип int. Новый активный ряд BaseDataTable.
     * @see tdo.swing.bind.JTableBinder
     */
    protected void componentChanging( Object value,  DataColumn column, int rowIndex ) {
    }

    /**
     * Изменяет значение в колонке dataTable, заданной параметрами columnIndex и rowIndex.<p>
     * Классы наследники, например, JTextField, переопределяют метод для преобразования своей модели
     * * данных в значение поля dataTable. Для реального проведения изменения следует использовать метод
     * * setValueAt класса PositionManager.
     * 
     * 
     * @param dataTable - тип BaseDataTable. Таблица данных, связанная с PositionManager.
     * @param rowIndex - тип int. Индекс активного ряда.
     * @param columnIndex - тип int. Индекс колонки активного ряда, связанной со свойством text компонента
     * JTextField.
     */
    protected void setValue( Table dataTable, int rowIndex, int columnIndex) {
        
    }
    
    protected void rowInserted(int rowIndex) {
        
    }
    protected void rowDeleted(int rowIndex) {
        
    }
    protected void rowUpdated(int rowIndex) {
        //componentChange(null,null, rowIndex);                        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Table dataTable = this.positionManager.getTable();
        int rowIndex = dataTable.getActiveRowIndex();
        if ( rowIndex < 0 )
            return;
        int columnIndex = dataTable.getColumns().find(this.columnName);
        setValue( dataTable, rowIndex, columnIndex);
    }
    
    @Override
    public void tableChanged(TableEvent e ) {
        Table dataTable = this.positionManager.getTable();
        if ( e.getCause() == TableEventCause.update ) {
            if ( e.getChangedColumns() == TableEvent.ALL_COLUMNS ) {
                if ( e.getChangedRow() == dataTable.getActiveRowIndex() )
                    rowUpdated(e.getChangedRow());
                //componentChange(null,null, e.getChangedRow());                
                //return;
            } else {
                DataColumn column = dataTable.getColumns().get(e.getChangedColumns());
                if ( column.getName().equals(this.columnName)) {
                    Object o = dataTable.getRow(e.getChangedRow()).getValue(e.getChangedColumns());
                    componentChange(o,column, e.getChangedRow());
                }
            }
        }
        if ( e.getCause() == TableEventCause.insert ) {
            //rowInserted(e.getChangedRow());
        }
        if ( e.getCause() == TableEventCause.newrow ) {
            rowInserted(e.getChangedRow());
        }
        
        if ( e.getCause() == TableEventCause.delete ) {
            rowDeleted(e.getChangedRow());
/*            if ( this.columnName != null ) {
                rowDeleted(e.getLastRow());
            }
 */
        }
        
/*        if ( e.isAfterClose() ) {
            this.component.repaint();
        }
        if ( e.isAfterOpen() ) {
            this.component.repaint();
        }
*/
    }
    
    protected void initBinder() {
        
    }
    @Override
    public void removing() {
        
    }
    
    @Override
    public void dataTableChanged( Table newDataTable) {
        
    }
    @Override
    public void dataTableChanging(Table oldDataTable, Table newDataTable) {
        
    }
    
}//class
