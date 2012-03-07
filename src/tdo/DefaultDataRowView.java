/*
 * DefaultDataRowView.java
 * 
 */

package tdo;

import tdo.service.TableServices;
/**
 *
 * @author Valera
 */
public class DefaultDataRowView extends DefaultDataRow implements DataRowView{

    private TableServices viewOwnerContext;
    private DataRow viewOwnerRow;


    public DefaultDataRowView(TableServices context, TableServices viewOwnerContext, DataRow viewOwnerRow) {
        super(context);
        this.viewOwnerContext = viewOwnerContext;
        this.viewOwnerRow = viewOwnerRow;
    }

    @Override
    public DataRow getViewOwnerRow() {
        return this.viewOwnerRow;
    }
    @Override
    public TableServices getViewOwnerContext() {
        return this.viewOwnerContext;
    }

    /**
     * Выполняет копирование заданного ряда в текущий ряд. 
     * @param row копируемый ряд
     */
    @Override
    public void copyFrom(DataRow row) {
        viewOwnerRow.copyFrom( ((DataRowView)row).getViewOwnerRow() );
    }
}
