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
     * ƒобавл€ет колонку к коллекции колонок. <p>
     * —оздаетс€ нова€ колонка, sql тип которой, определ€етс€ первым параметром.
     * ≈сли существует колонка с таким же именем, как значение второго параметра,
     * то колонка Ќ≈ добавл€етс€ и возвращаетс€ значение <code>null</code>.
     * @param sqlType целое, принимающее одно из значений констант,
     *     определенных в классе {@link java.sql.Types}.
     * @param columndName строка знаков - им€ пол€.
     * @return ссылка на новую колонку или <code>null</code>, если обнаружена
     *         колонка с таким же именем.
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
     * »щет объект типа <code>DataColumn</code> с заданным именем.<p>
     * @param columnName им€ колонки
     * @return индекс колонки
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