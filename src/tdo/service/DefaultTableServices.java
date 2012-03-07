/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.service;

import java.io.Serializable;
import tdo.DataTable;
import tdo.SimpleTable;
import tdo.Table;
import tdo.TableView;

/**
 *
 * @author valery
 */
public class DefaultTableServices implements TableServices,Serializable {
     //protected TableServices tableServices;
     
     protected ColumnServices columnServices;     
     protected CellServices cellServices;     
     protected CellCollectionServices cellCollectionServices;     

     protected CoreServices coreServices;          
     
     protected DataRowServices dataRowServices;                    
     protected DataRowViewServices dataRowViewServices;                    
     protected FilterServices filterServices;
     protected SorterServices sortServices;
     protected TreeSorterServices treeSorterServices;

     protected RowCollectionServices rowCollectionServices;                    
     
     public DefaultTableServices(Table table) {
         
         //this.tableServices = new TableServicesImpl(table);
         this.columnServices = new ColumnServicesImpl(table);
         this.cellServices = new CellServicesImpl(table);         
         this.coreServices = new CoreServicesImpl(table);
         //this.rowServices = new RowServicesImpl(table);         
         this.dataRowServices = new DataRowServicesImpl(table);                  
         this.rowCollectionServices = new RowCollectionServicesImpl(table); 
         this.filterServices = new FilterServicesImpl(table);
         this.sortServices = new SorterServicesImpl(table);
         this.treeSorterServices = new TreeSorterServicesImpl(table);
         if ( table.getClass().equals(SimpleTable.class) )
             this.cellCollectionServices = new CellCollectionServicesImpl(table);
         
         if ( table instanceof DataTable  )
             this.cellCollectionServices = new CellCollectionServicesImpl(table);
         
         if ( table instanceof TableView )
            this.dataRowViewServices = new DataRowViewServicesImpl((TableView)table);                           
         else
             this.dataRowViewServices = null;
     }
     
    @Override
     public ColumnServices getColumnServices() {
         return this.columnServices;
     }

    @Override
    public CoreServices getCoreServices() {
         return this.coreServices;
    }
/*
    @Override
    public RowServices getRowServices() {
        return this.rowServices;
    }
*/
    @Override
    public DataRowServices getDataRowServices() {
        return this.dataRowServices;
    }

    @Override
    public DataRowViewServices getDataRowViewServices() {
        return this.dataRowViewServices;
    }

    @Override
    public RowCollectionServices getRowCollectionServices() {
        return this.rowCollectionServices;
    }

    @Override
    public CellServices getCellServices() {
        return this.cellServices;
    }

    @Override
    public CellCollectionServices getCellCollectionServices() {
        return this.cellCollectionServices;
    }

    @Override
    public FilterServices getFilterServices() {
        return this.filterServices;
    }

    @Override
    public SorterServices getSorterServices() {
        return this.sortServices;
    }
    @Override
    public TreeSorterServices getTreeSorterServices() {
        return this.treeSorterServices;
    }
    
}
