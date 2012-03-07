/*
 * StartsWithEvaluator.java
 * 
 */
package tdo.expr;

import tdo.DataRow;
import tdo.Table;

/**
 *
 */
public class StartsWithEvaluator implements RowEvaluator {

    protected Table table;

    protected String columnName;
    protected int columnIndex;
    protected Object value;
    protected boolean caseSensitive;

    protected StartsWithEvaluator() {
    }

    public StartsWithEvaluator(Table table, String columnName, Object value) {
        this.table = table;
        this.columnName = columnName;
        this.value = value;
        this.columnIndex = -1;
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
     * <code>java.lang.String#startsWith(value)</code> и возвращенное значение
     * рассматривается как результат метода.
     *
     * <p>Если значение свойства <code>caseSensitive</code> равно <code>false</code>,
     * то и значение колонки и значение <code>value</code> преобразуется в
     * верхний регистр перед применением метода <code>startsWith</code>.
     *
     * @param row оцениваемый ряд
     * @return результат оценки
     */
    @Override
    public boolean evaluate(DataRow row) {
        Object columnValue;


        if (columnIndex < 0) {
            columnIndex = table.getColumns().find(columnName);
        }
        columnValue = row.getValue(columnIndex);

        if (value == null && columnValue == null) {
            return true;
        }
        if (value == null || columnValue == null) {
            return false;
        }
        String str = value.toString();
        String astr = columnValue.toString();

        if (!this.caseSensitive) {
            str = str.toUpperCase();
            astr = astr.toUpperCase();

        }
        if (astr.startsWith(str)) {
            return true;
        }
        return false;

    }
    /**
     * Устанавливает значение, используемое для сравнения со значением колонки
     * ряда.
     * @param value устанавливаемое значение
     */
    public void setValue(Object value) {
        this.value = value;
    }

    /**
     * Устанавливает указатель, является ли операция оценки чувствительной к
     * регистру.<p>
     * Если пзначение параметра равно <code>true</code>, то операция
     * чувствительна к регистру и не следует преобразовывать строковый значения,
     * участвующие в ней. false - в противном случае.
     *
     * @param caseSensitive
     */
    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }
}//class

