/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo;

import java.util.List;
import tdo.event.ActiveRowEvent;
import tdo.event.TableEvent;
import tdo.event.TableEvent.TableEventCause;

/**
 *
 * @author valery
 */
public class LinkedDataTable extends DataTable {
    protected DataTable parentTable;
    private String parentColumnName;
    protected String masterIdColumnName;    

    public String getParentColumnName() {
        return parentColumnName;
    }

/*    public void setParentColumnName(String parentColumnName) {
        this.parentColumnName = parentColumnName;
    }
*/
    protected <T> LinkedDataTable(DataTable parentTable, String parentColumnName, Class<T> clazz, String masterIdColumnName ) {
        super(clazz);
        this.parentTable = parentTable;
        this.parentColumnName = parentColumnName;
        this.masterIdColumnName = masterIdColumnName;
    }

    public DataTable getParentTable() {
        return parentTable;
    }
    protected void parentActiveRowChanged(ActiveRowEvent e) {
            String cn = getParentColumnName();
            DataRow r = parentTable.getRow(e.getNewValue());            
            List ls = (List)r.getValue(cn);
            if ( ls == null ) {
                ls = parentTable.getObjectList(beanClass, parentColumnName);
            }
            populate(ls);
            //!!comment the line below. if not then row selection disappeares
            //!!this.fireDataModel(new TableEvent(this, TableEvent.ALL_ROWS, TableEvent.ALL_COLUMNS, TableEventCause.schema));
    }
/*    public void setParentTable(DataTable parentTable) {
        this.parentTable = parentTable;
    }
*/
    @Override
    protected void objectListInsert(DataRow row) {
        if ( this.getObjectList() == null ) 
            return;
        this.getObjectList().add(row.getObject());
        Object value = parentTable.getActiveRow().getCellCollection().getValue(parentColumnName);
        if ( value == null )
            parentTable.getActiveRow().getCellCollection().setValue(this.objectList, parentColumnName);
        row.getCellCollection().setValue(parentTable.getActiveRow().getObject(), masterIdColumnName);    
    }
/*    @Override
    protected void notifyExternal(int kind, DataRow row, int columnInex) {
        switch (kind) {
            case DataRow.BEGINEDIT:
                //TODO
                break;
            case DataRow.ATTACH:
                if ( isLoading() )
                    break;
                objectListInsert(row);
                break;
            case DataRow.ATTACHNEW:
                objectListInsert(row);
                break;
            
            case DataRow.CANCELEDIT_RESET_ROW_VERSIONS:
                //TODO
                break;
            case DataRow.CANCELEDIT_INSERTING:
                break;
            case DataRow.CANCELEDIT:
                break;
            case DataRow.ENDEDIT_BEFORE:
                break;
            case DataRow.ENDEDIT_INSERTING:
                break;
            case DataRow.ENDEDIT_RESET_ROW_VERSIONS:
            case DataRow.ENDEDIT:
                break;
            case DataRow.DELETE:
        } //switch
    }
 */ 
    @Override
    public <T> void populate(List<T> rowList) {
        setLoading(true);
        this.objectList = null;       
        DataRow oRow;
        activeRow = -1;
        createInternalView();
        if ( rowList == null ) {
            objectList = null;
            return;
        }
        for (T obj : rowList ) {
            oRow = createRow(obj);
            this.addRow(oRow);
        }
        this.objectList = rowList;
        this.fireDataModel(new TableEvent(this, TableEvent.ALL_ROWS, TableEvent.ALL_COLUMNS, TableEventCause.schema));
        if ( getRowCount() != 0 )
            this.internalMoveTop();
        setLoading(false);
    }

    @Override
    public List getObjectList() {
        return this.objectList;
    }

}//class LinkeddataTable
