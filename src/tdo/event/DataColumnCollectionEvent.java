package tdo.event;

import java.util.EventObject;
import tdo.DataColumn;
import tdo.DataColumnCollection;

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
public class DataColumnCollectionEvent extends EventObject {
  
  public static final int COLUMN_ADDED = 1;  
  public static final int COLUMN_REMOVED = -1;  
  public static final int COLUMN_MOVED = 2;  
  
  private int columnsAction;
  private DataColumnCollection columns;
  private DataColumn column;
  private int columnIndex;
  //private int newColumnIndex;
  

  public DataColumnCollectionEvent(DataColumnCollection source, DataColumn column, int columnsAction ) {
    super(source);
    this.columns = source;
    this.column = column;
    this.columnsAction = columnsAction;
    //column.getIndex();    
  }
  public DataColumnCollectionEvent(DataColumnCollection source, DataColumn column, int fromColumnIndex, int columnsAction ) {
    super(source);
    this.columns = source;
    this.column = column;
    this.columnsAction = columnsAction;
    this.columnIndex = fromColumnIndex;
    //this.newColumnIndex = -1;
    
  }
/*
  public DataColumnCollectionEvent(DataColumnCollection source, DataColumn column, int columnIndex, int newColumnIndex, int columnsAction ) {
    super(source);
    this.columns = source;
    this.column = column;
    this.columnsAction = columnsAction;
    this.columnIndex = columnIndex;
    this.newColumnIndex = newColumnIndex;
    
  }
*/  
  public int getColumnsAction() {
      return this.columnsAction;
  }
  public void setColumnsAction( int columnsAction) {
      this.columnsAction = columnsAction;
  }
  
  public DataColumnCollection getColumns() {
      return this.columns;
  }
  public void setColumns( DataColumnCollection columns) {
      this.columns = columns;
  }
  public DataColumn getColumn() {
      return this.column;
  }
  public void setColumn( DataColumn column) {
      this.column = column;
  }
  public int getColumnIndex() {
      return this.columnIndex;
  }
  public void setColumnIndex( int columnIndex) {
      this.columnIndex = columnIndex;
  }
/*  public int getNewColumnIndex() {
      return this.newColumnIndex;
  }
  public void setNewColumnIndex( int newColumnIndex) {
      this.newColumnIndex = newColumnIndex;
  }
*/  
}//class