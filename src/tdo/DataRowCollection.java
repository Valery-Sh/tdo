package tdo;

import java.util.List;
import tdo.service.TableServices;

/**
 * ���������� ���������������� ��������-���������, ����������� ���������.
 * ������ ���������� ��������� �������, ���������, ���������, ������� � 
 * �������� �������� ���� {@link tdo.DataRow}. ������ � ����� ��������� �����
 * �������������� �� ������� (�������) ����.
 * <p>�����, ����������� ��������� ����� ������������ ����������� � ����������
 * ����� - �������� ���� <code>DataRow</code> ����� ������ ����
 * <code>java.util.List</code> ������������ ��������.
 */
public interface DataRowCollection<T> {

   /**
     * ������� � ��������� ��� � "�������" �������� � ����� ���������. 
     * ��������� ��� ���������� ��������� ������� ������ 
     * <code>get(getCount()-1)</code>.
     * @return ����������� ���.
     * @see tdo.DataRow
     */
    public DataRow add();
    /**
     * ��������� �������� ���  � ����� ���������. 
     * @param row ����������� ���.
     * @return ������ ������������ ����
     * @see tdo.DataRow
     * @see #add()
     */
    public int add(DataRow row);

    /**
     * ������� ��� �������� �� ��������� �����, ����� �������, ���
     * ����� {@link isEmpty} ���������� <code>true</code>.
     * �� �������� �����-���� ��������� ��� ������.
     */
    public void clear();
    /**
     * ������ ������� �������� ��������� ����� ����������� � ����� �������
     * ���������.
     * 
     * @param fromRows ���������, ���� ������� ����������� � �������
     */
    public void copyFrom(DataRowCollection fromRows);
    /**
     * ������ ������� �������� ��������� ����� ����������� � ����� �������
     * ���������.
     *
     * @param fromRows ���������, ���� ������� ����������� � �������
     */
    public void copyFrom(DataRow[] fromRows);

    /**
     * ������� � ���������� ����� ��������� �����, ��� ������� ��������� � 
     * ����� ������� ���������.
     * @return ����� ��������� ���� <code>DefaultDataRowCollection</code>
     */
    public DataRowCollection create();
    /**
     * ������� � ���������� ����� ��������� �����, ��� ������� ��������� � 
     * ����� ������� ���������.
     * ����� ��������� ����������� �������� ��������. ���� �������� ���������
     * <code>populate</code> ����� <code>true</code>, �� �������� �������
     * ��������� ����������� � ����� ���������.
     * @param context ��������, ��� �������� ��������� ��������� �����
     * @param populate , ���� ����� <code>true</code>, �� �������� �������
     * ��������� ����������� � ����� ���������. <code>false</code> �����
     * ��������� �� ����������.
     * 
     * @return ����� ��������� ����� � �������� ���������� 
     * 
     * @see #create()
     * @see tdo.service.TableServices
     */
    public DataRowCollection create(TableServices context, boolean populate);
    /**
     * ������� � ���������� ����� ��������� ����� ��� ��������� ���������
     * � �����, ��� ��������� ���� � ���� ���������� ���������.
     * ����� ������� � ������, ���� �� ���������� ��������� ��������� �
     * ���������� � ��� �������� ������ ���������.
     * @param context �������� �������, ��� �������� ��������� ���������
     * @return ����� ���������, ���������� ��������� ����� ������� ���������
     *    � ������� ����������
     */
    public DataRowCollection createShared(TableServices context);

    /**
     * ������� ��� � ������� ��������� �������.
     * @param rowIndex ��� <code>int</code> - ������ ���������� ����.
     * @return ��������� ���
     * <code><pre>rowIndex < 0 || rowIndex >= getCount()</pre></code> 
     */
    public DataRow delete(int rowIndex);
    /**
     * ������� � ������� �������� ��� �� ���������.
     * @param row ��������� ���.
     * @return ������ ���������� ���. -1, ���� ��� ����������� � ���������
     */
    public int delete(DataRow row);

    /**
     * ���������� ��� � �������� ��������.
     * @param rowIndex ������ �������� ����
     * @return ��� � �������� ��������
     * @throws IndexOutOfBoundsException ���� 
     * <code><pre>rowIndex < 0 || rowIndex >= getCount()</pre></code>
     * @see #set
     */
    public DataRow get(int rowIndex);

    /**
     * ���������� ���������� ����� � ���������.
     * @return ���������� ����� � ���������. ������ ���� <pre> >= 0 </pre>
     */
    public int getCount();

    /**
     * ���������� ������� ��������� ���� � ���������.
     * @param row ���, ��� �������� ������������ ������. ����� ���� <code>null</code>.
     * @return �������� ������� ��� ������ 0, ���� ��� ���������� � ���������.
     *   -1, � ��������� ������. ����� ������������ -1, ���� �������� ��������� 
     *    ����� <code>null</code>
     */
    public int indexOf(DataRow row);
   /**
     * ������� � ��������� ��� � "�������" ���������� � �������� �������. 
     * @param rowIndex ������� ������� ������ ����
     * @return ����������� ���.
     * @see #insert(int,DataRow)
     * @see #add()
     * @see tdo.DataRow
     * @throws IndexOutOfBoundsException ���� 
     * <code><pre>rowIndex < 0 || rowIndex > getCount()</pre></code>, �.� 
     *          ����������� 0 � getCount()
     * 
     * 
     */
    public DataRow insert(int rowIndex);
   /**
     * ��������� �������� ��� � �������� �������. 
     * @param rowIndex ������� ������� ������ ����
     * @param row ����������� ���
     * @see #insert(int)
     * @see #add(DataRow)
     * @see tdo.DataRow
     * @see tdo.DefaultDataRow
     * 
     * @throws IndexOutOfBoundsException ���� 
     *   <code><pre>rowIndex < 0 || rowIndex > getCount()</pre></code>, �.� 
     *          ����������� 0 � getCount()
     */
    public void insert(int rowIndex, DataRow row);
  /**
     * �������������, �������� �� ��������� ���� �� ���� ���. <p>
     *
     * @return ��� <code>boolean</code> <i>true</i> - ��������� �� �������� �����;
     *                             <i>false</i> - ��������� �������� ���� ��
     *                             ���� ���.
     */
    public boolean isEmpty();
    /**
     * ��������������� ��� �������� ��� � �������� ������� ���������.
     * 
     * @param row ���������� ���
     * @param rowIndex ������� ����������� ����
     * @throws IndexOutOfBoundsException ���� 
     * <code><pre>rowIndex < 0 || rowIndex >= getCount()</pre></code>
     */
    void set(int rowIndex, DataRow row);
    
    List<T> getObjectList();
    void setObjectList(List<T> olist);
    
}