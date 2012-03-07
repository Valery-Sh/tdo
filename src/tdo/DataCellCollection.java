/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package tdo;

/**
 *
 * @author valery
 */
public interface DataCellCollection {
    
    /**
     * !!! The metod cannot be applied to a calculated column. 
     * @param columnIndex
     * @return
     */
    Object getValue(int columnIndex);    
    Object getValue(String columnName);
    Object getObject();
    Object setValue(Object value, int columnIndex);
    Object setValue(Object value, String columnName);    
    /**
     *  опирует содержимое €чеек заданного р€да в €чейки текущего р€да.
     * —осто€ние, определ€емое свойством <code>state</code>, а
     * также пол€ и свойства текущего р€да не измен€ютс€.
     * @param source р€д-источник €чеек дл€ копировани€
     */
    void copyCells(DataCellCollection source);
    void copyCells(Object[] source);
    void columnAdded(int columnIndex);
    void columnRemoved(int columnIndex);
    void columnMoved( int columnIndex, int oldCellIndex, int newCellIndex);    
    
}
