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
 * ������������ ���������� �������� ������� <code>IsDeleted</code>.<p>
 * ��������� �������: <br>
 * <b>IsDeleted( [</b><i>alias</i> <b>])</b>
 * <p>�������� ���: <b><code>IsDel</code></b>
 * <p>���������� �������� <code>true</code> ���� ������� ��� ������
 *  ��� ��������� ������ ������� ��� ������ �� ���������, ���� ������ ��������.
 *  ���� ��� �� ������ ���������� <code>false</code>.
 *
 * <p>�������������� �������� ���������� ��������� �������� ������ �������.
 *
 */
public class IsDeletedExpression extends FunctionExpression {

    public IsDeletedExpression(ExpressionContext context, IOperator operator, IOperand op1) {
        super(context, operator, op1);
    }

    @Override
    public Object getValue(NamedValues row) {
        Boolean result;//isEmpty() = null;
        RowState rs = ((DataRow) row).getState();
        if (rs.isDeleted()) {
            result = true;
        } else {
            result = false;
        }
        return result;

    }

    @Override
    public Object getValue(NamedValues[] rows) {
        IOperand op = getOp1();

        String alias = null;
        if (op != null) {
            if (!(op instanceof LiteralOperand)) {
                String msg = "IsDeleted function. must have parameter of String type";
                this.getContext().addError(ExpressionContext.FUNCTIONEXPRESSION, this, msg);
                throw new ExpressionException(msg);
                
            }
            alias = (String) op.getValue(rows);
        }
        NamedValues row = getContext().getNamedValues(rows, alias);
        RowState rs = ((DataRow) row).getState();
        if (rs.isDeleted()) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public Class getType() {
        return Boolean.class;
    }
} //class IIFExpression
