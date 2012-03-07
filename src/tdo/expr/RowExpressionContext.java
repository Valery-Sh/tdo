/*
 * RowExpressionContext.java
 * 
 */

package tdo.expr;


import tdo.DataRow;
import tdo.DefaultExpressionContext;

/**
 * Расширяет функциональность класса {@link tdo.DefaultExpressionContext},
 * реализуя интерфейс {@link tdo.expr.RowEvaluator}.
 * <p>Используется, как правило, в выражениях-фильтрах таблиц {@link tdo.Table}
 * и валидаторах.
 *
 */
public class RowExpressionContext extends DefaultExpressionContext implements RowEvaluator{

    private IExpression expression;
    /**
     * Создает контекст выражения для заданного строкового выражения.
     * @param expr исходное строковое выражение
     */
    public RowExpressionContext(String expr) {
        super(expr);
    }

    /**
     * Оценивает сожержимое заданного ряда таблицы и результат оценки возвращает
     * как булевое значение.<p>
     * Создает, если еще не создан, объект типа {@link tdo.expr.IExpression}
     * и выполняет для него метод {@link tdo.expr.IExpression#getValue(tdo.NamedValues)  }
     *
     * @param row оцениваемый ряд
     * @return булевыое значение, как результат оценки ряда
     * @see #evaluate(tdo.DataRow[])
     * @see tdo.Table
     * @see tdo.impl.Filterer
     */
    @Override
    public boolean evaluate(DataRow row) {
        if ( this.expression == null )
            expression = this.createExpression();
        return ((Boolean)expression.getValue(row)).booleanValue();
    }
    /**
     * Оценивает сожержимое заданного массива рядов таблицы и результат оценки возвращает
     * как булевое значение.<p>
     * Создает, если еще не создан, объект типа {@link tdo.expr.IExpression}
     * и выполняет для него метод {@link tdo.expr.IExpression#getValue(tdo.NamedValues[])  }
     *
     * @param rows массив оцениваемых рядов
     * @return булевыое значение, как результат оценки ряда
     * @see #evaluate(tdo.DataRow)
     * @see tdo.Table
     * @see tdo.impl.Joiner
     */
    public boolean evaluate(DataRow[] rows) {
        if ( this.expression == null )
            expression = this.createExpression();
        return ((Boolean)expression.getValue(rows)).booleanValue();
    }

}
