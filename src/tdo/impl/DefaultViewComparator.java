/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.impl;

import tdo.DataColumn;
import tdo.DataRow;
import tdo.service.SorterServices;
import tdo.service.TableServices;
import tdo.util.Strings;

/**
 *
 * @author valery
 */
public class DefaultViewComparator {
    private String columnList;
    private String[] columnNames;
    //private SimpleInternalView table;
    private TableServices tableServices;
    private boolean[] caseSensitive;
    private boolean[] sortDirection;
    private boolean listSortDirection;    
    private int[] keys;

    protected DataColumn[] columns;

/*    public DefaultViewComparator() {
        columnNames = new String[0];
    }

    public DefaultViewComparator(String columnList ) {
        this(null,columnList,Sorter.ASCENDING);
    }
    public DefaultViewComparator(String columnList, boolean listSortDirection ) {
        this(null, columnList,listSortDirection);
    }
*/
    public DefaultViewComparator(TableServices services,String columnList, boolean listSortDirection ) {
        this.tableServices = services;
        this.columnList = columnList;
        this.listSortDirection = listSortDirection;
        buildColumnNames();
        this.setTableServices(tableServices);
    }
    
    public int[] getKeys() {
        return this.keys;
    }
    public void setKeys( int[] keys ) {
        this.keys = keys;
    }

    public String getColumnList() {
        return this.columnList;
    }
    public void setColumnList( String columnList ) {
        this.columnList = columnList.toUpperCase();
        buildColumnNames();
        createColumns();
    }
/*    public void setSortDirection(boolean listSortDirection) {
        this.listSortDirection = listSortDirection;
    }
 */ 
    private void buildColumnNames() {
        columnList = Strings.compressSpaces(columnList).trim();
        String[] s = Strings.split(columnList.toUpperCase());
        sortDirection = new boolean[s.length];
        caseSensitive = new boolean[s.length];

        this.columnNames = new String[s.length];
        for ( int i=0; i < s.length; i++ ) {
            sortDirection[i] = Sorter.ASCENDING;
            caseSensitive[i] = true;

            String s1[] = Strings.split( s[i], " " );

            columnNames[i] = s1[0];

            sortDirection[i] = this.listSortDirection;
            caseSensitive[i] = true;
            for ( int j=0; j < s1.length; j++ ) {
                if ( s1[j].toUpperCase().equals("ASC") ) {
                    sortDirection[i] = Sorter.ASCENDING;
                    continue;
                }
                if ( s1[j].toUpperCase().equals("DESC") ) {
                    sortDirection[i] = Sorter.DESCENDING;
                    continue;
                }

                if ( s1[j].toUpperCase().equals("CASEINS") || s1[j].toUpperCase().equals("CI") ) {
                    caseSensitive[i] = false;
                    continue;
                }

            }

        }
    }
/*
    public SimpleInternalView getView() {
        return this.table;
    }

    public void setView( SimpleInternalView view ) {
        this.table = view;
        createColumns();
    }
 */
    protected SorterServices sortServices;

    public TableServices getTableServices() {
        return this.tableServices;
    }

    private void setTableServices( TableServices tableServices ) {
        this.tableServices = tableServices;
        this.sortServices = this.tableServices.getSorterServices();
        createColumns();
    }

    protected void createColumns() {
        int[] k = new int[this.columnNames.length];
        columns = new DataColumn[this.columnNames.length];
        for ( int i=0; i < this.columnNames.length; i++ ) {
            k[i] = this.sortServices.getColumnIndex(columnNames[i]);
            columns[i] = this.sortServices.getColumn( k[i] );
        }
        this.setKeys(k);
    }

    public int compareRows(DataRow row, DataRow aRow) {
        int r = 0;
        int[] k = this.getKeys();
        for ( int i=0; i < k.length; i++ ) {
            Object op1 = row.getValue(k[i]);
            Object op2 = aRow.getValue(k[i]);

            if ( this.caseSensitive[i] ) {
                if ( columns[i].getType().equals(String.class) )
                    //op1 = ((PDBStringColumn)columns[i]).toUpperCase(op1);
                    op1 = op1 != null ? op1.toString() : null;
                if ( columns[i].getType().equals(String.class) )
                    //op2 = ((PDBStringColumn)columns[i]).toUpperCase(op2);
                    op2 = op2 != null ? op2.toString() : null;
            }

            r = columns[i].compareObjects( op1, op2 );
//            if ( this.listSortDirection == Sorter.ASCENDING) {
                if ( r!= 0 ) {
                    if ( this.sortDirection[i] == Sorter.DESCENDING )
                        r = -r;
                    break;
                }
//            }
  /*          } else {
                if ( r!= 0 ) {
                    if ( this.sortDirection[i] == Sorter.ASCENDING )
                        r = -r;
                    break;
                }
                
            }
   */ 
        }

        return r;
    }
}//class

