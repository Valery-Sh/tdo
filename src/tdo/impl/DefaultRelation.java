/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.impl;

import tdo.CollectionFactory;
import tdo.DataRow;
import tdo.DataRowCollection;
import tdo.Table;
import tdo.util.Strings;

/**
 *
 * @author Valery
 */
public class DefaultRelation implements Relation{
    private Table parentTable;
    private Table table;
    //private InternalView dataView;
    //private DataRowCollection emptyrow;
    private String keyList;
    private String parentKeyList;
    private String[] keyColumns;
    private String[] parentKeyColumns;
//    private Object[] values;

    /**
     * Creates a new instance of DataRelation
     */
    public DefaultRelation() {
        this(null,null,null,null);
    }
/*    public DefaultRelation(InternalView dataView) {
        this.dataView = dataView;
        source = dataView.getRows();
        relListeners = new Vector(1);
    }
*/
    public DefaultRelation(Table table, Table parentTable, String keyList, String parentKeyList) {
        this.table = table;
        this.parentTable = parentTable;
        this.setKeyList(keyList);
        this.setParentKeyList(parentKeyList);
        
    }
    /*************************************************************************
     * Реализация интерфейса Relation.
     ************************************************************************
     * @return
     */
/*    public InternalView getDataView() {
        return this.dataView;
    }
    public void setDataView(InternalView dataView) {
        this.dataView = dataView;
    }
*/
    @Override
    public Table getTable() {
        return this.table;
    }
    @Override
    public void setTable(Table table) {
        this.table = table;
    }

    /**
     *
     * @return
     */
    @Override
    public Table getParentTable() {
        return this.parentTable;
    }

    /**
     *
     * @param parentTable
     */
    @Override
    public void setParentTable(Table parentTable) {
        this.parentTable = parentTable;
    }

    /**
     *
     * @return
     */
    @Override
    public String getKeyList() {
        return this.keyList;
    }
    @Override
    public void setKeyList( String keyList ) {
        this.keyList = keyList.toUpperCase();
        this.keyColumns = Strings.split( keyList );
    }

    @Override
    public String getParentKeyList() {
        return this.keyList;
    }
    @Override
    public void setParentKeyList( String parentKeyList ) {
        this.parentKeyList = parentKeyList.toUpperCase();
        this.parentKeyColumns = Strings.split(parentKeyList);
    }

    @Override
    public Object[] getValues() {
        int rowIndex = this.parentTable.getActiveRowIndex();
        if ( rowIndex < 0 )
            return null;
        Object[] vo = new Object[this.keyColumns.length];
        for ( int i=0; i < vo.length; i++ ) {
            vo[i] = parentTable.getRow(rowIndex).getValue(this.parentKeyColumns[i] );
        }
        return vo;
    }

/*    @Override
    public void setValues( Object[] values) {
        this.values = values;
    }
*/
    @Override
    public String[] getKeyColumns() {
        return this.keyColumns;
    }

    @Override
    public String[] getParentKeyColumns() {
        return this.parentKeyColumns;
    }

/*    @Override
    public void refreshView(InternalView view) {
        this.source = view.getCurrentRows();
    }
*/


    //public InternalRowCollection getRows() {

    @Override
    public DataRowCollection getRows() {

        DataRowCollection ds = CollectionFactory.newInstance().newRowCollection();
        Object[] values = getValues();
        if ( table.getRowCount() == 0 || values == null )
            return ds;

        boolean found;
//        for ( int i=0; i < dataView.getTable().getRowCount(); i++ ) {
        for ( int i=0; i < table.getRowCount(); i++ ) {
            found = false;
            DataRow row = table.getRow(i);
            for ( int j=0; j < keyColumns.length ; j++) {
                Object o = row.getValue(keyColumns[j]);
                if ( o != null && ! o.equals(values[j]) ) {
                    found = false;
                    break;
                }
                if ( o == null && values[j] != null  ) {
                    found = false;
                    break;
                }
                found = true;
            }//for

            if ( found ) {
                ds.add(row);
            }
        }//for
        return ds;
    }

}//class

