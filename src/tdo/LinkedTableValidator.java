/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo;

import java.util.List;
import tdo.impl.RowValidator;
import tdo.impl.ValidationManager;
import tdo.impl.Validator;

/**
 *
 * @author Valery
 */
public class LinkedTableValidator extends RowValidator {
    private DataTable table;
    private List<LinkedDataTable> linkedTables;   
    private Validator childValidator;
    private int childValidatorIndex;
    private DataTable childTable;
    private DataRow childRow;   
    private DataRow masterRow;
    private String masterMessage;

    public String getMasterMessage() {
        String s = this.getMessage();
        this.setMessage(masterMessage);
        String result = table.getValidationManager().formatMessage(masterRow, this);
        this.setMessage(s);
        return result;
    }

    public void setMasterMessage(String masterMessage) {
        this.masterMessage = masterMessage;
    }

    public int getChildValidatorIndex() {
        return childValidatorIndex;
    }
    
    
    /**
     * —оздает экземпл€р класса дл€ заданной таблицы.<p>
     * @param tableServices сервисы таблицы, р€д которой провер€етс€ на валидность
     */
    public LinkedTableValidator(DataTable table, List<LinkedDataTable> linkedTables) {
        super(table.getContext());
        this.table = table;
        this.linkedTables = linkedTables;
    }
    public Table getChildTable() {
        return childTable;
    }

    public Validator getChildValidator() {
        return childValidator;
    }

    public Table getTable() {
        return table;
    }
    
    @Override
    public boolean validate(DataRow row) {
        this.masterRow = row;
        boolean result = true;
        
        if ( linkedTables == null || linkedTables.isEmpty() )
            return true;
        for ( LinkedDataTable lt : linkedTables  ) {
            int a = lt.getActiveRowIndex();
            if ( a >= 0 ) {
                DataRow r = lt.getRow(a); 
                ValidationManager vm = lt.getValidationManager();
                if ( vm == null)
                    continue;
                childValidator = vm.validateSilent(r);
                if ( childValidator != null  ) {
                    childTable = lt;
                    childRow = r;
                    childValidatorIndex = vm.getRowValidators().indexOf(childValidator);
                    result = false;
                    break;
                }
            }
        }
        if ( ! result  ) {
            this.setMessage(
               childTable.getValidationManager().formatMessage(childRow, childValidator)
               );
        }
        return result;
        
    }
}
