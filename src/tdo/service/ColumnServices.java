/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.service;

import tdo.DataColumn;
import tdo.DataRow;

/**
 *
 * @author valery
 */
public interface ColumnServices {
    
    boolean isCellEditable(int columnIndex);

    boolean isNullable(int columnIndex);

    boolean isCalculated(int columnIndex);

    Object calculateColumnValue(DataRow row, int columnIndex);

    int getColumnIndex(String columnName);
    
    int getColumnCount(int kind);
    
    int getColumnCount();  
    
    boolean hasColumns();
    
    DataColumn columns(int columnIndex);
    
    DataColumn columns(String columnName);    

}
