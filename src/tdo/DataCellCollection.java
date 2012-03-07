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
     * �������� ���������� ����� ��������� ���� � ������ �������� ����.
     * ���������, ������������ ��������� <code>state</code>, �
     * ����� ���� � �������� �������� ���� �� ����������.
     * @param source ���-�������� ����� ��� �����������
     */
    void copyCells(DataCellCollection source);
    void copyCells(Object[] source);
    void columnAdded(int columnIndex);
    void columnRemoved(int columnIndex);
    void columnMoved( int columnIndex, int oldCellIndex, int newCellIndex);    
    
}
