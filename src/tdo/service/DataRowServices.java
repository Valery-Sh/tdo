/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.service;
import tdo.DataRow;
import tdo.impl.ValidationManager;
/**
 *
 * @author valery
 */
public interface DataRowServices {
    
    
    boolean isLoading();

    boolean isEditProhibited();
    
    boolean isCellEditable(int columnIndex);
    
    boolean isCalculated(int columnIndex);
    
    Object calculateColumnValue(DataRow row, int columnIndex);
    
    DataRow createRow();
    
    Object toColumnType(Object value, int columnIndex);

    boolean isNullable(int columnIndex);

    boolean isValidationEnabled();
    
    //boolean isValidationEnabled();

    int getColumnIndex(String columnName);
    String getColumnName(int columnIndex);
    
    int getRowIndex(DataRow row);
    
    boolean processRowEditing(int cause, DataRow row, int columnIndex);
    
    //void fireRowEditing(int cause, DataRow row);
    
    //void fireValidate(DataRow row);
    ValidationManager getValidationManager();
    
    boolean validate(DataRow row, String columnName, Object value);
    boolean validate(DataRow row, boolean throwException);

    //boolean validate(DataRow row);


}
