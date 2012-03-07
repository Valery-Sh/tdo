/*
 */

package tdo;

import java.util.ArrayList;
import java.util.List;
import tdo.service.TableServices;

/**
 *      Класс поддерживает данные рядов, которые хранятся в списке типа
 * <code>java.util.ArrayList</code>. 
 * Каждый ряд, используемый  {@link tdo.Table} содержит собственный экземпляр
 * <code>DataCellCollection</code>.
 * 
 * <p>Если таблица {@link tdo.Table} имеет <code><b>n</b></code> колонок из которых
 * <code><b>k</b></code> колонок имеют значение свойства <code>kind</code>
 * равное {@link tdo.DataColumn#DATA_KIND}, то список ячеек имеет длину 
 * <code><b>k</b></code>. По умолчанию, применяются правила соответствие индекса
 * колонки в коллекции колонок индексу ячейки в коллекции ячеек:
 * <ol>
 *   <li>если для колнки с индексом <code><i>columnIndex</i></code>
 *       выполнено условие getKind() == DataColumn.CALC_KIND, то для нее нет
 *       соответствующей ячейки в коллекции ячеек. 
 *   </li>
 *   <li>если для колонки с индексом <code><i>columnIndex</i></code>
 *       выполнено условие getKind() == DataColumn.DATA_KIND, то ей 
 *       соответствует индекс ячейки <code><i>columnIndex - calcCount</i><code>,
 *       где <i><code>calcKind</code></i> - это количество колонок, индекс
 *       которых меньше <code>columnIncex</code> и для которых 
 *       getKind() != DataColumn.DATA_KIND.
 *   </li>
 * </ol>
 * 
 * <p>Таким образом, вычисляемые колонки не имеют явно созданных данных.
 * 
 * <p>Коллекция ячеек связана с таблицей <code>tdo.Table</code>. Эта связь 
 * реализована через использование сервисов 
 * {@link tdo.service.TableServices#getCellServices()).
 */
public class DefaultDataCellCollection implements DataCellCollection{
    /**
     * Хранилище ячеек ряда
     */
    protected List cells;

    /**
     * Контекст таблицы.
     */
    protected TableServices context;
    /**
     * Ряд, для которого создана коллекция ячеек.
     */
    //protected DataRow row;

    private DefaultDataCellCollection() {
    }
    
    /**
     * Создает экземпляр класса для заданного контекста.
     * 
     * @param context
     */
    public DefaultDataCellCollection(TableServices context) {
        this.context = context;
        //this.row = row;
        int cc = context.getCellServices().getColumnCount();
        this.cells = new ArrayList(cc);
        for (int i = 0; i < cc; ++i) {
            if ( context.getCellServices().isDataKind(i) ) {
                cells.add(context.getCellServices().createBlankObject(i));
            }
        }
    }

    /**
     * Создает экземпляр класса для заданного ряда и заданного контекста и 
     * заполняет ячейки значениями из заданного массива, используя начальный и 
     * конечный индексы.
     * Массив <code>values</code> должен иметь размер равный числу колонок в
     * коллекции колонок <code>tdo.Table</code> и <i>code>i-й</code></i> элемент
     * массива соответствует <i>code>i-й</code></i> колонке.
     * 
     * @param context заданный контекст таблицы. 
     * @param row ряд, для которого создается коллекция ячеек
     * @param values массив значения ячеек
     * @param start начальный индекс колонки
     * @param end конечный индекс колонки
     */
/*    protected DefaultDataCellCollection(TableServices context, DataRow row, Object[] values, int start, int end) {
        //this.table = table;
        //this.table = table;
        this.context = context;        
        this.row = row;
        
        this.cells = new ArrayList(context.getColumnServices().getColumnCount(DataColumn.DATA_KIND));

        for (int i = start; i < end; ++i) {
            int kind = context.getColumnServices().columns(i - start).getKind();
            if (kind == DataColumn.DATA_KIND) {
                cells.add(values[i]);
            }
        }
    }
 */ 
    /**
     * Копирует содержимое ячеек заданного ряда в ячейки другого заданного ряда.
     * Состояние, определяемое свойством <code>state</code>, а
     * также поля и свойства ряда <i><code>to</code</i> не изменяются.
     * 
     * @param target ряд назначения для копирования
     * @param source ряд-источник ячеек для копирования
     * @see #copyCells(DataRow)
     */
/*    private void copyCells(DataRow target, DataRow source) {
        DefaultDataCellCollection dto = (DefaultDataCellCollection) ((DataRow) target).getCellCollection();
        DefaultDataCellCollection dfrom = (DefaultDataCellCollection) ((DataRow) source).getCellCollection();
        for (int i = 0; i < dfrom.getCells().size(); i++) {
            dto.getCells().set(i, dfrom.getCells().get(i));
        }
    }
 */ 
    /**
     * Копирует содержимое ячеек заданной коллекции в ячейки текущей коллекции.
     * @param source коллекция-источник ячеек для копирования. Класс объекта
     *      должен быть <code>DefaultDataCellCollection</code> или наследовать
     *      от него.
     */
    @Override
    public void copyCells(DataCellCollection source) {
/*        DefaultDataCellCollection dfrom = (DefaultDataCellCollection)source;        
        for (int i = 0; i < dfrom.getCells().size(); i++) {
            Object from = dfrom.getCells().get(i);
            Object to = getCells().get(i);
            if ( ! context.getCellServices().copyCell(from,to));
                getCells().set(i, dfrom.getCells().get(i));
        }
 */ 

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
      
        for (int i = 0; i < source.length; i++) {
            int columnIndex = context.getCellServices().columnIndexByCell(i);
            if ( ! context.getCellServices().copyCell(columnIndex, source[i], getValue(i)))
                 getCells().set(i, source[i]);
        }
    }

    /**
     * @return список ячеек.
     */
    protected List getCells() {
        return this.cells;
    }
    /**
     * Устанавливает список ячеек
     * @param cells новый список
     */
    protected void setCells(List cells) {
        this.cells = cells;
    }
    /**
     * Возвращает значение ячейки по заданному индексу колонки.
     * Колонка <code>columnIndex</code> не должна быть вычисляемой, 
     * в противном случае метод или выбросит исключение или результат может
     * оказаться не предсказуемым. Это ответственность класса 
     * <code>DataRow</code> не допускать такой вызов.
     * @param columnIndex индекс колонки, для которой определяется значение 
     * ячейки.
     * @return значение ячейки
     */
    @Override
    public Object getValue(int columnIndex) {
/*        int kind = context.getColumnServices().columns(columnIndex).getKind();
        if (kind == DataColumn.CALC_KIND) {
            return context.getColumnServices().calculateColumnValue(row, columnIndex);
        }
*/
        int mappedIndex = context.getCellServices().getCellIndex(columnIndex);
        return cells.get(mappedIndex);
    }
    /**
     * Возвращает значение ячейки по заданному имени колонки.
     * Колонка с именем <code>columnName</code> не должна быть вычисляемой, 
     * в противном случае метод или выбросит исключение или результат может
     * оказаться не предсказуемым. Это ответственность класса 
     * <code>DataRow</code> не допускать такой вызов.
     * 
     * 
     * @param columnName имя колонки, для которой определяется значение 
     *    ячейки.
     * @return значение ячейки
     */
    @Override
    public Object getValue(String columnName) {
        return getValue( context.getCellServices().getColumnIndex(columnName) );
    }

    /**
     * Устанавливает новое значение ячейки по заданному индексу колонки.
     * Колонка <code>columnIndex</code> не должна быть вычисляемой.
     * @param value новое значение ячейки
     * @param columnIndex индекс колонки, для которой определяется значение 
     *    ячейки.
     * @return предыдущее значение ячейки до установки нового
     */
    @Override
    public Object setValue(Object value, int columnIndex) {

        int mapped = context.getCellServices().getCellIndex(columnIndex);
        if (mapped == -1) {
            return null;
        }
        Object oldValue = cells.get(mapped);
        cells.set(mapped, value);
        return oldValue;
    }

    /**
     * Устанавливает новое значение ячейки по заданному имени колонки.
     * Колонка <code>columnName</code> не должна быть вычисляемой.
     * @param value новое значение ячейки
     * @param columnName имя колонки, для которой определяется значение 
     *    ячейки.
     * @return предыдущее значение ячейки до установки нового
     */
    @Override
    public Object setValue(Object value, String columnName) {
        return setValue(value,context.getCellServices().getColumnIndex(columnName));
    }    
    /**
     * Метод выполняется в ответ на добавление колонки к коллекции колонок.
     * Когда колонка добавляется к коллекции, то необходимо реорганизовать
     * коллекцию ячеек. В реализации данного класса к списку ячеек добавляется
     * новая ячейка, содержащая значение, как определено методом 
     * {@link tdo.DataColumn#createBlankObject() }.
     * <p>Если ряд, для которого создана коллекция ячеек поддерживает состояние
     * ряда, то объект типа {@link tdo.RowState} ряда оповещается также
     * вызовом метода {@link tdo.RowState#columnAdded(tdo.DataColumn)}.
     * <p><b>Примечание.</b> Как следует из спецификации метода 
     *      {@link tdo.DataRow#getState() } метод должен возвращать 
     *      <code>null</code>, если состояние ряда не поддерживается.
     * @param columnIndex 
     */
    @Override
    public void columnAdded(int columnIndex) {
        int cellIndex = context.getCellServices().getCellIndex(columnIndex);
        
        Object o = context.getCellServices().createBlankObject(columnIndex);

        cells.add(cellIndex, o);
//        if ( row.getState() != null )
//            row.getState().columnAdded(column);
    }

    /**
     * Метод выполняется в ответ на удаление колонки из коллекции колонок.
     * Когда колонка удаляется из коллекции, то необходимо реорганизовать
     * коллекцию ячеек. В реализации данного класса из списка ячеек удаляется
     * ячейка, соответствующая заданной колонке.
     * <p>Если ряд, для которого создана коллекция ячеек поддерживает состояние
     * ряда, то объект типа {@link tdo.RowState} ряда оповещается также
     * вызовом метода {@link tdo.RowState#columnRemoved(tdo.DataColumn)}.
     * <p><b>Примечание.</b> Как следует из спецификации метода 
     *      {@link tdo.DataRow#getState() } метод должен возвращать 
     *      <code>null</code>, если состояние ряда не поддерживается.
     * @param columnIndex 
     */
    @Override
    public void columnRemoved(int columnIndex) {
        int cellIndex = context.getCellServices().getCellIndex(columnIndex);
        cells.remove(cellIndex);
    }

    /**
     * Метод выполняется в ответ на перемещение колонки внутри коллекции колонок.
     * Если заданная колонка является вычисляемой, то метод завершается не 
     * производя ни каких операций.
     * <p>Когда колонка перемещается, то необходимо реорганизовать
     * коллекцию ячеек. В реализации данного класса из списка ячеек удаляется
     * ячейка с заданным индексом <code>oldCellIndex</code> и вставляется новая
     * ячейка на место заданное <code>newCellIndex</code>. Значение в ячейке
     * <code>oldCellIndex</code> записывается в ячейку <code>newCellIndex</code>.
     * 
     * <p>Если ряд, для которого создана коллекция ячеек поддерживает состояние
     * ряда, то объект типа {@link tdo.RowState} ряда оповещается также
     * вызовом метода {@link tdo.RowState#columnMoved(tdo.DataColumn, int, int, int) )}.
     * <p><b>Примечание.</b> Как следует из спецификации метода 
     *      {@link tdo.DataRow#getState() } метод должен возвращать 
     *      <code>null</code>, если состояние ряда не поддерживается.
     * @param columnIndex индекс колонки бывший до перемещения
     * @param oldCellIndex старый индекс ячейки, соответствующий колонке
     * @param newCellIndex новый индекс ячейки соответствующий колонке
     * @see tdo.impl.AbstractTable#columnsHandler
     */
    @Override
    public void columnMoved(int columnIndex, int oldCellIndex, int newCellIndex) {
//if (column.getKind() != DataColumn.DATA_KIND) {
//            return;
//        }
//        if ( context.getColumnServices().isCalculated(columnIndex) )
//            return;
        Object o = cells.get(oldCellIndex);

        cells.remove(oldCellIndex);
        cells.add(newCellIndex, o);
        
//        if ( row.getState() != null )
//            row.getState().columnMoved(column, fromColumnIndex, oldCellIndex, newCellIndex);
    }

    public int size() {
        return this.cells.size();
    }

    @Override
    public Object getObject() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}//class DefaultDataCellCollection
