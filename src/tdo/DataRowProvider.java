/*
 * DataRowProvider.java
 */
package tdo;

/**
 *
 * @author Valera
 */
public interface DataRowProvider {

    //Table getTable();

    DataRow createRow();
    DataRow createRow(Object bean);
    
    //DataRow createRow(Object[] values, int start, int end);

    DataRow createRowCopy(DataRow row);


}
