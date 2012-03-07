package tdo.event;

import java.util.EventObject;
import tdo.DataColumn;
import tdo.DataRow;

/**
 * ¬озбуждаетс€ при изменении значени€ колонки в PDataStore. <p>
 * ќбычно используетс€ дл€ обработки вычисл€емых полей.<p>
 *
 * <p>Title: Filis Application</p>
 * <p>Description: Freq Sensor's Support</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: IS</p>
 * @author VNS
 * @version 1.0
 */
public class ColumnValueChangedEvent extends EventObject {
//  private Object source;
  private DataRow row;
  private DataColumn column;
  int rowIndex;
  int columnIndex;
  Object oldValue;
  Object newValue;

  public ColumnValueChangedEvent() {
    this( null , null, null , -1, -1, null, null );
    
  }

  public ColumnValueChangedEvent(Object source) {
    this( source , null, null , -1, -1, null, null );
  }

  public ColumnValueChangedEvent(Object source, DataRow row, DataColumn column,
                             int rowIndex, int columnIndex,
                             Object oldValue, Object newValue ) {
    super(source);
  }

  public void setSource( Object source ) {
    this.source = source;
  }
  
  public DataColumn getColumn() {
    return column;
  }
  public void setColumn(DataColumn column) {
    this.column = column;
  }
  public DataRow getRow() {
      return this.row;
  }
  public void setRow(DataRow row) {
      this.row = row;
  }
    
  public int getColumnIndex() {
    return columnIndex;
  }
  public void setColumnIndex(int columnIndex) {
    this.columnIndex = columnIndex;
  }
  public Object getNewValue() {
    return newValue;
  }
  public void setNewValue(Object newValue) {
    this.newValue = newValue;
  }
  public Object getOldValue() {
    return oldValue;
  }
  public void setOldValue(Object oldValue) {
    this.oldValue = oldValue;
  }
  public int getRowIndex() {
    return rowIndex;
  }
  public void setRowIndex(int rowIndex) {
    this.rowIndex = rowIndex;
  }
/*  public Object getSource() {
    return source;
  }
*/
}