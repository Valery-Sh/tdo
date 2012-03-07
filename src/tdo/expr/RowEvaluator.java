/*
 * RowEvaluator.java
 *
 */

package tdo.expr;

import tdo.DataRow;


/**
 * Определяет функциональность, позволяющую оценить объект {@link tdo.DataRow}.
 * Предназначен для использованиях в фильтрах таблицы.
 */
public interface RowEvaluator {
    /**
     * Оценивает ряд таблицы и возвращает результат, как булевое значение.
     * @param row оцениваемый ряд
     * @return true или false
     */
    boolean evaluate(DataRow row);    
      
}//interface RowEvaluator
