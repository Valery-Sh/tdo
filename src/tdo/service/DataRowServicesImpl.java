/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.service;

import java.io.Serializable;
import tdo.DataColumn;
import tdo.DataRow;
import tdo.Table;
import tdo.impl.ValidationManager;
/**
 *
 * @author valery
 */
public class DataRowServicesImpl implements DataRowServices,Serializable{
    protected Table table; 
    
    public DataRowServicesImpl(Table table) {
        this.table = table;
    }

    @Override
    public boolean isLoading() {
        return table.isLoading();
    }

    @Override
    public boolean isEditProhibited() {
        return table.isEditProhibited();
    }

    @Override
    public boolean isNullable(int columnIndex) {
        return this.table.getColumns().get(columnIndex).isNullable();
    }
    
    @Override
    public int getColumnIndex(String columnName) {
        return this.table.getColumns().find(columnName);
    }
    
    @Override
    public int getRowIndex(DataRow row) {
        return table.find(row);
    }

    @Override
    public boolean processRowEditing(int cause, DataRow row, int columnIndex) {
        return this.table.processRowEditing(cause, row, columnIndex);
    }

/*    @Override
    public void fireRowEditing(int cause, DataRow row) {
        this.table.fireRowEditing(cause, row);
    }
*/
    @Override
    public boolean isCellEditable(int columnIndex) {
        return this.table.isCellEditable(columnIndex);
    }

 /*   @Override
    public void fireValidate(DataRow row) {
        this.table.fireValidate(row);
    }
*/
    @Override
    public boolean isCalculated(int columnIndex) {
        return this.table.getColumns().get(columnIndex).getKind() == DataColumn.CALC_KIND;
    }

    @Override
    public Object calculateColumnValue(DataRow row, int columnIndex) {
        return this.table.calculateColumnValue(row, columnIndex);
    }
    
    @Override
    public Object toColumnType(Object value, int columnIndex) {
        return this.table.getColumns().get(columnIndex).toColumnType(value);
    }

    @Override
    public DataRow createRow() {
        return table.createRow();
    }

   @Override
    public boolean validate(DataRow row, String columnName, Object value) {
       if ( table.getValidationManager() == null )
           return true;
        return table.getValidationManager().validate(row,columnName, value);
    }

    @Override
    public boolean validate(DataRow row, boolean throwEx) {
        return table.getValidationManager().validate(row,throwEx);
    }

    @Override
    public String getColumnName(int columnIndex) {
        return this.table.getColumns().get(columnIndex).getName();
    }

    @Override
    public boolean isValidationEnabled() {
        return table.isValidationEnabled();
    }

    @Override
    public ValidationManager getValidationManager() {
        return table.getValidationManager();
    }
    
    

}
