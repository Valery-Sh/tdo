/*
 * TableModelEventEx.java
 *
 * Created on 26 Март 2007 г., 10:56
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.event;

import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;

/**
 *
 * @author valery
 */
public class TableModelEventEx extends TableModelEvent {
    
    /** 
     *  All row data in the table has changed, listeners should discard any state 
     *  that was based on the rows and requery the <code>TableModel</code>
     *  to get the new row count and all the appropriate values. 
     *  The <code>JTable</code> will repaint the entire visible region on
     *  receiving this event, querying the model for the cell values that are visible. 
     *  The structure of the table ie, the column names, types and order 
     *  have not changed.  
     * @param source 
     */
    public TableModelEventEx(TableModel source) {
        // Use Integer.MAX_VALUE instead of getRowCount() in case rows were deleted. 
	this(source, 0, Integer.MAX_VALUE, ALL_COLUMNS, UPDATE);
    }
    
    /**
     *  This row of data has been updated. 
     *  To denote the arrival of a completely new table with a different structure 
     *  use <code>HEADER_ROW</code> as the value for the <code>row</code>. 
     *  When the <code>JTable</code> receives this event and its
     *  <code>autoCreateColumnsFromModel</code> 
     *  flag is set it discards any TableColumns that it had and reallocates 
     *  default ones in the order they appear in the model. This is the 
     *  same as calling <code>setModel(TableModel)</code> on the <code>JTable</code>. 
     * @param source 
     * @param row 
     */
    public TableModelEventEx(TableModel source, int row) {
	this(source, row, row, ALL_COLUMNS, UPDATE);
    }
    
    /**
     *  The data in rows [<I>firstRow</I>, <I>lastRow</I>] have been updated. 
     * @param source 
     * @param firstRow 
     * @param lastRow 
     */
    public TableModelEventEx(TableModel source, int firstRow, int lastRow) {
	this(source, firstRow, lastRow, ALL_COLUMNS, UPDATE);
    }
    
    /**
     *  The cells in column <I>column</I> in the range 
     *  [<I>firstRow</I>, <I>lastRow</I>] have been updated. 
     * @param source 
     * @param firstRow 
     * @param lastRow 
     * @param column 
     */
    public TableModelEventEx(TableModel source, int firstRow, int lastRow, int column) {
	this(source, firstRow, lastRow, column, UPDATE);
    }
    
    /**
     *  The cells from (firstRow, column) to (lastRow, column) have been changed. 
     *  The <I>column</I> refers to the column index of the cell in the model's 
     *  co-ordinate system. When <I>column</I> is ALL_COLUMNS, all cells in the 
     *  specified range of rows are considered changed. 
     *  <p>
     *  The <I>type</I> should be one of: INSERT, UPDATE and DELETE. 
     * @param source 
     * @param firstRow 
     * @param lastRow 
     * @param column 
     * @param type 
     */
    public TableModelEventEx(TableModel source, int firstRow, int lastRow, int column, int type) {
	super(source);
	this.firstRow = firstRow;
	this.lastRow = lastRow;
	this.column = column;
	this.type = type;
    }
  
    public void setType(int type) {
        this.type = type;
    }
    public void setColumn(int column) {
        this.column = column;
    }
    public void setFirstRow(int firstRow) {
        this.firstRow = firstRow;
    }
    public void setLastRow(int lastRow) {
        this.lastRow = lastRow;
    }
    
}
