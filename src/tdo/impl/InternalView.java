/*
 * InternalViewImpl.java
 *
 * Created on 10 ������ 2006 �., 12:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package tdo.impl;

import tdo.DataRow;
import tdo.DataRowCollection;
import tdo.Table;

public interface InternalView {

    public DataRowCollection getCurrentRows();
    //void setTableRows(DataRowCollection rows );
    public DataRowCollection getTableRows();

//    int getKind(DataRowCollection rows);

    Table getTable();

    /**
     * @return ���������� ����� ��� �������� � ���� <code>DataView</code>
     *    ��������� ��������� ����� �� {@link #store}
     */
    public int getRowCount();

    /**
     * @param rowIndex ������ ������������� ����
     * @return ���������� ��� ������� �� ��������� ������� ����.<p>
     * ������������ ��� ���������� �� �������� ��������� �����
     * {@link #store}.
     */
    public DataRow getRow(int rowIndex);

    /**
     * ������������� �������� ��� � �������� ���������. <p>
     * ��� � ��������, ������������ ���������� <code>rowIndex</code> ������
     * ������������ �� ������ ������ ������. ����� �������, ����������
     * ������������ ������ ������������� ���� � ��������� ��������
     * ��������� {@link #store}
     * @param row ����� �������� ���� � �������� <code>rowIndex</code>
     * @param rowIndex ������ ����������� ����
     */
    public void setRow(DataRow row, int rowIndex);

    /**
     * ��������� ����� ��������� ����  � ��������� ��������
     * ��������� {@link #store} � ���������� ��� ������. <p>
     * @param row ���, ��� �������� ������������ ������
     * @return -1, ���� ��������� <code>store</code> �� ��������
     *      ��������� ����. �������� ������� ��� ������ ����, �
     *      ��������� ������.
     */
    public int find(DataRow row);

    /**
     * ��������� �������� ������ ���� � ����� ��������� ��������
     * ��������� {@link #store} ��� � ���������� ��� ������. <p>
     * ���� ������� ��������� � ���������, ����� �� �����
     * {@link BaseDataTable#isLoading} ���������� <code>true</code>, ��
     * ��� ����������� ����� � ���������, ��������� � ����������, ������ ��
     * ������� ������� � {@link #storeList}, ��������� ���� ���������������
     * � {@link RowState#UNCHANGED}.<p>
     *
     * ���� ����� {@link BaseDataTable#isLoading} ���������� <code>false</code>,
     * �� ��� �� ����������� � ���������, ��������� � ����������, �
     * ��������� ���� ��������������� � {@link RowState#INSERTING}. ���
     * ��������� ����������� ���, ��� ����������, ��������� �����, ���������
     * ��� �, �� ��� ���, ���� �� ����� ����� ����� postRow ��� ����
     * �� �� ������ ���� ����� ���������� �����, ����� �������� DataView.
     *
     * @param row ����������� ���
     * @return ������ ������������ ���� �� ��������� � ��������
     *         <code>store</code>
     */
    public int addRow(DataRow row);

    public DataRow addRow();

    /**
     * ��������� "������" ��� � �������� ������� ������� ��������� �����. <p>
     * ����������� ��� �������� ������ ������ <code>rowIndex</code>. ���� ��
     * ������� � ��������� �������� ��� ������� <code>rowIndex</code>
     * ����������, ������� ����� ������, �� 1 ������ �������. <p>
     *
     * ���� ������� ��������� � ���������, ����� �� �����
     * {@link BaseDataTable#isLoading} ���������� <code>true</code>, ��
     * ��� ����������� (�� �� �����������) ����� � ���������, ��������� �
     * ����������, ������ �� ������� ������� � {@link #storeList},
     * ��������� ���� ��������������� � {@link RowState#UNCHANGED}.<p>
     *
     * ���� ����� {@link BaseDataTable#isLoading} ���������� <code>false</code>,
     * �� ��� �� �����������  � ���������, ��������� � ����������, �
     * ��������� ���� ��������������� � {@link RowState#INSERTING}. ���
     * ��������� ����������� ���, ��� ����������, ��������� �����, ���������
     * ��� �, �� ��� ���, ���� �� ����� ����� ����� postRow ��� ����
     * �� �� ������ ���� ����� ���������� �����, ����� �������� DataView.
     * @param rowIndex ������� �������. ����� ��������� �������� �� 0 ��
     *   ���������� ����� �� ������� � ������� ��������� �����.
     */
    void insertRow(int rowIndex);

    void insertRow(int rowIndex, DataRow row);

    /**
     *
     * @param rowIndex
     * @return
     */
    DataRow deleteRow(int rowIndex);

    void cancelInserting(int rowIndex);

    ViewManager getViewManager();
}//interface InternalView