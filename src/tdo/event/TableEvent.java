/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.event;

import java.util.EventObject;
import tdo.Table;

/**
 *
 * @author Valery
 */
public class TableEvent extends EventObject{
    /** Specifies all columns in a row or rows. */
    public static final int ALL_COLUMNS = -1;
    public static final int ALL_ROWS = -1;

    public enum TableEventCause {
        insert, newrow,update, delete,stoppending,schema
    }

    protected TableEventCause cause;
    protected int changedRow;
  //  protected int lastChangedRow;
    protected int changedColumns;

    public TableEvent(Table source, TableEventCause cause) {

        // Use Integer.MAX_VALUE instead of getRowCount() in case rows were deleted.
        this(source, ALL_ROWS, ALL_COLUMNS, cause);
    }


    public TableEvent(Table source, int changedRow, int changedColumns, TableEventCause cause) {
        super(source);
        this.changedRow = changedRow;
        //this.changedRow = changedRow;
        this.changedColumns = changedColumns;
        this.cause = cause;
    }

/*    public TableEvent(Table source, int changedRow, int lastChangedRow, int changedColumns, TableEventCause cause) {
        super(source);
        this.changedRow = changedRow;
        this.changedColumns = changedColumns;
        this.cause = cause;
    }
*/
    public TableEventCause getCause() {
        return this.cause;
    }
    public int getChangedRow() {
        return this.changedRow;
    }
    public int getChangedColumns() {
        return this.changedColumns;
    }
}//class TableEvent
