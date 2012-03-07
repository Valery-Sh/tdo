/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.service;

/**
 *
 * @author valery
 */
public interface CellServices {
    int getCellIndex(int columnIndex);
    int getColumnIndex(String columnName);
    int columnIndexByCell(int cellIndex);
    
    int getColumnCount();
    boolean isDataKind(int columnIndex);
    String getPropertyName(int columnIndex);
    String getFieldName(int columnIndex);    
    Object createBlankObject(int columnIndex);
    boolean copyCell(int columnIndex, Object source, Object target);
}
