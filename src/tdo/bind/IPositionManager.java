/*
 * IPositionManager.java
 *
 * Created on 29 ������� 2006 �., 10:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.bind;

import java.awt.Component;
import tdo.Table;

/**
 * ���������� ����������, ������������� � �������, ������������ ��������
 * ��������� ���� ������ {@link pdata.BaseDataTable} � ���������� � �������� ������������
 * ���� {@link java.awt.Component}. <p>
 * �����, ����������� ���������, ������ � ����� �������� ������ ���� pdata.BaseDataTable.
 * ����� ������������� ��� ��������� ��� ����������� ���� java.awt.Component.
 * ����� ���������� �������� ���� java.awt.Component ����� ���� ��������� � ���������,
 * ���������, ����� ������� <i>����������</i> � �������� pdata.BaseDataTable. <p>
 * 
 * Defines the requirements for an object suitable for use with pdata.BaseDataTable 
 * in conjunction with managing active row position.
 */
public interface IPositionManager {
    /**
     * @return ������ �� ������, ������� ������ � ������������ ����������.
     */
    public Table getTable();
    /**
     * ������������� ������ �� ������, ��������� � ������������, ��������� � ���������.
     * @param dataTable ������ �� ��������������� ������.
     */
    public void setTable( Table table);
    /**
     * ��������� ��������� � ���������, ����� ��������� ��������� � �������� ������.
     * @param comp ����������� ���������.
     */
    public void add(Component comp);
    /**
     * ��������� ��������� � ���������, ����� ��������� � ������������ ����������
     * ������� ���������� � �������� ������.
     * 
     * @param comp ����������� ���������.
     * @param columnName - ��� ������� ������� ���� BaseDataTable, ������������ ��� ����������.
     */
    public void add(Component comp, String columnName);
    /**
     * ������� ��������� �� ����������.<p>
     * �����, ����������� ���������, ����������� �������� ���������� �������, � ���������
     * �������� �� �������������� ��������� ����������. ��������, �������������� ���
     * ���������� ��������� ������� ������ ���� �������.
     * 
     * @param comp ��������� ���������.
     */
    public void remove(Component comp);
    
    /**
     * ���������� ������� ���������� �������� ������ ���� �������. <p>
     * ��������, ��� ��������� �������� �������� text ���������� JTextField
     * ���������� �������� ��������� � ��������������� ������ ������� BaseDataTable. 
     * ���, ������������� �� ��� ���������, ������ ��������������� ������ ������� �,
     * � ���� �������, �� ���������� ��������� � ���� ��������� ��������� ��������.
     * 
     * 
     * @param value - ��������������� ��������.
     * @param rowIndex ������ ���� ������� BaseDataTable.
     * @param columnIndex - ������ ������� ������� BaseDataTable.
     * @return true, ���� ������� ���������� �������� �������. false - � ��������� ������.
     */
    public boolean setValue( Object value,int rowIndex, int columnIndex);
    public boolean setValue( Object value,int rowIndex, String columnName);

    /**
     * ���������� ������� �������� ������� ��������� ���� �������. <p>
     * ��������, ��� ��������� ��������� (selected) ������ JTable
     * ���������� �������� �������� ��������� ���� ������� BaseDataTable. 
     * ���, ������������� �� ��� ���������, ������ ��������������� ������ ������� �,
     * � ���� �������, �� ���������� ��������� � ���� ��������� ��������� ��������.
     * 
     * 
     * @param rowIndex ������ ������ ���� ������� BaseDataTable.
     * @return true, ���� ������� ���������� �������� �������. false - � ��������� ������.
     */
    public boolean moveTo( int rowIndex );

    /**
     * ��������� �������� �������������� ��������� ������ �������, ��������, ������� 
     * ����������� �������. <p>
     * ������ ���������� ��������������� ����� ���������� ������ �������� �������� 
     * <code>dataTable</code>.
     * @param oldDataTable ������ �������� �������� <code>dataTable</code>.
     * @param newDataTable ����� ��������.
     *
     */
    public void dataTableChanging(Table oldTable, Table newTable);
    
    /**
     * ��������� �������� ������������� �������, ��������, �������� ���������
     * ����������� �������. <p>
     * ������ ���������� ����� ����� ������ �������� �������� 
     * <code>dataTable</code>.
     * @param newDataTable ����� ��������.
     *
     */
    public void dataTableChanged( Table newTable);
    
}
