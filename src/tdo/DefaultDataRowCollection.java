/*
 * DataRowCollection.java
 * 
 */

package tdo;

import java.util.ArrayList;
import java.util.List;
import tdo.service.TableServices;

/**
 * Реализует интерфейс {@link tdo.DataRowCollection}.
 * Методы класса позволяют хранить, добавлять, вставлять, удалять и 
 * заменять элементы типа {@link tdo.DataRow}. Доступ к рядам коллекции может
 * осуществляться по позиции (индексу) ряда.<p>
 * Внутреннее хранилище данных представляет собой объект типа 
 * <code>java.util.ArrayList</code>. 
 * <p>Свойство <code>context</code> связывает коллекцию с конкретной
 * таблицей.
 */
public class DefaultDataRowCollection<T> implements DataRowCollection{

    /**
     * Хранилище данных.<p>
     */
    private List<DataRow> rows = null;

    private List<T> objectList;

    private TableServices context;
    /**
     * Создает новый экземпляр коллекции рядов заданного объема.
     * Вызывает конструктор со значением параметра равным 10.
     * @param capacity емкость коллекции, позволяющая добавлять
     *   новые ряды без перестройки внутреннего хранилища
     */
    public DefaultDataRowCollection(int capacity) {
        rows = new ArrayList<DataRow>(capacity);
    }
    /**
     * Создает новый экземпляр коллекции рядов.
     * Вызывает конструктор со значением параметра равным 10.
     */
    public DefaultDataRowCollection() {
        this(20);
    }
    /**
     * Создает и добавляет ряд с "пустыми" элементами в конец хранилища данных. 
     * Созданный ряд переводится в состояние {@link tdo.RowState#INSERTING}
     * вызовом метода {@link tdo.DataRow#attachNew() }.
     * @return добавленный ряд.
     * @see tdo.DataRow
     * @see tdo.DefaultDataRow
     */
    @Override
    public DataRow add() {
        DataRow row = context.getRowCollectionServices().createRow();
        rows.add(row);        
        row.attachNew();
        if ( objectList != null )
            objectList.add((T) row.getCellCollection().getObject());
        return row;
    }

    /**
     * Добавляет заданный ряд  в конец коллекции.
     * Применяет метод {@link tdo.DataRow#attach} к новому ряду для модификации,
     * если необходимо, состояния ряда.
     * @param row добавляемый ряд.
     * @return индекс добавленного ряда
     * @see tdo.DataRow
     * @see tdo.DefaultDataRow
     */
    @Override
    public int add(DataRow row) {
        rows.add(row);
        row.attach();
        if ( objectList != null )
            objectList.add((T) row.getCellCollection().getObject());
        return rows.size()-1;
    }
    /**
     * Удаляет все элементы из коллекции рядов.
     */
    @Override
    public void clear() {
        rows.clear();
        if ( objectList != null )
            objectList.clear();
        rows = new ArrayList<DataRow>(100);
    }
    /**
     * Каждый элемент заданной коллекции рядов добавляется в конец текущей
     * коллекции.
     * 
     * @param fromRows коллекция, ряды которой добавляются к текущей
     */
    @Override
    public void copyFrom(DataRowCollection fromRows) {
       // DefaultDataRowCollection drc = (DefaultDataRowCollection)rows;
        for (int i = 0; i < fromRows.getCount(); i++) {
            this.rows.add( fromRows.get(i));
        }
    }
    @Override
    public void copyFrom(DataRow[] fromRows) {
        for (int i = 0; i < fromRows.length; i++) {
            this.rows.add( fromRows[i]);
        }

    }

    /**
     * Создает и возвращает новую коллекцию рядов, тип которой совпадает с 
     * типом текущей коллекции.
     * @return новая коллекция типа <code>DefaultDataRowCollection</code>
     */
    @Override
    public DataRowCollection create() {
        return new DefaultDataRowCollection();
    }
    /**
     * Создает и возвращает новую коллекцию рядов, тип которой совпадает с 
     * типом текущей коллекции.
     * Новой соллекции назначается заданный контекст. Если значение параметра
     * <code>populate</code> равно <code>true</code>, то элементы текущей
     * коллекции добавляются к вновь созданной.
     * @param ctx контекст, для которого создается коллекция рядов
     * @param populate , если равен <code>true</code>, то элементы текущей
     * коллекции добавляются к вновь созданной. <code>false</code> новая
     * коллекция не изменяется.
     * 
     * @return новая коллекция рядов с заданным контекстом 
     * @see #create()
     * @see #create(TableServices)
     */
    @Override
    public DataRowCollection create(TableServices ctx, boolean populate) {
        DefaultDataRowCollection drc = (DefaultDataRowCollection)create();
        drc.setContext(ctx);
        if ( populate ) {
            drc.copyFrom(this);
        }
        return drc;
    }
    
    /**
     * Создает и возвращает новую коллекция рядов для заданного контекста
     * и такая, что разделяет одно и тоже внутреннее хранилище.
     * Метод полезен в случае, если не эффективно создавать коллекцию и
     * копировать в нее элементы другой коллекции.
     * @param ctx контекст таблицы, для которого создается коллекция
     * @return новая коллекция, внутреннее хранилище рядов которой совпадает
     *    с текущим хранилищем
     * @see tdo.impl.AbstractTable#createTableViewSource(java.lang.String, java.lang.String) 
     */
    @Override
    public DataRowCollection createShared(TableServices ctx) {
        DataRowCollection newds = create(ctx,false);
        ((DefaultDataRowCollection) newds).importData(this,objectList);
        return newds;
    }
    /**
     * Удаляет ряд в позиции заданного индекса.
     * 
     * @param rowIndex тип <code>int</code> - индекс удаляемого ряда.
     * @return удаленный ряд
     * <code><pre>rowIndex < 0 || rowIndex >= getCount()</pre></code> 
     */
    @Override
    public DataRow delete(int rowIndex) {
        
        DataRow r = rows.remove(rowIndex);
        if ( objectList != null )
            objectList.remove(r.getCellCollection().getObject());
        return r;

    }
    /**
     * Находит и удаляет заданный ряд из коллекции.
     * 
     * @param row удаляемый ряд.
     * @return индекс удаленного ряд. -1, если ряд отсутствует в коллекции
     */
    @Override
    public int delete(DataRow row) {
        int rowIndex = this.rows.indexOf(row);
        if (rowIndex >= 0) {
            delete(rowIndex);
        }
        return rowIndex;
    }

    /**
     * Возвращает ряд с заданным индексом.
     * @param rowIndex индекс искомого ряда
     * @return ряд с заданным индексом
     * @throws IndexOutOfBoundsException если 
     * <code><pre>rowIndex < 0 || rowIndex >= getCount()</pre></code>
     * @see #set
     */
    @Override
    public DataRow get(int rowIndex) {
        return rows.get(rowIndex);
    }

    /**
     * @return значение контекста таблицы
     */
    public TableServices getContext() {
        return context;
    }
    /**
     * Возвращает количество рядов в коллекции.
     * @return количество рядов в коллекции. Должно быть <pre> >= 0 </pre>
     */
    @Override
    public int getCount() {
        return this.rows.size();
    }
    /**
     * Назначает переменной <code>rows</code> значение переменной 
     * <code>rows</code> заданной коллекции.
     * @param sourceRows
     */
    private void importData(DataRowCollection sourceRows,List<T> sourceObjectList ) {
        this.rows = ((DefaultDataRowCollection) sourceRows).rows;
        this.objectList = sourceObjectList;
    }
    /**
     * 
     * Возвращает позицию заданного ряда в коллекции.
     * @param row ряд, для которого определяется индекс. Может быть <code>null</code>.
     * @return значение большее или равное 0, если ряд содержится в коллекции.
     *   -1, в противном случае. Также возвращается -1, если значение параметра 
     *    равно <code>null</code>
     */
    @Override
    public int indexOf(DataRow row) {
        return rows.indexOf(row);
    }    
   /**
     * Создает и вставляет ряд с "пустыми" элементами в заданную позицию. 
     * Созданный ряд переводится в состояние {@link tdo.RowState#INSERTING}
     * вызовом метода {@link tdo.DataRow#attachNew() }.
     * @param rowIndex позиция вставки нового ряда
     * @return вставленный ряд.
     * @see #insert(int,DataRow)
     * @see #add()
     * @see tdo.DataRow
     * @see tdo.DefaultDataRow
     * @throws IndexOutOfBoundsException если 
     * <code><pre>rowIndex < 0 || rowIndex > getCount()</pre></code>, т.е 
     *          допускаются 0 и getCount()
     * 
     * 
     */
    @Override
    public DataRow insert(int rowIndex) {
        DataRow row = context.getRowCollectionServices().createRow();
        rows.add(rowIndex, row);
        row.attachNew();
        if ( objectList != null )
            objectList.add((T) row.getCellCollection().getObject());
        return row;
    }
   /**
     * Вставляет заданный ряд в заданную позицию. 
     * @param rowIndex позиция вставки нового ряда
     * @param row вставляемый ряд
     * @see #insert(int)
     * @see #add(DataRow)
     * @see tdo.DataRow
     * @see tdo.DefaultDataRow
     * 
     * @throws IndexOutOfBoundsException если 
     *   <code><pre>rowIndex < 0 || rowIndex > getCount()</pre></code>, т.е 
     *          допускаются 0 и getCount()
     */
    @Override
    public void insert(int rowIndex,DataRow row) {
      rows.add(rowIndex, row);
      row.attach();
     if ( objectList != null )
         objectList.add((T) row.getCellCollection().getObject());
    }

    /**
     * Устанавливает, содержит ли коллекция хотя бы один ряд. 
     *
     * @return тип <code>boolean</code> <i>true</i> - коллекция не содержит рядов;
     *                             <i>false</i> - коллекция содержит хотя бы
     *                             один ряд.
     */
    @Override
    public boolean isEmpty() {
        return rows.isEmpty();        
    }

    /**
     * Заменяет ряд в заданной позиции заданным рядом.
     * 
     * @param row заменяющий ряд
     * @param rowIndex позиция заменяемого ряда
     * @throws IndexOutOfBoundsException если 
     * <code><pre>rowIndex < 0 || rowIndex >= getCount()</pre></code>
     */
    @Override
    public void set(int rowIndex,DataRow row) {
        rows.set(rowIndex, row);
    }

    
    /**
     * Устанавливает значение контекста таблицы.
     * @param context значение контекста
     */
    public void setContext(TableServices context) {
        this.context = context;
    }

    @Override
    public List getObjectList() {
        return this.objectList;
    }

    @Override
    public void setObjectList(List olist) {
        this.objectList = olist;
    }


} //class DefaultDataRowCollection
