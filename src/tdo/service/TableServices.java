/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.service;

import java.io.Serializable;

/**
 *
 * @author valery
 */
public interface TableServices extends Serializable{
    CoreServices getCoreServices();   
    CellServices getCellServices();
    CellCollectionServices getCellCollectionServices();    
    
    ColumnServices getColumnServices();
    DataRowServices getDataRowServices();
    FilterServices getFilterServices();
    SorterServices getSorterServices();
    TreeSorterServices getTreeSorterServices();
    DataRowViewServices getDataRowViewServices();        
    RowCollectionServices getRowCollectionServices();            
    
}//interface TableServices
