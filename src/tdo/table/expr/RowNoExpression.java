/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tdo.table.expr;

import tdo.DefaultExpressionContext;
import tdo.NamedValues;
import tdo.expr.*;
import tdo.tools.scanner.ExpressionException;
/**
 * Обеспечивает вычисление значения функции <code>RowNo</code>.<p>
 * Синтаксис функции: <br>
 * <b>RowNo( [</b><i>alias</i> <b>])</b>
 * <p>Возвращает индекс текущего ряда для заданного алиаса таблицы или
 *  алиаса по умолчанию, если опущен параметр.
 * <p>Необязательный параметр определяет строковое значение алиаса таблицы.
 */
public class RowNoExpression extends FunctionExpression {

    public RowNoExpression(ExpressionContext context, IOperator operator, IOperand op1) {
        super(context, operator, op1);
    }

    @Override
    public Object getValue(NamedValues row) {
        return ((DefaultExpressionContext)getContext()).getRowIndex(row);
    }

    @Override
    public Object getValue(NamedValues[] rows) {
        IOperand op = getOp1();
        String alias = null;
        if (op != null) {
            if (!(op instanceof LiteralOperand)) {
                String msg = "RowNo function. must have parameter of String type";
                this.getContext().addError(ExpressionContext.FUNCTIONEXPRESSION, this, msg);
                throw new ExpressionException(msg);
            }
            alias = (String) op.getValue(rows);
        }
        NamedValues r = getContext().getNamedValues(rows, alias);
        return ((DefaultExpressionContext)getContext()).getRowIndex(r, alias);
    }

    @Override
    public Class getType() {
        return Integer.class;
    }
} //class RowNoExpression
