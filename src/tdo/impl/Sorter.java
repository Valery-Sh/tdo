/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.impl;

import tdo.service.TableServices;

/**
 *
 * @author valery
 */
public interface Sorter {
    static final boolean ASCENDING = false;
    static final boolean DESCENDING = true;

    TableServices getTableServices();
    boolean getSortDirection();
    void setSortDirection(boolean direction);


/*    SimpleInternalView getView();
    void setView( SimpleInternalView view);
*/


    String getColumnList();
    void setColumnList( String keyList );

    String[] getColumnNames();

    //void sort(boolean direction);
    void sort();

}
