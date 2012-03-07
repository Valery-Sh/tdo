/*
 * InternalViewImpl.java
 *
 * Created on 10 Ноябрь 2006 г., 12:17
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
     * @return количество рядов для текущего в этом <code>DataView</code>
     *    менеджера коллекции рядов из {@link #store}
     */
    public int getRowCount();

    /**
     * @param rowIndex индекс возвращаемого ряда
     * @return Возвращает ряд таблицы по заданному индексу ряда.<p>
     * Возвращаемый ряд выбирается из текущего менеджера рядов
     * {@link #store}.
     */
    public DataRow getRow(int rowIndex);

    /**
     * Устанавливает заданный ряд в заданную позицицию. <p>
     * Ряд с индексом, определяемым параметром <code>rowIndex</code> должен
     * существовать на момент вызова метода. Таким образом, фактически
     * производится замена существующего ряда в коллекции текущего
     * менеджера {@link #store}
     * @param row новое значение ряда с индексом <code>rowIndex</code>
     * @param rowIndex индекс замещаемого ряда
     */
    public void setRow(DataRow row, int rowIndex);

    /**
     * Выполняет поиск заданного ряда  в коллекции текущего
     * менеджера {@link #store} и возвращает его индекс. <p>
     * @param row ряд, для которого определяется индекс
     * @return -1, если коллекция <code>store</code> не содержит
     *      заданного ряда. Значение большее или равное нулю, в
     *      противном случае.
     */
    public int find(DataRow row);

    /**
     * Добавляет заданный объект ряда в конец коллекции текущего
     * менеджера {@link #store} ряд и возвращает его индекс. <p>
     * Если таблица нажодится в состоянии, когда ее метод
     * {@link BaseDataTable#isLoading} возвращает <code>true</code>, то
     * ряд добавляются также в коллекции, связанные с операциями, ссылки на
     * которые имеются в {@link #storeList}, состояние ряда устанавливается
     * в {@link RowState#UNCHANGED}.<p>
     *
     * Если метод {@link BaseDataTable#isLoading} возвращает <code>false</code>,
     * то ряд НЕ добавляются в коллекции, связанные с операциями, а
     * состояние ряда устанавливается в {@link RowState#INSERTING}. Это
     * поведение обусловлено тем, что приложение, вызвавшее метод, добавляет
     * ряд и, до тех пор, пока не буден выдан метод postRow для ряда
     * он не должен быть виден приложению нигде, кроме текущего DataView.
     *
     * @param row добавляемый ряд
     * @return индекс добавленного ряда по отношению к текущему
     *         <code>store</code>
     */
    public int addRow(DataRow row);

    public DataRow addRow();

    /**
     * Вставляет "пустой" ряд в заданную позицию текущей коллекции рядов. <p>
     * Вставленный ряд получает индекс равный <code>rowIndex</code>. Ряды до
     * вставки с индексами большими или равными <code>rowIndex</code>
     * сдвигаются, получая новый индекс, на 1 больше старого. <p>
     *
     * Если таблица нажодится в состоянии, когда ее метод
     * {@link BaseDataTable#isLoading} возвращает <code>true</code>, то
     * ряд добавляются (но не вставляется) также в коллекции, связанные с
     * операциями, ссылки на которые имеются в {@link #storeList},
     * состояние ряда устанавливается в {@link RowState#UNCHANGED}.<p>
     *
     * Если метод {@link BaseDataTable#isLoading} возвращает <code>false</code>,
     * то ряд НЕ добавляются  в коллекции, связанные с операциями, а
     * состояние ряда устанавливается в {@link RowState#INSERTING}. Это
     * поведение обусловлено тем, что приложение, вызвавшее метод, вставляет
     * ряд и, до тех пор, пока не буден выдан метод postRow для ряда
     * он не должен быть виден приложению нигде, кроме текущего DataView.
     * @param rowIndex позиция вставки. Может принимать значение от 0 до
     *   количества рядов до вставки в текущей коллекции рядов.
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