/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.service;

import tdo.DataRow;

/**
 *
 * @author Valery
 */
public interface FilterServices {
    DataRow getRow(int rowIndex);
    int getRowCount();
    Class getColumnType(String columnName);
    //boolean isOwnerOf(DataRow row);
    String getTableName();
}
