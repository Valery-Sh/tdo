package tdo.table.expr;

import tdo.DefaultExpressionContext;
import tdo.expr.CompoundExpression.CommaListExpression;
import tdo.expr.*;
import tdo.tools.scanner.ExpressionException;
/**
 * Обеспечивает вычисление значения функции <code>RowValue</code>.<p>
 * Синтаксис функции: <br>
 * RowValue( <i>row-index</i>, <i>column-name</i> )
 * <p><i>row-index</i> первый операнд - выражение, задающее индек ряда таблицы;
 * <p><i>column-name</i> второй операнд - выражение, задающее имя колонки таблицы
 * <p>Возвращает значение ячейки ряда для заданного индекса таблицы и заданного
 *    имени колонки.
 */
public class RowValueExpression extends FunctionExpression {

    public RowValueExpression(ExpressionContext context, IOperator operator, IOperand op1) {
        super(context, operator, op1);
    }

    @Override
    protected Object computeFunction(IOperator oper, Object values) {

        if (!(values instanceof ValueList)) {
            String msg = "RowValue function accepts two parameters";
            this.getContext().addError(ExpressionContext.FUNCTIONEXPRESSION, this, msg);
            throw new ExpressionException(msg);
        }
        ValueList args = (ValueList) values;

        if (args.size() != 2) {
            String msg = "RowValue function accepts two parameters";
            this.getContext().addError(ExpressionContext.FUNCTIONEXPRESSION, this, msg);
            throw new ExpressionException(msg);
        }
        int rowIndex = -1;
        if (!(args.get(0) instanceof Integer)) {
            try {
                rowIndex = ((Number)args.get(0)).intValue();
            } catch(Exception e) {
                String msg = "RowValueFunction: first parameter must be of Integer type";
                this.getContext().addError(ExpressionContext.FUNCTIONEXPRESSION, this, msg);
                throw new ExpressionException(msg);
             }
        } else {
            rowIndex = ((Integer) args.get(0)).intValue();
        }

        //int rowIndex = ((Integer) args.get(0)).intValue();
        CommaListExpression cle = (CommaListExpression) getOp1();
        IdentifierOperand co = (IdentifierOperand) cle.get(1);
        String columnName = co.getName();
        return ((DefaultExpressionContext) this.getContext()).getRow(rowIndex).getValue(columnName);

    }

    @Override
    public Class getType() {
        CommaListExpression cle = (CommaListExpression) getOp1();
        IdentifierOperand co = (IdentifierOperand) cle.get(1);
        return co.getType();
    }
} //class RowValueExpression
