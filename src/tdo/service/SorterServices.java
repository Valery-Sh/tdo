/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.service;

import tdo.DataColumn;
import tdo.DataRow;

/**
 *
 * @author Valery
 */
public interface SorterServices {
    DataRow getRow(int rowIndex);
    void setRow(DataRow row, int rowIndex );
    int find(DataRow row);
    int getRowCount();
    int getColumnIndex(String columnName);
    DataColumn getColumn(int columnIndex);

    //Class getColumnType(String columnName);
    //boolean isOwnerOf(DataRow row);
    //String getTableName();

}
