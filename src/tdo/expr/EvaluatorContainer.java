/*
 * EvaluatorContainer.java
 *
 */

package tdo.expr;

import java.util.Vector;
import tdo.DataRow;

/**
 * Базовый класс для контейнеров {@link tdo.expr.PAND} и {@link tdo.expr.POR } .
 * Классы-наследники позволяют строить предикаты рядов, объединенные логикой
 * "И" и/или "ИЛИ".
 */
public class EvaluatorContainer implements RowEvaluator{
    /**
     * Внутреннее хранилище предикатов контейнера.
     */
    private Vector items;

    /**
     * Создает экземпляр класса и инициализирует внутреннюю коллекцию.
     */
    protected EvaluatorContainer() {
        this.items = new Vector(2);
    }
    
   /**
     * Добавляет предикат к коллекции контейнера. <p>
     * 
     * @param re добавляемый предикат типа <code>RowEvaluator</code>
    *  @see tdo.expr.RowEvaluator
    */
    public void add(RowEvaluator re) {
        this.items.add(re);
    }

    /**
     * Удаляет предикат из коллекции предикатов предиката-контейнера. <p>
     * 
     * @param re удаляемый предикат типа <code>RowEvaluator</code>
     * @see tdo.expr.RowEvaluator
     */
    public void remove(RowEvaluator re) {
        this.items.remove(re);
    }    
    /**
     * @return true , если контейнер не содержит элементов. false - в противном
     *    случае
     */
    public boolean isEmpty() {
        return this.items.isEmpty();
    }
    /**
     * Возвращает элемент коллекции предикатов по заданному индексую
     * @param index индекс искомого элемента
     * @return элемент типа <code>RowEvaluator</code>.
     * @see tdo.expr.RowEvaluator
     */
    public RowEvaluator get(int index) {
        return (RowEvaluator)this.items.elementAt(index);
    }
    /**
     * Возвращает количество элементов контейнера.
     * @return количество элементов контейнера
     */
    public int getCount() {
        return this.items.size();
    }
    /**
     * Для заданного ряда оценивает все предикаты в коллекции и возвращает
     * итоговое значение. <p>
     * @param row оцениваемый ряд
     * @return здесь <code>false</code>. Классы-наследники должны переопределять
     *  метод, предоставляя реальный код оценки.
     * @see tdo.expr.RowEvaluator
     * @see tdo.DataRow
     * @see #evaluate(tdo.DataRow[])
     */
    @Override
    public boolean evaluate(DataRow row ) {
        return false;
    }
    /**
     * Для заданного массива рядов оценивает все предикаты в коллекции и возвращает
     * итоговое значение. <p>
     * @param rows оцениваемый массив рядов
     * @return здесь <code>false</code>. Классы-наследники должны переопределять
     *  метод, предоставляя реальный код оценки.
     * @see tdo.expr.RowEvaluator
     * @see tdo.DataRow
     * @see #evaluate(tdo.DataRow)
     */
    public boolean evaluate(DataRow[] rows ) {
        return false;
    }
    
}//class EvaluatorContainer
