/*
 * ErrorExpression.java
 * 
 */

package tdo.expr;

import tdo.NamedValues;
import tdo.tools.scanner.ExpressionException;

/**
 * �����������, ����� �������� ��������� �������� ������, �� �����������
 * �������������� �������� �� ����������.
 * ��������� ��� �� ������� ���������, �������������� ��� ���������.
 *
 */
public class ErrorExpression extends AbstractExpression {
    
    protected ExpressionException exprException;
    
    public ErrorExpression() {
    }
    /**
     * ������������ � <code>tdo.DefaultExpressionContext</code> ��� �����������
     * ������ � ���������, ����� ������ ���������� �� ����������� ����� ��������
     * ����� ��������� ���������.<p>
     * �� ���� �����, ��� ������� ����� ���������� ��������, ��������� ������
     * � ��������.
     * @param context �������� ���������
     * @param oper ��������, ��� ��������� �������� ���������� ������.
     * @param op1 ������ �������
     * @param op2 ������ �������
     */
    public ErrorExpression(ExpressionContext context, IOperator oper, IOperand op1, IOperand op2) {
        this.operator = oper;
        this.op1 = op1;
        this.op2 = op2;
        this.exprException = null;
        this.setContext(context);
    }
    /**
    /**
     * ������������ � <code>tdo.DefaultExpressionContext</code> ��� �����������
     * ������ � ���������, ����� ������ ���������� ��� �������� ���������
     * ���������.<p>
     * ���� ������ ���������� ��������, �� ��������� ����������� ��������������
     * ��������, ������� ��������������� � ����������� � ������ ������ ���������.
     * ������ <code>getOperator, getOp1 � getOp2</code> � ������ ������
     * ���������� <code>null</code>. ������� ������ �������� �������
     * {@link #getExprException() }
     * @param context �������� ���������
     * @param exception ����������� �������� ����������
     */
    public ErrorExpression(ExpressionContext context, ExpressionException exception ) {
        this.operator = null;
        this.op1 = null;
        this.op2 = null;
        this.exprException = exception;       
        this.setContext(context);
    }
    /**
     * @return ���������� ������ ����������, ���������� ��� �������� ���������
     *  ���������.
     */
    public ExpressionException getExprException() {
        return this.exprException;
    }
    /**
     * ��� ������� ������� ����� ��������� �������������� ��������.
     * @param row
     * @return
     * @throws java.lang.UnsupportedOperationException
     */
    @Override
    public Object getValue(NamedValues[] row) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return {@link tdo.expr.ErrorExpression#getClass() }
     */
    @Override
    public Class getType() {
        return ErrorExpression.class;
    }
    /**
     * ��������� ��������� ���������� ������ �����������, ���� �������� ��������
     * <code>exprException</code> ����� <code>null</code>. � ��������� ������,
     * ���������� �������� ��������� ���������.
     * @return ������ ��� ��������� ���������, ����������� ������.
     * @see #getExprException()
     */
    @Override
    public String toString() {
        if ( this.exprException == null )
            return super.toString();
        String es = this.getContext().getExpressionString();
        return es;
    }
}//class ErrorExpression
