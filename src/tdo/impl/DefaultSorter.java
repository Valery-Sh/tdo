/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tdo.impl;

import tdo.service.TableServices;
import tdo.util.Strings;

/**
 *
 * @author valery
 */
public class DefaultSorter implements Sorter {

    private InternalView view;
    private TableServices tableServices;
    private boolean sortDirection;
    private String columnList;
    private String[] columnNames;

    /**
     * Creates a new instance of IRelation
     */
    public DefaultSorter() {
        this(null);
    }
    /*    public DefaultSorter(InternalView view) {
    this.view = view;
    }
     */

    public DefaultSorter(TableServices tableServices) {
        this.tableServices = tableServices;
    }

    /*************************************************************************
     * Реализация интерфейса ISorter.
     ************************************************************************
     * @return
     */
    @Override
    public boolean getSortDirection() {
        return this.sortDirection;
    }

    /**
     *
     * @param direction
     */
    @Override
    public void setSortDirection(boolean direction) {
        this.sortDirection = direction;
    }

    /**
     *
     * @return
     */
    /*    public InternalView getView() {
    return this.view;
    }
    public void setView(InternalView view) {
    this.view = view;
    }
     */
    @Override
    public String getColumnList() {
        return this.columnList;
    }

    @Override
    public void setColumnList(String columnList) {
        this.columnList = columnList;
        if (columnList != null) {
            this.columnList = columnList.toUpperCase();
            this.columnNames = Strings.split(this.columnList);
        } else {
            this.columnNames = null;
        }
    }

    @Override
    public String[] getColumnNames() {
        return this.columnNames;
    }

    @Override
    public void sort() {
        DefaultRowSort rs = new DefaultRowSort();
        DefaultViewComparator comp = new DefaultViewComparator(tableServices,columnList,sortDirection);
        rs.sort(tableServices, comp);
/*        if (sortDirection == Sorter.DESCENDING) {
            //rs.sortDesc(tableServices, comp);
            rs.sort(tableServices, comp);

        } else {
            rs.sort(tableServices, comp);
        }
 */
    }

    @Override
    public TableServices getTableServices() {
        return this.tableServices;
    }
}//class

