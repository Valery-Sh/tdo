/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.service;

import java.io.Serializable;
import tdo.DataRow;
import tdo.TableView;

/**
 *
 * @author Valery
 */
public class DataRowViewServicesImpl implements DataRowViewServices,Serializable {

    private TableView tableView;
    public DataRowViewServicesImpl(TableView tableView) {
        this.tableView = tableView;
    }
    /**
     * Создает ряд для таблицы владельца, а не для представления.
     * @return
     */
    @Override
    public DataRow createViewOwnerRow() {
        return this.tableView.getViewOwner().createRow();
                
    }
/*
    @Override
    public DataRow createViewOwnerRow(Object[] values, int start, int end) {
        return this.tableView.createRow(values, start, end);
        return null;
    }
 */ 
/*
    @Override
    public DataRow createRowCopy(DataRow row) {
        return this.tableView.getViewOwner().createRowCopy(row);
    }
 */ 
/*    @Override
    public DataRow getViewOwnerRow() {
        return this.tableView.getViewOwner();
    }
*/
}
