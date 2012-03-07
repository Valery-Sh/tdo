/**
 * IOperator.java
 * 
 * Created on 14.06.2007, 18:52:06 vvvv
 * 
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package tdo.expr;

/**
 * ������������ ����� ������ ��� ��������� ������� ���������� ���������.<p>
 * ������ ���������� ��������� ������� ������� {@link IExpression} �� ��������
 * ��������� ���������.
 * @see IOperand
 * @see AbstractOperator
 * @see FunctionOperator
 * 
 */
public interface IOperator extends IToken {
    void   setContext(ExpressionContext context);
    /**
     *  ������ ������ <code>IExpression</code> ��� ��������� ��������.
     * ��� ����� �������������� ������ ������ ������������ �������� ������������,
     * �.�. ����������� �������� ������ ���� �������. <p>
     *  ��������� ��������� <code>IExpression</code> �������� ����������� <code>IOperand</code>,
     * �� ����� ��������� ����� ����� ���� ������������ ��� �������.
     * 
     * @param context 
     * @param op1 �������, ������������ ��� ���������� ���������
     * @return ��������� ������������� ���������
     * @see IExpression
     * @see #createExpression(IOperand,IOperand)
     */
    IExpression createExpression(ExpressionContext context,IOperand op1); 
    /**
     *  ������ ������ <code>IExpression</code> ��� �������� ���������.
     * ��� ����� �������������� ������ ������ ������������ ��������� ������������,
     * �.�. ����������� �������� ������ ��� ��������.<p>
     *  ��������� ��������� <code>IExpression</code> �������� ����������� <code>IOperand</code>,
     * �� ����� ��������� ����� ����� ���� ������������ ��� �������.

     * @param context 
     * @param op1 ������ �������, ������������ ��� ���������� ���������
     * @param op2 ������ �������, ������������ ��� ���������� ���������
     * @return ��������� ������������� ���������
     * @see IExpression
     * @see #createExpression(IOperand)
     */
    IExpression createExpression(ExpressionContext context,IOperand op1, IOperand op2);    
    /**
     * 
     * @return �������� <code>true</code>, ���� ����� �������, ������������ ���������
     *      �������� ������� ����������. <code>false</code> - � ��������� ������.
     */
    boolean isUnary();
}
