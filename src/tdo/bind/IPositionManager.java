/*
 * IPositionManager.java
 *
 * Created on 29 Октябрь 2006 г., 10:24
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.bind;

import java.awt.Component;
import tdo.Table;

/**
 * Определяет требования, предъявляемые к объекту, управляющему позицией
 * активного ряда класса {@link pdata.BaseDataTable} и связанными с объектом компонентами
 * типа {@link java.awt.Component}. <p>
 * Класс, реализующий интерфейс, связан с одной таблицей данных типа pdata.BaseDataTable.
 * Класс функционирует как контейнер для компонентов типа java.awt.Component.
 * Любое количество объектов типа java.awt.Component могут быть добавлены в контейнер,
 * становясь, таким образом <i>связанными</i> с объектов pdata.BaseDataTable. <p>
 * 
 * Defines the requirements for an object suitable for use with pdata.BaseDataTable 
 * in conjunction with managing active row position.
 */
public interface IPositionManager {
    /**
     * @return ссылку на объект, который связан с компонентами контейнера.
     */
    public Table getTable();
    /**
     * Устанавливает ссылку на объект, связанный с компонентами, входящими в контейнер.
     * @param dataTable ссылка на устанавливаемый объект.
     */
    public void setTable( Table table);
    /**
     * Добавляет компонент в контейнер, делая компонент связанным с тавлицей данных.
     * @param comp добавляемый компонент.
     */
    public void add(Component comp);
    /**
     * Добавляет компонент в контейнер, делая компонент и определенную параметром
     * колонку связанными с тавлицей данных.
     * 
     * @param comp добавляемый компонент.
     * @param columnName - имя колонки объекта типа BaseDataTable, используемой для связывания.
     */
    public void add(Component comp, String columnName);
    /**
     * Удаляет компонент из контейнера.<p>
     * Класс, реализующий интерфейс, освобождает возможно полученные ресурсы, и выполняет
     * действия по восстановлению состояния компонента. Например, усстановленные для
     * компонента слушатели событий должны быть удалены.
     * 
     * @param comp удаляемый компонент.
     */
    public void remove(Component comp);
    
    /**
     * Производит попытку установить значение ячейки ряда таблицы. <p>
     * Например, при изменении значения свойства text компонента JTextField
     * необходимо изменить знначение в соответствующей ячейке таблицы BaseDataTable. 
     * Код, ответственный за это изменение, должен воспользоваться данным методом и,
     * в свою очередь, по результату обращения к нему выполнить некоторые действия.
     * 
     * 
     * @param value - устанавливаемое значение.
     * @param rowIndex индекс ряда таблицы BaseDataTable.
     * @param columnIndex - индекс колонки таблицы BaseDataTable.
     * @return true, если попытка установить значение удалась. false - в противном случае.
     */
    public boolean setValue( Object value,int rowIndex, int columnIndex);
    public boolean setValue( Object value,int rowIndex, String columnName);

    /**
     * Производит попытку изменить позицию активного ряда таблицы. <p>
     * Например, при изменении выбранной (selected) строки JTable
     * необходимо изменить значение активного ряда таблицы BaseDataTable. 
     * Код, ответственный за это изменение, должен воспользоваться данным методом и,
     * в свою очередь, по результату обращения к нему выполнить некоторые действия.
     * 
     * 
     * @param rowIndex индекс нового ряда таблицы BaseDataTable.
     * @return true, если попытка установить значение удалась. false - в противном случае.
     */
    public boolean moveTo( int rowIndex );

    /**
     * Позволяет провести дополнительную обработку старой таблицы, например, удалить 
     * обработчики событий. <p>
     * Обычно вызывается непосредственно перед установкой нового значения свойства 
     * <code>dataTable</code>.
     * @param oldDataTable старое значение свойства <code>dataTable</code>.
     * @param newDataTable новое значение.
     *
     */
    public void dataTableChanging(Table oldTable, Table newTable);
    
    /**
     * Позволяет провести инициализацию таблицы, например, добавить требуемые
     * обработчики событий. <p>
     * Обычно вызывается сразу после нового значения свойства 
     * <code>dataTable</code>.
     * @param newDataTable новое значение.
     *
     */
    public void dataTableChanged( Table newTable);
    
}
