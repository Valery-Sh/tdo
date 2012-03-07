/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tdo.table.expr;

import tdo.DataRow;
import tdo.NamedValues;
import tdo.expr.ExpressionContext;
import tdo.expr.FunctionExpression;
import tdo.expr.IOperand;
import tdo.expr.IOperator;
import tdo.expr.LiteralOperand;
import tdo.tools.scanner.ExpressionException;
/**
 * Обеспечивает вычисление значения функции <code>DEPTH</code>.<p>
 * Синтаксис функции: <br>
 * <b>Depth( [</b><i>alias</i> <b>])</b>
 * <p>Возвращает значение {@link tdo.RowState#getDepth() } для текущего ряда и
 * заданного алиаса таблицы или алиаса по умолчанию, если опущен параметр.
 *
 * <p>Необязательный параметр определяет строковое значение алиаса таблицы.
 */
public class DepthExpression extends FunctionExpression {

    public DepthExpression(ExpressionContext context, IOperator operator, IOperand op1) {
        super(context, operator, op1);
    }

    @Override
    public Object getValue(NamedValues row) {
        return new Integer(((DataRow) row).getState().getDepth());

    }

    @Override
    public Object getValue(NamedValues[] rows) {
        IOperand op = getOp1();

        String alias = null;
        if (op != null) {
            if (!(op instanceof LiteralOperand)) {
                String msg = "DepthExpression.getValue(DataRow[]) : function depth must have String parameter";
                this.getContext().addError(ExpressionContext.FUNCTIONEXPRESSION, this, msg);
                throw new ExpressionException(msg);
            }
            alias = (String) op.getValue(rows);
        }
        NamedValues row = getContext().getNamedValues(rows, alias);
        return new Integer(((DataRow) row).getState().getDepth());
    }

    @Override
    public Class getType() {
        return Integer.class;
    }
} //class DepthExpression
