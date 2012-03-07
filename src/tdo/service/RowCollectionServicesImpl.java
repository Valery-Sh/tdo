/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.service;


import tdo.DataRow;
import tdo.Table;

/**
 *
 * @author Valery
 */
public class RowCollectionServicesImpl implements RowCollectionServices {
    protected Table table; 
    
    public RowCollectionServicesImpl(Table table) {
        this.table = table;
    }

    @Override
    public DataRow createRow() {
        return this.table.createRow();
    }

/*    @Override
    public boolean isTableViewInstance() {
        return ( table instanceof TableView );
        
    }
*/
}
