/*
 * DefaultDataColumnCollection.java
 *
 */
package tdo;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static tdo.DataColumn.CALC_KIND;
import tdo.event.DataColumnCollectionEvent;
import tdo.event.DataColumnCollectionListener;

/**
 * Реализует интерфейс <code>DataColumnCollection</code>.
 * Обеспечивает хранилище данных для элементов  {@link tdo.DataColumn}
 * в виде списка типа <code>java.util.ArrayList</code>. Для более удобного и 
 * эффективного доступа к списку колонок параллельно поддерживается 
 * <code>java.util.Map</code> коллекция, позволяющая по имени колонки 
 * получить доступ к элементу <code>DataColumn</code>. <p>
 * Элементы коллекции могут содержать только одну колонку с заданным именем.<p>
 * Методы класса позволяют добавлять колонки в конец коллекции, вставлять 
 * колонки в заданные позиции, удалять колонки из коллекции и перемещать колонки
 * внутри коллекции. <p> 
 * Класс содержит набор методов, которые не добавляют функциональности, но 
 * являются удобными особенно при использовании в коде без IDE. Например, помимо
 * универсальных методов <code>add(...)</code> есть метод {@link #addInteger},
 * а также другие подобные методы для наиболее часто используемых типов. <p>
 * Хотя класс реализует методы <code>createColumn</code>, как специфицировано
 * интерфейсом, но, на самом деле, исполнение методов делегируется классам,
 * реализующих интерфейс {@link tdo.ColumnProvider}. Это позволяет, во-первых 
 * более эффективно организовать тестирование, а во-вторых делает класс
 * независимым от типов колонок, которыми класс может оперировать. Если для
 * создания экземпляра класса используется конструктор без параметров, то
 * по умолчанию будет использован класс {@link tdo.DefaultColumnProvider}, 
 * который обеспечивает создание колонок, соответствующих классам-оболочкам, 
 * таким как <code>java.lang.Integer</code>, и .т.д., классам 
 * <code>java.lang.String, java.util.Date,java.sql.Time, java.sql.Timestamp,
 * java.math.BigDecimal</code>. 
 * <code>DefaultColumnProvider</code> также обеспечивает mapping между типами,
 * определенными в классе <code>java.util.Types</code> и классами Java. <p>
 * Класс <code>DefaultDataColumnCollection</code> предоставляет также конструктор 
 * с параметром типа {@link tdo.ColumnProvider}, позволяющим создать другие 
 * типы колонок.<p>
 * Необходимо отметить, что класс не имеет каких-либо явных ссылок на типы 
 * <code>Table, DataRow, DataRowCollection</code> и другие. Однако, он позволяет
 * зарегистрировать обработчики события 
 * {@link tdo.event.DataColumnCollectionListener} и возбуждает это события
 * изменеиях в коллекции колонок, оповещая всех зарегистрированных слушателей 
 * типа {@link tdo.event.DataColumnCollectionListener}.<p>
 * 
 */
public class DefaultDataColumnCollection implements DataColumnCollection, Serializable {

    private List<DataColumnCollectionListener> dataColumnsListenerList = new ArrayList<DataColumnCollectionListener>();
    private PropertyChangeSupport changeSupport;
    /**
     * Внутреннее хранилище элементов типа {@link tdo.DataColumn}.
     */
    private List<DataColumn> columns;
    /**
     * Хранит пары <i>ключ/значение</i>, где в качестве <i>ключа</i> используется
     * имя колонки в верхнем реристре, а <i>значение</i> - ссылка на объект типа
     * {@link tdo.DataColumn}. Используется для быстрого доступа к колонкам.
     * Поодерживается классом при операциях вставки и удаления колонок в 
     * коллекцию.
     */
    private Map<String, DataColumn> columnMap;
    private PropertyChangeHandler propertyChangeHandler;
    /**
     * Текущий для экземпляра класса провайдер колонок.
     */
    private ColumnProvider columnProvider;

    /**
     * Создает новый экземпляр класса. 
     * Создает новый объект типа {@link tdo.DefaultColumnProvider} иазначает его
     * в качастве текущего провайдера колонок. 
     * @see #DefaultDataColumnCollection(ColumnProvider)
     * @see #DefaultDataColumnCollection(DataColumn[])
     * @see #DefaultDataColumnCollection(DataColumn[],ColumnProvider)
     */
    public DefaultDataColumnCollection() {
        this(new DefaultColumnProvider());
    }

    /**
     * Создает новый экземпляр класса. 
     * Создает новый объект типа {@link tdo.DefaultColumnProvider} и назначает его
     * в качастве текущего провайдера колонок. Добавляет колонки, заданные
     * параметром-массивом в коллекцию.
     * @param columnArr массив колонок, добавляемый при создании экземпляра
     *    класса к коллекции.
     * @see #DefaultDataColumnCollection(ColumnProvider)
     * @see #DefaultDataColumnCollection()
     * @see #DefaultDataColumnCollection(DataColumn[],ColumnProvider)
     */
    public DefaultDataColumnCollection(DataColumn[] columnArr) {
        this(columnArr, new DefaultColumnProvider());
    }

    /**
     * Создает новый экземпляр класса. 
     * Создает новый объект типа {@link tdo.DefaultColumnProvider} и назначает его
     * в качастве текущего провайдера колонок. Добавляет колонки, заданные
     * параметром-массивом в коллекцию.
     * @param cp внешний провайдер колонок, назначаемый создаваемому объекту.
     * @see #DefaultDataColumnCollection(DataColumn[])
     * @see #DefaultDataColumnCollection()
     * @see #DefaultDataColumnCollection(DataColumn[],ColumnProvider)
     */
    public DefaultDataColumnCollection(ColumnProvider cp) {
        this.columnProvider = cp;
        initColumns();
        this.propertyChangeHandler = new PropertyChangeHandler();
    }

    /**
     * Создает новый экземпляр класса. 
     * Создает новый объект типа {@link tdo.DefaultColumnProvider} и назначает его
     * в качастве текущего провайдера колонок. Добавляет колонки, заданные
     * параметром-массивом в коллекцию.
     * @param columnArr массив колонок, добавляемый при создании экземпляра
     *    класса к коллекции.
     * @param cp внешний провайдер колонок, назначаемый создаваемому объекту.
     * @see #DefaultDataColumnCollection(DataColumn[])
     * @see #DefaultDataColumnCollection()
     * @see #DefaultDataColumnCollection(ColumnProvider)
     */
    public DefaultDataColumnCollection(DataColumn[] columnArr, ColumnProvider cp) {
        this.columnProvider = cp;
        initColumns();
        this.propertyChangeHandler = new PropertyChangeHandler();
        for (int i = 0; i < columnArr.length; i++) {
            add(columnArr[i]);
        }
    }

    /**
     * Возвращает количество колонок, согласно способу вычисления, определяемому
     * параметром.
     * 
     * @param columnKind принимает одно из значений:
     *  <ul>
     *      <li><i>DataColumn.DATA_KIND</i> - колонка обычных данных;</li>
     *      <li><i>DataColumn.CALC_KIND - вычисляемая колонка;</i></li>
     *      <li><i>DataColumn.LOOKUP_KIND - в данной версии не используется;</i></li>
     *      <li><i>DataColumn.ALL_KIND - все колонки.</i></li>
     * </ul>
     * @return
     */
    @Override
    public int getCount(int columnKind) {

        if (columnKind == DataColumn.ALL_KIND) {
            return this.getCount();
        }
        int result = 0;

        for (DataColumn dc : this.columns) {
            if (dc.getKind() == columnKind) {
                result++;
            }
        }
        return result;
    }

    /**
     * Создает и возвращает новый экземпляр <code>DataColumn</code> заданного 
     * типа и с заданным именем.
     * Метод делегирует создание колонки одноименному методу текущего
     * провайдера колонок.
     * @param type тип данных, обслуживаемый колонкой
     * @param columnName имя новой колонки, которое может быть получено
     *   вызовом метода {@link tdo.DataColumn#getName}.
     * @return новый экземпляр колонки
     */
    @Override
    public DataColumn createColumn(Class type, String columnName) {
        return this.columnProvider.createColumn(type, columnName);
    }

    /**
     * Создает и возвращает новый экземпляр <code>DataColumn</code> заданного 
     * sql-типа и с заданным именем.
     * Метод делегирует создание колонки одноименному методу текущего
     * провайдера колонок.
     * @param sqlType тип данных, обслуживаемый колонкой, который принимает
     *    одно из значений, определенных классом <code>java.sql.Types</code>.
     * @param columnName имя новой колонки, которое может быть получено
     *   вызовом метода {@link tdo.DataColumn#getName}.
     * @return новый экземпляр колонки
     */
    @Override
    public DataColumn createColumn(int sqlType, String columnName) {
        return this.columnProvider.createColumn(sqlType, columnName);
    }

    /**
     * Вызывается методами добавлением(вставкой) колонкок в коллекцию.
     * Выполняет следующие функции:
     * <ul>
     *   <li>Создает уникальное для коллекции имя колонки 
     *      <i>(в верхнем регистре)</i>, если заданное имя равно 
     *       <code>null</code>
     *   </li>
     *   <li>Проверяет заданное имя на уникальность в коллекции и, если 
     *       обнаружен дубликат, то возбуждается исключение типа 
     *      <code>IllegalArgumentException</code>.
     *   </li>
     *   <li>Создает новый экземпляр колонки вызовом метода 
     *      {@link createColumn(int,String)} и возвращает его в точку вызова.
     *   </li>
     * </ul>
     * @param sqlType sql-тип принимающий одно из значений, определенных
     *      java.sql.Types
     * @param columnName имя колонки или <code>null</code>
     * @return созданную методом колонку
     */
    protected DataColumn prepareAddColumn(int sqlType, String columnName) {

        DataColumn column = null;
        String pcolumnName = columnName;
        if (columnName == null) {
            pcolumnName = this.produceColumnName().toUpperCase();
        }
        if (find(pcolumnName.toUpperCase()) >= 0) //pcolumnName = this.produceColumnName().toUpperCase();            
        {
            throw new IllegalArgumentException("Dublicate columnName '" + columnName.toUpperCase() + "'");
        }
        column = createColumn(sqlType, pcolumnName.toUpperCase());
        return column;
    }

    /**
     * Вызывается методами добавлением(вставкой) колонкок в коллекцию.
     * Выполняет следующие функции:
     * <ul>
     *   <li>Создает уникальное для коллекции имя колонки 
     *      <i>(в верхнем регистре)</i>, если заданное имя равно 
     *       <code>null</code>
     *   </li>
     *   <li>Проверяет заданное имя на уникальность в коллекции и, если 
     *       обнаружен дубликат, то возбуждается исключение типа 
     *      <code>IllegalArgumentException</code>.
     *   </li>
     *   <li>Создает новый экземпляр колонки вызовом метода 
     *      {@link createColumn(Class,String)} и возвращает его в точку вызова.
     *   </li>
     * </ul>
     * @param type класс объектов, соответствующий колонке
     * @param columnName имя колонки или <code>null</code>
     * @return созданную методом колонку
     * @throws IllegalArgumentException если заданное имя колонки уже имеет
     *     одна из колонок коллекции
     */
    protected DataColumn prepareAddColumn(Class type, String columnName) {

        DataColumn column = null;
        String pcolumnName = columnName;
        if (columnName == null) {
            pcolumnName = this.produceColumnName().toUpperCase();
        }
        if (find(pcolumnName.toUpperCase()) >= 0) {
            throw new IllegalArgumentException("Dublicate columnName '" + columnName.toUpperCase() + "'");
        }

        column = createColumn(type, pcolumnName.toUpperCase());
        return column;
    }

    /**
     * Создает колонку заданного sql-типа и добавляеет ее 
     * в конец коллекции колонок. <p>
     * Делегирует исполнение методу {@link #add(int,String)}, передавая 
     * <code>null</code> значением второго параметра
     * @param sqlType целое, принимающее одно из значений констант,
     *     определенных в классе <code>java.sql.Types</code>.
     * @return новый экземпляр колонки
     * @see #add(int,String)
     */
    @Override
    public DataColumn add(int sqlType) {
        //String columnName = this.produceColumnName().toUpperCase();
        return add(sqlType, null);
    }

    /**
     * Создает колонку заданного sql-типа и с заданным именем  и добавляеет ее 
     * в конец коллекции колонок. 
     * 
     * Если существует колонка с таким же именем, как значение второго параметра,
     * то выбрасывается исключение IllegalArgumentException.
     * 
     * @param sqlType целое, принимающее одно из значений констант,
     *     определенных в классе <code>java.sql.Types</code>.
     * @param columnName имя колонки или <code>null</code>
     *
     * @return новый экземпляр колонки
     * 
     * @throws IllegalArgumentException если заданное имя колонки уже имеет
     *     одна из колонок коллекции
     */
    @Override
    public DataColumn add(int sqlType, String columnName) {
        DataColumn column = prepareAddColumn(sqlType, columnName);
        appendColumn(column);
        return column;
    }

    /**
     * Создает колонку с заданными sql-типом, именем, размерностью и 
     * <i>десятичным порядком</i>  и добавляеет ее в конец коллекции колонок. 
     * 
     * Если существует колонка с таким же именем, как значение второго параметра,
     * то выбрасывается исключение IllegalArgumentException.
     * 
     * @param sqlType целое, принимающее одно из значений констант,
     *     определенных в классе <code>java.sql.Types</code>.
     * @param columnName имя колонки или <code>null</code>
     *
     * @param precision 
     * @return новый экземпляр колонки
     * 
     * @throws IllegalArgumentException если заданное имя колонки уже имеет
     *     одна из колонок коллекции
     */
    //@Override
/*    public DataColumn add(int sqlType, String columnName, int precision,
            int scale) {
        DataColumn column = prepareAddColumn(sqlType, columnName);
        column.setScale(scale);
        column.setSize(precision);
        appendColumn(column);
        return column;
    }
*/
    /**
     * Добавляет существующий экземпляр колонки в конец коллекции колонок. 
     * 
     * Если существует колонка с таким же именем, как значение второго параметра,
     * то выбрасывается исключение IllegalArgumentException.
     * 
     * @param column добавляемая колонка
     * 
     * @throws IllegalArgumentException если заданное имя колонки уже имеет
     *     одна из колонок коллекции
     */
    @Override
    public void add(DataColumn column) {

        if (column.getName() == null) {
            column.setName(this.produceColumnName().toUpperCase());
        } else if (find(column.getName()) >= 0) {
            throw new IllegalArgumentException("Dublicate columnName '" + column.getName().toUpperCase() + "'");
        }


        appendColumn(column);
    }

    /**
     * Создает колонку заданного типа и добавляеет ее в конец коллекции колонок. <p>
     * Делегирует исполнение методу {@link #add(Class,String)}, передавая 
     * <code>null</code> значением второго параметра
     * @param type java тип данных, обслуживаемых новой колонкой
     * @return новый экземпляр колонки
     * @see #add(int)
     * @see #add(Class,String)
     */
    @Override
    public DataColumn add(Class type) {
        return add(type, null);
    }

    /**
     * Создает колонку заданного типа и с заданным именем и 
     * добавляеет ее в конец коллекции колонок. 
     * 
     * Если существует колонка с таким же именем, как значение второго параметра,
     * то выбрасывается исключение IllegalArgumentException.
     * 
     * @param type java-тип данных, обслуживаемых колонкой
     * @param columnName имя колонки или <code>null</code>
     *
     * @return новый экземпляр колонки
     * 
     * @throws IllegalArgumentException если заданное имя колонки уже имеет
     *     одна из колонок коллекции
     */
    @Override
    public DataColumn add(Class type, String columnName) {
        DataColumn column = prepareAddColumn(type, columnName);
        appendColumn(column);
        return column;

    }

    /**
     * Создает вычисляемую колонку заданного типа и с заданным именем и 
     * добавляеет ее в конец коллекции колонок. 
     * 
     * Колонка является вычисляемой, если ее метод 
     * {@link tdo.DataColumn#getKind()} возвращает значение 
     * <code>DataColumn.CALC_KIND}</code>. 
     * 
     * Если существует колонка с таким же именем, как значение второго параметра,
     * то выбрасывается исключение IllegalArgumentException.
     * 
     * @param type java-тип данных, обслуживаемых колонкой
     * @param columnName имя колонки или <code>null</code>
     *
     * @return новый экземпляр колонки
     * 
     * @throws IllegalArgumentException если заданное имя колонки уже имеет
     *     одна из колонок коллекции
     */
    @Override
    public DataColumn addCalculated(Class type, String columnName) {
        //int sqlType = getSqlType(type);
        DataColumn column = prepareAddColumn(type, columnName);
        column.setKind(DataColumn.CALC_KIND);
        appendColumn(column);
        return column;

    }

    /**
     * Создает вычисляемую колонку заданного sql-типа и с заданным именем 
     * и добавляеет ее в конец коллекции колонок. 
     * 
     * Колонка является вычисляемой, если ее метод 
     * {@link tdo.DataColumn#getKind()} возвращает значение 
     * <code>DataColumn.CALC_KIND}</code>. 
     * 
     * Если существует колонка с таким же именем, как значение второго параметра,
     * то выбрасывается исключение IllegalArgumentException.
     * 
     * @param sqlType sql-тип данных, обслуживаемых колонкой
     * @param columnName имя колонки или <code>null</code>
     *
     * @return новый экземпляр колонки
     * 
     * @throws IllegalArgumentException если заданное имя колонки уже имеет
     *     одна из колонок коллекции
     */
/*    @Override
    public DataColumn addCalculated(int sqlType, String columnName) {
        DataColumn column = prepareAddColumn(sqlType, columnName);
        column.setKind(DataColumn.CALC_KIND);
        appendColumn(column);
        return column;

    }
*/
    /**
     * Создает <i>вычисляемую</i> колонку с заданными sql-типом, 
     * именем, размерностью и  <i>scale</i>  и добавляеет ее в конец коллекции
     * колонок. 
     * Колонка является вычисляемой, если ее метод 
     * {@link tdo.DataColumn#getKind()} возвращает значение 
     * <code>DataColumn.CALC_KIND}</code>. 
     * 
     * Если существует колонка с таким же именем, как значение второго параметра,
     * то выбрасывается исключение IllegalArgumentException.
     * 
     * @param sqlType целое, принимающее одно из значений констант,
     *     определенных в классе <code>java.sql.Types</code>.
     * @param columnName имя колонки или <code>null</code>
     *
     * @param precision размерность данных
     * @param scale 
     * @return новый экземпляр колонки
     * 
     * @throws IllegalArgumentException если заданное имя колонки уже имеет
     *     одна из колонок коллекции
     */
    //@Override
/*    public DataColumn addCalculated(int sqlType, String columnName, int precision, int scale) {
        DataColumn column = prepareAddColumn(sqlType, columnName);
        column.setKind(DataColumn.CALC_KIND);
        column.setPrecision(precision);
        column.setScale(scale);
        appendColumn(column);
        return column;
    }
*/
    /**
     * 
     * @param sqlType
     * @param columnName
     * @return
     */
    //@Override
/*    public DataColumn addLookup(int sqlType, String columnName) {
        DataColumn column = prepareAddColumn(sqlType, columnName);
        column.setKind(DataColumn.LOOKUP_KIND);
        appendColumn(column);
        return column;
    }
*/
    //@Override
/*    public DataColumn addLookup(int sqlType, String columnName, int precision, int scale) {
        DataColumn column = prepareAddColumn(sqlType, columnName);
        if (column == null) {
            return null;
        }
        column.setKind(DataColumn.LOOKUP_KIND);
        column.setPrecision(precision);
        column.setScale(scale);
        appendColumn(column);
        return column;

    }
*/
    @Override
    public DataColumn addLookup(Class type, String columnName) {
        //int sqlType = getSqlType(type);
        DataColumn column = prepareAddColumn(type, columnName);
        column.setKind(DataColumn.LOOKUP_KIND);
        appendColumn(column);
        return column;
    }

    /**
     * Создает колонку типа  <code>java.lang.Integer</code> c
     * заданным именем  и добавляеет ее в конец коллекции колонок. 
     * 
     * Метод не добавляет функциональности и предназначен для удобства в
     * использовании. <p>
     * 
     * Если существует колонка с таким же именем, параметра,
     * то выбрасывается исключение IllegalArgumentException.
     * 
     * @param columnName имя колонки или <code>null</code>
     *
     * @return новый экземпляр колонки
     * 
     * @throws IllegalArgumentException если заданное имя колонки уже имеет
     *     одна из колонок коллекции
     */
    //@Override
/*    public DataColumn addInteger(String columnName) {
        DataColumn column = prepareAddColumn(java.sql.Types.INTEGER, columnName);
        appendColumn(column);
        return column;
    }
*/
    /**
     * Создает колонку типа  <code>java.math.BigDecimal</code> c
     * заданным именем  и добавляеет ее в конец коллекции колонок. 
     * 
     * Метод не добавляет функциональности и предназначен для удобства в
     * использовании. <p>
     * 
     * Если существует колонка с таким же именем, параметра,
     * то выбрасывается исключение IllegalArgumentException.
     * 
     * @param columnName имя колонки или <code>null</code>
     *
     * @return новый экземпляр колонки
     * 
     * @throws IllegalArgumentException если заданное имя колонки уже имеет
     *     одна из колонок коллекции
     */
    //@Override
/*    public DataColumn addDecimal(String columnName) {
        DataColumn column = prepareAddColumn(java.sql.Types.DECIMAL, columnName);
        appendColumn(column);
        return column;
    }
*/
    /**
     * Создает колонку типа  <code>java.math.BigDecimal</code> 
     * заданным именем, размерностью и десятичным порядком и добавляеет ее в 
     * конец коллекции колонок. 
     * 
     * Метод не добавляет функциональности и предназначен для удобства в
     * использовании. <p>
     * 
     * Если существует колонка с таким же именем, параметра,
     * то выбрасывается исключение IllegalArgumentException.
     * 
     * @param columnName имя колонки или <code>null</code>
     *
     * @param precision размерность десятичных данных
     * @param scale десятичный порядок
     * @return новый экземпляр колонки
     * 
     * @throws IllegalArgumentException если заданное имя колонки уже имеет
     *     одна из колонок коллекции
     */
    //@Override
/*    public DataColumn addDecimal(String columnName, int precision, int scale) {
        DataColumn column = prepareAddColumn(java.sql.Types.DECIMAL, columnName);
        column.setPrecision(precision);
        column.setScale(scale);
        appendColumn(column);
        return column;
    }
*/
    /**
     * Создает колонку типа  <code>java.lang.Double</code> 
     * c заданным именем  и добавляеет ее в конец коллекции колонок. 
     * 
     * Метод не добавляет функциональности и предназначен для удобства в
     * использовании. <p>
     * 
     * Если существует колонка с таким же именем, параметра,
     * то выбрасывается исключение IllegalArgumentException.
     * 
     * @param columnName имя колонки или <code>null</code>
     *
     * @return новый экземпляр колонки
     * 
     * @throws IllegalArgumentException если заданное имя колонки уже имеет
     *     одна из колонок коллекции
     */
    //@Override
/*    public DataColumn addDouble(String columnName) {
        DataColumn column = prepareAddColumn(java.sql.Types.DOUBLE, columnName);
        appendColumn(column);
        return column;
    }
*/
    /**
     * Создает колонку типа  <code>java.lang.String</code>  c
     * заданным именем и добавляеет ее в конец коллекцию колонок. 
     * 
     * Метод не добавляет функциональности и предназначен для удобства в
     * использовании. <p>
     * 
     * Если существует колонка с таким же именем, параметра,
     * то выбрасывается исключение IllegalArgumentException.
     * 
     * @param columnName имя колонки или <code>null</code>
     *
     * @return новый экземпляр колонки
     * 
     * @throws IllegalArgumentException если заданное имя колонки уже имеет
     *     одна из колонок коллекции
     */
    //@Override
/*    public DataColumn addString(String columnName) {
        DataColumn column = prepareAddColumn(java.sql.Types.VARCHAR, columnName);
        appendColumn(column);
        return column;
    }
*/
    /**
     * Создает вычисляемую колонку заданного типа и заданным именем  и
     * вставляет ее в заданную позицию коллекции колонок. 
     * 
     * Колонка является вычисляемой, если ее метод 
     * {@link tdo.DataColumn#getKind()} возвращает значение 
     * <code>DataColumn.CALC_KIND}</code>. 
     * 
     * Если существует колонка с таким же именем, как значение второго параметра,
     * то выбрасывается исключение IllegalArgumentException.
     * 
     * @param position позиция вставки.
     * @param type java-тип данных, обслуживаемых колонкой
     * @param columnName имя колонки или <code>null</code>
     *
     * @return новый экземпляр колонки
     * 
     * @throws IllegalArgumentException если заданное имя колонки уже имеет
     *     одна из колонок коллекции
     * @throws IndexOutOfBoundsException для позиции вставки <i>position</i>
     * выполнено одно из условий:
     * <ul>
     *    <li><i>position</i> строго меньше 0</li>
     *    <li><i>position</i> строго больше текущего размера коллекции</li>
     * </ul>
     */
    @Override
    public DataColumn insertCalculated(int position, Class type, String columnName) {

        DataColumn column = prepareAddColumn(type, columnName);
        column.setKind(DataColumn.CALC_KIND);

        if (position < 0 || position > getCount()) {
            throw new IndexOutOfBoundsException("Insert position out of bounds ( insert(" + position + ") )");
        }
        if (column.getName() == null) {
            column.setName(this.produceColumnName().toUpperCase());
        } else if (find(column.getName()) >= 0) {
            throw new IllegalArgumentException("Dublicate columnName '" + column.getName().toUpperCase() + "'");
        }
        columns.add(position, column);
        columnMap.put(column.getName().toUpperCase(), column);
        //  updateIndexes();
        column.addPropertyChangeListener(propertyChangeHandler);

        fireDataColumns(new DataColumnCollectionEvent(this, column, DataColumnCollectionEvent.COLUMN_ADDED));

        return column;
    }

    /**
     * Создает  колонку заданного типа и заданным именем  и
     * вставляет ее в заданную позицию коллекции колонок. 
     * 
     * Если существует колонка с таким же именем, как значение второго параметра,
     * то выбрасывается исключение IllegalArgumentException.
     * 
     * @param position позиция вставки.
     * @param type java-тип данных, обслуживаемых колонкой
     * @param columnName имя колонки или <code>null</code>
     *
     * @return новый экземпляр колонки
     * 
     * @throws IllegalArgumentException если заданное имя колонки уже имеет
     *     одна из колонок коллекции
     * @throws IndexOutOfBoundsException для позиции вставки <i>position</i>
     * выполнено одно из условий:
     * <ul>
     *    <li><i>position</i> строго меньше 0</li>
     *    <li><i>position</i> строго больше текущего размера коллекции</li>
     * </ul>
     */
    @Override
    public DataColumn insert(int position, Class type, String columnName) {
        DataColumn column = prepareAddColumn(type, columnName);
        insert(position, column);
        return column;
    }
   /**
     * Существующую колонку вставляет в заданную позицию коллекции колонок. 
     * 
     * Если существует колонка с таким же именем, как у колонки, заданной 
     * параметром, то выбрасывается исключение 
     * <code>IllegalArgumentException</code>.
     * 
     * @param position позиция вставки.
     * @param column вставляемая колонка
     * @throws IllegalArgumentException если заданное имя колонки уже имеет
     *     одна из колонок коллекции
     * @throws IndexOutOfBoundsException для позиции вставки <i>position</i>
     * выполнено одно из условий:
     * <ul>
     *    <li><i>position</i> строго меньше 0</li>
     *    <li><i>position</i> строго больше текущего размера коллекции</li>
     * </ul>
     */
    public void insert(int position, DataColumn column) {
        if (position < 0 || position > getCount()) {
            throw new IndexOutOfBoundsException("Insert position out of bounds ( insert(" + position + ") )");
        }
        if (column == null) {
            throw new NullPointerException("'column' argument cannot be null. ( insert(" + position + ") )");
        }
        if (column.getName() == null) {
            column.setName(this.produceColumnName().toUpperCase());
        } else if (find(column.getName()) >= 0) {
            throw new IllegalArgumentException("Dublicate columnName '" + column.getName().toUpperCase() + "'");
        }

        columns.add(position, column);
        columnMap.put(column.getName().toUpperCase(), column);
        //    updateIndexes();
        column.addPropertyChangeListener(propertyChangeHandler);

        fireDataColumns(new DataColumnCollectionEvent(this, column, DataColumnCollectionEvent.COLUMN_ADDED));

    }

    public void renameColumn(DataColumn column, String newName) {
        if (column == null || newName == null) {
            throw new NullPointerException("'newName' argument cannot be null. ( renameColumn())");
        }
        String nm = column.getName().toUpperCase();
        column.setName(newName);
        columnMap.remove(nm);
        columnMap.put(column.getName().toUpperCase(), column);
    }

    protected void appendColumn(DataColumn column) {
        //    int oldIndex = column.getIndex();
        int oldIndex = indexOf(column);
        columns.add(column);

        columnMap.put(column.getName().toUpperCase(), column);
        //      updateIndexes();
        column.addPropertyChangeListener(propertyChangeHandler);

        fireDataColumns(new DataColumnCollectionEvent(this, column, DataColumnCollectionEvent.COLUMN_ADDED));
    }
    /**
     * Перемещает колонку с ззаданным индексом внутри коллекции в позицию перед
     * заданной колонкой
     *
     * Следует интерпретировать так: Переставить колонку с индексом 
     * <code>columnIndex</code> на позицию <i>ПЕРЕД</I> колонкой с индексом 
     * <code>newIndex</code>.  Из этого следует, если
     * <i>columnIndex == newIndex</i>,  то перестановка не имеет смысла и не 
     * проводится. Если newIndex больше columnIndex на 1, то также перестановка 
     * не имеет смысла. Особый случай, когда 
     * <i>newIndex == числу колонок коллекции</i>. В таком случае
     * колонка переставляется <i>ЗА</i> последним элементом.
     * @param columnIndex исходная позиция колонки.
     * @param newIndex newIndex новая позиция колонки. Должен быть больше или 
     *                  равен 0 и меньше или равен размеру коллекции колонок.
     * @see #set
     */
    @Override
    public void move(int columnIndex, int newIndex) {

        if (newIndex < 0 || newIndex > this.getCount()) {
            return;
        }
        int newIndexSafe = newIndex;

        if (newIndex - columnIndex == 1) {
            return;
        } else if (columnIndex < newIndex) {
            newIndex--;
        }

        HashMap<String, String> emap = new HashMap<String, String>();
        String s;//My 06.03.2012 = null;
        for (DataColumn dc : this.columns) {
            if (dc.getKind() == CALC_KIND && dc.getExpression() != null) {
                emap.put(dc.getName().toUpperCase(), dc.getExpression());
                dc.setExpression(null);
            }
        }

        DataColumn column = columns.remove(columnIndex);
        columns.add(newIndex, column);
        // We must notify Table
        fireDataColumns(new DataColumnCollectionEvent(this, column, newIndex, DataColumnCollectionEvent.COLUMN_MOVED));
        
        for (DataColumn dc : this.columns) {
            if (dc.getKind() == CALC_KIND) {
                s = emap.get(dc.getName().toUpperCase());
                dc.setExpression(s);
            }
        }
        //My 06.03.2012emap = null;
    }

    /**
     * Удаляет колонку с заданным индексов из коллекции.
     * <b>Примечание:</b> Не следует выполнять этот метод в цикле 
     * <code>foreach</code>, поскольку в процессе удаления могут, и, как правило
     * будут проводиться изменеия в дрегих колонках колекции, в частности
     * мдифицируется свойсвтво <code>DataColumn.cellIndex</code>.
     * @param columnIndex индекс удаляемой колонки
     * @throws IndexOutOfBoundsException если индеккс колонки отрицательный или
     *    больше или равен числу колонок
     */
    @Override
    public void remove(int columnIndex) {
        if (columnIndex < 0 || columnIndex >= this.getCount()) {
            throw new IndexOutOfBoundsException(" remove(" + columnIndex +
                    ") out of bounds");
        }
        DataColumn column = this.get(columnIndex);

        column.removePropertyChangeListener(propertyChangeHandler);

        this.columns.remove(columnIndex);
        this.columnMap.remove(column.getName());

        int kind = column.getKind();
//        this.updateIndexes();
        fireDataColumns(new DataColumnCollectionEvent(this, column, columnIndex, DataColumnCollectionEvent.COLUMN_REMOVED));
    }
    /**
     * Удаляет заданную колонку  из коллекции.
     * <b>Примечание:</b> Не следует выполнять этот метод в цикле 
     * <code>foreach</code>, поскольку в процессе удаления могут, и, как правило
     * будут проводиться изменеия в дрегих колонках колекции, в частности
     * мдифицируется свойсвтво <code>DataColumn.cellIndex</code>.<p>
     * 
     * @param column удаляемая колонка
     * @throws NullPointerException если <code>column</code> равен <code>null</code>
     * @throws IndexOutOfBoundsException если коолекция не содержит указанной 
     * колонки. Т.к. метод, в конкчном счете, делегирует исполнение методу 
     * {@link #remove(int)}
     * @see #remove(java.lang.String) 
     * @see #remove(int) 
     */
    @Override
    public int remove(DataColumn column) {
        if (column == null )
            throw new NullPointerException("'column' argument cannot be null");
        return remove(column.getName());
    }

    /**
     * Удаляет колонку с заданным именем из коллекции.
     * <b>Примечание:</b> Не следует выполнять этот метод в цикле 
     * <code>foreach</code>, поскольку в процессе удаления могут, и, как правило
     * будут проводиться изменеия в дрегих колонках колекции, в частности
     * мдифицируется свойсвтво <code>DataColumn.cellIndex</code>.
     * @param columnName имя удаляемой колонки
     * @return индекс удаленной колонки
     * @throws IndexOutOfBoundsException если коолекция не содержит указанной 
     * колонки. Т.к. метод, в конкчном счете, делегирует исполнение методу 
     * {@link #remove(int)}
     * @see #remove(tdo.DataColumn) 
     * @see #remove(int) 
     */
    @Override
    public int remove(String columnName) {
        int columnIndex = this.find(columnName);
        remove(columnIndex);
        return columnIndex;
    }

    /**
     * Выполняет поиск в коллекции объекта типа <code>DataColumn</code> с 
     * заданным именем.<p>
     * Поиск осуществляется по имени колонки. Регист игнорируется, т.е.
     * не различаются литеры в "верхнем/нижнем" регистре.<p>
     *
     * @param columnName строковое значения имени колонки, как определено в
     *   классе <code>DataColumn</code> свойством <code>name</code>.
     * @return тип <code>int</code> - если колонка с заданным именем
     *         найдена, то возвращается ее индекс в коллекции
     *         Если поиск не увенчался успехом, то возвращается значение -1
     *         (минус 1).
     */
    @Override
    public int find(String columnName) {
        int result = 0;
        boolean found = false;
        DataColumn dc;//My 06.03.2012 = null;

        for (int i = 0; i < columns.size(); i++) {
            dc = columns.get(i);
            if (dc.getName().equalsIgnoreCase(columnName)) {
                return i;
            }
            ++result;
        }//for

        return -1;
    }

    /**
     * Возвращает индекс заданной колонки в коллекции.
     * @param column колонка, индекс которой определяется
     * @return индекс найденной колонки или -1, если поиск не удачен
     */
    @Override
    public int indexOf(DataColumn column) {
        return this.columns.indexOf(column);
    }

    /**
     * Строит уникальное имя среди имен колонок, содержащихся в коллекции.
     * Просматриваются поля, имена которых имеют преффикс
     * <code>COL_</code>. Предполагается, что за префиксом следует
     * подстрока, состоящая из десятичных цифр - постфикс. Определяется число,
     * имеющее максимальное значение по всем постфиксам. к нему добавляется 1 и
     * полученный результат используется для создания нового имени.
     *
     * @return строка знаков - построенное имя поля.
     */
    protected String produceColumnName() {
        String template = "COL_";
        String s;
        int p = -1;
        DataColumn dc;//My 06.03.2012 = null;
        for (int i = 0; i < columns.size(); i++) {
            dc = columns.get(i);
            s = dc.getName().toUpperCase();
            if (s.startsWith(template)) {
                p = Math.max(Integer.parseInt(s.substring(template.length())), p);
            }
        }//for

        return template + String.valueOf(++p);
    }

    /**
     * Удаляет все элементы коллекции. <p>
     */
    @Override
    public void clear() {
        columns.clear();
        columnMap.clear();
    }

    /**
     * Устанавливает, содержит ли коллекция колонок хотя бы один элемент.<p>
     * @return тип <code>boolean</code> <i>true</i> - если коллекция не содержит
     *    ни одного элемента. <i>false</i> - в противном случае.
     */
    @Override
    public boolean isEmpty() {
        return columns.isEmpty();
    }

    /**
     * Дообавляет все элементы данной коллекции, в конец коллекции, 
     * заданной параметром. <p>
     * Коллекция, заданная параметром не очищается пере копированием в нее
     * элементов исходной коллекции. Для каждого копируемого элемента
     * определяется новая ссылка на тот же самый элемент исходной коллекции, т.е.
     * новый объект не создается.
     * @param columns  результирующая коллекция.
     */
    @Override
    public void copyTo(DataColumnCollection columns) {

        DataColumn dc;//My 06.03.2012 = null;
        for (int i = 0; i < this.columns.size(); i++) {
            dc = this.columns.get(i);
            columns.add(dc);
        }//for

    }
/*
    public Map<String, DataColumn> getColumnMap() {
        return this.columnMap;
    }
*/
    /**
     * Возвращает колонку, если она входит в коллекцию, по заданному индексу 
     * колонки.
     * @param columnIndex индекс колонки.
     * @return колонку по заданному индексу. 
     * @throws IndexOutOfBoundsException - Если индекс для индекса выполнено условие
     *     (index < 0 || index >= getCount())
     */
    @Override
    public DataColumn get(int columnIndex) {
        return columns.get(columnIndex);
    }
    /**
     * Возвращает колонку, если она входит в коллекцию, по заданному имени. 
     * @param columnName имя колонки.
     * @return колонку по заданному имени или <code>null</code>, если такой 
     *    колонки не найдено. 
     */ 
    @Override
    public DataColumn get(String columnName) {
        return columnMap.get(columnName.toUpperCase());
    }

    /**
     * Перемещает колонку внутри коллекции таким образом, что ее новый индекс
     * становится разным заданному.
     * <ul>
     *   <li>Если заданная колонки содержится в коллекции и ее индекс совпадает с 
     *       новым индексом, то выполнение метода завершается.
     *   </li>
     *   <li>Если выполнено условие <code><pre>
     *          index < 0 || index > getCount()</pre></code>
     *       то выбрасывается исключение.
     *   </li>
     *   <li>Если колонка в коллекции не обнаружена, то она вставляется
     *       в заданную позицию.
     *   </li>
     * </ul>
     * <p>
     * Обратим внимание на различия в  результате исполнения данного метода 
     * и метода {@link #move}
     * @param index новый индекс колонки
     * @param column перемещаемая колонка
     * @throws IndexOutOfBoundsException если выполнено 
     * <code><pre> index < 0 || index > getCount()</pre></code>,
     */
    @Override
    public void set(int index, DataColumn column) {

        int oldIndex = indexOf(column);
        if (oldIndex == index) {
            return;
        }
        if (index < 0 || index > getCount()) {
            throw new IndexOutOfBoundsException("Invalid constraint 'new index < 0 or  > column count'");
        }

        if (oldIndex < 0) {
            this.insert(index, column);
        } else if (index > oldIndex) {
            move(oldIndex, index + 1);
        } else {
            move(oldIndex, index);
        }

    }

    /**
     * Возвращает количество элементов в коллекции колонок.<p>
     *
     * @return количество колонок в коллекции.
     */
    @Override
    public int getCount() {
        return columns.size();
    }

    /**
     * Добавляет обработчик события изменеия в коллекции к списку обработчиков
     * типа <code>tdo.DataColumnCollectionListener</code>.
     * Событие <code>tdo.DataColumnCollectionEvent</code> возбуждается в 
     * слуячаях добавления, вставки, удаления и перемещения колонки.
     * 
     * @param l добавляемый обработчик
     * @see tdo.event.DataColumnCollectionListener
     * @see tdo.event.DataColumnCollectionEvent
     * @see #removeDataColumnColectionListener
     * @see #fireDataColumns
     */
    @Override
    public void addDataColumnCollectionListener(DataColumnCollectionListener l) {
        dataColumnsListenerList.add(l);
    }
    /**
     * Удаляет обработчик события изменеия в коллекции из списка обработчиков
     * типа <code>tdo.DataColumnCollectionListener</code>.
     * 
     * @param l удаляемый обработчик
     * @see tdo.event.DataColumnCollectionListener
     * @see tdo.event.DataColumnCollectionEvent
     * @see #addDataColumnColectionListener
     * @see #fireDataColumns
     */
    @Override
    public void removeDataColumnColectionListener(DataColumnCollectionListener l) {
        dataColumnsListenerList.remove(l);
    }

    /**
     * Оповещает все зарегистрированные обработчики события 
     * <code>tdo.DataColumnCollectionEvent</code>.
     * Событие <code>tdo.DataColumnCollectionEvent</code> возбуждается в 
     * слуячаях добавления, вставки, удаления и перемещения колонки.
     * 
     * @param e экземпляр события
     * @see tdo.event.DataColumnCollectionListener
     * @see tdo.event.DataColumnCollectionEvent
     * @see #removeDataColumnColectionListener
     */
    protected void fireDataColumns(DataColumnCollectionEvent e) {
        if (dataColumnsListenerList.isEmpty()) {
            return;
        }
        for (DataColumnCollectionListener l : new ArrayList<DataColumnCollectionListener>(this.dataColumnsListenerList)) {
            l.columnsChanged(e);
        }
    }
    /**
     * Обработчик события <code>PropertyChangeEvent</code>.
     * Когда колонка добавляется к коллекции, ей назначается обработчик
     * события изменения свойств колонки.<p> 
     * Когда колонка удаляется из коллекции, обработчик
     * события изменения свойств колонки удаляется.
     */
    public class PropertyChangeHandler implements PropertyChangeListener, Serializable {

        public PropertyChangeHandler() {
        }

        @Override
        public void propertyChange(PropertyChangeEvent e) {
            if (e.getPropertyName().equals("columnName")) {
                if (e.getOldValue() != null) {
                    columnMap.remove(((String) e.getOldValue()).toUpperCase());
                }
                if (e.getNewValue() != null) {
                    columnMap.put(((String) e.getNewValue()).toUpperCase(), (DataColumn) e.getSource());
                }
            }
        }
    }//class PropertyChangeHandler
/*
    protected void setColumnMap(Map<String, DataColumn> columnMap) {
        this.columnMap = columnMap;
    }
*/
    protected void initColumns() {
        this.columns = new ArrayList<DataColumn>(10);
        this.columnMap = new HashMap<String, DataColumn>();
    }
    /**
     * Создает и возвращает новый экземпляр коллекции колонок, используя
     * конструктор без параметров {@link tdo.DefaultDataColumnCollection} как 
     * специфицировано {@link tdo.DataColumnCollection}.
     * 
     * @return новый экземпляр коллекции
     */
    @Override
    public DataColumnCollection create() {
        return new DefaultDataColumnCollection();
    }

    /**
     * Для целей тестирования. Может содержать любой код.
     * @return
     */
    @Override
    public boolean isValid() {
        boolean r = true;
        /*        for (int i = 0; i < columns.size(); i++) {
        DataColumn dc = columns.get(i);
        if (indexOf(dc) != i) {
        r = false;
        break;
        }
        }
        
        int n = 0;
        for (int i = 0; i < this.getCount(); i++) {
        DataColumn dc = get(i);
        if (dc.getKind() == DataColumn.DATA_KIND) {
        
        if (dc.getCellIndex() != n) {
        return false;
        }
        n++;
        } else {
        if (dc.getCellIndex() != -1) {
        return false;
        }
        }
        }
         */
        return r;
    }
}//class DefaultDataColumnCollection
