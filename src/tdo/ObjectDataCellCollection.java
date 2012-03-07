/*
 * ObjectDataCellProvider.java
 * 
 */
package tdo;

import tdo.service.TableServices;

/**
 *      Класс является оболочкой вокруг любого объекта, придерживающегося
 * спецификации Java Beans.
 * 
 *      Позволяет рассматривать java bean не как набор полей и свойств, а как 
 * коллекцию ячеек, доступ к которым осуществляется с использованием 
 * целочисленного индекса.
 * 
 *  <p>Поскольку класс реализует интерфейс <code>tdo.dataCellCollection</code>,
 * то он может использоваться совместно, например с {@link tdo.DefaultDataRow}.
 * 
 *  <p>Каждый ряд, используемый  {@link tdo.Table} содержит собственный экземпляр
 * <code>DataCellCollection</code>.
 * 
 * <p> По умолчанию, применяются правила соответствие индекса колонки в 
 * коллекции колонок индексу ячейки в коллекции ячеек:
 * <ol>
 *   <li>если для колонки с индексом <code><i>columnIndex</i></code>
 *       выполнено условие getKind() == DataColumn.CALC_KIND, то для нее нет
 *       соответствующей ячейки в коллекции ячеек. 
 *   </li>
 *   <li> если для колонки с индексом <code><i>columnIndex</i></code>
 *       выполнено условие getKind() == DataColumn.DATA_KIND, то ей 
 *       соответствует некоторый индекс ячейки, которой, в свою очередь,
 *       соответствует поле или свойство объекта данных.
 *   </li>
 * </ol>
 * 
 * <p>Таким образом, вычисляемые колонки не имеют явно созданных данных.
 * 
 * <p>Коллекция ячеек связана с таблицей <code>tdo.Table</code>. Эта связь 
 * реализована через использование сервисов 
 * {@link tdo.service.TableServices#getCellServices()).
 * 
 * <p>
 */
public class ObjectDataCellCollection implements DataCellCollection {

    /**
     * Контекст таблицы, для которой создается экземпляр класса.
     */
    protected TableServices context;
    /**
     * Хранилище значения свойства - ссылка на объект данных, для которого 
     * строися коллекция ячеек.
     */
    private Object rowObject;
    /**
     * Хранилище значения свойства - кдасс объекта данных.
     */
    private Class objectClass;
//    private ObjectRowSupport objectRowSupport;
    /**
     * 
     */
    private ObjectValueAccessor objectRowSupport;

    /**
     * Создает новый экземпляр для заданоого контекста таблицы. 
     * Нормальное функционирование созданного конструктором объекта возможно,
     * если явно назначается значение свойства <code>objectClass</code> и
     * <code>objectRowSupport</code>. 
     * 
     * @param context контекст таблицы
     * @see 
     */
    public ObjectDataCellCollection(TableServices context) {
        this.context = context;
        rowObject = null;
        objectClass = null;
        objectRowSupport = null;
    }

    /**
     * Создает новый экземпляр для заданоого контекста таблицы и заданного
     * объекта, реализующего интерфейс <code>tdo.ObjectValueAccessor</code>.
     * Конcтруктор позволяет создавать коллекции ячеек, для объектов java
     * без использования интроспекции, поскольку требует для функционирования
     * класса объект, реализующий относительно простой интерфейс 
     * <code>ObjectValueAccessor</code> и не требует использования класса
     * <code>ObjectRowSupport</code>. 
     * <p>Конструктор удобен при тестировании с применением <code>JUnit</code>.
     * @param context контекст таблицы
     * @param va 
     * @see tdo.ObjectValueAccessor
     * @see tdo.ObjectRowSupport
     * @see tdo.ObjectDataRowSupport     
     */
    public ObjectDataCellCollection(TableServices context, ObjectValueAccessor va) {
        this(context);
        objectRowSupport = va;
    }

    /**
     * Возвращает объект, для которого создана данная коллекция.
     * @return java bean объект данных
     * @see #setObject(java.lang.Object)
     */
    @Override
    public Object getObject() {
        return this.rowObject;
    }

    /**
     * Возвращает класс, объекты которого могут быть использованы
     * для создания коллекции.
     * @return класс java bean объекта данных
     * @see #setObjectClass(java.lang.Class) 
     */
    public Class getObjectClass() {
        return this.objectClass;
    }

    /**
     * Устанавливает объект, для которого будет создаваться коллекция.
     * @param  rowObject новое значение объекта
     * @see #getObject
     */
    public void setObject(Object rowObject) {
        this.rowObject = rowObject;
    }

    /**
     * Устанавливает класс, объекты которого могут быть использованы
     * для создания коллекции.
     * 
     * @param  objectClass новое значение класса объектов
     * @see #setObjectClass(java.lang.Class) 
     */
    public void setObjectClass(Class objectClass) {
        this.objectClass = objectClass;
    }

    /**
     * Устанавливает новое значение объекта, создающего внутреннюю 
     * инфраструктуру для java bean объекта, используемую компонентами
     * {@link tdo.Table}.
     * 
     * @param objectRowSupport новое значение объекта поддержки
     */
    protected void setObjectRowSupport(ObjectRowSupport objectRowSupport) {
        this.objectRowSupport = objectRowSupport;
    }

    /**
     * Возвращает значение свойства bean объекта по заданному имени.
     * @param pName имя свойства
     * @return значение свойства объекта
     * @see #getFieldValue
     * @see #setPropertyValue
     */
    protected Object getPropertyValue(String pName) {
        return this.objectRowSupport.getValue(pName, this.rowObject);
    }

    /**
     * Возвращает значение поля bean объекта по заданному имени.
     * @param pName имя поля
     * @return значение свойства объекта
     */
    protected Object getFieldValue(String fName) {
        return this.objectRowSupport.getValue(fName, this.rowObject);
    }

    /**
     * Устанавливает новое значение поля bean объекта для заданному имени.
     * @param fName имя поля
     * @param value новое значение для поля
     */
    protected void setFieldValue(String fName, Object value) {
        this.objectRowSupport.setValue(fName, this.rowObject, value);
    }

    /**
     * Устанавливает новое значение свойства bean объекта для заданному имени.
     * @param pName имя поля
     * @param value новое значение для свойства
     */
    protected void setPropertyValue(String pName, Object value) {
        this.objectRowSupport.setValue(pName, this.rowObject, value);
    }

    /**
     * Возвращает значение свойства или поля объекта по индексу колонки.
     * Индекс колонки - это индекс, как определено
     * {@link tdo.DataColumnCollection}.
     * @param columnIndex индекс колонки в коллекции колонки таблицы
     * @return значение свойства или поля объекта
     * @see #getValue(String) 
     * @see #setValue(Object,int) 
     */
    @Override
    public Object getValue(int columnIndex) {
        Object result = null;
        String pname = context.getCellServices().getPropertyName(columnIndex);
        String fname = context.getCellServices().getFieldName(columnIndex);

        //DataColumn column = context.getColumnServices().columns(columnIndex);
        if (pname != null) {
            result = getPropertyValue(pname);
        } else {
            if (fname != null) {
                result = getFieldValue(fname);
            }
        }
        return result;
    }

    /**
     * Возвращает значение свойства или поля объекта по имени колонки.
     * Имя колонки - это имя, как определено
     * {@link tdo.DataColumn}.
     * @param columnName имя колонки в коллекции колонки таблицы
     * @return значение свойства или поля объекта
     * @see #getValue(int) 
     * @see #setValue(Object,String) 
     */
    @Override
    public Object getValue(String columnName) {
        return getValue(context.getCellServices().getColumnIndex(columnName));
    }

    /**
     * Устанавливает новое значение свойства или поля объекта по индексу колонки.
     * Индекс колонки - это индекс, как определено
     * {@link tdo.DataColumnCollection}.
     * @param value новое значение свойства или поля
     * @param columnIndex индекс колонки в коллекции колонки таблицы
     * @see #getValue(int) 
     */
    @Override
    public Object setValue(Object value, int columnIndex) {
        int mapped = context.getCellServices().getCellIndex(columnIndex);
        if (mapped == -1) {
            return null;
        }
        Object oldValue = this.getValue(columnIndex);
        String pname = context.getCellServices().getPropertyName(columnIndex);
        String fname = context.getCellServices().getFieldName(columnIndex);

        if (pname != null) {
            setPropertyValue(pname, value);
        } else {
            if (fname != null) {
                setFieldValue(fname, value);
            }
        }
        return oldValue;
    }

    /**
     * Устанавливает новое значение свойства или поля объекта по индексу колонки.
     * Индекс колонки - это индекс, как определено
     * {@link tdo.DataColumnCollection}.
     * @param value новое значение свойства или поля
     * @param columnIndex индекс колонки в коллекции колонки таблицы
     * @see #getValue(int) 
     * @see #setValue(Object,int) 
     */
    protected void setValue1(Object value, int columnIndex) {
        int mapped = context.getCellServices().getCellIndex(columnIndex);
        if (mapped == -1) {
            return;
        }
        String pname = context.getCellServices().getPropertyName(columnIndex);
        String fname = context.getCellServices().getFieldName(columnIndex);

        if (pname != null) {
            setPropertyValue(pname, value);
        } else {
            if (fname != null) {
                setFieldValue(fname, value);
            }
        }
    }

    /**
     * Устанавливает новое значение свойства или поля объекта по имени колонки.
     * Имя колонки - это имя, как определено
     * {@link tdo.DataColumn}.
     * @param value новое значение свойства или поля
     * @param columnName имя колонки в коллекции колонки таблицы
     * @see #getValue(int) 
     * @see #getValue(String) 
     * @see #setValue(Object,int) 
     */
    @Override
    public Object setValue(Object value, String columnName) {
        return setValue(value, context.getCellServices().getColumnIndex(columnName));
    }

    /**
     * Копирует содержимое ячеек заданной коллекции в ячейки текущей коллекции.
     * @param source коллекция-источник ячеек для копирования.
     */
    @Override
    public void copyCells(DataCellCollection source) {
        for (int i = 0; i < context.getCellServices().getColumnCount(); i++) {
            if (context.getCellServices().isDataKind(i)) {
            if ( ! context.getCellServices().copyCell(i,source.getValue(i),getValue(i)) )
                this.setValue(source.getValue(i), i);
            }
        }
    }

    /**
     * Значения элементов заданного массива заменяют значения в коллекции ячеек.
     * i-й элемент массива записывается на место i-го элемента списка ячеек. 
     * Остальные ячейки не изменяются.
     * @param source массив-источник значеек ячеек
     */
    @Override
    public void copyCells(Object[] source) {
/*        for (int i = 0; i < source.length; i++) {
            this.setValue1(source[i], i);
        }
 */
        for (int i = 0; i < source.length; i++) {
            int columnIndex = context.getCellServices().columnIndexByCell(i);
            if ( ! context.getCellServices().copyCell(columnIndex, source[i], getValue(i)))
                 this.setValue1(source[i], i);
        }
        
    }

    /**
     * Не производит никаких действий
     * @param columnIndex
     */
    @Override
    public void columnAdded(int columnIndex) {
    }

    /**
     * Не производит никаких действий
     * @param columnIndex
     */
    @Override
    public void columnRemoved(int columnIndex) {
    }

    /**
    /**
     * Не производит никаких действий
     * @param columnIndex
     * @param oldCellIndex
     * @param newCellIndex
     */
    @Override
    public void columnMoved(int columnIndex, int oldCellIndex, int newCellIndex) {
    }
    /**
     * Возвращает количество элементов коллекции.
     * @return сумма: <i>количество полей + соличество свойств</i>
     */
    public int size() {
        return this.objectRowSupport.size();
    }

}
