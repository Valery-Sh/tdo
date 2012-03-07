/*
 * DataTableEventInfo.java
 *
 * Created on 25 ���� 2007 �., 17:41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.event;

/**
 *
 * @author Valera
 */
public interface DataTableEventInfo {
    boolean isBeforeOpen();
    boolean isAfterOpen();
    boolean isBeforeClose();
    boolean isAfterClose();
    
    /**
     * ���������� TableModelEvent.getType() == INSERT
     */
    boolean isInsert();
    /**
     * ���������� TableModelEvent.getType() == UPDATE
     */
    boolean isUpdate();
    /**
     * ���������� TableModelEvent.getType() == DELETE
     */
    boolean isDelete();
    
    /**
     * @return the column for the event. 
     * If the return value is ALL_COLUMNS; 
     * it means every column in the specified rows changed.
     */
     int getChangedColumns();
     int getChangedRow();
     
     
     
}//interface  
