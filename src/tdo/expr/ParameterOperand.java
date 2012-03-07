/*
 * ParameterOperand.java
 */

package tdo.expr;

import tdo.NamedValues;
/**
 * ������������ ���������������� ���������, �������������� � ����������
 * ��� ���������. <p>
 * � ���������� ������������� ����� ������: <br>
 * <b><i>:���</i></b>
 * <p>�������������� ���������� � �������  ������������� ���������� ���������
 * � {@link tdo.DefaultExpressionContext}.
 *
 */
public class ParameterOperand extends NamedOperand{
    /**
     * ������� ��������� ������ �� ��������� ����� ��������� ������
     * <code>null</code>.
     */
    public ParameterOperand() {
        this(null);
    }
    /**
     * ������� ��������� ������ � �������� �������� ����� ���������.
     * @param paramName ��� ���������, ��� ��������������� �������
     * ���������.
     */
    public ParameterOperand(String paramName) {
        super(paramName);
    }
    /**
     * ���������� �������� ���������.
     * ���������� ���������� ������ {@link #getValue(NamedValues)} �� ���������
     * ��������� ������ <code>null</code>.
     * @return �������� ���������
     */
    @Override
    public Object getValue() {
        NamedValues v = null;
        return this.getValue(v);
    }
    /**
     * ���������� �������� ��������������� ��������� ��� �������� ���������
     * ����������� ��������.<p>
     * ���������� ���������� ������ ��������� ���������
     * {@link tdo.expr.ExpressionContext#getParameterValue(tdo.NamedValues, java.lang.String) }
     * @param values ��������� ����������� ��������, ��������, ������
     *   ���� <code>tdo.DataRow</code>
     * @return �������� ���������
     * @see tdo.DataRow
     * @see #getValue(tdo.NamedValues[])
     */
    @Override
    public Object getValue(NamedValues values) {
        return context.getParameterValue(values, getName());
    }
    /**
     * ���������� �������� ��������������� ��������� ��� ��������� �������
     * ��������� ����������� ��������.<p>
     * ���������� ���������� ������ ��������� ���������
     * {@link tdo.expr.ExpressionContext#getParameterValue(tdo.NamedValues, java.lang.String) }
     * @param values ������ ��������� ����������� ��������, ��������, ������ ��������
     *   ���� <code>tdo.DataRow</code>
     * @return �������� ���������
     * @see tdo.DataRow
     * @see #getValue(tdo.NamedValues)
     */
    @Override
    public Object getValue(NamedValues[] values) {
        return context.getParameterValue(values, getName());
    }
    /**
     * ���������� ����� ��������, ��������������� ���������.
     * ����� �������� ����� �������� ������� ������
     * <code>tdo.expr.ExpressionContext.getParameterType</code>,
     * ��������� ��� ��� ���������. ���� ����� ���������
     * ������ �������� <code>null</code>, �� ������������
     * <code>ParameterOperand.class</code>, ����� �������� ��� ���������,
     * ����������� �������� ���� <code>tdo.DefaultExpressionContext</code>,
     * ������������ � ���� ������������.
     * @return <code>ParameterOperand.class</code> ��� ����� ��������,
     *   ��������������� ���������.
     * @see tdo.expr.ExpressionContext#getParameterType(java.lang.String)
     * @see tdo.DefaultExpressionContext
     */
    @Override
    public Class getType() {
       Class c = context.getParameterType(getName());
       return c == null ? ParameterOperand.class : c;
    }
    /**
     * ���������� ������ ������, �������������� ��������.
     * @return �������� ":" + <code>getName()</code>
     */
    @Override
    public String toString() {
        return ":" + getName();
    }
    /**
     * @return �������� -1, ��������� �������� ������� ��� ������ ����,
     *     ���������� ��� ����������, �� �� ��� ���������
     */
    @Override
    public int getPriority() {
        return -1;
    }

}//class ParameterOperand
