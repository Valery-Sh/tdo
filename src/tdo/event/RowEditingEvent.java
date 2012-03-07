/*
 * RowEditingEvent.java
 * 
 * Created on 08.10.2007, 10:21:33
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.event;

import java.util.EventObject;
import tdo.DataRow;

/**
 *
 * @author valery
 */
public class RowEditingEvent extends EventObject{
    private int kind;
    private DataRow row;
    private int columnIndex;
    
    public RowEditingEvent(Object source) {
        this(source,DataRow.UNKNOWN,null,-1);
    }

    public RowEditingEvent(Object source, int kind, DataRow row, int columnIndex) {
        super(source);
        this.kind = kind;
        this.row = row;
        this.columnIndex = columnIndex;
    }
    
    public int getKind() {
        return this.kind;
    }
    public void setKind(int kind) {
        this.kind = kind;
    }
    
    public int getColumnIndex() {
        return this.columnIndex;
    }
    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    public DataRow getRow() {
        return this.row;
    }
    public void setRow(DataRow row) {
        this.row = row;
    }
}//RowEditingEvent
