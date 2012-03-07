/*
 * Validator.java
 *
 */

package tdo.impl;


import tdo.DataRow;
import tdo.expr.ExpressionContext;

/**
 * Определяет функциональность классов, предназначенных для выполнения
 * валидации ряда или колонки таблицы.
 * <p>
 * Метод с сигнатурой validate(DataRow,Object) делегирует иcполнение
 * перегруженному методу validate(DataRow).
 */
public interface Validator {
    /**
     * Возвращает контекст выражения, если оно используется как условие
     * валидности ряда или колонки.
     * @return контекст выражения или <code>null</code>
     */
    ExpressionContext getExpressionContext();
    /**
     * Выполняет валидацию ряда.
     *
     * @param row проверяемый на валидность ряд
     * @return <code>true</code> если данные ряда успешно проходят контороль.
     *   <code>false</code> в противном случае
     * @see #validate(tdo.DataRow, java.lang.Object)
     */
    boolean validate(DataRow row);
    /**
     * Выполняет валидацию ряда для заданного значения.
     * <p>Обычно этот метод используется для валидаторов колонки.
     * @param row проверяемый на валидность ряд
     * @param value новое значение колонки, для которого проводится валидация
     * @return <code>true</code> если данные ряда успешно проходят контороль.
     *   <code>false</code> в противном случае
     * @see #validate(tdo.DataRow)
     */
    boolean validate(DataRow row, Object value);
    /**
     * Возвращает строку знаков, содержимое которой описывает ошибку и ее причину
     * @return информация об ошибке
     */
    String getMessage();
    /**
     * Устанавливает значение строки знаков, содержимое которой описывает
     * ошибку и ее причину.
     * @param message новое содержимое сообщения
     */
    void setMessage(String message);
}//interface Validator
