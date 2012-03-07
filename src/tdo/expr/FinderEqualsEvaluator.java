/*
 * FinderEqualsEvaluator.java
 * 
 * Created on 12.06.2007, 23:00:16
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.expr;

import tdo.DataRow;
import tdo.Table;

/**
 *
 * @author Valera
 */
public class FinderEqualsEvaluator extends StartsWithEvaluator {

    public FinderEqualsEvaluator() {
    }
    public FinderEqualsEvaluator(Table table, String columnName, Object value) {
        super(table, columnName, value);
    }

    /**
     * Оценивает содержимое заданного ряда, используя назначенные значения
     * свойств <code>columnName, value, caseSensitive</code>.
     * <p>Имя колонки и значение назначаются конструктором класса. Кроме того,
     * значения свойств <code>caseSensitive, value</code> могут быть установлены
     * с использованием setter-методов.
     * <p>Для заданного ряда извлекается значение колонки по ее имени.
     * <p>Если значения колонки и <code>value</code> оба равны <code>null</code>,
     * то возвращается <code>true</code>.
     * <p>Если только значения колонки или только  <code>value</code> равен
     * <code>null</code>, то возвращается <code>false</code>.
     *
     * <p>Если значение колонки и <code>value</code> не равны <code>null</code>, то
     * к полученному значению, как строке знаков применяется метод
     * <code>java.lang.String#equals(value)</code> и возвращенное значение
     * рассматривается как результат метода.
     *
     * <p>Если значение свойства <code>caseSensitive</code> равно <code>false</code>,
     * то и значение колонки и значение <code>value</code> преобразуется в
     * верхний регистр перед применением метода <code>equals</code>.
     *
     * @param row оцениваемый ряд
     * @return результат оценки
     */

    @Override
    public boolean evaluate(DataRow row) {
        Object columnValue;
        columnValue = row.getValue(columnName);    
        
        if ( value == null && columnValue == null ) {
            return true;
        }
        if ( value == null || columnValue == null ) {
            return false;
        }
        String str = value.toString();
        String astr = columnValue.toString();
        
        if ( ! this.caseSensitive ) {
            str = str.toUpperCase();
            astr = astr.toUpperCase();
        } 
        if ( astr.equals(str) )
            return true;
        return false;

    }
    
}//class FinderEqualsEvaluator
