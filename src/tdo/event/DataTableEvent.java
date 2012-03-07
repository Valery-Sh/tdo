package tdo.event;
import tdo.Table;

/**
 * <p>Title: Filis Application</p>
 * <p>Description: Freq Sensor's Support</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: IS</p>
 * @author VNS
 * @version 1.0
 */

public class DataTableEvent extends java.util.EventObject 
    implements DataTableEventInfo {
    public static final int BEFORE_OPEN  = 2;
    public static final int AFTER_OPEN   = 3;
    public static final int BEFORE_CLOSE = -2;
    public static final int AFTER_CLOSE  = -3;
    
    /** Identifies the addtion of new rows or columns. */
    public static final int INSERT =  1;
    public static final int NEWROW =  11;
    /** Identifies a change to existing data. */
    public static final int UPDATE =  0;
    /** Identifies the removal of rows or columns. */
    public static final int DELETE = -1;
    
    /** Identifies the header row. */
    public static final int HEADER_ROW = -1;
    
    /** Specifies all columns in a row or rows. */
    public static final int ALL_COLUMNS = -1;
    public static final int ALL_ROWS = -1;
    
    protected int changedRow;
    protected int changedColumns;
    protected int type;
    
    public DataTableEvent(Table source, int type) {
        
        // Use Integer.MAX_VALUE instead of getRowCount() in case rows were deleted.
        this(source, ALL_ROWS, ALL_COLUMNS, type);
    }
    
    
    public DataTableEvent(Table source, int changedRow, int changedColumns, int type) {
        super(source);
        this.changedRow = changedRow;
        this.changedColumns = changedColumns;
        this.type = type;
    }
    
    public int getType() {
        return this.type;
    }

    @Override
    public boolean isBeforeOpen() {
        return type == BEFORE_OPEN;
    }

    @Override
    public boolean isAfterOpen() {
        return type == AFTER_OPEN;
    }

    @Override
    public boolean isBeforeClose() {
        return type == BEFORE_CLOSE;
    }

    @Override
    public boolean isAfterClose() {
        return type == AFTER_OPEN;
    }

    @Override
    public boolean isInsert() {
        return type == INSERT;
    }
    public boolean isNewRow() {
        return type == NEWROW;
    }

    @Override
    public boolean isUpdate() {
        return type == UPDATE;
    }

    @Override
    public boolean isDelete() {
        return type == DELETE;
    }

    @Override
    public int getChangedColumns() {
        return this.changedColumns;
    }

    @Override
    public int getChangedRow() {
        return this.changedRow;
    }
}//class