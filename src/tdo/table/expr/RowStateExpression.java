/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tdo.table.expr;

import tdo.DataRow;
import tdo.NamedValues;
import tdo.RowState;
import tdo.expr.*;
import tdo.tools.scanner.ExpressionException;
/**
 * ������������ ���������� �������� ������� <code>RowState</code>.<p>
 * ��������� �������: <br>
 * <b>RowState( [</b><i>alias</i> <b>])</b>
 * <p>���������� �������� �������� <code>editingState</code> �������� ����
 *  ��� ��������� ������ ������� ��� ������ �� ���������, ���� ������ ��������.
 *
 * <p>�������������� �������� ���������� ��������� �������� ������ �������.
 *
 * @see tdo.RowState#getEditingState() 
 */
public class RowStateExpression extends FunctionExpression {

    public RowStateExpression(ExpressionContext context, IOperator operator, IOperand op1) {
        super(context, operator, op1);
    }

    @Override
    public Object getValue(NamedValues row) {
        return ((DataRow) row).getState().toString(((DataRow) row).getState().getEditingState());
    }

    @Override
    public Object getValue(NamedValues[] rows) {
        IOperand op = getOp1();
        String alias = null;
        if (op != null) {
            if (!(op instanceof LiteralOperand)) {
                String msg = "RowState function. must have parameter of String type";
                this.getContext().addError(ExpressionContext.FUNCTIONEXPRESSION, this, msg);
                throw new ExpressionException(msg);
            }
            alias = (String) op.getValue(rows);
        }
        NamedValues row = getContext().getNamedValues(rows, alias);
        RowState rs = ((DataRow) row).getState();
        return rs.toString(rs.getEditingState());

    }

    @Override
    public Class getType() {
        return String.class;
    }
}//class RowStateExpression
    
