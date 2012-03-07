package tdo;

import java.util.List;
import tdo.service.TableServices;

/**
 * Определяет функциональность объектов-коллекций, реализующих интерфейс.
 * Методы интерфейса позволяют хранить, добавлять, вставлять, удалять и 
 * заменять элементы типа {@link tdo.DataRow}. Доступ к рядам коллекции может
 * осуществляться по позиции (индексу) ряда.
 * <p>класс, реализующий интерфейс может поддерживать параллельно с коллекцией
 * рядов - объектов типа <code>DataRow</code> также список типа
 * <code>java.util.List</code> произвольных объектов.
 */
public interface DataRowCollection<T> {

   /**
     * Создает и добавляет ряд с "пустыми" ячейками в конец коллекции. 
     * Созданный ряд становится доступным вызовом метода 
     * <code>get(getCount()-1)</code>.
     * @return добавленный ряд.
     * @see tdo.DataRow
     */
    public DataRow add();
    /**
     * Добавляет заданный ряд  в конец коллекции. 
     * @param row добавляемый ряд.
     * @return индекс добавленного ряда
     * @see tdo.DataRow
     * @see #add()
     */
    public int add(DataRow row);

    /**
     * Удаляет все элементы из коллекции рядов, таким образом, что
     * метод {@link isEmpty} возвращает <code>true</code>.
     * Не проводит каких-либо изменений над рядами.
     */
    public void clear();
    /**
     * Каждый элемент заданной коллекции рядов добавляется в конец текущей
     * коллекции.
     * 
     * @param fromRows коллекция, ряды которой добавляются к текущей
     */
    public void copyFrom(DataRowCollection fromRows);
    /**
     * Каждый элемент заданной коллекции рядов добавляется в конец текущей
     * коллекции.
     *
     * @param fromRows коллекция, ряды которой добавляются к текущей
     */
    public void copyFrom(DataRow[] fromRows);

    /**
     * Создает и возвращает новую коллекцию рядов, тип которой совпадает с 
     * типом текущей коллекции.
     * @return новая коллекция типа <code>DefaultDataRowCollection</code>
     */
    public DataRowCollection create();
    /**
     * Создает и возвращает новую коллекцию рядов, тип которой совпадает с 
     * типом текущей коллекции.
     * Новой соллекции назначается заданный контекст. Если значение параметра
     * <code>populate</code> равно <code>true</code>, то элементы текущей
     * коллекции добавляются к вновь созданной.
     * @param context контекст, для которого создается коллекция рядов
     * @param populate , если равен <code>true</code>, то элементы текущей
     * коллекции добавляются к вновь созданной. <code>false</code> новая
     * коллекция не изменяется.
     * 
     * @return новая коллекция рядов с заданным контекстом 
     * 
     * @see #create()
     * @see tdo.service.TableServices
     */
    public DataRowCollection create(TableServices context, boolean populate);
    /**
     * Создает и возвращает новую коллекция рядов для заданного контекста
     * и такую, что разделяет одно и тоже внутреннее хранилище.
     * Метод полезен в случае, если не эффективно создавать коллекцию и
     * копировать в нее элементы другой коллекции.
     * @param context контекст таблицы, для которого создается коллекция
     * @return новая коллекция, внутреннее хранилище рядов которой совпадает
     *    с текущим хранилищем
     */
    public DataRowCollection createShared(TableServices context);

    /**
     * Удаляет ряд в позиции заданного индекса.
     * @param rowIndex тип <code>int</code> - индекс удаляемого ряда.
     * @return удаленный ряд
     * <code><pre>rowIndex < 0 || rowIndex >= getCount()</pre></code> 
     */
    public DataRow delete(int rowIndex);
    /**
     * Находит и удаляет заданный ряд из коллекции.
     * @param row удаляемый ряд.
     * @return индекс удаленного ряд. -1, если ряд отсутствует в коллекции
     */
    public int delete(DataRow row);

    /**
     * Возвращает ряд с заданным индексом.
     * @param rowIndex индекс искомого ряда
     * @return ряд с заданным индексом
     * @throws IndexOutOfBoundsException если 
     * <code><pre>rowIndex < 0 || rowIndex >= getCount()</pre></code>
     * @see #set
     */
    public DataRow get(int rowIndex);

    /**
     * Возвращает количество рядов в коллекции.
     * @return количество рядов в коллекции. Должно быть <pre> >= 0 </pre>
     */
    public int getCount();

    /**
     * Возвращает позицию заданного ряда в коллекции.
     * @param row ряд, для которого определяется индекс. Может быть <code>null</code>.
     * @return значение большее или равное 0, если ряд содержится в коллекции.
     *   -1, в противном случае. Также возвращается -1, если значение параметра 
     *    равно <code>null</code>
     */
    public int indexOf(DataRow row);
   /**
     * Создает и вставляет ряд с "пустыми" элементами в заданную позицию. 
     * @param rowIndex позиция вставки нового ряда
     * @return вставленный ряд.
     * @see #insert(int,DataRow)
     * @see #add()
     * @see tdo.DataRow
     * @throws IndexOutOfBoundsException если 
     * <code><pre>rowIndex < 0 || rowIndex > getCount()</pre></code>, т.е 
     *          допускаются 0 и getCount()
     * 
     * 
     */
    public DataRow insert(int rowIndex);
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
    public void insert(int rowIndex, DataRow row);
  /**
     * Устанавливает, содержит ли коллекция хотя бы один ряд. <p>
     *
     * @return тип <code>boolean</code> <i>true</i> - коллекция не содержит рядов;
     *                             <i>false</i> - коллекция содержит хотя бы
     *                             один ряд.
     */
    public boolean isEmpty();
    /**
     * Специфицируемый ряд заменяет ряд в заданной позиции коллекции.
     * 
     * @param row заменяющий ряд
     * @param rowIndex позиция заменяемого ряда
     * @throws IndexOutOfBoundsException если 
     * <code><pre>rowIndex < 0 || rowIndex >= getCount()</pre></code>
     */
    void set(int rowIndex, DataRow row);
    
    List<T> getObjectList();
    void setObjectList(List<T> olist);
    
}