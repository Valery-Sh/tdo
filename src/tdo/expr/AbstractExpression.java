/*
 * CompoundOperand.java
 *
 */

package tdo.expr;

import tdo.NamedValues;
import tdo.tools.expr.LexConst;

/**
 * ������� ����� ��� �������, ���������� ������� ��������� ���������,
 * ���������������� ���������� ���������.<p>
 * ��������, ������ ������ {@link EqualsOperator} ������� ��������� 
 * {@link EqualsExpresion}. <p>
 * �����, ����� ����, ��� ��������� ������ ���������� <code>IExpression</code>,
 * ������ ����� �������� � ������ ������� � ���, ����� ���:
 * <ul>
 *   <li>{@link #getOperator() } ���������� ������ ���� <code>IOperator</code></li>
 *   <li>{@link #getOp1() } ���������� ������ ���� <code>IOperand</code>,
 *            ��������������� ������� �������� ���������</li>
 *   <li>{@link #getOp2() } ���������� ������ ���� <code>IOperand</code>,
 *            ��������������� ������� �������� ���������</li>
 * </ul>
 * ������-���������� ������������ ���������� ������� <code>getValue</code>.
 * ��� ������ ���������� ��������� ���������� ���������.<p>
 * �� ����������� �������-���������, ����� ��� <code>LiteralOperand,
 * NamedOperand, IdentifierOperand, ParameterOperand</code>, ��� ���������
 * ��������� �� ������ <code>AbstractExpression</code>.
 * 
 */
public  abstract class AbstractExpression implements IExpression{

    /**
     * ����� ������ ���� {@link IOperator} ������� ��������������� ��� 
     * ������ ���� <code>IExpression</code>, ��������� ����� 
     * <code>createExpression</code>, �� �������� ������� ���� ���������������
     * ������ �������������� ���� <i>���������</i>, ��� ���������� � 
     * {@link tdo.tools.expr.LexConst}.
     */
    public int lexType; // for test and print
    
    protected IOperator operator;
    protected IOperand op1;
    protected IOperand op2;
    
    private ExpressionContext context;

/*    public AbstractExpression() {
    }
 */
    /**
     * ������������� ��������, � ������� �������������� ���������.
     * @param context ������ �� �������� ���������
     * @see #getContext()
     * @see tdo.expr.ExpressionContext
     */
    @Override
    public void setContext(ExpressionContext context) {
        this.context = context;
    }
    /**
     * ���������� ��������, � ������� �������������� ���������.
     * @return context ������ �� �������� ���������
     * @see tdo.expr.ExpressionContext
     * @see #setContext(tdo.expr.ExpressionContext)
     */
    public ExpressionContext getContext() {
        return this.context;
    }    


    protected void setLexType() {
        lexType = -2;
    }

    /**
     * ���������� �������� ��������� ��� �������� ��������� ������������
     * ���������.
     * ����� ����������� ���������� ��� ������� ��� ����������. ������-����������
     * ������ �������������� ��� ��� ���������� �������� ��������.
     * @param row ��������� ����������� ���������, �������� ����
     *     <code>tdo.DataRoe</code>
     * @return �������� ���������
     */
    @Override
    public Object getValue(NamedValues row) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    /**
     * ���������� �������� ���������.
     * ���������� ���������� ������ {@link #getValue(NamedValues)} �� ���������
     * ��������� ������ <code>null</code>.
     * @return �������� ���������
     */
    @Override
    public Object getValue() {
        NamedValues row = null;
        return this.getValue(row);
    }
    /**
     * ���������� �������� ���������.
     * @return ������ ��������� ���� {@link tdo.expr.IOperator}, ���������� ���������
     */
    public IOperator getOperator() {
        return this.operator;
    }
    /**
     * ���������� ������ ����������, ��������������� ������� ��������.
     * @return ������ ���� {@link tdo.expr.IOperand}
     * @see #getOp2()
     */
    public IOperand getOp1() {
        return this.op1;
    }
    /**
     * ���������� ������ ����������, ��������������� ������� ��������.
     * ��������� ��������� �������� <i>��������</i> �, �������, ��� ��� ������
     * ������� �� ������������
     * @return ������ ���� {@link tdo.expr.IOperand}
     * @see #getOp1()
     * @see tdo.expr.IOperator#isUnary()
     */
    public IOperand getOp2() {
        return this.op2;
    }
    /**
     *
     * @return ������ -1.
     */
    @Override
    public int getPriority() {
        return -1;
    }
    
    @Override
    public String toString() {
        String s = "";
        String s1 = op1 == null ? "" : op1.toString();
        String s2 = op2 == null ? "" : op2.toString();
        if ( operator.isUnary() )
            s = "(" + operator.toString() + " " + s1 + ")";
        else
            s = "(" + s1 + " " + operator.toString() + " " + s2 + ")";
        return s;    
    }
    /**
     * �����, ������������� <code>NOT</code> �������� ��� �������� ��������.
     * ����� <code>getType</code> ���������� <code>Boolean.class</code>.
     */
    public static class NotExpression extends AbstractExpression{
        
        private IOperand op;
        /**
         * ������� ��������� ������ ��� ��������� ��������.
         * ��������� ������� �������� �������, �� ������ �������� ����������.
         * @param op �������, � �������� ����������� �������� <code>NOT</code>
         * @see tdo.expr.AbstractOperator.NotOperator
         */
        public NotExpression(IOperand op) {
            this.op = op;
        }
        /**
         * ���������� �������� ��������� ��� �������� ����������� ���������.
         * ����� �������� �������� �������� ��� <code>boolean</code> � ���������
         * � ���� ���������� java-�������� <code><b>!</b></code>.
         * 
         * @param row ��������� ���������, ������ � ������� ������������ ��
         *   �����, ��������, <code>tdo.DataRow</code>
         * @return �������� ���� <code>Boolean</code>
         * @see #getValue(tdo.NamedValues[])
         * @see tdo.expr.AbstractExpression#getValue()
         */
        @Override
        public Object getValue(NamedValues row) {
            boolean b = ((Boolean)op.getValue(row)).booleanValue();
            return new Boolean( !b ); 
        }
        /**
         * ���������� �������� ��������� ��� ��������� ������� ����������� ���������.
         *
         * @param row ������ ��������� ���������, ������ � ������� ������������ ��
         *   �����, ��������, <code>tdo.DataRow[]</code>
         * @return �������� ���� <code>Boolean</code>
         * @see #getValue(tdo.NamedValues)
         * @see tdo.expr.AbstractExpression#getValue()
         */
        @Override
        public Object getValue(NamedValues[] row) {
            boolean b = ((Boolean)op.getValue(row)).booleanValue();
            return new Boolean( !b ); 
        }
        /**
         * 
         * @return
         */
        public IOperand getOp() {
            return op;
        }

        @Override
        public Class getType() {
            return Boolean.class;
        }        
        @Override
        protected void setLexType() {
            lexType = LexConst.NOT;
        }
        @Override
        public String toString() {
           return "NOT (" +  op.toString() + ")";
        }
        
    }//class NotExpression
    
}//class AbstractExpression
