package tdo;

import tdo.event.DataColumnCollectionListener;

/**
 * <p>Title: Filis Application</p>
 * <p>Description: Freq Sensor's Support</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: IS</p>
 * @author VNS
 * @version 1.0
 */
public interface DataColumnCollection {

    public DataColumn add(int sqlType);

    /**
     * ��������� ������� � ��������� �������. <p>
     * ��������� ����� �������, sql ��� �������, ������������ ������ ����������.
     * ���� ���������� ������� � ����� �� ������, ��� �������� ������� ���������,
     * �� ������� �� ����������� � ������������ �������� <code>null</code>.
     * @param sqlType �����, ����������� ���� �� �������� ��������,
     *     ������������ � ������ {@link java.sql.Types}.
     * @param columndName ������ ������ - ��� ����.
     * @return ������ �� ����� ������� ��� <code>null</code>, ���� ����������
     *         ������� � ����� �� ������.
     */
    DataColumn add(int sqlType, String columndName);

    void add(DataColumn column);
    DataColumn add(Class type);

    DataColumn add(Class type, String columnName);

    DataColumn addCalculated(Class type, String columnName);

    DataColumn addLookup(Class type, String columnName);

    /**
     * 
     * @return
     */
    DataColumnCollection create();

    DataColumn createColumn(Class clazz, String columnName);

    DataColumn createColumn(int sqlType, String columnName);

    DataColumn insert(int position, Class type, String columnName);

    DataColumn insertCalculated(int position, Class type, String columnName);

    void move(int columnIndex, int newIndex);

    void remove(int columnIndex);

    int   remove(DataColumn column);

    int remove(String columnName);

    /**
     * ���� ������ ���� <code>DataColumn</code> � �������� ������.<p>
     * @param columnName ��� �������
     * @return ������ �������
     */
    int find(String columnName);

    int indexOf(DataColumn column);

    void clear();

    boolean isEmpty();

    void copyTo(DataColumnCollection columns);

    DataColumn get(int columnIndex);

    DataColumn get(String columnName);

    int getCount();

    int getCount(int columnKind);

    void addDataColumnCollectionListener(DataColumnCollectionListener listener);

    void removeDataColumnColectionListener(DataColumnCollectionListener listener);

    boolean isValid(); // for testing

    void set(int index, DataColumn column);
}