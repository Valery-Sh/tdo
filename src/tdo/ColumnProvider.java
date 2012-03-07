/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo;

/**
 *
 * @author Valery
 */
public interface ColumnProvider {
    DataColumn createColumn(Class clazz, String columnName);
    DataColumn createColumn(int sqlType, String columnName);    
}
