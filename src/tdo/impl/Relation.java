/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo.impl;

import tdo.DataRowCollection;
import tdo.Table;

/**
 *
 * @author Valery
 */
public interface Relation {
    DataRowCollection getRows();

//    DataRowCollection getSource();
    Table getTable();
    void setTable(Table table);

    Table getParentTable();

    void setParentTable(Table parentTable);

    public String getKeyList();

    public void setKeyList(String keyList);

    public String getParentKeyList();

    public void setParentKeyList(String parentKeyList);

    String[] getKeyColumns();

    String[] getParentKeyColumns();

    Object[] getValues();

//    public void setValues(Object[] values);


}
